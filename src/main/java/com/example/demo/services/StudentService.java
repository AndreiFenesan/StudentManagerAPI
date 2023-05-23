package com.example.demo.services;

import com.example.demo.domain.Student;
import com.example.demo.exception.ServiceException;
import com.example.demo.repositories.StudentRepo;
import com.example.demo.validators.ValidationError;
import com.example.demo.validators.Validator;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger();

    /**
     * method that validate and adds a new student to the database.
     *
     * @param student String, firstName of the student we want to add.
     * @return the added student, if possible
     * @throws ValidationError  if the student is not valid.
     * @throws ServiceException if there is already a student in the database with the same username.
     */

    public Student addStudent(Student student) throws ValidationError, ServiceException {
        logger.info("Adding student: {}", student);
        studentValidator.validate(student);
        if (this.studentRepo.findStudentByUsername(student.getUsername()).isPresent()) {
            logger.warn("No student with {} already exists", student.getUsername());
            throw new ServiceException("Student with this username already exists");
        }
        String encryptedPassword = Hashing.sha256().hashString(student.getPassword(), StandardCharsets.UTF_8).toString();
        student.setPassword(encryptedPassword);
        student.setCreated(LocalDateTime.now());
        logger.info("Student {} added successfully", student);
        return studentRepo.save(student);

    }

    /**
     * @param username Unique string, representing the username of the student.
     * @return the student which has the int parameter username
     */
    public Optional<Student> findStudentByUsername(String username) {
        logger.info("Finding student with username: {}", username);
        Optional<Student> studentByUsername = studentRepo.findStudentByUsername(username);
        logger.info("Found student: {}", studentByUsername.isPresent());
        return studentByUsername;
    }

    /**
     * @return all students found
     */
    public List<Student> getAllStudents() {
        logger.info("Returning all students");
        return studentRepo.findAll();
    }

    public List<Student> getStudentsFromGroup(Integer group) {
        logger.info("Getting students from group: {}", group);
        List<Student> studentsByGroup = this.studentRepo.findStudentsByGroup(group);
        logger.info("Found students from group: {}", group);
        return studentsByGroup;
    }
}

