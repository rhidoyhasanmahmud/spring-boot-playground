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
        basePackages = "com.codemechanix.cqrs.query.repo",
        entityManagerFactoryRef = "readEntityManagerFactoryBean",
        transactionManagerRef = "readTxManager"
)
public class ReadDbConfig {

    @Bean(name = "readEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean readEntityManagerFactoryBean(
            @Qualifier("readDataSource") DataSource ds) {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(ds);
        emfb.setPackagesToScan("com.codemechanix.cqrs.query.model");
        emfb.setPersistenceUnitName("read");
        emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfb.setJpaPropertyMap(Map.of(
                "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect",
                "hibernate.hbm2ddl.auto", "update"
        ));
        return emfb;
    }

    @Bean(name = "readTxManager")
    public PlatformTransactionManager readTxManager(
            @Qualifier("readEntityManagerFactoryBean") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}