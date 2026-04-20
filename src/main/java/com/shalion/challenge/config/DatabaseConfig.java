package com.shalion.challenge.config;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public interface DatabaseConfig {

    /**
     * Creates the application data source from PostgreSQL properties.
     *
     * @param postgresProperties PostgreSQL connection properties
     * @return configured data source
     */
    DataSource dataSource(PostgresProperties postgresProperties);

    /**
     * Creates a JDBC template bound to the configured data source.
     *
     * @param dataSource application data source
     * @return JDBC template instance
     */
    JdbcTemplate jdbcTemplate(DataSource dataSource);

}
