package com.absurd.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPubSub;

/**
 * @author <a href="mailto:wangwenwei@myhexin.com">王文伟</a>
 * @Company:浙江核新同花顺网络信息股份有限公司
 * @Title: mq-demo-parent
 * @Package com.absurd.redis
 * @Description:
 * @date 2016/12/14 19:32
 */
public class Subscriber extends JedisPubSub {
    Logger logger = LoggerFactory.getLogger(Subscriber.class);

    public Subscriber() {
    }
    // 取得订阅的消息后的处理
    @Override
    public void onMessage(String channel, String message) {
//        super.onMessage(channel, message);
        logger.debug("|"+message);
    }

    // 初始化订阅时候的处理
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
    }

    // 取消订阅时候的处理
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        super.onUnsubscribe(channel, subscribedChannels);
    }
}
