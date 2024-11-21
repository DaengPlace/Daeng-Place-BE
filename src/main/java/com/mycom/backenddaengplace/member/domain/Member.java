package com.mycom.backenddaengplace.member.domain;

import com.mycom.backenddaengplace.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;
    private String email;
    private String name;
    private String nickname;
    private LocalDateTime birthDate;
    private Gender gender;
    private String phone;
    private String profileImageUrl;
    private boolean locationStatus;


}
