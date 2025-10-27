package com.np.playground.tests.sec03;

import com.np.playground.sec03.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient // autoconfigure host, port etc.
// Annotation that can be applied to a test class to enable a WebTestClient that is bound directly to the application.
// Tests do not rely upon an HTTP server and use mock requests and responses. At the moment, only WebFlux applications
// are supported.
@SpringBootTest(properties = "sec=sec03")
public class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void testListAllCustomers() {
        this.client.get()
                .uri("/api/v1/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDTO.class)
                .value(list -> log.info("{}", list))
                .hasSize(10);
    }

    @Test
    public void testListPaginatedCustomers() {
        this.client.get()
                .uri("/api/v1/customers/paginated?page=3&size=2")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(r.getResponseBody())))
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(7)
                .jsonPath("$[1].id").isEqualTo(8);
    }

    @Test
    public void testGetCustomerById() {
        this.client.get()
                .uri("/api/v1/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(r.getResponseBody())))
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam")
                .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    public void createAndDeleteCustomer() {
        var dto = new CustomerDTO(null, "marshall", "marshall@gmail.com");
        this.client.post()
                .uri("/api/v1/customers")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(11)
                .jsonPath("$.name").isEqualTo("marshall")
                .jsonPath("$.email").isEqualTo("marshall@gmail.com");

        this.client.delete()
                .uri("/api/v1/customers/11")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();
    }

    @Test
    public void updateCustomer() {
        var dto = new CustomerDTO(null, "noel", "noel@gmail.com");
        this.client.put()
                .uri("/api/v1/customers/10")
                // bodyValue - raw object
                // body - for publisher types, e.g.
                // Mono<CustomerDTO> mono = ...
                // body(mono, CustomerDTO.class)
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(10)
                .jsonPath("$.name").isEqualTo("noel")
                .jsonPath("$.email").isEqualTo("noel@gmail.com");
    }

    @Test
    public void testCustomerNotFound() {
        this.client.get()
                .uri("/api/v1/customers/15")
                .exchange()
                .expectStatus().isNotFound();
    }
}
