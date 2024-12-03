package com.mycom.backenddaengplace.review.service;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.review.domain.Review;
import com.mycom.backenddaengplace.review.domain.ReviewLike;
import com.mycom.backenddaengplace.review.dto.response.ReviewLikeResponse;
import com.mycom.backenddaengplace.review.exception.ReviewException;
import com.mycom.backenddaengplace.review.exception.ReviewNotFoundException;
import com.mycom.backenddaengplace.review.repository.ReviewLikeRepository;
import com.mycom.backenddaengplace.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewLikeService {
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewLikeResponse createLike(Long placeId, Long reviewId, Member member) {
        Review review = findReviewAndValidate(placeId, reviewId);
        validateNotAlreadyLiked(review, member);

        ReviewLike reviewLike = reviewLikeRepository.save(new ReviewLike(review, member));
        return ReviewLikeResponse.from(reviewId,
                reviewLikeRepository.countByReview(review), true);
    }

    @Transactional
    public ReviewLikeResponse deleteLike(Long placeId, Long reviewId, Member member) {
        Review review = findReviewAndValidate(placeId, reviewId);
        ReviewLike reviewLike = reviewLikeRepository.findByReviewAndMember(review, member)
                .orElseThrow(() -> new ReviewNotFoundException(placeId, reviewId));

        reviewLikeRepository.delete(reviewLike);
        return ReviewLikeResponse.from(reviewId,
                reviewLikeRepository.countByReview(review), false);
    }

    private Review findReviewAndValidate(Long placeId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(placeId, reviewId));

        if (!review.getPlace().getId().equals(placeId)) {
            throw new ReviewNotFoundException(placeId, reviewId);
        }
        return review;
    }

    private void validateNotAlreadyLiked(Review review, Member member) {
        if (reviewLikeRepository.existsByReviewAndMember(review, member)) {
            throw ReviewException.alreadyLiked(review.getId());
        }
    }
}
