package com.codemechanix.cqrs.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.codemechanix.cqrs.query.repo",
        entityManagerFactoryRef = "readEmf",
        transactionManagerRef   = "readTx"
)
public class ReadDbConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.read")
    public DataSourceProperties readDsProps() { return new DataSourceProperties(); }

    @Bean(name = "readDataSource")
    @ConfigurationProperties("spring.datasource.read.hikari")
    public DataSource readDataSource() {
        return readDsProps().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "readEmf")
    public LocalContainerEntityManagerFactoryBean readEmf() {
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(readDataSource());
        em.setPackagesToScan("com.codemechanix.cqrs.query.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(Map.of("hibernate.hbm2ddl.auto", "none"));
        return em;
    }

    @Bean(name = "readTx")
    public JpaTransactionManager readTx(
            @Qualifier("readEmf") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(Objects.requireNonNull(emf.getObject()));
    }
}
