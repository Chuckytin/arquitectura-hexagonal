package com.springboot.web.product.infraestructure.database.repository;

import com.springboot.web.product.infraestructure.database.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryProductRepository extends JpaRepository<ProductEntity, Long> {


}
