package com.nival.chit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the chit fund application.
 * Provides password encoding and basic authentication setup.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * Password encoder bean for hashing passwords.
     * Uses BCrypt algorithm for secure password storage.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/user", "/api/chit/user/register").permitAll()  // public APIs
                        .anyRequest().authenticated()                // everything else needs login
                )
                .httpBasic(Customizer.withDefaults())               // basic auth
                .formLogin(Customizer.withDefaults());              // default login page

        return http.build();
    }
}
