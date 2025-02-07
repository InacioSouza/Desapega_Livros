package br.com.livraria.desapega_livros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.livraria.desapega_livros.controllers.dto.AutorDTO;
import br.com.livraria.desapega_livros.controllers.form.AutorFORM;
import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repository.AutorRepository;
import br.com.livraria.desapega_livros.repository.entity.Autor;

@Service
public class AutorService {

	@Autowired
	private AutorRepository autorRepo;

	@Transactional
	public ResponseEntity<?> cadastra(AutorFORM autorForm) {

		if (autorRepo.existsByNome(autorForm.nome())) {
			throw new RegistroEncontradoException("Autor já cadastrado no banco!");
		}

		Autor autor = new Autor(autorForm);
		AutorDTO autorSalvoDTO = new AutorDTO(autorRepo.save(autor));

		return ResponseEntity.ok(autorSalvoDTO);
	}

	@Transactional
	public ResponseEntity<Page<AutorDTO>> listar(Pageable pagina) {

		var autores = autorRepo.findAll(pagina).map(AutorDTO::new);

		if (autores.getNumberOfElements() == 0) {
			throw new NenhumRegistroEncontradoException("Nenhum autor encontrado no banco de dados");
		}

		return ResponseEntity.ok(autores);
	}

	@Transactional
	public ResponseEntity<?> excluir(Integer id) {

		if (!autorRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Não existe autor cadastrado para o id : " + id);
		}

		autorRepo.deleteById(id);

		return ResponseEntity.ok("Autor deletado!");

	}

	@Transactional
	public ResponseEntity<?> atualizar(Integer id, AutorFORM autorForm) {

		if (!autorRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Não existe autor cadastrado para o id: " + id);
		}

		Autor autor = autorRepo.findById(id).get();
		autor.atualizarDados(autorForm);

		return ResponseEntity.ok(new AutorDTO(autor));
	}

}
