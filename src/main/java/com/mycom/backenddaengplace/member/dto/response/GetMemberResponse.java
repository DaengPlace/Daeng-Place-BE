package com.mycom.backenddaengplace.member.dto.response;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.enums.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMemberResponse {

    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String profileImageUrl;
    private Gender gender;
    private String birthDate;
    private Boolean locationStatus;

    public static GetMemberResponse from(Member member) {
        return GetMemberResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .profileImageUrl(member.getProfileImageUrl())
                .gender(member.getGender())
                .birthDate(member.getBirthDate().toString())
                .locationStatus(member.getLocationStatus())
                .build();
    }

}
