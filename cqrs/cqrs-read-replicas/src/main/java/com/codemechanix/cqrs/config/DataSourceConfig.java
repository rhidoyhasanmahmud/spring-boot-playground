package com.codemechanix.cqrs.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "writeDataSource")
    public DataSource writeDataSource(
            @Value("${spring.datasource.write.jdbc-url}") String jdbcUrl,
            @Value("${spring.datasource.write.username}") String username,
            @Value("${spring.datasource.write.password}") String password,
            @Value("${spring.datasource.write.driver-class-name}") String driverClassName) {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean(name = "readDataSource")
    public DataSource readDataSource(
            @Value("${spring.datasource.read.jdbc-url}") String jdbcUrl,
            @Value("${spring.datasource.read.username}") String username,
            @Value("${spring.datasource.read.password}") String password,
            @Value("${spring.datasource.read.driver-class-name}") String driverClassName) {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }
}