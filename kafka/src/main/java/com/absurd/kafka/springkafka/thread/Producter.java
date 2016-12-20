package com.absurd.kafka.springkafka.thread;

import com.absurd.kafka.springkafka.model.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka.thread
 * @Description:
 * @date 2016/12/19 19:26
 */
@Component
public class Producter {
    private static Logger logger = LoggerFactory.getLogger(Producter.class);
    public static AtomicInteger atomicInteger = new AtomicInteger();
    @Autowired
    private KafkaTemplate kafkaTemplate;
private static final ObjectMapper objectMapper = new ObjectMapper();


    @Scheduled(cron = "0/1 * * * * ?")
    public void product2()   {
        Integer id = atomicInteger.incrementAndGet();
        logger.info(id+"><");
        UserDTO userDTO = new UserDTO(id.longValue(),"www","fdsf","福建","34234",323423532L);
        kafkaTemplate.send("absurtopic2",userDTO);
        UserDTO userDTO1 = new UserDTO(id.longValue(),"ww1","fdsf","台湾","34234",323423532L);
        UserDTO userDTO2 = new UserDTO(id.longValue(),"ww2","fdsf","泰国","34234",323423532L);
        kafkaTemplate.send("absurtopic2",userDTO1);
        kafkaTemplate.send("absurtopic2",userDTO2);

    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void product() throws JsonProcessingException {
        Integer id = atomicInteger.incrementAndGet();
        logger.info(id+"><");
        UserDTO userDTO = new UserDTO(id.longValue(),"www","fdsf","福建","34234",323423532L);
        kafkaTemplate.send("absurtopic",objectMapper.writeValueAsString(userDTO));
        UserDTO userDTO1 = new UserDTO(id.longValue(),"ww1","fdsf","台湾","34234",323423532L);
        UserDTO userDTO2 = new UserDTO(id.longValue(),"ww2","fdsf","泰国","34234",323423532L);
        kafkaTemplate.send("absurtopic",objectMapper.writeValueAsString(userDTO1));
        kafkaTemplate.send("absurtopic",objectMapper.writeValueAsString(userDTO2));

    }
}
