package com.example.backenddevtest.web.controller.product;

import com.example.backenddevtest.core.service.products.SimilarProductsQuery;
import com.example.backenddevtest.core.service.products.SimilarProductsQueryService;
import com.example.backenddevtest.core.model.ProductDetail;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/product")
public class SimilarProductsController {
    private final SimilarProductsQueryService similarProductsQueryService;

    public SimilarProductsController(SimilarProductsQueryService similarProductsQueryService) {
        this.similarProductsQueryService = similarProductsQueryService;
    }

    @GetMapping("/{productId}/similar")
    @ResponseBody
    public List<ProductDetail> similar(@PathVariable String productId) {
        return similarProductsQueryService.execute(new SimilarProductsQuery(productId));
    }
}
