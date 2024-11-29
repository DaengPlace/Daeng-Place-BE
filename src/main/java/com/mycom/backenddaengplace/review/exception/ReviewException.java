package com.mycom.backenddaengplace.review.exception;

import com.mycom.backenddaengplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ReviewException extends BaseException {

    public ReviewException(String message, HttpStatus status) {
        super(message, status);
    }

    public static ReviewException notFound(Long placeId, Long reviewId) {
        return new ReviewException(
                String.format("장소 ID [%d]의 리뷰 ID [%d]를 찾을 수 없습니다.", placeId, reviewId),
                HttpStatus.NOT_FOUND
        );
    }

    public static ReviewException alreadyLiked(Long reviewId) {
        return new ReviewException(
                String.format("이미 좋아요한 리뷰입니다. (리뷰 ID: %d)", reviewId),
                HttpStatus.BAD_REQUEST
        );
    }
}
