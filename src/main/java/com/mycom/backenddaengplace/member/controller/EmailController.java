package com.mycom.backenddaengplace.member.controller;

import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.member.dto.request.BaseEmailRequest;
import com.mycom.backenddaengplace.member.dto.request.EmailCodeCheckRequest;
import com.mycom.backenddaengplace.member.dto.response.EmailCodeCheckResponse;
import com.mycom.backenddaengplace.member.dto.response.EmailDuplicateCheckResponse;
import com.mycom.backenddaengplace.member.dto.response.EmailSendCodeResponse;
import com.mycom.backenddaengplace.member.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/check-duplicate")
    public ResponseEntity<ApiResponse<EmailDuplicateCheckResponse>> checkDuplicateEmail(
            @Valid @RequestBody BaseEmailRequest request
    ) {
        EmailDuplicateCheckResponse response = emailService.checkDuplicateEmail(request);
        return ResponseEntity.ok(ApiResponse.success("이메일 중복 체크가 완료되었습니다.", response));
    }

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<EmailSendCodeResponse>> sendEmailCode(
            @Valid @RequestBody BaseEmailRequest request) throws MessagingException, UnsupportedEncodingException {
        EmailSendCodeResponse response = emailService.sendEmailCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("인증 번호를 전송하였습니다.", response));
    }

    @PostMapping("/check-code")
    public ResponseEntity<ApiResponse<EmailCodeCheckResponse>> emailCodeCheck(
            @Valid @RequestBody EmailCodeCheckRequest request) {
        EmailCodeCheckResponse response = emailService.checkEmailCode(request);
        return ResponseEntity.ok(ApiResponse.success("이메일 인증 성공하였습니다.", response));
    }


}
