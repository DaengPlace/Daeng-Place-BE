package com.mycom.backenddaengplace.review.repository;

import com.mycom.backenddaengplace.member.domain.QMember;
import com.mycom.backenddaengplace.place.domain.QPlace;
import com.mycom.backenddaengplace.review.domain.QReview;
import com.mycom.backenddaengplace.review.domain.QReviewLike;
import com.mycom.backenddaengplace.review.domain.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Review> findPopularReviews() {
        QReview review = QReview.review;
        QReviewLike reviewLike = QReviewLike.reviewLike;
        QMember member = QMember.member;
        QPlace place = QPlace.place;

        return queryFactory
                .selectFrom(review)
                .leftJoin(review.member, member).fetchJoin()
                .leftJoin(review.place, place).fetchJoin()
                .leftJoin(review.reviewLikes, reviewLike)
                .groupBy(review.id, member.id, place.id, place.category)
                .orderBy(reviewLike.count().desc())
                .fetch();
    }
}
