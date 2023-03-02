package com.example.demo.services;

import com.example.demo.domain.Grade;
import com.example.demo.domain.Student;
import com.example.demo.domain.Subject;
import com.example.demo.exception.ServiceException;
import com.example.demo.repositories.GradeRepo;
import com.example.demo.repositories.StudentRepo;
import com.example.demo.repositories.SubjectRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class GradeService {
    private final GradeRepo gradeRepo;
    private final StudentRepo studentRepo;
    private final SubjectRepo subjectRepo;

    public GradeService(GradeRepo gradeRepo, StudentRepo studentRepo, SubjectRepo subjectRepo) {
        this.gradeRepo = gradeRepo;
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
    }

    public Grade addGrade(String subjectCode, String studentId, Integer grade, LocalDate graduationDate) throws ServiceException {
        Optional<Student> optionalStudent = this.studentRepo.findById(studentId);
        if (optionalStudent.isEmpty()) {
            throw new ServiceException("Student does not exists");
        }
        Subject subject = this.subjectRepo.findSubjectBySubjectCode(subjectCode);
        if(subject == null){
            throw new ServiceException("Subject does not exists");
        }

        Optional<Grade> optionalGrade = this.gradeRepo.findGradeBySubjectCodeAndStudentId(subjectCode, studentId);
        if (optionalGrade.isPresent()) {
            throw new ServiceException("The student already has a grade for this subject");
        }
        Grade studentGrade = new Grade(studentId, subjectCode, grade, graduationDate);
        this.gradeRepo.save(studentGrade);
        return studentGrade;
    }
}
