package com.mycom.backenddaengplace.review.controller;

import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.review.dto.request.ReviewRequest;
import com.mycom.backenddaengplace.review.dto.response.ReviewResponse;
import com.mycom.backenddaengplace.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/places")
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
}
