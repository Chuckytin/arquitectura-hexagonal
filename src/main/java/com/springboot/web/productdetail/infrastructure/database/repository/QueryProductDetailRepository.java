package com.springboot.web.productdetail.infrastructure.database.repository;

import com.springboot.web.productdetail.infrastructure.database.entity.ProductDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryProductDetailRepository extends JpaRepository<ProductDetailEntity, Long> {
}
