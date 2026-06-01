package com.springboot.web.productdetail.infrastructure.database;

import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import com.springboot.web.product.infrastructure.database.repository.QueryProductRepository;
import com.springboot.web.productdetail.domain.entity.ProductDetail;
import com.springboot.web.productdetail.domain.port.ProductDetailRepository;
import com.springboot.web.productdetail.infrastructure.database.entity.ProductDetailEntity;
import com.springboot.web.productdetail.infrastructure.database.mapper.ProductDetailEntityMapper;
import com.springboot.web.productdetail.infrastructure.database.repository.QueryProductDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductDetailRepositoryImpl implements ProductDetailRepository {

    private final QueryProductDetailRepository queryProductDetailRepository;
    private final QueryProductRepository queryProductRepository;
    private final ProductDetailEntityMapper productDetailEntityMapper;

    @Override
    public ProductDetail upsert(ProductDetail productDetail) {
        ProductDetailEntity entity = productDetailEntityMapper.mapToProductDetailEntity(productDetail);
        ProductDetailEntity saved = queryProductDetailRepository.save(entity);
        return productDetailEntityMapper.mapToProductDetail(saved);
    }

    @Override
    public Optional<ProductDetail> findById(Long id) {
        log.info("Finding productDetail by id {}", id);
        return queryProductDetailRepository.findById(id)
                .map(productDetailEntityMapper::mapToProductDetail);
    }

    @Override
    public Optional<ProductDetail> findByProductId(Long productId) {
        log.info("Finding productDetail by productId {}", productId);
        return queryProductDetailRepository.findByProductId(productId)
                .map(productDetailEntityMapper::mapToProductDetail);
    }

    @Override
    public boolean existsById(Long id) {
        return queryProductDetailRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        queryProductDetailRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = "products", key = "#productId")
    public void linkToProduct(Long productDetailId, Long productId) {
        ProductDetailEntity detail = queryProductDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new RuntimeException("ProductDetail not found: " + productDetailId));
        ProductEntity product = queryProductRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        if (product.getProductDetail() != null) {
            throw new RuntimeException("Product " + productId + " already has a product detail assigned");
        }

        product.setProductDetail(detail);
        queryProductRepository.save(product);
    }

    @Override
    public void unlinkFromProduct(Long productDetailId) {
        queryProductRepository.findByProductDetailId(productDetailId)
                .ifPresent(product -> {
                    product.setProductDetail(null);
                    queryProductRepository.save(product);
                });
    }
}