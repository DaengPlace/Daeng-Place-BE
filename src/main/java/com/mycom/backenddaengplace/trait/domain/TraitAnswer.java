package com.mycom.backenddaengplace.trait.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trait_answer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraitAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private TraitQuestion traitQuestion;

    @Column(name = "content", nullable = false)
    private String content;


}