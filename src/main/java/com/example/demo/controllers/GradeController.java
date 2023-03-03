package com.example.demo.controllers;

import com.example.demo.domain.Grade;
import com.example.demo.dtos.GradeDto;
import com.example.demo.dtos.StudentIdDto;
import com.example.demo.exception.ServiceException;
import com.example.demo.services.GradeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

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

    @GetMapping()
    public List<GradeDto> getGradesForStudent(@RequestBody StudentIdDto studentIdDto) {
        if (studentIdDto.getStudentId() == null) {
            throw new ResponseStatusException(HttpStatus.valueOf(500),"The given id is null");
        }
        try {
            return this.gradeService.getStudentGrades(studentIdDto.getStudentId());
        } catch (ServiceException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        }
    }
}
