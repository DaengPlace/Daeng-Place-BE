package com.mycom.backenddaengplace.member.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends BaseException {
    public MemberNotFoundException(Long memberId) {
        super(String.format("회원 ID [%d]를 찾을 수 없습니다.", memberId),
                HttpStatus.NOT_FOUND);
    }
}
