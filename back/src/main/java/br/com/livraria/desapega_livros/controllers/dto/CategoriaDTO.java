package br.com.livraria.desapega_livros.controllers.dto;

import br.com.livraria.desapega_livros.repository.entity.Categoria;

public record CategoriaDTO(Integer id, String nome) {
	public CategoriaDTO(Categoria cateoria) {
		this(cateoria.getId(), cateoria.getNome());
	}
}
