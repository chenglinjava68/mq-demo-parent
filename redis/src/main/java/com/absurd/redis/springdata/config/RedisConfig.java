package com.absurd.redis.springdata.config;

import com.absurd.redis.springdata.listen.RedisMessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author <a href="mailto:wangwenwei@myhexin.com">王文伟</a>
 * @Company:浙江核新同花顺网络信息股份有限公司
 * @Title: mq-demo-parent
 * @Package com.absurd.redis.springdata.config
 * @Description:
 * @date 2016/12/15 13:51
 */
@Configuration
public class RedisConfig {
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    protected RedisTemplate<String,Object> redisTemplate() {
        final RedisTemplate<String, Object> template =  new RedisTemplate<String,Object>();
        template.setConnectionFactory( jedisConnectionFactory );
        template.setKeySerializer( new StringRedisSerializer() );
        template.setHashValueSerializer( new GenericToStringSerializer<Object>( Object.class ) );
        template.setValueSerializer( new GenericToStringSerializer<Object>( Object.class ) );
        return template;
    }

    @Bean
    protected MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter( new RedisMessageListener() );
    }

    @Bean
    protected RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory( jedisConnectionFactory );
        container.addMessageListener( messageListener(), topic() );

        return container;
    }

    @Bean
    protected ChannelTopic topic() {
        return new ChannelTopic( "user:topic1" );
    }

}
