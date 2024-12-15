package com.mycom.backenddaengplace.member.service;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.dto.request.MemberRegisterRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberUpdateRequest;
import com.mycom.backenddaengplace.member.dto.response.BaseMemberResponse;
import com.mycom.backenddaengplace.member.exception.MemberNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.pet.exception.InvalidBirthDateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public BaseMemberResponse getMember(Long memberId) {
        return BaseMemberResponse.from(memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId)));
    }

    @Transactional
    public BaseMemberResponse registerMember(MemberRegisterRequest request) {
        // 카카오 이메일로 기존 회원 조회
        Member existingMember = memberRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (existingMember != null) {
            // 기존 회원이 있다면 프로필 정보 업데이트
            return updateExistingMember(existingMember, request);
        } else {
            // 신규 회원이라면 새로 생성
            return createNewMember(request);
        }
    }

    private BaseMemberResponse updateExistingMember(Member existingMember, MemberRegisterRequest request) {
        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        existingMember.setName(request.getName());
        existingMember.setNickname(request.getNickname());
        existingMember.setBirthDate(birthDate);
        existingMember.setGender(request.getGender());
        existingMember.setState(request.getState());
        existingMember.setCity(request.getCity());
        existingMember.setProfileImageUrl(request.getProfileImageUrl());

        Member savedMember = memberRepository.save(existingMember);
        return BaseMemberResponse.from(savedMember);
    }

    private BaseMemberResponse createNewMember(MemberRegisterRequest request) {
        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .nickname(request.getNickname())
                .birthDate(birthDate)
                .gender(request.getGender())
                .state(request.getState())
                .city(request.getCity())
                .profileImageUrl(request.getProfileImageUrl())
                .locationStatus(false)
                .build();

        Member savedMember = memberRepository.save(member);
        return BaseMemberResponse.from(savedMember);
    }

    @Transactional
    public BaseMemberResponse reviseMember(MemberUpdateRequest request, Long memberId) {
        Member updatedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        updatedMember.setNickname(request.getNickname());
        updatedMember.setGender(request.getGender());
        updatedMember.setState(request.getState());
        updatedMember.setCity(request.getCity());
        updatedMember.setBirthDate(birthDate);
        updatedMember.setProfileImageUrl(request.getProfileImageUrl());

        memberRepository.save(updatedMember);
        return BaseMemberResponse.from(updatedMember);
    }

    private LocalDateTime parseBirthDate(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            return LocalDate.parse(birthDate, formatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new InvalidBirthDateException();
        }
    }

    @Transactional
    public BaseMemberResponse deleteMember(Long memberId) {
        Member deletedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        memberRepository.delete(deletedMember);
        return BaseMemberResponse.from(deletedMember);
    }

    @Transactional
    public BaseMemberResponse updateProfileImage(Member member, String imageUrl) {
        Member updatedMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberNotFoundException(member.getId()));

        updatedMember.setProfileImageUrl(imageUrl);
        memberRepository.save(updatedMember);

        return BaseMemberResponse.from(updatedMember);
    }
}