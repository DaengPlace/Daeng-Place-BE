package com.mycom.backenddaengplace.review.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "media_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "file_path", nullable = false)
    private String filePath;
}

