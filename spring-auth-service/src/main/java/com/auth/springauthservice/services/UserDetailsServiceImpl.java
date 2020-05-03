package com.auth.springauthservice.services;

import java.util.HashSet;
import java.util.Set;

import com.auth.springauthservice.models.Roles;
import com.auth.springauthservice.models.User;
import com.auth.springauthservice.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
        User user = userRepository.findByUserID(userID);

        if(user == null){
            throw new UsernameNotFoundException(userID);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for(Roles role: user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        return new org.springframework.security.core.userdetails.User(user.getUserID(), user.getPasswd(), grantedAuthorities);
    }
}