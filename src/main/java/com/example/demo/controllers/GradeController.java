package com.example.demo.controllers;

import com.example.demo.domain.Grade;
import com.example.demo.dtos.GradeDto;
import com.example.demo.dtos.StudentIdAndSubjectCodeDto;
import com.example.demo.dtos.StudentIdDto;
import com.example.demo.exception.ServiceException;
import com.example.demo.services.GradeService;
import com.example.demo.validators.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/grade")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/professorGrades")
    public void addGrade(@RequestBody Grade grade) {
        try {
            this.gradeService.addGrade(grade.getSubjectCode(), grade.getStudentId(), grade.getGrade(), grade.getGraduationDate());
        } catch (ServiceException | ValidationError exception) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        }
    }

    @GetMapping("/studentGrades")
    public List<GradeDto> getGradesForStudent(@RequestBody StudentIdDto studentIdDto) {
        if (studentIdDto.getStudentId() == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(500), "The given id is null");
        }
        try {
            return this.gradeService.getStudentGrades(studentIdDto.getStudentId());
        } catch (ServiceException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        }
    }

    @DeleteMapping("/professorGrades")
    public void deleteGradeForStudent(@RequestBody StudentIdAndSubjectCodeDto studentIdAndSubjectCode) {
        try {
            this.gradeService.deleteGradeForStudent(studentIdAndSubjectCode.getStudentId(), studentIdAndSubjectCode.getSubjectCode());
        } catch (ServiceException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        }
    }
}
