package com.springboot.web.review.application.command.delete;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.review.domain.exception.ReviewNotFoundException;
import com.springboot.web.review.domain.port.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteReviewHandler implements RequestHandler<DeleteReviewRequest, Void> {

    private final ReviewRepository reviewRepository;

    @Override
    public Void handle(DeleteReviewRequest request) {
        if (!reviewRepository.existsById(request.getId())) {
            throw new ReviewNotFoundException(request.getId());
        }
        reviewRepository.deleteById(request.getId());
        log.info("Review deleted with id {}", request.getId());
        return null;
    }

    @Override
    public Class<DeleteReviewRequest> getRequestType() {
        return DeleteReviewRequest.class;
    }
}