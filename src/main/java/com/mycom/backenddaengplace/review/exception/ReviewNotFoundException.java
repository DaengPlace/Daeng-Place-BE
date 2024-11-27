package com.mycom.backenddaengplace.review.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends BaseException {
    public ReviewNotFoundException(Long reviewId, Long placeId) {
        super(String.format("장소 ID [%d]의 리뷰 ID [%d]를 찾을 수 없습니다.", placeId, reviewId),
                HttpStatus.NOT_FOUND);
    }
}
