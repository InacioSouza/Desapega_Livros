package br.com.livraria.desapega_livros.controllers;

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
import br.com.livraria.desapega_livros.service.EnderecoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

	@Autowired
	private EnderecoService service;

	@PostMapping
	public ResponseEntity<?> cadastraEndereco(@RequestBody @Valid EnderecoFORM enderecoForm) {
		return service.cadastra(enderecoForm);
	}

	@GetMapping
	public ResponseEntity<?> listarEnderecos(@PageableDefault(size = 10, sort = { "logradouro" }) Pageable pagina) {
		return service.listar(pagina);
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
