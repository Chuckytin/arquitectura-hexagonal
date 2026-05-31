package com.springboot.web.category.domain.port;

import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;

import java.util.Optional;

/**
 * Proporciona métodos de acceso a datos para la gestión de categorías,
 * incluyendo operaciones CRUD, paginación y asociación producto-categoría.
 */
public interface CategoryRepository {

    Category upsert(Category category);

    Optional<Category> findById(Long id);

    boolean existsById(Long id);

    PaginationResult<Category> findAll(PaginationQuery paginationQuery);

    void deleteById(Long id);

    void assignCategoryToProduct(Long categoryId, Long productId);
}