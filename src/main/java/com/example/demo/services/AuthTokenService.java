package com.example.demo.services;

import com.example.demo.domain.*;
import com.example.demo.repositories.AuthTokenRepo;
import com.example.demo.repositories.ProfessorRepo;
import com.example.demo.repositories.StudentRepo;
import com.example.demo.utility.TokenGenerator;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthTokenService {
    private final AuthTokenRepo authTokenRepo;
    private final StudentRepo studentRepo;
    private final ProfessorRepo professorRepo;

    public Optional<AuthorisationTokens> authenticateUser(String userId, String authenticationToken, String refreshToken) {
        Optional<AuthorisationTokens> tokenOptional = authTokenRepo.findFirst1AuthorisationTokensByUserIdOrderByTokenAvailabilityDesc(userId);
        if (tokenOptional.isEmpty()) {
            return Optional.empty();
        }
        AuthorisationTokens token = tokenOptional.get();
        if (authenticationToken.equals(token.getAuthToken()) && refreshToken.equals(token.getRefreshToken())
                && LocalDateTime.now().isBefore(token.getTokenAvailability())) {
            return tokenOptional;
        }
        return Optional.empty();
    }

    public Optional<AuthorisationTokens> registerNewSession(String username, String password, UserType userType) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        User user = null;
        if (userType.equals(UserType.PROFESSOR)) {
            Optional<Professor> optionalProfessor = this.professorRepo.findProfessorByUsername(username);
            if (optionalProfessor.isEmpty()) {
                return Optional.empty();
            }
            user = optionalProfessor.get();
        } else if (userType.equals(UserType.STUDENT)) {
            Optional<Student> optionalStudent = this.studentRepo.findStudentByUsername(username);
            if (optionalStudent.isEmpty()) {
                return Optional.empty();
            }
            user = optionalStudent.get();
        }
        if (user == null) {
            return Optional.empty();
        }
        Optional<AuthorisationTokens> optionalAuthorisationTokens = getTokenForUser(user, password, userType);
        if (optionalAuthorisationTokens.isEmpty()) {
            return Optional.empty();
        }
        AuthorisationTokens authorisationTokens = optionalAuthorisationTokens.get();
        this.authTokenRepo.save(authorisationTokens);
        return Optional.of(authorisationTokens);

    }

    private Optional<AuthorisationTokens> getTokenForUser(User user, String password, UserType userType) {
        if (!checkPasswords(password, user.getPassword())) {
            return Optional.empty();
        }
        if (isUserAlreadyLoggedIn(user.getId())) {
            return Optional.empty();
        }

        TokenGenerator generator = new TokenGenerator();
        AuthorisationTokens authorisationTokens = generator.getNewAuthToken(user.getId(), userType);
        return Optional.of(authorisationTokens);
    }

    private boolean isUserAlreadyLoggedIn(String userId) {
        Optional<AuthorisationTokens> optionalToken =
                authTokenRepo.findFirst1AuthorisationTokensByUserIdOrderByTokenAvailabilityDesc(userId);
        //user already logged in
        return optionalToken.isPresent()
                && optionalToken.get().getTokenAvailability().isAfter(LocalDateTime.now());
    }

    private boolean checkPasswords(String nonHashedPassword, String storedPassword) {
        String encryptedPassword = Hashing.sha256().hashString(nonHashedPassword, StandardCharsets.UTF_8).toString();
        return storedPassword.equals(encryptedPassword);
    }

    public Optional<AuthorisationTokens> renewAuthenticationToken(String userId, String refreshToken) {
        Optional<AuthorisationTokens> tokenOptional = authTokenRepo.findAuthTokenByUserId(userId);
        if (tokenOptional.isEmpty()) {
            return Optional.empty();
        }
        AuthorisationTokens oldToken = tokenOptional.get();
        if (!oldToken.getRefreshToken().equals(refreshToken)
                || oldToken.getTokenAvailability().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }

        if (oldToken.getTokenAvailability().isAfter(LocalDateTime.now().plusMinutes(1))) {
            return Optional.empty();
        }

        TokenGenerator tokenGenerator = new TokenGenerator();
        AuthorisationTokens availableAuthorisationTokens = tokenGenerator.getNewAuthToken(userId, oldToken.getUserType());
        authTokenRepo.save(availableAuthorisationTokens);
        authTokenRepo.delete(oldToken);
        return Optional.of(availableAuthorisationTokens);
    }
}
