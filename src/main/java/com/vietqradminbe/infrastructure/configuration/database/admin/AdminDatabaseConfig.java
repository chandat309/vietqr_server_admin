package com.vietqradminbe.infrastructure.configuration.database.admin;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.vietqradminbe.domain.repositories",
        entityManagerFactoryRef = "adminEntityManagerFactory",
        transactionManagerRef = "adminTransactionManager"
)
@PropertySource("classpath:application.properties")
public class AdminDatabaseConfig {
    @Primary
    @Bean(name = "adminDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.admin")
    public DataSourceProperties adminDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "adminDataSource")
    public DataSource adminDataSource(){
        return adminDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "adminEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean adminEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(adminDataSource())
                .packages("com.vietqradminbe.domain.models")
                .build();
    }

    @Bean(name = "adminTransactionManager")
    public PlatformTransactionManager adminTransactionManager(
            @Qualifier("adminEntityManagerFactory") EntityManagerFactory adminEntityManagerFactory) {
        return new JpaTransactionManager(adminEntityManagerFactory);
    }
}
