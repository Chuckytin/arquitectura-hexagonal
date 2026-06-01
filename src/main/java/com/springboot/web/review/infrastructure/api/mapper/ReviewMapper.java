package com.springboot.web.review.infrastructure.api.mapper;

import com.springboot.web.review.application.command.create.CreateReviewRequest;
import com.springboot.web.review.application.command.update.UpdateReviewRequest;
import com.springboot.web.review.domain.entity.Review;
import com.springboot.web.review.infrastructure.api.dto.CreateReviewDto;
import com.springboot.web.review.infrastructure.api.dto.ReviewDto;
import com.springboot.web.review.infrastructure.api.dto.UpdateReviewDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface ReviewMapper {

    CreateReviewRequest mapToCreateReviewRequest(CreateReviewDto dto);

    UpdateReviewRequest mapToUpdateReviewRequest(UpdateReviewDto dto);

    @BeanMapping(ignoreUnmappedSourceProperties = {"productId"})
    ReviewDto mapToReviewDto(Review review);
}