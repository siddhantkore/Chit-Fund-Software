# Backend Agent Guide

This file is for coding assistants working inside `chit`.

## Mission

The backend is the source of truth for business rules, data persistence, role enforcement, and chat authorization in the Chit Fund platform.

It owns:

- authentication and current-user resolution
- group and membership access control
- payments, loans, ledger, reports, and notifications
- group-scoped chat and websocket security
- deterministic local seed data

When in doubt, preserve backend-enforced scope rules even if the frontend currently looks permissive.

## Stack

- Java 21
- Spring Boot 3.5.7
- Spring Web
- Spring Data JPA
- Spring Security
- Spring WebSocket
- Spring Mail
- MySQL runtime driver
- PostgreSQL runtime driver also present in pom
- Lombok
- SpringDoc OpenAPI
- Maven
- Checkstyle + Spotless in `verify`

## Entry Points

- Main application: `src/main/java/com/nival/chit/ChitApplication.java`
- Runtime config: `src/main/resources/application.properties`
- Security config: `src/main/java/com/nival/chit/security/SecurityConfig.java`
- Global exception handling: `src/main/java/com/nival/chit/exceptions/GlobalExceptionHandler.java`
- Database seed: `src/main/java/com/nival/chit/config/DatabaseSeeder.java`

## Runtime Defaults

- Port: `8080`
- Default DB: MySQL `chitfund_db`
- `spring.jpa.hibernate.ddl-auto=update`
- SQL logging enabled
- CORS is configurable through `app.cors.*`

## Critical Local Environment Warning

The backend currently wipes and reseeds local data at startup through `DatabaseSeeder`.

- Trigger: `@Value("${app.seed.reset-on-start:true}")`
- Default behavior: reset happens unless explicitly disabled
- Consequence: local data is not stable across restarts
- Numeric IDs are not reliable anchors across repeated reseeds

If a future task depends on persistent local data, first decide whether the seed reset should stay enabled.

## Security Model

### Authentication

- HTTP Basic
- Stateless session policy
- Custom user lookup via `CustomUserDetailsService`
- Explicit `AuthController` provides `/auth/login` and `/auth/me`

### Authorization

There are two levels of authority:

- system role on `User.role`
- group role on `Membership.role`

System role meanings:

- `ADMIN`: SaaS admin
- `MEMBER`
- `ACCOUNTANT`

Group role meanings:

- `ADMIN`
- `MEMBER`

Membership status is also important:

- `ACTIVE`
- `PENDING`
- `SUSPENDED`
- `INACTIVE`

Most group-scoped access requires active membership. Group administration requires active membership plus `GroupRole.ADMIN`.

## Most Important Authorization Helper

- `src/main/java/com/nival/chit/security/AccessControlService.java`

This is the first place to inspect before changing role or scope behavior. It currently centralizes:

- current user resolution
- SaaS admin checks
- self-or-admin checks
- active membership checks
- group-admin checks

## Websocket Chat Security

Files involved:

- `config/WebSocketConfig.java`
- `security/WebSocketAuthChannelInterceptor.java`
- `controllers/GroupChatController.java`
- `services/GroupChatService.java`

Important behavior:

- websocket connect expects Basic auth
- subscriptions to `/topic/group/{groupId}` are membership-checked
- REST chat history and send endpoints are also membership-checked

## Package Responsibilities

- `config`: application configuration, CORS, websocket, logging, seed data
- `controllers`: HTTP API endpoints
- `controllers/chitgroup`: group-specific CRUD and membership operations
- `dto`: request/response models
- `entity`: JPA entities
- `enums`: role, status, and domain enums
- `exceptions`: global exception translation
- `repository`: JPA repositories and custom queries
- `security`: auth, access control, websocket auth
- `services`: business logic layer
- `utils`: helper utilities

## Common Change Entry Points

### Auth or identity

- `controllers/AuthController.java`
- `security/SecurityConfig.java`
- `security/CustomUserDetailsService.java`
- `security/AccessControlService.java`

### Group and membership rules

- `services/ChitGroupService.java`
- `services/ChitGroupMemberOperationsService.java`
- `repository/MembershipRepository.java`
- `entity/Membership.java`
- `enums/GroupRole.java`

### Payments, loans, ledger, reports

- `services/PaymentsService.java`
- `services/MemberLoanService.java`
- `services/LedgerService.java`
- `services/ReportingService.java`

These are role-sensitive and should remain aligned with membership scope.

### Chat

- `services/GroupChatService.java`
- `controllers/GroupChatController.java`
- `config/WebSocketConfig.java`
- `security/WebSocketAuthChannelInterceptor.java`

### Notifications

- `services/NotificationService.java`
- `controllers/NotificationController.java`

### Seed data

- `config/DatabaseSeeder.java`

Use this file for local demo/testability changes, but be careful: it resets the whole dataset at startup.

## Controller Design Notes

- Most controller paths live under `/api/chit/...`
- Controllers are thin and mostly delegate to services
- If behavior changes, service changes usually matter more than controller changes

## Error Handling Notes

- `AuthenticationException` -> `401`
- `AccessDeniedException` -> `403`
- `NoResourceFoundException` -> `404`
- `IllegalArgumentException` -> `400`
- uncaught errors -> `500`

If the UI sees unexpected `500`s for access issues, check `GlobalExceptionHandler` first.

## Build And Validation

- Compile: `./mvnw -q -DskipTests compile`
- Run app: `./mvnw spring-boot:run`
- Full quality gate: `./mvnw verify`

`verify` is stricter than `compile` because of Checkstyle and Spotless.

## Editing Guidelines For Future Agents

- Start from the service layer for business rules.
- Use repositories only after understanding the service contract.
- Keep controller paths and frontend `apiRoutes` aligned.
- If you introduce a new scope rule, add it to `AccessControlService` or make the reason for not doing so explicit.
- When changing seed behavior, consider startup impact and developer expectations.
- Do not assume group IDs or user IDs are stable after restart.

