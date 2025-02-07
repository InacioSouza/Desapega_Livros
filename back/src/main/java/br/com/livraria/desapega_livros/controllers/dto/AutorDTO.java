package br.com.livraria.desapega_livros.controllers.dto;

import br.com.livraria.desapega_livros.repository.entity.Autor;

public record AutorDTO(Integer id, String nome) {
	public AutorDTO(Autor autor) {
		this(autor.getId(), autor.getNome());
	}
}
