package com.absurd.redis.springdata.async;

import com.absurd.redis.springdata.web.SampleController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * Created by wangwenwei on 16/12/17.
 */
@Component
public class AsyncService {
    private static Logger logger = LoggerFactory.getLogger(AsyncService.class);
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
}
