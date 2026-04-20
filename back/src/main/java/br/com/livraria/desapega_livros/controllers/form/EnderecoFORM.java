package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoFORM(
		@NotBlank
		@NotNull
		String cep,

		@NotNull
		Integer numero,

		String complemento
) {}
