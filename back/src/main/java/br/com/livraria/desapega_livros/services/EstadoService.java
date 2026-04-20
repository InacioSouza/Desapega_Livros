package br.com.livraria.desapega_livros.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.livraria.desapega_livros.controllers.dto.EstadoDTO;
import br.com.livraria.desapega_livros.controllers.form.EstadoFORM;
import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.EstadoRepository;
import br.com.livraria.desapega_livros.entities.Estado;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repository;

	@Transactional
	public ResponseEntity<?> cadastra(EstadoFORM estadoForm) {
		if (estadoJaCadastrado(estadoForm.nome())) {
			throw new RegistroEncontradoException("Estado já cadastrado no banco de dados");
		}

		Estado estadoSalvo = repository.save(new Estado(estadoForm));

		var id = estadoSalvo.getId();

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
		var uri = uriBuilder.path("/estado/{id}").buildAndExpand(id).toUri();

		return ResponseEntity.created(uri).body(new EstadoDTO(estadoSalvo));
	}

	private boolean estadoJaCadastrado(String estado) {
		return repository.existsByNomeIgnoreCase(estado);
	}

	@Transactional
	public ResponseEntity<Page<Estado>> listar(Pageable pagina) {
		Page<Estado> pageEstados = repository.findAll(pagina);

		if (pageEstados.getTotalElements() == 0) {
			throw new NenhumRegistroEncontradoException("Nenhum Estado foi encontrado no banco de dados");
		}

		return ResponseEntity.ok(pageEstados);
	}

	@Transactional
	public ResponseEntity<?> excluir(Integer id) {
		if (!repository.existsById(id)) {
			throw new RegistroNaoExisteException("Não existe Estado para o ID: " + id);
		}
		repository.deleteById(id);

		return ResponseEntity.ok("Estado de id " + id + " excluído com sucesso");
	}
}
