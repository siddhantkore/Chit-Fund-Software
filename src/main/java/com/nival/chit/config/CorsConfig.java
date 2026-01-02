package com.nival.chit.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){

        var reg = registry.addMapping("/**")
                          .allowedMethods(methods)
                          .allowedHeaders(headers)
                          .allowCredentials(allowCredentials);

        // If explicit wildcard is used, prefer origin patterns (supports "*")
        if (origins.length == 1 && "*".equals(origins[0])) {
            reg.allowedOriginPatterns("*");
        } else if (origins.length > 0) {
            reg.allowedOrigins("*");
        } else {
            reg.allowedOriginPatterns("*");
        }
    }
}
