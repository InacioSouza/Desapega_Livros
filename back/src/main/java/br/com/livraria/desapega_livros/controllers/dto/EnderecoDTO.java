package br.com.livraria.desapega_livros.controllers.dto;

import br.com.livraria.desapega_livros.entities.Endereco;

public record EnderecoDTO(
		Integer id,
		String cep,
		Integer numero) {

	public EnderecoDTO(Endereco endereco) {
		this(endereco.getId(), endereco.getCep(), endereco.getNumero());
	}

}
