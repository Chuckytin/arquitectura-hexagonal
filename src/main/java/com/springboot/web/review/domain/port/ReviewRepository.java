package com.springboot.web.review.domain.port;

import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.review.domain.entity.Review;

import java.util.Optional;

public interface ReviewRepository {

    Review upsert(Review review);

    Optional<Review> findById(Long id);

    boolean existsById(Long id);

    PaginationResult<Review> findAllByProductId(Long productId, PaginationQuery paginationQuery);

    void deleteById(Long id);
}