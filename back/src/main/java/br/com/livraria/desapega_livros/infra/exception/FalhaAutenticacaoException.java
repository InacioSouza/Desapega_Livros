package br.com.livraria.desapega_livros.infra.exception;

public class FalhaAutenticacaoException extends RuntimeException{
    public FalhaAutenticacaoException(String mensagem) {
        super(mensagem);
    }
}
