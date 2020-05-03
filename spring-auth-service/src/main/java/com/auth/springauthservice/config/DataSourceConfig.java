package com.auth.springauthservice.config;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

public class DataSourceConfig {
    
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setName("usersdb")
            .addScript("classpath:org/springframework/security/core/userdetails/jdbc/users.ddl")
            .build();
    }
}