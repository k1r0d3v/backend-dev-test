package com.example.backenddevtest.core.service.products.impl;

import com.example.backenddevtest.core.model.ProductDetail;
import com.example.backenddevtest.core.service.internal.products.ProductsService;
import com.example.backenddevtest.core.service.products.SimilarProductsQuery;
import com.example.backenddevtest.core.service.products.SimilarProductsQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimilarProductsQueryServiceImpl implements SimilarProductsQueryService {
    private final ProductsService productsService;

    public SimilarProductsQueryServiceImpl(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public List<ProductDetail> execute(SimilarProductsQuery query) {
        return productsService.getSimilarProducts(query.getId());
    }
}
