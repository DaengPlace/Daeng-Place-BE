package com.mycom.backenddaengplace.review.repository;

import com.mycom.backenddaengplace.review.domain.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    @Modifying
    @Query("DELETE FROM MediaFile mf WHERE mf.review.id = :reviewId")
    void deleteByReviewId(Long reviewId);
}
