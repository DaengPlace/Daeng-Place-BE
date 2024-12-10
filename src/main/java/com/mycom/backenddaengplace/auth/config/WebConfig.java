package com.mycom.backenddaengplace.auth.config;

import com.mycom.backenddaengplace.auth.interceptor.AuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
         * TODO: 로그인 체크 인터셉터 등록 (사실 이 역할을 SecurityConfig 에서 처리하므로, 이 예제에서는 사용하지 않음)
         *  cf . .authorizeHttpRequests(auth -> auth
         *              // (...)
         *              .anyRequest().authenticated()) // 그 외의 요청은 인증 필요
         */
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/**") // 모든 경로에 적용, '/' 제외 필요 없음
                .excludePathPatterns("/error", "/logout", "/api/login"); // 에러 페이지, 로그아웃, 로그인 API는 제외
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://daengplace.vercel.app")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}