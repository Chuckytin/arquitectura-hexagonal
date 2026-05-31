package com.springboot.web.category.infrastructure.api;

import com.springboot.web.category.infrastructure.api.dto.CategoryDto;
import com.springboot.web.category.infrastructure.api.dto.CreateCategoryDto;
import com.springboot.web.category.infrastructure.api.dto.UpdateCategoryDto;
import com.springboot.web.common.domain.PaginationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interfaz CategoryApi que define los endpoints para la gestión de categorías.
 */
public interface CategoryApi {

    ResponseEntity<PaginationResult<CategoryDto>> getAllCategories(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    );

    ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id);

    ResponseEntity<Void> createCategory(@RequestBody CreateCategoryDto dto);

    ResponseEntity<Void> updateCategory(@RequestBody UpdateCategoryDto dto);

    ResponseEntity<Void> deleteCategory(@PathVariable Long id);

    ResponseEntity<Void> assignToProduct(
            @PathVariable Long categoryId,
            @PathVariable Long productId
    );
}