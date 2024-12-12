package com.mycom.backenddaengplace.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycom.backenddaengplace.auth.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/oauth2/kakao")
public class KakaoOAuthController {

    private final JWTUtil jwtUtil;
    private final RestTemplate restTemplate;

    // 환경 변수에서 Kakao Client ID 가져오기
    @Value("${KAKAO_CLIENT_ID}")
    private String kakaoClientId;

    public KakaoOAuthController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/callback")
    public ResponseEntity<?> handleKakaoCallback(@RequestParam String code) throws IOException {
        // 카카오로부터 액세스 토큰 요청
        String tokenUrl = "https://kauth.kakao.com/oauth/token"; // 카카오 토큰 발급 URL

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", kakaoClientId); // 환경 변수에서 가져온 값 사용
        parameters.add("redirect_uri", "https://api.daengplace.com/auth/kakao");
        parameters.add("code", code);

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 필수 Content-Type 설정
        HttpEntity<MultiValueMap<String, String>> accessTokenRequestEntity = new HttpEntity<>(parameters, tokenHeaders);

        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(tokenUrl, accessTokenRequestEntity, String.class);
        Map<String, Object> tokenResponseBody = new ObjectMapper().readValue(tokenResponse.getBody(), Map.class);

        String accessToken = (String) tokenResponseBody.get("access_token");

        // 2. 사용자 정보 요청
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> userInfoRequestEntity = new HttpEntity<>(userInfoHeaders);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequestEntity, Map.class);

        Map<String, Object> userInfo = userInfoResponse.getBody();
        String username = userInfo.get("id").toString();

        // 3. JWT 토큰 생성
        String jwtToken = jwtUtil.createJwt("access", username, "ROLE_USER", 600000L);

        // 4. 프론트엔드에 JWT 토큰 전달
        return ResponseEntity.ok(Map.of("accessToken", jwtToken));
    }
}
