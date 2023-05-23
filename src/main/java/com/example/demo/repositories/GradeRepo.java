package com.example.demo.repositories;

import com.example.demo.domain.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepo extends MongoRepository<Grade, String> {
    Optional<Grade> findGradeBySubjectCodeAndStudentId(String subjectCode, String studentId);

    List<Grade> findGradesByStudentId(String studentId);

    Optional<Grade> deleteGradeByStudentIdAndSubjectCode(String studentId, String subjectCode);
}
