package dev.journey.movieapi.auth.repositories;

import dev.journey.movieapi.auth.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findRefreshToken(String refreshToken);
}
