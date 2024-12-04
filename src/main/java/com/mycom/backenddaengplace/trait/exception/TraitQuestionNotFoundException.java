package com.mycom.backenddaengplace.trait.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class TraitQuestionNotFoundException extends BaseException {
    public TraitQuestionNotFoundException(Long questionId) {
      super(String.format("성향 질문 ID [%d]를 찾을 수 없습니다.", questionId),
              HttpStatus.NOT_FOUND);
    }
}
