package com.mycom.backenddaengplace.review.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "media_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Builder
    public MediaFile(
            Review review,
            String filePath
    ) {
        this.review = review;
        this.filePath = filePath;
    }
}

