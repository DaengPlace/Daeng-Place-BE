package com.mycom.backenddaengplace.review.dto.response;

import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.review.domain.Review;
import com.mycom.backenddaengplace.trait.dto.response.TraitTagCountResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class MemberReviewResponse {
    private Long reviewId;
    private Long placeId;
    private String nickname;
    private String placeName;
    private Category category;
    private Double rating;
    private String content;
    private List<TraitTagCountResponse> traitTags;
    private LocalDateTime createdAt;
    private String profileImageUrl;

    public static MemberReviewResponse from(Review review) {
        return MemberReviewResponse.builder()
                .reviewId(review.getId())
                .placeId(review.getPlace().getId())
                .nickname(review.getMember().getNickname())
                .placeName(review.getPlace().getName())
                .category(review.getPlace().getCategory())
                .rating(review.getRating())
                .content(review.getContent())
                .traitTags(review.getTraitTag().stream()
                        .map(TraitTagCountResponse::from)
                        .collect(Collectors.toList()))
                .createdAt(review.getCreatedAt())
                .profileImageUrl(review.getMember().getProfileImageUrl())
                .build();
    }
}
