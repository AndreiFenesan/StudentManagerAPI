package com.example.demo.repositories;

import com.example.demo.domain.AuthorisationTokens;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthTokenRepo extends MongoRepository<AuthorisationTokens, String> {
    public Optional<AuthorisationTokens> findAuthTokenByUserId(String userId);

    Optional<AuthorisationTokens> findFirst1AuthorisationTokensByUserIdOrderByTokenAvailabilityDesc(String userId);
}
