package com.nival.chit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${app.cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${app.cors.allow-credentials:false}")
    private boolean allowCredentials;

    @Override
    public void addCorsMappings(CorsRegistry registry){
        String[] origins = Arrays.stream(allowedOrigins.split(","))
                                 .map(String::trim)
                                 .filter(s -> !s.isEmpty())
                                 .toArray(String[]::new);
        String[] methods = Arrays.stream(allowedMethods.split(","))
                                 .map(String::trim)
                                 .filter(s -> !s.isEmpty())
                                 .toArray(String[]::new);
        String[] headers = Arrays.stream(allowedHeaders.split(","))
                                 .map(String::trim)
                                 .filter(s -> !s.isEmpty())
                                 .toArray(String[]::new);

        var reg = registry.addMapping("/**")
                          .allowedMethods(methods)
                          .allowedHeaders(headers)
                          .allowCredentials(allowCredentials);

        // If explicit wildcard is used, prefer origin patterns (supports "*")
        if (origins.length == 1 && "*".equals(origins[0])) {
            reg.allowedOriginPatterns("*");
        } else if (origins.length > 0) {
            reg.allowedOrigins(origins);
        } else {
            reg.allowedOriginPatterns("*");
        }
    }
}
