package com.mycom.backenddaengplace.favorite.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteDeleteRequest {

    private Long memberId;
    private Long placeId;

}

