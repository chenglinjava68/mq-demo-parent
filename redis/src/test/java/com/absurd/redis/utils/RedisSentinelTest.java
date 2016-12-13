package com.absurd.redis.utils;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wangwenwei on 16/12/13.
 */
public class RedisSentinelTest {
    @Test
    public void get_with_sentinel(){
        Set<String> sentinels = new HashSet<String>();
        String hostAndPort1 = "127.0.0.1:26379";
        sentinels.add(hostAndPort1);

        String clusterName = "mymaster";
        String password = "";

        JedisSentinelPool redisSentinelJedisPool = new JedisSentinelPool(clusterName,sentinels);
//                JedisSentinelPool(clusterName,sentinels,password);

        Jedis jedis = null;
        try {
            jedis = redisSentinelJedisPool.getResource();
            jedis.set("key", "value");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis!=null)
                jedis.close();
        }

        redisSentinelJedisPool.close();
    }
}
