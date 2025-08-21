package com.codemechanix.cqrs.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.codemechanix.cqrs.command.repo",
        entityManagerFactoryRef = "writeEntityManagerFactoryBean",
        transactionManagerRef = "writeTxManager"
)
public class WriteDbConfig {

    @Bean(name = "writeEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean writeEntityManagerFactoryBean(
            @Qualifier("writeDataSource") DataSource ds) {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(ds);
        emfb.setPackagesToScan("com.codemechanix.cqrs.command.model");
        emfb.setPersistenceUnitName("write");
        emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfb.setJpaPropertyMap(Map.of(
                "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect",
                "hibernate.hbm2ddl.auto", "update"
        ));
        return emfb;
    }

    @Bean(name = "writeTxManager")
    public PlatformTransactionManager writeTxManager(
            @Qualifier("writeEntityManagerFactoryBean") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}