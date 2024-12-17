package com.mycom.backenddaengplace.favorite.repository;

import com.mycom.backenddaengplace.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByMemberIdAndPlaceId(Long memberId, Long placeId);

    @Query("select f from Favorite f where f.member.id = :memberId")
    List<Favorite> findFavoritesByMemberId(Long memberId);

    Boolean existsByPlaceIdAndMemberId(Long placeId, Long memberId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.member.id = :memberId")
    void deleteByMemberId(Long memberId);
}
