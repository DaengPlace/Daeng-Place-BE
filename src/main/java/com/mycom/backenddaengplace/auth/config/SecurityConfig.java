package com.mycom.backenddaengplace.auth.config;

import com.mycom.backenddaengplace.auth.handler.CustomSuccessHandler;
import com.mycom.backenddaengplace.auth.jwt.CustomLogoutFilter;
import com.mycom.backenddaengplace.auth.jwt.JWTFilter;
import com.mycom.backenddaengplace.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

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
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/oauth2/**", "/auth/**", "/reissue"
                                ,"/health", "/hc","/reviews/**","/ocr/**","/places/**").permitAll()
                        .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler) // JSON 응답으로 수정
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .addFilterBefore(customLogoutFilter, LogoutFilter.class);

        return http.build();
    }
}
