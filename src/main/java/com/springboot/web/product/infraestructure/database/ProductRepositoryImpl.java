package com.springboot.web.product.infraestructure.database;

import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import com.springboot.web.product.infraestructure.database.entity.ProductEntity;
import com.springboot.web.product.infraestructure.database.mapper.ProductEntityMapper;
import com.springboot.web.product.infraestructure.database.repository.QueryProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

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
    public PaginationResult<Product> findAll(PaginationQuery paginationQuery) {

        PageRequest pageRequest = PageRequest.of(paginationQuery.page(), paginationQuery.size());

        Page<ProductEntity> page = queryProductRepository.findAll(pageRequest);

        return new PaginationResult<>(
                page.getContent().stream().map(productEntityMapper::mapToProduct).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @CacheEvict(value = "products", key = "#id")
    @Override
    public void deleteById(Long id) {
        queryProductRepository.deleteById(id);
    }

}
