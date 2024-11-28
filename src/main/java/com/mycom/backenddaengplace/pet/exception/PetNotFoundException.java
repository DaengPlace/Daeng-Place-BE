package com.mycom.backenddaengplace.pet.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PetNotFoundException extends BaseException {
    public PetNotFoundException(Long petId) {
        super(String.format("견종 ID [%d]를 찾을 수 없습니다.", petId),
                HttpStatus.NOT_FOUND);
    }
}
