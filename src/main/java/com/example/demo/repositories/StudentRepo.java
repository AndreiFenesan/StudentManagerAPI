package com.example.demo.repositories;

import com.example.demo.domain.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends MongoRepository<Student, String> {
     Optional<Student> findStudentByUsername(String username);
     List<Student> findStudentsByGroup(Integer group);
}
