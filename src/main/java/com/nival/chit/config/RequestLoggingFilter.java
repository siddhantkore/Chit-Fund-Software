package com.nival.chit.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter to generate a unique request ID and manage MDC logging context.
 * Logs incoming request endpoints and overall latency.
 */
@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String MDC_REQUEST_ID_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Determine or Generate Request ID
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        // 2. Put into SLF4J MDC
        MDC.put(MDC_REQUEST_ID_KEY, requestId);

        long startTime = System.currentTimeMillis();

        try {
            // Log entry
            log.info("Incoming Request: [{}] {} - Source IP: {}", 
                    request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

            // 3. Continue filter chain
            filterChain.doFilter(request, response);
            
        } finally {
            // 4. Log exit and latency
            long duration = System.currentTimeMillis() - startTime;
            
            // Try to extract username if authenticated during filter chain
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) 
                ? " (User: " + authentication.getName() + ")" 
                : "";

            log.info("Outgoing Response: [{}] {} - Status: {} - Latency: {}ms{}", 
                    request.getMethod(), request.getRequestURI(), response.getStatus(), duration, user);

            // 5. Always clear MDC state to prevent thread-pool leakage
            MDC.remove(MDC_REQUEST_ID_KEY);
        }
    }
}
