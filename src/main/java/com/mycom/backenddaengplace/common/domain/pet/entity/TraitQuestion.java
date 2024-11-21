package com.mycom.backenddaengplace.common.domain.pet.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "trait_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraitQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trait_question_id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "trait")
    private String trait;

}
