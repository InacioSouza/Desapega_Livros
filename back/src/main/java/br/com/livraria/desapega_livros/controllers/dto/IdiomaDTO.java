package br.com.livraria.desapega_livros.controllers.dto;

import br.com.livraria.desapega_livros.repository.entity.Idioma;

public record IdiomaDTO(Integer id, String nome) {
	public IdiomaDTO(Idioma idioma) {
		this(idioma.getId(), idioma.getNome());
	}

}
