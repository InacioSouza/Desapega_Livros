package br.com.livraria.desapega_livros.infra.exception;

public class RegistroEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegistroEncontradoException(String mensagem) {
		super(mensagem);
	}

}
