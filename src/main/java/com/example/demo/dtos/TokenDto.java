package com.example.demo.dtos;

public class TokenDto {
    private final String authenticationToken;
    private final String refreshToken;

    public TokenDto(String authenticationToken, String refreshToken) {
        this.authenticationToken = authenticationToken;
        this.refreshToken = refreshToken;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
