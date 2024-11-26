package com.mycom.backenddaengplace.place.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteRegisterRequest {

    private Long memberId;
    private Long placeId;

}
