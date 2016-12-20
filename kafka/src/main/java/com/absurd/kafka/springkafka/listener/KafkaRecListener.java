package com.absurd.kafka.springkafka.listener;

import com.absurd.kafka.springkafka.model.UserDTO;
import com.absurd.kafka.springkafka.thread.Producter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka.listener
 * @Description:
 * @date 2016/12/19 19:16
 */
@Component
public class KafkaRecListener {
    private static Logger logger = LoggerFactory.getLogger(Producter.class);
    @KafkaListener(topics = "absurtopic2",group="myGroup",containerFactory = "kafkaJsonListenerContainerFactory")
    public void processMessage(UserDTO content) {
        logger.info(content.toString());
    }

    @KafkaListener(topics = "absurtopic",group="myGroup")
    public void processMessage2(UserDTO content) {
        logger.info(content.toString());
    }

}
