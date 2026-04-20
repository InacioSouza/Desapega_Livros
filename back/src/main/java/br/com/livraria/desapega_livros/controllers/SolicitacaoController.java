package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.bases.BaseController;
import br.com.livraria.desapega_livros.controllers.form.SolicitacaoFORM;
import br.com.livraria.desapega_livros.entities.Solicitacao;
import br.com.livraria.desapega_livros.services.SolicitacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solicitacao")
@Tag(name = "Solicitação",
		description = "End-points destinados ao gerenciamento de solicitações. Um usuário solicita um livro de outro usuário, o usuário deno do livro decide se a solicitação será aprovada")
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
