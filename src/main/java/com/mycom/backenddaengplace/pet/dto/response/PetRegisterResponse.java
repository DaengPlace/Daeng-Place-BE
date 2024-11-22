package com.mycom.backenddaengplace.pet.dto.response;

import com.mycom.backenddaengplace.member.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PetRegisterResponse {
    private Long petId;
    private String name;
    private String breed;
    private String birthDate;
    private String age;
    private Double weight;
    private Gender gender;
    private Boolean isNeutered;
    private LocalDateTime registeredAt;
}
