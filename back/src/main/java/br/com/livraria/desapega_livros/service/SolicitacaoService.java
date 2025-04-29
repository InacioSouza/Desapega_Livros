package br.com.livraria.desapega_livros.service;

import java.time.LocalDate;
import java.time.YearMonth;

import br.com.livraria.desapega_livros.infra.exception.NaoAtendeValidacaoException;
import br.com.livraria.desapega_livros.repository.entity.Livro;
import br.com.livraria.desapega_livros.repository.entity.Solicitacao;
import br.com.livraria.desapega_livros.repository.entity.enuns.StatusSolicitacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.livraria.desapega_livros.controllers.form.SolicitacaoFORM;
import br.com.livraria.desapega_livros.infra.exception.RecursoIndisponivelException;
import br.com.livraria.desapega_livros.infra.exception.RequisicaoInvalidaException;
import br.com.livraria.desapega_livros.repository.LivroRepository;
import br.com.livraria.desapega_livros.repository.SolicitacaoRepository;
import br.com.livraria.desapega_livros.repository.UsuarioRepository;
import br.com.livraria.desapega_livros.repository.entity.enuns.StatusLivro;
import jakarta.validation.Valid;

@Service
public class SolicitacaoService {

	@Autowired
	private SolicitacaoRepository solicitacaoRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private LivroRepository livroRepo;

	@Transactional
	public ResponseEntity<?> cadastrar(@Valid SolicitacaoFORM solicitacaoForm) {

		if (!usuarioRepo.existsById(solicitacaoForm.idSolicitante())) {
			throw new RequisicaoInvalidaException(
					"Não há usuário cadastrado para o id:" + solicitacaoForm.idSolicitante());
		}

		if (!livroRepo.existsById(solicitacaoForm.idLivro())) {
			throw new RequisicaoInvalidaException("Não há livro cadastrado para o id:" + solicitacaoForm.idLivro());
		}

		if (livroRepo.statusLivro(solicitacaoForm.idLivro()) != StatusLivro.DISPONIVEL) {
			throw new RecursoIndisponivelException("O livro solicitado não está disponível!");
		}

		if(solicitacaoMesPorUsuario(solicitacaoForm.idSolicitante()) >= 2) {
			throw new NaoAtendeValidacaoException("O usuário atingiu a quantidade máxima de solicitações no mês");
		}

		Livro livroSolicitado = livroRepo.findById(solicitacaoForm.idLivro()).get();

		if(livroSolicitado.getDono().getId() == solicitacaoForm.idSolicitante()) {
			throw new NaoAtendeValidacaoException("O usuário não pode solicitar um livro que ele mesmo está doando!");
		}

		Solicitacao solicitacao = new Solicitacao();
		solicitacao.setLivro(livroSolicitado);
		solicitacao.setUsuario(usuarioRepo.findById(solicitacaoForm.idSolicitante()).get());
		solicitacao.setStatus(StatusSolicitacao.AGUARDANDO_APROVACAO);

		return ResponseEntity.ok(solicitacaoRepo.save(solicitacao));
	}

	@Transactional
	private ResponseEntity<?> cancelarSolicitacao(Integer idSolicitacao) {
		if(!solicitacaoRepo.existsById(idSolicitacao)) {
			throw  new RequisicaoInvalidaException("Não existe solicitação cadastrada para o id: " + idSolicitacao);
		}

		Solicitacao solicitacao = solicitacaoRepo.findById(idSolicitacao).get();
		solicitacao.setStatus(StatusSolicitacao.CANCELADA);

		return ResponseEntity.status(HttpStatus.OK).body(solicitacaoRepo.save(solicitacao));
	}

	@Transactional
	private ResponseEntity<?> negarSolicitacao(Integer idSolicitacao) {
		if(!solicitacaoRepo.existsById(idSolicitacao)) {
			throw  new RequisicaoInvalidaException("Não existe solicitação cadastrada para o id: " + idSolicitacao);
		}

		Solicitacao solicitacao = solicitacaoRepo.findById(idSolicitacao).get();
		solicitacao.setStatus(StatusSolicitacao.NEGADA);

		return ResponseEntity.status(HttpStatus.OK).body(solicitacaoRepo.save(solicitacao));
	}

	@Transactional
	private ResponseEntity<?> aprovarSolicitacao(Integer idSolicitacao) {
		if(!solicitacaoRepo.existsById(idSolicitacao)) {
			throw  new RequisicaoInvalidaException("Não existe solicitação cadastrada para o id: " + idSolicitacao);
		}

		Solicitacao solicitacao = solicitacaoRepo.findById(idSolicitacao).get();
		solicitacao.setStatus(StatusSolicitacao.APROVADA);

		return ResponseEntity.status(HttpStatus.OK).body(solicitacaoRepo.save(solicitacao));
	}

	//Preciso criar um mecanismo para mudar o status da solicitacao para CANCELADA caso a aprovação demore 30 dias

	private Integer solicitacaoMesPorUsuario(Integer idUsuario) {
		YearMonth mesAtual = YearMonth.now();

		LocalDate primeiroDiaMes = mesAtual.atDay(1);
		LocalDate ultimoDiaMes = mesAtual.atEndOfMonth();
		
		Integer solicitacoesMes = solicitacaoRepo.solicitacoesNoPeriodo(idUsuario, primeiroDiaMes, ultimoDiaMes);
		
		return solicitacoesMes;
	}

}
