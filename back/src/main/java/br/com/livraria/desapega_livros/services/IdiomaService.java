package br.com.livraria.desapega_livros.services;

import br.com.livraria.desapega_livros.controllers.dto.IdiomaDTO;
import br.com.livraria.desapega_livros.controllers.form.IdiomaFORM;
import br.com.livraria.desapega_livros.entities.Idioma;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.IdiomaRepository;
import br.com.livraria.desapega_livros.services.bases.BaseService;
import br.com.livraria.desapega_livros.services.bases.BaseServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class IdiomaService
		extends BaseServiceImpl<Idioma, Integer>
		implements BaseService<Idioma, Integer> {

	private IdiomaRepository idiomaRepo;

	public IdiomaService(IdiomaRepository idiomaRepo) {
		super(idiomaRepo);
		this.idiomaRepo = idiomaRepo;
	}

	@Transactional
	public ResponseEntity<?> cadastrar(IdiomaFORM idiomaForm) {
		if (idiomaRepo.existsByNomeIgnoreCase(idiomaForm.nome())) {
			throw new RegistroNaoExisteException("Idioma '" + idiomaForm.nome() + "' já cadastrado no banco de dados!");
		}

		Idioma idiomaSalvo = new Idioma();
		idiomaSalvo.setNome(idiomaForm.nome());
		idiomaSalvo = idiomaRepo.save(idiomaSalvo);

		IdiomaDTO idiomaSalvoDTO = new IdiomaDTO(idiomaSalvo);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
		var uri = uriBuilder.path("/idioma/{id}").buildAndExpand(idiomaSalvo.getId()).toUri();

		return ResponseEntity.created(uri).body(idiomaSalvoDTO);
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
	
}
