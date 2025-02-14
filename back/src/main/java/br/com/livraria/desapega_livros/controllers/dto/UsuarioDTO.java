package br.com.livraria.desapega_livros.controllers.dto;

import java.time.LocalDate;

import br.com.livraria.desapega_livros.repository.entity.Usuario;

public record UsuarioDTO(Integer id, String nome, String sobrenome, LocalDate dataNascimento, String email,
		String whatsapp, Integer advertencias, String status) {

	public UsuarioDTO(Usuario usuario) {
		this(usuario.getId(), usuario.getNome(), usuario.getSobrenome(), usuario.getDataNascimento(),
				usuario.getEmail(), usuario.getWhatsapp(), usuario.getAdvertencias(), usuario.getStatus());
	}

}
