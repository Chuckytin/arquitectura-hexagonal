package com.springboot.web.product.infrastructure.api;

import com.springboot.web.common.application.mediator.Mediator;
import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.product.application.command.create.CreateProductRequest;
import com.springboot.web.product.application.command.create.CreateProductResponse;
import com.springboot.web.product.application.command.delete.DeleteProductRequest;
import com.springboot.web.product.application.command.update.UpdateProductRequest;
import com.springboot.web.product.application.query.getAll.GetAllProductRequest;
import com.springboot.web.product.application.query.getAll.GetAllProductResponse;
import com.springboot.web.product.application.query.getById.GetProductByIdRequest;
import com.springboot.web.product.application.query.getById.GetProductByIdResponse;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.entity.ProductFilter;
import com.springboot.web.product.infrastructure.api.dto.CreateProductDto;
import com.springboot.web.product.infrastructure.api.dto.ProductDto;
import com.springboot.web.product.infrastructure.api.dto.UpdateProductDto;
import com.springboot.web.product.infrastructure.api.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController implements ProductApi {

    private final Mediator mediator;
    private final ProductMapper productMapper;

    @Operation(summary = "Get All products", description = "Retrieve a page of products")
    @GetMapping()
    public ResponseEntity<PaginationResult<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax
    ) {

        log.info("Getting all products with  optional pagination");

        PaginationQuery paginationQuery = new PaginationQuery(pageNumber, pageSize, sortBy, direction);

        ProductFilter productFilter = new ProductFilter(name, description, priceMin, priceMax);

        GetAllProductRequest getAllProductRequest = new GetAllProductRequest(paginationQuery, productFilter);

        GetAllProductResponse response = mediator.dispatch(getAllProductRequest);

        PaginationResult<Product> productsPage = response.getPaginationResult();

        PaginationResult<ProductDto> productDtoPaginationResult = new PaginationResult<>(
                productsPage.content().stream().map(productMapper::mapToProductDto).toList(),
                productsPage.page(),
                productsPage.size(),
                productsPage.totalPages(),
                productsPage.totalElements()
        );

        return ResponseEntity.ok(productDtoPaginationResult);
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a single product by its unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {

        log.info("Getting product with id {}", id);

        GetProductByIdResponse response = mediator.dispatch(new GetProductByIdRequest(id));

        ProductDto productDto = productMapper.mapToProductDto(response.getProduct());

        log.info("Found product with id {}", id);

        return ResponseEntity.ok(productDto);
    }

    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveProduct(@ModelAttribute @Valid CreateProductDto createProductdto) {

        log.info("Saving new product with name {}", createProductdto.getName());

        CreateProductRequest request = productMapper.mapToCreateProductRequest(createProductdto);

        CreateProductResponse response = mediator.dispatch(request);

        Product product = response.getProduct();

        log.info("Saved product with id {}", product.getId());

        return ResponseEntity.created(URI.create("/api/v1/products/".concat(product.getId().toString()))).build();
    }

    @Operation(summary = "Update an existing product", description = "Update the details of an existing product by its ID")
    //@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PutMapping()
    public ResponseEntity<Void> updateProduct(@RequestBody @Valid UpdateProductDto updateProductDto) {

        log.info("Updating product with id {}", updateProductDto.getId());

        UpdateProductRequest request = productMapper.mapToUpdateProductRequest(updateProductDto);

        mediator.dispatch(request);

        log.info("Updated product with id {}", updateProductDto.getId());

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a product", description = "Delete an existing product by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        log.info("Deleting product with id {}", id);

        mediator.dispatchAsync(new DeleteProductRequest(id)); // Eliminar de forma asíncrona.

        log.info("Deleted product {}", id);

        return ResponseEntity.accepted().build();
    }

}
