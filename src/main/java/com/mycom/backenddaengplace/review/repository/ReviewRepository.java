package com.mycom.backenddaengplace.review.repository;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.place.domain.Place;
import com.mycom.backenddaengplace.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    long countByPlaceId(Long placeId);

    List<Review> findTop3ByPlaceIdOrderByCreatedAtDesc(Long placeId);

    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM Review r WHERE r.place.id = :placeId")
    Double findAverageRatingByPlaceId(@Param("placeId") Long placeId);

    boolean existsByMemberAndPlace(Member member, Place place);

    List<Review> findByPlaceId(Long placeId);

    Optional<Review> findByIdAndPlaceId(Long reviewId, Long placeId);

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.traitTags WHERE r.member.id = :memberId")
    List<Review> findByMemberId(@Param("memberId") Long memberId);

    Review findByIdAndMemberId(Long reviewId, Long memberId);
}
