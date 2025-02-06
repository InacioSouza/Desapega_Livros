package br.com.livraria.desapega_livros.infra.exception;

public class RegistroNaoExisteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegistroNaoExisteException(String mensagem) {
		super(mensagem);
	}
}
