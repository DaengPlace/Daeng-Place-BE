package com.mycom.backenddaengplace.common.domain.pet.entity;


import com.mycom.backenddaengplace.common.domain.BaseEntity;
import com.mycom.backenddaengplace.common.domain.member.entity.Member;
import com.mycom.backenddaengplace.common.domain.member.entity.type.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "breed_type_id")
    private BreedType breedType;

    private String name;
    private int age;
    private boolean isNeutered;
    private Gender gender;
    private int weight;
    private String chipId;

}
