package com.mycom.backenddaengplace.trait.repository;

import com.mycom.backenddaengplace.trait.domain.TraitTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TraitTagRepository extends JpaRepository<TraitTag, Long> {
    List<TraitTag> findByContentIn(List<String> content);

}
