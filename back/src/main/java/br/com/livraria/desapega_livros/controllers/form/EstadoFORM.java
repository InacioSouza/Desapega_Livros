package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.NotBlank;

public record EstadoFORM(@NotBlank String nome, @NotBlank String uf) {

}
