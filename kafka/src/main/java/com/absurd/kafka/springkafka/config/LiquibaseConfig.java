package com.absurd.kafka.springkafka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

import liquibase.exception.LiquibaseException;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka.config
 * @Description:
 * @date 2017/1/4 9:34
 */
@Configuration
public class LiquibaseConfig {
    @Autowired
    private DataSource dataSource;
    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
    public liquibase.integration.spring.SpringLiquibase springLiquibase() throws LiquibaseException {
        liquibase.integration.spring.SpringLiquibase liquibase = new liquibase.integration.spring.SpringLiquibase();
        liquibase.setChangeLog("classpath:db/db.changelog-master.xml");
        liquibase.setDataSource(dataSource);
        liquibase.setShouldRun(true);
        liquibase.setDropFirst(false);
        return liquibase;
    }

}
