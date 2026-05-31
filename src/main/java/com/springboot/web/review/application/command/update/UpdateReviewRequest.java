package com.springboot.web.review.application.command.update;

import com.springboot.web.common.application.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewRequest implements Request<Void> {

    private Long id;
    private String comment;
    private Integer score;
}