package com.example.demo.services;

import com.example.demo.domain.AuthorisationTokens;
import com.example.demo.domain.Student;
import com.example.demo.domain.UserType;
import com.example.demo.repositories.AuthTokenRepo;
import com.example.demo.repositories.ProfessorRepo;
import com.example.demo.repositories.StudentRepo;
import org.junit.After;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class AuthTokenServiceTest {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private AuthTokenRepo tokenRepo;

    @Autowired
    private ProfessorRepo professorRepo;
    @Autowired
    private AuthTokenService authTokenService;
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
    public void destroy() {
        studentRepo.deleteAll();
        tokenRepo.deleteAll();
    }

    @Test
    @DisplayName("Register user successfully")
    public void authenticateUser() {
        studentService.addStudent("Marius", "Sica", "Mas", "Marius12345");

        Optional<AuthorisationTokens> tokensOptional = authTokenService.registerNewSession("Mas", "Marius12345", UserType.STUDENT);
        assertTrue(tokensOptional.isPresent());
    }

    @Test
    @DisplayName("Authenticate student invalid password")
    public void authenticateUserInvalidPassword() {
        studentService.addStudent("Marius", "Sica", "Mas", "Marius12345");
        Optional<AuthorisationTokens> tokensOptional = authTokenService.registerNewSession("Mas", "Marius1234", UserType.STUDENT);
        assertTrue(tokensOptional.isEmpty());
    }

    @Test
    @DisplayName("Authenticate student invalid username")
    public void authenticateUserInvalidUsername() {
        studentService.addStudent("Marius", "Sica", "Mas", "Marius12345");
        Optional<AuthorisationTokens> tokensOptional = authTokenService.registerNewSession("mas", "Marius12345", UserType.STUDENT);
        assertTrue(tokensOptional.isEmpty());
    }

    @Test
    @DisplayName("Authenticate user token successfully")
    public void renewTokenSuccessfully() {
        Student student = studentService.addStudent("Marius", "Sica", "Mas", "Marius12345");
        Optional<AuthorisationTokens> tokensOptional = authTokenService.registerNewSession("Mas", "Marius12345", UserType.STUDENT);
        assertTrue(tokensOptional.isPresent());

        AuthorisationTokens tokens = tokensOptional.get();
        Optional<AuthorisationTokens> optionalValidTokens = authTokenService
                .authenticateUserToken(student.getId(), tokens.getAuthToken(), tokens.getRefreshToken());

        assertTrue(optionalValidTokens.isPresent());
        AuthorisationTokens validToken = optionalValidTokens.get();

        assertEquals(validToken.getUserId(), student.getId());
        assertEquals(validToken.getAuthToken(), validToken.getAuthToken());
        assertEquals(validToken.getRefreshToken(), validToken.getRefreshToken());
    }

    @Test
    @DisplayName("Authenticate user token fail")
    public void AuthenticateUserTokenFail() {
        Student student = studentService.addStudent("Marius", "Sica", "Mas", "Marius12345");
        Optional<AuthorisationTokens> tokensOptional = authTokenService.registerNewSession("Mas", "Marius12345", UserType.STUDENT);
        assertTrue(tokensOptional.isPresent());

        AuthorisationTokens tokens = tokensOptional.get();
        Optional<AuthorisationTokens> optionalValidTokens = authTokenService
                .authenticateUserToken(student.getId() + "4", tokens.getAuthToken(), tokens.getRefreshToken());
        assertTrue(optionalValidTokens.isEmpty());

        optionalValidTokens = authTokenService
                .authenticateUserToken(student.getId(), tokens.getAuthToken() + "a", tokens.getRefreshToken());
        assertTrue(optionalValidTokens.isEmpty());
    }

    @Test
    @DisplayName("Register new session even if the usser is already logged in")
    public void registerNewSessionUserAlreadyLoggedIn() {
        Student student = studentService.addStudent("Marius", "Sica", "Mas", "Marius12345");
        Optional<AuthorisationTokens> tokensOptional1 = authTokenService.registerNewSession("Mas", "Marius12345", UserType.STUDENT);
        assertTrue(tokensOptional1.isPresent());
        AuthorisationTokens tokens1 = tokensOptional1.get();

        Optional<AuthorisationTokens> tokensOptional2 = authTokenService.registerNewSession("Mas", "Marius12345", UserType.STUDENT);
        assertTrue(tokensOptional2.isPresent());
        AuthorisationTokens tokens2 = tokensOptional2.get();

        assertNotEquals(tokens1.getAuthToken(), tokens2.getAuthToken());
        assertNotEquals(tokens1.getRefreshToken(), tokens2.getRefreshToken());
        assertEquals(tokens1.getUserId(), tokens2.getUserId());


    }

}