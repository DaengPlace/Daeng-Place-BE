package com.mycom.backenddaengplace.trait.domain;

import com.mycom.backenddaengplace.trait.enums.QuestionTarget;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "trait_question")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TraitQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trait_question_id")
    private Long id;

    @OneToMany(mappedBy = "traitQuestion")
    private List<TraitAnswer> traitAnswers = new ArrayList<>();

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "target", nullable = false)
    private QuestionTarget target;
}
