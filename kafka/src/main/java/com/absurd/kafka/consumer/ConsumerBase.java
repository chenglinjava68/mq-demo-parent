package com.absurd.kafka.consumer;

import com.absurd.kafka.examples.KafkaProperties;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.Decoder;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangwenwei on 16/11/27.
 */
public class ConsumerBase<K,V> {
    private final ConsumerConnector consumer;
    private final String topic;
    private final int threadNum;
    private ExecutorService executor;

    public ConsumerBase(String topic, int threadNum) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoProducer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        kafka.consumer.ConsumerConfig config = new kafka.consumer.ConsumerConfig(props);
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
        this.topic = topic;
        this.threadNum = threadNum;
    }


    public void run(Decoder<K> keyDecoder, Decoder<V> valueDecoder) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, threadNum);
        Map<String, List<KafkaStream<K, V>>> consumerMap = consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);

        List<KafkaStream<K, V>> streams = consumerMap.get(topic);

        executor = Executors.newFixedThreadPool(threadNum);

        int threadNo = 0;
        for (final KafkaStream<K, V> stream : streams) {
            ConsumerWorker<K, V> worker = new ConsumerWorker<K, V>(stream, threadNo);
            executor.submit(worker);
            threadNo++;
        }

    }
}
