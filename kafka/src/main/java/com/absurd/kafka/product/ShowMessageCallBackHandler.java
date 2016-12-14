package com.absurd.kafka.product;

import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * Created by wangwenwei on 16/11/27.
 */
public class ShowMessageCallBackHandler implements MessageCallBackHandler {
    @Override
    public void callBack(RecordMetadata meta) {
        // TODO Auto-generated method stub
        System.out.println(meta.offset() + " " + meta.partition() + " " + meta.topic());
        System.out.println(meta.toString());
    }
}
