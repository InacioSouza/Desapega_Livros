package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank String login, @NotBlank String senha) {
}
