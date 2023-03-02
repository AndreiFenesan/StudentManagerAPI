package com.example.demo.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private LocalDateTime created;

    public User(String firstName, String lastName, String username, String password, LocalDateTime created) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.created = created;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }
}
