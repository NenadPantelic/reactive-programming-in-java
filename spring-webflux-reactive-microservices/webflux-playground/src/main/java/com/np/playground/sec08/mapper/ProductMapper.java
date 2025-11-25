package com.np.playground.sec08.mapper;

import com.np.playground.sec08.dto.ProductDTO;
import com.np.playground.sec08.entity.Product;

public class ProductMapper {

    public static Product toEntity(ProductDTO dto) {
        var product = new Product();
        product.setId(dto.id());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        return product;
    }

    public static ProductDTO toDto(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
