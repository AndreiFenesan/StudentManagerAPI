package com.example.demo.dtos;

import java.time.LocalDate;

public class GradeDto {
    private String subjectCode;
    private Integer grade;
    private LocalDate graduationDate;

    public GradeDto(String subjectCode, Integer grade, LocalDate graduationDate) {
        this.subjectCode = subjectCode;
        this.grade = grade;
        this.graduationDate = graduationDate;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public Integer getGrade() {
        return grade;
    }

    public LocalDate getGraduationDate() {
        return graduationDate;
    }
}
