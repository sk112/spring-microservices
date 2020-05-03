package com.auth.springauthservice.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users_impl")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long Id;
    String firstName;
    String lastName;
    String userID;

    String passwd;

    @ManyToMany
    Set<Roles> roles;

    public User() {
    }


    public User(String firstName, String lastName, String userID, String passwd) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.passwd = passwd;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Set<Roles> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    
    
    
}