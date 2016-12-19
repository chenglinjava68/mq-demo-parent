package com.absurd.kafka.examples;

/**
 * Created by wangwenwei on 16/11/27.
 */
public class KeywordMessage implements kafka.serializer.Encoder<User>{
    @Override
    public byte[] toBytes(User user) {
        return user.toString().getBytes();
    }
}
