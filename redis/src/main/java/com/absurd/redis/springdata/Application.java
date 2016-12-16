package com.absurd.redis.springdata;

import com.absurd.redis.springdata.listen.RedisMessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author <a href="mailto:wangwenwei@myhexin.com">王文伟</a>
 * @Company:浙江核新同花顺网络信息股份有限公司
 * @Title: mq-demo-parent
 * @Package com.absurd.redis.springdata
 * @Description:
 * @date 2016/12/15 13:42
 */
@EnableScheduling
@SpringBootApplication
public class Application  extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
