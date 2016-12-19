package com.absurd.kafka.springkafka.model;

import java.io.Serializable;

/**
 * Created by wangwenwei on 16/12/18.
 */
public class Address implements Serializable{
    private String addr;
    private String addCode;
    private Long pos ;

    public Address() {
    }

    public Address(String addr, String addCode, Long pos) {
        this.addr = addr;
        this.addCode = addCode;
        this.pos = pos;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAddCode() {
        return addCode;
    }

    public void setAddCode(String addCode) {
        this.addCode = addCode;
    }

    public Long getPos() {
        return pos;
    }

    public void setPos(Long pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addr='" + addr + '\'' +
                ", addCode='" + addCode + '\'' +
                ", pos=" + pos +
                '}';
    }
}
