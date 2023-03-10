package com.example.demo.domain.builders;

import com.example.demo.domain.Student;

import java.time.LocalDateTime;

public class StudentBuilder {
    private Student student;

    public StudentBuilder(Student student) {
        this.student = student;
    }

    public StudentBuilder() {
        student = new Student();
    }

    public StudentBuilder reset() {
        this.student = new Student();
        return this;
    }

    public Student build() {
        return student;
    }

    public StudentBuilder withStudentId(String studentId) {
        student.setId(studentId);
        return this;
    }

    public StudentBuilder withPassword(String password) {
        student.setPassword(password);
        return this;
    }

    public StudentBuilder withFirstName(String firstName) {
        student.setFirstName(firstName);
        return this;
    }

    public StudentBuilder withLastName(String lastName) {
        student.setLastName(lastName);
        return this;
    }

    public StudentBuilder withEmailAddress(String emailAddress) {
        this.student.setEmailAddress(emailAddress);
        return this;
    }

    public StudentBuilder withSocialSecurityNumber(String socialSecurityNumber) {
        student.setSocialSecurityNumber(socialSecurityNumber);
        return this;
    }

    public StudentBuilder withGroup(Integer group) {
        student.setGroup(group);
        return this;
    }

    public StudentBuilder withUsername(String username) {
        student.setUsername(username);
        return this;
    }

    public StudentBuilder withCreatedAt(LocalDateTime createdAt) {
        student.setCreated(createdAt);
        return this;
    }
}
