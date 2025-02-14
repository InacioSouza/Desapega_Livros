package br.com.livraria.desapega_livros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.livraria.desapega_livros.controllers.dto.CidadeDTO;
import br.com.livraria.desapega_livros.controllers.form.CidadeFORM;
import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
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

	@Autowired
	private UriComponentsBuilder uribuilder;

	@Transactional
	public ResponseEntity<?> cadastra(CidadeFORM cidadeForm) {
		if (cidadeRepo.existsByNomeIgnoreCase(cidadeForm.nome())) {
			throw new RegistroEncontradoException(cidadeForm.nome() + " já está cadastrada no banco de dados!");
		}

		if (!estadoRepo.existsById(cidadeForm.idEstado())) {
			throw new RegistroNaoExisteException("Não existe Estado para o ID: " + cidadeForm.idEstado());
		}

		Cidade cidade = new Cidade();
		cidade.setNome(cidadeForm.nome().trim());

		Estado estadoCid = estadoRepo.findById(cidadeForm.idEstado()).get();
		cidade.setEstado(estadoCid);

		cidade = cidadeRepo.save(cidade);

		var uri = uribuilder.path("/cidade/{id}").buildAndExpand(cidade.getId()).toUri();

		return ResponseEntity.created(uri).body(new CidadeDTO(cidade));
	}

	@Transactional
	public ResponseEntity<?> listar(Pageable pagina) {

		var cidades = cidadeRepo.findAll(pagina).map(CidadeDTO::new);

		if (cidades.getNumberOfElements() == 0) {
			throw new NenhumRegistroEncontradoException("Não há cidades cadastradas no banco!");
		}

		return ResponseEntity.ok(cidades);
	}

}
