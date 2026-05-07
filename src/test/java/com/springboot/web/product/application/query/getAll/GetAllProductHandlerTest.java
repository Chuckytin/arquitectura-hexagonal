package com.springboot.web.product.application.query.getAll;

import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;
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
    void shouldReturnAllProductsWithPagination() {
        // Arrange
        PaginationQuery paginationQuery = new PaginationQuery(0, 10);

        List<Product> productsMock = List.of(
                Product.builder().id(1L).name("Product 1").description("Description 1").price(10.0).build(),
                Product.builder().id(2L).name("Product 2").description("Description 2").price(20.0).build(),
                Product.builder().id(3L).name("Product 3").description("Description 3").price(30.0).build()
        );

        PaginationResult<Product> expectedPaginationResult = new PaginationResult<>(
                productsMock,
                0,      // página actual
                10,     // tamaño de página
                1,      // total de páginas
                3L      // total de elementos
        );

        when(productRepository.findAll(paginationQuery))
                .thenReturn(expectedPaginationResult);

        // Act
        GetAllProductResponse response = getAllProductHandler.handle(new GetAllProductRequest(paginationQuery));

        // Assert
        assertNotNull(response);
        assertNotNull(response.getPaginationResult());

        PaginationResult<Product> result = response.getPaginationResult();
        assertEquals(3, result.content().size());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(1, result.totalPages());
        assertEquals(3, result.totalElements());

        assertEquals(List.of(1L, 2L, 3L),
                result.content().stream().map(Product::getId).toList());

        verify(productRepository, times(1)).findAll(paginationQuery);
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() {
        // Arrange
        PaginationQuery paginationQuery = new PaginationQuery(0, 10);

        PaginationResult<Product> emptyResult = new PaginationResult<>(
                List.of(),
                0,
                10,
                0,
                0L
        );

        when(productRepository.findAll(paginationQuery))
                .thenReturn(emptyResult);

        // Act
        GetAllProductResponse response = getAllProductHandler.handle(new GetAllProductRequest(paginationQuery));

        // Assert
        assertNotNull(response);
        assertNotNull(response.getPaginationResult());

        PaginationResult<Product> result = response.getPaginationResult();
        assertTrue(result.content().isEmpty());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(0, result.totalPages());
        assertEquals(0, result.totalElements());

        verify(productRepository, times(1)).findAll(paginationQuery);
    }

    @Test
    void shouldHandleSecondPageCorrectly() {
        // Arrange
        PaginationQuery paginationQuery = new PaginationQuery(1, 2); // página 1, tamaño 2

        List<Product> secondPageProducts = List.of(
                Product.builder().id(3L).name("Product 3").description("Description 3").price(30.0).build()
        );

        PaginationResult<Product> expectedPaginationResult = new PaginationResult<>(
                secondPageProducts,
                1,      // página actual
                2,      // tamaño de página
                2,      // total de páginas (3 elementos: página 0 con 2, página 1 con 1)
                3L      // total de elementos
        );

        when(productRepository.findAll(paginationQuery))
                .thenReturn(expectedPaginationResult);

        // Act
        GetAllProductResponse response = getAllProductHandler.handle(new GetAllProductRequest(paginationQuery));

        // Assert
        assertNotNull(response);
        assertNotNull(response.getPaginationResult());

        PaginationResult<Product> result = response.getPaginationResult();
        assertEquals(1, result.content().size());
        assertEquals(1, result.page());
        assertEquals(2, result.size());
        assertEquals(2, result.totalPages());
        assertEquals(3, result.totalElements());

        assertEquals(List.of(3L),
                result.content().stream().map(Product::getId).toList());

        verify(productRepository, times(1)).findAll(paginationQuery);
    }

    @Test
    void shouldHandleCustomPageSize() {
        // Arrange
        PaginationQuery paginationQuery = new PaginationQuery(0, 5); // página 0, tamaño 5

        List<Product> productsMock = List.of(
                Product.builder().id(1L).name("Product 1").price(10.0).build(),
                Product.builder().id(2L).name("Product 2").price(20.0).build(),
                Product.builder().id(3L).name("Product 3").price(30.0).build(),
                Product.builder().id(4L).name("Product 4").price(40.0).build(),
                Product.builder().id(5L).name("Product 5").price(50.0).build()
        );

        PaginationResult<Product> expectedResult = new PaginationResult<>(
                productsMock,
                0,
                5,
                2,      // 10 elementos totales / 5 por página = 2 páginas
                10L
        );

        when(productRepository.findAll(paginationQuery))
                .thenReturn(expectedResult);

        // Act
        GetAllProductResponse response = getAllProductHandler.handle(new GetAllProductRequest(paginationQuery));

        // Assert
        assertNotNull(response);
        PaginationResult<Product> result = response.getPaginationResult();
        assertEquals(5, result.content().size());
        assertEquals(5, result.size());
        assertEquals(0, result.page());

        verify(productRepository, times(1)).findAll(paginationQuery);
    }
}