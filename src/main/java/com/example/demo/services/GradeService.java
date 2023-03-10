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
import com.example.demo.validators.ValidationError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeService {
    @Value("${newGradeMessage}")
    private String message;
    @Value("${newGradeSubject}")
    private String subject;
    private final EmailSenderService emailSenderService;
    private final GradeRepo gradeRepo;
    private final StudentRepo studentRepo;
    private final SubjectRepo subjectRepo;
    private final GradeValidator gradeValidator;

    public GradeService(GradeRepo gradeRepo, StudentRepo studentRepo, SubjectRepo subjectRepo, GradeValidator gradeValidator,
                        EmailSenderService emailSenderService) {
        this.gradeRepo = gradeRepo;
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
        this.gradeValidator = gradeValidator;
        this.emailSenderService = emailSenderService;
    }

    /**
     * @param studentGrade the student grade.
     * @return the added grade, if the addition is possible.
     * @throws ServiceException if there is no student with the studentId or there is no subject with subjectCode
     * @throws ValidationError  if grade is not valid
     */
    public Grade addGrade(Grade studentGrade) throws ServiceException, ValidationError {
        gradeValidator.validate(studentGrade);

        String studentId = studentGrade.getStudentId();
        Optional<Student> optionalStudent = this.studentRepo.findById(studentId);
        if (optionalStudent.isEmpty()) {
            throw new ServiceException("Student does not exists");
        }
        String subjectCode = studentGrade.getSubjectCode();
        Optional<Subject> optionalSubject = this.subjectRepo.findSubjectBySubjectCode(subjectCode);
        if (optionalSubject.isEmpty()) {
            throw new ServiceException("Subject does not exists");
        }

        Optional<Grade> optionalGrade = this.gradeRepo.findGradeBySubjectCodeAndStudentId(subjectCode, studentId);
        if (optionalGrade.isPresent()) {
            throw new ServiceException("The student already has a grade for this subject");
        }
        this.gradeRepo.save(studentGrade);
        //send the email to the student
        emailSenderService.sendEmail(optionalStudent.get().getEmailAddress(),message,subject);
        return studentGrade;
    }

    /**
     * @param studentId String representing the id of the student for which we want to get all grades.
     * @return a list with all grades of the student with studentId.
     * @throws ServiceException if the student with studentID does not exist.
     */
    public List<GradeDto> getStudentGrades(String studentId) throws ServiceException {
        if (studentRepo.findById(studentId).isEmpty()) {
            throw new ServiceException("Student does not exist");
        }
        List<Grade> studentGrades = this.gradeRepo.findGradesByStudentId(studentId);
        return studentGrades.stream().map(grade -> new GradeDto(grade.getSubjectCode(), grade.getGrade()
                , grade.getGraduationDate())).collect(Collectors.toList());
    }

    /**
     * @param studentId   String representing the id of the student whose grade we are deleting.
     * @param subjectCode String representing the id of the subject whose grade we are deleting.
     * @return the deleted grade.
     * @throws ServiceException if there is no grade at this subject and for this student.
     */
    public Grade deleteGradeForStudent(String studentId, String subjectCode) throws ServiceException {
        Optional<Grade> optionalGrade = this.gradeRepo.deleteGradeByStudentIdAndSubjectCode(studentId, subjectCode);
        if (optionalGrade.isEmpty()) {
            throw new ServiceException("Student has no grade at this subject");
        }
        return optionalGrade.get();
    }
}
