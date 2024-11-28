package com.mycom.backenddaengplace.member.service;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.dto.request.EmailCheckRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberRegisterRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberReviseRequest;
import com.mycom.backenddaengplace.member.dto.response.EmailCheckResponse;
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
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        return BaseMemberResponse.from(member);
    }

    @Transactional
    public BaseMemberResponse reviseMember(MemberReviseRequest request, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        Member revisedMember = Member.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(request.getNickname())
                .birthDate(birthDate)
                .gender(request.getGender())
                .state(request.getState())
                .city(request.getCity())
                .profileImageUrl(request.getProfileImageUrl())
                .locationStatus(member.getLocationStatus())
                .build();

        memberRepository.save(revisedMember);

        return BaseMemberResponse.from(revisedMember);
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

    public EmailCheckResponse checkEmail(EmailCheckRequest request) {

        boolean isValid = memberRepository.existsByEmail(request.getEmail());

        return EmailCheckResponse.from(!isValid);
    }

    public BaseMemberResponse deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        memberRepository.delete(member);

        return BaseMemberResponse.from(member);
    }
}
