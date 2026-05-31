package com.springboot.web.productdetail.infrastructure.database.repository;

import com.springboot.web.productdetail.infrastructure.database.entity.ProductDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueryProductDetailRepository extends JpaRepository<ProductDetailEntity, Long> {

    Optional<ProductDetailEntity> findByProductId(Long productId);

}
