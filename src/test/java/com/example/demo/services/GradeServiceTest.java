package com.example.demo.services;

import com.example.demo.domain.Grade;
import com.example.demo.domain.Student;
import com.example.demo.dtos.GradeDto;
import com.example.demo.exception.ServiceException;
import com.example.demo.repositories.GradeRepo;
import com.example.demo.repositories.StudentRepo;
import com.example.demo.repositories.SubjectRepo;
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

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class GradeServiceTest {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private GradeRepo gradeRepo;
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
    static void init() {
        container.start();
    }

    @AfterEach
    void destroy() {
        this.gradeRepo.deleteAll();
        this.studentRepo.deleteAll();
        this.subjectRepo.deleteAll();
    }

    @Test
    @DisplayName("Grade added successfully")
    void addGradeSuccessfully() {
        Student student = new Student(
                "Alex",
                "Mihai",
                "Alex.Mihai",
                "AlexMih12345",
                "Alex.Mihai@yahoo.com",
                113,
                "5020308945271",
                null);
        student = studentService.addStudent(student);

        subjectService.addSubject("MLR1234", 6);
        subjectService.addSubject("MLR1334", 6);

        Grade grade1 = new Grade(student.getId(), "MLR1234", 8, LocalDate.now());
        Grade grade2 = new Grade(student.getId(), "MLR1334", 10, LocalDate.now());

        this.gradeService.addGrade(grade1);
        this.gradeService.addGrade(grade2);

        List<GradeDto> grades = gradeService.getStudentGrades(student.getId());
        assertEquals(grades.size(), 2);
    }

    @Test
    @DisplayName("Grade is invalid")
    void addGradeValidationException() {
        Student student = new Student(
                "Alex",
                "Mihai",
                "Alex.Mihai",
                "AlexMih12345",
                "Alex.Mihai@yahoo.com",
                113,
                "5020308945271",
                null);
        studentService.addStudent(student);
        subjectService.addSubject("MLR1234", 6);
        Grade grade = new Grade("asdasd", "MLR1234", 0, LocalDate.now());
        assertThrows(
                ValidationError.class,
                () -> this.gradeService.addGrade(grade)
        );
    }

    @Test
    @DisplayName("Student does not exist service error")
    void addGradeStudentDoesNotExist() {
        subjectService.addSubject("MLR1234", 6);
        Grade grade = new Grade("basdads", "MLR1234", 6, LocalDate.now());

        assertThrows(
                ServiceException.class,
                () -> gradeService.addGrade(grade));
    }

    @Test
    @DisplayName("Subject does not exist service error")
    void addGradeSubjectDoesNotExist() {
        Student student = new Student(
                "Alex",
                "Mihai",
                "Alex.Mihai",
                "AlexMih12345",
                "Alex.Mihai@yahoo.com",
                113,
                "5020308945271",
                null);
        studentService.addStudent(student);

        Grade grade = new Grade(student.getId(), "MLR1234", 6, LocalDate.now());

        assertThrows(
                ServiceException.class,
                () -> gradeService.addGrade(grade));
    }

    @Test
    @DisplayName("ServiceError trying to add two grades to the same student and subject")
    void addGradeToTheSameStudent() {
        Student student = new Student(
                "Alex",
                "Mihai",
                "Alex.Mihai",
                "AlexMih12345",
                "Alex.Mihai@yahoo.com",
                113,
                "5020308945271",
                null);
        student = studentService.addStudent(student);

        subjectService.addSubject("MLR1234", 6);

        Grade grade1 = new Grade(student.getId(), "MLR1234", 8, LocalDate.now());
        Grade grade2 = new Grade(student.getId(), "MLR1234", 6, LocalDate.now());

        this.gradeService.addGrade(grade1);

        assertThrows(
                ServiceException.class,
                () -> this.gradeService.addGrade(grade2)
        );

        List<GradeDto> grades = gradeService.getStudentGrades(student.getId());
        assertEquals(grades.size(), 1);
    }

    @Test
    @DisplayName("Get student grades successfully")
    void getStudentGrades() {
        Student student = new Student(
                "Alex",
                "Mihai",
                "Alex.Mihai",
                "AlexMih12345",
                "Alex.Mihai@yahoo.com",
                113,
                "5020308945271",
                null);
        student = studentService.addStudent(student);

        subjectService.addSubject("MLR1111", 3);
        subjectService.addSubject("MLR2222", 4);

        Grade grade1 = new Grade(student.getId(), "MLR1111", 5, LocalDate.now());
        Grade grade2 = new Grade(student.getId(), "MLR2222", 6, LocalDate.now());

        this.gradeService.addGrade(grade1);
        this.gradeService.addGrade(grade2);
        List<GradeDto> grades = gradeService.getStudentGrades(student.getId());
        assertEquals(grades.size(), 2);
        grades.sort(Comparator.comparing(GradeDto::getSubjectCode));
        assertEquals(grades.get(0).getSubjectCode(), "MLR1111");
        assertEquals(grades.get(0).getGrade(), 5);
        assertEquals(grades.get(1).getSubjectCode(), "MLR2222");
        assertEquals(grades.get(1).getGrade(), 6);
    }

    @Test
    @DisplayName("Get student grades: student does not exist")
    void getGradeOfStudentWhoDoesNotExists() {
        assertThrows(
                ServiceException.class,
                () -> gradeService.getStudentGrades("asdasd")
        );
    }

    @Test
    @DisplayName("Delete grades successfully")
    void deleteGradeForStudent() {
        Student student = new Student(
                "Alex",
                "Mihai",
                "Alex.Mihai",
                "AlexMih12345",
                "Alex.Mihai@yahoo.com",
                113,
                "5020308945271",
                null);
        student = studentService.addStudent(student);

        subjectService.addSubject("MLR1234", 6);
        subjectService.addSubject("MLR1334", 6);

        Grade grade1 = new Grade(student.getId(), "MLR1234", 8, LocalDate.now());
        Grade grade2 = new Grade(student.getId(), "MLR1334", 10, LocalDate.now());

        this.gradeService.addGrade(grade1);
        this.gradeService.addGrade(grade2);
        this.gradeService.deleteGradeForStudent(student.getId(), "MLR1234");

        List<GradeDto> grades = gradeService.getStudentGrades(student.getId());
        assertEquals(grades.size(), 1);

        this.gradeService.deleteGradeForStudent(student.getId(), "MLR1334");
        grades = gradeService.getStudentGrades(student.getId());
        assertEquals(grades.size(), 0);

    }

    @Test
    @DisplayName("Delete grade: Grade does not exist")
    void deleteGradeNotExist() {
        Student student = new Student(
                "Alex",
                "Mihai",
                "Alex.Mihai",
                "AlexMih12345",
                "Alex.Mihai@yahoo.com",
                113,
                "5020308945271",
                null);
        studentService.addStudent(student);

        subjectService.addSubject("MLR1234", 6);
        assertThrows(
                ServiceException.class,
                () -> gradeService.deleteGradeForStudent(student.getId(), "MLR1234")
        );

        assertThrows(
                ServiceException.class,
                () -> gradeService.deleteGradeForStudent("blabla", "MLR1234")
        );

        assertThrows(
                ServiceException.class,
                () -> gradeService.deleteGradeForStudent(student.getId(), "MLR1224")
        );

    }
}