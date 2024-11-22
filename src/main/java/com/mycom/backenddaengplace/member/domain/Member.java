package com.mycom.backenddaengplace.member.domain;

import com.mycom.backenddaengplace.common.domain.BaseEntity;
import com.mycom.backenddaengplace.member.enums.Gender;
import com.mycom.backenddaengplace.pet.domain.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String name;
    private String nickname;
    private LocalDateTime birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<Pet> pets = new ArrayList<>();

    private String phone;
    private String profileImageUrl;
    private Boolean locationStatus;

    @Builder
    public Member(
            String email,
            String name,
            String nickname,
            LocalDateTime birthDate,
            Gender gender
    ) {

        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;

    }


}
