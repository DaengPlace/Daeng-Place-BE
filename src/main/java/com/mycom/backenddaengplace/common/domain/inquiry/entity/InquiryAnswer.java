package com.mycom.backenddaengplace.common.domain.inquiry.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inquiry_answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inquiry_id", nullable = false)
    private Inquiry inquiry;

    @Column(name = "answer_content", nullable = false)
    private String answerContent;
}

