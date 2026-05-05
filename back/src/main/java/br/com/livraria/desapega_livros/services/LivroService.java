package br.com.livraria.desapega_livros.services;

import br.com.livraria.desapega_livros.controllers.dto.LivroDTO;
import br.com.livraria.desapega_livros.controllers.form.LivroFORM;
import br.com.livraria.desapega_livros.controllers.form.LivroIsbnFORM;
import br.com.livraria.desapega_livros.entities.*;
import br.com.livraria.desapega_livros.entities.enuns.StatusLivro;
import br.com.livraria.desapega_livros.infra.exception.DadoInvalidoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.infra.exception.RequisicaoInvalidaException;
import br.com.livraria.desapega_livros.repositories.CidadeRepository;
import br.com.livraria.desapega_livros.repositories.LivroAutorRepository;
import br.com.livraria.desapega_livros.repositories.LivroCategoriaRepository;
import br.com.livraria.desapega_livros.repositories.LivroRepository;
import br.com.livraria.desapega_livros.services.bases.BaseServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LivroService
		extends BaseServiceImpl<Livro, Integer> {

	private LivroRepository livroRepo;

	private EditoraService editoraService;

	private UsuarioService usuarioService;

	private IdiomaService idiomaService;

	private CidadeRepository cidadeRepo;

	private AutorService autorService;

	private LivroAutorRepository livroAutorRepo;

	private CategoriaService categoriaService;

	private LivroCategoriaRepository livroCatRepo;

	private ISBNService isbnService;

	public LivroService(
			LivroRepository livroRepository,
			EditoraService editoraService,
			UsuarioService usuarioService,
			IdiomaService idiomaService,
			CidadeRepository cidadeRepo,
			AutorService autorService,
			LivroAutorRepository livroAutorRepo,
			CategoriaService categoriaService,
			LivroCategoriaRepository livroCatRepo,
			ISBNService isbnService
	) {
		super(livroRepository);
		this.livroRepo = livroRepository;
		this.editoraService = editoraService;
		this.usuarioService = usuarioService;
		this.idiomaService = idiomaService;
		this.cidadeRepo = cidadeRepo;
		this.autorService = autorService;
		this.livroAutorRepo = livroAutorRepo;
		this.categoriaService = categoriaService;
		this.livroCatRepo = livroCatRepo;
		this.isbnService = isbnService;
	}

	private void validacaoBasica(LivroFORM livroForm, MultipartFile capa) {
		if (this.verificaLivroJaCadastrado(livroForm.titulo(), livroForm.idDono(), livroForm.idIdioma())) {
			throw new RegistroEncontradoException("Livro já cadastrado!");
		}

		int anoAtual = LocalDate.now().getYear();

		if (livroForm.anoPublicacao() > anoAtual) {
			throw new DadoInvalidoException("O ano de publicação do livro não pode ser no futuro!");
		}

		if (capa.isEmpty()) {
			throw new RequisicaoInvalidaException("A capa deve ser passada como arquivo Multipart!");
		}
	}

	@Transactional
	public ResponseEntity<?> cadastrar(LivroFORM livroForm, MultipartFile capa) {

		this.validacaoBasica(livroForm, capa);

		this.validaEntidadesRelacionadas(livroForm);

		List<Autor> autoresSalvos = this.verificaAutoresCadastradosPorId(livroForm.autores());
		List<Categoria> categoriasSalvos = this.verificaCategoriasCadastradasPorId(livroForm.categorias());

		Livro livro = new Livro();

		Editora editora = this.editoraService.findById(livroForm.idEditora());
		livro.setEditora(editora);

		livro.setTitulo(livroForm.titulo());
		livro.setSubtitulo(livroForm.subtitulo());
		livro.setDescricao(livroForm.descricao());
		livro.setAnoPublicacao(livroForm.anoPublicacao());
		livro.setQtdPaginas(livroForm.qtdPaginas());
		livro.setOpniaoDoador(livroForm.opniaoDoador());
		livro.setIsbn(this.isbnService.formataISBN(livroForm.isbn()));

		Usuario dono;
		dono = this.usuarioService.findById(livroForm.idDono());

		Idioma idioma;
		idioma = this.idiomaService.findById(livroForm.idIdioma());

		livro.setDono(dono);
		livro.setIdioma(idioma);

		try {
			livro.setCapa(capa.getBytes());

		} catch (IOException e) {

			throw new RuntimeException(
					"Erro interno ao processar imagem!");
		}

		Cidade cidade;

		if (livroForm.idCidade() == null) {
			cidade = dono.getEndereco().getCidade();

		} else {
			cidade = this.cidadeRepo.findById(livroForm.idCidade()).get();
		}

		livro.setCidade(cidade);
		livro.setStatus(StatusLivro.DISPONIVEL.toString());

		Livro livroSalvo = this.livroRepo.save(livro);
		livroSalvo.setCategorias(categoriasSalvos);
		livroSalvo.setAutores(autoresSalvos);

		this.relacionaAutoresComLivro(autoresSalvos, livroSalvo);
		this.relacionaCategoriasComLivros(categoriasSalvos, livroSalvo);

		var livroDTO = new LivroDTO(livroSalvo);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
		var uri = uriBuilder.path("/livro/{id}").buildAndExpand(livroSalvo.getId()).toUri();

		return ResponseEntity.created(uri).body(livroDTO);

	}

	private void validaEntidadesRelacionadas(LivroFORM livroForm) {

		if (!this.usuarioService.existsById(livroForm.idDono())) {
			throw new RegistroNaoExisteException(
					"Não há usuário cadastrado para o id :" + livroForm.idDono());
		}

		if (!this.idiomaService.existsById(livroForm.idIdioma())) {
			throw new RegistroNaoExisteException(
					"Não há idioma cadastrado para o id : " + livroForm.idIdioma());
		}

		if (!this.editoraService.existsById(livroForm.idEditora())) {
			throw new RegistroNaoExisteException(
					"Não há editora cadastrada para o id : " + livroForm.idEditora());
		}
	}

	@Transactional
	private List<Autor> verificaAutoresCadastradosPorId(List<Integer> idAutores) {
		List<Autor> autores = new ArrayList<>();

		for (int i = 0; i < idAutores.size(); i++) {
			if (!this.autorService.existsById(idAutores.get(i))) {
				throw new RegistroNaoExisteException(
						"Não há autor cadastrado para o id : " + idAutores.get(i));
			}

			autores.add(this.autorService.findById(idAutores.get(i)));
		}
		return autores;
	}

	@Transactional
	private void relacionaAutoresComLivro(List<Autor> autores, Livro livro) {

		try {

			autores.forEach(autor -> {
				var livroAutor = new LivroAutor();
				livroAutor.setAutor(autor);
				livroAutor.setLivro(livro);

				if (!livroAutorRepo.existsLivroAutor(autor, livro)) {

					livroAutorRepo.save(livroAutor);
				}

			});

		} catch (Exception ex) {
			livroRepo.deleteById(livro.getId());
		}
	}

	@Transactional
	private List<Categoria> verificaCategoriasCadastradasPorId(List<Integer> idCategorias) {

		List<Categoria> categorias = new ArrayList<>();

		for (int i = 0; i < idCategorias.size(); i++) {
			if (!this.categoriaService.existsById(idCategorias.get(i))) {
				throw new RegistroNaoExisteException(
						"Não há categoria cadastrada para o id : " + idCategorias.get(i));
			}
			categorias.add(this.categoriaService.findById(idCategorias.get(i)));
		}

		return categorias;
	}

	@Transactional
	private void relacionaCategoriasComLivros(List<Categoria> categorias, Livro livro) {

		try {
			categorias.forEach(categoria -> {
				var livroCategoria = new LivroCategoria();
				livroCategoria.setCategoria(categoria);
				livroCategoria.setLivro(livro);

				if (!livroCatRepo.existsLivroCategoria(livro, categoria)) {

					System.out.println(livroCatRepo.save(livroCategoria));
				}
			});

		} catch (Exception ex) {
			this.categoriaService.simpleDeleteById(livro.getId());
		}

	}

	@Transactional
	public ResponseEntity<?> cadastraPorISBN(LivroIsbnFORM livroForm, MultipartFile capa) {

		if (!idiomaService.existsById(livroForm.idIdioma())) {
			throw new RequisicaoInvalidaException(
					"Não existe idioma cadastrad para o id:" + livroForm.idIdioma());
		}

		if (!this.usuarioService.existsById(livroForm.idDono())) {
			throw new RequisicaoInvalidaException(
					"Não existe usuario cadastrado para o id: " + livroForm.idDono());
		}

		var dadosLivro = this.isbnService.buscaISBN(livroForm.isbn());

		if (this.verificaLivroJaCadastrado(dadosLivro.titulo(), livroForm.idDono(), livroForm.idIdioma())) {
			throw new RegistroEncontradoException("Livro já cadastrado!");
		}

		Usuario dono = this.usuarioService.findById(livroForm.idDono());
		Idioma idioma = this.idiomaService.findById(livroForm.idIdioma());

		Livro livro = new Livro(dadosLivro);

		livro.setDono(dono);
		livro.setIdioma(idioma);

		Cidade cidade;

		if (livroForm.idCidade() == null) {
			cidade = dono.getEndereco().getCidade();
		} else {
			cidade = this.cidadeRepo.findById(livroForm.idCidade()).get();
		}

		livro.setCidade(cidade);

		livro.setOpniaoDoador(livroForm.opniaoDoador());
		livro.setEditora(this.verificaEditoraCadastradaPorNome(dadosLivro.editora()));
		livro.setStatus(StatusLivro.DISPONIVEL.toString());

		try {
			livro.setCapa(capa.getBytes());

		} catch (IOException e) {
			throw new RuntimeException(
					"Erro interno ao processar imagem!");
		}

		Livro livroSalvo = this.livroRepo.save(livro);

		List<Autor> autores = this.verificaAutoresCadastradosPorListNome(dadosLivro.nomesAutores());
		List<Categoria> categorias = this.verificaCategoriasCadastradasPorListNomes(dadosLivro.nomesCategorias());

		this.relacionaAutoresComLivro(autores, livroSalvo);
		this.relacionaCategoriasComLivros(categorias, livroSalvo);

		livro.setAutores(autores);
		livro.setCategorias(categorias);

		LivroDTO livroSalvoDTO = new LivroDTO(livroSalvo);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
		var uri = uriBuilder.path("/livro/{id}").buildAndExpand(livroSalvo.getId()).toUri();

		return ResponseEntity.created(uri).body(livroSalvoDTO);
	}

	private boolean verificaLivroJaCadastrado(String titulo, Integer idDono, Integer ididioma) {
		return livroRepo.livroJaCadastrado(titulo, idDono, ididioma);
	}

	private Editora verificaEditoraCadastradaPorNome(String nome) {
		Editora ed = this.editoraService.findByNome(nome);

		if (ed == null) {
			ed = this.editoraService.simpleSave(new Editora(null, nome));
		}
		return ed;

	}

	private List<Autor> verificaAutoresCadastradosPorListNome(List<String> nomesAutores) {

		List<Autor> autores = new ArrayList<Autor>();

		nomesAutores.forEach(nome -> {
			Autor autor = this.autorService.findByNome(nome);

			if (autor == null) {
				autor = this.autorService.simpleSave(new Autor(null, nome));
			}
			autores.add(autor);
		});

		return autores;
	}

	private List<Categoria> verificaCategoriasCadastradasPorListNomes(List<String> nomesCategorias) {
		List<Categoria> categorias = new ArrayList<Categoria>();

		nomesCategorias.forEach(nome -> {
			Categoria categoria = this.categoriaService.findByNome(nome);

			if (categoria == null) {
				categoria = this.categoriaService.simpleSave(new Categoria(null, nome));
			}

			categorias.add(categoria);
		});
		return categorias;
	}

	@Transactional
	public ResponseEntity<?> remover(Integer id) {
		if (!livroRepo.existsById(id)) {
			throw new RegistroNaoExisteException(
					"Não existe livro cadastrado para o id: " + id);
		}

		Livro livro = livroRepo.findById(id).get();
		livro.setStatus(StatusLivro.REMOVIDO.toString());

		return ResponseEntity.ok(new LivroDTO(livro));
	}

	@Transactional
	public ResponseEntity<?> atualizar(Integer id, LivroFORM livroForm, MultipartFile capa) {

		if (!livroRepo.existsById(id)) {
			throw new RequisicaoInvalidaException(
					"Não existe livro cadastrado para o id:" + id);
		}

		if (capa.isEmpty()) {
			throw new RequisicaoInvalidaException(
					"A capa deve ser passada como arquivo Multipart!");
		}

		this.validaEntidadesRelacionadas(livroForm);

		Livro livro = this.livroRepo.findById(id).get();

		Usuario dono = this.usuarioService.findById(livroForm.idDono());
		Idioma idioma = this.idiomaService.findById(livroForm.idIdioma());
		Editora editora = this.editoraService.findById(livroForm.idEditora());

		livro.setDono(dono);
		livro.setIdioma(idioma);
		livro.setEditora(editora);

		try {
			livro.setCapa(capa.getBytes());

		} catch (IOException e) {
			throw new RuntimeException("Erro interno ao processar imagem!");
		}

		livro.setTitulo(livroForm.titulo());
		livro.setSubtitulo(livroForm.subtitulo());
		livro.setDescricao(livroForm.descricao());
		livro.setAnoPublicacao(livroForm.anoPublicacao());
		livro.setQtdPaginas(livroForm.qtdPaginas());
		livro.setOpniaoDoador(livroForm.opniaoDoador());
		livro.setIsbn(isbnService.formataISBN(livroForm.isbn()));

		if(!Objects.isNull(livroForm.status())){
			livro.setStatus(livroForm.status());
		}

		Cidade cidade;

		if (livroForm.idCidade() == null) {
			cidade = dono.getEndereco().getCidade();
		} else {
			cidade = cidadeRepo.findById(livroForm.idCidade()).get();
		}

		livro.setCidade(cidade);

		this.removeLivroAutorAntigo(id, livroForm.autores(), livro.getAutores());
		this.removeLivroCategoriaAntigo(id, livroForm.categorias(), livro.getCategorias());

		this.verificaAutoresCadastradosPorId(livroForm.autores());
		this.verificaCategoriasCadastradasPorId(livroForm.categorias());

		livro = livroRepo.save(livro);

		LivroDTO livroAtualizadoDTO = new LivroDTO(livro);

		return ResponseEntity.ok(livroAtualizadoDTO);
	}

	private void removeLivroCategoriaAntigo(
			Integer idLivro,
			List<Integer> idNovascategorias,
			List<Categoria> categoriasAntesDeAtualizar) {

		List<Integer> idCategoriasAntes = categoriasAntesDeAtualizar
				.stream().map(Categoria::getId).toList();

		List<Integer> idCategoriasParaRemocao = new ArrayList<Integer>();

		idCategoriasAntes.forEach(idCategoriaAntes -> {
			if (!idNovascategorias.contains(idCategoriasAntes)) {
				idCategoriasParaRemocao.add(idCategoriaAntes);
			}
		});

		idCategoriasParaRemocao.forEach(idCategoria -> {
			livroCatRepo.removePorIdLivroEIdCategoria(idLivro, idCategoria);
		});
	}

	private void removeLivroAutorAntigo(
			Integer idLivro,
			List<Integer> idNovosAutores,
			List<Autor> autoresAntesDeAtualizar ) {

		List<Integer> idAutoresAntes = autoresAntesDeAtualizar
				.stream().map(Autor::getId).toList();

		List<Integer> idAutoresParaRemocao = new ArrayList<Integer>();

		idAutoresAntes.forEach(idAutorAntes -> {
			if (!idNovosAutores.contains(idAutorAntes)) {
				idAutoresParaRemocao.add(idAutorAntes);
			}
		});

		idAutoresParaRemocao.forEach(idAutor -> {
			this.livroAutorRepo.removePorIdLivroEIdAutor(idLivro, idAutor);
		});
	}
}
