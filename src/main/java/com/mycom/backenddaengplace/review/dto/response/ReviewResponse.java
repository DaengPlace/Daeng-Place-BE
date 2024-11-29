package com.mycom.backenddaengplace.review.dto.response;

import com.mycom.backenddaengplace.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> imageUrls;  // 추가

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .memberId(review.getMember().getId())
                .placeId(review.getPlace().getId())
                .rating(review.getRating())
                .content(review.getContent())
                .traitTag(review.getTraitTag())
                .createdAt(review.getCreatedAt())
                .imageUrls(new ArrayList<>())  // S3 설정 전까지 빈 리스트 반환
                .build();
    }
}
