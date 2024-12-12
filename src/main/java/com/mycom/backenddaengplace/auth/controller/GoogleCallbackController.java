package com.mycom.backenddaengplace.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/google")
public class GoogleCallbackController {

    @GetMapping("/callback")
    public ResponseEntity<String> handleGoogleCallback(@RequestParam("code") String authorizationCode) {
        // 코드 처리 로직
        return ResponseEntity.ok("Authorization Code: " + authorizationCode);
    }
}
