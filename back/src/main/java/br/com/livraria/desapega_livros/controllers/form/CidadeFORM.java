package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CidadeFORM(@NotBlank String nome, @NotNull Integer idEstado) {

}
