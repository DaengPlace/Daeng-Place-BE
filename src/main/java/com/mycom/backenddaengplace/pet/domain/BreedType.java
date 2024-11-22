package com.mycom.backenddaengplace.pet.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "breed_type")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BreedType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "breed_type_id")
    private Long id;

    private String breedType;

    @OneToOne(mappedBy = "breedType")
    private Pet pet;


}
