package com.mycom.backenddaengplace.auth.jwt;

import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.auth.dto.UserDTO;
import com.mycom.backenddaengplace.auth.repository.AuthMemberRepository;
import com.mycom.backenddaengplace.member.domain.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final AuthMemberRepository authMemberRepository; // 추가

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // JWT 헤더 확인 및 로깅
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("{}{} {}{}Authorization: {}", System.lineSeparator(), request.getMethod(), request.getRequestURI(),
                System.lineSeparator(), accessToken);

        // **수정됨**: 헤더에 토큰이 없으면 다음 필터로 바로 진행
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            log.info("No Authorization header or invalid format.");
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " 제거
        accessToken = accessToken.substring(7);

        try {
            // **수정됨**: JWT 만료 여부 확인 (예외 처리)
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            log.warn("Access token expired: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("access token expired"); // 클라이언트에 메시지 전송
            return;
        }

        // 토큰 카테고리 확인
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            log.warn("Invalid access token category: {}", category);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("invalid access token");
            return;
        }

        // 사용자 정보 추출
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        // **수정됨**: username 파싱 로직 예외 처리 추가
        String[] parts = username.split("_");
        if (parts.length != 2) {
            log.error("Invalid username format in token: {}", username);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("invalid username format");
            return;
        }

        String provider = parts[0];
        String providerId = parts[1];

        // **수정됨**: 사용자 정보 조회 실패 시 로그 추가
        Member member = authMemberRepository.findByProviderAndProviderId(provider, providerId);
        if (member == null) {
            log.warn("No member found for provider: {}, providerId: {}", provider, providerId);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("user not found");
            return;
        }

        // UserDTO 생성
        UserDTO userDTO = new UserDTO();
        userDTO.setProvider(provider);
        userDTO.setProviderId(providerId);
        userDTO.setEmail(member.getEmail());
        userDTO.setNickname(member.getNickname());
        userDTO.setProfileImage(member.getProfileImageUrl());

        // CustomOAuth2User 생성
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO, member);

        // **수정됨**: SecurityContext에 인증 정보 설정
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customOAuth2User,
                null,
                customOAuth2User.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.info("Authentication set for user: {}", username);

        // **수정 없음**: 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/hc")
                || path.equals("/health")
                || path.equals("/logout")
                || path.startsWith("/oauth2")
                || path.startsWith("/kakao/callback")
                || path.startsWith("/auth");
    }
}
