package com.example.demo.services;

import com.example.demo.domain.Grade;
import com.example.demo.domain.Student;
import com.example.demo.domain.Subject;
import com.example.demo.dtos.GradeDto;
import com.example.demo.exception.ServiceException;
import com.example.demo.repositories.GradeRepo;
import com.example.demo.repositories.StudentRepo;
import com.example.demo.repositories.SubjectRepo;
import com.example.demo.validators.GradeValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeService {
    private final GradeRepo gradeRepo;
    private final StudentRepo studentRepo;
    private final SubjectRepo subjectRepo;
    private final GradeValidator gradeValidator;

    public GradeService(GradeRepo gradeRepo, StudentRepo studentRepo, SubjectRepo subjectRepo, GradeValidator gradeValidator) {
        this.gradeRepo = gradeRepo;
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
        this.gradeValidator = gradeValidator;
    }

    public Grade addGrade(String subjectCode, String studentId, Integer grade, LocalDate graduationDate) throws ServiceException {
        Grade studentGrade = new Grade(studentId, subjectCode, grade, graduationDate);
        gradeValidator.validate(studentGrade);

        Optional<Student> optionalStudent = this.studentRepo.findById(studentId);
        if (optionalStudent.isEmpty()) {
            throw new ServiceException("Student does not exists");
        }
        Subject subject = this.subjectRepo.findSubjectBySubjectCode(subjectCode);
        if (subject == null) {
            throw new ServiceException("Subject does not exists");
        }

        Optional<Grade> optionalGrade = this.gradeRepo.findGradeBySubjectCodeAndStudentId(subjectCode, studentId);
        if (optionalGrade.isPresent()) {
            throw new ServiceException("The student already has a grade for this subject");
        }
        this.gradeRepo.save(studentGrade);
        return studentGrade;
    }

    public List<GradeDto> getStudentGrades(String studentId) throws ServiceException {
        System.out.println(studentId);
        if (studentRepo.findById(studentId).isEmpty()) {
            throw new ServiceException("Student does not exist");
        }
        List<Grade> studentGrades = this.gradeRepo.findGradesByStudentId(studentId);
        return studentGrades.stream().map(grade -> new GradeDto(grade.getSubjectCode(), grade.getGrade()
                , grade.getGraduationDate())).collect(Collectors.toList());
    }

    public Grade deleteGradeForStudent(String studentId, String subjectCode) throws ServiceException {
        Optional<Grade> optionalGrade = this.gradeRepo.deleteGradeByStudentIdAndSubjectCode(studentId, subjectCode);
        if (optionalGrade.isEmpty()) {
            throw new ServiceException("Student has no grade at this subject");
        }
        return optionalGrade.get();
    }
}
