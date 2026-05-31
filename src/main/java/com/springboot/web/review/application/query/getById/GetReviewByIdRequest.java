package com.springboot.web.review.application.query.getById;

import com.springboot.web.common.application.mediator.Request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetReviewByIdRequest implements Request<GetReviewByIdResponse> {

    private Long id;
}