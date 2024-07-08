package dev.journey.movieapi;

import dev.journey.movieapi.auth.entities.RefreshToken;
import dev.journey.movieapi.auth.entities.User;
import dev.journey.movieapi.auth.services.AuthService;
import dev.journey.movieapi.auth.services.JwtService;
import dev.journey.movieapi.auth.services.RefreshTokenService;
import dev.journey.movieapi.auth.utils.AuthResponse;
import dev.journey.movieapi.auth.utils.LoginRequest;
import dev.journey.movieapi.auth.utils.RefreshTokenRequest;
import dev.journey.movieapi.auth.utils.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService,
                          RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refToken.getUser();

        String accessToken = jwtService.generateTokenForUser(user);

        AuthResponse result = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refToken.getRefreshToken())
                .build();

        return ResponseEntity.ok(result);
    }
}
