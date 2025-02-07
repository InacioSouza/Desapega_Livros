package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AutorFORM(@NotBlank @NotNull String nome, @NotBlank @NotNull String sobrenome,
		@NotBlank @NotNull String nacionalidade, String nomeArtistico) {

}
