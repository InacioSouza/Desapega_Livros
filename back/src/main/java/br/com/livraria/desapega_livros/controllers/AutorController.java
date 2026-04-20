package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.bases.BaseController;
import br.com.livraria.desapega_livros.controllers.form.AutorFORM;
import br.com.livraria.desapega_livros.entities.Autor;
import br.com.livraria.desapega_livros.services.AutorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autor")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Autor")
public class AutorController extends BaseController<Autor, Integer> {

	private AutorService service;

	public AutorController(AutorService service) {
		super(service);
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> cadastraAutor(@RequestBody @Valid AutorFORM autorForm) {
		return this.service.cadastra(autorForm);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Integer id, @Valid @RequestBody AutorFORM autorForm) {
		return this.service.atualizar(id, autorForm);
	}
}
