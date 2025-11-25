package com.np.playground.sec08.service;

import com.np.playground.sec08.dto.ProductDTO;
import com.np.playground.sec08.mapper.ProductMapper;
import com.np.playground.sec08.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Flux<ProductDTO> saveProducts(Flux<ProductDTO> flux) {
        return flux.map(ProductMapper::toEntity)
                .as(this.repository::saveAll)
                .map(ProductMapper::toDto);
    }

    public Mono<Long> getProductsCount() {
        return this.repository.count();
    }

    public Flux<ProductDTO> allProducts() {
        return this.repository.findAll()
                .map(ProductMapper::toDto);
    }

}