package com.mycom.backenddaengplace.review.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ReviewNotOwnedException extends BaseException {
    public ReviewNotOwnedException(Long memberId, Long reviewId) {
        super(String.format("회원 ID [%d]가 작성한 리뷰 ID [%d]가 아닙니다.", memberId, reviewId),
                HttpStatus.FORBIDDEN);
    }
}
