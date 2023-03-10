package com.example.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Professor extends User {
    public Professor(String firstName, String lastName, String username, String password, LocalDateTime created,
                     String emailAddress, String socialSecurityNumber) {
        super(firstName, lastName, username, password, emailAddress, socialSecurityNumber, created);
    }
}
