package com.mycom.backenddaengplace.favorite.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FavoriteDeleteResponse {
    private String message;
}
