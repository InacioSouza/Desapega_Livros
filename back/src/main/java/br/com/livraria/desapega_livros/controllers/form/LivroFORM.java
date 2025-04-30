package br.com.livraria.desapega_livros.controllers.form;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record LivroFORM(@NotBlank String titulo, String subtitulo, @NotBlank String descricao, String status,
		Integer anoPublicacao,
		@NotNull Integer idEditora, @NotNull Integer idIdioma, @NotNull Integer idDono, Integer idCidade,
		@NotNull Integer qtdPaginas, @NotBlank String isbn, String opniaoDoador, @NotNull List<Integer> autores,
		@NotNull List<Integer> categorias) {

}
