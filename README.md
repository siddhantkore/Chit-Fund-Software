<div align="center">
  <h1>Chit Fund Management Software</h1>
</div>

<p align="center">
  <a href="https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html">
    <img src="https://img.shields.io/badge/Java-21-orange.svg" alt="Java 21">
  </a>
  <a href="https://spring.io/projects/spring-boot">
    <img src="https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg" alt="Spring Boot 3.5.7">
  </a>
  <a href="https://spring.io/projects/spring-security">
    <img src="https://img.shields.io/badge/Spring%20Security-6.x-green.svg" alt="Spring Security">
  </a>
  <a href="https://www.mysql.com/">
    <img src="https://img.shields.io/badge/MySQL-8.0-blue.svg" alt="MySQL 8.0">
  </a>
  </br>
  <a href="https://www.postgresql.org/">
    <img src="https://img.shields.io/badge/PostgreSQL-13+-336791.svg" alt="PostgreSQL">
  </a>
  <a href="https://hibernate.org/">
    <img src="https://img.shields.io/badge/Hibernate-6.x-59666C.svg" alt="Hibernate">
  </a>
  <a href="https://www.docker.com/">
    <img src="https://img.shields.io/badge/Docker-ready-2496ED.svg" alt="Docker">
  </a>
  <a href="https://swagger.io/">
    <img src="https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D.svg" alt="Swagger OpenAPI">
  </a>
  <a href="https://maven.apache.org/">
    <img src="https://img.shields.io/badge/Maven-3.8+-C71A36.svg" alt="Maven">
  </a>
</p>

---

A comprehensive digital solution for managing traditional chit funds, built with **Java Spring Boot** and designed to handle thousands of concurrent users.  This platform modernizes chit fund operations by providing robust APIs for group management, auction systems, loan tracking, payments, and real-time financial reporting.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Use Cases](#use-cases)
- [Database Schema](#database-schema)
- [Docker Deployment](#docker-deployment)
- [Security](#security)
- [Contributing](#contributing)

---

## Overview

Chit Fund Software is a enterprise-grade backend system designed to digitalize traditional chit fund operations. The platform supports two primary chit fund models:

1. **Monthly Collection Model**:  Members contribute a fixed amount monthly, with funds managed by the group president/leader
2. **Auction-Based Model**: Members contribute monthly and participate in auctions to win the pooled amount

The system enables real-time loan tracking, voting mechanisms for group decisions, member management, notifications, and comprehensive financial reporting.

---

## Features

### User & Membership Management
- User registration with role-based access (Admin, Member)
- Multi-group membership support
- Profile management and password security
- Member addition/removal by group admins

### Chit Group Operations
- Create and manage chit groups with unique group codes
- Configure group parameters (duration, monthly amount, member limits)
- Group status lifecycle management (Pending, Active, Completed, Inactive)
- Search and filter capabilities

### Auction System
- Monthly auction creation and tracking
- Winner recording with commission management
- Auction history per group and user
- Automatic ledger entry generation

### Loan Management
- Real-time loan tracking with daily interest calculation
- Multiple loan support per member across different groups
- Automated payable amount computation
- Loan status monitoring (Active, Completed, Overdue)
- Tenure and interest rate configuration

### Payment Processing
- Record monthly contributions
- Track loan repayments
- Payment history and status tracking
- Integration with ledger system

### Financial Reporting
- Real-time group financial summaries
- Member-wise financial overview across all groups
- Total contributions, outstanding loans, and fund balance tracking
- Daily recalculated reports

### Ledger System
- Comprehensive transaction logging
- Credit/debit entry tracking
- Group-wise and user-wise ledger views
- Reference linking to payments and auctions

### Notification System
- Configurable notification rules per group
- Payment reminders with advance day settings
- Loan due notifications
- Custom message templates
- Notification status tracking (Read/Unread)

### Group Chat
- Private chat area per chit group
- Real-time messaging for group members
- Message history retrieval
- Polling support for new messages

### Security
- BCrypt password encryption
- Spring Security with HTTP Basic Authentication
- Role-based access control
- CORS configuration for React frontend integration

---

## Technology Stack

### Backend
- **Java 21** - Modern Java features and performance
- **Spring Boot 3.5.7** - Application framework
- **Spring Data JPA** - Data persistence layer
- **Hibernate** - ORM implementation
- **Spring Security** - Authentication and authorization
- **Maven** - Dependency management

### Database Support
- **MySQL 8** (Primary)
- **PostgreSQL** (Alternative support)

### API Documentation
- **SpringDoc OpenAPI 3** (Swagger UI)
- Interactive API documentation at `/swagger-ui/index.html`

### Deployment
- **Docker & Docker Compose** - Containerization
- **MySQL Container** - Database service
- Health checks and orchestration

---

## Architecture

The application follows a clean **3-tier architecture**:

```
┌─────────────────────────────────────────┐
│         React Frontend Client           │
│   (github.com/siddhantkore/             │
│    Chit-Fund-Client)                     │
└──────────────┬──────────────────────────┘
               │ REST API
┌──────────────▼──────────────────────────┐
│      Spring Boot REST Controllers       │
│  (User, Groups, Auctions, Payments...)   │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Service Layer (Business Logic)   │
│  (FundsService, LoanService, etc.)       │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│   Repository Layer (Spring Data JPA)     │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│       Database (MySQL/PostgreSQL)        │
└──────────────────────────────────────────┘
```

### Key Design Patterns
- **DTO Pattern** - Data transfer between layers
- **Repository Pattern** - Data access abstraction
- **Service Layer Pattern** - Business logic encapsulation
- **RESTful API Design** - Stateless communication

---

## Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.8+**
- **MySQL 8.0+** or **PostgreSQL 13+**
- **Docker & Docker Compose** (for containerized deployment)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/siddhantkore/Chit-Fund-Software.git
   cd Chit-Fund-Software
   ```

2. **Configure database**
   - Copy example configuration: 
     ```bash
     cp src/main/resources/application. properties.example src/main/resources/application.properties
     ```
   - Update database credentials in `application.properties`

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

### Configuration

#### Database Configuration (MySQL)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chitfund_db? createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

#### PostgreSQL Alternative

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/chitfund
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

#### Security Configuration

```properties
spring.security.user.name=admin
spring.security.user.password={noop}admin123
```

### Running the Application

#### Local Development

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

#### Using Docker Compose

1. **Create environment file**
   ```bash
   cp .env.example .env
   ```
   Update `.env` with your configuration

2. **Start services**
   ```bash
   docker-compose up -d
   ```

3. **Access the application**
   - API:  `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`

4. **Stop services**
   ```bash
   docker-compose down
   ```

---

## API Documentation

Access the interactive Swagger API documentation: 

**Local**:  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### API Categories

| Category | Endpoints | Description |
|----------|-----------|-------------|
| **Users** | `/api/chit/user/*` | User registration, profiles, authentication |
| **Chit Groups** | `/api/chit/chit/*` | Group CRUD operations, search |
| **Group Members** | `/api/chit/chit/{groupId}/members/*` | Member management |
| **Auctions** | `/api/chit/auction/*` | Auction creation and tracking |
| **Payments** | `/api/chit/payments/*` | Contributions and repayments |
| **Loans** | `/api/chit/loan/*` | Loan tracking and calculations |
| **Funds** | `/api/chit/funds/*` | Fund balance and interest rates |
| **Ledger** | `/api/chit/ledger/*` | Transaction history |
| **Notifications** | `/api/chit/notifications/*` | Notification config and viewing |
| **Reporting** | `/api/chit/report/*` | Financial summaries |
| **Group Chat** | `/api/chit/chat/*` | Group messaging |
| **Memberships** | `/api/chit/membership/*` | User memberships |

---

## Use Cases

### 1. **Traditional Chit Fund Group**
A community organizes a 12-month chit fund with 10 members:
- Admin creates group with ₹10,000 monthly contribution
- Members join using group code
- Monthly payments tracked automatically
- Auction conducted each month
- Winner receives pooled amount minus commission
- Loans calculated with interest

### 2. **Savings Circle**
Friends create a saving group: 
- Fixed monthly contributions
- President manages fund
- Members view real-time balance
- Profit sharing based on interest earned
- Chat for coordination

### 3. **Microfinance Operations**
Organization manages multiple chit groups:
- Track hundreds of groups simultaneously
- Monitor loan portfolios across groups
- Generate financial reports
- Automated payment reminders
- Ledger for audit trails

### 4. **Member Experience**
Individual member: 
- Join multiple groups with one account
- View consolidated loan obligations
- Track contribution history
- Receive timely notifications
- Access financial summaries daily

---

## Database Schema

Key entities:

- **User** - System users with roles
- **ChitGroup** - Chit fund groups
- **Membership** - User-Group relationships
- **Funds** - Group fund tracking
- **Auction** - Monthly auctions
- **MemberLoan** - Loan records
- **Payments** - Contributions and repayments
- **Ledger** - Transaction logs
- **Notification** - User notifications
- **NotificationConfig** - Notification rules
- **GroupChatMessage** - Chat messages

Refer to `database schema.drawio` and `RAW_DATA.sql` for detailed schema. 

---

## Docker Deployment

### Services
- **app** - Spring Boot application (Port 8080)
- **db** - MySQL 8 database (Port 3306)

### Docker Commands

```bash
# Build and start
docker-compose up --build -d

# View logs
docker-compose logs -f app

# Stop and remove
docker-compose down -v

# Rebuild after code changes
docker-compose up --build
```

### Environment Variables

Create `.env` file:
```env
MYSQL_ROOT_PASSWORD=rootpass
MYSQL_DATABASE=chitfund_db
MYSQL_USER=chituser
MYSQL_PASSWORD=chitpass
DB_HOST=db
DB_PORT=3306
```

---

## Security

- **Password Encryption**: BCrypt hashing
- **Authentication**: HTTP Basic Auth
- **Authorization**: Role-based (ADMIN, USER)
- **CORS**:  Configured for React frontend
- **SQL Injection**: Prevented via JPA/Hibernate
- **Public Endpoints**: Only user registration

### Default Credentials
```
Username: admin
Password: admin123
```
**Change in production! **

---

## Contributing

Contributions are welcome! Please: 

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## Related Projects

- **Frontend Client**:  [Chit-Fund-Client](https://github.com/siddhantkore/Chit-Fund-Client) (React)

---

## Contact

- [Siddhant Kore](mailto:siddhskore@gmail.com)

---
