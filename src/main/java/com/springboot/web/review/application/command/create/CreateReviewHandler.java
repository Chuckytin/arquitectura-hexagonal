package com.springboot.web.review.application.command.create;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.review.domain.entity.Review;
import com.springboot.web.review.domain.port.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateReviewHandler implements RequestHandler<CreateReviewRequest, CreateReviewResponse> {

    private final ReviewRepository reviewRepository;

    @Override
    public CreateReviewResponse handle(CreateReviewRequest request) {
        Review review = Review.builder()
                .comment(request.getComment())
                .score(request.getScore())
                .productId(request.getProductId())
                .build();

        Review stored = reviewRepository.upsert(review);
        log.info("Review created with id {}", stored.getId());
        return new CreateReviewResponse(stored);
    }

    @Override
    public Class<CreateReviewRequest> getRequestType() {
        return CreateReviewRequest.class;
    }
}