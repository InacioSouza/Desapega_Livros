package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.form.UsuarioFORM;
import br.com.livraria.desapega_livros.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody @Valid UsuarioFORM usuarioForm) {
		return service.cadastrar(usuarioForm);
	}

	@PutMapping("/suspender/{id}")
	public ResponseEntity<?> suspender(@PathVariable("id") Integer idUsuario){
		return service.suspender(idUsuario);
	}

	@PutMapping("/inativar/{id}")
	public ResponseEntity<?> inativar(@PathVariable("id") Integer idUsuario){
		return service.inativar(idUsuario);
	}
}
