package com.mycom.backenddaengplace.place.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PlaceNotFoundException extends BaseException {
    public PlaceNotFoundException(Long placeId) {
        super(String.format("장소 ID [%d]를 찾을 수 없습니다.", placeId),
                HttpStatus.NOT_FOUND);
    }
}
