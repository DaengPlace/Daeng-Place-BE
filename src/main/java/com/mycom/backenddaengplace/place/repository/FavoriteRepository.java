package com.mycom.backenddaengplace.place.repository;

import com.mycom.backenddaengplace.review.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {


    @Modifying
    @Query("delete from Favorite f where f.id = :favoriteId")
    void deleteFavoriteById(@Param("favoriteId") Long favoriteId);

    Optional<Favorite> findByMemberIdPlaceId(Long memberId, Long placeId);


}
