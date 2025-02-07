package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IdiomaFORM(@NotBlank @NotNull String nome) {

}
