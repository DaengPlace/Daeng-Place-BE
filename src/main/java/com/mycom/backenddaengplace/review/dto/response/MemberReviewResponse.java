package com.mycom.backenddaengplace.review.dto.response;

import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.review.domain.Review;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MemberReviewResponse {
    private Long reviewId;
    private String nickname;
    private String placeName;
    private Category category;
    private Double rating;
    private String content;
    private String traitTag;
    private LocalDateTime createdAt;
    private String profileImageUrl;

    public static MemberReviewResponse from(Review review) {
        return MemberReviewResponse.builder()
                .reviewId(review.getId())
                .nickname(review.getMember().getNickname())
                .placeName(review.getPlace().getName())
                .category(review.getPlace().getCategory())
                .rating(review.getRating())
                .content(review.getContent())
                .traitTag(review.getTraitTag())
                .createdAt(review.getCreatedAt())
                .profileImageUrl(review.getMember().getProfileImageUrl())
                .build();
    }
}
