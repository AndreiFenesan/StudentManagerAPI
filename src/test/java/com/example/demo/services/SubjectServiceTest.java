package com.example.demo.services;

import com.example.demo.domain.Subject;
import com.example.demo.repositories.SubjectRepo;
import com.example.demo.validators.ValidationError;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class SubjectServiceTest {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectRepo subjectRepo;
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
        this.subjectRepo.deleteAll();
    }


    @Test
    @DisplayName("Find subject by subjectCode")
    void findSubjectBySubjectCode() {
        Subject subject = new Subject("MLR1234", 6);
        assertNotNull(subjectService.addSubject(subject));

        Optional<Subject> optionalSubject = this.subjectService.findSubjectBySubjectCode("MLR1234");
        assertTrue(optionalSubject.isPresent());
        Subject subjectFromDb = optionalSubject.get();
        assertEquals(subjectFromDb.getSubjectCode(), "MLR1234");
        assertEquals(subjectFromDb.getNumberOfCredits(), 6);
    }

    @Test
    @DisplayName("Subject added successfully: tried adding subjects with the same subjectCode")
    void addSubjectTest() {
        Subject subject = new Subject("MLR12345", 6);
        assertNotNull(subjectService.addSubject(subject));
        assertNull(subjectService.addSubject(subject));
        assertEquals(subjectRepo.findAll().size(), 1);
    }

    @Test
    @DisplayName("Subject is not valid: null properties")
    void addSubjectNotValid() {
        try {
            subjectService.addSubject(new Subject(null, null));
            fail();
        } catch (ValidationError validationError) {
            assertTrue(true);
        }

    }

    @Test
    @DisplayName("Subject not valid: invalid subjectCode")
    void invalidSubjectCode() {
        try {
            subjectService.addSubject(new Subject("ML", 7));
            fail();
        } catch (ValidationError validationError) {
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("Subject not valid: invalid numberOfCredits")
    void invalidNumberOfCredits() {
        try {
            subjectService.addSubject(new Subject("MLR", 0));
            fail();
        } catch (ValidationError validationError) {
            assertTrue(true);
        }
    }

}