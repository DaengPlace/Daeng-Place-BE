package com.mycom.backenddaengplace.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.review.dto.request.ReviewRequest;
import com.mycom.backenddaengplace.review.dto.response.MemberReviewResponse;
import com.mycom.backenddaengplace.review.dto.response.PopularReviewResponse;
import com.mycom.backenddaengplace.review.dto.response.ReviewResponse;
import com.mycom.backenddaengplace.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/{placeId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @PathVariable Long placeId,
            @RequestParam("reviewData") String reviewString,
            @RequestParam(value = "file", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ReviewRequest request = mapper.readValue(reviewString, ReviewRequest.class);

            Member member = customOAuth2User.getMember();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("리뷰가 등록되었습니다.",
                            reviewService.createReview(placeId, request, files, member.getId())));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("리뷰 데이터 파싱에 실패했습니다.", e);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberReviewResponse>>> getUserReview(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(ApiResponse.success("사용자의 리뷰 목록을 조회했습니다.",
                reviewService.getUserReview(member.getId())));
    }

    @GetMapping("/places/{placeId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviews(
            @PathVariable Long placeId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
            ) {
        Long currentMemberId = customOAuth2User != null ? customOAuth2User.getMember().getId() : null;
        return ResponseEntity.ok(ApiResponse.success("리뷰 목록을 조회했습니다.",
                reviewService.getReviews(placeId, currentMemberId)));
    }

    @GetMapping("/{placeId}/and/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewDetail(
            @PathVariable Long placeId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
            ) {
        Long currentMemberId = customOAuth2User != null ? customOAuth2User.getMember().getId() : null;
        return ResponseEntity.ok(ApiResponse.success("리뷰 상세 정보를 조회했습니다.",
                reviewService.getReviewDetail(placeId, reviewId, currentMemberId)));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        reviewService.deleteReview(reviewId, member.getId());
        return ResponseEntity.ok(ApiResponse.success("리뷰가 삭제되었습니다."));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<PopularReviewResponse>>> getPopularReviews() {
        return ResponseEntity.ok(ApiResponse.success("인기 리뷰를 조회했습니다.",
                reviewService.getPopularReviews()));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        reviewService.updateReview(reviewId, request, member.getId());
        return ResponseEntity.ok(ApiResponse.success("리뷰가 수정되었습니다."));
    }
}
