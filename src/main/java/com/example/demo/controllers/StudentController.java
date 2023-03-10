package com.example.demo.controllers;

import com.example.demo.exception.ServiceException;
import com.example.demo.services.StudentService;
import com.example.demo.domain.Student;
import com.example.demo.validators.ValidationError;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/students")
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<Student> fetchAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public Student AddStudent(@RequestBody Student student) {
        try {
            return studentService.addStudent(student);
        } catch (ServiceException | ValidationError exception) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        }
    }
}
