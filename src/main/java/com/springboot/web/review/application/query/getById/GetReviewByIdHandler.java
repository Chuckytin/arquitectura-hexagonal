package com.springboot.web.review.application.query.getById;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.review.domain.exception.ReviewNotFoundException;
import com.springboot.web.review.domain.port.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetReviewByIdHandler implements RequestHandler<GetReviewByIdRequest, GetReviewByIdResponse> {

    private final ReviewRepository reviewRepository;

    @Override
    public GetReviewByIdResponse handle(GetReviewByIdRequest request) {
        log.info("GetReviewByIdHandler start for id {}", request.getId());
        return reviewRepository.findById(request.getId())
                .map(GetReviewByIdResponse::new)
                .orElseThrow(() -> new ReviewNotFoundException(request.getId()));
    }

    @Override
    public Class<GetReviewByIdRequest> getRequestType() {
        return GetReviewByIdRequest.class;
    }
}