package com.absurd.redis.springdata.listen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * @author <a href="mailto:wangwenwei@myhexin.com">王文伟</a>
 * @Company:浙江核新同花顺网络信息股份有限公司
 * @Title: mq-demo-parent
 * @Package com.absurd.redis.springdata.listen
 * @Description:
 * @date 2016/12/15 16:09
 */
public class RedisMessageListener implements MessageListener {


    private static Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);
    @Override
    public void onMessage(Message message, byte[] bytes) {
        logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Message received: " + message.toString() );
    }
}
