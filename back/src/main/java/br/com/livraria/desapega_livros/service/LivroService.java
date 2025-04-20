package br.com.livraria.desapega_livros.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.livraria.desapega_livros.controllers.dto.LivroDTO;
import br.com.livraria.desapega_livros.controllers.form.LivroFORM;
import br.com.livraria.desapega_livros.controllers.form.LivroIsbnFORM;
import br.com.livraria.desapega_livros.infra.exception.DadoInvalidoException;
import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.infra.exception.RequisicaoInvalidaException;
import br.com.livraria.desapega_livros.repository.AutorRepository;
import br.com.livraria.desapega_livros.repository.CategoriaRepository;
import br.com.livraria.desapega_livros.repository.CidadeRepository;
import br.com.livraria.desapega_livros.repository.EditoraRepository;
import br.com.livraria.desapega_livros.repository.IdiomaRepository;
import br.com.livraria.desapega_livros.repository.LivroAutorRepository;
import br.com.livraria.desapega_livros.repository.LivroCategoriaRepository;
import br.com.livraria.desapega_livros.repository.LivroRepository;
import br.com.livraria.desapega_livros.repository.UsuarioRepository;
import br.com.livraria.desapega_livros.repository.entity.Autor;
import br.com.livraria.desapega_livros.repository.entity.Categoria;
import br.com.livraria.desapega_livros.repository.entity.Cidade;
import br.com.livraria.desapega_livros.repository.entity.Editora;
import br.com.livraria.desapega_livros.repository.entity.Idioma;
import br.com.livraria.desapega_livros.repository.entity.Livro;
import br.com.livraria.desapega_livros.repository.entity.LivroAutor;
import br.com.livraria.desapega_livros.repository.entity.LivroCategoria;
import br.com.livraria.desapega_livros.repository.entity.Usuario;
import br.com.livraria.desapega_livros.repository.entity.enuns.StatusLivro;
import jakarta.validation.constraints.NotNull;

@Service
public class LivroService {

	@Autowired
	private LivroRepository livroRepo;

	@Autowired
	private EditoraRepository editoraRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private IdiomaRepository idiomaRepo;

	@Autowired
	private CidadeRepository cidadeRepo;

	@Autowired
	private AutorRepository autorRepo;

	@Autowired
	private LivroAutorRepository livroAutorRepo;

	@Autowired
	CategoriaRepository categoriaRepo;

	@Autowired
	LivroCategoriaRepository livroCatRepo;

	@Autowired
	private ISBNService isbnService;

	@Autowired
	private UriComponentsBuilder uriBuilder;

	@Transactional
	public ResponseEntity<?> cadastrar(LivroFORM livroForm, MultipartFile capa) {

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

		this.validaEntidadesRelacionadas(livroForm);

		List<Autor> autoresSalvos = new ArrayList<Autor>();
		List<Categoria> categoriasSalvos = new ArrayList<Categoria>();

		autoresSalvos = this.verificaAutoresCadastradosPorId(livroForm.autores());
		categoriasSalvos = this.verificaCategoriasCadastradasPorId(livroForm.categorias());

		Livro livro = new Livro();

		Editora editora = editoraRepo.findById(livroForm.idEditora()).get();
		livro.setEditora(editora);

		livro.setTitulo(livroForm.titulo());
		livro.setSubtitulo(livroForm.subtitulo());
		livro.setDescricao(livroForm.descricao());
		livro.setAnoPublicacao(livroForm.anoPublicacao());
		livro.setQtdPaginas(livroForm.qtdPaginas());
		livro.setOpniaoDoador(livroForm.opniaoDoador());
		livro.setIsbn(isbnService.formataISBN(livroForm.isbn()));

		Usuario dono;
		dono = usuarioRepo.findById(livroForm.idDono()).get();

		Idioma idioma;
		idioma = idiomaRepo.findById(livroForm.idIdioma()).get();

		livro.setDono(dono);
		livro.setIdioma(idioma);

		try {
			livro.setCapa(capa.getBytes());

		} catch (IOException e) {

			throw new RuntimeException("Erro interno ao processar imagem!");
		}

		Cidade cidade;

		if (livroForm.idCidade() == null) {
			cidade = dono.getEndereco().getCidade();

		} else {
			cidade = cidadeRepo.findById(livroForm.idCidade()).get();
		}

		livro.setCidade(cidade);
		livro.setStatus(StatusLivro.DISPONIVEL.toString());

		Livro livroSalvo = livroRepo.save(livro);
		livroSalvo.setCategorias(categoriasSalvos);
		livroSalvo.setAutores(autoresSalvos);

		this.relacionaAutoresComLivro(autoresSalvos, livroSalvo);
		this.relacionaCategoriasComLivros(categoriasSalvos, livroSalvo);

		var livroDTO = new LivroDTO(livroSalvo);

		var uri = uriBuilder.path("/livro/{id}").buildAndExpand(livroSalvo.getId()).toUri();

		return ResponseEntity.created(uri).body(livroDTO);

	}

	private void validaEntidadesRelacionadas(LivroFORM livroForm) {

		if (!usuarioRepo.existsById(livroForm.idDono())) {
			throw new RegistroNaoExisteException("Não há usuário cadastrado para o id :" + livroForm.idDono());
		}

		if (!idiomaRepo.existsById(livroForm.idIdioma())) {
			throw new RegistroNaoExisteException("Não há idioma cadastrado para o id : " + livroForm.idIdioma());
		}

		if (!editoraRepo.existsById(livroForm.idEditora())) {
			throw new RegistroNaoExisteException("Não há editora cadastrada para o id : " + livroForm.idEditora());
		}
	}

	@Transactional
	private List<Autor> verificaAutoresCadastradosPorId(List<Integer> idAutores) {
		List<Autor> autores = new ArrayList<>();

		for (int i = 0; i < idAutores.size(); i++) {
			if (!autorRepo.existsById(idAutores.get(i))) {
				throw new RegistroNaoExisteException("Não há autor cadastrado para o id : " + idAutores.get(i));
			}

			autores.add(autorRepo.findById(idAutores.get(i)).get());
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
			if (!categoriaRepo.existsById(idCategorias.get(i))) {
				throw new RegistroNaoExisteException("Não há categoria cadastrada para o id : " + idCategorias.get(i));
			}
			categorias.add(categoriaRepo.findById(idCategorias.get(i)).get());
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
			categoriaRepo.deleteById(livro.getId());
		}

	}

	@Transactional
	public ResponseEntity<?> cadastraPorISBN(LivroIsbnFORM livroForm, MultipartFile capa) {

		if (!idiomaRepo.existsById(livroForm.idIdioma())) {
			throw new RequisicaoInvalidaException("Não existe idioma cadastrad para o id:" + livroForm.idIdioma());
		}

		if (!usuarioRepo.existsById(livroForm.idDono())) {
			throw new RequisicaoInvalidaException("Não existe usuario cadastrado para o id: " + livroForm.idDono());
		}

		var dadosLivro = isbnService.buscaISBN(livroForm.isbn());

		if (this.verificaLivroJaCadastrado(dadosLivro.titulo(), livroForm.idDono(), livroForm.idIdioma())) {
			throw new RegistroEncontradoException("Livro já cadastrado!");
		}

		Usuario dono = usuarioRepo.findById(livroForm.idDono()).get();
		Idioma idioma = idiomaRepo.findById(livroForm.idIdioma()).get();

		Livro livro = new Livro(dadosLivro);

		livro.setDono(dono);
		livro.setIdioma(idioma);

		Cidade cidade;

		if (livroForm.idCidade() == null) {
			cidade = dono.getEndereco().getCidade();
		} else {
			cidade = cidadeRepo.findById(livroForm.idCidade()).get();
		}

		livro.setCidade(cidade);

		livro.setOpniaoDoador(livroForm.opniaoDoador());
		livro.setEditora(this.verificaEditoraCadastradaPorNome(dadosLivro.editora()));
		livro.setStatus(StatusLivro.DISPONIVEL.toString());

		try {
			livro.setCapa(capa.getBytes());

		} catch (IOException e) {
			throw new RuntimeException("Erro interno ao processar imagem!");
		}

		Livro livroSalvo = livroRepo.save(livro);

		List<Autor> autores = this.verificaAutoresCadastradosPorListNome(dadosLivro.nomesAutores());
		List<Categoria> categorias = this.verificaCategoriasCadastradasPorListNomes(dadosLivro.nomesCategorias());

		this.relacionaAutoresComLivro(autores, livroSalvo);
		this.relacionaCategoriasComLivros(categorias, livroSalvo);

		livro.setAutores(autores);
		livro.setCategorias(categorias);

		LivroDTO livroSalvoDTO = new LivroDTO(livroSalvo);

		var uri = uriBuilder.path("/livro/{id}").buildAndExpand(livroSalvo.getId()).toUri();

		return ResponseEntity.created(uri).body(livroSalvoDTO);
	}

	private boolean verificaLivroJaCadastrado(String titulo, Integer idDono, Integer ididioma) {
		return livroRepo.livroJaCadastrado(titulo, idDono, ididioma);
	}

	private Editora verificaEditoraCadastradaPorNome(String nome) {
		Editora ed = editoraRepo.findByNome(nome);

		if (ed == null) {
			ed = editoraRepo.save(new Editora(null, nome));
		}
		return ed;

	}

	private List<Autor> verificaAutoresCadastradosPorListNome(List<String> nomesAutores) {

		List<Autor> autores = new ArrayList<Autor>();

		nomesAutores.forEach(nome -> {
			Autor autor = autorRepo.findByNome(nome);

			if (autor == null) {
				autor = autorRepo.save(new Autor(null, nome));
			}
			autores.add(autor);
		});

		return autores;
	}

	private List<Categoria> verificaCategoriasCadastradasPorListNomes(List<String> nomesCategorias) {
		List<Categoria> categorias = new ArrayList<Categoria>();

		nomesCategorias.forEach(nome -> {
			Categoria categoria = categoriaRepo.findByNome(nome);

			if (categoria == null) {
				categoria = categoriaRepo.save(new Categoria(null, nome));
			}

			categorias.add(categoria);
		});
		return categorias;
	}

	public ResponseEntity<?> listar(Pageable pagina) {

		Page<Livro> livros = livroRepo.findAll(pagina);

		if (livros.isEmpty()) {
			throw new NenhumRegistroEncontradoException("Não há livros cadastrados no banco!");

		}
		List<LivroDTO> livrosDTO = livros.stream().map(LivroDTO::new).collect(Collectors.toList());

		return ResponseEntity.ok(livrosDTO);
	}

	@Transactional
	public ResponseEntity<?> remover(Integer id) {
		if (!livroRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Não existe livro cadastrado para o id: " + id);
		}

		Livro livro = livroRepo.findById(id).get();
		livro.setStatus(StatusLivro.REMOVIDO.toString());

		return ResponseEntity.ok(livro);
	}

	@Transactional
	public ResponseEntity<?> atualizar(Integer id, LivroFORM livroForm, MultipartFile capa) {

		if (!livroRepo.existsById(id)) {
			throw new RequisicaoInvalidaException("Não existe livro cadastrado para o id:" + id);
		}

		if (capa.isEmpty()) {
			throw new RequisicaoInvalidaException("A capa deve ser passada como arquivo Multipart!");
		}

		this.validaEntidadesRelacionadas(livroForm);

		Livro livro = livroRepo.findById(id).get();

		Usuario dono = usuarioRepo.findById(livroForm.idDono()).get();
		Idioma idioma = idiomaRepo.findById(livroForm.idIdioma()).get();
		Editora editora = editoraRepo.findById(livroForm.idEditora()).get();

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

		Cidade cidade;

		if (livroForm.idCidade() == null) {
			cidade = dono.getEndereco().getCidade();
		} else {
			cidade = cidadeRepo.findById(livroForm.idCidade()).get();
		}

		livro.setCidade(cidade);

		this.removeLivroAutorAntigo(id, livroForm.autores(), livro.getAutores());
		this.removeLivroCategoriaAntigo(id, livroForm.categorias(), livro.getCategorias());

		List<Autor> autoresLivroAtualizado = this.verificaAutoresCadastradosPorId(livroForm.autores());
		List<Categoria> categoriaLivroAtualizado = this.verificaCategoriasCadastradasPorId(livroForm.categorias());

		livro = livroRepo.save(livro);

		LivroDTO livroAtualizadoDTO = new LivroDTO(livro);

		return ResponseEntity.ok(livroAtualizadoDTO);

	}

	private void removeLivroCategoriaAntigo(Integer idLivro, @NotNull List<Integer> idNovascategorias,
			List<Categoria> categoriasAntesDeAtualizar) {

		List<Integer> idCategoriasAntes = categoriasAntesDeAtualizar.stream().map(categoria -> categoria.getId())
				.collect(Collectors.toList());

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

	private void removeLivroAutorAntigo(Integer idLivro, List<Integer> idNovosAutores,
			List<Autor> autoresAnteDeAtualizar) {

		List<Integer> idAutoresAntes = (List<Integer>) autoresAnteDeAtualizar.stream().map(autor -> autor.getId())
				.collect(Collectors.toList());

		List<Integer> idAutoresParaRemocao = new ArrayList<Integer>();

		idAutoresAntes.forEach(idAutorAntes -> {
			if (!idNovosAutores.contains(idAutorAntes)) {
				idAutoresParaRemocao.add(idAutorAntes);
			}
		});

		idAutoresParaRemocao.forEach(idAutor -> {
			livroAutorRepo.removePorIdLivroEIdAutor(idLivro, idAutor);
		});
	}
}
