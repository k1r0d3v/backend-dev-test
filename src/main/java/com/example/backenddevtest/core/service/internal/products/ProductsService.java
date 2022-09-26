package com.example.backenddevtest.core.service.internal.products;

import com.example.backenddevtest.repository.products.ProductsRepository;
import com.example.backenddevtest.core.model.ProductDetail;
import com.example.backenddevtest.repository.exception.DataNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Internal service that provides new methods with common code (commonly built over repositories).
 */
@Component
public class ProductsService {
    private final ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<ProductDetail> getSimilarProducts(String productId)
    {
        return productsRepository.getSimilarProductsIds(productId)
                // If no product identifiers are found fallback to an empty list
                .onErrorResume(DataNotFoundException.class, exception -> Mono.just(Collections.emptyList()))
                // Generates a Flux of product identifiers
                .flatMapMany(Flux::fromIterable)
                // Retrieve the product details for each product identifier as a Mono and merge it into a single Flux
                .concatMap(productsRepository::getProduct)
                // Collect results
                .collect(Collectors.toList())
                // Wait until the process ends
                .block();
    }
}
