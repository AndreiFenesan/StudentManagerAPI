package com.example.demo.controllers;

import com.example.demo.domain.AuthorisationTokens;
import com.example.demo.domain.UserType;
import com.example.demo.dtos.TokenDto;
import com.example.demo.services.AuthTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/token")
@AllArgsConstructor
public class TokenController {
    private final AuthTokenService tokenService;

    /**
     * methdo that register a new session for a student.
     *
     * @param header map that must values for 2 keys: username and password.
     * @return the student tokens (authentication token and refresh token), if the credentials are valid.
     * Otherwise, returns an error message.
     */
    @PostMapping("/login/student")
    public TokenDto registerStudentSession(@RequestHeader Map<String, String> header) {
        Optional<AuthorisationTokens> optionalAuthToken = tokenService.registerNewSession(header.get("username"), header.get("password"), UserType.STUDENT);
        if (optionalAuthToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        } else {
            return getTokenDto(optionalAuthToken);
        }
    }

    /**
     * method that register a new session for the professor.
     *
     * @param header map that must values for 2 keys: username and password.
     * @return the professor tokens (authentication token and refresh token), if the credentials are valid.
     * Otherwise, returns an error message.
     */
    @PostMapping("/login/professor")
    public TokenDto registerProfessorSession(@RequestHeader Map<String, String> header) {
        Optional<AuthorisationTokens> optionalAuthToken = tokenService.registerNewSession(header.get("username"), header.get("password"), UserType.PROFESSOR);
        if (optionalAuthToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        } else {
            return getTokenDto(optionalAuthToken);
        }
    }

    private TokenDto getTokenDto(Optional<AuthorisationTokens> optionalAuthToken) {
        AuthorisationTokens authorisationTokens = optionalAuthToken.get();
        String authorisationToken = authorisationTokens.getAuthToken();
        String refreshToken = authorisationTokens.getRefreshToken();
        return new TokenDto(authorisationToken, refreshToken);
    }

    /**
     * method that genereate another token for the user who sent the request
     *
     * @param header map that must values for 2 keys: userid and refreshtoken.
     * @return the new generated token, if the provided values are valid.
     * Otherwise, return a error message.
     */
    @PostMapping("/renewtoken")
    public TokenDto renewToken(@RequestHeader Map<String, String> header) {
        Optional<AuthorisationTokens> optionalAuthToken = tokenService.renewAuthenticationToken(header.get("userid"), header.get("refreshtoken"));
        if (optionalAuthToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        } else {
            return getTokenDto(optionalAuthToken);
        }
    }

}
