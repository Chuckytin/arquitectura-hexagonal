package com.springboot.web.review.infrastructure.database.repository;

import com.springboot.web.review.infrastructure.database.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
