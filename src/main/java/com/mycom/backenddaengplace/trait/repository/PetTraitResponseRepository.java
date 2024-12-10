package com.mycom.backenddaengplace.trait.repository;

import com.mycom.backenddaengplace.trait.domain.PetTraitResponse;
import com.mycom.backenddaengplace.trait.domain.PetTraitResponseId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetTraitResponseRepository extends JpaRepository<PetTraitResponse, PetTraitResponseId> {
    List<PetTraitResponse> findByPetId(Long petId);
}
