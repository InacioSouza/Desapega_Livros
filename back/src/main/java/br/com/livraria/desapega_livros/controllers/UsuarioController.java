package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.dto.UsuarioDTO;
import br.com.livraria.desapega_livros.controllers.form.UsuarioFORM;
import br.com.livraria.desapega_livros.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@PutMapping("/suspender/{id}")
	public ResponseEntity<?> suspender(@PathVariable("id") Integer idUsuario){
		return ResponseEntity.ok(
				new UsuarioDTO(service.suspender(idUsuario))
		);
	}

	@PutMapping("/inativar/{id}")
	public ResponseEntity<?> inativar(@PathVariable("id") Integer idUsuario){
		return ResponseEntity.ok(new UsuarioDTO(service.inativar(idUsuario)));
	}
}
