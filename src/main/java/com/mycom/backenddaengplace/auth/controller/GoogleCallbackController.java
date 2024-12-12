package com.mycom.backenddaengplace.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth2/google")
public class GoogleCallbackController {

    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> handleGoogleCallback(@RequestParam("code") String authorizationCode) {
        // JSON 형태로 반환
        return ResponseEntity.ok(Map.of("authorizationCode", authorizationCode));
    }
}
