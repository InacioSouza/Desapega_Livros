package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.bases.BaseController;
import br.com.livraria.desapega_livros.controllers.dto.EnderecoDTO;
import br.com.livraria.desapega_livros.controllers.form.EnderecoFORM;
import br.com.livraria.desapega_livros.entities.Endereco;
import br.com.livraria.desapega_livros.services.EnderecoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/endereco")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Endereço")
public class EnderecoController
		extends BaseController<Endereco, Integer> {

	private EnderecoService service;

	public EnderecoController(EnderecoService service) {
		super(service);
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> cadastraEndereco(@RequestBody @Valid EnderecoFORM enderecoForm) {

		Endereco endereco = service.cadastra(enderecoForm);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
		var uri = uriBuilder.path("/endereco/{id}").buildAndExpand(endereco.getId()).toUri();

		return ResponseEntity.created(uri).body(new EnderecoDTO(endereco));
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
