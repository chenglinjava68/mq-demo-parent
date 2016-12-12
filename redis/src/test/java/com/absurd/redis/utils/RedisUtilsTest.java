package com.absurd.redis.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:wangwenwei@myhexin.com">王文伟</a>
 * @Company:浙江核新同花顺网络信息股份有限公司
 * @Title: mq-demo-parent
 * @Package com.absurd.redis.utils
 * @Description:
 * @date 2016/12/12 19:05
 */
public class RedisUtilsTest {
    @Test
    public void simple_set(){
       assertEquals(RedisUtils.set("my_simple_set","b"),"OK");
    }

    @Test
    public void simple_get(){
        RedisUtils.set("my_simple_get","b");
        assertEquals( RedisUtils.get("my_simple_get"),"b");
    }

    @Test
    public void simple_get_with_time() throws InterruptedException {
        RedisUtils.set("my_simple_get_with_time","b",1L);
        assertEquals( RedisUtils.get("my_simple_get_with_time"),"b");
        assertNotEquals( RedisUtils.ttl("my_simple_get_with_time"),-2L);
        assertEquals( RedisUtils.pttl("my_simple_get_with_time"),999L);
        Thread.sleep(1000L);
        assertNull( RedisUtils.get("my_simple_get_with_time"));
        assertEquals( RedisUtils.ttl("my_simple_get_with_time"),-2L);
        assertEquals( RedisUtils.pttl("my_simple_get_with_time"),-2L);
        assertFalse(RedisUtils.exists("my_simple_get_with_time"));
    }

    @Test
    public void simple_incr(){
        RedisUtils.set("my_simple_incr",1);
        for(int i=0;i<100;i++)
        RedisUtils.incr("my_simple_incr");
        assertEquals( RedisUtils.get("my_simple_incr"),"101");
    }

    @Test
    public void simple_lpush_rpop(){
      if(  RedisUtils.exists("my_simple_lpush")){
          RedisUtils.del("my_simple_lpush");
      }
        RedisUtils.lpush("my_simple_lpush","a","b");
        RedisUtils.lpush("my_simple_lpush","c","d");
        assertEquals(  RedisUtils.lpop("my_simple_lpush"),"d");
        assertEquals(  RedisUtils.rpop("my_simple_lpush"),"a");
        RedisUtils.rpush("my_simple_lpush","e","f");
        assertEquals(  RedisUtils.lpop("my_simple_lpush"),"c");
        assertEquals(  RedisUtils.rpop("my_simple_lpush"),"f");
    }

    @Test
    public void simple_setbit(){
        if(  RedisUtils.exists("simple_setbit")){
            RedisUtils.del("simple_setbit");
        }
        RedisUtils.setbit("simple_setbit",1,true);
        assertFalse(RedisUtils.getbit("simple_setbit",2));
        assertTrue(RedisUtils.getbit("simple_setbit",1));
        RedisUtils.setbit("simple_setbit",2,true);
        RedisUtils.setbit("simple_setbit",3,true);
        assertEquals(RedisUtils.bitcount("simple_setbit").longValue(),3L);
    }






}
