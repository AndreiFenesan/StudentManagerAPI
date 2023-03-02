package com.example.demo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Student extends User {

    public Student(String firstName, String lastName, String username, String password, LocalDateTime created) {
        super(firstName, lastName, username, password, created);
    }
}
