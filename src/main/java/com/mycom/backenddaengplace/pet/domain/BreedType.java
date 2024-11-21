package com.mycom.backenddaengplace.pet.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "breed_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreedType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "breed_type_id")
    private Long id;

    private String breedType;

}
