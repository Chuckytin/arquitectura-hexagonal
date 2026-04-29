package com.springboot.web.product.application.query.getAll;

import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllProductHandlerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetAllProductHandler getAllProductHandler;

    @Test
    void shouldReturnAllProducts() {

        // Arrange
        List<Product> productsMock = List.of(
                Product.builder().id(1L).name("Product 1").description("Description 1").price(10.0).build(),
                Product.builder().id(2L).name("Product 2").description("Description 2").price(20.0).build(),
                Product.builder().id(3L).name("Product 3").description("Description 3").price(30.0).build()
        );

        when(productRepository.findAll())
                .thenAnswer(invocation -> {
                    System.out.println(">>> productRepository.findAll() llamado");
                    System.out.println(">>> Devolviendo " + productsMock.size() + " productos");
                    return productsMock;
                });

        // Act
        GetAllProductResponse response = getAllProductHandler.handle(new GetAllProductRequest());

        // Assert
        assertNotNull(response);
        assertEquals(3, response.getProducts().size());
        assertEquals(List.of(1L, 2L, 3L),
                response.getProducts().stream().map(Product::getId).toList());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() {

        // Arrange
        when(productRepository.findAll())
                .thenAnswer(invocation -> {
                    System.out.println(">>> productRepository.findAll() llamado");
                    System.out.println(">>> No hay productos, devolviendo lista vacía");
                    return List.of();
                });

        // Act
        GetAllProductResponse response = getAllProductHandler.handle(new GetAllProductRequest());

        // Assert
        assertNotNull(response);
        assertTrue(response.getProducts().isEmpty());

        verify(productRepository, times(1)).findAll();
    }
}