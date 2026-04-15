package com.shalion.challenge.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.datasource")
@Getter
public class PostgresProperties {

    @Value("${app.datasource.url}")
    private String url;
    @Value("${app.datasource.username}")
    private String username;
    @Value("${app.datasource.password}")
    private String password;
    @Value("${app.datasource.driver-class-name}")
    private String driverClassName;

}
