package com.springboot.web.product.infraestructure.api;

import com.springboot.web.common.mediator.Mediator;
import com.springboot.web.product.application.command.create.CreateProductRequest;
import com.springboot.web.product.application.command.delete.DeleteProductRequest;
import com.springboot.web.product.application.command.update.UpdateProductRequest;
import com.springboot.web.product.application.query.getAll.GetAllProductRequest;
import com.springboot.web.product.application.query.getAll.GetAllProductResponse;
import com.springboot.web.product.application.query.getById.GetProductByIdRequest;
import com.springboot.web.product.application.query.getById.GetProductByIdResponse;
import com.springboot.web.product.infraestructure.api.dto.CreateProductDto;
import com.springboot.web.product.infraestructure.api.dto.ProductDto;
import com.springboot.web.product.infraestructure.api.dto.UpdateProductDto;
import com.springboot.web.product.infraestructure.api.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController implements ProductApi {

    private final Mediator mediator;
    private final ProductMapper productMapper;

    @Operation(summary = "Get all products", description = "Retrieve a list of all products with optional pagination")
    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(defaultValue = "10") Long pageSize) {

        log.info("Getting all products with pageSize={}", pageSize);

        GetAllProductResponse response = mediator.dispatch(new GetAllProductRequest(pageSize));

        List<ProductDto> productsDto = response.getProducts().stream()
                .map(productMapper::mapToProductDto)
                .toList();

        log.info("Found {} products", productsDto.size());

        return ResponseEntity.ok(productsDto);
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

        log.info("Saving product with id {}", createProductdto.getId());

        CreateProductRequest request = productMapper.mapToCreateProductRequest(createProductdto);

        mediator.dispatch(request);

        log.info("Saved product with id {}", createProductdto.getId());

        return ResponseEntity.created(URI.create("/api/v1/products/".concat(createProductdto.getId().toString()))).build();
    }

    @Operation(summary = "Update an existing product", description = "Update the details of an existing product by its ID")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProduct(@ModelAttribute @Valid UpdateProductDto updateProductDto) {

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
