package com.mycom.backenddaengplace.pet.dto.response;

import com.mycom.backenddaengplace.member.enums.Gender;
import com.mycom.backenddaengplace.pet.domain.Pet;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetReviseResponse {

    private Long petId;
    private String name;
    private String breed;
    private String birthDate;
    private String age;
    private Double weight;
    private Gender gender;
    private Boolean isNeutered;

    public static PetReviseResponse from(Pet pet) {
        return PetReviseResponse.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .breed(pet.getBreedType().toString())
                .birthDate(pet.getBirthDate().toString())
                .weight(pet.getWeight())
                .gender(pet.getGender())
                .isNeutered(pet.getIsNeutered())
                .build();
    }
}
