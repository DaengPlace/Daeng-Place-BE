package com.mycom.backenddaengplace.member.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EmailNotFoundException extends BaseException {
    public EmailNotFoundException() {
        super("등록되지 않은 이메일입니다.",
                HttpStatus.NOT_FOUND);
    }
}
