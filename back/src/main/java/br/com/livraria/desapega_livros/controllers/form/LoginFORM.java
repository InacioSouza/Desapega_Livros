package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.NotBlank;

public record LoginFORM(@NotBlank String login, @NotBlank String senha) {
}
