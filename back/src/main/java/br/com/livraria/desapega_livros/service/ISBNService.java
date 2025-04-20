package br.com.livraria.desapega_livros.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.livraria.desapega_livros.controllers.dto.ISBNResponseDTO;
import br.com.livraria.desapega_livros.infra.exception.DadoInvalidoException;
import br.com.livraria.desapega_livros.repository.AutorRepository;
import br.com.livraria.desapega_livros.repository.CategoriaRepository;
import br.com.livraria.desapega_livros.repository.EditoraRepository;
import br.com.livraria.desapega_livros.repository.entity.Autor;
import br.com.livraria.desapega_livros.repository.entity.Categoria;
import br.com.livraria.desapega_livros.repository.entity.Editora;
import br.com.livraria.desapega_livros.repository.entity.Livro;

@Service
public class ISBNService {

	@Autowired
	EditoraRepository editoraRepo;

	@Autowired
	AutorRepository autorRepo;

	@Autowired
	CategoriaRepository categoriaRepo;

	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;

	public ISBNService(HttpClient httpClient, ObjectMapper objectMapper) {
		this.httpClient = httpClient;
		this.objectMapper = objectMapper;
	}

	public ISBNResponseDTO buscaISBN(String isbn) {

		isbn = formataISBN(isbn);
		String url = "https://brasilapi.com.br/api/isbn/v1/" + isbn;

		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				return null;
			}

			ISBNResponseDTO isbnDTO = objectMapper.readValue(response.body(), ISBNResponseDTO.class);

			if (!dadosValidos(isbnDTO)) {
				throw new DadoInvalidoException(
						"API de ISBNs não possui todos os dados referentes ao livro, utilize o endpoint '/livro' com o método POST para fazer o cadastro do livro ");
			}

			return isbnDTO;

		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Erro interno ao se comunicar com Brasil API");
		}

	}

	public String formataISBN(String isbnForm) {

		if (isbnForm == null) {
			return isbnForm;
		}

		var isbn = isbnForm.replaceAll("\\D", "");

		if (isbn.length() != 13 && isbn.length() != 10) {
			throw new DadoInvalidoException("O ISBN deve ter 13 ou 10 caracteres conforme modelos vigentes!");
		}

		return isbn;
	}

	private boolean dadosValidos(ISBNResponseDTO livro) {

		if (livro.titulo() == null || livro.descricao() == null || livro.qtdPaginas() == null || livro.editora() == null
				|| livro.nomesAutores() == null || livro.nomesCategorias() == null) {

			return false;

		}
		return true;
	}

	private Livro converteDadosIsbnParaLivro(ISBNResponseDTO livroISBN) {

		var livro = new Livro();

		livro.setTitulo(livroISBN.titulo());
		livro.setDescricao(livroISBN.descricao());
		livro.setQtdPaginas(livroISBN.qtdPaginas());
		livro.setIsbn(livroISBN.isbn());

		Editora editora = editoraRepo.findByNome(livroISBN.editora());

		if (editora == null) {
			editora = editoraRepo.save(new Editora(livroISBN.editora()));
		}

		livro.setEditora(editora);

		List<Autor> autores = buscaAutores(livroISBN.nomesAutores());
		livro.setAutores(autores);

		List<Categoria> categorias = buscaCategorias(livroISBN.nomesCategorias());
		livro.setCategorias(categorias);

		return null;

	}

	@Transactional
	private List<Categoria> buscaCategorias(List<String> nomesCategorias) {

		List<Categoria> categoriasSalvas = new ArrayList<Categoria>();

		nomesCategorias.forEach(nome -> {

			var categoria = categoriaRepo.findByNome(nome);

			if (categoria == null) {
				categoria = categoriaRepo.save(new Categoria(nome));
			}

			categoriasSalvas.add(categoria);
		});

		return categoriasSalvas;
	}

	@Transactional
	private List<Autor> buscaAutores(List<String> nomesAutores) {

		List<Autor> autoresSalvos = new ArrayList<Autor>();

		nomesAutores.forEach(nome -> {
			var autor = autorRepo.findByNome(nome);

			if (autor == null) {
				autor = autorRepo.save(new Autor(nome));
			}

			autoresSalvos.add(autor);
		});

		return autoresSalvos;
	}

}
