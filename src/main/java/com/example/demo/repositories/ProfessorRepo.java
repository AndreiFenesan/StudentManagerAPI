package com.example.demo.repositories;

import com.example.demo.domain.Professor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfessorRepo extends MongoRepository<Professor, String> {
    Optional<Professor> findProfessorByUsername(String username);
}
