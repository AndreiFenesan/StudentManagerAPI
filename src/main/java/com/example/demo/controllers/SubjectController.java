package com.example.demo.controllers;

import com.example.demo.domain.Subject;
import com.example.demo.services.SubjectService;
import com.example.demo.validators.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * method that adds a new subject. If the add is not possible, a error message will be returned.
     * @param subject the subject to add
     */
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
