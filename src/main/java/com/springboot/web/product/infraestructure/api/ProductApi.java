package com.springboot.web.product.infraestructure.api;

import com.springboot.web.product.infraestructure.api.dto.CreateProductDto;
import com.springboot.web.product.infraestructure.api.dto.ProductDto;
import com.springboot.web.product.infraestructure.api.dto.UpdateProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductApi {

    ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(required = false) Long pageSize);

    ResponseEntity<ProductDto> getProductById(@PathVariable Long id);

    ResponseEntity<Void> saveProduct(@RequestBody CreateProductDto createProductdto);

    ResponseEntity<Void> updateProduct(@RequestBody UpdateProductDto updateProductDto);

    ResponseEntity<Void> deleteProduct(@PathVariable Long id);

}
