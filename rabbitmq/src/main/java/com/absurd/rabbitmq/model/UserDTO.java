package com.absurd.rabbitmq.model;

import java.io.Serializable;

/**
 * Created by wangwenwei on 16/12/18.
 */
//@JsonSerialize(using = UserSerializer.class)
public class UserDTO implements Serializable,Cloneable{
    private Long id;
    private String userName;
    private String password;
    private Address address;

    public UserDTO() {
    }

    public UserDTO(Long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public UserDTO(Long id, String userName, String password,String addr, String addCode, Long pos) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.address = new Address(addr,addCode,pos);
    }

    public UserDTO(Long id, String userName, String password, Address address) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", address=" + address +
                '}';
    }

}
