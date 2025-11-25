package com.np.playground.tests.sec08;

import com.np.playground.sec08.dto.ProductDTO;
import com.np.playground.sec08.dto.UploadResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductClient {

    private final WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();

    //    public Mono<UploadResponse> uploadProducts(Flux<ProductDTO> flux) {
    public Flux<ProductDTO> uploadProducts(Flux<ProductDTO> flux) {
        return this.client.post()
                .uri("/products/upload")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(flux, ProductDTO.class)
                .retrieve()
//                .bodyToMono(UploadResponse.class);
                .bodyToFlux(ProductDTO.class);
    }

    public Flux<ProductDTO> downloadProducts() {
        return this.client.get()
                .uri("/products/download")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(ProductDTO.class);
    }

}