package com.shalion.challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String OPEN_API_TITLE = "Schools & Students API";
    private static final String OPEN_API_DESCRIPTION = "REST API for managing schools and students";
    private static final String OPEN_API_VERSION = "1.0.0";
    private static final String OPEN_API_CONTACT = "Challenge API";
    private static final String OPEN_API_LICENSE = "MIT";

    /**
     * Creates the OpenAPI definition shown in Swagger UI.
     *
     * @return OpenAPI metadata for the challenge API
     */
    @Bean
    public OpenAPI challengeOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title(OPEN_API_TITLE)
                        .description(OPEN_API_DESCRIPTION)
                        .version(OPEN_API_VERSION)
                        .contact(new Contact().name(OPEN_API_CONTACT))
                        .license(new License().name(OPEN_API_LICENSE)));
    }
}
