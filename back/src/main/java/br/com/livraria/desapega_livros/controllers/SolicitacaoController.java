package br.com.livraria.desapega_livros.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livraria.desapega_livros.controllers.form.SolicitacaoFORM;
import br.com.livraria.desapega_livros.service.SolicitacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/solicitacao")
public class SolicitacaoController {

	@Autowired
	private SolicitacaoService service;

	@PostMapping
	public ResponseEntity<?> cadastrar(@Valid SolicitacaoFORM solicitacaoForm) {
		return service.cadastrar(solicitacaoForm);
	}

}
