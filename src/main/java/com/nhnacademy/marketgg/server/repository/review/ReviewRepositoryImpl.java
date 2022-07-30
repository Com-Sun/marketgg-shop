package com.nhnacademy.marketgg.server.repository.review;

import com.nhnacademy.marketgg.server.dto.response.ReviewResponse;
import com.nhnacademy.marketgg.server.entity.QReview;
import com.nhnacademy.marketgg.server.entity.Review;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ReviewRepositoryImpl extends QuerydslRepositorySupport implements ReviewRepositoryCustom {

    public ReviewRepositoryImpl() {
        super(Review.class);
    }

    @Override
    public Page<ReviewResponse> retrieveReviews(final Pageable pageable) {
        QReview review = QReview.review;

        QueryResults<ReviewResponse> results =
            from(review).select(selectAllReviewColumns())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public ReviewResponse queryById(final Long id) {
        QReview review = QReview.review;

        return from(review)
            .select(selectAllReviewColumns())
            .where(review.id.eq(id))
            .fetchOne();
    }

    private ConstructorExpression<ReviewResponse> selectAllReviewColumns() {
        QReview review = QReview.review;

        return Projections.constructor(ReviewResponse.class,
                                       review.id,
                                       review.member.id,
                                       review.asset.id,
                                       review.content,
                                       review.rating,
                                       review.isBest,
                                       review.createdAt,
                                       review.updatedAt,
                                       review.deletedAt);
    }
}
