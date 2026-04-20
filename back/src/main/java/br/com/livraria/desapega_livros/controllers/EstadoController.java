package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.bases.BaseController;
import br.com.livraria.desapega_livros.entities.Estado;
import br.com.livraria.desapega_livros.services.bases.BaseService;
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
import br.com.livraria.desapega_livros.services.EstadoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/estado")
public class EstadoController
		extends BaseController<Estado, Integer> {

	private EstadoService service;

	public EstadoController(EstadoService service) {
		super(service);
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> cadastraEstado(@RequestBody @Valid EstadoFORM estadoForm) {
		return service.cadastra(estadoForm);
	}
}
