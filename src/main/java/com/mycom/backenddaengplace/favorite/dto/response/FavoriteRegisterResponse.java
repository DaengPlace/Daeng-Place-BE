package com.mycom.backenddaengplace.favorite.dto.response;

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
}
