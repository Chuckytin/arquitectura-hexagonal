package com.springboot.web.product.infrastructure.database.mapper;

import com.springboot.web.category.infrastructure.database.mapper.CategoryEntityMapper;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import com.springboot.web.productdetail.infrastructure.database.mapper.ProductDetailEntityMapper;
import com.springboot.web.review.infrastructure.database.mapper.ReviewEntityMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        uses = {ProductDetailEntityMapper.class, ReviewEntityMapper.class, CategoryEntityMapper.class}
)
public interface ProductEntityMapper {

    @Mapping(target = "reviews", source = "reviews")
    ProductEntity mapToProductEntity(Product product);

    @Mapping(target = "reviews", source = "reviews")
    Product mapToProduct(ProductEntity productEntity);

    @AfterMapping
    default void linkReviews(@MappingTarget ProductEntity productEntity, Product product) {
        if (productEntity.getReviews() != null) {
            productEntity.getReviews().forEach(review -> review.setProduct(productEntity));
        }
    }
}