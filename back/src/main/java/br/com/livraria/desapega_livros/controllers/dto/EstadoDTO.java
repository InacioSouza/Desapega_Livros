package br.com.livraria.desapega_livros.controllers.dto;

import br.com.livraria.desapega_livros.repository.entity.Estado;

public record EstadoDTO(Integer id, String nome, String uf) {

	public EstadoDTO(Estado estado) {
		this(estado.getId(), estado.getNome(), estado.getUf());
	}
}
