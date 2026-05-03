package com.springboot.web.product.infraestructure.database;

import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.infraestructure.database.entity.ProductEntity;
import com.springboot.web.product.infraestructure.database.mapper.ProductEntityMapper;
import com.springboot.web.product.infraestructure.database.repository.QueryProductRepository;
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
    private QueryProductRepository queryProductRepository;

    @Mock
    private ProductEntityMapper productEntityMapper;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L).name("Product 1").description("Description 1").price(10.0)
                .build();

        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Product 1");
        productEntity.setDescription("Description 1");
        productEntity.setPrice(10.0);
    }

    // ------------------------------------------------------------------ upsert

    @Test
    void shouldReturnSavedProductWhenUpsert() {

        // Arrange
        when(productEntityMapper.mapToProductEntity(product)).thenReturn(productEntity);
        when(queryProductRepository.save(productEntity)).thenReturn(productEntity);
        when(productEntityMapper.mapToProduct(productEntity)).thenReturn(product);

        // Act
        Product result = productRepository.upsert(product);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Product 1", result.getName());

        verify(productEntityMapper, times(1)).mapToProductEntity(product);
        verify(queryProductRepository, times(1)).save(productEntity);
        verify(productEntityMapper, times(1)).mapToProduct(productEntity);
    }

    // --------------------------------------------------------------- findById

    @Test
    void shouldReturnProductWhenFindByIdExists() {

        // Arrange
        when(queryProductRepository.findById(1L)).thenReturn(Optional.of(productEntity));
        when(productEntityMapper.mapToProduct(productEntity)).thenReturn(product);

        // Act
        Optional<Product> result = productRepository.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());

        verify(queryProductRepository, times(1)).findById(1L);
        verify(productEntityMapper, times(1)).mapToProduct(productEntity);
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotExists() {

        // Arrange
        when(queryProductRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productRepository.findById(99L);

        // Assert
        assertTrue(result.isEmpty());

        verify(queryProductRepository, times(1)).findById(99L);
        verify(productEntityMapper, never()).mapToProduct(any(ProductEntity.class));
    }

    // ------------------------------------------------------------- existsById

    @Test
    void shouldReturnTrueWhenProductExists() {

        // Arrange
        when(queryProductRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean exists = productRepository.existsById(1L);

        // Assert
        assertTrue(exists);
        verify(queryProductRepository, times(1)).existsById(1L);
    }

    @Test
    void shouldReturnFalseWhenProductNotExists() {

        // Arrange
        when(queryProductRepository.existsById(99L)).thenReturn(false);

        // Act
        boolean exists = productRepository.existsById(99L);

        // Assert
        assertFalse(exists);
        verify(queryProductRepository, times(1)).existsById(99L);
    }

    // --------------------------------------------------------------- findAll

    @Test
    void shouldReturnAllProducts() {

        // Arrange
        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setId(2L);
        productEntity2.setName("Product 2");

        Product product2 = Product.builder().id(2L).name("Product 2").build();

        when(queryProductRepository.findAll()).thenReturn(List.of(productEntity, productEntity2));
        when(productEntityMapper.mapToProduct(productEntity)).thenReturn(product);
        when(productEntityMapper.mapToProduct(productEntity2)).thenReturn(product2);

        // Act
        List<Product> result = productRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(List.of(1L, 2L), result.stream().map(Product::getId).toList());

        verify(queryProductRepository, times(1)).findAll();
        verify(productEntityMapper, times(2)).mapToProduct(any(ProductEntity.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() {

        // Arrange
        when(queryProductRepository.findAll()).thenReturn(List.of());

        // Act
        List<Product> result = productRepository.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(productEntityMapper, never()).mapToProduct(any(ProductEntity.class));
    }

    // ------------------------------------------------------------- deleteById

    @Test
    void shouldCallDeleteByIdOnRepository() {

        // Arrange
        doNothing().when(queryProductRepository).deleteById(1L);

        // Act
        productRepository.deleteById(1L);

        // Assert
        verify(queryProductRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldNotFailWhenDeleteNonExistentProduct() {

        // Arrange
        doNothing().when(queryProductRepository).deleteById(99L);

        // Act & Assert
        assertDoesNotThrow(() -> productRepository.deleteById(99L));
        verify(queryProductRepository, times(1)).deleteById(99L);
    }
}