package com.springboot.web.product.infraestructure.api;

import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.product.infraestructure.api.dto.CreateProductDto;
import com.springboot.web.product.infraestructure.api.dto.ProductDto;
import com.springboot.web.product.infraestructure.api.dto.UpdateProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interfaz ProductApi que define los endpoints para la gestión de productos.
 */
public interface ProductApi {

    ResponseEntity<PaginationResult<ProductDto>> getAllProducts(int pageNumber, int pageSize, String sortBy, String direction, String name, String description, Double priceMin, Double priceMax);

    ResponseEntity<ProductDto> getProductById(@PathVariable Long id);

    ResponseEntity<Void> saveProduct(@RequestBody CreateProductDto createProductdto);

    ResponseEntity<Void> updateProduct(@RequestBody UpdateProductDto updateProductDto);

    ResponseEntity<Void> deleteProduct(@PathVariable Long id);

}
