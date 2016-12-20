package com.absurd.kafka.springkafka.thread;

import com.absurd.kafka.springkafka.model.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
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
        logger.info(id+">-------<");
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


    @Scheduled(cron = "0/1 * * * * ?")
    public void product3()   {
        final Integer id = atomicInteger.incrementAndGet();
        logger.info(id+"><");
        UserDTO userDTO = new UserDTO(id.longValue(),"www","fdsf","福建","34234",323423532L);
        kafkaTemplate.send("absurtopic3",userDTO.toString());
        UserDTO userDTO1 = new UserDTO(id.longValue(),"ww1","fdsf","台湾","34234",323423532L);
        UserDTO userDTO2 = new UserDTO(id.longValue(),"ww2","fdsf","泰国","34234",323423532L);
        kafkaTemplate.send("absurtopic3",userDTO1.toString());
        kafkaTemplate.send("absurtopic3",userDTO2.toString());
        kafkaTemplate.execute(new KafkaOperations.ProducerCallback() {
            @Override
            public Object doInKafka(Producer producer) {
                return producer.send(new ProducerRecord("absurtopic3", 0, "test",">>>>" + id + "<<"), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        logger.info("----->>>>>>>"+ recordMetadata.partition()+"|"+recordMetadata.offset()+"<<<<<<<<<<<<<<<<------------");
                    }
                });
            }
        });


        //会根据key 的hash值去自动分区
        kafkaTemplate.execute(new KafkaOperations.ProducerCallback() {
            @Override
            public Object doInKafka(Producer producer) {
                return producer.send(new ProducerRecord("absurtopic3",id.toString(),">>>>" + id + "<<"), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        logger.info("----2->>>>>>>"+ recordMetadata.partition()+"|"+recordMetadata.offset()+"<<<<<<<<<<<<<<<<------------");
                    }
                });
            }
        });

    }
}
