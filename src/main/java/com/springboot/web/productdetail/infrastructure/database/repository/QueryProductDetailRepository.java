package com.springboot.web.productdetail.infrastructure.database.repository;

import com.springboot.web.productdetail.infrastructure.database.entity.ProductDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QueryProductDetailRepository extends JpaRepository<ProductDetailEntity, Long> {

    @Query("SELECT p.productDetail FROM ProductEntity p WHERE p.id = :productId")
    Optional<ProductDetailEntity> findByProductId(@Param("productId") Long productId);

}
