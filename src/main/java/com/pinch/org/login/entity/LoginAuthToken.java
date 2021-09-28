package com.pinch.org.login.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "loginAuthToken")
public class LoginAuthToken {

	private String userName;
	private String token;
	private Long createdDate;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
}
