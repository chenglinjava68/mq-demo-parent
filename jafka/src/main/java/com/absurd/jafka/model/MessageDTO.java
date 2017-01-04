package com.absurd.jafka.model;

import java.util.Date;

public class MessageDTO {
private String content;
private Date createTime;

	public MessageDTO() {
	}

	public MessageDTO(String content, Date createTime) {
		this.content = content;
		this.createTime = createTime;
	}

	public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}

}
