# Backend Project Map

## Top-Level Structure

- `pom.xml`: Maven config and dependencies
- `README.md`: generic project info
- `BACKEND_CONTEXT.md`: older lightweight summary
- `AGENTS.md`: operational backend guide for coding assistants
- `PROJECT_MAP.md`: this map
- `src/main/java/com/nival/chit`: application code
- `src/main/resources`: runtime config
- `config/checkstyle`: style rules
- `docker/`: container-related assets

## Package Map

### `config`

Key files:

- `WebSocketConfig.java`
- `DatabaseSeeder.java`
- other app config classes such as CORS/logging

Purpose:

- app wiring
- websocket broker and endpoint config
- startup seed/reset behavior

### `controllers`

Key API surfaces:

- `AuthController`
- `UserController`
- `MembershipController`
- `ChitGroupController`
- `ChitGroupOperationsController`
- `PaymentsController`
- `MemberLoanController`
- `LedgerController`
- `ReportingController`
- `NotificationController`
- `GroupChatController`
- `GroupMediaController`
- `AuctionController`
- `PollController`
- `FundsController`

### `services`

Main business layer:

- `UserService`
- `ChitGroupService`
- `ChitGroupMemberOperationsService`
- `PaymentsService`
- `MemberLoanService`
- `LedgerService`
- `ReportingService`
- `NotificationService`
- `GroupChatService`
- `GroupMediaService`
- `AuctionService`
- `PollService`
- `FundsService`
- `LedgerPdfService`

### `security`

Critical files:

- `SecurityConfig.java`
- `CustomUserDetailsService.java`
- `AccessControlService.java`
- `WebSocketAuthChannelInterceptor.java`

### `entity`

Important domain entities include:

- `User`
- `Membership`
- `ChitGroup`
- `Funds`
- `Payments`
- `MemberLoan`
- `Ledger`
- `Auction`
- `Notification`
- `NotificationConfig`
- `GroupChatMessage`
- `GroupMedia`
- `Poll`
- `PollOption`
- `Vote`

### `repository`

JPA repositories plus custom query methods for visibility, reporting, and group scope.

## Endpoint Map

### Auth

- `/api/chit/auth/login`
- `/api/chit/auth/me`

### Users

- `/api/chit/user/register`
- `/api/chit/user/{userId}`
- `/api/chit/user/username/{username}`
- `/api/chit/user/all`

### Groups

- `/api/chit/chit/`
- `/api/chit/chit/{groupId}`
- `/api/chit/chit/code/{groupCode}`
- `/api/chit/chit/search`
- `/api/chit/chit/all`
- `/api/chit/chit/status/{status}`

### Group membership operations

- `/api/chit/chit/{groupId}/members/{userId}`
- `/api/chit/chit/{groupId}/members/`
- `/api/chit/chit/{groupId}/members/{userId}/status`
- `/api/chit/membership/user/{userId}`
- `/api/chit/membership/join`

### Money and reporting

- `/api/chit/payments/...`
- `/api/chit/loan/...`
- `/api/chit/ledger/...`
- `/api/chit/report/...`
- `/api/chit/funds/...`

### Collaboration

- `/api/chit/chat/...`
- `/api/chit/media/...`
- `/api/chit/notifications/...`
- `/api/chit/polls...`

## Current Access-Control Map

### SaaS admin

- system role: `User.role == ADMIN`
- can see all groups
- can bypass normal group membership checks in `AccessControlService`

### Group admin

- requires active membership in that group
- requires `Membership.role == GroupRole.ADMIN`
- admin power is only for that specific group

### Member

- can only access groups where membership is active
- can only see own user-scoped or own group-scoped data unless elevated by specific rules

### Accountant

- still a user-level role
- has payment verification relevance
- does not automatically become group admin

## Membership Flow Map

1. Group is created
2. Creator becomes membership `ACTIVE + GroupRole.ADMIN`
3. Another user joins by group code
4. Join creates `PENDING` membership
5. Group admins receive join-request notifications
6. Group admin changes membership status to activate access

Core files:

- `services/ChitGroupService.java`
- `services/ChitGroupMemberOperationsService.java`
- `repository/MembershipRepository.java`

## Chat Flow Map

### REST

- get history: `GroupChatController -> GroupChatService`
- send message: `GroupChatController -> GroupChatService`

### Websocket

- endpoint: `/ws/chat`
- broker topic: `/topic/group/{groupId}`
- auth/subscription checks: `WebSocketAuthChannelInterceptor`

### Guard rails

- only active members of a group can read or send group chat messages
- websocket subscription is also membership-checked, not only REST

## Seed Data Map

Seeder file:

- `config/DatabaseSeeder.java`

Behavior:

- deletes existing application data
- nulls `ChitGroup.fundId` before fund deletion
- seeds users, groups, memberships, notifications, payments, loans, ledger entries, and chat messages

Current seeded usernames:

- `chitadmin`
- `anitaadmin`
- `bharatlead`
- `charumember`
- `devmember`
- `acctmeera`

Important note:

- seeded records are suitable for demos and local verification
- repeated restarts recreate the domain state but not stable numeric IDs

## High-Risk Files

These files influence large areas of behavior and should be edited carefully:

- `security/AccessControlService.java`
- `security/SecurityConfig.java`
- `security/WebSocketAuthChannelInterceptor.java`
- `exceptions/GlobalExceptionHandler.java`
- `config/DatabaseSeeder.java`
- `services/PaymentsService.java`
- `services/MemberLoanService.java`
- `services/LedgerService.java`
- `services/ReportingService.java`
- `services/ChitGroupMemberOperationsService.java`
- `services/ChitGroupService.java`

## Practical Debug Paths

### User sees wrong data scope

1. Check service authorization call
2. Check `AccessControlService`
3. Check `MembershipRepository` query
4. Confirm seed/user membership state

### Frontend gets wrong status code

1. Check service exception type
2. Check `GlobalExceptionHandler`
3. Check Spring Security entry point behavior

### Chat works in REST but not websocket

1. Check `WebSocketConfig`
2. Check `WebSocketAuthChannelInterceptor`
3. Check frontend websocket auth header
4. Check group ID and membership state

### Data disappears after restart

1. Check `DatabaseSeeder`
2. Check `app.seed.reset-on-start`
3. Confirm whether restart was expected to preserve local state

