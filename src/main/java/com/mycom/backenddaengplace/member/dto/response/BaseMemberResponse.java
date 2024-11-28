package com.mycom.backenddaengplace.member.dto.response;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.enums.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseMemberResponse {

    private String email;
    private String name;
    private String nickname;
    private String profileImageUrl;
    private Gender gender;
    private String birthDate;
    private Boolean locationStatus;

    public static BaseMemberResponse from(Member member) {
        return BaseMemberResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .gender(member.getGender())
                .birthDate(member.getBirthDate().toString())
                .locationStatus(member.getLocationStatus())
                .build();
    }

}
