package br.com.livraria.desapega_livros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DesapegaLivrosApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesapegaLivrosApplication.class, args);
	}
}
