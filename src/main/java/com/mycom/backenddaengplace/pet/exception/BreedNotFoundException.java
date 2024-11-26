package com.mycom.backenddaengplace.pet.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BreedNotFoundException extends BaseException {
    public BreedNotFoundException(Long breedTypeId) {
        super(String.format("견종 ID [%d]를 찾을 수 없습니다.", breedTypeId),
                HttpStatus.NOT_FOUND);
    }
}
