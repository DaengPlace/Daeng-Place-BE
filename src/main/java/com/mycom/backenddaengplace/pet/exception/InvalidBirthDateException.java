package com.mycom.backenddaengplace.pet.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidBirthDateException extends BaseException {
    public InvalidBirthDateException() {
        super("생년월일은 YYYYMMDD 형식으로 입력해주세요.",
                HttpStatus.BAD_REQUEST);
    }
}
