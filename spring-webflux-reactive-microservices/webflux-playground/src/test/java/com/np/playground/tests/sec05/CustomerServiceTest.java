package com.np.playground.tests.sec05;


import com.np.playground.sec05.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec05")
public class CustomerServiceTest {

    // just validate HTTP response status codes!
    // unauthorized - no token
    // unauthorized - invalid token
    // standard category - GET - success
    // standard category - POST/PUT/DELETE - forbidden
    // prime category - GET - success
    // prime category - POST/PUT/DELETE - success

    private static final String STANDARD_SECRET_TOKEN = "secret123";
    private static final String PRIME_SECRET_TOKEN = "secret456";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testUnauthorized() {
        this.webTestClient.get()
                .uri("/api/v1/customers")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testPrime() {
        validateGet(PRIME_SECRET_TOKEN, HttpStatus.OK);
        validatePost(PRIME_SECRET_TOKEN, HttpStatus.OK);
    }

    @Test
    public void testStandard() {
        validateGet(STANDARD_SECRET_TOKEN, HttpStatus.OK);
        validatePost(STANDARD_SECRET_TOKEN, HttpStatus.FORBIDDEN);
    }

    public void validateGet(String token, HttpStatus expectedStatus) {
        this.webTestClient.get()
                .uri("/api/v1/customers")
                .header("auth-token", token)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    public void validatePost(String token, HttpStatus expectedStatus) {
        var customerDTO = new CustomerDTO(null, "nenadp", "nenadp@gmail.com");
        this.webTestClient.post()
                .uri("/api/v1/customers")
                .header("auth-token", token)
                .bodyValue(customerDTO)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }
}
