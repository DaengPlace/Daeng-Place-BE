package com.mycom.backenddaengplace.review.dto.response;

import com.mycom.backenddaengplace.review.domain.Review;
import com.mycom.backenddaengplace.trait.dto.response.TraitTagCountResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReviewResponse {
    private Long reviewId;
    private String memberNickname;
    private Long placeId;
    private Double rating;
    private String content;
    private List<TraitTagCountResponse> traitTags;
    private LocalDateTime createdAt;
    private List<String> imageUrls;  // 추가
    private Long likeCount;  // 추가
    private boolean isLiked;  // 추가

    public static ReviewResponse from(Review review, Long likeCount, boolean isLiked) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .memberNickname(review.getMember().getNickname())  // nickname으로 변경
                .placeId(review.getPlace().getId())
                .rating(review.getRating())
                .content(review.getContent())
                .traitTags(review.getTraitTag().stream()
                        .map(TraitTagCountResponse::from)
                        .collect(Collectors.toList()))
                .createdAt(review.getCreatedAt())
                .imageUrls(new ArrayList<>())  // S3 설정 전까지 빈 리스트 반환
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }
}
