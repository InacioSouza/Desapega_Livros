package br.com.livraria.desapega_livros.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.livraria.desapega_livros.controllers.dto.ISBNResponseDTO;
import br.com.livraria.desapega_livros.infra.exception.DadoInvalidoException;

@Service
public class ISBNService {

	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;

	public ISBNService() {
		this.httpClient = HttpClient.newBuilder().build();
		this.objectMapper = new ObjectMapper();
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

}
