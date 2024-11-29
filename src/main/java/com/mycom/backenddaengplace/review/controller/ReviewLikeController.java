package com.mycom.backenddaengplace.review.controller;

import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.review.dto.response.ReviewLikeResponse;
import com.mycom.backenddaengplace.review.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping("/likes/{placeId}/and/{reviewId}/members/{memberId}")
    public ResponseEntity<ApiResponse<ReviewLikeResponse>> createLike(
            @PathVariable Long placeId,
            @PathVariable Long reviewId,
            @PathVariable Long memberId
    ) {
        ReviewLikeResponse response = reviewLikeService.createLike(placeId, reviewId, memberId);
        return ResponseEntity.ok(ApiResponse.success("좋아요가 등록되었습니다.", response));
    }

    @DeleteMapping("/likes/{placeId}/and/{reviewId}/members/{memberId}")
    public ResponseEntity<ApiResponse<ReviewLikeResponse>> deleteLike(
            @PathVariable Long placeId,
            @PathVariable Long reviewId,
            @PathVariable Long memberId
    ) {
        ReviewLikeResponse response = reviewLikeService.deleteLike(placeId, reviewId, memberId);
        return ResponseEntity.ok(ApiResponse.success("좋아요가 취소되었습니다.", response));
    }
}
