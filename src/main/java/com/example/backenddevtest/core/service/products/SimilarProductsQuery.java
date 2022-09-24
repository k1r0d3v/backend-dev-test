package com.example.backenddevtest.core.service.products;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SimilarProductsQuery {
    @NotNull(message = "Missing product identifier.")
    @NotBlank(message = "Blank product identifier.")
    private final String id;

    public SimilarProductsQuery(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
