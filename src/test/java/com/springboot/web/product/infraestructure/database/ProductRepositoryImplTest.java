package com.springboot.web.product.infraestructure.database;

import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.infraestructure.database.entity.ProductEntity;
import com.springboot.web.product.infraestructure.database.mapper.ProductEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {

    @Mock
    private ProductEntityMapper productEntityMapper;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private Product product1;
    private ProductEntity productEntity1;

    @BeforeEach
    void setUp() {
        // Limpiamos la lista en memoria antes de cada test
        productRepository.products.clear();

        product1 = Product.builder()
                .id(1L).name("Product 1").description("Description 1").price(10.0)
                .build();

        productEntity1 = new ProductEntity();
        productEntity1.setId(1L);
        productEntity1.setName("Product 1");
    }

    @Test
    void shouldInsertProductWhenUpsertAndProductNotExists() {

        // Arrange
        when(productEntityMapper.mapToProductEntity(any(Product.class)))
                .thenAnswer(invocation -> {
                    System.out.println(">>> mapToProductEntity() llamado - producto nuevo");
                    return productEntity1;
                });

        // Act
        productRepository.upsert(product1);

        // Assert
        assertEquals(1, productRepository.products.size());
        assertEquals(1L, productRepository.products.getFirst().getId());

        verify(productEntityMapper, times(1)).mapToProductEntity(any(Product.class));
    }

    @Test
    void shouldUpdateProductWhenUpsertAndProductAlreadyExists() {

        // Arrange
        productRepository.products.add(productEntity1); // simulamos que ya existe

        ProductEntity updatedEntity = new ProductEntity();
        updatedEntity.setId(1L);
        updatedEntity.setName("Product 1 updated");

        Product updatedProduct = Product.builder()
                .id(1L).name("Product 1 updated").description("desc").price(99.0)
                .build();

        when(productEntityMapper.mapToProductEntity(any(Product.class)))
                .thenAnswer(invocation -> {
                    System.out.println(">>> mapToProductEntity() llamado - producto existente, actualizando");
                    return updatedEntity;
                });

        // Act
        productRepository.upsert(updatedProduct);

        // Assert
        assertEquals(1, productRepository.products.size()); // no se duplicó
        assertEquals("Product 1 updated", productRepository.products.getFirst().getName());
    }

    @Test
    void shouldReturnProductWhenFindByIdExists() {

        // Arrange
        productRepository.products.add(productEntity1);

        when(productEntityMapper.mapToProduct(any(ProductEntity.class)))
                .thenAnswer(invocation -> {
                    System.out.println(">>> mapToProduct() llamado con id: " + productEntity1.getId());
                    return product1;
                });

        // Act
        Optional<Product> result = productRepository.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());

        verify(productEntityMapper, times(1)).mapToProduct(any(ProductEntity.class));
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotExists() {

        // Arrange - lista vacía

        // Act
        Optional<Product> result = productRepository.findById(99L);

        // Assert
        assertTrue(result.isEmpty());
        System.out.println(">>> findById(99) devolvió Optional.empty() correctamente");

        verify(productEntityMapper, never()).mapToProduct(any(ProductEntity.class));
    }

    @Test
    void shouldReturnTrueWhenProductExists() {

        // Arrange
        productRepository.products.add(productEntity1);

        // Act
        boolean exists = productRepository.existsById(1L);

        // Assert
        assertTrue(exists);
        System.out.println(">>> existsById(1) = true");
    }

    @Test
    void shouldReturnFalseWhenProductNotExists() {

        // Act
        boolean exists = productRepository.existsById(99L);

        // Assert
        assertFalse(exists);
        System.out.println(">>> existsById(99) = false");
    }

    @Test
    void shouldReturnAllProducts() {

        // Arrange
        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setId(2L);
        productEntity2.setName("Product 2");

        productRepository.products.add(productEntity1);
        productRepository.products.add(productEntity2);

        Product product2 = Product.builder().id(2L).name("Product 2").build();

        when(productEntityMapper.mapToProduct(any(ProductEntity.class)))
                .thenAnswer(invocation -> {
                    ProductEntity pe = invocation.getArgument(0);
                    System.out.println(">>> mapToProduct() llamado con id: " + pe.getId());
                    return pe.getId().equals(1L) ? product1 : product2;
                });

        // Act
        List<Product> result = productRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(List.of(1L, 2L), result.stream().map(Product::getId).toList());

        verify(productEntityMapper, times(2)).mapToProduct(any(ProductEntity.class));
    }

    @Test
    void shouldDeleteProductById() {

        // Arrange
        productRepository.products.add(productEntity1);

        // Act
        productRepository.deleteById(1L);

        // Assert
        assertTrue(productRepository.products.isEmpty());
        System.out.println(">>> deleteById(1) - lista vacía: " + true);
    }

    @Test
    void shouldNotFailWhenDeleteNonExistentProduct() {

        // Arrange - lista vacía

        // Act & Assert — no debe lanzar excepción
        assertDoesNotThrow(() -> productRepository.deleteById(99L));
        System.out.println(">>> deleteById(99) sobre lista vacía no lanzó excepción");
    }
}