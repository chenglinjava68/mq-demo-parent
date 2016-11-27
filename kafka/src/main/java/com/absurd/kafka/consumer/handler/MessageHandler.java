package com.absurd.kafka.consumer.handler;

public interface MessageHandler<T> {
	 void dealMessage(String topic, T t);
}
