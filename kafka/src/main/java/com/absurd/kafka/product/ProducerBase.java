package com.absurd.kafka.product;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by wangwenwei on 16/11/27.
 */
public class ProducerBase<K,V> {
    private final Producer<K, V> producer;
    private ProducerRecord<K, V> record;
    public ProducerBase(Producer<K, V> producer) {
        this.producer = producer;
    }

    public void sendMessage(String topic, V value, MessageCallBackHandler callBackHandler) {
        record = new ProducerRecord<K, V>(topic, value);
        sendMessage(record, callBackHandler);
    }

    public void sendMessage(String topic, K key, V value, MessageCallBackHandler callBackHandler) {
        record = new ProducerRecord<K, V>(topic, key, value);
        sendMessage(record, callBackHandler);
    }


    public void sendMessage(String topic, int partition, K key, V value, MessageCallBackHandler callBackHandler) {
        record = new ProducerRecord<K, V>(topic, partition, key, value);
        sendMessage(record, callBackHandler);
    }

    private void sendMessage(ProducerRecord<K, V> record, MessageCallBackHandler callBackHandler) {
        Future<RecordMetadata> future = producer.send(record);
        producer.close();
        while (future.isDone()) {
            try {
                if (callBackHandler != null) {
                    callBackHandler.callBack(future.get());
                    break;
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            }
        }
    }
}
