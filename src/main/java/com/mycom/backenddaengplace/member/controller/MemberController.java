package com.mycom.backenddaengplace.member.controller;


import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.dto.request.BaseEmailRequest;
import com.mycom.backenddaengplace.member.dto.request.EmailCodeCheckRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberRegisterRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberUpdateRequest;
import com.mycom.backenddaengplace.member.dto.response.EmailCodeCheckResponse;
import com.mycom.backenddaengplace.member.dto.response.EmailDuplicateCheckResponse;
import com.mycom.backenddaengplace.member.dto.response.BaseMemberResponse;
import com.mycom.backenddaengplace.member.dto.response.EmailSendCodeResponse;
import com.mycom.backenddaengplace.member.service.EmailService;
import com.mycom.backenddaengplace.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> getMember(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        BaseMemberResponse response = memberService.getMember(member);
        return ResponseEntity.ok(ApiResponse.success("회원 조회가 완료되었습니다.", response));
    }

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> updateMember(
            @Valid @RequestBody MemberUpdateRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        BaseMemberResponse response = memberService.reviseMember(request, member);
        return ResponseEntity.ok(ApiResponse.success("회원 수정이 완료되었습니다.", response));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> deleteMember(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        BaseMemberResponse response = memberService.deleteMember(member);
        return ResponseEntity.ok(ApiResponse.success("회원 삭제가 완료되었습니다.", response));
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> registerMember(
            @Valid @RequestBody MemberRegisterRequest request
    ) {
        BaseMemberResponse response = memberService.registerMember(request);
        return ResponseEntity.ok(ApiResponse.success("회원 등록이 완료되었습니다.", response));
    }

    @GetMapping("/email-duplicate-check")
    public ResponseEntity<ApiResponse<EmailDuplicateCheckResponse>> checkDuplicateEmail(
            @Valid @RequestBody BaseEmailRequest request
    ) {
        EmailDuplicateCheckResponse response = memberService.checkDuplicateEmail(request);
        return ResponseEntity.ok(ApiResponse.success("이메일 중복 체크가 완료되었습니다.", response));
    }

    @PostMapping("/email-send-code")
    public ResponseEntity<ApiResponse<EmailSendCodeResponse>> sendEmailCode(
            @Valid @RequestBody BaseEmailRequest request) throws MessagingException, UnsupportedEncodingException {
        EmailSendCodeResponse response = emailService.sendEmailCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("인증 번호를 전송하였습니다.", response));
    }

    @PostMapping("/email-code-check")
    public ResponseEntity<ApiResponse<EmailCodeCheckResponse>> emailCodeCheck(
            @Valid @RequestBody EmailCodeCheckRequest request) {
        EmailCodeCheckResponse response = emailService.checkEmailCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(ApiResponse.success("이메일 인증 성공하였습니다..", response));
    }

}
