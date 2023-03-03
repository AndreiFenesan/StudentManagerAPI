package com.example.demo.validators;

import com.example.demo.domain.Grade;
import org.springframework.stereotype.Component;

@Component
public class GradeValidator implements Validator<Grade> {
    @Override
    public void validate(Grade grade) throws ValidationError {
        checkGradeForNullness(grade);
        String error = "";
        Integer studentGrade = grade.getGrade();
        if (studentGrade < 1 || studentGrade > 10) {
            error += "Grade must be between 1 and 10.";
        }
        if (error.length() > 0) {
            throw new ValidationError(error);
        }
    }

    private void checkGradeForNullness(Grade grade) throws ValidationError {
        if (grade == null) {
            throw new ValidationError("Grade is null");
        }
        if (grade.getGrade() == null || grade.getSubjectCode() == null || grade.getStudentId() == null
                || grade.getGraduationDate() == null) {
            throw new ValidationError("Grade properties are null");
        }
    }
}
