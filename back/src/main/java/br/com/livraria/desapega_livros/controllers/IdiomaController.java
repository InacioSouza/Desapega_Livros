package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.bases.BaseController;
import br.com.livraria.desapega_livros.controllers.form.IdiomaFORM;
import br.com.livraria.desapega_livros.entities.Idioma;
import br.com.livraria.desapega_livros.services.IdiomaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/idioma")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Idioma")
public class IdiomaController
		extends BaseController<Idioma, Integer> {

	private IdiomaService service;

	public IdiomaController(IdiomaService service) {
		super(service);
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> cadastrarIdioma(@RequestBody @Valid IdiomaFORM idiomaForm) {
		return service.cadastrar(idiomaForm);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody @Valid IdiomaFORM idiomaForm) {
		return service.atualizar(id, idiomaForm);
	}
}
