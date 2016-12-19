package com.absurd.rabbitmq.thread;

import com.absurd.rabbitmq.model.MessageDTO;
import com.absurd.rabbitmq.utils.RabbitMQUtil;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.rabbitmq.thread
 * @Description:
 * @date 2016/12/19 10:50
 */
public class Producter {
    private static final AtomicInteger atomicInteger = new AtomicInteger();

    public  void sendMessage(String queue,String content){
        RabbitMQUtil.pushMessage(queue,new MessageDTO(content+atomicInteger.incrementAndGet(),new Date()));
    }

}
