package com.springboot.web.product.domain.entity;

public record ProductFilter(
        String name,
        String description,
        Double priceMin,
        Double priceMax
) {
}
