package com.absurd.kafka.consumer;

import kafka.serializer.Decoder;

/**
 * Created by wangwenwei on 16/11/27.
 */
public interface ConsumerDecoder<T> extends Decoder<T> {
}
