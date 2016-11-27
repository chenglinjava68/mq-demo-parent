package com.absurd.kafka.consumer.handler.impl;


import com.absurd.kafka.consumer.handler.MessageHandler;

public class ByteMessageHandler implements MessageHandler<byte[]> {

	public void dealMessage(String topic, byte[] t) {
		// TODO Auto-generated method stub
		System.out.println(new String(t));
	}

}
