package com.mycom.backenddaengplace.auth.controller;

import com.mycom.backenddaengplace.auth.repository.RefreshRepository;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
@Slf4j
public class LogoutController {

    private final RefreshRepository refreshRepository;

    public LogoutController(RefreshRepository refreshRepository) {
        this.refreshRepository = refreshRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractTokenFromCookies(request);

        if (refreshToken == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Refresh token not found in cookies", HttpStatus.BAD_REQUEST.value()));
        }

        if (!refreshRepository.existsByRefresh(refreshToken)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid refresh token", HttpStatus.BAD_REQUEST.value()));
        }

        refreshRepository.deleteByRefresh(refreshToken);
        clearRefreshCookie(response);

        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }

    private void clearRefreshCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
