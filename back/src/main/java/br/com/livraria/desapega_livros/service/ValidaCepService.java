package br.com.livraria.desapega_livros.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.livraria.desapega_livros.controllers.dto.ViaCepResponseDTO;
import br.com.livraria.desapega_livros.infra.exception.DadoInvalidoException;
import br.com.livraria.desapega_livros.infra.exception.FalhaNaCominucacaoComAPIException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;

@Service
public class ValidaCepService {

	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;

	public ValidaCepService() {
		this.httpClient = HttpClient.newBuilder().build();
		this.objectMapper = new ObjectMapper();
	}

	public ViaCepResponseDTO cepExistente(String cepForm) {

		if (cepForm == null || cepForm == "") {
			throw new DadoInvalidoException("O CEP não pode ser nulo ou vazio!");
		}

		String cep = formataCEP(cepForm);
		String url = "https://viacep.com.br/ws/" + cep + "/json/";

		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new FalhaNaCominucacaoComAPIException(
						"Erro ao fazer a busca do CEP no webservice ViaCep : " + response.statusCode());
			}

			ViaCepResponseDTO viaCepResponse = objectMapper.readValue(response.body(), ViaCepResponseDTO.class);

			if (Boolean.TRUE.equals(viaCepResponse.erro())) {
				throw new RegistroNaoExisteException("O CEP informado não existe: " + cep);
			}

			return viaCepResponse;

		} catch (Exception e) {
			throw new RuntimeException("Ero interno ao se comunicar com webservice ViaCep");
		}

	}

	public String formataCEP(String cepForm) {
		String cep = cepForm.replaceAll("\\D", "");

		if (cep.length() != 8) {
			throw new DadoInvalidoException("O formato de CEP não é válido!");
		}
		return cep;
	}

}
