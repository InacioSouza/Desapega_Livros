package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.NotBlank;

public record CidadeFORM(
        @NotBlank String nome,
        String estado,
        Integer idEstado) {

}
