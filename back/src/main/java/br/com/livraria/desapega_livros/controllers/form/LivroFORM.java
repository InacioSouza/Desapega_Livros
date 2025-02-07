package br.com.livraria.desapega_livros.controllers.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroFORM(@NotBlank @NotNull String titulo, String descricao, LocalDate dataPublicacao,
		@NotNull Byte[] capa, @NotNull Integer idEditora, @NotNull Integer idIdioma, @NotNull Integer idDono,
		@NotNull Integer idCidade, @NotNull Integer qtdPaginas, String isbn, String opniaoDoador) {

}
