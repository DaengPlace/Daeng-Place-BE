package com.mycom.backenddaengplace.place.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FavoriteDeleteResponse {
    private String message;
}
