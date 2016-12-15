package com.absurd.redis.thread;

import com.absurd.redis.springdata.listen.RedisMessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:wangwenwei@myhexin.com">王文伟</a>
 * @Company:浙江核新同花顺网络信息股份有限公司
 * @Title: mq-demo-parent
 * @Package com.absurd.redis.thread
 * @Description:
 * @date 2016/12/15 20:23
 */
@Configuration
@EnableScheduling
public class Publisher {

    private static Logger logger = LoggerFactory.getLogger(Publisher.class);
    private AtomicInteger atomicInteger = new AtomicInteger();
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Scheduled(cron = "0/20 * * * * ?")
    public void reportCurrentTime() {
        String channel = "user:topic1";
        stringRedisTemplate.convertAndSend(channel, "发布一个项目"+atomicInteger.incrementAndGet()+"The time is now " + dateFormat.format(new Date()));
        logger.info("发布一个项目"+atomicInteger.incrementAndGet()+"The time is now " + dateFormat.format(new Date()));
    }
}
