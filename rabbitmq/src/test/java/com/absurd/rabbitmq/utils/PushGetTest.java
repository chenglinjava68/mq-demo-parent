package com.absurd.rabbitmq.utils;

import com.absurd.rabbitmq.thread.Consumer;
import com.absurd.rabbitmq.thread.Producter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.rabbitmq.utils
 * @Description:
 * @date 2016/12/19 10:54
 */
public class PushGetTest {
    private static Logger logger = LoggerFactory.getLogger(PushGetTest.class);
    @Test
    public void test2() throws IOException {
        logger.info(this.getClass().getResource("").getPath());
        logger.info(this.getClass().getClassLoader().getResource("").getPath());
        File directory = new File("");//设定为当前文件夹
        logger.info(directory.getCanonicalPath());
        logger.info(directory.getAbsolutePath());
    }
    @Test
    public void ps() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        String queueName = "dsbnfioas";
        RabbitMQUtil.queueDeclare(queueName);
        Producter p = new Producter();
        Consumer c  = new Consumer();
        Runnable pr = new Runnable() {
            @Override
            public void run() {
                while(true){
                    p.sendMessage(queueName,"fsdfe");
                }
            }
        };
        Runnable cr = new Runnable() {
            @Override
            public void run() {
                while(true){
                    c.getMessage(queueName);
                }
            }
        };
        executorService.submit(pr);
//        executorService.submit(cr);

        executorService.shutdown();
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
