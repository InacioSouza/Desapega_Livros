package br.com.livraria.desapega_livros.controllers.dto;

import br.com.livraria.desapega_livros.repository.entity.Autor;

public record AutorDTO(Integer id, String nome, String sobrenome, String nacionalidade, String nomeArtistico) {
	public AutorDTO(Autor autor) {
		this(autor.getId(), autor.getNome(), autor.getSobrenome(), autor.getNacionalidade(), autor.getNomeArtistico());
	}
}
