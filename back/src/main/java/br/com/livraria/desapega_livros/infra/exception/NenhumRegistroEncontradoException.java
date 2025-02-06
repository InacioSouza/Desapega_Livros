package br.com.livraria.desapega_livros.infra.exception;

public class NenhumRegistroEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NenhumRegistroEncontradoException(String mensagem) {
		super(mensagem);
	}

}
