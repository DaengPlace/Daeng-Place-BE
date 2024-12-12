package com.mycom.backenddaengplace.pet.repository;

import com.mycom.backenddaengplace.pet.domain.BreedType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreedTypeRepository extends JpaRepository<BreedType, Long> {
    @Query("SELECT b FROM BreedType b ORDER BY b.breedType")
    List<BreedType> findAllSorted();
    List<BreedType> findByBreedTypeContaining(String keyword);
}
