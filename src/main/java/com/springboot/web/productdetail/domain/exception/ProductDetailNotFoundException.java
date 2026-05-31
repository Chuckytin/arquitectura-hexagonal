package com.springboot.web.productdetail.domain.exception;

public class ProductDetailNotFoundException extends RuntimeException {

    public ProductDetailNotFoundException(Long id) {
        super("ProductDetail not found with id: " + id);
    }
}