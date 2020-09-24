package io.github.lprakashv.springbulkupsert;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "entityManager", basePackages = {
        "io.github.lprakashv.springbulkupsert" }, transactionManagerRef = "transactionManager")
@EntityScan(basePackages = { "io.github.lprakashv.springbulkupsert" })
public class Config {

    @Bean
    public DataSource dataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setURL("jdbc:sqlserver://localhost:1433");
        ds.setDatabaseName("testing");
        ds.setPassword("testing1234");
        ds.setUser("sa");
        ds.setUseBulkCopyForBatchInsert(true);

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setDataSource(dataSource);
        lcemfb.setPackagesToScan("io.github.lprakashv.springbulkupsert");
        lcemfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
        properties.put("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
        lcemfb.setJpaPropertyMap(properties);
        return lcemfb;
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean lcemfb) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(lcemfb.getObject());
        return transactionManager;
    }
}