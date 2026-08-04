package com.auth.auth_service.model;

import jakarta.persistence.*;

@Entity
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer user_id;



    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public UserData(String email, String password, String roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserData() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(unique = true)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String roles;

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
