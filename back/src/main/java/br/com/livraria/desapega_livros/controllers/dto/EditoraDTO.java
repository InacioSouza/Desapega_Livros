package br.com.livraria.desapega_livros.controllers.dto;

import br.com.livraria.desapega_livros.repository.entity.Editora;

public record EditoraDTO(Integer id, String nome) {
	public EditoraDTO(Editora editora) {
		this(editora.getId(), editora.getNome());
	}
}
