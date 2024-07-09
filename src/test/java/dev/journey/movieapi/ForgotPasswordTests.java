package dev.journey.movieapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForgotPasswordTests {

    @Autowired
    TestRestTemplate restTemplate;

    private static final String BASE_URL = "/forgotPassword";
    private static final String EMAIL = "jose.jimenez@imdb.com";

    @Test
    void shouldVerifyEmail() {
        ResponseEntity<String> response = restTemplate
                .postForEntity(BASE_URL + "/verifyMail/" + EMAIL, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
