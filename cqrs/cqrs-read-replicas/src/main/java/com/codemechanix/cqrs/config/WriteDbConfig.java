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
        basePackages = "com.codemechanix.cqrs.command.repo",
        entityManagerFactoryRef = "writeEmf",
        transactionManagerRef   = "writeTx"
)
public class WriteDbConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.write")
    public DataSourceProperties writeDsProps() { return new DataSourceProperties(); }

    @Bean(name = "writeDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.write.hikari")
    public DataSource writeDataSource() {
        return writeDsProps().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "writeEmf")
    @Primary
    public LocalContainerEntityManagerFactoryBean writeEmf() {
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(writeDataSource());
        em.setPackagesToScan("com.codemechanix.cqrs.command.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(Map.of("hibernate.hbm2ddl.auto", "none"));
        return em;
    }

    @Bean(name = "writeTx")
    @Primary
    public JpaTransactionManager writeTx(
            @Qualifier("writeEmf") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(Objects.requireNonNull(emf.getObject()));
    }
}
