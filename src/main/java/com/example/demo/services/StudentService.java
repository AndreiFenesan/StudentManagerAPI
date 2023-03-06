package com.example.demo.services;

import com.example.demo.domain.Student;
import com.example.demo.exception.ServiceException;
import com.example.demo.repositories.StudentRepo;
import com.example.demo.validators.StudentValidator;
import com.example.demo.validators.ValidationError;
import com.example.demo.validators.Validator;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {
    private final StudentRepo studentRepo;
    private final Validator<Student> studentValidator;

    public Student addStudent(String firstName, String lastName, String username, String password) throws ValidationError, ServiceException {
        Student student = new Student(firstName,lastName,username,password,LocalDateTime.now());
        studentValidator.validate(student);
        if (this.studentRepo.findStudentByUsername(student.getUsername()).isPresent()) {
            throw new ServiceException("Student with this username already exists");
        }
        String encryptedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        student.setPassword(encryptedPassword);
        student.setCreated(LocalDateTime.now());
        return studentRepo.save(student);
    }

    public Optional<Student> findStudentByUsername(String username){
        return studentRepo.findStudentByUsername(username);
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }
}

