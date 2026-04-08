# Backend Context: Chit Fund Service

The backend is a Java Spring Boot application built with Maven, focusing on Chit Fund management operations.

## Technical Stack
- **Framework:** Spring Boot 3.5.7
- **Language:** Java 21
- **Build Tool:** Maven
- **Database:** PostgreSQL / MySQL (configured via `spring-boot-starter-data-jpa`)
- **Key Starters:** 
    - `spring-boot-starter-data-jpa`: ORM/Persistence
    - `spring-boot-starter-security`: Authentication/Authorization
    - `spring-boot-starter-web`: Web API support
    - `spring-boot-starter-websocket`: Real-time communication
    - `spring-boot-starter-mail`: Notification handling
- **API Documentation:** SpringDoc OpenAPI (Swagger UI)
- **Quality & Standards:** Checkstyle, Spotless (Google Java Format)

## Key Directories
- `src/main/java`: Core application logic (controllers, services, entities, repositories).
- `src/main/resources`: Configuration (e.g., application properties, Logback settings).
- `config/checkstyle`: Static analysis rules.
- `docker`: Deployment-related scripts and entry points.

## Build and Quality
- **Maven lifecycle:** Uses standard Maven lifecycle phases.
- **Checkstyle:** Configured to fail on violations during the `verify` phase.
- **Formatting:** Spotless is enforced during the `verify` phase using AOSP style.
