package com.absurd.kafka.springkafka.config;

import com.alibaba.druid.pool.DruidDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2016/5/20.
 */
@Configuration
@EnableTransactionManagement
public class DruidConfiguration {


    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.max-wait}")
    private Long maxWaitMillis;

    @Value("${spring.datasource.max-active}")
    private Integer maxActive;

    @Value("${spring.datasource.test-on-borrow}")
    private Boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    private Boolean testOnReturn;

    @Value("${spring.datasource.testWhileIdle}")
    private Boolean testWhileIdle;

    @Value("${spring.datasource.initialSize}")
    private Integer initialSize;

    @Value("${spring.datasource.minIdle}")
    private Integer minIdle;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private Integer maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.filters}")
    private String filters;

    @Value("${spring.datasource.connectionProperties}")
    private Properties connectionProperties;

    @Bean
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setMaxWait(maxWaitMillis);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setTestOnReturn(testOnReturn);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTimeBetweenConnectErrorMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        druidDataSource.setConnectProperties(connectionProperties);
        try {
            druidDataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }


//    @Autowired
//    private DataSourceProperties dataSourceProperties;
//
//    @Bean
////    @Order(1)
////    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        DataSourceBuilder factory = DataSourceBuilder
//                .create(dataSourceProperties.getClassLoader())
//                .driverClassName(dataSourceProperties.getDriverClassName())
//                .url(dataSourceProperties.getUrl())
//                .username(dataSourceProperties.getUsername())
//                .password(dataSourceProperties.getPassword());
//        return factory.build();
//    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());

    }


}
