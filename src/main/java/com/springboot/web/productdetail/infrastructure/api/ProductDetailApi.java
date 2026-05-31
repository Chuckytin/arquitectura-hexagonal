package com.springboot.web.productdetail.infrastructure.api;

import com.springboot.web.productdetail.infrastructure.api.dto.CreateProductDetailDto;
import com.springboot.web.productdetail.infrastructure.api.dto.ProductDetailDto;
import com.springboot.web.productdetail.infrastructure.api.dto.UpdateProductDetailDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interfaz ProductDetailApi que define los endpoints para la gestión de detalles de producto.
 */
public interface ProductDetailApi {

    ResponseEntity<ProductDetailDto> getProductDetailById(@PathVariable Long id);

    ResponseEntity<ProductDetailDto> getProductDetailByProductId(@PathVariable Long productId);

    ResponseEntity<Void> createProductDetail(@RequestBody CreateProductDetailDto dto);

    ResponseEntity<Void> updateProductDetail(@RequestBody UpdateProductDetailDto dto);

    ResponseEntity<Void> deleteProductDetail(@PathVariable Long id);
}