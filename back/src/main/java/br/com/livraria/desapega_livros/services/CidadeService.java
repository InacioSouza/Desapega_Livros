package br.com.livraria.desapega_livros.services;

import br.com.livraria.desapega_livros.controllers.dto.CidadeDTO;
import br.com.livraria.desapega_livros.controllers.form.CidadeFORM;
import br.com.livraria.desapega_livros.entities.Cidade;
import br.com.livraria.desapega_livros.entities.Estado;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.CidadeRepository;
import br.com.livraria.desapega_livros.services.bases.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CidadeService
		extends BaseServiceImpl<Cidade, Integer> {

	private EstadoService estadoService;

	private CidadeRepository cidadeRepo;

	public CidadeService(
			CidadeRepository cidadeRepo,
			EstadoService estadoService
			) {
		super(cidadeRepo);
		this.cidadeRepo = cidadeRepo;
		this.estadoService = estadoService;
	}

	@Transactional
	public ResponseEntity<?> cadastra(CidadeFORM cidadeForm) {
		if (this.cidadeRepo.existsByNomeIgnoreCase(cidadeForm.nome())) {
			throw new RegistroEncontradoException(
					cidadeForm.nome() + " já está cadastrada no banco de dados!");
		}

		if (!this.estadoService.existsById(cidadeForm.idEstado())) {
			throw new RegistroNaoExisteException(
					"Não existe Estado para o ID: " + cidadeForm.idEstado());
		}

		Cidade cidade = new Cidade();
		cidade.setNome(cidadeForm.nome().trim());

		Estado estadoCid = this.estadoService.findById(cidadeForm.idEstado());
		cidade.setEstado(estadoCid);

		cidade = this.cidadeRepo.save(cidade);

		UriComponentsBuilder uribuilder = UriComponentsBuilder.newInstance();
		var uri = uribuilder.path("/cidade/{id}").buildAndExpand(cidade.getId()).toUri();

		return ResponseEntity.created(uri).body(new CidadeDTO(cidade));
	}

	public Cidade findByNome(String nome) {
		return this.cidadeRepo.findByNome(nome);
	}

}
