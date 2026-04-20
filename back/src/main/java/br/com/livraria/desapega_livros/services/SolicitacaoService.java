package br.com.livraria.desapega_livros.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import br.com.livraria.desapega_livros.controllers.dto.SolicitacaoDTO;
import br.com.livraria.desapega_livros.infra.exception.NaoAtendeValidacaoException;
import br.com.livraria.desapega_livros.entities.Livro;
import br.com.livraria.desapega_livros.entities.Solicitacao;
import br.com.livraria.desapega_livros.entities.enuns.StatusSolicitacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.livraria.desapega_livros.controllers.form.SolicitacaoFORM;
import br.com.livraria.desapega_livros.infra.exception.RecursoIndisponivelException;
import br.com.livraria.desapega_livros.infra.exception.RequisicaoInvalidaException;
import br.com.livraria.desapega_livros.repositories.LivroRepository;
import br.com.livraria.desapega_livros.repositories.SolicitacaoRepository;
import br.com.livraria.desapega_livros.repositories.UsuarioRepository;
import br.com.livraria.desapega_livros.entities.enuns.StatusLivro;

@Service
public class SolicitacaoService {

	@Autowired
	private SolicitacaoRepository solicitacaoRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private LivroRepository livroRepo;

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public ResponseEntity<?> cadastrar(SolicitacaoFORM solicitacaoForm) {

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

		if(qtdSolicitacaoMesPorUsuario(solicitacaoForm.idSolicitante()) >= 2) {
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
		solicitacao.setDataSolicitacao(LocalDate.now());

		livroSolicitado.setStatus(StatusLivro.SOLICITADO.toString());

		SolicitacaoDTO solicitacaoSalvaDTO = new SolicitacaoDTO(solicitacaoRepo.save(solicitacao));

		return ResponseEntity.ok(solicitacaoSalvaDTO);
	}

	@Transactional
	public ResponseEntity<?> cancelarSolicitacao(Integer idSolicitacao) {
		if(!solicitacaoRepo.existsById(idSolicitacao)) {
			throw  new RequisicaoInvalidaException("Não existe solicitação cadastrada para o id: " + idSolicitacao);
		}

		Solicitacao solicitacao = solicitacaoRepo.findById(idSolicitacao).get();
		solicitacao.setStatus(StatusSolicitacao.CANCELADA);

		return ResponseEntity.status(HttpStatus.OK).body(solicitacaoRepo.save(solicitacao));
	}

	@Transactional
	public ResponseEntity<?> negarSolicitacao(Integer idSolicitacao) {
		if(!solicitacaoRepo.existsById(idSolicitacao)) {
			throw  new RequisicaoInvalidaException("Não existe solicitação cadastrada para o id: " + idSolicitacao);
		}

		Solicitacao solicitacao = solicitacaoRepo.findById(idSolicitacao).get();
		solicitacao.setStatus(StatusSolicitacao.NEGADA);

		return ResponseEntity.status(HttpStatus.OK).body(solicitacaoRepo.save(solicitacao));
	}

	@Transactional
	public ResponseEntity<?> aprovarSolicitacao(Integer idSolicitacao) {
		if(!solicitacaoRepo.existsById(idSolicitacao)) {
			throw  new RequisicaoInvalidaException("Não existe solicitação cadastrada para o id: " + idSolicitacao);
		}

		Solicitacao solicitacao = solicitacaoRepo.findById(idSolicitacao).get();
		solicitacao.setStatus(StatusSolicitacao.APROVADA);

		return ResponseEntity.status(HttpStatus.OK).body(solicitacaoRepo.save(solicitacao));
	}

	private Integer qtdSolicitacaoMesPorUsuario(Integer idUsuario) {
		YearMonth mesAtual = YearMonth.now();

		LocalDate primeiroDiaMes = mesAtual.atDay(1);
		LocalDate ultimoDiaMes = mesAtual.atEndOfMonth();
		
		Integer solicitacoesMes = solicitacaoRepo.solicitacoesNoPeriodo(idUsuario, primeiroDiaMes, ultimoDiaMes);
		
		return solicitacoesMes;
	}

	private void verificaTempoDaSolicitacao(){

	}

	@Scheduled(fixedDelay = 172800000) // 48h
	@Transactional
	public void alteraStatusDaSolicitacaoPorTempoDeEspera(){
		TypedQuery<Solicitacao> querySolicitacoes = em.createNamedQuery("solicitacoes.status", Solicitacao.class);
		querySolicitacoes.setParameter("statusSolicitacao", StatusSolicitacao.AGUARDANDO_APROVACAO);

		List<Solicitacao> solicitacoesEmAguardo = querySolicitacoes.getResultList();

		List<Solicitacao> solicitacoesExpiradas = new ArrayList<>();

		solicitacoesEmAguardo.forEach(solicitacao -> {

			long dias = ChronoUnit.DAYS.between(solicitacao.getDataSolicitacao(), LocalDate.now());

			if(dias > 30){
				solicitacao.setStatus(StatusSolicitacao.EXPIRADA);
				solicitacoesExpiradas.add(solicitacao);
			}
		});

		solicitacoesExpiradas.forEach(solicitacaoExpirada -> {
			solicitacaoRepo.save(solicitacaoExpirada);
		});
	}
}
