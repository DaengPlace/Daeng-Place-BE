package com.mycom.backenddaengplace.review.dto.response;


import com.mycom.backenddaengplace.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class PopularReviewResponse {
    private Long reviewId;
    private String content;
    private Integer rating;
    private List<String> imageUrls;
    private long likeCount;
    private boolean isLiked;
    private LocalDateTime createdAt;
    private Long memberId;
    private String memberName;
    private Long placeId;
    private String placeName;

    public static PopularReviewResponse from(Review review) {
        return PopularReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .rating(review.getRating().intValue())
                .imageUrls(new ArrayList<>())  // S3 설정 전까지 빈 리스트 반환
                .likeCount(review.getReviewLikes().size())
                .isLiked(false)
                .createdAt(review.getCreatedAt())
                .memberId(review.getMember().getId())
                .memberName(review.getMember().getName())
                .placeId(review.getPlace().getId())
                .placeName(review.getPlace().getName())
                .build();
    }
}
