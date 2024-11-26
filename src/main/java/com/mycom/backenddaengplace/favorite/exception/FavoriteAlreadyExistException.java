package com.mycom.backenddaengplace.favorite.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class FavoriteAlreadyExistException extends BaseException {
    public FavoriteAlreadyExistException(Long favoriteId) {
        super(String.format("즐겨찾기 ID [%d]가 이미 존재합니다.",
                        favoriteId),
                HttpStatus.CONFLICT);
    }
}
