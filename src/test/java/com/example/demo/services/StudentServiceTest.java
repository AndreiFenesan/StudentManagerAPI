package com.example.demo.services;

import com.example.demo.domain.Student;
import com.example.demo.exception.ServiceException;
import com.example.demo.repositories.StudentRepo;
import com.example.demo.services.StudentService;
import com.example.demo.validators.ValidationError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class StudentServiceTest {

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepo studentRepo;
    @Container
    final static GenericContainer container = new GenericContainer(DockerImageName.parse("mongo:4.0.10"))
            .withExposedPorts(27017)
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "test")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", container::getHost);
        registry.add("spring.data.mongodb.port", container::getFirstMappedPort);
        registry.add("spring.data.mongodb.database", () -> "app_db");
        registry.add("spring.data.mongodb.username", () -> "test");
        registry.add("spring.data.mongodb.password", () -> "test");
    }

    @BeforeAll
    static void initAll() {
        container.start();
    }

    @AfterEach
    void destroy() {
        studentRepo.deleteAll();
    }

    @Test
    void containerStartsAndPortIsAvailable() {
        assertThatPortIsAvailable(container);
    }

    private void assertThatPortIsAvailable(GenericContainer container) {
        try {
            new Socket(container.getContainerIpAddress(), container.getFirstMappedPort());
        } catch (IOException e) {
            throw new AssertionError("Port: " + container.getFirstMappedPort() + " is not available");
        }
    }

    @Test
    @DisplayName("The student is added successfully")
    void addStudentSuccessfully() {
        Student student = new Student(
                "Vasile",
                "Micu",
                "MicuVasile",
                "assddsaBGG1234",
                "Vasile.Micu@yahoo.com",
                113,
                "1870818340915",
                null);
        studentService.addStudent(student);
        assertEquals(studentService.getAllStudents().size(), 1);
        Optional<Student> optionalStudent = studentService.findStudentByUsername("MicuVasile");
        assertTrue(optionalStudent.isPresent());
        Student foundStudent = optionalStudent.get();
        assertEquals(foundStudent.getFirstName(), "Vasile");
        assertEquals(foundStudent.getLastName(), "Micu");
        assertEquals(foundStudent.getUsername(), "MicuVasile");
    }

    @Test
    @DisplayName("ServiceError add student with the same username")
    void addStudentWithSameUsername() {
        Student student = new Student(
                "Vasile",
                "Micu",
                "MicuVasile",
                "assddsaBGG1234",
                "Vasile.Micu@yahoo.com",
                113,
                "1870818340915",
                null);
        studentService.addStudent(student);

        Student studentWithSameUsername = new Student(
                "Vassile",
                "Miceu",
                "MicuVasile",
                "assddsaBGG1234",
                "Vasilee.Micu@yahoo.com",
                113,
                "1870818340915",
                null);
        assertEquals(studentService.getAllStudents().size(), 1);
        assertThrows(
                ServiceException.class,
                () -> studentService.addStudent(studentWithSameUsername)
        );
    }

    @Test
    @DisplayName("The student firstname is not valid")
    void addStudentNotValidFirstName() {
        Student student = new Student(
                null,
                "Micu",
                "MicuVasile",
                "assddsaBGG1234",
                "Vasile.Micu@yahoo.com",
                113,
                "5020308945271",
                null);
        assertThrows(ValidationError.class,
                () -> studentService.addStudent(student));
    }

    @Test
    @DisplayName("The student password is not valid")
    void addStudentNotValidPassword() {
        Student student = new Student(
                "Vasile",
                "Micu",
                "MicuVasile",
                "assddsa1234",
                "Vasile.Micu@yahoo.com",
                113,
                "5020308945271",
                null);
        assertThrows(ValidationError.class,
                () -> studentService.addStudent(student));
    }

    @Test
    @DisplayName("Add student: Validation error, invalid email address")
    public void addStudentInvalidEmailAddress() {
        Student student = new Student(
                "Vasile",
                "Micu",
                "MicuVasile",
                "assddsaBGG1234",
                "Vasile.Micu.yahoo.com",
                113,
                "5020308945271",
                null);
        assertThrows(ValidationError.class,
                () -> studentService.addStudent(student));
    }
}