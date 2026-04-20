package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.bases.BaseController;
import br.com.livraria.desapega_livros.controllers.form.CategoriaFORM;
import br.com.livraria.desapega_livros.entities.Categoria;
import br.com.livraria.desapega_livros.services.CategoriaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categoria")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Categoria")
public class CategoriaController
		extends BaseController<Categoria, Integer> {

	private CategoriaService service;

	public CategoriaController(CategoriaService service) {
		super(service);
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> cadastrarCategoria(@RequestBody @Valid CategoriaFORM categoriaForm) {
		return service.cadastrar(categoriaForm);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody @Valid CategoriaFORM categoriaForm) {
		return service.atualizar(id, categoriaForm);
	}

}
