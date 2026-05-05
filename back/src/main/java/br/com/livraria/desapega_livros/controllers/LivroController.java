package br.com.livraria.desapega_livros.controllers;

import br.com.livraria.desapega_livros.controllers.dto.LivroDTO;
import br.com.livraria.desapega_livros.controllers.form.LivroFORM;
import br.com.livraria.desapega_livros.controllers.form.LivroIsbnFORM;
import br.com.livraria.desapega_livros.services.LivroService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/livro")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Livro")
public class LivroController {

	private LivroService service;

	public LivroController(
			LivroService service) {
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> cadastrarLivro(
			@RequestPart("capa") MultipartFile capa,
			@RequestPart("infos") @Valid LivroFORM livroForm) {
		return this.service.cadastrar(livroForm, capa);
	}

	@PostMapping("/isbn")
	public ResponseEntity<?> cadastrarLivroPorISBN(
			@RequestPart("capa") MultipartFile capa,
			@RequestPart("infos")
			@Valid LivroIsbnFORM livroForm) {
		return this.service.cadastraPorISBN(livroForm, capa);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Integer id) {
		return service.remover(id);
	}

	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/{id}")
	public ResponseEntity<?> atualizar(@PathVariable("id") Integer id, @RequestPart("infos") LivroFORM livroForm,
			@RequestPart("capa") MultipartFile capa) {
		return this.service.atualizar(id, livroForm, capa);
	}

	@GetMapping("/all")
	public ResponseEntity<Page<LivroDTO>> findWithPagination(
			@PageableDefault(size = 10, sort = { "id" }) Pageable pagina) {
		return ResponseEntity.ok(this.service.findWithPagination(pagina).map(LivroDTO::new));
	}

	@GetMapping("/{id}")
	public ResponseEntity<LivroDTO> findById(Integer id) {
		return ResponseEntity.ok(new LivroDTO(this.service.findById(id)));
	}
}
