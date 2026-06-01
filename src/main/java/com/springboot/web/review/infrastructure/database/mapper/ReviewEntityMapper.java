package com.springboot.web.review.infrastructure.database.mapper;

import com.springboot.web.review.domain.entity.Review;
import com.springboot.web.review.infrastructure.database.entity.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

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

    @Mapping(target = "product.id", source = "productId")
    ReviewEntity mapToReviewEntity(Review review);

    @Mapping(target = "productId", source = "product.id")
    Review mapToReview(ReviewEntity reviewEntity);
}