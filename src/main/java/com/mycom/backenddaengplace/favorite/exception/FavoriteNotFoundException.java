package com.mycom.backenddaengplace.favorite.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class FavoriteNotFoundException extends BaseException {
    public FavoriteNotFoundException(Long memberId, Long placeId) {
        super(String.format("회원 ID [%d]가 장소 ID [%d]의 즐겨찾기가 없습니다.",
                        memberId, placeId),
                HttpStatus.NOT_FOUND);
    }
}
