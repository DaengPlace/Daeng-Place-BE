package com.mycom.backenddaengplace.favorite.dto.response;

import com.mycom.backenddaengplace.place.dto.response.PlaceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FavoritesResponse {

    private Long memberId;
    private List<PlaceDto> places;

    public static FavoritesResponse from(Long memberId, List<PlaceDto> places) {
        return FavoritesResponse.builder()
                .memberId(memberId)
                .places(places)
                .build();
    }
}
