package br.com.livraria.desapega_livros.service;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.beans.factory.annotation.Autowired;
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

		// O usuário pode solicitar apenas dois livros por mês!

		if (!usuarioRepo.existsById(solicitacaoForm.idSolicitante())) {
			throw new RequisicaoInvalidaException(
					"Não há usuário cadastrado para o id:" + solicitacaoForm.idSolicitante());
		}

		// Pesquisar por alguma forma de acessar um calendário no java para descobrir o
		// mês atual do sistema e pegar o primeiro e último dia do mês
		// a partir disso vou fazer a Query

		if (!livroRepo.existsById(solicitacaoForm.idLivro())) {
			throw new RequisicaoInvalidaException("Não há livro cadastrado para o id:" + solicitacaoForm.idLivro());
		}

		if (livroRepo.statusLivro(solicitacaoForm.idLivro()) != StatusLivro.DISPONIVEL) {
			throw new RecursoIndisponivelException("O livro solicitado não está disponível!");
		}

		return null;
	}

	private Integer verificaQtdSolicitacaoNomes() {
		YearMonth mesAtual = YearMonth.now();

		LocalDate primeiroDiaMes = mesAtual.atDay(1);
		LocalDate ultimoDiaMes = mesAtual.atEndOfMonth();
		
		// Integer solicitacoesMes = solicitacaoRepo.solicitacoesNoPeriodo(LocalDate primeiroDiaMes, ultimoDiaMes);
		
		return 0;
	}

}
