package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.dto.UsuarioDTO;
import br.com.livraria.desapega_livros.services.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuários")
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
