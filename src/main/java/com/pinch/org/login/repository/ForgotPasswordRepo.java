package com.pinch.org.login.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pinch.org.login.entity.ForgotPasswordToken;

public interface ForgotPasswordRepo extends MongoRepository<ForgotPasswordToken, String> {

}
