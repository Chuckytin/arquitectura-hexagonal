package com.springboot.web.product.infrastructure.database.repository;

import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueryProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    boolean existsByName(String name);

    long countByPrice(Double price);

    @Modifying
    @Query("UPDATE ProductEntity p SET p.price = p.price * :percent")
    int updateAllPricesByPercent(@Param("percent") double percent);

    @EntityGraph(attributePaths = {"productDetail", "reviews", "categories"})
    Optional<ProductEntity> findById(Long id);

    @EntityGraph(attributePaths = {"productDetail", "reviews", "categories"})
    Page<ProductEntity> findAll(@NonNull Specification<ProductEntity> specification, Pageable pageable);

    Optional<ProductEntity> findByProductDetailId(Long productDetailId);
}
