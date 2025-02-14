package br.com.livraria.desapega_livros.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.livraria.desapega_livros.controllers.dto.LivroDTO;
import br.com.livraria.desapega_livros.controllers.form.LivroFORM;
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

		System.out.println("\n\n\n Entrei no service!!! \n\n\n");

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
		livro.setDescricao(livroForm.descricao());
		livro.setDataPublicacao(livroForm.dataPublicacao());
		livro.setQtdPaginas(livroForm.qtdPaginas());
		livro.setOpniaoDoador(livroForm.opniaoDoador());
		livro.setIsbn(isbnService.formataISBN(livroForm.isbn()));

		Usuario dono;
		dono = usuarioRepo.findById(livroForm.idDono()).get();

		Idioma idioma;
		idioma = idiomaRepo.findById(livroForm.idIdioma()).get();

		this.verificaLivroCadastrado(livroForm);

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

		livro.setCiade(cidade);
		livro.setStatus(StatusLivro.SOLICITADO.toString());

		Livro livroSalvo = livroRepo.save(livro);
		livroSalvo.setCategorias(categoriasSalvos);
		livroSalvo.setAutores(autoresSalvos);

		this.relacionaAutoresComLivro(autoresSalvos, livroSalvo);
		this.relacionaCategoriasComLivros(categoriasSalvos, livroSalvo);

		var livroDTO = new LivroDTO(livroSalvo);

		var uri = uriBuilder.path("/livro/{id}").buildAndExpand(livroSalvo.getId()).toUri();

		return ResponseEntity.created(uri).body(livroDTO);

	}

	private void verificaLivroCadastrado(LivroFORM livroForm) {
		if (livroRepo.livroJaCadastrado(livroForm.titulo(), livroForm.idDono(), livroForm.idIdioma())) {
			throw new RegistroEncontradoException("Livro já cadastrado!");
		}
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
}
