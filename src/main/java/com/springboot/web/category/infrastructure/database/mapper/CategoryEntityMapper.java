package com.springboot.web.category.infrastructure.database.mapper;

import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.category.infrastructure.database.entity.CategoryEntity;
import org.mapstruct.*;

/**
 * Mapper entre CategoryEntity y Category.
 * Category.products y CategoryEntity.productsEntity son campos circulares
 * (apuntan de vuelta a Product/ProductEntity) y se ignoran en ambas
 * direcciones para evitar recursión infinita.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface CategoryEntityMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"products"})
    @Mapping(target = "products", ignore = true)
    CategoryEntity mapToCategoryEntity(Category category);

    @BeanMapping(ignoreUnmappedSourceProperties = {"products"})
    @Mapping(target = "products", ignore = true)
    Category mapToCategory(CategoryEntity categoryEntity);
}