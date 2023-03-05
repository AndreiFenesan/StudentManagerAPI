package com.example.demo.services;

import com.example.demo.domain.Subject;
import com.example.demo.repositories.SubjectRepo;
import com.example.demo.validators.SubjectValidator;
import com.example.demo.validators.ValidationError;
import org.springframework.stereotype.Service;

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
     * @param subjectCode     - String representing the unique code of the subject
     * @param numberOfCredits - Integer representing the subject's number of credits
     * @return the added subject, if there is no other subject with the same subjectCode.
     * @throws ValidationError if subject is not valid
     * null, if the add was not possible
     **/
    public Subject addSubject(String subjectCode, Integer numberOfCredits) throws ValidationError {
        Subject subject = new Subject(subjectCode, numberOfCredits);
        this.subjectValidator.validate(subject);

        Subject dataSubject = subjectRepo.findSubjectBySubjectCode(subjectCode);
        if (dataSubject != null) {
            return null;
        }
        this.subjectRepo.save(subject);
        return subject;
    }

    public Subject findSubjectBySubjectCode(String subjectCode) {
        return this.subjectRepo.findSubjectBySubjectCode(subjectCode);
    }
}
