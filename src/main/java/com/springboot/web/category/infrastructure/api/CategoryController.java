package com.springboot.web.category.infrastructure.api;

import com.springboot.web.category.application.command.assign.AssignCategoryToProductRequest;
import com.springboot.web.category.application.command.create.CreateCategoryRequest;
import com.springboot.web.category.application.command.create.CreateCategoryResponse;
import com.springboot.web.category.application.command.delete.DeleteCategoryRequest;
import com.springboot.web.category.application.command.update.UpdateCategoryRequest;
import com.springboot.web.category.application.query.getAll.GetAllCategoryRequest;
import com.springboot.web.category.application.query.getAll.GetAllCategoryResponse;
import com.springboot.web.category.application.query.getById.GetCategoryByIdRequest;
import com.springboot.web.category.application.query.getById.GetCategoryByIdResponse;
import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.category.infrastructure.api.dto.CategoryDto;
import com.springboot.web.category.infrastructure.api.dto.CreateCategoryDto;
import com.springboot.web.category.infrastructure.api.dto.UpdateCategoryDto;
import com.springboot.web.category.infrastructure.api.mapper.CategoryMapper;
import com.springboot.web.common.application.mediator.Mediator;
import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/categories")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Endpoints for managing categories")
public class CategoryController implements CategoryApi {

    private final Mediator mediator;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<PaginationResult<CategoryDto>> getAllCategories(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Getting all categories");
        PaginationQuery paginationQuery = new PaginationQuery(pageNumber, pageSize, sortBy, direction);
        GetAllCategoryResponse response = mediator.dispatch(new GetAllCategoryRequest(paginationQuery));

        PaginationResult<Category> categoriesPage = response.getPaginationResult();
        PaginationResult<CategoryDto> result = new PaginationResult<>(
                categoriesPage.content().stream().map(categoryMapper::mapToCategoryDto).toList(),
                categoriesPage.page(),
                categoriesPage.size(),
                categoriesPage.totalPages(),
                categoriesPage.totalElements()
        );
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        log.info("Getting category with id {}", id);
        GetCategoryByIdResponse response = mediator.dispatch(new GetCategoryByIdRequest(id));
        return ResponseEntity.ok(categoryMapper.mapToCategoryDto(response.getCategory()));
    }

    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CreateCategoryDto dto) {
        log.info("Creating category with name {}", dto.getName());
        CreateCategoryRequest request = categoryMapper.mapToCreateCategoryRequest(dto);
        CreateCategoryResponse response = mediator.dispatch(request);
        return ResponseEntity.created(
                URI.create("/api/v1/categories/" + response.getCategory().getId())
        ).build();
    }

    @Operation(summary = "Update an existing category")
    @PutMapping
    public ResponseEntity<Void> updateCategory(@RequestBody @Valid UpdateCategoryDto dto) {
        log.info("Updating category with id {}", dto.getId());
        UpdateCategoryRequest request = categoryMapper.mapToUpdateCategoryRequest(dto);
        mediator.dispatch(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a category")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Deleting category with id {}", id);
        mediator.dispatch(new DeleteCategoryRequest(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign a category to a product")
    @PostMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Void> assignToProduct(
            @PathVariable Long categoryId,
            @PathVariable Long productId
    ) {
        log.info("Assigning category {} to product {}", categoryId, productId);
        mediator.dispatch(new AssignCategoryToProductRequest(categoryId, productId));
        return ResponseEntity.noContent().build();
    }
}