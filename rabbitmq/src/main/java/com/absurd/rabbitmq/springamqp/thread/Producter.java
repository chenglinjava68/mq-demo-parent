package com.absurd.rabbitmq.springamqp.thread;

import com.absurd.rabbitmq.model.UserDTO;
import com.absurd.rabbitmq.springamqp.listener.MQReceiverListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.rabbitmq.springamqp.thread
 * @Description:
 * @date 2016/12/19 15:18
 */
@Component
public class Producter {
    private static Logger logger = LoggerFactory.getLogger(Producter.class);
    public static AtomicInteger atomicInteger = new AtomicInteger();
    @Autowired
    private AmqpTemplate rabbitmqTemplate;
    @Scheduled(cron = "0/1 * * * * ?")
    public void product(){
        Integer id = atomicInteger.incrementAndGet();
        logger.info(id+"><");
        UserDTO userDTO = new UserDTO(id.longValue(),"www","fdsf","福建","34234",323423532L);
        rabbitmqTemplate.convertAndSend("userQueueDir",userDTO);
        UserDTO userDTO1 = new UserDTO(id.longValue(),"ww1","fdsf","台湾","34234",323423532L);
        UserDTO userDTO2 = new UserDTO(id.longValue(),"ww2","fdsf","泰国","34234",323423532L);
        rabbitmqTemplate.convertAndSend("userQueueDir",userDTO1);
        rabbitmqTemplate.convertAndSend("userQueueDir",userDTO2);

    }
}
