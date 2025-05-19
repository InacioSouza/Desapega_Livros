package br.com.livraria.desapega_livros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.livraria.desapega_livros.controllers.dto.UsuarioDTO;
import br.com.livraria.desapega_livros.controllers.form.UsuarioFORM;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repository.EnderecoRepository;
import br.com.livraria.desapega_livros.repository.UsuarioRepository;
import br.com.livraria.desapega_livros.repository.entity.Usuario;
import br.com.livraria.desapega_livros.repository.entity.enuns.StatusUsuario;

@Service
public class UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepo;

	@Autowired
	private EnderecoRepository enderecoRepo;

	@Transactional
	public ResponseEntity<?> cadastrar(UsuarioFORM usuarioForm) {

		Usuario usuario = new Usuario(usuarioForm);

		if (!enderecoRepo.existsById(usuarioForm.idEndereco())) {
			throw new RegistroNaoExisteException(
					"Não existe endereço cadastrado para o id : " + usuarioForm.idEndereco());
		}

		var endereco = enderecoRepo.findById(usuarioForm.idEndereco()).get();

		usuario.setEndereco(endereco);
		usuario.setStatus(StatusUsuario.ATIVO.toString());

		var usuarioSalvo = usuarioRepo.save(usuario);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
		var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(usuarioSalvo.getId()).toUri();

		return ResponseEntity.created(uri).body(new UsuarioDTO(usuarioSalvo));
	}

	@Transactional
	public ResponseEntity<?> suspender(Integer idUsuario){

		if(!usuarioRepo.existsById(idUsuario)){
			throw new RegistroNaoExisteException("Não existe usuário cadastrado para o id : " + idUsuario);
		}

		Usuario usuario = usuarioRepo.findById(idUsuario).get();

		usuario.setStatus(StatusUsuario.SUSPENSO.toString());

		return ResponseEntity.ok(new UsuarioDTO(usuarioRepo.save(usuario)));
	}

	@Transactional
	public ResponseEntity<?> inativar(Integer idUsuario){

		if(!usuarioRepo.existsById(idUsuario)){
			throw new RegistroNaoExisteException("Não existe usuário para o id : " + idUsuario);
		}

		Usuario usuario = usuarioRepo.findById(idUsuario).get();

		usuario.setStatus(StatusUsuario.INATIVO.toString());

		return ResponseEntity.ok(new UsuarioDTO(usuarioRepo.save(usuario)));
	}
}
