package br.com.livraria.desapega_livros.services;

import br.com.livraria.desapega_livros.controllers.dto.EnderecoDTO;
import br.com.livraria.desapega_livros.controllers.dto.ViaCepResponseDTO;
import br.com.livraria.desapega_livros.controllers.form.EnderecoFORM;
import br.com.livraria.desapega_livros.entities.Cidade;
import br.com.livraria.desapega_livros.entities.Endereco;
import br.com.livraria.desapega_livros.entities.Estado;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.CidadeRepository;
import br.com.livraria.desapega_livros.repositories.EnderecoRepository;
import br.com.livraria.desapega_livros.repositories.EstadoRepository;
import br.com.livraria.desapega_livros.repositories.UsuarioRepository;
import br.com.livraria.desapega_livros.services.bases.BaseService;
import br.com.livraria.desapega_livros.services.bases.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoService
		extends BaseServiceImpl<Endereco, Integer>
		implements BaseService <Endereco, Integer> {

	private EnderecoRepository enderecoRepo;

	@Autowired
	private EstadoRepository estadoRepo;

	@Autowired
	private CidadeRepository cidadeRepo;

	@Autowired
	private ValidaCepService validaCEP;

	@Autowired
	private UsuarioRepository usuarioRepo;

	public EnderecoService(EnderecoRepository enderecoRepo) {
		super(enderecoRepo);
		this.enderecoRepo = enderecoRepo;
	}

	@Transactional
	public Endereco cadastra(EnderecoFORM enderecoForm) {

		if (this.enderecoJaCadastrado(enderecoForm)) {
			throw new RegistroEncontradoException(
					"Endereço já cadastrado no banco de dados!");
		}

		ViaCepResponseDTO dadosEndereco = this.validaCEP
				.cepExistente(enderecoForm.cep());

		Endereco endereco = new Endereco(dadosEndereco);
		endereco.setNumero(enderecoForm.numero());
		endereco.setComplemento(enderecoForm.complemento());

		String nomeCidadeViaAPI = dadosEndereco.localidade().trim();
		Cidade cidade = cidadeRepo.findByNome(nomeCidadeViaAPI);

		if (cidade == null) {
			String nomeEstadoViaAPI = dadosEndereco.estado().trim();
			Estado estado = this.estadoRepo.findByNome(nomeEstadoViaAPI);

			if (estado == null ) {
				estado = estadoRepo.save(
						new Estado(nomeEstadoViaAPI, dadosEndereco.uf().trim() )
				);
			}
			cidade = this.cidadeRepo.save(new Cidade(nomeCidadeViaAPI, estado));
		}
		endereco.setCidade(cidade);

		return this.enderecoRepo.save(endereco);
	}

	private boolean enderecoJaCadastrado(EnderecoFORM enderecoF) {
		Integer idEndereco = enderecoRepo
				.existsByCepAndNumero(enderecoF.cep(), enderecoF.numero());
		return idEndereco != null;
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

	@Transactional
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
