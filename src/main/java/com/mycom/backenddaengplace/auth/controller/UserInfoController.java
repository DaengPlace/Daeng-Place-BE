package com.mycom.backenddaengplace.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;

@RestController
@RequestMapping("/api")
public class UserInfoController {

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomOAuth2User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        return ResponseEntity.ok(user.getAttributes());
    }
}
