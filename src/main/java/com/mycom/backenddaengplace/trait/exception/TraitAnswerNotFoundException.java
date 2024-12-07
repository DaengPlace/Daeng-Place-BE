package com.mycom.backenddaengplace.trait.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class TraitAnswerNotFoundException extends BaseException {
    public TraitAnswerNotFoundException(Long answerId) {
        super(String.format("성향 답변 ID [%d]를 찾을 수 없습니다.", answerId),
              HttpStatus.NOT_FOUND);
    }
}
