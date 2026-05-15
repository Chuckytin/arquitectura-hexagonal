package com.springboot.web.review.infrastructure.repository;

import com.springboot.web.review.infrastructure.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
