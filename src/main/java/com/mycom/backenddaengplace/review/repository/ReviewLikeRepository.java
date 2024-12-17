package com.mycom.backenddaengplace.review.repository;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.review.domain.Review;
import com.mycom.backenddaengplace.review.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    @Query("SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review = :review")
    long countByReview(Review review);

    @Query("SELECT CASE WHEN COUNT(rl) > 0 THEN true ELSE false END FROM ReviewLike rl WHERE rl.review = :review AND rl.member = :member")
    boolean existsByReviewAndMember(@Param("review") Review review, @Param("member") Member member);

    Optional<ReviewLike> findByReviewAndMember(Review review, Member member);

    boolean existsByReviewAndMemberId(Review review, Long memberId);

    @Modifying
    @Query("DELETE FROM ReviewLike rl WHERE rl.review.id = :reviewId")
    void deleteByReviewId(Long reviewId);

    @Modifying
    @Query("DELETE FROM ReviewLike rl WHERE rl.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
