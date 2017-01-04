package com.absurd.kafka.springkafka.job;

import java.io.Serializable;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka.job
 * @Description:
 * @date 2017/1/3 13:47
 */
public class ResponseDTO<T> implements Serializable{
    private int code;
    private String message;
    private T obj;

    public ResponseDTO() {
    }

    public ResponseDTO(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseDTO(int code, String message, T obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
