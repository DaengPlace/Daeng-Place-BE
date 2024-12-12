package com.mycom.backenddaengplace.member.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EmailAuthenticationCodeMismatchException extends BaseException {
    public EmailAuthenticationCodeMismatchException() {
        super("이메일 인증코드가 일치하지 않습니다.",
                HttpStatus.BAD_REQUEST);
    }
}
