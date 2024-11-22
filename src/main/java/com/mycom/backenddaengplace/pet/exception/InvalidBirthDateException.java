package com.mycom.backenddaengplace.pet.exception;

import org.springframework.http.HttpStatus;

public class InvalidBirthDateException extends PetException {
    public InvalidBirthDateException() {
        super("생년월일은 YYMMDD 형식으로 입력해주세요.",
                HttpStatus.BAD_REQUEST);
    }
}
