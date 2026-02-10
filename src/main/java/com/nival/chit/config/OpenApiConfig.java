package com.nival.chit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Central OpenAPI configuration for the Chit Fund backend.
 *
 * <p>This metadata is used to generate human‑readable HTML documentation
 * via the built‑in Swagger UI at {@code /swagger-ui.html}.</p>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Chit Fund Platform API",
                version = "v1",
                description = """
                        Backend APIs for the digital chit fund platform.

                        The API covers:
                        - User and membership management
                        - Chit group lifecycle and auctions
                        - Loans, ledgers, and payments
                        - Group media (Poster area) and Voting/Polling
                        - Notifications, reporting, and Accountant audit flows
                        """,
                contact = @Contact(
                        name = "Chit Fund Platform",
                        url = "https://example.com"
                ),
                license = @License(
                        name = "Proprietary",
                        url = "https://example.com/license"
                )
        ),
        servers = {
                @Server(
                        description = "Local development",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
}


