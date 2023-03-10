package com.example.demo.controllers;

import com.example.demo.domain.Subject;
import com.example.demo.services.SubjectService;
import com.example.demo.validators.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/subject")
public class SubjectController {
    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @PostMapping
    public void addSubject(@RequestBody Subject subject) {

        try {
            Subject subject1 = this.service.addSubject(subject);
            if (subject1 == null) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Subject already exists");
            }
        } catch (ValidationError validationError) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, validationError.getMessage());
        }
    }
}
