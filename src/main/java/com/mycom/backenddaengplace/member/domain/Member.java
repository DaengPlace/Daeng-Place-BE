package com.mycom.backenddaengplace.member.domain;

import com.mycom.backenddaengplace.common.domain.BaseEntity;
import com.mycom.backenddaengplace.member.enums.Gender;
import com.mycom.backenddaengplace.pet.domain.Pet;
import com.mycom.backenddaengplace.review.domain.ReviewLike;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();
    private String state;
    private String city;
    private String profileImageUrl;
    private Boolean locationStatus;
    private String provider;
    private String providerId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    @Builder
    public Member(
            String email,
            String name,
            String nickname,
            LocalDateTime birthDate,
            Gender gender,
            String provider,
            String providerId,
            String state,
            String city,
            String profileImageUrl,
            Boolean locationStatus
    ) {

        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.state = state;
        this.city = city;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
        this.providerId = providerId;
        this.locationStatus = locationStatus;

    }

    public void update(String email, String nickname, String profileImageUrl, String provider, String providerId) {
        if (email != null) this.email = email;
        if (nickname != null) this.nickname = nickname;
        if (profileImageUrl != null) this.profileImageUrl = profileImageUrl;
        if (provider != null) this.provider = provider;
        if (providerId != null) this.providerId = providerId;
    }

}
