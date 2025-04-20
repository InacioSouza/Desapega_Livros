package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroIsbnFORM(@NotBlank String isbn, @NotNull Integer idDono, @NotNull Integer idIdioma,
		String opniaoDoador, Integer idCidade) {

}
