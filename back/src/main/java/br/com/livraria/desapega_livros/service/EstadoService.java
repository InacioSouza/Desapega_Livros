package br.com.livraria.desapega_livros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.livraria.desapega_livros.controllers.form.EstadoFORM;
import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repository.EstadoRepository;
import br.com.livraria.desapega_livros.repository.entity.Estado;

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

		return ResponseEntity.status(HttpStatus.CREATED).body(estadoSalvo);
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
