package com.mycom.backenddaengplace.auth.service;

import com.mycom.backenddaengplace.auth.repository.AuthMemberRepository;
import com.mycom.backenddaengplace.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthMemberService {

    private final AuthMemberRepository authMemberRepository;

    public AuthMemberService(AuthMemberRepository authMemberRepository) {
        this.authMemberRepository = authMemberRepository;
    }

    /**
     * Member 업데이트 메서드
     */
    public void updateMember(Member member, String email, String nickname, String profileImageUrl, String provider, String providerId) {
        // Member 클래스의 update 메서드를 사용하여 엔티티 필드 업데이트
        member.update(email, nickname, profileImageUrl, provider, providerId);

        // 변경된 정보를 저장
        authMemberRepository.save(member);
    }


    /**
     * Member 저장 또는 업데이트 메서드
     */
    public Member saveOrUpdate(Member member, String email, String nickname, String profileImageUrl, String provider, String providerId) {
        if (member == null) {
            // 새 사용자 저장
            member = Member.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .email(email)
                    .nickname(nickname)
                    .profileImageUrl(profileImageUrl)
                    .build();
        } else {
            // 기존 사용자 업데이트
            member.update(email, nickname, profileImageUrl, provider, providerId);
        }

        return authMemberRepository.save(member);
    }

}
