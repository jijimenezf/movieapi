package dev.journey.movieapi;

import dev.journey.movieapi.auth.utils.AuthResponse;
import dev.journey.movieapi.auth.utils.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

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
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(BASE_URL + "/register", registerRequest, AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
