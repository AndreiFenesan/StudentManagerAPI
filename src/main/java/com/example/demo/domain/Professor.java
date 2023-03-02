package com.example.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Professor extends User {
    public Professor(String firstName, String lastName, String username, String password, LocalDateTime created) {
        super(firstName, lastName, username, password, created);
    }
}
