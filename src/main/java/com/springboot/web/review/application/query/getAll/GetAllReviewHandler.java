package com.springboot.web.review.application.query.getAll;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.review.domain.entity.Review;
import com.springboot.web.review.domain.port.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllReviewHandler implements RequestHandler<GetAllReviewRequest, GetAllReviewResponse> {

    private final ReviewRepository reviewRepository;

    @Override
    public GetAllReviewResponse handle(GetAllReviewRequest request) {
        log.info("GetAllReviewHandler start for productId {}", request.getProductId());
        PaginationResult<Review> reviews = reviewRepository.findAllByProductId(
                request.getProductId(), request.getPaginationQuery()
        );
        return new GetAllReviewResponse(reviews);
    }

    @Override
    public Class<GetAllReviewRequest> getRequestType() {
        return GetAllReviewRequest.class;
    }
}