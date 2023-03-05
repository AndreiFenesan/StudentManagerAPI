package com.example.demo.services;

import com.example.demo.domain.Subject;
import com.example.demo.repositories.SubjectRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubjectService {
    private final SubjectRepo subjectRepo;

    public SubjectService(SubjectRepo subjectRepo) {
        this.subjectRepo = subjectRepo;
    }

    /**
     * add a new subject, if there is no other subject with the same subjectCode
     * @param subjectCode - String representing the unique code of the subject
     * @param numberOfCredits - Integer representing the subject's number of credits
     * @return the added subject, if there is no other subject with the same subjectCode.
     * null, if the add was not possible
     **/
    public Subject addSubject(String subjectCode, Integer numberOfCredits) {
        Subject dataSubject = subjectRepo.findSubjectBySubjectCode(subjectCode);
        if (dataSubject != null) {
            return null;
        }
        Subject subject = new Subject(subjectCode, numberOfCredits);
        this.subjectRepo.save(subject);
        return subject;
    }

    public Subject findSubjectBySubjectCode(String subjectCode){
        return this.subjectRepo.findSubjectBySubjectCode(subjectCode);
    }
}
