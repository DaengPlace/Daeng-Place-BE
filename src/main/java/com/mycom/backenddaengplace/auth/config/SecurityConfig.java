package com.mycom.backenddaengplace.auth.config;

import com.mycom.backenddaengplace.auth.handler.CustomSuccessHandler;
import com.mycom.backenddaengplace.auth.jwt.CustomLogoutFilter;
import com.mycom.backenddaengplace.auth.jwt.JWTFilter;
import com.mycom.backenddaengplace.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

    private final JWTFilter jwtFilter;
    private final CustomLogoutFilter customLogoutFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (REST API 스타일에서는 보통 사용하지 않음)
                .csrf(AbstractHttpConfigurer::disable)
                // 폼 로그인 비활성화 (소셜 로그인만 사용)
                .formLogin(AbstractHttpConfigurer::disable)
                // HTTP 기본 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // CORS 설정
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8080")); // React 프론트엔드 URL
                    configuration.setAllowedMethods(Collections.singletonList("*")); // 모든 HTTP 메서드 허용
                    configuration.setAllowCredentials(true); // 쿠키 허용
                    configuration.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더 허용
                    configuration.setMaxAge(3600L); // CORS 캐싱 시간 설정
                    return configuration;
                }))

                // 소셜 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)) // 사용자 정보 처리
                        .successHandler(customSuccessHandler)) // 로그인 성공 시 처리

                // JWT 인증 필터 추가
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 로그아웃 필터 추가
                .addFilterBefore(customLogoutFilter, LogoutFilter.class)

                // 권한별 요청 처리
                .authorizeHttpRequests(auth -> auth

                        // 토큰 재발급, 루트 페이지, 로그인, OAuth2 요청은 인증 없이 접근 가능
                        .requestMatchers("/reissue", "/", "/login", "/oauth2/**").permitAll()

                        // 나머지 요청은 인증 필요
                        .anyRequest().authenticated())

                // 세션 정책: Stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

