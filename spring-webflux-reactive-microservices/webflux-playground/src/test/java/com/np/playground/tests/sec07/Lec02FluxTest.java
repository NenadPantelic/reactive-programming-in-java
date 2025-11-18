package com.np.playground.tests.sec07;

import com.np.playground.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec02FluxTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void testStreamingGet() throws InterruptedException {
        this.client.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .take(Duration.ofSeconds(3)) // take data for first 3 seconds
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();

        // the server will take 1s to respond
        Thread.sleep(2000);
    }

    @Test
    public void testConcurrentRequests() throws InterruptedException {
        for (int i = 1; i <= 50; i++) {
            this.client.get()
                    .uri("/lec01/product/" + i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .subscribe();
        }

        // the server will take 1s to respond
        Thread.sleep(2000);
    }
}
