package br.com.livraria.desapega_livros.infra.exception;

public class RequisicaoInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequisicaoInvalidaException(String mensage) {
		super(mensage);
	}
	
}
