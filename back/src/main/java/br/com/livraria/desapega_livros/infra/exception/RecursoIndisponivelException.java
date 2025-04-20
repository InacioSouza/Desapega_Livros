package br.com.livraria.desapega_livros.infra.exception;

public class RecursoIndisponivelException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RecursoIndisponivelException(String mensagem) {
		super(mensagem);
	}

}
