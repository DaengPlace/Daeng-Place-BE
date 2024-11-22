package com.mycom.backenddaengplace.pet.repository;

import com.mycom.backenddaengplace.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

}
