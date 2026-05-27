package com.springboot.web.product.infrastructure.database.specification;

import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Especificaciones de JPA para la búsqueda dinámica de productos.
 */
public class ProductSpecification {

    public static Specification<ProductEntity> byName(String name) {
        return likeSpec(name, "name");
    }

    public static Specification<ProductEntity> byDescription(String description) {
        return likeSpec(description, "description");
    }

    public static Specification<ProductEntity> byPrice(Double priceMin, Double priceMax) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (priceMin != null && priceMax != null) {
                return criteriaBuilder.between(root.get("price"), priceMin, priceMax);
            }
            if (priceMin != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin);
            }
            if (priceMax != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax);
            }
            return null;
        };
    }

    private static Specification<ProductEntity> likeSpec(String value, String field) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (value == null || value.isBlank()) {
                return null;
            }
            //return criteriaBuilder.like(root.get(field), "%" + value.trim().toLowerCase() + "%");
            return criteriaBuilder.like(
                    criteriaBuilder.function("lower", String.class, root.get(field)),
                    "%" + value.trim().toLowerCase() + "%"
            );
        };
    }

}