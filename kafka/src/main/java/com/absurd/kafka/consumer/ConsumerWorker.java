package com.absurd.kafka.consumer;

import com.absurd.kafka.consumer.handler.MessageHandler;
import com.absurd.kafka.consumer.handler.impl.DefaultMessageHandlerImpl;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

/**
 * Created by wangwenwei on 16/11/27.
 */
public class ConsumerWorker<K,V> implements Runnable {
    private KafkaStream<K, V> mStream;
    private int threadNo;


    public ConsumerWorker(KafkaStream mStream, int threadNo) {
        this.mStream = mStream;
        this.threadNo = threadNo;
    }

    public void run() {
        // TODO Auto-generated method stub
        MessageHandler<V> msgHandler = new DefaultMessageHandlerImpl();
        ConsumerIterator it = mStream.iterator();
        while (it.hasNext()) {
            MessageAndMetadata<K, V> meta = it.next();
            msgHandler.dealMessage(meta.topic(), (V) meta.message());
            System.out.printf("thread:" + threadNo + " get message:" + meta.message());
        }
    }
}
