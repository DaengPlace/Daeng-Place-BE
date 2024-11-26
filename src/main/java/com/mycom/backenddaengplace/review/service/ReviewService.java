package com.mycom.backenddaengplace.review.service;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.exception.MemberNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.place.domain.Place;
import com.mycom.backenddaengplace.place.exception.PlaceNotFoundException;
import com.mycom.backenddaengplace.place.repository.PlaceRepository;
import com.mycom.backenddaengplace.review.domain.Review;
import com.mycom.backenddaengplace.review.dto.request.ReviewRequest;
import com.mycom.backenddaengplace.review.dto.response.ReviewResponse;
import com.mycom.backenddaengplace.review.exception.ReviewAlreadyExistsException;
import com.mycom.backenddaengplace.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewResponse createReview(Long placeId, ReviewRequest request) {
        // 회원 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(request.getMemberId()));

        // 장소 조회
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(placeId));

        // 리뷰 중복 검사
        if (reviewRepository.existsByMemberAndPlace(member, place)) {
            throw new ReviewAlreadyExistsException(request.getMemberId(), placeId);
        }

        // 리뷰 생성
        Review review = Review.builder()
                .member(member)
                .place(place)
                .content(request.getContent())
                .rating(request.getRating())
                .traitTag(request.getTraitTag())
                .build();

        review = reviewRepository.save(review);

        return ReviewResponse.from(review);
    }

}
