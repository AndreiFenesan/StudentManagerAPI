package com.example.demo.controllers;

import com.example.demo.domain.Grade;
import com.example.demo.exception.ServiceException;
import com.example.demo.services.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/grade")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/add")
    public void addGrade(@RequestBody Grade grade) {
        try {
            this.gradeService.addGrade(grade.getSubjectCode(), grade.getStudentId(), grade.getGrade(), grade.getGraduationDate());
        } catch (ServiceException serviceException) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, serviceException.getMessage());
        }
    }
}
