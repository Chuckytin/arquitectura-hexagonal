package com.springboot.web.productdetail.infrastructure.api;

import com.springboot.web.common.application.mediator.Mediator;
import com.springboot.web.productdetail.application.command.create.CreateProductDetailRequest;
import com.springboot.web.productdetail.application.command.create.CreateProductDetailResponse;
import com.springboot.web.productdetail.application.command.delete.DeleteProductDetailRequest;
import com.springboot.web.productdetail.application.command.update.UpdateProductDetailRequest;
import com.springboot.web.productdetail.application.query.getById.GetProductDetailByIdRequest;
import com.springboot.web.productdetail.application.query.getById.GetProductDetailByIdResponse;
import com.springboot.web.productdetail.application.query.getByProductId.GetProductDetailByProductIdRequest;
import com.springboot.web.productdetail.application.query.getByProductId.GetProductDetailByProductIdResponse;
import com.springboot.web.productdetail.infrastructure.api.dto.CreateProductDetailDto;
import com.springboot.web.productdetail.infrastructure.api.dto.ProductDetailDto;
import com.springboot.web.productdetail.infrastructure.api.dto.UpdateProductDetailDto;
import com.springboot.web.productdetail.infrastructure.api.mapper.ProductDetailMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/product-details")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Product Details", description = "Endpoints for managing product details")
public class ProductDetailController implements ProductDetailApi {

    private final Mediator mediator;
    private final ProductDetailMapper productDetailMapper;

    @Operation(summary = "Get product detail by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDto> getProductDetailById(@PathVariable Long id) {
        log.info("Getting product detail with id {}", id);
        GetProductDetailByIdResponse response = mediator.dispatch(new GetProductDetailByIdRequest(id));
        return ResponseEntity.ok(productDetailMapper.mapToProductDetailDto(response.getProductDetail()));
    }

    @Operation(summary = "Get product detail by product ID")
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDetailDto> getProductDetailByProductId(@PathVariable Long productId) {
        log.info("Getting product detail for product {}", productId);
        GetProductDetailByProductIdResponse response = mediator.dispatch(new GetProductDetailByProductIdRequest(productId));
        return ResponseEntity.ok(productDetailMapper.mapToProductDetailDto(response.getProductDetail()));
    }

    @Operation(summary = "Create a new product detail")
    @PostMapping
    public ResponseEntity<Void> createProductDetail(@RequestBody @Valid CreateProductDetailDto dto) {
        log.info("Creating product detail for product {}", dto.getProductId());
        CreateProductDetailRequest request = productDetailMapper.mapToCreateProductDetailRequest(dto);
        CreateProductDetailResponse response = mediator.dispatch(request);
        return ResponseEntity.created(
                URI.create("/api/v1/product-details/" + response.getProductDetail().getId())
        ).build();
    }

    @Operation(summary = "Update an existing product detail")
    @PutMapping
    public ResponseEntity<Void> updateProductDetail(@RequestBody @Valid UpdateProductDetailDto dto) {
        log.info("Updating product detail with id {}", dto.getId());
        UpdateProductDetailRequest request = productDetailMapper.mapToUpdateProductDetailRequest(dto);
        mediator.dispatch(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a product detail")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductDetail(@PathVariable Long id) {
        log.info("Deleting product detail with id {}", id);
        mediator.dispatch(new DeleteProductDetailRequest(id));
        return ResponseEntity.noContent().build();
    }
}