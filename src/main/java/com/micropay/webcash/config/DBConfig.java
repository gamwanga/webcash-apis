package com.micropay.webcash.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

//@Configuration
public class DBConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        System.out.println("=================================Begin JDBC Assignment " + dbUrl);
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        HikariDataSource hikari = new HikariDataSource(config);
        System.out.println("=================================End JDBC Assignment");
        return  hikari;

    }
}
