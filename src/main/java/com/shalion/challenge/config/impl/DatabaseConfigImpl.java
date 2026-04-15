package com.shalion.challenge.config.impl;

import javax.sql.DataSource;

import com.shalion.challenge.config.DatabaseConfig;
import com.shalion.challenge.config.PostgresProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseConfigImpl implements DatabaseConfig {

    @Override
    @Bean
    public DataSource dataSource(PostgresProperties postgresProperties) {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(postgresProperties.getUrl())
                .username(postgresProperties.getUsername())
                .password(postgresProperties.getPassword())
                .driverClassName(postgresProperties.getDriverClassName())
                .build();
    }

    @Override
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
