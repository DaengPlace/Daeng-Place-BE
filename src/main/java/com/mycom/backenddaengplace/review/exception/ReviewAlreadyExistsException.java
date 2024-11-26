package com.mycom.backenddaengplace.review.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ReviewAlreadyExistsException extends BaseException {
    public ReviewAlreadyExistsException(Long memberId, Long placeId) {
        super(String.format("회원 ID [%d]가 장소 ID [%d]에 이미 리뷰를 작성했습니다.",
                        memberId, placeId),
                HttpStatus.CONFLICT);
    }
}
