package br.com.livraria.desapega_livros.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livraria.desapega_livros.controllers.form.EstadoFORM;
import br.com.livraria.desapega_livros.service.EstadoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/estado")
public class EstadoController {

	@Autowired
	private EstadoService service;

	@PostMapping
	public ResponseEntity<?> cadastraEstado(@RequestBody @Valid EstadoFORM estadoForm) {
		return service.cadastra(estadoForm);
	}
	
	@GetMapping
	public ResponseEntity<?> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable pagina){
		return service.listar(pagina);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirPorId(@PathVariable Integer id){
		return service.excluir(id);
	}
	
}
