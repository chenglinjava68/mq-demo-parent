package com.absurd.jafka;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.jafka.api.FetchRequest;
import io.jafka.consumer.Consumer;
import io.jafka.consumer.ConsumerConfig;
import io.jafka.consumer.ConsumerConnector;
import io.jafka.consumer.MessageStream;
import io.jafka.consumer.SimpleConsumer;
import io.jafka.message.MessageAndOffset;
import io.jafka.producer.Producer;
import io.jafka.producer.ProducerConfig;
import io.jafka.producer.StringProducerData;
import io.jafka.producer.serializer.StringDecoder;
import io.jafka.producer.serializer.StringEncoder;
import io.jafka.utils.ImmutableMap;
import io.jafka.utils.Utils;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.jafka
 * @Description:
 * @date 2016/12/23 16:48
 */
public class SimpleProCusTest {
    Logger logger = LoggerFactory.getLogger(SimpleProCusTest.class);
    @Test
    public void product(){
        Properties props = new Properties();
        props.put("broker.list", "0:127.0.0.1:9092");
        props.put("serializer.class", StringEncoder.class.getName());
        //
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);
        //
        StringProducerData data = new StringProducerData("demo");
        for(int i=0;i<1000;i++) {
            data.add("Hello world #"+i);
        }
        //
        try {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                producer.send(data);
            }
            long cost = System.currentTimeMillis() - start;
            logger.info("send 100000 message cost: "+cost+" ms");
        } finally {
            producer.close();
        }
    }

    @Test
    public void consumer() throws IOException {
        SimpleConsumer consumer = new SimpleConsumer("127.0.0.1", 9092);
//
        long offset = 0;
        while (true) {
            FetchRequest request = new FetchRequest("test", 0, offset);
            for (MessageAndOffset msg : consumer.fetch(request)) {
                System.out.println(Utils.toString(msg.message.payload(), "UTF-8"));
                offset = msg.offset;
            }
        }
    }

    @Test
    public void productWithZookeeper(){
        Properties props = new Properties();
        props.put("zk.connect", "localhost:2181");
        props.put("serializer.class", StringEncoder.class.getName());
        //
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);
        //
        StringProducerData data = new StringProducerData("demo");
        for(int i=0;i<100;i++) {
            data.add("Hello world #"+i);
        }
        //
        try {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                producer.send(data);
            }
            long cost = System.currentTimeMillis() - start;
            logger.info("send 10000 message cost: "+cost+" ms");
        } finally {
            producer.close();
        }
    }

    @Test
    public void consumerWithZookeeper() throws IOException, InterruptedException {
        Properties props = new Properties();
        props.put("zk.connect", "localhost:2181");
        props.put("groupid", "test_group");
        //
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector connector = Consumer.create(consumerConfig);
        //
        Map<String, List<MessageStream<String>>> topicMessageStreams = connector.createMessageStreams(ImmutableMap.of("demo", 2), new StringDecoder());
        List<MessageStream<String>> streams = topicMessageStreams.get("demo");
        //
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final AtomicInteger count = new AtomicInteger();
        for (final MessageStream<String> stream : streams) {
            executor.submit(new Runnable() {

                public void run() {
                    for (String message : stream) {
                        logger.info(count.incrementAndGet() + " => " + message);
                    }
                }
            });
        }
        //
        executor.awaitTermination(1, TimeUnit.HOURS);
    }
}
