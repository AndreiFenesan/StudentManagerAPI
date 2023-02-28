package com.example.demo.services;

import com.example.demo.domain.AuthorisationTokens;
import com.example.demo.domain.Student;
import com.example.demo.repositories.AuthTokenRepo;
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

    public boolean authenticateUser(String userId, String authenticationToken, String refreshToken) {
        Optional<AuthorisationTokens> tokenOptional = authTokenRepo.findAuthTokenByUserId(userId);;
        if (tokenOptional.isEmpty()) {
            return false;
        }
        AuthorisationTokens token = tokenOptional.get();
        return authenticationToken.equals(token.getAuthToken()) && refreshToken.equals(token.getRefreshToken())
                && LocalDateTime.now().isBefore(token.getTokenAvailability());
    }

    public Optional<AuthorisationTokens> registerNewSession(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        Optional<Student> optionalStudent = studentRepo.findStudentByUsername(username);
        if (optionalStudent.isEmpty()) {
            return Optional.empty();
        }
        Student student = optionalStudent.get();
        String storedPassword = student.getPassword();
        String encryptedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        if (!storedPassword.equals(encryptedPassword)) {
            return Optional.empty();
        }


        Optional<AuthorisationTokens> optionalToken = authTokenRepo.findAuthTokenByUserId(student.getId());
        if (optionalToken.isPresent()
                && optionalToken.get().getTokenAvailability().isAfter(LocalDateTime.now())) {
            //user already logged in
            return Optional.empty();
        }
        TokenGenerator generator = new TokenGenerator();
        AuthorisationTokens authorisationTokens = generator.getNewAuthToken(student.getId());
        authTokenRepo.save(authorisationTokens);
        return Optional.of(authorisationTokens);
    }

    public Optional<AuthorisationTokens> renewAuthenticationToken(String userId, String refreshToken) {
        System.out.println(userId);
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
        AuthorisationTokens availableAuthorisationTokens = tokenGenerator.getNewAuthToken(userId);
        authTokenRepo.save(availableAuthorisationTokens);
        authTokenRepo.delete(oldToken);
        return Optional.of(availableAuthorisationTokens);
    }
}
