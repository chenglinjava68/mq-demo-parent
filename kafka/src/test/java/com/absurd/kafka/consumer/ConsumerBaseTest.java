package com.absurd.kafka.consumer;

import com.absurd.examples.KafkaProperties;
import com.absurd.examples.User;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by wangwenwei on 16/11/27.
 */
public class ConsumerBaseTest {
    @Test
    public void testCon(){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoProducer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");

        ConsumerBase<String,User> consumer = new ConsumerBase<>(KafkaProperties.TOPIC2,1);
        //

    }
}
