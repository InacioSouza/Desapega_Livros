package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.bases.BaseController;
import br.com.livraria.desapega_livros.entities.Solicitacao;
import br.com.livraria.desapega_livros.services.bases.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.livraria.desapega_livros.controllers.form.SolicitacaoFORM;
import br.com.livraria.desapega_livros.services.SolicitacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/solicitacao")
public class SolicitacaoController
		extends BaseController<Solicitacao, Integer> {

	private SolicitacaoService service;

	public SolicitacaoController(SolicitacaoService service) {
		super(service);
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody @Valid SolicitacaoFORM solicitacaoForm) {
		return service.cadastrar(solicitacaoForm);
	}

	@PutMapping("/cancelar/{id}")
	public ResponseEntity<?> cancelar(@PathVariable("id") Integer idSolicitacao){
		return service.cancelarSolicitacao(idSolicitacao);
	}

	@PutMapping("/negar/{id}")
	public ResponseEntity<?> negar(@PathVariable("id") Integer idSolicitacao){
		return service.negarSolicitacao(idSolicitacao);
	}

	@PutMapping("/aprovar/{id}")
	public ResponseEntity<?> aprovar(@PathVariable("id") Integer idSolicitacao){
		return service.aprovarSolicitacao(idSolicitacao);
	}
}
