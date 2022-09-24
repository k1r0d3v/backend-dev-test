package com.example.backenddevtest.web.controller.product;

import com.example.backenddevtest.core.model.ProductDetail;
import com.example.backenddevtest.repository.exception.DataAccessException;
import com.example.backenddevtest.repository.exception.DataNotFoundException;
import com.example.backenddevtest.repository.products.ProductsRepository;
import com.example.backenddevtest.repository.products.impl.model.ExternalProductDetail;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Products repository mock implementation (an adapter in hexagonal architecture) for testing purposes.
 */
public class MockProductsRepository implements ProductsRepository {
    private static final List<String> IDENTIFIERS = Arrays.asList("1", "2", "3", "4", "5");

    @Override
    public Mono<ProductDetail> getProduct(String productId) {
        ExternalProductDetail externalProductDetail = new ExternalProductDetail();
        externalProductDetail.setId(productId);
        externalProductDetail.setName("Name");
        externalProductDetail.setPrice(BigDecimal.valueOf(0.1));
        externalProductDetail.setAvailability(true);
        
        return Mono.just(externalProductDetail.toProductDetail());
    }

    @Override
    public Mono<List<String>> getSimilarProductsIds(String productId) {
        // Simulates a timeout for example
        if (IDENTIFIERS.get(0).equals(productId)) {
            return Mono.error(new DataAccessException("Timeout"));
        }

        // Simulates a not found
        if (!IDENTIFIERS.contains(productId)) {
            return Mono.error(new DataNotFoundException("Not found"));
        }

        List<String> identifiers = new ArrayList<>(IDENTIFIERS);
        identifiers.remove(productId);
        return Mono.just(identifiers);
    }
}
