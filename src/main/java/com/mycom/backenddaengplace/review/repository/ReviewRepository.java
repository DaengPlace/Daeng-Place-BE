package com.mycom.backenddaengplace.review.repository;

import com.mycom.backenddaengplace.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    long countByPlaceId(Long placeId);

    List<Review> findTop3ByPlaceIdOrderByCreatedAtDesc(Long placeId);
}
