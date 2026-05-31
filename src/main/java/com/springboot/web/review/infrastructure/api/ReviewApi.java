package com.springboot.web.review.infrastructure.api;

import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.review.infrastructure.api.dto.CreateReviewDto;
import com.springboot.web.review.infrastructure.api.dto.ReviewDto;
import com.springboot.web.review.infrastructure.api.dto.UpdateReviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interfaz ReviewApi que define los endpoints para la gestión de reseñas.
 */
public interface ReviewApi {

    ResponseEntity<PaginationResult<ReviewDto>> getAllReviewsByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    );

    ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id);

    ResponseEntity<Void> createReview(@RequestBody CreateReviewDto dto);

    ResponseEntity<Void> updateReview(@RequestBody UpdateReviewDto dto);

    ResponseEntity<Void> deleteReview(@PathVariable Long id);
}