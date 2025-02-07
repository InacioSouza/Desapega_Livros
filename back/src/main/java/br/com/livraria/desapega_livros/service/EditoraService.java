package br.com.livraria.desapega_livros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.livraria.desapega_livros.controllers.dto.EditoraDTO;
import br.com.livraria.desapega_livros.controllers.form.EditoraFORM;
import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repository.EditoraRepository;
import br.com.livraria.desapega_livros.repository.entity.Editora;

@Service
public class EditoraService {

	@Autowired
	private EditoraRepository editoraRepo;

	@Transactional
	public ResponseEntity<?> cadastrar(EditoraFORM editoraForm) {

		if (editoraRepo.existsByNome(editoraForm.nome())) {
			throw new RegistroEncontradoException("Editora " + editoraForm.nome() + " já cadastrada no banco!");
		}

		Editora editora = new Editora(editoraForm);
		EditoraDTO editoraSalvaDTO = new EditoraDTO(editoraRepo.save(editora));

		return ResponseEntity.status(HttpStatus.CREATED).body(editoraSalvaDTO);
	}

	@Transactional
	public ResponseEntity<?> listar(Pageable pagina) {
		var editoras = editoraRepo.findAll(pagina).map(EditoraDTO::new);

		if (editoras.getNumberOfElements() == 0) {
			throw new NenhumRegistroEncontradoException("Ainda não há editoras cadastradas no banco");
		}

		return ResponseEntity.ok(editoras);
	}

	@Transactional
	public ResponseEntity<?> atualizar(Integer id, EditoraFORM ediotraForm) {
		if (!editoraRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Não existe editora cadastrada para o id : " + id);
		}

		Editora editora = editoraRepo.findById(id).get();
		editora.setNome(ediotraForm.nome());

		return ResponseEntity.ok(new EditoraDTO(editora));

	}

	@Transactional
	public ResponseEntity<?> excluir(Integer id) {
		if (!editoraRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Não existe editora cadastrada para o id : " + id);
		}

		editoraRepo.deleteById(id);

		return ResponseEntity.ok("Editora excluída com sucesso!");
	}
}
