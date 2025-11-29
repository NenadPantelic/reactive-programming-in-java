package com.np.playground.tests.sec09;

import com.np.playground.sec09.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

@SpringBootTest(properties = "sec=sec09")
public class ServerSentEventTest {

    private static final Logger log = LoggerFactory.getLogger(ServerSentEventTest.class);

    private final WebTestClient client = WebTestClient.bindToServer()
            .baseUrl("http://localhost:8080") // Replace with your application's base URL
            .build();

    @Test
    public void serverSentEvents() {
        client.get()
                .uri("/products/stream?maxPrice=80")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(ProductDTO.class)
                .getResponseBody()
                .take(3) // to make the test determinstic and stable
                .doOnNext(dto -> log.info("Received: {}", dto))
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list -> {
                    Assertions.assertEquals(3, list.size());
                    Assertions.assertTrue(list.stream().allMatch(p -> p.price() <= 80));
                })
                .expectComplete();
    }
}
