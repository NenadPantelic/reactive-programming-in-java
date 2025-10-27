package com.np.playground.tests.sec04;

import com.np.playground.sec03.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec04")
public class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void testGetCustomerById() {
        this.client.get()
                .uri("/api/v1/customers/111")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(r.getResponseBody())))
                .jsonPath("$.detail").isEqualTo("Customer [id=111] is not found");
    }

    @Test
    public void testCreateWithInvalidName() {
        var dto = new CustomerDTO(null, null, "marshall@gmail.com");
        this.client.post()
                .uri("/api/v1/customers")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Name is required");
    }

    @Test
    public void testCreateWithNullEmail() {
        var dto = new CustomerDTO(null, "marshall", null);
        this.client.post()
                .uri("/api/v1/customers")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Valid email is required");
    }

    @Test
    public void testCreateWithInvalidEmail() {
        var dto = new CustomerDTO(null, "marshall", "marshallgmail.com");
        this.client.post()
                .uri("/api/v1/customers")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Valid email is required");
    }

    @Test
    public void testUpdateWithInvalidName() {
        var dto = new CustomerDTO(null, null, "marshall@gmail.com");
        this.client.put()
                .uri("/api/v1/customers/9")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Name is required");
    }

    @Test
    public void testUpdateWithInvalidEmail() {
        var dto = new CustomerDTO(null, "marshall", "marshallgmail.com");
        this.client.put()
                .uri("/api/v1/customers/8")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Valid email is required");
    }

    @Test
    public void testUpdateCustomer() {
        var dto = new CustomerDTO(null, "noel", "noel@gmail.com");
        this.client.put()
                .uri("/api/v1/customers/110")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Customer [id=110] is not found");
    }

    @Test
    public void testDeleteCustomerNotFound() {
        this.client.get()
                .uri("/api/v1/customers/15")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Customer [id=15] is not found");
    }
}
