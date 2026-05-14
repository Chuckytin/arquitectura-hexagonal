package com.springboot.web.product.infrastructure.database.repository;

import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueryProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    boolean existsByName(String name);

    Optional<ProductEntity> findByNameContaining(String name);

    List<ProductEntity> findAllByPriceBetween(Double priceAfter, Double priceBefore);

    long countByPrice(Double price);

    Page<ProductEntity> findAll(Specification<ProductEntity> specification, Pageable pageable);

    @Modifying
    @Query("UPDATE ProductEntity p SET p.price = p.price * :percent")
    int updateAllPricesByPercent(@Param("percent") double percent);

    @EntityGraph(attributePaths = {"productDetailEntity"})
    Optional<ProductEntity> findById(Long id);

}
