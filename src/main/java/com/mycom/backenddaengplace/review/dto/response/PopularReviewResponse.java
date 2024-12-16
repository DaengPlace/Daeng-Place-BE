package com.mycom.backenddaengplace.review.dto.response;


import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.review.domain.MediaFile;
import com.mycom.backenddaengplace.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private Category category;

    public static PopularReviewResponse from(Review review) {
        List<String> imageUrls = review.getMediaFiles() != null ?
                review.getMediaFiles().stream()
                        .map(MediaFile::getFilePath)
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        return PopularReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .rating(review.getRating().intValue())
                .imageUrls(imageUrls)
                .likeCount(review.getReviewLikes().size())
                .isLiked(false)
                .createdAt(review.getCreatedAt())
                .memberId(review.getMember().getId())
                .memberName(review.getMember().getName())
                .placeId(review.getPlace().getId())
                .placeName(review.getPlace().getName())
                .category(review.getPlace().getCategory())
                .build();
    }
}
