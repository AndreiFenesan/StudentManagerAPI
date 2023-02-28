package com.example.demo.services;

import com.example.demo.domain.Student;
import com.example.demo.repositories.StudentRepo;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class StudentService {
    private final StudentRepo studentRepo;

    public Student addStudent(Student student) {
        String password = student.getPassword();
        String encryptedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        student.setPassword(encryptedPassword);
        student.setCreated(LocalDateTime.now());
        return studentRepo.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }
}

