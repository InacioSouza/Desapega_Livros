package br.com.livraria.desapega_livros.controllers.form;

import jakarta.validation.constraints.NotNull;

public record SolicitacaoFORM(@NotNull Integer idLivro, @NotNull Integer idSolicitante) {

}
