package com.mycom.backenddaengplace.favorite.dto.response;

import com.mycom.backenddaengplace.favorite.domain.Favorite;
import com.mycom.backenddaengplace.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FavoritesResponse {

    private Long favoriteId;
    private Long memberId;
    private Long placeId;
    private LocalDateTime createdAt;

    public static FavoritesResponse from(Favorite favorite) {
        return FavoritesResponse.builder()
                .favoriteId(favorite.getId())
                .memberId(favorite.getMember().getId())
                .placeId(favorite.getPlace().getId())
                .createdAt(favorite.getCreatedAt())
                .build();
    }
}
