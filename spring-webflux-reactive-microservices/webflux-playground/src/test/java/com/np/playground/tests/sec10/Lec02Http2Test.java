package com.np.playground.tests.sec10;

import com.np.playground.sec09.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class Lec02Http2Test extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> {
                var poolSize = 10000;
                var provider = ConnectionProvider.builder("np")
                        .lifo() // OS will not maintain all 500 connections active, and it will try to retake older
                        // connections if not used for better use of resources
                        // so newer connections will be used more, that's why we prefer LIFO
                        .maxConnectionPools(poolSize)
                        // queue size (if num of parallel connections is bigger than the pool)
                        //  NOTE: if the connection pool is too big, we can get an error saying Too Many Open Files
                        // when we send the request, it is important to get the response ASAP so that we can release
                        // that connection for the next request
                        .pendingAcquireMaxCount(poolSize * 5)
                        .build();

                var httpClient = HttpClient.create(provider)
                        .protocol(HttpProtocol.H2C)
                        .compress(true)
                        .keepAlive(true);

                b.clientConnector(new ReactorClientHttpConnector(httpClient));
            }

    );

    @Test
    public void concurrentRequests() throws InterruptedException {
        var max = 10000;
        Flux.range(1, max)
                .flatMap(this::getProduct, max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> Assertions.assertEquals(max, l.size()))
                .expectComplete()
                .verify();

        // test will complete after 5s, the connection will get killed, add thread sleep to catch it
        Thread.sleep(60_000);
    }

    private Mono<Product> getProduct(Integer id) {
        return client.get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
