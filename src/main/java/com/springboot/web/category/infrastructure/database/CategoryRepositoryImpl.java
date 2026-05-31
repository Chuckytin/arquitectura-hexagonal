package com.springboot.web.category.infrastructure.database;

import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.category.domain.port.CategoryRepository;
import com.springboot.web.category.infrastructure.database.entity.CategoryEntity;
import com.springboot.web.category.infrastructure.database.mapper.CategoryEntityMapper;
import com.springboot.web.category.infrastructure.database.repository.QueryCategoryRepository;
import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import com.springboot.web.product.infrastructure.database.repository.QueryProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CategoryRepositoryImpl implements CategoryRepository {

    private final QueryCategoryRepository queryCategoryRepository;
    private final QueryProductRepository queryProductRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public Category upsert(Category category) {
        CategoryEntity entity = categoryEntityMapper.mapToCategoryEntity(category);
        CategoryEntity saved = queryCategoryRepository.save(entity);
        return categoryEntityMapper.mapToCategory(saved);
    }

    @Override
    public Optional<Category> findById(Long id) {
        log.info("Finding category by id {}", id);
        return queryCategoryRepository.findById(id)
                .map(categoryEntityMapper::mapToCategory);
    }

    @Override
    public boolean existsById(Long id) {
        return queryCategoryRepository.existsById(id);
    }

    @Override
    public PaginationResult<Category> findAll(PaginationQuery paginationQuery) {
        PageRequest pageRequest = PageRequest.of(
                paginationQuery.page(),
                paginationQuery.size(),
                Sort.by(Sort.Direction.fromString(paginationQuery.direction()), paginationQuery.sortBy())
        );

        Page<CategoryEntity> page = queryCategoryRepository.findAll(pageRequest);

        return new PaginationResult<>(
                page.getContent().stream().map(categoryEntityMapper::mapToCategory).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public void deleteById(Long id) {
        queryCategoryRepository.deleteById(id);
    }

    @Override
    public void assignCategoryToProduct(Long categoryId, Long productId) {
        CategoryEntity category = queryCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));
        ProductEntity product = queryProductRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        if (!product.getCategories().contains(category)) {
            product.getCategories().add(category);
            queryProductRepository.save(product);
            log.info("Assigned category {} to product {}", categoryId, productId);
        }
    }
}