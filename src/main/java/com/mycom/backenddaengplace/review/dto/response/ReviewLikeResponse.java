package com.mycom.backenddaengplace.review.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewLikeResponse {
    private Long reviewId;
    private long likeCount;
    private boolean isLiked;

    public static ReviewLikeResponse from(Long reviewId, long likeCount, boolean isLiked) {
        return ReviewLikeResponse.builder()
                .reviewId(reviewId)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }
}