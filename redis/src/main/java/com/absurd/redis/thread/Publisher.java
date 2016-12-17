package com.absurd.redis.thread;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.redis.thread
 * @Description:
 * @date 2016/12/15 20:23
 */
@Component()
public class Publisher {

    private static Logger logger = LoggerFactory.getLogger(Publisher.class);
    public static  final  AtomicInteger atomicInteger = new AtomicInteger();
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Scheduled(cron = "0/20 * * * * ?")
    public void publish() {
            logger.info(">>>>>>>>>");
        String channel = "user:topic1";
        stringRedisTemplate.convertAndSend(channel, "发布一个项目"+atomicInteger.incrementAndGet()+"The time is now " + dateFormat.format(new Date()));
    }

    @PostConstruct
    public void tt(){
        logger.info("???????????>>>>>>>>>d"+atomicInteger.get());
    }
}
