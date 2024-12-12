package com.mycom.backenddaengplace.pet.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BreedApiException extends BaseException {
    public BreedApiException() {
        super("견종 API 호출 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
