package com.example.demo.repositories;

import com.example.demo.domain.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubjectRepo extends MongoRepository<Subject, String> {
     Optional<Subject> findSubjectBySubjectCode(String subjectCode);
}
