package br.com.livraria.desapega_livros.services;

import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import br.com.livraria.desapega_livros.services.bases.BaseService;
import br.com.livraria.desapega_livros.services.bases.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.livraria.desapega_livros.controllers.dto.AutorDTO;
import br.com.livraria.desapega_livros.controllers.form.AutorFORM;
import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.AutorRepository;
import br.com.livraria.desapega_livros.entities.Autor;

@Service
public class AutorService
		extends BaseServiceImpl<Autor, Integer> {

	private AutorRepository autorRepo;

	public AutorService(AutorRepository baseRepository) {
		super(baseRepository);
	}

	@Transactional
	public ResponseEntity<?> cadastra(AutorFORM autorForm) {

		if (autorRepo.existsByNome(autorForm.nome())) {
			throw new RegistroEncontradoException("Autor já cadastrado no banco!");
		}

		Autor autor = new Autor(autorForm);
		AutorDTO autorSalvoDTO = new AutorDTO(autorRepo.save(autor));

		UriComponentsBuilder uribuilder = UriComponentsBuilder.newInstance();

		var uri = uribuilder.path("/autor/{id}").buildAndExpand(autor.getId()).toUri();

		return ResponseEntity.created(uri).body(autorSalvoDTO);
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
