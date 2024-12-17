package com.mycom.backenddaengplace.member.dto.response;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BaseMemberResponse {

    private String email;
    private String name;
    private String nickname;
    private String profileImageUrl;
    private Gender gender;
    private String birthDate;
    private String state;
    private String city;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;
    private Boolean locationStatus;

    public static BaseMemberResponse from(Member member) {
        return BaseMemberResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .state(member.getState())
                .city(member.getCity())
                .gender(member.getGender())
                .birthDate(member.getBirthDate()!= null ? member.getBirthDate().toString():null)
                .isDeleted(member.getIsDeleted())
                .deletedAt(member.getDeletedAt())
                .locationStatus(member.getLocationStatus())
                .build();
    }
}