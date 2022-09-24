package com.example.backenddevtest.repository.products.impl.model;

import com.example.backenddevtest.core.model.ProductDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

/**
 * Represents the external REST service product details.
 * A change in this model will not affect to the business logic.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalProductDetail {
    private String id;
    private String name;
    private BigDecimal price;
    private Boolean availability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    @NonNull
    public ProductDetail toProductDetail() {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(getId());
        productDetail.setName(getName());
        productDetail.setPrice(getPrice());
        productDetail.setAvailability(getAvailability());

        return productDetail;
    }
}
