package br.com.livraria.desapega_livros.controllers.dto;

import br.com.livraria.desapega_livros.repository.entity.Solicitacao;

public record SolicitacaoDTO(Integer id, LivroDTO livro) {
    public SolicitacaoDTO(Solicitacao solicitacao) {
        this(solicitacao.getId(), new LivroDTO(solicitacao.getLivro()));
    }
}
