package com.springboot.web.product.infraestructure.database.repository;

import com.springboot.web.product.infraestructure.database.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    //Page<ProductEntity> findAll(Pageable pageable, Specification<ProductEntity> specification);

    @Modifying
    @Query("UPDATE ProductEntity p SET p.price = p.price * :percent")
    int updateAllPricesByPercent(@Param("percent") double percent);
}
