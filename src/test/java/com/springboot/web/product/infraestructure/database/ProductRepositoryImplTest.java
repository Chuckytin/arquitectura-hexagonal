package com.springboot.web.product.infraestructure.database;

import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

    // --------------------------------------------------------------- findAll (Paginado)

    @Test
    void shouldReturnPaginationResultWhenFindAll() {

        // Arrange
        PaginationQuery paginationQuery = new PaginationQuery(0, 10);
        PageRequest pageRequest = PageRequest.of(0, 10);

        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setId(2L);
        productEntity2.setName("Product 2");

        Product product2 = Product.builder().id(2L).name("Product 2").build();

        List<ProductEntity> productEntities = List.of(productEntity, productEntity2);
        Page<ProductEntity> page = new PageImpl<>(productEntities, pageRequest, 2L);

        when(queryProductRepository.findAll(pageRequest)).thenReturn(page);
        when(productEntityMapper.mapToProduct(productEntity)).thenReturn(product);
        when(productEntityMapper.mapToProduct(productEntity2)).thenReturn(product2);

        // Act
        PaginationResult<Product> result = productRepository.findAll(paginationQuery);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(1, result.totalPages()); // 2 elementos con page size 10 = 1 página
        assertEquals(2L, result.totalElements());

        assertEquals(List.of(1L, 2L), result.content().stream().map(Product::getId).toList());

        verify(queryProductRepository, times(1)).findAll(pageRequest);
        verify(productEntityMapper, times(2)).mapToProduct(any(ProductEntity.class));
    }

    @Test
    void shouldReturnPaginationResultWithMultiplePages() {

        // Arrange
        PaginationQuery paginationQuery = new PaginationQuery(0, 2);
        PageRequest pageRequest = PageRequest.of(0, 2);

        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setId(2L);
        productEntity2.setName("Product 2");

        ProductEntity productEntity3 = new ProductEntity();
        productEntity3.setId(3L);
        productEntity3.setName("Product 3");

        Product product2 = Product.builder().id(2L).name("Product 2").build();

        List<ProductEntity> productEntities = List.of(productEntity, productEntity2);
        Page<ProductEntity> page = new PageImpl<>(productEntities, pageRequest, 3L); // 3 elementos totales

        when(queryProductRepository.findAll(pageRequest)).thenReturn(page);
        when(productEntityMapper.mapToProduct(productEntity)).thenReturn(product);
        when(productEntityMapper.mapToProduct(productEntity2)).thenReturn(product2);

        // Act
        PaginationResult<Product> result = productRepository.findAll(paginationQuery);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals(0, result.page());
        assertEquals(2, result.size());
        assertEquals(2, result.totalPages()); // 3 elementos / 2 por página = 2 páginas
        assertEquals(3L, result.totalElements());

        assertEquals(List.of(1L, 2L), result.content().stream().map(Product::getId).toList());

        verify(queryProductRepository, times(1)).findAll(pageRequest);
        verify(productEntityMapper, times(2)).mapToProduct(any(ProductEntity.class));
    }

    @Test
    void shouldReturnSecondPageCorrectly() {

        // Arrange
        PaginationQuery paginationQuery = new PaginationQuery(1, 2); // página 1, tamaño 2
        PageRequest pageRequest = PageRequest.of(1, 2);

        ProductEntity productEntity3 = new ProductEntity();
        productEntity3.setId(3L);
        productEntity3.setName("Product 3");

        Product product3 = Product.builder().id(3L).name("Product 3").build();

        List<ProductEntity> productEntities = List.of(productEntity3);
        Page<ProductEntity> page = new PageImpl<>(productEntities, pageRequest, 3L); // 3 elementos totales

        when(queryProductRepository.findAll(pageRequest)).thenReturn(page);
        when(productEntityMapper.mapToProduct(productEntity3)).thenReturn(product3);

        // Act
        PaginationResult<Product> result = productRepository.findAll(paginationQuery);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(1, result.page());
        assertEquals(2, result.size());
        assertEquals(2, result.totalPages()); // 3 elementos / 2 por página = 2 páginas
        assertEquals(3L, result.totalElements());

        assertEquals(List.of(3L), result.content().stream().map(Product::getId).toList());

        verify(queryProductRepository, times(1)).findAll(pageRequest);
        verify(productEntityMapper, times(1)).mapToProduct(productEntity3);
    }

    @Test
    void shouldReturnEmptyPaginationResultWhenNoProducts() {

        // Arrange
        PaginationQuery paginationQuery = new PaginationQuery(0, 10);
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductEntity> emptyPage = new PageImpl<>(List.of(), pageRequest, 0L);

        when(queryProductRepository.findAll(pageRequest)).thenReturn(emptyPage);

        // Act
        PaginationResult<Product> result = productRepository.findAll(paginationQuery);

        // Assert
        assertNotNull(result);
        assertTrue(result.content().isEmpty());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(0, result.totalPages());
        assertEquals(0L, result.totalElements());

        verify(queryProductRepository, times(1)).findAll(pageRequest);
        verify(productEntityMapper, never()).mapToProduct(any(ProductEntity.class));
    }

    @Test
    void shouldHandleCustomPageSize() {

        // Arrange
        PaginationQuery paginationQuery = new PaginationQuery(0, 20);
        PageRequest pageRequest = PageRequest.of(0, 20);

        List<ProductEntity> productEntities = List.of(productEntity);
        Page<ProductEntity> page = new PageImpl<>(productEntities, pageRequest, 1L);

        when(queryProductRepository.findAll(pageRequest)).thenReturn(page);
        when(productEntityMapper.mapToProduct(productEntity)).thenReturn(product);

        // Act
        PaginationResult<Product> result = productRepository.findAll(paginationQuery);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(0, result.page());
        assertEquals(20, result.size());
        assertEquals(1, result.totalPages());
        assertEquals(1L, result.totalElements());

        verify(queryProductRepository, times(1)).findAll(pageRequest);
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