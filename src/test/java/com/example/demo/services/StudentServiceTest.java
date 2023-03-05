package com.example.demo.services;

import com.example.demo.domain.Student;
import com.example.demo.services.StudentService;
import com.example.demo.validators.ValidationError;
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
                LocalDateTime.now()
        );
        studentService.addStudent(student);
        assertEquals(studentService.getAllStudents().size(),1);
        Optional<Student> optionalStudent = studentService.findStudentByUsername("MicuVasile");
        assertTrue(optionalStudent.isPresent());
        Student foundStudent = optionalStudent.get();
        assertEquals(foundStudent.getFirstName(),"Vasile");
        assertEquals(foundStudent.getLastName(),"Micu");
        assertEquals(foundStudent.getUsername(),"MicuVasile");
    }

    @Test
    @DisplayName("The student firstname is not valid")
    void addStudentNotValidFirstName() {
        Student student = new Student(
                null,
                "Micu",
                "MicuVasile",
                "assddsaBGG1234",
                LocalDateTime.now()
        );
        try {
            studentService.addStudent(student);
            fail();
        }
        catch (ValidationError validationError){
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("The student password is not valid")
    void addStudentNotValidPassword() {
        Student student = new Student(
                "Laurentiu",
                "Micu",
                "MicuVasile",
                "asdasdad81232",
                LocalDateTime.now()
        );
        try {
            studentService.addStudent(student);
            fail();
        }
        catch (ValidationError validationError){
            assertTrue(true);
        }
    }
}