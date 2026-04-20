package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UsuarioFORM(
		@NotBlank
		String nome,

		@NotBlank
		String sobrenome,

		@PastOrPresent(message = "A data de nascimento do usuário não pode ser no futuro!")
		LocalDate dataNascimento,

		@NotBlank
		@Email
		String email,

		String whatsapp,

		Integer idEndereco,

		@NotNull
		@Min(6)
		String senha,

		EnderecoFORM endereco

) {}
