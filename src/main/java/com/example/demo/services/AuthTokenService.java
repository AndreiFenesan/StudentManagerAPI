package com.example.demo.services;

import com.example.demo.domain.*;
import com.example.demo.exception.ServiceException;
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

    public Optional<AuthorisationTokens> authenticateUserToken(String userId, String authenticationToken, String refreshToken) {
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

    public Optional<AuthorisationTokens> registerNewSession(String username, String password, UserType userType) throws ServiceException {
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
        if (!authenticateUserToken(user, password)) {
            return Optional.empty();
        }
        AuthorisationTokens authorisationTokens = getTokenForUser(user, userType);
        this.authTokenRepo.save(authorisationTokens);
        return Optional.of(authorisationTokens);

    }

    private AuthorisationTokens getTokenForUser(User user, UserType userType) {
        TokenGenerator generator = new TokenGenerator();
        return generator.getNewAuthToken(user.getId(), userType);
    }

    private boolean authenticateUserToken(User user, String password) throws ServiceException {
        if (!checkPasswords(password, user.getPassword())) {
            return false;
        }
        return !isUserAlreadyLoggedIn(user.getId());
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
