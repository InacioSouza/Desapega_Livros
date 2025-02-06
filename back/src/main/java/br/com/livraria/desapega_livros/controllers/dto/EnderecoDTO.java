package br.com.livraria.desapega_livros.controllers.dto;

import br.com.livraria.desapega_livros.repository.entity.Endereco;

public record EnderecoDTO(Integer id, String cep, Integer numero, String logradouro, String bairro, String complemento,
		CidadeDTO cidade) {

	public EnderecoDTO(Endereco endereco) {
		this(endereco.getId(), endereco.getCep(), endereco.getNumero(), endereco.getLogradouro(), endereco.getBairro(),
				endereco.getComplemento(), new CidadeDTO(endereco.getCidade()));
	}

}
