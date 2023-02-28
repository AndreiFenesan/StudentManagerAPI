package com.example.demo.utility;

import com.example.demo.domain.AuthorisationTokens;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

public class TokenGenerator {
    public TokenGenerator() {
    }

    private String generateRandomString() {
        Random random = new Random();
        final int stringLength = random.nextInt(6, 10);
        final int lowerLimit = 48;
        final int upperLimit = 122;
        StringBuilder stringBuilder = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            int asciiCode = random.nextInt(lowerLimit, upperLimit + 1);
            stringBuilder.append(Character.toChars(asciiCode));
        }
        return stringBuilder.toString();
    }

    public String getNewToken() {
        return Hashing.sha256().hashString(generateRandomString(), StandardCharsets.UTF_8).toString();
    }

    public AuthorisationTokens getNewAuthToken(String userId){
        String availableAuthenticationToken = this.getNewToken();
        String availableRefreshToken = this.getNewToken();
        LocalDateTime tokenAvailability = LocalDateTime.now().plusMinutes(5);
        return new AuthorisationTokens(userId,availableAuthenticationToken,availableRefreshToken,tokenAvailability);
    }
}
