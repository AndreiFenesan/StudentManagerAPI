package com.example.demo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class AuthorisationTokens {
    @Id
    private String id;
    private String userId;
    private String authToken;
    private String refreshToken;

    private LocalDateTime tokenAvailability;

    public AuthorisationTokens(String userId, String authToken, String refreshToken, LocalDateTime tokenAvailability) {
        this.userId = userId;
        this.authToken = authToken;
        this.refreshToken = refreshToken;
        this.tokenAvailability = tokenAvailability;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LocalDateTime getTokenAvailability() {
        return tokenAvailability;
    }
}
