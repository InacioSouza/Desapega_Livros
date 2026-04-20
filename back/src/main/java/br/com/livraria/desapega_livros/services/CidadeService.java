package br.com.livraria.desapega_livros.services;

import br.com.livraria.desapega_livros.controllers.dto.CidadeDTO;
import br.com.livraria.desapega_livros.controllers.form.CidadeFORM;
import br.com.livraria.desapega_livros.entities.Cidade;
import br.com.livraria.desapega_livros.entities.Estado;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.CidadeRepository;
import br.com.livraria.desapega_livros.repositories.EstadoRepository;
import br.com.livraria.desapega_livros.services.bases.BaseService;
import br.com.livraria.desapega_livros.services.bases.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CidadeService
		extends BaseServiceImpl<Cidade, Integer>
		implements BaseService<Cidade, Integer> {

	@Autowired
	private EstadoRepository estadoRepo;

	private CidadeRepository cidadeRepo;

	public CidadeService(CidadeRepository cidadeRepo) {
		super(cidadeRepo);
		this.cidadeRepo = cidadeRepo;
	}

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

		UriComponentsBuilder uribuilder = UriComponentsBuilder.newInstance();
		var uri = uribuilder.path("/cidade/{id}").buildAndExpand(cidade.getId()).toUri();

		return ResponseEntity.created(uri).body(new CidadeDTO(cidade));
	}

}
