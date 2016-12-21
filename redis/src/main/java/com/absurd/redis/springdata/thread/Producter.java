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
    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private ThreadLocal<Long> startBeTime = new ThreadLocal<Long>();
    @Autowired
    private RedisTemplate redisTemplate;
    @Scheduled(cron = "0/60 * * * * ?")
    //11s
    public void product(){
        Integer id = Publisher.atomicInteger.incrementAndGet();
        UserDTO userDTO = new UserDTO(id.longValue(),"www","fdsf","福建","34234",323423532L);

        String queueName = "userQueue";
        startTime.set(System.currentTimeMillis());
        startBeTime.set(System.currentTimeMillis());
        for(int i=0;i<100000;i++) {
            id = Publisher.atomicInteger.incrementAndGet();
            userDTO.setId(id.longValue());
            redisTemplate.opsForList().leftPush(queueName, userDTO);
            if (id%10==0){
                Long lo = startTime.get();
                long elapsedTime = System.currentTimeMillis() - lo;
                logger.info("take {} ms",  elapsedTime);
                startTime.set(System.currentTimeMillis());
            }
        }
        Long lo = startBeTime.get();
        long elapsedTime = System.currentTimeMillis() - lo;

        logger.info("ttoll take {} ms",  elapsedTime);
    }


//    @Scheduled(cron = "0/60 * * * * ?")
//    //11s
    public void productMid(){
        Integer id = Publisher.atomicInteger.incrementAndGet();
        StringBuilder sbf = new StringBuilder();
        //5k
        for(int i=0;i<1000;i++)
        sbf.append("www");
        UserDTO userDTO = new UserDTO(id.longValue(),sbf.toString(),"fdsf","福建","34234",323423532L);

        String queueName = "userQueue1";
        startTime.set(System.currentTimeMillis());
        startBeTime.set(System.currentTimeMillis());
        for(int i=0;i<100000;i++) {
            id = Publisher.atomicInteger.incrementAndGet();
            userDTO.setId(id.longValue());
            redisTemplate.opsForList().leftPush(queueName, userDTO);
            if (id%10==0){
                Long lo = startTime.get();
                long elapsedTime = System.currentTimeMillis() - lo;
                logger.info("take {} ms",  elapsedTime);
                startTime.set(System.currentTimeMillis());
            }
        }
        Long lo = startBeTime.get();
        long elapsedTime = System.currentTimeMillis() - lo;

        logger.info("ttoll take {} ms",  elapsedTime);
    }

//    @Scheduled(cron = "0/60 * * * * ?")
    //>10000s
    public void productLong(){
        Integer id = Publisher.atomicInteger.incrementAndGet();
        StringBuilder sbf = new StringBuilder();
        //5M
        for(int i=0;i<1000000;i++)
            sbf.append("www");
        UserDTO userDTO = new UserDTO(id.longValue(),sbf.toString(),"fdsf","福建","34234",323423532L);

        String queueName = "userQueue1";
        startTime.set(System.currentTimeMillis());
        startBeTime.set(System.currentTimeMillis());
        for(int i=0;i<100000;i++) {
            id = Publisher.atomicInteger.incrementAndGet();
            userDTO.setId(id.longValue());
            redisTemplate.opsForList().leftPush(queueName, userDTO);
            if (id%10==0){
                Long lo = startTime.get();
                long elapsedTime = System.currentTimeMillis() - lo;
                logger.info("take {} ms",  elapsedTime);
                startTime.set(System.currentTimeMillis());
            }
        }
        Long lo = startBeTime.get();
        long elapsedTime = System.currentTimeMillis() - lo;

        logger.info("ttoll take {} ms",  elapsedTime);
    }

}
