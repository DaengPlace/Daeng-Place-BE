package com.mycom.backenddaengplace.trait.domain;

import com.mycom.backenddaengplace.pet.domain.Pet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pet_trait_response")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetTraitResponse {

    @EmbeddedId
    private PetTraitResponseId id;

    @MapsId("petId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @MapsId("traitAnswerId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trait_answer_id", nullable = false)
    private TraitAnswer traitAnswer;

}