package com.springboot.web.review.application.query.getById;

import com.springboot.web.review.domain.entity.Review;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetReviewByIdResponse {

    private Review review;
}