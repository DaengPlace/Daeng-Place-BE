package com.mycom.backenddaengplace.review.repository;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.place.domain.Place;
import com.mycom.backenddaengplace.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    long countByPlaceId(Long placeId);

    List<Review> findTop3ByPlaceIdOrderByCreatedAtDesc(Long placeId);

    boolean existsByMemberAndPlace(Member member, Place place);

    List<Review> findByPlaceId(Long placeId);

    Optional<Review> findByIdAndPlaceId(Long reviewId, Long placeId);

    @Query("SELECT r.place.id, AVG(r.rating) as avgRating, COUNT(r) as reviewCount " +
            "FROM Review r GROUP BY r.place.id " +
            "ORDER BY avgRating DESC")
    List<Object[]> findTopPlacesByAverageRating();
}
