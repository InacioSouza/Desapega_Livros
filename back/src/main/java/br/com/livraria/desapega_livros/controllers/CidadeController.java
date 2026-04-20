package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.bases.BaseController;
import br.com.livraria.desapega_livros.controllers.form.CidadeFORM;
import br.com.livraria.desapega_livros.entities.Cidade;
import br.com.livraria.desapega_livros.services.CidadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cidade")
public class CidadeController
		extends BaseController<Cidade, Integer> {

	private CidadeService service;

	public CidadeController(CidadeService service) {
		super(service);
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> cadastrarCidade(@RequestBody @Valid CidadeFORM cidadeForm) {

		return service.cadastra(cidadeForm);
	}

}
