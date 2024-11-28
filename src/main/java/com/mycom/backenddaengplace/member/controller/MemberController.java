package com.mycom.backenddaengplace.member.controller;


import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.member.dto.request.EmailCheckRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberRegisterRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberReviseRequest;
import com.mycom.backenddaengplace.member.dto.response.EmailCheckResponse;
import com.mycom.backenddaengplace.member.dto.response.BaseMemberResponse;
import com.mycom.backenddaengplace.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/profile/{memberId}")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> getMember(@PathVariable("memberId") Long memberId) {
        BaseMemberResponse response = memberService.getMember(memberId);
        return ResponseEntity.ok(ApiResponse.success("회원 조회가 완료되었습니다.", response));
    }

    @PostMapping("/profile/{memberId}")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> reviseMember(
            @Valid @RequestBody MemberReviseRequest request, @PathVariable("memberId") Long memberId) {
        BaseMemberResponse response = memberService.reviseMember(request, memberId);
        return ResponseEntity.ok(ApiResponse.success("회원 수정이 완료되었습니다.", response));
    }

    @DeleteMapping("/profile/{memberId}")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> deleteMember(
            @PathVariable("memberId") Long memberId) {
        BaseMemberResponse response = memberService.deleteMember(memberId);
        return ResponseEntity.ok(ApiResponse.success("회원 삭제가 완료되었습니다.", response));
    }

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<BaseMemberResponse>> registerMember(
            @Valid @RequestBody MemberRegisterRequest request
    ) {
        BaseMemberResponse response = memberService.registerMember(request);
        return ResponseEntity.ok(ApiResponse.success("회원 등록이 완료되었습니다.", response));
    }

    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<EmailCheckResponse>> checkMemberEmail(
            @Valid @RequestBody EmailCheckRequest request
    ) {
        EmailCheckResponse response = memberService.checkEmail(request);
        return ResponseEntity.ok(ApiResponse.success("이메일 중복 체크가 완료되었습니다.", response));
    }


}
