package com.mycom.backenddaengplace.review.controller;

import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.review.dto.response.ReviewLikeResponse;
import com.mycom.backenddaengplace.review.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews/likes")
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping("/{placeId}/and/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewLikeResponse>> createLike(
            @PathVariable Long placeId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(ApiResponse.success("좋아요가 등록되었습니다.",
                reviewLikeService.createLike(placeId, reviewId, customOAuth2User.getMember())));
    }

    @DeleteMapping("/{placeId}/and/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewLikeResponse>> deleteLike(
            @PathVariable Long placeId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(ApiResponse.success("좋아요가 취소되었습니다.",
                reviewLikeService.deleteLike(placeId, reviewId, customOAuth2User.getMember())));
    }
}
