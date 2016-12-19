package com.absurd.kafka.product;

import com.absurd.kafka.examples.KafkaProperties;
import com.absurd.kafka.examples.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by wangwenwei on 16/11/27.
 */
public class ProducerBaseTest {
    @Test
    public void testPro(){
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        ProducerBase<String,User> producer = new ProducerBase<>(new KafkaProducer<String, User>(props));
        producer.sendMessage(KafkaProperties.TOPIC2, "a", new User(1L,"ww","d"), new ShowMessageCallBackHandler());

    }

}
