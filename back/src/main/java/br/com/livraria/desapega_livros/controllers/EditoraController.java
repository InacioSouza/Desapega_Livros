package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.bases.BaseController;
import br.com.livraria.desapega_livros.entities.Editora;
import br.com.livraria.desapega_livros.services.bases.BaseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livraria.desapega_livros.controllers.form.EditoraFORM;
import br.com.livraria.desapega_livros.services.EditoraService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/editora")
@SecurityRequirement(name = "bearer-key")
public class EditoraController
		extends BaseController<Editora, Integer> {

	private EditoraService service;

	public EditoraController(EditoraService service) {
		super(service);
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> cadastrarEditora(@RequestBody EditoraFORM editoraForm) {
		return service.cadastrar(editoraForm);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody @Valid EditoraFORM editoraForm) {
		return service.atualizar(id, editoraForm);
	}
}
