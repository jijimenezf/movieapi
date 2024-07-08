package dev.journey.movieapi;

import dev.journey.movieapi.auth.utils.AuthResponse;
import dev.journey.movieapi.auth.utils.LoginRequest;
import dev.journey.movieapi.auth.utils.RefreshTokenRequest;
import dev.journey.movieapi.auth.utils.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Application.class, AuthController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    private static final String BASE_URL = "/api/v1/auth";

    @Test
    void shouldRegisterANewUser() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("Luisa",
                "luisa.iglesias@imer.com.mx", "la_morrigan", "estavivo107");
        ResponseEntity<AuthResponse> response = restTemplate
                .postForEntity(BASE_URL + "/register", registerRequest, AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldLoginAnExistingUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("jose.jimenez@imdb.com", "Not4r3al$");
        ResponseEntity<AuthResponse> response = restTemplate
                .postForEntity(BASE_URL + "/login", loginRequest, AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldRefreshToken() {
        final String TOKEN = "cbebf44e-8086-4e91-b219-abc2dbb1be41";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken(TOKEN);

        ResponseEntity<AuthResponse> response = restTemplate
                .postForEntity(BASE_URL + "/refresh", refreshTokenRequest, AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        AuthResponse result = response.getBody();
        assertNotNull(result.getAccessToken());
    }
}
