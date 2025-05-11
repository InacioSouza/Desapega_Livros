package br.com.livraria.desapega_livros.infra.beans;

import java.net.http.HttpClient;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class criadorDeObjetos {

	@Bean
	public ObjectMapper beanObjectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public HttpClient beanHttpClient() {
		return HttpClient.newBuilder().build();
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper;
	}
}
