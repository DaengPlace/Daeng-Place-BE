package com.mycom.backenddaengplace.common.domain.inquiry.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inquiry_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_type_id")
    private Long id;

    @Column(name = "inquiry", nullable = false, length = 255)
    private String inquiry;
}
