package com.example.demo.validators;

import com.example.demo.domain.Student;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

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

    private void validateEmailAddress(String emailAddress) throws ValidationError {
        if (!Pattern.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", emailAddress)) {
            throw new ValidationError("Invalid email");
        }
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
                student.getPassword() == null ||
                student.getFirstName() == null ||
                student.getLastName() == null ||
                student.getEmailAddress() == null ||
                student.getGroup() == null ||
                student.getSocialSecurityNumber() == null
        ) {
            throw new ValidationError("Properties of student are null");
        }
    }

    private void validateSocialSecurityNumber(String socialSecurityNumber) throws ValidationError {
        String regex = "\\b[1-9][0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12][0-9]|3[01])(?:0[1-9]|[1-3][0-9]|4[0-6]|51|52)[0-9]{4}\\b";
        if (!Pattern.matches(regex, socialSecurityNumber)) {
            throw new ValidationError("Invalid social security number");
        }
    }

    @Override
    public void validate(Student student) throws ValidationError {
        checkNullnessOfStudent(student);
        validatePassword(student.getPassword());
        validateEmailAddress(student.getEmailAddress());
        validateSocialSecurityNumber(student.getSocialSecurityNumber());

        String error = "";
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
