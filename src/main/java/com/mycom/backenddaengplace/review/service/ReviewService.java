package com.mycom.backenddaengplace.review.service;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.exception.MemberNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.place.domain.Place;
import com.mycom.backenddaengplace.place.exception.PlaceNotFoundException;
import com.mycom.backenddaengplace.place.repository.PlaceRepository;
import com.mycom.backenddaengplace.review.domain.Review;
import com.mycom.backenddaengplace.review.dto.request.ReviewRequest;
import com.mycom.backenddaengplace.review.dto.response.MemberReviewResponse;
import com.mycom.backenddaengplace.review.dto.response.PopularReviewResponse;
import com.mycom.backenddaengplace.review.dto.response.ReviewResponse;
import com.mycom.backenddaengplace.review.exception.ReviewAlreadyExistsException;
import com.mycom.backenddaengplace.review.exception.ReviewNotFoundException;
import com.mycom.backenddaengplace.review.exception.ReviewNotOwnedException;
import com.mycom.backenddaengplace.review.repository.ReviewQueryRepository;
import com.mycom.backenddaengplace.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final ReviewQueryRepository reviewQueryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewResponse createReview(Long placeId, ReviewRequest request, Long memberId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(placeId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        if (reviewRepository.existsByMemberAndPlace(member, place)) {
            throw new ReviewAlreadyExistsException(member.getId(), placeId);
        }

        Review review = Review.builder()
                .member(member)
                .place(place)
                .content(request.getContent())
                .rating(request.getRating())
                .traitTags(request.getTraitTags())
                .build();

        return ReviewResponse.from(reviewRepository.save(review));
    }

    public List<ReviewResponse> getReviews(Long placeId) {
        if (!placeRepository.existsById(placeId)) {
            throw new PlaceNotFoundException(placeId);
        }
        return reviewRepository.findByPlaceId(placeId).stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }

    public ReviewResponse getReviewDetail(Long placeId, Long reviewId) {
        if (!placeRepository.existsById(placeId)) {
            throw new PlaceNotFoundException(placeId);
        }
        return ReviewResponse.from(reviewRepository.findByIdAndPlaceId(reviewId, placeId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId, placeId)));
    }

    @Transactional
    public void deleteReview(Long reviewId, Member member) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId, null));

        if (!review.getMember().getId().equals(member.getId())) {
            throw new ReviewNotOwnedException(member.getId(), reviewId);
        }
        reviewRepository.delete(review);
    }

    public List<PopularReviewResponse> getPopularReviews() {
        return reviewQueryRepository.findPopularReviews().stream()
                .map(PopularReviewResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewRequest request, Member member) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId, null));

        if (!review.getMember().getId().equals(member.getId())) {
            throw new ReviewNotOwnedException(member.getId(), reviewId);
        }
        review.update(request.getContent(), request.getRating(), request.getTraitTags());
    }

    public List<MemberReviewResponse> getUserReview(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }

        List<Review> reviews = reviewRepository.findByMemberId(memberId);
        return reviews.stream()
                .map(MemberReviewResponse::from)
                .collect(Collectors.toList());
    }
}
