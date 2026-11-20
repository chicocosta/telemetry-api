package org.exemple.telemetryapi.config;

import org.exemple.telemetryapi.security.ApiKeyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import lombok.RequiredArgsConstructor;

@Configuration 
@RequiredArgsConstructor 
public class WebConfig implements WebMvcConfigurer {
    private final ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
            .addPathPatterns("/v1/telemetry/**");
    }
}
