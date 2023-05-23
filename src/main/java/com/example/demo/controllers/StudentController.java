package com.example.demo.controllers;

import com.example.demo.domain.builders.StudentBuilder;
import com.example.demo.exception.ServiceException;
import com.example.demo.services.StudentService;
import com.example.demo.domain.Student;
import com.example.demo.validators.ValidationError;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@RestController
@RequestMapping("api/students")
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private static final Logger logger = LogManager.getLogger();

    /**
     * method that returns all students from specific group.
     *
     * @param groupNumber Integer that indicates the group from which all students will be returned.
     * @return a list with all students from group groupNumber.
     */
    @GetMapping()
    public ResponseEntity<List<Student>> getAllStudentsFromGroup(@RequestParam Integer groupNumber) {
        logger.traceEntry("Get students from group: {}", groupNumber);
        StudentBuilder studentBuilder = new StudentBuilder();
        List<Student> students = this.studentService.getStudentsFromGroup(groupNumber).stream().map(
                student -> {
                    studentBuilder.reset();
                    return studentBuilder
                            .withStudentId(student.getId())
                            .withFirstName(student.getFirstName())
                            .withLastName(student.getLastName())
                            .withSocialSecurityNumber(student.getSocialSecurityNumber())
                            .withUsername(student.getUsername())
                            .withEmailAddress(student.getEmailAddress()).build();
                }
        ).toList();
        logger.traceExit("Got all students from group: {}", groupNumber);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    /**
     * method that adds a student.
     *
     * @param student a student
     * @return the added student, if this is possible. Otherwise, the error due to which the student could not be added
     */
    @PostMapping
    public ResponseEntity<Student> AddStudent(@RequestBody Student student) {
        try {
            Student studentFromDb = studentService.addStudent(student);
            StudentBuilder studentBuilder = new StudentBuilder();
            studentBuilder
                    .withCreatedAt(studentFromDb.getCreated())
                    .withStudentId(studentFromDb.getId());

            return new ResponseEntity<>(studentBuilder.build(), HttpStatus.OK);
        } catch (ServiceException | ValidationError exception) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        }
    }
}
