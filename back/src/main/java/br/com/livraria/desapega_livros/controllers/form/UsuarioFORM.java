package br.com.livraria.desapega_livros.controllers.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record UsuarioFORM(@NotBlank String nome, @NotBlank String sobrenome,
		@PastOrPresent(message = "A data de nascimento do usuário não pode ser no futuro!") LocalDate dataNascimento,
		@NotBlank @Email String email, @NotBlank String whatsapp, @NotNull Integer idEndereco, @NotNull String senha) {

}
