package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.bases.BaseController;
import br.com.livraria.desapega_livros.entities.Endereco;
import br.com.livraria.desapega_livros.services.bases.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livraria.desapega_livros.controllers.form.EnderecoFORM;
import br.com.livraria.desapega_livros.services.EnderecoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/endereco")
public class EnderecoController
		extends BaseController<Endereco, Integer> {

	private EnderecoService service;

	public EnderecoController(EnderecoService service) {
		super(service);
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> cadastraEndereco(@RequestBody @Valid EnderecoFORM enderecoForm) {
		return service.cadastra(enderecoForm);
	}
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<?> enderecoUsuario(@PathVariable("id")  Integer idUsuario){
		return service.enderecoPorUsuario(idUsuario);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarEndereco(@PathVariable Integer id,
			@RequestBody @Valid EnderecoFORM enderecoForm) {
		return service.atualizar(id, enderecoForm);
	}
}
