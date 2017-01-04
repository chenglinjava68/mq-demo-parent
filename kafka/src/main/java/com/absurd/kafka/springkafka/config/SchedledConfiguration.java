package com.absurd.kafka.springkafka.config;

import com.absurd.kafka.springkafka.thread.ProductJob;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.ParseException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;


/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka.config
 * @Description:
 * @date 2017/1/3 10:48
 */
@Configuration
//@Lazy
@ConditionalOnProperty(name = "quartz.enabled")
public class SchedledConfiguration {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataSource dataSource;



    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

//    @Bean
//    protected Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws SchedulerException, IOException {
//        return schedulerFactoryBean.getScheduler();
//    }

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
//        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactoryBean.setJobDetails(sampleJobDetail().getObject());
        schedulerFactoryBean.setTriggers(sampleJobTrigger().getObject());
        logger.info("><><><");
        return schedulerFactoryBean;
    }



    @Bean
    public JobDetailFactoryBean sampleJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ProductJob.class);
        // job has to be durable to be stored in DB:
        factoryBean.setDurability(true);
        factoryBean.setName("sampleJob");
        return factoryBean;
    }

    @Bean
    public CronTriggerFactoryBean sampleJobTrigger() {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(sampleJobDetail().getObject());
        factoryBean.setName("sampleTrigger");
        factoryBean.setCronExpression("0/4 * * * * ?");
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        return factoryBean;
    }
//        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
//        factoryBean.setJobDetail(jobDetail);
//        factoryBean.setStartDelay(0L);
//        factoryBean.setRepeatInterval(frequency);
//        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
//        // in case of misfire, ignore all missed triggers and continue :
//        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
//        return factoryBean;
//  }




//    @Bean
//    public Scheduler scheduler() throws IOException, SchedulerException {
//        SchedulerFactory schedulerFactory = new StdSchedulerFactory(quartzProperties());
//        Scheduler scheduler = schedulerFactory.getScheduler();
//        scheduler.start();
//        return scheduler;
//    }
//
//    /**
//     * 设置quartz属性
//     * @throws IOException
//     * 2016年10月8日下午2:39:05
//     */
//    public Properties quartzProperties() throws IOException {
//        Properties prop = new Properties();
//        prop.put("quartz.scheduler.instanceName", "ServerScheduler");
//        prop.put("org.quartz.scheduler.instanceId", "AUTO");
//        prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
//        prop.put("org.quartz.scheduler.instanceId", "NON_CLUSTERED");
//        prop.put("org.quartz.scheduler.jobFactory.class", "org.quartz.simpl.SimpleJobFactory");
//        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
//        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
//        prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");
//        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
//        prop.put("org.quartz.jobStore.isClustered", "true");
//        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
//        prop.put("org.quartz.threadPool.threadCount", "5");
//
//        prop.put("org.quartz.dataSource.quartzDataSource.driver", "com.mysql.jdbc.Driver");
//        prop.put("org.quartz.dataSource.quartzDataSource.URL", "jdbc:mysql://localhost:3306/quartz-demo");
//        prop.put("org.quartz.dataSource.quartzDataSource.user", "root");
//        prop.put("org.quartz.dataSource.quartzDataSource.password", "123456");
//        prop.put("org.quartz.dataSource.quartzDataSource.maxConnections", "10");
//        return prop;
//    }

}
