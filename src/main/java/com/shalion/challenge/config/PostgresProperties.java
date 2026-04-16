package com.shalion.challenge.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.datasource")
@Getter
@Setter
public class PostgresProperties {

    private String url = "jdbc:postgresql://localhost:5433/postgres";
    private String username = "postgres";
    private String password = "";
    private String driverClassName = "org.postgresql.Driver";

}
