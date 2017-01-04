package com.absurd.kafka.springkafka.job;

import org.springframework.core.NestedRuntimeException;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka
 * @Description:
 * @date 2017/1/3 13:29
 */
public class ServiceException extends NestedRuntimeException {
    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
