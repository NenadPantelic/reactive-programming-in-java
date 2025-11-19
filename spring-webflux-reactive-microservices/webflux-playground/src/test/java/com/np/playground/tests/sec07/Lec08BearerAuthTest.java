package com.np.playground.tests.sec07;

import com.np.playground.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec08BearerAuthTest extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> b.defaultHeaders(
            h -> h.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))
    );

    @Test
    public void testUriBuilderVariables() {
        this.client.get()
                .uri("/lec08/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
