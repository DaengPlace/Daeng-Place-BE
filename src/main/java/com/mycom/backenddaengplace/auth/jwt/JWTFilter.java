package com.mycom.backenddaengplace.auth.jwt;

import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.auth.dto.CustomUserDetails;
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
import java.io.PrintWriter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final AuthMemberRepository authMemberRepository; // 추가

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("{}{} {}{}Authorization: {}", System.lineSeparator(), request.getMethod(), request.getRequestURI(),
                System.lineSeparator(), accessToken);

        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring(7); // "Bearer " 제거

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        // username에서 provider와 providerId 추출
        String[] parts = username.split("_");
        if (parts.length == 2) {
            String provider = parts[0];
            String providerId = parts[1];

            // DB에서 Member 조회  String[] parts = username.split("_");
            Member member = authMemberRepository.findByProviderAndProviderId(provider, providerId);
            if (member != null) {
                // UserDTO 생성
                UserDTO userDTO = new UserDTO();
                userDTO.setProvider(provider);
                userDTO.setProviderId(providerId);
                userDTO.setEmail(member.getEmail());
                userDTO.setNickname(member.getNickname());
                userDTO.setProfileImage(member.getProfileImageUrl());

                // CustomOAuth2User 생성
                CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO, member);

                // Authentication 객체 생성 및 SecurityContext에 설정
                Authentication authToken = new UsernamePasswordAuthenticationToken(
                        customOAuth2User,
                        null,
                        customOAuth2User.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}