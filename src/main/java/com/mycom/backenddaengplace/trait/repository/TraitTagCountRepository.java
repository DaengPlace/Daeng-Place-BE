package com.mycom.backenddaengplace.trait.repository;

import com.mycom.backenddaengplace.trait.domain.TraitTagCount;
import com.mycom.backenddaengplace.trait.domain.TraitTagCountId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraitTagCountRepository extends JpaRepository<TraitTagCount, TraitTagCountId> {
    void deleteByReviewId(Long reviewId);
}
