package com.mycom.backenddaengplace.pet.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BreedTypeResponse {
    private Long breedTypeId;
    private String breedType;
}
