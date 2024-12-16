package com.mycom.backenddaengplace.favorite.dto.response;

import com.mycom.backenddaengplace.favorite.domain.Favorite;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteDeleteResponse {
    private String message;

    public static FavoriteDeleteResponse from(Favorite favorite) {
        return FavoriteDeleteResponse.builder().message(favorite.toString()).build();
    }
}
