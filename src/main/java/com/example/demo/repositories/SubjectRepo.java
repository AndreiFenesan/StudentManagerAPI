package com.example.demo.repositories;

import com.example.demo.domain.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubjectRepo extends MongoRepository<Subject, String> {
    public Subject findSubjectBySubjectCode(String subjectCode);
}
