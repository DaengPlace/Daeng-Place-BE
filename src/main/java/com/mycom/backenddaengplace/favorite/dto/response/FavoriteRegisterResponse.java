package com.mycom.backenddaengplace.favorite.dto.response;

import com.mycom.backenddaengplace.favorite.domain.Favorite;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FavoriteRegisterResponse {
    private Long favoriteId;
    private Long memberId;
    private Long placeId;
    private LocalDateTime createdAt;

    public static FavoriteRegisterResponse from(Favorite favorite) {
        return FavoriteRegisterResponse.builder()
                .favoriteId(favorite.getId())
                .memberId(favorite.getMember().getId())
                .placeId(favorite.getPlace().getId())
                .createdAt(favorite.getCreatedAt())
                .build();
    }
}
