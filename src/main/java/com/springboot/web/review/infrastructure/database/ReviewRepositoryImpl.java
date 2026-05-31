package com.springboot.web.review.infrastructure.database;

import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.review.domain.entity.Review;
import com.springboot.web.review.domain.port.ReviewRepository;
import com.springboot.web.review.infrastructure.database.entity.ReviewEntity;
import com.springboot.web.review.infrastructure.database.mapper.ReviewEntityMapper;
import com.springboot.web.review.infrastructure.database.repository.QueryReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReviewRepositoryImpl implements ReviewRepository {

    private final QueryReviewRepository queryReviewRepository;
    private final ReviewEntityMapper reviewEntityMapper;

    @Override
    public Review upsert(Review review) {
        ReviewEntity entity = reviewEntityMapper.mapToReviewEntity(review);
        ReviewEntity saved = queryReviewRepository.save(entity);
        return reviewEntityMapper.mapToReview(saved);
    }

    @Override
    public Optional<Review> findById(Long id) {
        log.info("Finding review by id {}", id);
        return queryReviewRepository.findById(id)
                .map(reviewEntityMapper::mapToReview);
    }

    @Override
    public boolean existsById(Long id) {
        return queryReviewRepository.existsById(id);
    }

    @Override
    public PaginationResult<Review> findAllByProductId(Long productId, PaginationQuery paginationQuery) {
        PageRequest pageRequest = PageRequest.of(
                paginationQuery.page(),
                paginationQuery.size(),
                Sort.by(Sort.Direction.fromString(paginationQuery.direction()), paginationQuery.sortBy())
        );

        Page<ReviewEntity> page = queryReviewRepository.findAllByProductId(productId, pageRequest);

        return new PaginationResult<>(
                page.getContent().stream().map(reviewEntityMapper::mapToReview).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public void deleteById(Long id) {
        queryReviewRepository.deleteById(id);
    }
}