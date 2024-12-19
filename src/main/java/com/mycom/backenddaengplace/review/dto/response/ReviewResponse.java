package com.mycom.backenddaengplace.review.dto.response;

import com.mycom.backenddaengplace.review.domain.MediaFile;
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
    private String memberProfileImage;
    private Long placeId;
    private Double rating;
    private String content;
    private List<TraitTagCountResponse> traitTags;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private Long likeCount;
    private boolean isLiked;

    public static ReviewResponse from(Review review, Long likeCount, boolean isLiked) {
        List<String> imageUrls = review.getMediaFiles() != null ?
                review.getMediaFiles().stream()
                        .map(MediaFile::getFilePath)
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        String nickName = (Boolean.TRUE.equals(review.getMember().getIsDeleted())) ? "삭제된 사용자" : review.getMember().getNickname();


        String profileImage = (Boolean.TRUE.equals(review.getMember().getIsDeleted()))
                ? null
                : review.getMember().getProfileImageUrl();

        return ReviewResponse.builder()
                .reviewId(review.getId())
                .memberNickname(nickName)
                .memberProfileImage(profileImage)
                .placeId(review.getPlace().getId())
                .rating(review.getRating())
                .content(review.getContent())
                .traitTags(review.getTraitTag().stream()
                        .map(TraitTagCountResponse::from)
                        .collect(Collectors.toList()))
                .createdAt(review.getCreatedAt())
                .imageUrls(imageUrls)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }
}
