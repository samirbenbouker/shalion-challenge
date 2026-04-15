package com.shalion.challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI challengeOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Schools & Students API")
                        .description("REST API for managing schools and students")
                        .version("1.0.0")
                        .contact(new Contact().name("Challenge API"))
                        .license(new License().name("MIT")));
    }
}
