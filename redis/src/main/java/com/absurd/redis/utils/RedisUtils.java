package com.absurd.redis.utils;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author <a href="mailto:wangwenwei@myhexin.com">王文伟</a>
 * @Company:浙江核新同花顺网络信息股份有限公司
 * @Title: redis
 * @Package com.absurd.redis.utils
 * @Description:
 * @date 2016/12/12 18:53
 */
public class RedisUtils {


    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    public static ConcurrentHashMap<String, JedisPool> pools = new ConcurrentHashMap<>();
    protected static String DEFAULT_ADDRESS = "127.0.0.1";
    protected static Integer DEFAULT_PORT = 6379;
    protected static Integer DEFAULT_TIMEOUT =1000;

    protected static String  DEFAULT_ID = DEFAULT_ADDRESS + ":" + DEFAULT_PORT;
    protected static   JedisPool pool;

    protected static ReentrantLock lockPool = new ReentrantLock();
    protected static ReentrantLock lockJedis = new ReentrantLock();

    static {
        init();
    }

    private static void init() {
        if ( pool != null ) return;
        String id = DEFAULT_ID;
        logger.info("connecting to redis on " + id);
        assert ! lockPool.isHeldByCurrentThread();
        lockPool.lock();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setMaxTotal(10000);
        config.setMaxIdle(1000);
        config.setMinIdle(500);
        pool = new JedisPool(config, DEFAULT_ADDRESS, DEFAULT_PORT, DEFAULT_TIMEOUT);
        pools.put(id,pool);
        lockPool.unlock();

        if ( !connected(pool) )
            throw new RuntimeException("Failed to connect to redis at " + id);
    }


    private static  boolean connected(JedisPool pool) {
        assert ! lockPool.isHeldByCurrentThread();
        lockPool.lock();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.isConnected();
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockPool.unlock();
        }
    }

    public static Jedis getJedis(){
        init();
        return pool.getResource();
    }
    private static void closeResource(Jedis jedis){
        if(jedis!=null)
            jedis.close();
    }

    public static String hget(String hashname, String key){
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        String value = null;
        try {
            jedis = getJedis();
            value = jedis.hget(hashname, key);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }

        return value;
    }

    public static  void hset(String hashname, String key, String value) {

        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;

        try {
            jedis = getJedis();
            jedis.hset(hashname, key, value);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
    }

    public static  Set<String> hkeys(String hashname) {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        Set<String> keys;

        try {
            jedis = getJedis();
            keys = jedis.hkeys(hashname);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return keys;
    }

    // this is only used in testing, so we dont need to validate
    public static void del(String hashname) {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;

        try {
            jedis = getJedis();
            jedis.del(hashname);
            Set<String> keys = jedis.keys(".*");
            Iterator<String> iter = keys.iterator();
            String key;
            while ( iter.hasNext() ) {
                key = iter.next();
                logger.info("deleting key " + key);
                jedis.del(key);
            }
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
    }

    public static void delkeys() {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;

        try {
            jedis = getJedis();
            Set<String> keys = jedis.keys("*");
            Iterator<String> iter = keys.iterator();
            String key;
            while ( iter.hasNext() ) {
                key = iter.next();
                logger.info("deleting key " + key);
                jedis.del(key);
            }
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
    }

    public static int incr(String keyname) {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        int result = -1;
        long value = -1;

        try {
            jedis = getJedis();
            value = jedis.incr(keyname);
            result = (int)value;
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }

        if ( result != value )
            throw new IllegalArgumentException("Out of range: " + value);

        return result;
    }

    public static int decr(String keyname) {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        int result = -1;
        long value = -1;

        try {
            jedis = getJedis();
            value = jedis.decr(keyname);
            result = (int)value;
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }

        if ( result != value )
            throw new IllegalArgumentException("Out of range: " + value);

        return result;
    }

    public static boolean exists(String keyname) {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;

        try {
            jedis =getJedis();
            return jedis.exists(keyname);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
    }

    public static String get(String key) {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        String value = null;

        try {
            jedis = getJedis();
            value = jedis.get(key);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }

        return value;
    }

    public static void set(String keyname, int value) {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(keyname, String.valueOf(value));
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
    }

    public static Long lpush(String keyname,String... values){
        long intReply = 0L;
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            intReply =   jedis.lpush(keyname, values);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return intReply;
    }

    public static Long bitcount(String keyname){
        Long intReply = 0L;
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            intReply =   jedis.bitcount(keyname);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return intReply;
    }

    public static Long bitcount(String keyname,int start,int end){
        Long intReply = 0L;
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            intReply =   jedis.bitcount(keyname,start,end);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return intReply;
    }

    public static Boolean getbit(String keyname,long offset){
        Boolean intReply = false;
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            intReply =   jedis.getbit(keyname,offset);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return intReply;
    }


    public static Boolean setbit(String keyname,long offset,boolean bit){
        Boolean intReply = false;
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            intReply =   jedis.setbit(keyname, offset,bit);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return intReply;
    }


    public static Long rpush(String keyname,String... values){
        long intReply = 0L;
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            intReply =   jedis.rpush(keyname, values);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return intReply;
    }

    public static String lpop(String keyname){
        String result = null;
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result =   jedis.lpop(keyname);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return result;
    }

    public static String rpop(String keyname){
        String result = null;
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result =   jedis.rpop(keyname);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return result;
    }

    /**
     *
     * @param keyname
     * @param value
     * @param time 秒
     */
    public static void set(String keyname, int value , long time) {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;

        try {
            jedis = getJedis();
            jedis.set(keyname, String.valueOf(value),"NX","EX",time);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
    }

    public static String set(String keyname, String value) {
        String statusCodeReply = "";
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            statusCodeReply =  jedis.set(keyname, String.valueOf(value));
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return statusCodeReply;
    }

    /**
     *
     * @param keyname
     * @param value
     * @param time 秒
     */
    public static void set(String keyname, String value , long time) {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;

        try {
            jedis = getJedis();
            jedis.set(keyname, value,"NX","EX",time);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
    }

    public static long ttl(String keyname){
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        Long time = 0L;
        try {
            jedis = getJedis();
            time = jedis.ttl(keyname);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return time;
    }


    public static long pttl(String keyname){
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        Jedis jedis = null;
        Long time = 0L;
        try {
            jedis = getJedis();
            time = jedis.pttl(keyname);
        } finally {
            if ( jedis != null ) closeResource(jedis);
            lockJedis.unlock();
        }
        return time;
    }
}
