package com.mycom.backenddaengplace.review.dto.response;

import com.mycom.backenddaengplace.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {
    private Long reviewId;
    private Long memberId;
    private Long placeId;
    private Double rating;
    private String content;
    private String traitTag;
    private LocalDateTime createdAt;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .memberId(review.getMember().getId())
                .placeId(review.getPlace().getId())
                .rating(review.getRating())
                .content(review.getContent())
                .traitTag(review.getTraitTag())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
