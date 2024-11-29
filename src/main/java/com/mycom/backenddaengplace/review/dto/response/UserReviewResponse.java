package com.mycom.backenddaengplace.review.dto.response;

import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.review.domain.Review;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserReviewResponse {
    private Long reviewId;
    private String nickname;
    private String placeName;
    private Category category;
    private Double rating;
    private String content;
    private LocalDateTime createdAt;
    private String profileImageUrl;

    public static UserReviewResponse from(Review review) {
        return UserReviewResponse.builder()
                .reviewId(review.getId())
                .nickname(review.getMember().getNickname())
                .placeName(review.getPlace().getName())
                .category(review.getPlace().getCategory())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .profileImageUrl(review.getMember().getProfileImageUrl())
                .build();
    }
}
