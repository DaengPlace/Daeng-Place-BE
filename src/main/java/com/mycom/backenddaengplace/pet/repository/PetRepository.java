package com.mycom.backenddaengplace.pet.repository;

import com.mycom.backenddaengplace.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByMemberId(Long memberId);

    @Modifying
    @Query("DELETE FROM Pet p WHERE p.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

}
