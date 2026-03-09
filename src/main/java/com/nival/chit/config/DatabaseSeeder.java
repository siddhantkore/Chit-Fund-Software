package com.nival.chit.config;

import com.nival.chit.entity.User;
import com.nival.chit.enums.UserRoles;
import com.nival.chit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Checking for Super Admin user (chitadmin)...");
        
        if (userRepository.findByUsername("chitadmin").isEmpty()) {
            User admin = new User();
            admin.setName("SaaS Owner");
            admin.setUsername("chitadmin");
            // Encoding password explicitly here so it matches the AuthController logic.
            admin.setPassword(passwordEncoder.encode("chitadmin"));
            admin.setEmail("admin@chitfund.saas");
            admin.setPhone(1234567890L);
            admin.setRole(UserRoles.ADMIN);
            
            userRepository.save(admin);
            log.info("Super Admin 'chitadmin' created successfully.");
        } else {
            log.info("Super Admin 'chitadmin' already exists.");
        }
    }
}
