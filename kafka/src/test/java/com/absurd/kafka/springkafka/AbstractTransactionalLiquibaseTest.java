package com.absurd.kafka.springkafka;

import com.absurd.kafka.springkafka.config.DruidConfiguration;
import com.absurd.kafka.springkafka.config.KafkaConfig;
import com.absurd.kafka.springkafka.config.LiquibaseConfig;
import com.absurd.kafka.springkafka.config.SchedledConfiguration;
import com.absurd.kafka.springkafka.job.TaskInfo;

import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import liquibase.integration.spring.SpringLiquibase;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka
 * @Description:
 * @date 2017/1/4 9:48
 */
@ContextConfiguration(classes = {DruidConfiguration.class,LiquibaseConfig.class, SchedledConfiguration.class,KafkaConfig.class}, loader = AnnotationConfigContextLoader.class)
public class AbstractTransactionalLiquibaseTest extends AbstractTransactionalJUnit4SpringContextTests {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SpringLiquibase springLiquibase;

    public AbstractTransactionalLiquibaseTest() {
    }

    @Test
    public void select_from_liquibase(){

        List<Map<String, Object>> list = jdbcTemplate.queryForList("SHOW TABLES");
        logger.info(">>>>"+list);
    }

    @Autowired
    private Scheduler scheduler;

    @Test
    public void task_test(){
        List<TaskInfo> list = new ArrayList<>();
        try {
            for(String groupJob: scheduler.getJobGroupNames()){
                for(JobKey jobKey: scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))){
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    for (Trigger trigger: triggers) {
                        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

                        String cronExpression = "", createTime = "";

                        if (trigger instanceof CronTrigger) {
                            CronTrigger cronTrigger = (CronTrigger) trigger;
                            cronExpression = cronTrigger.getCronExpression();
                            createTime = cronTrigger.getDescription();
                        }
                        TaskInfo info = new TaskInfo();
                        info.setJobName(jobKey.getName());
                        info.setJobGroup(jobKey.getGroup());
                        info.setJobDescription(jobDetail.getDescription());
                        info.setJobStatus(triggerState.name());
                        info.setCronExpression(cronExpression);
                        info.setCreateTime(createTime);
                        list.add(info);
                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        logger.info(list.get(0).getJobName()+"@@@@@@@@@@@@@@@@@@@@");
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList("SELECT * FROM QRTZ_JOB_DETAILS WHERE SCHED_NAME = 'schedulerFactoryBean' AND JOB_NAME = ? AND JOB_GROUP = ?","sampleJob","DEFAULT");
        logger.info(">>>>"+list2);

    }

}
