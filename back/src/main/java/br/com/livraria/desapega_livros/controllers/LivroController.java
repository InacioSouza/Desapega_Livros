package br.com.livraria.desapega_livros.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.livraria.desapega_livros.controllers.form.LivroFORM;
import br.com.livraria.desapega_livros.controllers.form.LivroIsbnFORM;
import br.com.livraria.desapega_livros.service.LivroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/livro")
public class LivroController {

	@Autowired
	private LivroService service;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> cadastrarLivro(@RequestPart("capa") MultipartFile capa,
			@RequestPart("infos") @Valid LivroFORM livroForm) {

		return service.cadastrar(livroForm, capa);
	}

	@PostMapping("/isbn")
	public ResponseEntity<?> cadastrarLivroPorISBN(@RequestPart("capa") MultipartFile capa,
			@RequestPart("infos") @Valid LivroIsbnFORM livroForm) {
		return service.cadastraPorISBN(livroForm, capa);
	}

	@GetMapping
	public ResponseEntity<?> listar(@PageableDefault(size = 10, sort = { "titulo" }) Pageable pagina) {
		return service.listar(pagina);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Integer id) {
		return service.remover(id);
	}

	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, name = "/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestPart("infos") LivroFORM livroForm,
			@RequestPart("capa") MultipartFile capa) {
		return service.atualizar(id, livroForm, capa);
	}
}
