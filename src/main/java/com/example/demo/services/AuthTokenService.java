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

    /**
     * @param userId              string, representing the user who has the authenticationToken.
     * @param authenticationToken string, token of authentication token of user.
     * @param refreshToken        string, refresh token of user
     * @return Optional.empty, if the user does not have valid authentication tokens,
     * otherwise the found authentication tokens.
     */

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

    /**
     * method that register a new session for the user who was the given username and password, if those are valid.
     *
     * @param username the use username.
     * @param password the user password
     * @param userType the user type
     * @return Optional of AuthorisationTokens, if the username and password are valid.
     */

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
        if (!authenticateUserToken(user, password)) {
            return Optional.empty();
        }
        AuthorisationTokens authorisationTokens = getTokenForUser(user, userType);
        this.authTokenRepo.save(authorisationTokens);
        return Optional.of(authorisationTokens);

    }

    /**
     * method that generates a token for the user
     *
     * @param user     the user we generate the tokens for.
     * @param userType the user type
     * @return the generated token
     */
    private AuthorisationTokens getTokenForUser(User user, UserType userType) {
        TokenGenerator generator = new TokenGenerator();
        return generator.getNewAuthToken(user.getId(), userType);
    }

    /**
     * method that checks is the entered password and username are valid, and if the user is not logged in.
     *
     * @param user     - user that we check
     * @param password -  the password we check
     * @return true, if the data is valid, false otherwise.
     */
    private boolean authenticateUserToken(User user, String password) {
        if (!checkPasswords(password, user.getPassword())) {
            return false;
        }
        return !isUserAlreadyLoggedIn(user.getId());
    }

    /**
     * method that checks if the user is logged in.
     * @param userId id of user we check if is logged in.
     * @return true, if user with userId is logged in, false otherwise.
     */
    private boolean isUserAlreadyLoggedIn(String userId) {
        Optional<AuthorisationTokens> optionalToken =
                authTokenRepo.findFirst1AuthorisationTokensByUserIdOrderByTokenAvailabilityDesc(userId);
        //user already logged in
        return optionalToken.isPresent()
                && optionalToken.get().getTokenAvailability().isAfter(LocalDateTime.now());
    }

    /**
     *
     * @param nonHashedPassword password that did not have the sha256 applied.
     * @param storedPassword data stored in the database (already hashed with sha256)
     * @return true, if after hashing nonHashedPassword, this match the stored password.
     */
    private boolean checkPasswords(String nonHashedPassword, String storedPassword) {
        String encryptedPassword = Hashing.sha256().hashString(nonHashedPassword, StandardCharsets.UTF_8).toString();
        return storedPassword.equals(encryptedPassword);
    }

    /**
     *
     * @param userId userId that of the user we renew the token for.
     * @param refreshToken refresh token of the user.
     * @return Optional.empty() if the refresh toke of the user is not valid.
     *          optional of the new generated token, if the refresh toke of the user is valid.
     */

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
