package com.mycom.backenddaengplace.pet.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class PetException extends BaseException {
    public PetException(String message, HttpStatusCode status) {
        super(message, status);
    }
}
