package com.pinch.org.login.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pinch.org.login.entity.User;

public interface LoginRepo extends MongoRepository<User, String> {

	Optional<User> findByUserName(String userName);

	Optional<User> findByEmail(String email);

	Optional<User> findByUserNameAndIsActive(String username, boolean b);

}
