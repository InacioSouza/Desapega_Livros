package br.com.livraria.desapega_livros.infra.exception;

public class FalhaNaCominucacaoComAPIException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FalhaNaCominucacaoComAPIException(String mensagem) {
		super(mensagem);
	}

}
