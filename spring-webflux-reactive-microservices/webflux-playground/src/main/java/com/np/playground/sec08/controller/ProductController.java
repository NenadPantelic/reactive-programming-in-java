package com.np.playground.sec08.controller;

import com.np.playground.sec08.dto.ProductDTO;
import com.np.playground.sec08.dto.UploadResponse;
import com.np.playground.sec08.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @PostMapping(value = "upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
//    public Flux<UploadResponse> uploadProducts(@RequestBody Flux<ProductDTO> flux) {
    public Flux<ProductDTO> uploadProducts(@RequestBody Flux<ProductDTO> flux) {
        log.info("invoked"); // executed only once, for other calls the endpoint will use a long-running connection
        // to send data via it;
        // client sends a request only once, and we send back products in a stream
        return this.service.saveProducts(flux);
//                .then(this.service.getProductsCount())
//                .map(count -> new UploadResponse(UUID.randomUUID().toString(), count));
    }

    @GetMapping(value = "download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDTO> downloadProducts() {
        return this.service.allProducts();
    }

}