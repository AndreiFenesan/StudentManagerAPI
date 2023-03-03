package com.example.demo.validators;

import com.example.demo.domain.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class StudentValidator implements Validator<Student> {

    private boolean hasStringAtLeastOneCapitalLetter(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (Character.isUpperCase(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private void validatePassword(String password) {
        String error = "";
        if (password.length() < 8) {
            error += "Password must have at least 8 characters.";
        }
        if (!hasStringAtLeastOneCapitalLetter(password)) {
            error += "Password must have at least one capital letter";
        }
        if (error.length() > 0) {
            throw new ValidationError(error);
        }
    }

    private void checkNullnessOfStudent(Student student) {
        if (student == null) {
            throw new ValidationError("Student is null");
        }
        if (student.getUsername() == null ||
                student.getPassword() == null || student.getFirstName() == null ||
                student.getLastName() == null) {
            throw new ValidationError("Properties of student are null");
        }
    }

    @Override
    public void validate(Student student) throws ValidationError {
        checkNullnessOfStudent(student);
        String error = "";
        validatePassword(student.getPassword());
        if (student.getUsername().length() < 3) {
            error += "Username must have at least 3 characters.";
        }
        if (student.getFirstName().length() < 3) {
            error += "Username must have at least 3 characters.";
        }
        if (error.length() > 0) {
            throw new ValidationError(error);
        }
    }
}
