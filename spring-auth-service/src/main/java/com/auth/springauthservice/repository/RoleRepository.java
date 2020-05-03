package com.auth.springauthservice.repository;

import com.auth.springauthservice.models.Roles;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles, Long> {
    
}