package com.pinch.org.login.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pinch.org.login.entity.LoginAuthToken;

public interface LoginAuthTokenRepo extends MongoRepository<LoginAuthToken, String> {

	Optional<LoginAuthToken> findByToken(String token);

}
