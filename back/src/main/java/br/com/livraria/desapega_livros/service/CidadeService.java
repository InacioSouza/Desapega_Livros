package br.com.livraria.desapega_livros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.livraria.desapega_livros.controllers.form.CidadeFORM;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repository.CidadeRepository;
import br.com.livraria.desapega_livros.repository.EstadoRepository;
import br.com.livraria.desapega_livros.repository.entity.Cidade;
import br.com.livraria.desapega_livros.repository.entity.Estado;

@Service
public class CidadeService {

	@Autowired
	private EstadoRepository estadoRepo;

	@Autowired
	private CidadeRepository cidadeRepo;

	@Transactional
	public ResponseEntity<?> cadastra(CidadeFORM cidadeForm) {
		if (cidadeRepo.existsByNomeIgnoreCase(cidadeForm.nome())) {
			throw new RegistroEncontradoException(cidadeForm.nome() + " já está cadastrada no banco de dados!");
		}

		if (!estadoRepo.existsById(cidadeForm.idEstado())) {
			throw new RegistroNaoExisteException("Não existe Estado para o ID: " + cidadeForm.idEstado());
		}

		Cidade cidade = new Cidade();
		cidade.setNome(cidadeForm.nome());

		Estado estadoCid = estadoRepo.findById(cidadeForm.idEstado()).get();
		cidade.setEstado(estadoCid);

		return ResponseEntity.status(HttpStatus.CREATED).body(cidadeRepo.save(cidade));
	}

}
