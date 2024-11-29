package com.mycom.backenddaengplace.review.service;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.exception.MemberNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.review.domain.Review;
import com.mycom.backenddaengplace.review.domain.ReviewLike;
import com.mycom.backenddaengplace.review.dto.response.ReviewLikeResponse;
import com.mycom.backenddaengplace.review.exception.ReviewException;
import com.mycom.backenddaengplace.review.repository.ReviewLikeRepository;
import com.mycom.backenddaengplace.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewLikeResponse createLike(Long placeId, Long reviewId, Long memberId) {
        Review review = findReviewAndValidate(placeId, reviewId);
        Member member = findMember(memberId);

        validateNotAlreadyLiked(review, member);

        ReviewLike reviewLike = reviewLikeRepository.save(new ReviewLike(review, member));
        long likeCount = reviewLikeRepository.countByReview(review);

        return ReviewLikeResponse.from(reviewId, likeCount, true);
    }

    @Transactional
    public ReviewLikeResponse deleteLike(Long placeId, Long reviewId, Long memberId) {
        Review review = findReviewAndValidate(placeId, reviewId);
        Member member = findMember(memberId);

        ReviewLike reviewLike = reviewLikeRepository.findByReviewAndMember(review, member)
                .orElseThrow(() -> ReviewException.notFound(placeId, reviewId));

        reviewLikeRepository.delete(reviewLike);
        long likeCount = reviewLikeRepository.countByReview(review);

        return ReviewLikeResponse.from(reviewId, likeCount, false);
    }

    private Review findReviewAndValidate(Long placeId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewException.notFound(placeId, reviewId));

        if (!review.getPlace().getId().equals(placeId)) {
            throw ReviewException.notFound(placeId, reviewId);
        }

        return review;
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    private void validateNotAlreadyLiked(Review review, Member member) {
        if (reviewLikeRepository.existsByReviewAndMember(review, member)) {
            throw ReviewException.alreadyLiked(review.getId());
        }
    }
}
