package com.springboot.web.review.infrastructure.database.mapper;

import com.springboot.web.review.domain.entity.Review;
import com.springboot.web.review.infrastructure.database.entity.ReviewEntity;
import org.mapstruct.*;

/**
 * Mapper entre ReviewEntity y Review.
 * Review.product y ReviewEntity.productEntity son campos circulares
 * (apuntan de vuelta a Product/ProductEntity) y se ignoran en ambas
 * direcciones para evitar recursión infinita.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface ReviewEntityMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"product"})
    @Mapping(target = "product", ignore = true)
    ReviewEntity mapToReviewEntity(Review review);

    @BeanMapping(ignoreUnmappedSourceProperties = {"product"})
    @Mapping(target = "product", ignore = true)
    Review mapToReview(ReviewEntity reviewEntity);
}