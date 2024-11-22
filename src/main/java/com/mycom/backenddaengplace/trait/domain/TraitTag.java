package com.mycom.backenddaengplace.trait.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "trait_tag")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraitTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trait_tag_id")
    private Long id;

    @OneToMany(mappedBy = "traitTag")
    private List<TraitTagCount> traitTagCounts = new ArrayList<>();

    @Column(name = "content", nullable = false)
    private String content;

}
