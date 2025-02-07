package br.com.livraria.desapega_livros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.livraria.desapega_livros.controllers.dto.IdiomaDTO;
import br.com.livraria.desapega_livros.controllers.form.IdiomaFORM;
import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repository.IdiomaRepository;
import br.com.livraria.desapega_livros.repository.entity.Idioma;

@Service
public class IdiomaService {

	@Autowired
	private IdiomaRepository idiomaRepo;

	@Transactional
	public ResponseEntity<?> cadastrar(IdiomaFORM idiomaForm) {
		if (idiomaRepo.existsByNomeIgnoreCase(idiomaForm.nome())) {
			throw new RegistroNaoExisteException("Idioma '" + idiomaForm.nome() + "' já cadastrado no banco de dados!");
		}

		Idioma idiomaSalvo = new Idioma();
		idiomaSalvo.setNome(idiomaForm.nome());
		idiomaSalvo = idiomaRepo.save(idiomaSalvo);

		IdiomaDTO idiomaSalvoDTO = new IdiomaDTO(idiomaSalvo);

		return ResponseEntity.status(HttpStatus.CREATED).body(idiomaSalvoDTO);
	}

	@Transactional
	public ResponseEntity<?> listar(Pageable pagina) {
		var idiomas = idiomaRepo.findAll(pagina).map(IdiomaDTO::new);

		if (idiomas.getNumberOfElements() == 0) {
			throw new NenhumRegistroEncontradoException("Ainda não há idiomas cadastrados no banco de dados!");
		}

		return ResponseEntity.ok(idiomas);
	}

	@Transactional
	public ResponseEntity<?> atualizar(Integer id, IdiomaFORM idiomaForm) {

		if (!idiomaRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Não existe idioma cadastrado para o id : " + id);
		}

		Idioma idioma = idiomaRepo.findById(id).get();
		idioma.setNome(idiomaForm.nome().trim());

		return ResponseEntity.ok(new IdiomaDTO(idioma));

	}

	@Transactional
	public ResponseEntity<?> excluir(Integer id) {

		if (!idiomaRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Não existe idioma cadastrado para o id : " + id);
		}

		idiomaRepo.deleteById(id);

		return ResponseEntity.ok("Idioma excluído com sucesso!");
	}

}
