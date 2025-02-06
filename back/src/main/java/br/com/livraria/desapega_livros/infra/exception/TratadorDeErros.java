package br.com.livraria.desapega_livros.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

	@ExceptionHandler(RegistroEncontradoException.class)
	public ResponseEntity<?> trataErroEncontrado(RegistroEncontradoException ex) {
		return ResponseEntity.status(HttpStatus.FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(NenhumRegistroEncontradoException.class)
	public ResponseEntity<?> tratarErroNenhumRegistroEncontrado(NenhumRegistroEncontradoException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(RegistroNaoExisteException.class)
	public ResponseEntity<?> tratarErroRegistroNaoExiste(RegistroNaoExisteException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(DadoInvalidoException.class)
	public ResponseEntity<?> tratarErroDadoInvalido(DadoInvalidoException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(FalhaNaCominucacaoComAPIException.class)
	public ResponseEntity<?> trataErroCominucacaoAPI(FalhaNaCominucacaoComAPIException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}
}
