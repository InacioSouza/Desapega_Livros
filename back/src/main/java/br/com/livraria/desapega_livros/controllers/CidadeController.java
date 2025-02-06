package br.com.livraria.desapega_livros.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livraria.desapega_livros.controllers.form.CidadeFORM;
import br.com.livraria.desapega_livros.service.CidadeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cidade")
public class CidadeController {

	@Autowired
	private CidadeService service;
	
	@PostMapping
	public ResponseEntity<?> cadastrarCidade(@RequestBody @Valid CidadeFORM cidadeForm) {
	
	return service.cadastra(cidadeForm);	
	}
}
