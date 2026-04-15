package com.shalion.challenge.config;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public interface DatabaseConfig {

    DataSource dataSource(PostgresProperties postgresProperties);
    JdbcTemplate jdbcTemplate(DataSource dataSource);

}
