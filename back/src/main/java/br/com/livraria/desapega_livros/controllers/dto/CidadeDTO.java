package br.com.livraria.desapega_livros.controllers.dto;

import br.com.livraria.desapega_livros.entities.Cidade;

public record CidadeDTO(Integer id, String nome, EstadoDTO estado) {
	public CidadeDTO(Cidade cidade) {
		this(cidade.getId(), cidade.getNome(), new EstadoDTO(cidade.getEstado()));
	}
}
