package br.com.livraria.desapega_livros.controllers;

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
import br.com.livraria.desapega_livros.service.EditoraService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/editora")
public class EditoraController {

	@Autowired
	private EditoraService service;

	@PostMapping
	public ResponseEntity<?> cadastrarEditora(@RequestBody EditoraFORM editoraForm) {
		return service.cadastrar(editoraForm);
	}

	@GetMapping
	public ResponseEntity<?> listar(@PageableDefault(size = 10, sort = { "nome" }) Pageable pagina) {
		return service.listar(pagina);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody @Valid EditoraFORM editoraForm) {
		return service.atualizar(id, editoraForm);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletar(@PathVariable Integer id) {
		return service.excluir(id);
	}
}
