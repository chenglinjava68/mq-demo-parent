package com.absurd.kafka.springkafka.thread;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka.thread
 * @Description:
 * @date 2017/1/3 17:39
 */
public class ProductJob implements Job{
    private static Logger logger = LoggerFactory.getLogger(ProductJob.class);
    @Autowired
    private  Producter producter;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info(">>>>>>>>>>>");
        producter.product2();
    }
}
