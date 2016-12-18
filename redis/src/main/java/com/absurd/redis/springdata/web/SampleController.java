package com.absurd.redis.springdata.web;

import com.absurd.redis.springdata.async.AsyncService;
import com.absurd.redis.springdata.thread.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * Created by wangwenwei on 16/12/17.
 */
@Controller
public class SampleController {
    private static Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private AsyncService asyncService;
    @RequestMapping("/")
    @ResponseBody
    String home() {
        asyncService.asyncMeth();
        Future<String> future = asyncService.asyncMeWithRt();
        if (future.isDone()) {  //判断是否执行完毕
            try {
                logger.info("Result from asynchronous process - " + future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return "Hello World!"+ Publisher.atomicInteger.get();
    }




}
