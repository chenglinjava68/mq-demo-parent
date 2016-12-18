package com.absurd.redis.springdata.thread;

import com.absurd.redis.springdata.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by wangwenwei on 16/12/18.
 */
@Component
public class Producter {
    private static Logger logger = LoggerFactory.getLogger(Producter.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Scheduled(cron = "0/1 * * * * ?")
    public void product(){
        Integer id = Publisher.atomicInteger.incrementAndGet();
        UserDTO userDTO = new UserDTO(id.longValue(),"www","fdsf","福建","34234",323423532L);
        String queueName = "userQueue";
        redisTemplate.opsForList().leftPush(queueName,userDTO);
        UserDTO userDTO1 = new UserDTO(id.longValue(),"ww1","fdsf","台湾","34234",323423532L);
        UserDTO userDTO2 = new UserDTO(id.longValue(),"ww2","fdsf","泰国","34234",323423532L);

        //分数小的在头部
        redisTemplate.opsForZSet().add("uQue",userDTO1,0.6);
        redisTemplate.opsForZSet().add("uQue",userDTO2,0.3);
    }

}
