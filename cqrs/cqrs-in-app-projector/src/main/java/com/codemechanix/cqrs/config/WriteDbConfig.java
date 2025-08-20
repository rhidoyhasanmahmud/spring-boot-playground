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
        basePackages = "com.codemechanix.cqrs.write.repo",
        entityManagerFactoryRef = "writeEmf",
        transactionManagerRef = "writeTx"
)
public class WriteDbConfig {

    @Bean
    public DataSource writeDs(
            @Value("${app.datasource.write.url}") String url,
            @Value("${app.datasource.write.username}") String user,
            @Value("${app.datasource.write.password}") String pass) {
        var ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(url); ds.setUsername(user); ds.setPassword(pass);
        return ds;
    }

    @Bean(name = "writeEmf")
    public LocalContainerEntityManagerFactoryBean writeEmf(DataSource writeDs) {
        var vendor = new HibernateJpaVendorAdapter();
        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(writeDs);
        emf.setPackagesToScan("com.codemechanix.cqrs.write.domain");
        emf.setJpaVendorAdapter(vendor);
        emf.setPersistenceUnitName("writePU");
        return emf;
    }

    @Bean(name = "writeTx")
    public PlatformTransactionManager writeTx(
            @Qualifier("writeEmf") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
