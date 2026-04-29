package com.springboot.web.product.application.query.getById;

import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.exception.ProductNotFoundException;
import com.springboot.web.product.domain.port.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductByIdHandlerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetProductByIdHandler getProductByIdHandler;

    @Test
    void shouldReturnProductById() {

        // Arrange
        Product productMock = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description 1")
                .price(10.0)
                .build();

        when(productRepository.findById(anyLong()))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0);
                    System.out.println(">>> productRepository.findById() llamado con id: " + id);
                    System.out.println(">>> Producto encontrado: id=" + productMock.getId() + ", name=" + productMock.getName());
                    return Optional.of(productMock);
                });

        // Act
        GetProductByIdResponse response = getProductByIdHandler.handle(new GetProductByIdRequest(1L));

        // Assert
        assertNotNull(response);
        assertNotNull(response.getProduct());
        assertEquals(1L, response.getProduct().getId());
        assertEquals("Product 1", response.getProduct().getName());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        // Arrange
        when(productRepository.findById(anyLong()))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0);
                    System.out.println(">>> productRepository.findById() llamado con id: " + id);
                    System.out.println(">>> Producto no encontrado, lanzando ProductNotFoundException");
                    return Optional.empty();
                });

        // Act & Assert
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> getProductByIdHandler.handle(new GetProductByIdRequest(99L))
        );

        System.out.println(">>> Excepción capturada: " + exception.getMessage());

        verify(productRepository, times(1)).findById(99L);
    }
}