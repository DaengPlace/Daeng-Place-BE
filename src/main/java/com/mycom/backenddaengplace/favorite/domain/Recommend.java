package com.mycom.backenddaengplace.favorite.domain;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.review.domain.Review;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "review_count", nullable = false)
    private Long reviewCount;

    @Column(name = "avg_rating", precision = 2, nullable = false)
    private Double avgRating;

    @Builder
    public Recommend(
            Member member,
            Review review,
            Long reviewCount,
            Double avgRating
    ) {
        this.member = member;
        this.review = review;
        this.reviewCount = reviewCount;
        this.avgRating = avgRating;

    }
}
