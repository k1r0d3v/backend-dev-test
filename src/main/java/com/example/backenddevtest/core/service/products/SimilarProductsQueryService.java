package com.example.backenddevtest.core.service.products;

import com.example.backenddevtest.core.model.ProductDetail;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * Service or input port definition for use case "find similar products".
 */
@Validated
public interface SimilarProductsQueryService {
    List<ProductDetail> execute(@Valid SimilarProductsQuery query);
}
