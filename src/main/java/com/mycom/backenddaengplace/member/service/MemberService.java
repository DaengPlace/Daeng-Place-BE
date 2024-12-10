package com.mycom.backenddaengplace.member.service;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.dto.request.BaseEmailRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberRegisterRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberUpdateRequest;
import com.mycom.backenddaengplace.member.dto.response.BaseMemberResponse;
import com.mycom.backenddaengplace.member.dto.response.EmailDuplicateCheckResponse;
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

    public BaseMemberResponse getMember(Member member) {
//        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        return BaseMemberResponse.from(member);
    }

    @Transactional
    public BaseMemberResponse reviseMember(MemberUpdateRequest request, Member member) {

        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow(() -> new MemberNotFoundException(member.getId()));

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

    @Transactional
    public BaseMemberResponse registerMember(MemberRegisterRequest request) {

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

    private LocalDateTime parseBirthDate(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            return LocalDate.parse(birthDate, formatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new InvalidBirthDateException();
        }
    }


    @Transactional
    public BaseMemberResponse deleteMember(Member member) {

        Member deletedMember = memberRepository.findById(member.getId()).orElseThrow(() -> new MemberNotFoundException(member.getId()));

        memberRepository.delete(deletedMember);

        return BaseMemberResponse.from(deletedMember);
    }
}
