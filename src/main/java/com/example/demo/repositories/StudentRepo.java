package com.example.demo.repositories;

import com.example.demo.domain.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepo extends MongoRepository<Student, String> {
    public Optional<Student> findStudentByUsername(String username);
}
