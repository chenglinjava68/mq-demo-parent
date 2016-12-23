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
    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private ThreadLocal<Long> startBeTime = new ThreadLocal<Long>();
    @Autowired
    private AmqpTemplate rabbitmqTemplate;


    //19s 106byte
    //93s (jackson 3s) 3103byte 0.3k
    //11000s  3000103byte  0.3M
    //140000s 27000103byte 3M
    @Scheduled(cron = "0/60000 * * * * ?")
    public void product(){
        Integer id = atomicInteger.incrementAndGet();
        logger.info(id+"><");
        UserDTO userDTO = new UserDTO(id.longValue(),"www","fdsf","福建","34234",323423532L);
        rabbitmqTemplate.convertAndSend("userQueueDir",userDTO);
        UserDTO userDTO1 = new UserDTO(id.longValue(),"ww1","fdsf","台湾","34234",323423532L);
        UserDTO userDTO2 = new UserDTO(id.longValue(),"ww2","fdsf","泰国","34234",323423532L);
        rabbitmqTemplate.convertAndSend("userQueueDir",userDTO1);
        rabbitmqTemplate.convertAndSend("userQueueDir",userDTO2);
        StringBuilder sbf = new StringBuilder();
        //0.3k
//        for(int i=0;i<1000;i++)
        // 0.3M
//        for(int i=0;i<1000000;i++)

        // 3M
//        for(int i=0;i<9000000;i++)
            sbf.append("www");
        userDTO.setUserName(sbf.toString());

        startTime.set(System.currentTimeMillis());
        startBeTime.set(System.currentTimeMillis());

        for(int i=0;i<100000;i++) {
            id = atomicInteger.incrementAndGet();
            userDTO.setId(id.longValue());
            rabbitmqTemplate.convertAndSend("userQueueDir",userDTO);
            if (id%10==0){
                Long lo = startTime.get();
                long elapsedTime = System.currentTimeMillis() - lo;
                logger.info("take {} ms",  elapsedTime);
                startTime.set(System.currentTimeMillis());
            }
        }
        Long lo = startBeTime.get();
        long elapsedTime = System.currentTimeMillis() - lo;

        logger.info("ttoll take {} ms",  elapsedTime);

    }
}
