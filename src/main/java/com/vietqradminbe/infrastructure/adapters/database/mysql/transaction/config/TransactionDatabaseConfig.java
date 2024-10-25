package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.repositories"},
        entityManagerFactoryRef = "transactionV2EntityManagerFactory",
        transactionManagerRef = "transactionV2TransactionManager"
)
@EntityScan(basePackages = "com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.entities")
@PropertySource("classpath:application.properties")
public class TransactionDatabaseConfig {


    @Bean(name = "transactionV2DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.transaction-v2")
    public DataSourceProperties transactionV2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaProperties jpaProperties) {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(),
                jpaProperties.getProperties(),
                null
        );
    }

    @Bean(name = "transactionV2DataSource")
    public DataSource transactionV2DataSource(){
        return transactionV2DataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "transactionV2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean transactionV2EntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(transactionV2DataSource())
                .packages("com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.entities")
                .build();
    }

    @Bean(name = "transactionV2TransactionManager")
    public PlatformTransactionManager transactionV2TransactionManager(
            @Qualifier("transactionV2EntityManagerFactory") EntityManagerFactory transactionV2EntityManagerFactory) {
        return new JpaTransactionManager(transactionV2EntityManagerFactory);
    }
}
