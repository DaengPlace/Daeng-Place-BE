package com.mycom.backenddaengplace.review.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name = "basic_tag")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basic_tag_id")
    private Long id;

    @OneToMany(mappedBy = "basicTag")
    private List<BasicTagCount> basicTagCounts = new ArrayList<>();

    @Column(name = "content", nullable = false)
    private String content;

}
