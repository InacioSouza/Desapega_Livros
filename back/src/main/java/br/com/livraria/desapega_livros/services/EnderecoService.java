package br.com.livraria.desapega_livros.services;

import br.com.livraria.desapega_livros.controllers.dto.EnderecoDTO;
import br.com.livraria.desapega_livros.controllers.dto.ViaCepResponseDTO;
import br.com.livraria.desapega_livros.controllers.form.EnderecoFORM;
import br.com.livraria.desapega_livros.entities.Cidade;
import br.com.livraria.desapega_livros.entities.Endereco;
import br.com.livraria.desapega_livros.entities.Estado;
import br.com.livraria.desapega_livros.entities.Usuario;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.EnderecoRepository;
import br.com.livraria.desapega_livros.repositories.UsuarioRepository;
import br.com.livraria.desapega_livros.services.bases.BaseServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EnderecoService
		extends BaseServiceImpl<Endereco, Integer> {

	private EnderecoRepository enderecoRepo;

	private EstadoService estadoService;

	private CidadeService cidadeService;

	private ValidaCepService validaCEP;

	private UsuarioRepository usuarioRepository;

	public EnderecoService(
			EnderecoRepository enderecoRepo,
			EstadoService estadoService,
	 		CidadeService cidadeService,
	 		ValidaCepService validaCEP,
	 		UsuarioRepository usuarioService) {

		super(enderecoRepo);
		this.enderecoRepo = enderecoRepo;
		this.estadoService = estadoService;
		this.cidadeService = cidadeService;
		this.validaCEP = validaCEP;
		this.usuarioRepository = usuarioService;
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
		Cidade cidade = this.cidadeService.findByNome(nomeCidadeViaAPI);

		if (cidade == null) {
			String nomeEstadoViaAPI = dadosEndereco.estado().trim();
			Estado estado = this.estadoService.findByNome(nomeEstadoViaAPI);

			if (estado == null ) {
				estado = this.estadoService.simpleSave(
						new Estado(nomeEstadoViaAPI, dadosEndereco.uf().trim() )
				);
			}
			cidade = this.cidadeService.simpleSave(new Cidade(nomeCidadeViaAPI, estado));
		}
		endereco.setCidade(cidade);

		return this.enderecoRepo.save(endereco);
	}

	private boolean enderecoJaCadastrado(EnderecoFORM enderecoF) {
		Integer idEndereco = this.enderecoRepo
				.existsByCepAndNumero(enderecoF.cep(), enderecoF.numero());
		return idEndereco != null;
	}

	@Transactional
	public ResponseEntity<?> atualizar(Integer id, EnderecoFORM enderecoForm) {

		if (!enderecoRepo.existsById(id)) {
			throw new RegistroNaoExisteException(
					"Não foi encontrado endereço cadastrado no banco para o id " + id);
		}

		Endereco endereco = enderecoRepo.findById(id).get();

		var dadosPorCep = validaCEP.cepExistente(enderecoForm.cep());

		endereco.setCep(dadosPorCep.cep());
		endereco.setLogradouro(dadosPorCep.logradouro());
		endereco.setBairro(dadosPorCep.bairro());

		Cidade cidade = this.cidadeService.findByNome(dadosPorCep.localidade());
		Estado estado;

		if (cidade == null) {
			cidade = new Cidade();
			cidade.setNome(dadosPorCep.localidade());

			estado = this.estadoService.findByNome(dadosPorCep.estado());

			if (estado == null) {
				estado = new Estado();
				estado.setNome(dadosPorCep.estado());
				estado.setUf(dadosPorCep.uf());

				this.estadoService.simpleSave(estado);
			}

			cidade.setEstado(estado);
			cidade = this.cidadeService.simpleSave(cidade);
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
		Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(id);
		if (usuarioOptional.isEmpty()) {
			throw new RegistroNaoExisteException(
					"Nenhum usuário encontrado para o id : " + id);
		}

		Endereco endereco = usuarioOptional.get().getEndereco();

		if (endereco == null) {
			throw new RegistroNaoExisteException(
					"O Usuário de id " + id + " não possui endereço cadastrado");
		}

		EnderecoDTO enderecoDTO = new EnderecoDTO(endereco);

		return ResponseEntity.ok(enderecoDTO);
	}
}
