package com.mycom.backenddaengplace.trait.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TraitResultResponse {
    private List<PetTraitResult> petTraits;
    private List<TraitResult> memberTraits;
}
