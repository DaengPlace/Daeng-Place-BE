package com.mycom.backenddaengplace.pet.repository;

import com.mycom.backenddaengplace.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByMemberId(Long memberId);
}
