package com.absurd.rabbitmq.thread;

import com.absurd.rabbitmq.utils.RabbitMQUtil;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.rabbitmq.thread
 * @Description:
 * @date 2016/12/19 10:53
 */
public class Consumer {
    public void getMessage(String queue){
        RabbitMQUtil.getMessage(queue);
    }
}
