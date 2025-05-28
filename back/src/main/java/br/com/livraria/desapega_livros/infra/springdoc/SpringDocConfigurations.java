package br.com.livraria.desapega_livros.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        var openAPI = new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));

        openAPI.info(new Info().title("Desapega Livros API")
                .description("Documentação da API de livraria compartilhada - Desapega Livros")
                .version("1.0")
                .contact(new Contact()
                        .name("INÁCIO SOUZA ROCHA")
                        .url("https://www.linkedin.com/in/inacio-souza/")));

        return openAPI;
    }


}
