package com.absurd.kafka.product;

import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * Created by wangwenwei on 16/11/27.
 */
public interface MessageCallBackHandler {
    void callBack(RecordMetadata recordMetadata);
}
