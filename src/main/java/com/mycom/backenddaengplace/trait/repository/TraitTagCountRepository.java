package com.mycom.backenddaengplace.trait.repository;

import com.mycom.backenddaengplace.trait.domain.TraitTagCount;
import com.mycom.backenddaengplace.trait.domain.TraitTagCountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TraitTagCountRepository extends JpaRepository<TraitTagCount, TraitTagCountId> {
    @Modifying
    @Query("DELETE FROM TraitTagCount ttc WHERE ttc.review.id = :reviewId")
    void deleteByReviewId(Long reviewId);
}
