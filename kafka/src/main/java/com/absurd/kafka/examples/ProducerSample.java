package com.absurd.kafka.examples;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangwenwei on 16/11/26.
 */
public class ProducerSample {
    public static void main(String[] args) {

        final  AtomicInteger  count =  new AtomicInteger(0);


        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {
                Properties props = new Properties();
                props.put("bootstrap.servers", KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
                props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
                props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

                KafkaProducer<Integer, String> producer = new KafkaProducer<Integer, String>(props);
                while(true){
                    producer.send(new ProducerRecord<Integer, String>(KafkaProperties.TOPIC,count.get(),"msg"),
                            new DemoCallBack(System.currentTimeMillis(),count.incrementAndGet(),"msg"));

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Properties props = new Properties();
                props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
                props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoProducer");
                props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
                props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
                props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
                props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
                props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

                KafkaConsumer<Integer,String> consumer = new KafkaConsumer<Integer, String>(props);
                consumer.subscribe(Collections.singletonList(KafkaProperties.TOPIC));
                while (true) {
                    ConsumerRecords<Integer, String> records = consumer.poll(1000);

                    for (ConsumerRecord<Integer, String> record : records) {
                        System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
                    }
                }
            }
        });
        t2.start();
        t.start();
        t.start();
        t.start();




    }

    static class DemoCallBack implements Callback {
        private final long startTime;
        private final int key;
        private final String message;
        public DemoCallBack(long startTime, int key, String message) {
            this.startTime = startTime;
            this.key = key;
            this.message = message;
        }

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (metadata != null) {
                System.out.println(
                        "message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
                                "), " +
                                "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
            } else {
                exception.printStackTrace();
            }
        }
    }

}

