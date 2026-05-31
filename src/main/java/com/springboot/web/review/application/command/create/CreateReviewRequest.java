package com.springboot.web.review.application.command.create;

import com.springboot.web.common.application.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest implements Request<CreateReviewResponse> {

    private String comment;
    private Integer score;
    private Long productId;
}