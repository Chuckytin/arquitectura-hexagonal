package com.springboot.web.review.application.query.getAll;

import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.review.domain.entity.Review;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllReviewResponse {

    private PaginationResult<Review> paginationResult;
}