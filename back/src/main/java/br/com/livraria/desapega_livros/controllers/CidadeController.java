package br.com.livraria.desapega_livros.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping
	public ResponseEntity<?> listarCidades(@PageableDefault(size = 10, sort = { "nome" }) Pageable pagina) {
		return service.listar(pagina);
	}
}
