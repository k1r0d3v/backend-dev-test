package com.example.backenddevtest.repository.products.impl;

import com.example.backenddevtest.core.model.ProductDetail;
import com.example.backenddevtest.repository.exception.DataAccessException;
import com.example.backenddevtest.repository.exception.DataNotFoundException;
import com.example.backenddevtest.repository.products.ProductsRepository;
import com.example.backenddevtest.repository.products.impl.model.ExternalProductDetail;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Products repository mock implementation (an adapter in hexagonal architecture) for testing purposes.
 * <p>
 * The behavior of this class is like follows:
 * <ul>
 *   <li>Only has five products details, identifiers in the range [1, 5].</li>
 *   <li>{@link #getProduct(String)} returns mock product details for identifiers in the range [1, 5], else a
 *   {@link DataNotFoundException} exception is returned.</li>
 *   <li>{@link #getSimilarProductsIds(String)} returns the following values:
 *     <ul>
 *         <li>For product identifier 1, a {@link DataAccessException} is returned</li>
 *         <li>For product identifiers not in range [1, 5], a {@link DataNotFoundException} is returned</li>
 *         <li>Else, returns all products identifier distinct from the given product identifier</li>
 *     </ul>
 *   </li>
 * </ul>
 * Note that this class will be enabled only when property "products.repository.mock" is set to "true".
 */
@Primary
@Repository
@ConditionalOnProperty(name="products.repository.mock", havingValue="true")
public class MockProductsRepository implements ProductsRepository {
    private static final List<String> IDENTIFIERS = Arrays.asList("1", "2", "3", "4", "5");

    @Override
    public Mono<ProductDetail> getProduct(String productId) {
        // Simulates a not found
        if (!IDENTIFIERS.contains(productId)) {
            return Mono.error(new DataNotFoundException("Not found"));
        }

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
