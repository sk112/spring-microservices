package com.auth.springauthservice.services;

import java.util.HashSet;

import com.auth.springauthservice.models.User;
import com.auth.springauthservice.repository.RoleRepository;
import com.auth.springauthservice.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;


    @Override
    public void save(User user) {
        user.setPasswd(passwordEncoder.encode(user.getPasswd()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                                    .username(user.getUserID())
                                    .password(passwordEncoder.encode(user.getPasswd()))
                                    .roles("USER")
                                    .build();

        jdbcUserDetailsManager.createUser(userDetails);
        userRepository.save(user);
    }

    @Override
    public User findByUserID(String userid) {
        return userRepository.findByUserID(userid);
    }

}