package com.example.backenddevtest.repository.products;

import com.example.backenddevtest.core.model.ProductDetail;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Definition of a repository for products (or a port definition in hexagonal architecture).
 */
public interface ProductsRepository {
    /**
     * Retrieves a product by identifier.
     *
     * @param productId The product to retrieve identifier.
     * @return A future of the product.
     */
    @NonNull
    Mono<ProductDetail> getProduct(String productId);

    /**
     * Retrieves similar products identifiers to a given product given its identifier.
     *
     * @param productId The reference product identifier.
     * @return A future of products identifiers of the given reference product.
     */
    @NonNull
    Mono<List<String>> getSimilarProductsIds(String productId);
}
