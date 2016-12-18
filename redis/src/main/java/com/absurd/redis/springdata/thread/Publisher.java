package com.absurd.redis.springdata.thread;


import com.absurd.redis.springdata.async.AsyncService;
import com.absurd.redis.springdata.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.redis.springdata.thread
 * @Description:
 * @date 2016/12/15 20:23
 */
@Component
public class Publisher {

    private static Logger logger = LoggerFactory.getLogger(Publisher.class);
    @Autowired
    private TaskExecutor taskExecutor;
    public static  final  AtomicInteger atomicInteger = new AtomicInteger();
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AsyncService asyncService;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//    @Scheduled(cron = "0/1 * * * * ?")
    public void publish() {
            logger.info(">>>>>>>>>");
        String channel = "user:topic1";
        taskExecutor.execute(()->{
            stringRedisTemplate.convertAndSend(channel, "发布一个项目"+atomicInteger.incrementAndGet()+"The time is now " + dateFormat.format(new Date()));

        });
       }

//    @Scheduled(fixedDelay = 1000L)
    public void tt(){
        while(true){
            taskExecutor.execute(()->{
//              Future<String> future = asyncService.asyncPublisher("user:topic1");
//                if (future.isDone()) {
//                    try {
//                        logger.info("???????????>>>>>>>>>d" + future.get());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                }
                Integer id = Publisher.atomicInteger.incrementAndGet();
                stringRedisTemplate.convertAndSend("user:topic1", "发布一个项目" + id + "The time is now " + dateFormat.format(new Date()));
                UserDTO userDTO = new UserDTO(id.longValue(),"www","fdsf","福建","34234",323423532L);
                redisTemplate.convertAndSend("user:topic2", userDTO);
            });
        }
    }
}
