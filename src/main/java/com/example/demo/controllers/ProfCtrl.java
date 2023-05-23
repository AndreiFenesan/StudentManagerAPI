package com.example.demo.controllers;

import com.example.demo.domain.Professor;
import com.example.demo.repositories.ProfessorRepo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/professor")
public class ProfCtrl {
    private final ProfessorRepo professorRepo;

    public ProfCtrl(ProfessorRepo professorRepo) {
        this.professorRepo = professorRepo;
    }

    @PostMapping("/add")
    public void addProf(@RequestBody Professor professor) {
        this.professorRepo.save(professor);
    }
}
