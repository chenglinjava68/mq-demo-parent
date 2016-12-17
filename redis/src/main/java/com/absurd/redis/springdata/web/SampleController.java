package com.absurd.redis.springdata.web;

import com.absurd.redis.thread.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by wangwenwei on 16/12/17.
 */
@Controller
public class SampleController {
    private static Logger logger = LoggerFactory.getLogger(SampleController.class);

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!"+ Publisher.atomicInteger.get();
    }

}
