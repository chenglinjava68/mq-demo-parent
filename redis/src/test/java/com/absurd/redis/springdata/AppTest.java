package com.absurd.redis.springdata;

import com.absurd.redis.utils.RedisUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.redis.springdata
 * @Description:
 * @date 2016/12/15 13:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void simple_set(){
        stringRedisTemplate.opsForValue().set("my_simple_set", "111");
    }

    @Test
    public void simple_get(){
        RedisUtils.set("my_simple_get","111");
        assertEquals( "111", stringRedisTemplate.opsForValue().get("my_simple_get"));
    }
    @Test
    public void simple_mset_get(){
        Map<String,String> keyvalue = new HashMap<>();
        keyvalue.put("my_simple_mset_get1","1");
        keyvalue.put("my_simple_mset_get2","2");
        keyvalue.put("my_simple_mset_get3","3");
        keyvalue.put("my_simple_mset_get4","4");
        keyvalue.put("my_simple_mset_get5","5");
        keyvalue.put("my_simple_mset_get6","6");
        stringRedisTemplate.opsForValue().multiSet(keyvalue);
//        List<String> values = RedisUtils.mget("my_simple_mset_get1","my_simple_mset_get2","my_simple_mset_get3","my_simple_mset_get4","my_simple_mset_get5","my_simple_mset_get6");
        List<String> values = stringRedisTemplate.opsForValue().multiGet(Arrays.asList("my_simple_mset_get1","my_simple_mset_get2","my_simple_mset_get3","my_simple_mset_get4","my_simple_mset_get5","my_simple_mset_get6"));

        assertThat(values,hasItems("1","3","5"));
    }

    @Test
    public void simple_sadd(){
        if(  stringRedisTemplate.hasKey("simple_add")){
            stringRedisTemplate.delete("simple_add");
        }
        assertEquals( stringRedisTemplate.opsForSet().add("simple_add","a","b","c").longValue(),3L);
        assertEquals( stringRedisTemplate.opsForSet().size("simple_add").longValue(),3L);
        assertThat(  stringRedisTemplate.opsForSet().members("simple_add"),containsInAnyOrder("c","b","a"));
    }
    @Test
    public void simple_set_diff(){
        if(  stringRedisTemplate.hasKey("simple_add")){
            stringRedisTemplate.delete("simple_add");
        }
        assertEquals( stringRedisTemplate.opsForSet().add("simple_add","a","b","c").longValue(),3L);
        if(  stringRedisTemplate.hasKey("simple_add2")){
            stringRedisTemplate.delete("simple_add2");
        }
        assertEquals( stringRedisTemplate.opsForSet().add("simple_add2","b","c","d").longValue(),3L);

        assertEquals(stringRedisTemplate.opsForSet().difference("simple_add","simple_add2"),containsInAnyOrder("a"));
    }

    @Test
    public void simple_get_with_time() throws InterruptedException {
        stringRedisTemplate.opsForValue().set("my_simple_get_with_time","b",1L, TimeUnit.SECONDS);
        assertEquals( stringRedisTemplate.opsForValue().get("my_simple_get_with_time"),"b");
        Thread.sleep(1000L);
        assertNull( stringRedisTemplate.opsForValue().get("my_simple_get_with_time"));
        assertEquals( redisTemplate.getExpire("my_simple_get_with_time").longValue(),-2L);
        assertFalse(stringRedisTemplate.hasKey("my_simple_get_with_time"));
    }


    @Test
    public void simple_lpush_rpop(){
        if(  stringRedisTemplate.hasKey("my_simple_lpush")){
            stringRedisTemplate.delete("my_simple_lpush");
        }
        stringRedisTemplate.opsForList().leftPushAll("my_simple_lpush","a","b");
        stringRedisTemplate.opsForList().leftPushAll("my_simple_lpush","c","d");
        assertEquals(   stringRedisTemplate.opsForList().leftPop("my_simple_lpush"),"d");
        assertEquals( stringRedisTemplate.opsForList().rightPop("my_simple_lpush"),"a");
        stringRedisTemplate.opsForList().rightPushAll("my_simple_lpush","e","f");
        assertEquals(  stringRedisTemplate.opsForList().leftPop("my_simple_lpush"),"c");
        assertEquals(  stringRedisTemplate.opsForList().rightPop("my_simple_lpush"),"f");
    }

    @Test
    public void simple_pub(){
        String channel = "user:topic1";
//        redisTemplate.convertAndSend(channel, "from app 1");
        redisTemplate.convertAndSend(channel,Arrays.asList("东方飒","SSFe"));
        stringRedisTemplate.convertAndSend(channel, "from app 3");
    }


}
