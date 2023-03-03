package com.example.demo.dtos;

public class StudentIdAndSubjectCodeDto {
    private String studentId;
    private String subjectCode;

    public StudentIdAndSubjectCodeDto(String studentId, String subjectCode) {
        this.studentId = studentId;
        this.subjectCode = subjectCode;
    }

    public StudentIdAndSubjectCodeDto() {
        this.studentId = null;
        this.subjectCode = null;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }
}
