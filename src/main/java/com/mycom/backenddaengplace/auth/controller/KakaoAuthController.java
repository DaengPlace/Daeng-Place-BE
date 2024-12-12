package com.mycom.backenddaengplace.auth.controller;

import com.mycom.backenddaengplace.auth.dto.UserDTO;
import com.mycom.backenddaengplace.auth.jwt.JWTUtil;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.member.domain.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/login/oauth2/code")
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthController {

    private final JWTUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;

    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String CLIENT_ID = "YOUR_KAKAO_CLIENT_ID";
    private static final String REDIRECT_URI = "https://api.daengplace.com/oauth2/kakao/callback";

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<?>> handleKakaoLogin(@RequestParam("code") String authorizationCode, HttpServletResponse response) {
        try {
            // Step 1: Authorization Code -> Access Token
            Map<String, String> tokenResponse = getAccessTokenFromKakao(authorizationCode);
            String accessToken = tokenResponse.get("access_token");

            // Step 2: Access Token -> User Info
            UserDTO userDTO = getUserInfoFromKakao(accessToken);

            // Step 3: Check if Member exists or save new Member
            Optional<Member> existingMember = memberRepository.findByProviderAndProviderId(
                    userDTO.getProvider(),
                    userDTO.getProviderId()
            );

            Member member;
            if (existingMember.isPresent()) {
                member = existingMember.get();
                log.info("Existing member logged in: {}", member.getEmail());
            } else {
                member = saveNewMember(userDTO);
                log.info("New member registered: {}", member.getEmail());
            }

            // Step 4: Issue JWT Tokens
            String jwtAccessToken = jwtUtil.createJwt("access", member.getProviderId(), "ROLE_USER", 604800000L);
            String jwtRefreshToken = jwtUtil.createJwt("refresh", member.getProviderId(), "ROLE_USER", 2592000000L);

            // Set Refresh Token as Cookie
            Cookie refreshCookie = new Cookie("refresh", jwtRefreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setMaxAge(2592000); // 30 days
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);

            // Return Access Token in JSON
            return ResponseEntity.ok(ApiResponse.success("Login successful", Map.of(
                    "accessToken", jwtAccessToken
            )));

        } catch (Exception e) {
            log.error("Error during Kakao login: ", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to authenticate using Kakao", 400));
        }
    }

    private Map<String, String> getAccessTokenFromKakao(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("redirect_uri", REDIRECT_URI);
        requestBody.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URI, requestEntity, Map.class);

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new IllegalStateException("Failed to retrieve access token from Kakao");
        }

        return response.getBody();
    }

    private UserDTO getUserInfoFromKakao(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                USER_INFO_URI, org.springframework.http.HttpMethod.GET, requestEntity, Map.class
        );

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new IllegalStateException("Failed to retrieve user info from Kakao");
        }

        Map<String, Object> userInfo = response.getBody();
        log.info("Kakao User Info: {}", userInfo);

        return new UserDTO(
                "kakao",
                userInfo.get("id").toString(),
                ((Map<String, Object>) userInfo.get("properties")).get("nickname").toString(),
                ((Map<String, Object>) userInfo.get("kakao_account")).get("email").toString(),
                ((Map<String, Object>) userInfo.get("properties")).get("profile_image").toString()
        );
    }

    private Member saveNewMember(UserDTO userDTO) {
        Member member = Member.builder()
                .email(userDTO.getEmail())
                .nickname(userDTO.getNickname())
                .profileImageUrl(userDTO.getProfileImage())
                .provider(userDTO.getProvider())
                .providerId(userDTO.getProviderId())
                .build();

        return memberRepository.save(member);
    }
}
