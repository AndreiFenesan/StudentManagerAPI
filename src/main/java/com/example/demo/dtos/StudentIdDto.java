package com.example.demo.dtos;

public class StudentIdDto {
    private String studentId;

    public StudentIdDto() {
        studentId = null;
    }

    public StudentIdDto(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }
}
