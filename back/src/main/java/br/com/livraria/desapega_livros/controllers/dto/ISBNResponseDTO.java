package br.com.livraria.desapega_livros.controllers.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ISBNResponseDTO(String isbn, @JsonProperty("title") String titulo,
		@JsonProperty("subtitle") String subtitulo, @JsonProperty("synopsis") String descricao,
		@JsonProperty("authors") List<String> nomesAutores, @JsonProperty("subjects") List<String> nomesCategorias,
		@JsonProperty("publisher") String editora, Integer anoPublicacao, Integer qtdPaginas) {

}
