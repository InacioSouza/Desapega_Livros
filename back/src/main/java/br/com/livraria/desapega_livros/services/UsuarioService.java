package br.com.livraria.desapega_livros.services;

import br.com.livraria.desapega_livros.controllers.form.UsuarioFORM;
import br.com.livraria.desapega_livros.entities.Endereco;
import br.com.livraria.desapega_livros.entities.Usuario;
import br.com.livraria.desapega_livros.entities.enuns.StatusUsuario;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.EnderecoRepository;
import br.com.livraria.desapega_livros.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepo;

	@Autowired
	private EnderecoRepository enderecoRepo;

	@Autowired
	private EnderecoService enderecoService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private Endereco obtemEnderecoUsuario(UsuarioFORM usuarioForm) {

		if (usuarioForm.idEndereco() != null) {
			var optionalEndereco = enderecoRepo.findById(usuarioForm.idEndereco());

			if (optionalEndereco.isEmpty()) {
				throw new RegistroNaoExisteException(
						"Não existe endereço cadastrado para o id : " + usuarioForm.idEndereco());
			}
			return optionalEndereco.get();
		}

		boolean naoInformouDadosEndereco =  usuarioForm.endereco() == null ||
				(usuarioForm.endereco().cep() == null || usuarioForm.endereco().numero() == null);

		if(naoInformouDadosEndereco){
			throw new IllegalArgumentException(
					"Caso o id do endereco não seja informado os dados do mesmo devem ser informados!");
		}
		return this.enderecoService.cadastra(usuarioForm.endereco());
	}

	@Transactional
	public Usuario cadastrar(UsuarioFORM usuarioForm) {

		Usuario usuario = new Usuario(usuarioForm);

		var endereco = this.obtemEnderecoUsuario(usuarioForm);
		usuario.setEndereco(endereco);

		usuario.setStatus(StatusUsuario.ATIVO.toString());
		usuario.setSenha(this.passwordEncoder.encode(usuarioForm.senha()));

		return  this.usuarioRepo.save(usuario);
	}

	@Transactional
	public Usuario suspender(Integer idUsuario){

		if(!usuarioRepo.existsById(idUsuario)){
			throw new RegistroNaoExisteException(
					"Não existe usuário cadastrado para o id : " + idUsuario);
		}

		Usuario usuario = usuarioRepo.findById(idUsuario).get();
		usuario.setStatus(StatusUsuario.SUSPENSO.toString());

		return this.usuarioRepo.save(usuario);
	}

	@Transactional
	public Usuario inativar(Integer idUsuario){

		if(!usuarioRepo.existsById(idUsuario)){
			throw new RegistroNaoExisteException("Não existe usuário para o id : " + idUsuario);
		}

		Usuario usuario = usuarioRepo.findById(idUsuario).get();

		usuario.setStatus(StatusUsuario.INATIVO.toString());

		return  this.usuarioRepo.save(usuario);
	}
}
