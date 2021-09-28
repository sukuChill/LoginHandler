package com.pinch.org.login.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pinch.org.login.entity.LoginAuthToken;

public interface LoginAuthTokenRepo extends MongoRepository<LoginAuthToken, String> {

}
