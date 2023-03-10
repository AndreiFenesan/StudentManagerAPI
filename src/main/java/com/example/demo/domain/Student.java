package com.example.demo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Student extends User {
    private Integer group;

    public Student(String firstName, String lastName, String username, String password,
                   String emailAddress, Integer group, String socialSecurityNumber, LocalDateTime created) {
        super(firstName, lastName, username, password, emailAddress, socialSecurityNumber, created);
        this.group = group;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }
}
