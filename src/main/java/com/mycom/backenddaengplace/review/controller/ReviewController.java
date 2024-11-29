package com.mycom.backenddaengplace.review.controller;

import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.review.dto.request.ReviewRequest;
import com.mycom.backenddaengplace.review.dto.response.ReviewResponse;
import com.mycom.backenddaengplace.review.dto.response.UserReviewResponse;
import com.mycom.backenddaengplace.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{placeId}/review")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @PathVariable Long placeId,
            @Valid @RequestBody ReviewRequest request) {

        ReviewResponse response = reviewService.createReview(placeId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("리뷰가 성공적으로 등록되었습니다.", response));
    }

    @GetMapping("/{placeId}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviews(@PathVariable Long placeId) {
        List<ReviewResponse> reviews = reviewService.getReviews(placeId);

        return ResponseEntity.ok(ApiResponse.success("리뷰 목록을 성공적으로 조회했습니다.", reviews));
    }

    @GetMapping("/{placeId}/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewDetail(
            @PathVariable Long placeId,
            @PathVariable Long reviewId) {

        ReviewResponse review = reviewService.getReviewDetail(placeId, reviewId);

        return ResponseEntity.ok(
                ApiResponse.success("리뷰 상세 정보를 성공적으로 조회했습니다.", review)
        );
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<List<UserReviewResponse>>> getUserReview(@PathVariable Long memberId) {
        List<UserReviewResponse> reviews = reviewService.getUserReview(memberId);

        return ResponseEntity.ok(ApiResponse.success("사용자의 리뷰 목록을 성공적으로 조회했습니다.", reviews));
    }
}
