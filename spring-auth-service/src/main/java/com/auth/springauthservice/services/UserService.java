package com.auth.springauthservice.services;

import com.auth.springauthservice.models.User;

public interface UserService{
    void save(User user);
    User findByUserID(String userid);
}