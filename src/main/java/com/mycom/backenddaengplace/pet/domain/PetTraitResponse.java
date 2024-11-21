package com.mycom.backenddaengplace.pet.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pet_trait_response")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetTraitResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private TraitAnswer answer;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private TraitQuestion question;
}