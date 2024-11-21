package com.mycom.backenddaengplace.common.domain.member.entity;

import com.mycom.backenddaengplace.common.domain.BaseEntity;
import com.mycom.backenddaengplace.common.domain.member.entity.type.Gender;
import com.mycom.backenddaengplace.common.domain.member.entity.type.SocialType;
import com.mycom.backenddaengplace.common.domain.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
