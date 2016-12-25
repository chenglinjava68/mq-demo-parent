package com.absurd.rabbitmq.utils;

import com.absurd.rabbitmq.model.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class RabbitMQUtil {
    private static Logger logger = LoggerFactory.getLogger(RabbitMQUtil.class);
    private static final String        RABBITMQ_HOST          = "127.0.0.1";
    private static final String        RABBITMQ_PORT          = "5672";
    private static final String        RABBITMQ_VIRTUALHOST   = "/";
    private static final String        RABBITMQ_USERNAME      = "absurd";
    private static final String        RABBITMQ_PASSWORD      = "absurd";
    private static final String        RABBITMQ_EXCHANGE_TYPE = "fanout";
    private static final String        RABBITMQ_EXCHANGE_NAME = "absurd_exchange";
    private static final String        RABBITMQ_ROUTEKEY      = "";
    public static final String		   RABBITMQ_QUEUE_NAME    = "absurdqueue1";

	private static ConnectionFactory factory                  = new ConnectionFactory();
    private  static ObjectMapper objectMapper                 = new ObjectMapper();
    static {
        factory.setHost(RABBITMQ_HOST);
        factory.setPort(Integer.parseInt(RABBITMQ_PORT));
        factory.setVirtualHost(RABBITMQ_VIRTUALHOST);
        factory.setUsername(RABBITMQ_USERNAME);
        factory.setPassword(RABBITMQ_PASSWORD);
        init();
    }

    private volatile static Connection connection             = null;
    private volatile static Channel channel                   = null;

    public synchronized static void init() {
        if (channel == null || !channel.isOpen() || connection == null || !connection.isOpen()) {
            try {
                if (channel != null && channel.isOpen()) {
                    try {
                        channel.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                if (connection != null && connection.isOpen()) {
                    try {
                        connection.close();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
//                connection = factory.newConnection();
                 ExecutorService es = Executors.newFixedThreadPool(10);
                connection = factory.newConnection(es);
                channel = connection.createChannel();
                channel.exchangeDeclare(RABBITMQ_EXCHANGE_NAME, RABBITMQ_EXCHANGE_TYPE, true);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
				e.printStackTrace();
			}
        }
    }

    public static void queueDeclare(String queueName){
        if (channel == null || !channel.isOpen() || connection == null || !connection.isOpen()) {
            init();
        }
        try {
            channel.queueDeclare(queueName, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean pushMessage(String queueName,MessageDTO content) {
        boolean result = false;
        if (channel == null || !channel.isOpen() || connection == null || !connection.isOpen()) {
            init();
        }
        String message = null;
        try {
            message = objectMapper.writeValueAsString(content);
            if (channel != null && channel.isOpen()) {
//            	channel.queueDeclare(RABBITMQ_QUEUE_NAME, false, false, false, null);
                channel.basicPublish("", queueName,
                                     MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                logger.info(" [x] Sent '" + message + "'");
//                channel.close();
//    			connection.close();
            }
            result =true;

        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (TimeoutException e) {
//			e.printStackTrace();
//		}

        return result;
    }

    public static boolean getMessage(String queueName){
        boolean result = false;
         if (channel == null || !channel.isOpen() || connection == null || !connection.isOpen()) {
             init();
         }
         try {
             if (channel != null && channel.isOpen()) {
//            	 channel.queueDeclare(RABBITMQ_QUEUE_NAME, false, false, false, null);
            	 Consumer consumer = new DefaultConsumer(channel) {
            	      @Override
            	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            	          throws IOException {
                          String routingKey = envelope.getRoutingKey();
                          String contentType = properties.getContentType();
                          long deliveryTag = envelope.getDeliveryTag();
            	        String message = new String(body, "UTF-8");
                        logger.info(routingKey+contentType+" [x] Received '" + message + "'");
            	      }
            	    };
            	    channel.basicConsume(queueName, true, consumer);
             }
             result =true;
         } catch (IOException e) {
             e.printStackTrace();
         }
         return result;
    }
}

