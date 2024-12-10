package com.mycom.backenddaengplace.trait.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PetTraitResult {
    private Long petId;
    private String petName;
    private List<TraitResult> petTraits;
}
