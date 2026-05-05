package br.com.livraria.desapega_livros.services;

import br.com.livraria.desapega_livros.controllers.dto.EditoraDTO;
import br.com.livraria.desapega_livros.controllers.form.EditoraFORM;
import br.com.livraria.desapega_livros.entities.Editora;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.EditoraRepository;
import br.com.livraria.desapega_livros.services.bases.BaseServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EditoraService
		extends BaseServiceImpl<Editora, Integer> {

	private EditoraRepository editoraRepo;

	public EditoraService(EditoraRepository editoraRepo) {
		super(editoraRepo);
		this.editoraRepo = editoraRepo;
	}

	@Transactional
	public ResponseEntity<?> cadastrar(EditoraFORM editoraForm) {

		if (this.editoraRepo.existsByNome(editoraForm.nome())) {
			throw new RegistroEncontradoException(
					"Editora " + editoraForm.nome() + " já cadastrada no banco!");
		}

		Editora editora = new Editora(editoraForm);
		EditoraDTO editoraSalvaDTO = new EditoraDTO(this.editoraRepo.save(editora));

		UriComponentsBuilder uribuilder = UriComponentsBuilder.newInstance();
		var uri = uribuilder.path("/editora/{id}").buildAndExpand(editora.getId()).toUri();

		return ResponseEntity.created(uri).body(editoraSalvaDTO);
	}

	@Transactional
	public ResponseEntity<?> atualizar(Integer id, EditoraFORM ediotraForm) {
		if (!this.editoraRepo.existsById(id)) {
			throw new RegistroNaoExisteException(
					"Não existe editora cadastrada para o id : " + id);
		}

		Editora editora = this.editoraRepo.findById(id).get();
		editora.setNome(ediotraForm.nome());

		return ResponseEntity.ok(new EditoraDTO(editora));

	}

	public Editora findByNome(String nome) {
		return this.editoraRepo.findByNome(nome);
	}
}
