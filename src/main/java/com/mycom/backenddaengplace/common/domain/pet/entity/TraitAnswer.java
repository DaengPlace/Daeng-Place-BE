package com.mycom.backenddaengplace.common.domain.pet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trait_answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraitAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private TraitQuestion question;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "trait_code")
    private String traitCode;
}