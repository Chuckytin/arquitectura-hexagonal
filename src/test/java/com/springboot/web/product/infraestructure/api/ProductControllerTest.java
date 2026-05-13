package com.springboot.web.product.infraestructure.api;

import com.springboot.web.common.application.mediator.Mediator;
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
import com.springboot.web.product.infraestructure.api.dto.CreateProductDto;
import com.springboot.web.product.infraestructure.api.dto.ProductDto;
import com.springboot.web.product.infraestructure.api.dto.UpdateProductDto;
import com.springboot.web.product.infraestructure.api.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private Mediator mediator;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController productController;

    @Test
    void shouldReturnAllProducts() {

        PaginationResult<Product> paginationResult = new PaginationResult<>(
                List.of(
                        Product.builder().id(1L).name("Product 1").description("Description 1").price(10.0).build(),
                        Product.builder().id(2L).name("Product 2").description("Description 2").price(20.0).build(),
                        Product.builder().id(3L).name("Product 3").description("Description 3").price(30.0).build(),
                        Product.builder().id(4L).name("Product 4").description("Description 4").price(40.0).build(),
                        Product.builder().id(5L).name("Product 5").description("Description 5").price(50.0).build()
                ),
                0,
                5,
                1,
                5
        );

        GetAllProductResponse responseMock =
                new GetAllProductResponse(paginationResult);

        when(mediator.dispatch(any(GetAllProductRequest.class)))
                .thenReturn(responseMock);

        when(productMapper.mapToProductDto(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product p = invocation.getArgument(0);

                    ProductDto dto = new ProductDto();
                    dto.setId(p.getId());

                    return dto;
                });

        ResponseEntity<PaginationResult<ProductDto>> response =
                productController.getAllProducts(0, 5, "id", "asc", "", "", 0.0, 100.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        PaginationResult<ProductDto> result = response.getBody();

        assertNotNull(result);

        List<ProductDto> products = result.content();

        assertEquals(5, products.size());

        assertEquals(
                List.of(1L, 2L, 3L, 4L, 5L),
                products.stream().map(ProductDto::getId).toList()
        );

        assertEquals(0, result.page());
        assertEquals(5, result.size());
        assertEquals(1, result.totalPages());
        assertEquals(5, result.totalElements());

        verify(mediator, times(1))
                .dispatch(any(GetAllProductRequest.class));
    }

    @Test
    void shouldReturnProductById() {

        // Arrange
        Product productMock = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description 1")
                .price(10.0)
                .build();

        GetProductByIdResponse responseMock = new GetProductByIdResponse(productMock);

        when(mediator.dispatch(any(GetProductByIdRequest.class)))
                .thenAnswer(invocation -> {
                    GetProductByIdRequest req = invocation.getArgument(0);
                    System.out.println(">>> Mediator.dispatch() llamado con id: " + req.getId());
                    return responseMock;
                });

        when(productMapper.mapToProductDto(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product p = invocation.getArgument(0);
                    System.out.println(">>> Mapper llamado con producto: id=" + p.getId() + ", name=" + p.getName());
                    ProductDto dto = new ProductDto();
                    dto.setId(p.getId());
                    System.out.println(">>> DTO creado: id=" + dto.getId());
                    return dto;
                });

        // Act
        ResponseEntity<ProductDto> response = productController.getProductById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(mediator, times(1)).dispatch(any(GetProductByIdRequest.class));
        verify(productMapper, times(1)).mapToProductDto(any(Product.class));
    }

    @Test
    void shouldSaveProduct() {

        // Arrange
        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setName("Product 1");
        createProductDto.setDescription("Description 1");
        createProductDto.setPrice(10.0);

        CreateProductRequest requestMock = new CreateProductRequest(
                createProductDto.getName(),
                createProductDto.getDescription(),
                createProductDto.getPrice(),
                createProductDto.getFile()
        );

        // El producto guardado que devuelve el handler
        Product savedProduct = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description 1")
                .price(10.0)
                .build();

        CreateProductResponse createProductResponse = new CreateProductResponse(savedProduct);

        when(productMapper.mapToCreateProductRequest(any(CreateProductDto.class)))
                .thenReturn(requestMock);

        when(mediator.dispatch(any(CreateProductRequest.class)))
                .thenReturn(createProductResponse);

        // Act
        ResponseEntity<Void> response = productController.saveProduct(createProductDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/api/v1/products/1", response.getHeaders().getLocation().getPath());

        verify(productMapper, times(1)).mapToCreateProductRequest(any(CreateProductDto.class));
        verify(mediator, times(1)).dispatch(any(CreateProductRequest.class));
    }

    @Test
    void shouldUpdateProduct() {

        // Arrange
        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setId(1L);
        updateProductDto.setName("Product 1 updated");
        updateProductDto.setDescription("Description 1 updated");
        updateProductDto.setPrice(99.0);

        UpdateProductRequest requestMock = new UpdateProductRequest(
                updateProductDto.getId(),
                updateProductDto.getName(),
                updateProductDto.getDescription(),
                updateProductDto.getPrice(),
                updateProductDto.getFile()
        );

        when(productMapper.mapToUpdateProductRequest(any(UpdateProductDto.class)))
                .thenAnswer(invocation -> {
                    UpdateProductDto dto = invocation.getArgument(0);
                    System.out.println(">>> Mapper llamado con dto: id=" + dto.getId() + ", name=" + dto.getName());
                    System.out.println(">>> UpdateProductRequest creado: id=" + requestMock.getId());
                    return requestMock;
                });

        when(mediator.dispatch(any(UpdateProductRequest.class)))
                .thenAnswer(invocation -> {
                    UpdateProductRequest req = invocation.getArgument(0);
                    System.out.println(">>> Mediator.dispatch() llamado con: id=" + req.getId());
                    return null;
                });

        // Act
        ResponseEntity<Void> response = productController.updateProduct(updateProductDto);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(productMapper, times(1)).mapToUpdateProductRequest(any(UpdateProductDto.class));
        verify(mediator, times(1)).dispatch(any(UpdateProductRequest.class));
    }

    @Test
    void shouldDeleteProduct() {

        // Arrange
        doAnswer(invocation -> {
            DeleteProductRequest req = invocation.getArgument(0);
            System.out.println(">>> Mediator.dispatchAsync() llamado con id: " + req.getId());
            return null;
        }).when(mediator).dispatchAsync(any(DeleteProductRequest.class));

        // Act
        ResponseEntity<Void> response = productController.deleteProduct(1L);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNull(response.getBody());

        verify(mediator, times(1)).dispatchAsync(any(DeleteProductRequest.class));
    }

}