package com.mycom.backenddaengplace.pet.domain;


import com.mycom.backenddaengplace.common.domain.BaseEntity;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "pet")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breed_type_id")
    private BreedType breedType;
    private String name;
    private LocalDateTime birthDate;
    private Boolean isNeutered;
    private Gender gender;
    private Integer weight;

    private String chipId;

    @Builder
    public Pet(
            Member member,
            BreedType breedType,
            String name,
            LocalDateTime birthDate,
            boolean isNeutered,
            int weight,
            Gender gender
    ) {
        this.member = member;
        this.breedType = breedType;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.weight = weight;
        this.isNeutered = isNeutered;

    }
}
