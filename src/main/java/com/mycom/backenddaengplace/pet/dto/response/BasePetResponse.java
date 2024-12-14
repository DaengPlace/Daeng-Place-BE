package com.mycom.backenddaengplace.pet.dto.response;

import com.mycom.backenddaengplace.member.enums.Gender;
import com.mycom.backenddaengplace.pet.domain.Pet;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
public class BasePetResponse {
    private Long petId;
    private String name;
    private String breed;
    private String birthDate;
    private String age;
    private Double weight;
    private Gender gender;
    private Boolean isNeutered;

    public static BasePetResponse from(Pet pet) {

        return BasePetResponse.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .breed(pet.getBreedType().getBreedType())
                .birthDate(pet.getBirthDate().toString())
                .age(calculateAge(pet.getBirthDate()))
                .weight(pet.getWeight())
                .gender(pet.getGender())
                .isNeutered(pet.getIsNeutered())
                .build();
    }

    private static String calculateAge(LocalDateTime birthDate) {
        LocalDateTime now = LocalDateTime.now();
        long months = ChronoUnit.MONTHS.between(birthDate, now);
        return months + "개월";
    }
}
