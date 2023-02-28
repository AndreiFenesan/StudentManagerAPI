package com.example.demo.controllers;

import com.example.demo.services.StudentService;
import com.example.demo.domain.Student;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/students")
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<Student> fetchAllStudents(){
        return studentService.getAllStudents();
    }
    @PostMapping(path = "/add")
    public Student AddStudent(@RequestBody Student student){
        System.out.println("aici");
        return studentService.addStudent(student);
    }
}
