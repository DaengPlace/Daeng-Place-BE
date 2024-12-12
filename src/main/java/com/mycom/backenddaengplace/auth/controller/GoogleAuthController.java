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
import org.springframework.http.HttpMethod;
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
public class GoogleAuthController {

    private final JWTUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;

    private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";
    private static final String CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID";
    private static final String CLIENT_SECRET = "YOUR_GOOGLE_CLIENT_SECRET";
    private static final String REDIRECT_URI = "https://api.daengplace.com/oauth2/google/callback";

    @GetMapping("/google")
    public ResponseEntity<ApiResponse<?>> handleGoogleLogin(@RequestParam("code") String authorizationCode, HttpServletResponse response) {
        try {
            // Step 1: Authorization Code -> Access Token
            Map<String, String> tokenResponse = getAccessTokenFromGoogle(authorizationCode);
            String accessToken = tokenResponse.get("access_token");

            // Step 2: Access Token -> User Info
            UserDTO userDTO = getUserInfoFromGoogle(accessToken);

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
            log.error("Error during Google login: ", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to authenticate using Google", 400));
        }
    }

    private Map<String, String> getAccessTokenFromGoogle(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", "YOUR_GOOGLE_CLIENT_ID");
        requestBody.add("client_secret", "YOUR_GOOGLE_CLIENT_SECRET");
        requestBody.add("redirect_uri", "https://api.daengplace/oauth2/google/callback");
        requestBody.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token", requestEntity, Map.class
        );

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new IllegalStateException("Failed to retrieve access token from Google");
        }

        return response.getBody();
    }


    private UserDTO getUserInfoFromGoogle(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new IllegalStateException("Failed to retrieve user info from Google");
        }

        Map<String, Object> userInfo = response.getBody();

        return new UserDTO(
                "google",
                userInfo.get("id").toString(),
                userInfo.get("name").toString(),
                userInfo.get("email").toString(),
                userInfo.get("picture").toString()
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
