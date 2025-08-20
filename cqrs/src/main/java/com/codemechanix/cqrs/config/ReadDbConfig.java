package com.codemechanix.cqrs.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.codemechanix.cqrs.read.repo",
        entityManagerFactoryRef = "readEmf",
        transactionManagerRef = "readTx"
)
public class ReadDbConfig {

    @Bean
    public DataSource readDs(
            @Value("${app.datasource.read.url}") String url,
            @Value("${app.datasource.read.username}") String user,
            @Value("${app.datasource.read.password}") String pass) {
        var ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(url); ds.setUsername(user); ds.setPassword(pass);
        return ds;
    }

    @Bean(name = "readEmf")
    public LocalContainerEntityManagerFactoryBean readEmf(DataSource readDs) {
        var vendor = new HibernateJpaVendorAdapter();
        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(readDs);
        emf.setPackagesToScan("com.codemechanix.cqrs.read.view");
        emf.setJpaVendorAdapter(vendor);
        emf.setPersistenceUnitName("readPU");
        return emf;
    }

    @Bean(name = "readTx")
    public PlatformTransactionManager readTx(
            @Qualifier("readEmf") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
