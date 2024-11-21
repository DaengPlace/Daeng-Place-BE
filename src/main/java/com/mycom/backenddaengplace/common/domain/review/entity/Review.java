package com.mycom.backenddaengplace.common.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "rating", precision = 2, scale = 1, nullable = false)
    private Double rating;

    @Column(name = "trait_tag")
    private String traitTag;

    @Column(name = "basic_tag")
    private String basicTag;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFiles> mediaFiles;
}
