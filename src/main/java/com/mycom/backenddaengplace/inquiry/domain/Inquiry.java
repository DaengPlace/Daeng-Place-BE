package com.mycom.backenddaengplace.inquiry.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inquiry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "inquiry_type_id", nullable = false)
    private InquiryType inquiryType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "inquiry_date", nullable = false)
    private LocalDateTime inquiryDate;

}
