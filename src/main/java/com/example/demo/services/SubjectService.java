package com.example.demo.services;

import com.example.demo.domain.Subject;
import com.example.demo.repositories.SubjectRepo;
import com.example.demo.validators.SubjectValidator;
import com.example.demo.validators.ValidationError;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubjectService {
    private final SubjectRepo subjectRepo;
    private final SubjectValidator subjectValidator;

    public SubjectService(SubjectRepo subjectRepo, SubjectValidator subjectValidator) {
        this.subjectRepo = subjectRepo;
        this.subjectValidator = subjectValidator;
    }

    /**
     * add a new subject, if there is no other subject with the same subjectCode
     *
     * @param subject the subject we want to add
     * @return the added subject, if there is no other subject with the same subjectCode.
     * @throws ValidationError if subject is not valid
     *                         null, if the add was not possible
     **/
    public Subject addSubject(Subject subject) throws ValidationError {
        this.subjectValidator.validate(subject);

        Optional<Subject> optionalSubject = subjectRepo.findSubjectBySubjectCode(subject.getSubjectCode());
        if (optionalSubject.isPresent()) {
            return null;
        }
       return this.subjectRepo.save(subject);
    }

    /**
     *
     * @param subjectCode -String representing the subjectCode of the subject we want to find.
     * @return the found subject
     */
    public Optional<Subject> findSubjectBySubjectCode(String subjectCode) {
        return this.subjectRepo.findSubjectBySubjectCode(subjectCode);
    }
}
