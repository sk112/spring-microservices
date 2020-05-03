package com.auth.springauthservice.repository;

import com.auth.springauthservice.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUserID(String userID);
}