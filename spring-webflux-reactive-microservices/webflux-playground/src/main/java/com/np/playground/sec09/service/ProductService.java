package com.np.playground.sec09.service;

import com.np.playground.sec09.dto.ProductDTO;
import com.np.playground.sec09.mapper.ProductMapper;
import com.np.playground.sec09.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private Sinks.Many<ProductDTO> sink;

    public Mono<ProductDTO> saveProduct(Mono<ProductDTO> mono) {
        return mono.map(ProductMapper::toEntity)
                .flatMap(this.repository::save)
                .map(ProductMapper::toDto)
                .doOnNext(sink::tryEmitNext);
    }

    public Flux<ProductDTO> productStream(Integer maxPrice) {
        Flux<ProductDTO> flux = sink.asFlux();
        return maxPrice == null ? flux : flux.filter(dto -> dto.price() <= maxPrice);
    }
}