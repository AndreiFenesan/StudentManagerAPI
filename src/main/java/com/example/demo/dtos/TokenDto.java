package com.example.demo.dtos;

public class TokenDto {
    private final String authenticationToken;
    private final String refreshToken;
    private final String userId;

    public TokenDto(String userId, String authenticationToken, String refreshToken) {
        this.userId = userId;
        this.authenticationToken = authenticationToken;
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
