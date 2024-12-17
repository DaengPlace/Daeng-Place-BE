package com.mycom.backenddaengplace.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.dto.request.MemberRegisterRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberUpdateRequest;
import com.mycom.backenddaengplace.member.dto.request.NicknameCheckRequest;
import com.mycom.backenddaengplace.member.dto.response.BaseMemberResponse;
import com.mycom.backenddaengplace.member.dto.response.DuplicateCheckResponse;
import com.mycom.backenddaengplace.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> getMember(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(ApiResponse.success("회원 조회가 완료되었습니다.",
                memberService.getMember(member.getId())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BaseMemberResponse>> registerMember(
            @RequestParam("memberData") String memberString,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        try {
            MemberRegisterRequest request = objectMapper.readValue(memberString, MemberRegisterRequest.class);
            Member member = customOAuth2User.getMember();
            request.setEmail(member.getEmail());

            BaseMemberResponse response = memberService.registerMember(request, file);
            return ResponseEntity.ok(ApiResponse.success("회원 등록이 완료되었습니다.", response));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("회원 데이터 파싱에 실패했습니다.", e);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> updateMember(
            @RequestParam("memberData") String memberString,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        try {
            MemberUpdateRequest request = objectMapper.readValue(memberString, MemberUpdateRequest.class);
            Member member = customOAuth2User.getMember();

            BaseMemberResponse response = memberService.reviseMember(request, file, member.getId());
            return ResponseEntity.ok(ApiResponse.success("회원 수정이 완료되었습니다.", response));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("회원 데이터 파싱에 실패했습니다.", e);
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> deleteMember(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        BaseMemberResponse response = memberService.logicalDeleteMember(member.getId());
        return ResponseEntity.ok(ApiResponse.success("회원 논리적 삭제가 완료되었습니다.", response));
    }

    @PostMapping("/check-duplicate-nickname")
    public ResponseEntity<ApiResponse<DuplicateCheckResponse>> checkDuplicateNickname(
            @Valid @RequestBody NicknameCheckRequest request
    ) {
        DuplicateCheckResponse response = memberService.checkDuplicateNickname(request.getNickname());
        return ResponseEntity.ok(ApiResponse.success("닉네임 중복 체크가 완료되었습니다.", response));
    }
}