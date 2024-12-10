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
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/error", "/logout", "/login", 
                    "/oauth2/**", "/auth/**", "/reissue","/health", 
                    "/hc", "/reviews/**", "/ocr/**", "/places/**", "/email/**"
                );
    }
}
