package com.springboot.web.review.application.command.update;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.review.domain.entity.Review;
import com.springboot.web.review.domain.exception.ReviewNotFoundException;
import com.springboot.web.review.domain.port.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateReviewHandler implements RequestHandler<UpdateReviewRequest, Void> {

    private final ReviewRepository reviewRepository;

    @Override
    public Void handle(UpdateReviewRequest request) {
        Review existing = reviewRepository.findById(request.getId())
                .orElseThrow(() -> new ReviewNotFoundException(request.getId()));

        Review updated = Review.builder()
                .id(existing.getId())
                .comment(request.getComment())
                .score(request.getScore())
                .product(existing.getProduct())
                .build();

        reviewRepository.upsert(updated);
        log.info("Review updated with id {}", request.getId());
        return null;
    }

    @Override
    public Class<UpdateReviewRequest> getRequestType() {
        return UpdateReviewRequest.class;
    }
}