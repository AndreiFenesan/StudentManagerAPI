package com.example.demo.validators;

import com.example.demo.domain.Subject;
import org.springframework.stereotype.Component;

@Component
public class SubjectValidator implements Validator<Subject> {
    @Override
    public void validate(Subject subject) throws ValidationError {
        checkSubjectNullness(subject);
        if (subject.getSubjectCode().length() < 3) {
            throw new ValidationError("Subject code should have at least 3 characters");
        }
        Integer numberOfCredits = subject.getNumberOfCredits();
        if (numberOfCredits < 1) {
            throw new ValidationError("Subject must have a positive number of credits");
        }
    }

    private static void checkSubjectNullness(Subject subject) throws ValidationError {
        if (subject == null) {
            throw new ValidationError("Subject is null");
        }
        String subjectCode = subject.getSubjectCode();
        Integer numberOfCredits = subject.getNumberOfCredits();
        if (subjectCode == null || numberOfCredits == null) {
            throw new ValidationError("Subject property is null");
        }
    }
}
