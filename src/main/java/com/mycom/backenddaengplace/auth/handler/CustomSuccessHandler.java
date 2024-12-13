package com.mycom.backenddaengplace.auth.handler;

import com.mycom.backenddaengplace.auth.domain.RefreshEntity;
import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.auth.jwt.JWTUtil;
import com.mycom.backenddaengplace.auth.repository.RefreshRepository;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
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

        // DB에서 회원 여부 확인
        Optional<Member> member = memberRepository.findByProviderAndProviderId(provider, providerId);

        if (member.isPresent()) {
            log.info("Existing member found: {}", member.get().getEmail());

            // Access와 Refresh 토큰 생성
            String accessToken = jwtUtil.createJwt("access", username, role, 60000000L);
            String refreshToken = jwtUtil.createJwt("refresh", username, role, 2592000000L);

            // Refresh 토큰 저장
            addRefreshEntity(username, refreshToken, 2592000000L);

            // 프론트엔드 리다이렉트 URL에 토큰 추가
            String redirectUrl = String.format(
                    "http://localhost:3000/main?accessToken=%s&refreshToken=%s",
                    accessToken,
                    refreshToken
            );

            // 클라이언트 리다이렉트
            response.sendRedirect(redirectUrl);

        } else {
            log.info("No member found. Please register.");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"No member found. Please register.\"}");
        }
    }


    private void addRefreshEntity(String username, String refreshToken, Long expiredMs) {
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refreshToken);
        refreshEntity.setExpiration(new Date(System.currentTimeMillis() + expiredMs).toString());
        refreshRepository.save(refreshEntity);
    }
}
