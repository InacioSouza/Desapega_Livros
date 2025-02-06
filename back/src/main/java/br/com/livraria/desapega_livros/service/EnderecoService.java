package br.com.livraria.desapega_livros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.livraria.desapega_livros.controllers.dto.EnderecoDTO;
import br.com.livraria.desapega_livros.controllers.dto.ViaCepResponseDTO;
import br.com.livraria.desapega_livros.controllers.form.EnderecoFORM;
import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repository.CidadeRepository;
import br.com.livraria.desapega_livros.repository.EnderecoRepository;
import br.com.livraria.desapega_livros.repository.EstadoRepository;
import br.com.livraria.desapega_livros.repository.UsuarioRepository;
import br.com.livraria.desapega_livros.repository.entity.Cidade;
import br.com.livraria.desapega_livros.repository.entity.Endereco;
import br.com.livraria.desapega_livros.repository.entity.Estado;

@Service
public class EnderecoService {
	@Autowired
	private EnderecoRepository enderecoRepo;

	@Autowired
	private EstadoRepository estadoRepo;

	@Autowired
	private CidadeRepository cidadeRepo;

	@Autowired
	private ValidaCepService validaCEP;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Transactional
	public ResponseEntity<?> cadastra(EnderecoFORM enderecoForm) {

		if (enderecoJaCadastrado(enderecoForm) > 0) {
			throw new RegistroEncontradoException("Endereço já cadastrado no banco de dados!");
		}

		ViaCepResponseDTO dadosEndereco = this.validaCEP.cepExistente(enderecoForm.cep());

		Endereco endereco = new Endereco(dadosEndereco);
		endereco.setNumero(enderecoForm.numero());
		endereco.setComplemento(enderecoForm.complemento());

		Cidade cidade;
		Estado estado;

		if ((enderecoForm.idCidade() != null && !cidadeRepo.existsById(enderecoForm.idCidade()))
				|| !cidadeRepo.existsByNomeIgnoreCase(dadosEndereco.localidade())) {

			cidade = new Cidade();
			cidade.setNome(dadosEndereco.localidade());

			if (!estadoRepo.existsByNomeIgnoreCase(dadosEndereco.localidade())) {
				estado = new Estado();
				estado.setNome(dadosEndereco.estado());
				estado.setUf(dadosEndereco.uf());

				estado = estadoRepo.save(estado);
			} else {
				estado = estadoRepo.findByNome(dadosEndereco.estado());
			}

			cidade.setEstado(estado);

			cidade = cidadeRepo.save(cidade);
		} else {
			cidade = cidadeRepo.findByNome(dadosEndereco.localidade());
		}

		endereco.setCidade(cidade);

		EnderecoDTO enderecoSalvoDTO = new EnderecoDTO(enderecoRepo.save(endereco));

		return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSalvoDTO);
	}

	private int enderecoJaCadastrado(EnderecoFORM enderecoF) {
		Integer idEndereco = enderecoRepo.existsByCepAndNumero(enderecoF.cep(), enderecoF.numero());
		return (idEndereco == null) ? -1 : idEndereco;
	}

	@Transactional
	public ResponseEntity<Page<EnderecoDTO>> listar(Pageable pagina) {

		var enderecos = enderecoRepo.findAll(pagina).map(EnderecoDTO::new);

		if (enderecos.getTotalElements() == 0) {
			throw new NenhumRegistroEncontradoException("Ainda não há endereços cadastrados no banco de dados");
		}

		return ResponseEntity.ok(enderecos);
	}

	@Transactional
	public ResponseEntity<?> atualizar(Integer id, EnderecoFORM enderecoForm) {

		if (!enderecoRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Não foi encontrado endereço cadastrado no banco para o id " + id);
		}

		Endereco endereco = enderecoRepo.findById(id).get();

		var dadosPorCep = validaCEP.cepExistente(enderecoForm.cep());

		endereco.setCep(dadosPorCep.cep());
		endereco.setLogradouro(dadosPorCep.logradouro());
		endereco.setBairro(dadosPorCep.bairro());

		Cidade cidade = cidadeRepo.findByNome(dadosPorCep.localidade());
		Estado estado;

		if (cidade == null) {
			cidade = new Cidade();
			cidade.setNome(dadosPorCep.localidade());

			estado = estadoRepo.findByNome(dadosPorCep.estado());

			if (estado == null) {
				estado.setNome(dadosPorCep.estado());
				estado.setUf(dadosPorCep.uf());

				estadoRepo.save(estado);
			}

			cidade.setEstado(estado);
			cidade = cidadeRepo.save(cidade);
		}
		endereco.setCidade(cidade);

		if (enderecoForm.numero() != null) {
			endereco.setNumero(enderecoForm.numero());
		}

		if (enderecoForm.complemento() != null) {
			endereco.setComplemento(enderecoForm.complemento());
		}

		return ResponseEntity.ok(new EnderecoDTO(endereco));
	}

	public ResponseEntity<?> enderecoPorUsuario(Integer id) {

		if (!usuarioRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Nenhum usuário encontrado para o id : " + id);
		}

		Endereco endereco = usuarioRepo.getEnderecoUsuario(id);

		if (endereco == null) {
			throw new RegistroNaoExisteException("O Usuário de id " + id + " não possui endereço cadastrado");
		}

		EnderecoDTO enderecoDTO = new EnderecoDTO(endereco);

		return ResponseEntity.ok(enderecoDTO);
	}
}
