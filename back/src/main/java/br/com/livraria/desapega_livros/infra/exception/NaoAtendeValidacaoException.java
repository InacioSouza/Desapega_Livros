package br.com.livraria.desapega_livros.infra.exception;

public class NaoAtendeValidacaoException extends  RuntimeException {
    public NaoAtendeValidacaoException(String message) {
        super(message);
    }
}
