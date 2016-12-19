package com.absurd.rabbitmq.springamqp.listener;

import com.absurd.rabbitmq.model.UserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.rabbitmq.springamqp.listener
 * @Description:
 * @date 2016/12/19 14:53
 */
@Component
public class MQReceiverListener {
    private static Logger logger = LoggerFactory.getLogger(MQReceiverListener.class);
//    @RabbitListener(bindings=@QueueBinding(value = @Queue("dsbnfioas"),exchange = @Exchange( value = "absurd_exchange",type = "fanout")))
//    @RabbitListener(queues = "dsbnfioas")
    public void processMessage(byte[] content) {
        String message = null;
        try {
            message = new String(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.info(""+message);
    }


//    @RabbitListener(queues = "userQueue")
//    public void processMessage(UserDTO content) {
//        logger.info(""+content.toString());
//    }


    @RabbitListener(queues = "userQueueDir")
    public void processMessageWithTopic(UserDTO content) {
        logger.info(""+content.toString());
    }
}
