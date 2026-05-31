package com.springboot.web.review.application.command.create;

import com.springboot.web.review.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewResponse {

    private Review review;
}