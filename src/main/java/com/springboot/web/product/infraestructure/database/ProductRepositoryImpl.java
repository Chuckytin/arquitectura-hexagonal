package com.springboot.web.product.infraestructure.database;

import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import com.springboot.web.product.infraestructure.database.entity.ProductEntity;
import com.springboot.web.product.infraestructure.database.mapper.ProductEntityMapper;
import com.springboot.web.product.infraestructure.database.repository.QueryProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final QueryProductRepository queryProductRepository;

    private final ProductEntityMapper productEntityMapper;

    /**
     * Actualiza o Crea
     */
    @Override
    public Product upsert(Product product) {
        ProductEntity productEntity = productEntityMapper.mapToProductEntity(product);
        ProductEntity productEntitySaved = queryProductRepository.save(productEntity);

        return productEntityMapper.mapToProduct(productEntitySaved);
    }

    @Cacheable(value = "products", key = "#id")
    @Override
    public Optional<Product> findById(Long id) {
        log.info("Finding product by id {}", id);

        return queryProductRepository.findById(id)
                .map(productEntityMapper::mapToProduct);
    }

    @Override
    public boolean existsById(Long id) {
        return queryProductRepository.existsById(id);
    }

    @Override
    public List<Product> findAll() {
        return queryProductRepository.findAll()
                .stream()
                .map(productEntityMapper::mapToProduct)
                .toList();
    }

    @CacheEvict(value = "products", key = "#id")
    @Override
    public void deleteById(Long id) {
        queryProductRepository.deleteById(id);
    }

}
