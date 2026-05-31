package com.springboot.web.review.infrastructure.api;

import com.springboot.web.common.application.mediator.Mediator;
import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.review.application.command.create.CreateReviewRequest;
import com.springboot.web.review.application.command.create.CreateReviewResponse;
import com.springboot.web.review.application.command.delete.DeleteReviewRequest;
import com.springboot.web.review.application.command.update.UpdateReviewRequest;
import com.springboot.web.review.application.query.getAll.GetAllReviewRequest;
import com.springboot.web.review.application.query.getAll.GetAllReviewResponse;
import com.springboot.web.review.application.query.getById.GetReviewByIdRequest;
import com.springboot.web.review.application.query.getById.GetReviewByIdResponse;
import com.springboot.web.review.domain.entity.Review;
import com.springboot.web.review.infrastructure.api.dto.CreateReviewDto;
import com.springboot.web.review.infrastructure.api.dto.ReviewDto;
import com.springboot.web.review.infrastructure.api.dto.UpdateReviewDto;
import com.springboot.web.review.infrastructure.api.mapper.ReviewMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/reviews")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Endpoints for managing reviews")
public class ReviewController implements ReviewApi {

    private final Mediator mediator;
    private final ReviewMapper reviewMapper;

    @Operation(summary = "Get all reviews by product")
    @GetMapping("/product/{productId}")
    public ResponseEntity<PaginationResult<ReviewDto>> getAllReviewsByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Getting all reviews for product {}", productId);
        PaginationQuery paginationQuery = new PaginationQuery(pageNumber, pageSize, sortBy, direction);
        GetAllReviewResponse response = mediator.dispatch(new GetAllReviewRequest(productId, paginationQuery));

        PaginationResult<Review> reviewsPage = response.getPaginationResult();
        PaginationResult<ReviewDto> result = new PaginationResult<>(
                reviewsPage.content().stream().map(reviewMapper::mapToReviewDto).toList(),
                reviewsPage.page(),
                reviewsPage.size(),
                reviewsPage.totalPages(),
                reviewsPage.totalElements()
        );
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get review by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        log.info("Getting review with id {}", id);
        GetReviewByIdResponse response = mediator.dispatch(new GetReviewByIdRequest(id));
        return ResponseEntity.ok(reviewMapper.mapToReviewDto(response.getReview()));
    }

    @Operation(summary = "Create a new review")
    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody @Valid CreateReviewDto dto) {
        log.info("Creating review for product {}", dto.getProductId());
        CreateReviewRequest request = reviewMapper.mapToCreateReviewRequest(dto);
        CreateReviewResponse response = mediator.dispatch(request);
        return ResponseEntity.created(
                URI.create("/api/v1/reviews/" + response.getReview().getId())
        ).build();
    }

    @Operation(summary = "Update an existing review")
    @PutMapping
    public ResponseEntity<Void> updateReview(@RequestBody @Valid UpdateReviewDto dto) {
        log.info("Updating review with id {}", dto.getId());
        UpdateReviewRequest request = reviewMapper.mapToUpdateReviewRequest(dto);
        mediator.dispatch(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a review")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        log.info("Deleting review with id {}", id);
        mediator.dispatch(new DeleteReviewRequest(id));
        return ResponseEntity.noContent().build();
    }
}