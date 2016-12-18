package com.absurd.redis.springdata.thread;

import com.absurd.redis.springdata.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Created by wangwenwei on 16/12/18.
 */
@Component
public class Customer {
    private static Logger logger = LoggerFactory.getLogger(Customer.class);

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(initialDelay = 1000L,fixedRate = 3000L)
    public void customer(){
        String queueName = "userQueue";
        redisTemplate.setValueSerializer( new Jackson2JsonRedisSerializer<UserDTO>(UserDTO.class) );
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<UserDTO>(UserDTO.class));

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                UserDTO userDTO =  (UserDTO) redisTemplate.opsForList().rightPop(queueName);
                if (userDTO==null) return;
                logger.info(">>>"+userDTO);
               LinkedHashSet<UserDTO> ob =  (LinkedHashSet)  redisTemplate.opsForZSet().rangeByScore("uQue",0,1);
                if (ob==null) return;
                Iterator<UserDTO> ita = ob.iterator();
                while(ita.hasNext())
                logger.info(">>----<<<<<<<>"+ita.next());
            }
        };
        taskExecutor.execute(runnable);

    }
}
