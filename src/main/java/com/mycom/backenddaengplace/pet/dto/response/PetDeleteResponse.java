package com.mycom.backenddaengplace.pet.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PetDeleteResponse {
    private Long petId;
    private LocalDateTime deletedAt;

    public PetDeleteResponse(
            Long petId,
            LocalDateTime deletedAt
    ) {
        this.petId = petId;
        this.deletedAt = deletedAt;
    }
}
