package com.absurd.redis.springdata.async;

import com.absurd.redis.springdata.web.SampleController;
import com.absurd.redis.thread.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

/**
 * Created by wangwenwei on 16/12/17.
 */
@Component
public class AsyncService {
    private static Logger logger = LoggerFactory.getLogger(AsyncService.class);


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Async
    public void asyncMeth() {
        try {
            Thread.sleep(1000 * 5L);
            logger.info("><><><><----");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Async
    public Future<String> asyncMeWithRt(){
        try {
            Thread.sleep(1000 * 5L);
            logger.info(">>>>>>>>mefdjl>>>>>>>>>>>>>>>><----");
            return new AsyncResult<String>("hello world !!!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Async
    public Future<String> asyncPublisher(String channel ) {
            stringRedisTemplate.convertAndSend(channel, "发布一个项目" + Publisher.atomicInteger.incrementAndGet() + "The time is now " + dateFormat.format(new Date()));
            return new AsyncResult<String>("OK");
    }
}
