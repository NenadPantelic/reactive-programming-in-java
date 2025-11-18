package com.np.playground.tests.sec07;

import com.np.playground.tests.sec07.AbstractWebClient;
import com.np.playground.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

public class Lec01MonoTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void testSimpleGet() throws InterruptedException {
        this.client.get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .subscribe();

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
