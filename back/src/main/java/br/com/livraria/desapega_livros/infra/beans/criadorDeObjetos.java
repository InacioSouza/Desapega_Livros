package br.com.livraria.desapega_livros.infra.beans;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class criadorDeObjetos {

	@Bean
	public UriComponentsBuilder beanUriComponentsBuilder() {
		return UriComponentsBuilder.newInstance();
	}

	@Bean
	public ObjectMapper beanObjectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public HttpClient beanHttpClient() {
		return HttpClient.newBuilder().build();
	}
}
