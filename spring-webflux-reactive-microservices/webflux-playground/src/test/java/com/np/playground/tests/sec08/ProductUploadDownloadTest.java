package com.np.playground.tests.sec08;


import com.np.playground.sec08.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.file.Path;

/*
    Just for demo
 */
public class ProductUploadDownloadTest {

    private static final Logger log = LoggerFactory.getLogger(ProductUploadDownloadTest.class);
    private final ProductClient productClient = new ProductClient();

    @Test
    public void upload() {
        var flux = Flux.range(1, 1_000_000)
                .map(i -> new ProductDTO(null, "product-" + i, i));

        this.productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void download() {
        this.productClient.downloadProducts()
                .map(ProductDTO::toString)
                .as(flux -> FileWriter.create(flux, Path.of("products.txt")))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}