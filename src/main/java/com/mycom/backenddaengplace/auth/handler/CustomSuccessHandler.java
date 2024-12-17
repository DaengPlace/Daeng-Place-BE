package com.mycom.backenddaengplace.auth.handler;

import com.mycom.backenddaengplace.auth.domain.RefreshEntity;
import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.auth.jwt.JWTUtil;
import com.mycom.backenddaengplace.auth.repository.RefreshRepository;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // OAuth2User 정보를 가져옴
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        String provider = customUserDetails.getProvider();
        String providerId = customUserDetails.getProviderId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.iterator().next().getAuthority();

        // Access와 Refresh 토큰 생성
        String accessToken = jwtUtil.createJwt("access", username, role, 60000000L);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, 2592000000L);

        // Refresh 토큰을 저장
        addRefreshEntity(username, refreshToken, 2592000000L);

        // DB에서 회원 여부 확인
        Optional<Member> optionalMember = memberRepository.findByProviderAndProviderId(provider, providerId);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            log.info("Existing member found: {}", member.getEmail());

            if (isCompleteMember(member)) {
                // 기존 회원: 메인 페이지로 리다이렉트
                redirectWithTokens(response, accessToken, refreshToken, "/main");
            } else {
                // 첫 로그인 회원: 프론트엔드의 회원가입 경로(/signin)로 리다이렉트
                redirectWithTokens(response, accessToken, refreshToken, "/signin");
            }
        } else {
            // 회원 정보가 없는 경우 (에러 처리)
            log.info("No member found. Please register.");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"No member found. Please register.\"}");
        }
    }

    private void redirectWithTokens(HttpServletResponse response, String accessToken, String refreshToken, String path) throws IOException {
        // Refresh Token을 HttpOnly Secure 쿠키로 저장
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true); // JavaScript에서 접근 불가
        refreshCookie.setSecure(true);   // HTTPS 환경에서만 전송
        refreshCookie.setPath("/");      // 전체 경로에서 유효
        refreshCookie.setMaxAge(2592000); // 만료 시간 설정 (30일)
        response.addCookie(refreshCookie);

        // 리다이렉트 URL에 Access Token만 추가
        String redirectUrl = String.format("http://localhost:3000%s?accessToken=%s", path, accessToken);

        response.sendRedirect(redirectUrl);
    }

    private boolean isCompleteMember(Member member) {
        // 모든 필드가 채워져 있는지 확인
        return member.getGender() != null &&
                member.getState() != null &&
                member.getCity() != null;
    }

    private void addRefreshEntity(String username, String refreshToken, Long expiredMs) {
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refreshToken);
        refreshEntity.setExpiration(new Date(System.currentTimeMillis() + expiredMs).toString());
        refreshRepository.save(refreshEntity);
    }
}