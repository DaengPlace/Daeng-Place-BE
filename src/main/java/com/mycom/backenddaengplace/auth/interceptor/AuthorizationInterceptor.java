package com.mycom.backenddaengplace.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

/**
 * TODO:
 *  인증 - 입장권 (인증 실패 시 HTTP status code 401), 인가 - 권한 보유 여부 확인 (권한 미보유 시 HTTP status code 403)
 *  cf. (서블릿) 필터와 (스프링) 인터셉터
 */
@Slf4j
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        log.info("Authorization interceptor URL: {}", request.getRequestURL());

        // /ocr/** 경로는 인증 생략
        if (uri.startsWith("/ocr/")) {
            log.info("Skipping interceptor for /ocr/**");
            return true;
        }

        String accessToken = request.getHeader("Authorization");
        if (Objects.isNull(accessToken)) {
            throw new IllegalStateException("Authentication required");
        }

        // 추가적인 검증 로직...
        return true;
    }

}
