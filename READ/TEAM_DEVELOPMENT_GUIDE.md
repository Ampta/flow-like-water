# Ampta Loan Service — Master Project Setup & Team Development Guide

This comprehensive guide is designed for the **Team Lead** and **all Module Developers** (Manish, Prince, Anupama, Shivam, Faraz) working on the **Ampta Loan Service** microservice.

It covers:
1. **How the Project is Built from Scratch** (Maven, Spring Boot 3/4, MySQL, Mail, JWT).
2. **Authentication & Security Architecture** (JWT, Spring Security, Dev Mode vs. Prod Mode, RBAC).
3. **Reference Module Implementation: Loan Closure & Foreclosure** (Complete End-to-End Blueprint).
4. **Developer Guide for Team Members** (Folder structure, Step-by-Step implementation recipe for new modules, Coding conventions, and Git workflow).

---

## Table of Contents
- [1. Building the Project from Scratch](#1-building-the-project-from-scratch)
  - [1.1 Spring Initializr Configuration](#11-spring-initializr-configuration)
  - [1.2 Dependency Management (`pom.xml`)](#12-dependency-management-pomxml)
  - [1.3 Application Configuration (`application.properties`)](#13-application-configuration-applicationproperties)
  - [1.4 Folder & Package Hierarchy](#14-folder--package-hierarchy)
- [2. Authentication & Security Architecture](#2-authentication--security-architecture)
  - [2.1 The Authentication Flow](#21-the-authentication-flow)
  - [2.2 Spring Security Configuration (`SecurityConfig.java`)](#22-spring-security-configuration-securityconfigjava)
  - [2.3 JWT Utility & Verification Filter](#23-jwt-utility--verification-filter)
  - [2.4 User & Customer Linkage](#24-user--customer-linkage)
  - [2.5 Dev Mode vs. Production Mode](#25-dev-mode-vs-production-mode)
- [3. Reference Implementation: Loan Closure Module](#3-reference-implementation-loan-closure-module)
  - [3.1 Business Requirements & State Machine](#31-business-requirements--state-machine)
  - [3.2 Domain Entities & Enums](#32-domain-entities--enums)
  - [3.3 Spring Data Repository](#33-spring-data-repository)
  - [3.4 Data Transfer Objects (DTOs)](#34-data-transfer-objects-dtos)
  - [3.5 Service Layer (Business Logic)](#35-service-layer-business-logic)
  - [3.6 REST Controller & API Contracts](#36-rest-controller--api-contracts)
  - [3.7 Frontend Alignment (`loan-closure-angular`)](#37-frontend-alignment-loan-closure-angular)
- [4. Developer Guide: How Teammates Must Build Their Modules](#4-developer-guide-how-teammates-must-build-their-modules)
  - [4.1 Module Ownership Matrix](#41-module-ownership-matrix)
  - [4.2 Folder Responsibilities: Where Does Your Code Go?](#42-folder-responsibilities-where-does-your-code-go)
  - [4.3 The 7-Step Recipe for Implementing Any Feature](#43-the-7-step-recipe-for-implementing-any-feature)
  - [4.4 Coding Standards & Mandatory Rules](#44-coding-standards--mandatory-rules)
  - [4.5 Git Workflow & Pull Request Checklist](#45-git-workflow--pull-request-checklist)

---

# 1. Building the Project from Scratch

### 1.1 Spring Initializr Configuration
When starting a microservice like `loan-service`, initialize it via [start.spring.io](https://start.spring.io) or IntelliJ Spring Initializr with these settings:
* **Project**: Maven
* **Language**: Java
* **Spring Boot Version**: `3.x` (or parent configured in `pom.xml`)
* **Group**: `com.ampta`
* **Artifact**: `loan-service`
* **Name**: `loan-service`
* **Package Name**: `com.ampta`
* **Packaging**: Jar
* **Java Version**: `21` (or `26` compatible JDK)

### 1.2 Dependency Management (`pom.xml`)
Add the core dependencies in your `pom.xml`:

```xml
<dependencies>
    <!-- 1. Web & REST -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>

    <!-- 2. JPA & Database -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- 3. Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- 4. Security & JWT -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.13.0</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.13.0</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.13.0</version>
        <scope>runtime</scope>
    </dependency>

    <!-- 5. 2FA (TOTP) -->
    <dependency>
        <groupId>dev.samstevens.totp</groupId>
        <artifactId>totp</artifactId>
        <version>1.7.1</version>
    </dependency>

    <!-- 6. Mail Service -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>

    <!-- 7. Utilities -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.modelmapper</groupId>
        <artifactId>modelmapper</artifactId>
        <version>3.2.6</version>
    </dependency>
</dependencies>
```

### 1.3 Application Configuration (`application.properties`)
Create `src/main/resources/application.properties` with database connection, JPA settings, JWT parameters, and mail credentials:

```properties
spring.application.name=loan-service

# Database Configuration (MySQL)
spring.datasource.url=jdbc:mysql://<HOST>:<PORT>/<DB_NAME>?useSSL=true&serverTimezone=UTC
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASSWORD>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
app.jwt.secret=verySecretKeyForLocalDevelopmentThatIsLongEnoughToSatisfyRequirements
app.jwt.expirationMs=900000          # 15 minutes
app.jwt.refreshExpirationMs=604800000 # 7 days
app.base-url=http://localhost:8080

# Security Mode: Set 'true' in development so teammates can build without 401 blocks.
# Set 'false' in production or staging to enforce strict JWT authentication!
app.security.dev-mode=true

# SMTP Mail Settings (for verification emails)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-app-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

### 1.4 Folder & Package Hierarchy
All source code resides inside `src/main/java/com/ampta`:

```text
com.ampta
 ├── config/            # ModelMapper, CORS, Swagger configs
 ├── controller/        # REST Controllers (@RestController)
 ├── dto/               # Data Transfer Objects
 │    ├── request/      # Request payloads (@Valid @RequestBody)
 │    └── response/     # Standardized responses & ApiResponse<T>
 ├── entity/            # JPA Entities & Enums
 │    └── enums/        # Role, LoanStatus, DocumentStatus, etc.
 ├── exception/         # Custom exceptions & GlobalExceptionHandler
 ├── repository/        # Spring Data JPA Repositories
 ├── security/          # SecurityConfig, JwtUtils, JwtFilter, UserDetails
 │    ├── jwt/
 │    ├── service/
 │    └── totp/
 ├── service/           # Service Interfaces
 │    └── impl/         # Service Implementations (@Service, @Transactional)
 ├── utils/             # Endpoints.java, EmailService, NotificationUtil
 └── LoanServiceApplication.java
```

---

# 2. Authentication & Security Architecture

### 2.1 The Authentication Flow
The security system implements modern stateless token authentication with optional 2FA:
1. **Registration** (`POST /api/v1/auth/register`): Creates a `User` (and linked `Customer` record) with password hashed using `BCryptPasswordEncoder`. An email verification token is dispatched.
2. **Email Verification** (`GET/POST /api/v1/auth/verify-email?token=`): Sets `isEmailVerified = true`.
3. **Login** (`POST /api/v1/auth/login`): Verifies credentials.
   - If 2FA is disabled: returns access token + refresh token directly.
   - If 2FA is enabled: returns a short-lived `preAuthToken` (5 minutes).
4. **2FA Verification** (`POST /api/v1/auth/verify-2fa`): Validates TOTP code against `preAuthToken`, issuing final access token and refresh token.
5. **Subsequent API Calls**: The client sends `Authorization: Bearer <token>` in the HTTP header.

### 2.2 Spring Security Configuration (`SecurityConfig.java`)
Key features:
* Disables CSRF (stateless REST APIs).
* Sets `SessionCreationPolicy.STATELESS`.
* Defines public endpoints (`/api/v1/auth/**`).
* Integrates `dev-mode` toggle (`app.security.dev-mode`).
* Registers `JwtAuthenticationFilter` before `UsernamePasswordAuthenticationFilter`.

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.security.dev-mode:false}")
    private boolean devMode;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/api/v1/auth/**").permitAll();
                auth.requestMatchers("/", "/login", "/register", "/verify-2fa", "/forgot-password",
                        "/reset-password", "/reset-2fa", "/2fa/**", "/logout").permitAll();

                if (devMode) {
                    // DEV mode: teammates can develop freely without JWT header blocks
                    auth.anyRequest().permitAll();
                } else {
                    // PROD mode: enforce authentication on protected endpoints
                    auth.anyRequest().authenticated();
                }
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
```

### 2.3 JWT Utility & Verification Filter
* **`JwtUtils`**: Generates signed HMAC-SHA256 tokens embedding user `email` and `role` claims.
* **`JwtAuthenticationFilter`**: Intercepts requests, strips `Bearer `, validates signature and expiration, loads `UserDetails`, and sets `SecurityContextHolder.getContext().setAuthentication(authentication)`.

### 2.4 User & Customer Linkage
In `com.ampta.entity`:
* **`User`**: Handles authentication credentials, password hash, role (`CUSTOMER`, `OFFICER`, `ADMIN`), email verification status, and 2FA secret.
* **`Customer`**: Stores personal details (PAN, Aadhaar, salary, employer, address). Linked via `@OneToOne` with `User`.
* **Extracting Logged-in User**:
  ```java
  // In any controller or service:
  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  String email = auth.getName(); // or ((UserDetailsImpl) auth.getPrincipal()).getUser();
  ```

### 2.5 Dev Mode vs. Production Mode
* **When `app.security.dev-mode=true`**: Teammates can test endpoints directly in Postman or browser without logging in first.
* **When `app.security.dev-mode=false`**: Every request (except `/api/v1/auth/**`) must include `Authorization: Bearer <valid_jwt>`. Method security annotations like `@PreAuthorize("hasRole('OFFICER')")` are strictly checked!

---

# 3. Reference Implementation: Loan Closure Module

The **Loan Closure Module** (covering Foreclosure and Partial Closure) is the official reference implementation for the entire development team.

### 3.1 Business Requirements & State Machine
1. **Foreclosure**: Early settlement of the entire outstanding balance of a loan.
2. **Partial Closure**: Early lump-sum payment of an arbitrary amount reducing principal.
3. **State Machine Transitions**:
   ```
   [PENDING]
      │
      ├── (Officer Approves) ──► [APPROVED]
      │                             │
      │                             └── (Customer/Officer Records Payment) ──► [PAID]
      │                                                                          │
      │                                                                          └── (Officer Completes) ──► [COMPLETED]
      │
      └── (Officer Rejects) ──► [REJECTED]
   ```
4. **Business Rules**:
   * A loan account must exist and have an active status (e.g. `DISBURSED` or `ACTIVE`).
   * An account can have **at most one active settlement request** in flight at a time (`PENDING`, `APPROVED`, or `PAID`).
   * For Foreclosure, `amount` equals `LoanAccount.outstandingPrincipal`.
   * For Partial Closure, `amount` must be greater than zero and less than `LoanAccount.outstandingPrincipal`.
   * On completion of Foreclosure: `LoanAccount.outstandingPrincipal = 0.0` and `LoanAccount.loanStatus = "CLOSED"`.
   * On completion of Partial Closure: `LoanAccount.outstandingPrincipal -= request.amount`.

---

### 3.2 Domain Entities & Enums

#### Settlement Status Enum (`SettlementStatus.java`)
```java
package com.ampta.entity.enums;

public enum SettlementStatus {
    PENDING,
    APPROVED,
    PAID,
    COMPLETED,
    REJECTED
}
```

#### Settlement Type Enum (`SettlementType.java`)
```java
package com.ampta.entity.enums;

public enum SettlementType {
    FORECLOSURE,
    PARTIAL_CLOSURE
}
```

#### Settlement Request Entity (`SettlementRequest.java`)
Create in `com.ampta.entity`:
```java
package com.ampta.entity;

import com.ampta.entity.enums.SettlementStatus;
import com.ampta.entity.enums.SettlementType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "settlement_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_account_id", nullable = false)
    private LoanAccount loanAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SettlementType requestType;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private SettlementStatus status = SettlementStatus.PENDING;

    @Column(length = 500)
    private String rejectionReason;

    @Column(length = 100)
    private String paymentReference;

    private LocalDateTime reviewedAt;
    private LocalDateTime paidAt;
    private LocalDateTime completedAt;
}
```

---

### 3.3 Spring Data Repository

Create in `com.ampta.repository`:
```java
package com.ampta.repository;

import com.ampta.entity.SettlementRequest;
import com.ampta.entity.enums.SettlementStatus;
import com.ampta.entity.enums.SettlementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettlementRequestRepository extends JpaRepository<SettlementRequest, Long> {

    List<SettlementRequest> findByRequestType(SettlementType requestType);

    List<SettlementRequest> findByLoanAccount_LoanAccountIdAndRequestType(Long loanAccountId, SettlementType requestType);

    boolean existsByLoanAccount_LoanAccountIdAndStatusIn(Long loanAccountId, List<SettlementStatus> statuses);
}
```

---

### 3.4 Data Transfer Objects (DTOs)

#### Request DTOs:
```java
package com.ampta.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SettlementCreateRequest {
    @NotNull(message = "Loan account ID is required")
    private Long loanAccountId;

    @Positive(message = "Payment amount must be greater than zero")
    private Double paymentAmount; // Required for partial closure, optional for foreclosure
}
```

```java
package com.ampta.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectSettlementRequest {
    @NotBlank(message = "Rejection reason is required")
    private String reason;
}
```

```java
package com.ampta.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RecordPaymentRequest {
    @NotBlank(message = "Payment reference is required")
    private String paymentReference;
}
```

#### Response DTO (`SettlementResponse.java`):
*Matches both internal conventions and `loan-closure-angular` frontend requirements.*
```java
package com.ampta.dto.response;

import com.ampta.entity.enums.SettlementStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SettlementResponse {
    private Long requestId;
    private Long loanAccountId;
    private Double amount;
    private SettlementStatus status;
    private String rejectionReason;
    private String paymentReference;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime paidAt;
    private LocalDateTime completedAt;
}
```

---

### 3.5 Service Layer (Business Logic)

#### Interface (`com.ampta.service.SettlementService`):
```java
package com.ampta.service;

import com.ampta.dto.request.RecordPaymentRequest;
import com.ampta.dto.request.RejectSettlementRequest;
import com.ampta.dto.request.SettlementCreateRequest;
import com.ampta.dto.response.SettlementResponse;
import com.ampta.entity.enums.SettlementType;

import java.util.List;

public interface SettlementService {
    SettlementResponse createRequest(SettlementCreateRequest request, SettlementType type);
    List<SettlementResponse> getAllRequests(SettlementType type);
    List<SettlementResponse> getRequestsByLoanAccountId(Long loanAccountId, SettlementType type);
    SettlementResponse getRequestById(Long requestId);
    SettlementResponse approveRequest(Long requestId);
    SettlementResponse rejectRequest(Long requestId, RejectSettlementRequest request);
    SettlementResponse recordPayment(Long requestId, RecordPaymentRequest request);
    SettlementResponse completeRequest(Long requestId);
}
```

#### Implementation (`com.ampta.service.impl.SettlementServiceImpl`):
```java
package com.ampta.service.impl;

import com.ampta.dto.request.RecordPaymentRequest;
import com.ampta.dto.request.RejectSettlementRequest;
import com.ampta.dto.request.SettlementCreateRequest;
import com.ampta.dto.response.SettlementResponse;
import com.ampta.entity.LoanAccount;
import com.ampta.entity.SettlementRequest;
import com.ampta.entity.enums.SettlementStatus;
import com.ampta.entity.enums.SettlementType;
import com.ampta.exception.InvalidLoanStatusException;
import com.ampta.exception.ResourceAlreadyExistsException;
import com.ampta.exception.ResourceNotFoundException;
import com.ampta.repository.LoanAccountRepository;
import com.ampta.repository.SettlementRequestRepository;
import com.ampta.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SettlementServiceImpl implements SettlementService {

    private final SettlementRequestRepository settlementRepo;
    private final LoanAccountRepository loanAccountRepo;

    @Override
    public SettlementResponse createRequest(SettlementCreateRequest request, SettlementType type) {
        log.info("Creating {} request for loan account: {}", type, request.getLoanAccountId());

        LoanAccount loanAccount = loanAccountRepo.findById(request.getLoanAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan account not found with ID: " + request.getLoanAccountId()));

        // Active request check
        boolean hasActive = settlementRepo.existsByLoanAccount_LoanAccountIdAndStatusIn(
                loanAccount.getLoanAccountId(),
                List.of(SettlementStatus.PENDING, SettlementStatus.APPROVED, SettlementStatus.PAID)
        );
        if (hasActive) {
            throw new ResourceAlreadyExistsException("An active settlement request already exists for this loan account.");
        }

        double settlementAmount;
        if (type == SettlementType.FORECLOSURE) {
            settlementAmount = loanAccount.getOutstandingPrincipal() != null ? loanAccount.getOutstandingPrincipal() : 0.0;
            if (settlementAmount <= 0) {
                throw new InvalidLoanStatusException("Loan has no outstanding principal to foreclose.");
            }
        } else {
            if (request.getPaymentAmount() == null || request.getPaymentAmount() <= 0) {
                throw new InvalidLoanStatusException("Payment amount must be greater than zero for partial closure.");
            }
            if (request.getPaymentAmount() >= loanAccount.getOutstandingPrincipal()) {
                throw new InvalidLoanStatusException("Partial closure amount must be less than total outstanding principal. Use Foreclosure instead.");
            }
            settlementAmount = request.getPaymentAmount();
        }

        SettlementRequest entity = SettlementRequest.builder()
                .loanAccount(loanAccount)
                .requestType(type)
                .amount(settlementAmount)
                .status(SettlementStatus.PENDING)
                .build();

        SettlementRequest saved = settlementRepo.save(entity);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SettlementResponse> getAllRequests(SettlementType type) {
        return settlementRepo.findByRequestType(type).stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SettlementResponse> getRequestsByLoanAccountId(Long loanAccountId, SettlementType type) {
        return settlementRepo.findByLoanAccount_LoanAccountIdAndRequestType(loanAccountId, type)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SettlementResponse getRequestById(Long requestId) {
        SettlementRequest entity = settlementRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Settlement request not found with ID: " + requestId));
        return mapToResponse(entity);
    }

    @Override
    public SettlementResponse approveRequest(Long requestId) {
        SettlementRequest entity = findAndValidateStatus(requestId, SettlementStatus.PENDING);
        entity.setStatus(SettlementStatus.APPROVED);
        entity.setReviewedAt(LocalDateTime.now());
        return mapToResponse(settlementRepo.save(entity));
    }

    @Override
    public SettlementResponse rejectRequest(Long requestId, RejectSettlementRequest request) {
        SettlementRequest entity = findAndValidateStatus(requestId, SettlementStatus.PENDING);
        entity.setStatus(SettlementStatus.REJECTED);
        entity.setRejectionReason(request.getReason());
        entity.setReviewedAt(LocalDateTime.now());
        return mapToResponse(settlementRepo.save(entity));
    }

    @Override
    public SettlementResponse recordPayment(Long requestId, RecordPaymentRequest request) {
        SettlementRequest entity = findAndValidateStatus(requestId, SettlementStatus.APPROVED);
        entity.setStatus(SettlementStatus.PAID);
        entity.setPaymentReference(request.getPaymentReference());
        entity.setPaidAt(LocalDateTime.now());
        return mapToResponse(settlementRepo.save(entity));
    }

    @Override
    public SettlementResponse completeRequest(Long requestId) {
        SettlementRequest entity = findAndValidateStatus(requestId, SettlementStatus.PAID);
        entity.setStatus(SettlementStatus.COMPLETED);
        entity.setCompletedAt(LocalDateTime.now());

        LoanAccount loanAccount = entity.getLoanAccount();
        if (entity.getRequestType() == SettlementType.FORECLOSURE) {
            loanAccount.setOutstandingPrincipal(0.0);
            loanAccount.setLoanStatus("CLOSED");
        } else {
            double currentOutstanding = loanAccount.getOutstandingPrincipal() != null ? loanAccount.getOutstandingPrincipal() : 0.0;
            loanAccount.setOutstandingPrincipal(Math.max(0.0, currentOutstanding - entity.getAmount()));
        }
        loanAccountRepo.save(loanAccount);

        return mapToResponse(settlementRepo.save(entity));
    }

    private SettlementRequest findAndValidateStatus(Long requestId, SettlementStatus requiredStatus) {
        SettlementRequest entity = settlementRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Settlement request not found with ID: " + requestId));
        if (entity.getStatus() != requiredStatus) {
            throw new InvalidLoanStatusException(
                    "Invalid state transition. Action requires status: " + requiredStatus + ", but current status is: " + entity.getStatus());
        }
        return entity;
    }

    private SettlementResponse mapToResponse(SettlementRequest entity) {
        LocalDateTime createdDate = entity.getCreatedAt() != null
                ? LocalDateTime.ofInstant(entity.getCreatedAt(), ZoneId.systemDefault())
                : LocalDateTime.now();

        return SettlementResponse.builder()
                .requestId(entity.getRequestId())
                .loanAccountId(entity.getLoanAccount().getLoanAccountId())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .rejectionReason(entity.getRejectionReason())
                .paymentReference(entity.getPaymentReference())
                .createdAt(createdDate)
                .reviewedAt(entity.getReviewedAt())
                .paidAt(entity.getPaidAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }
}
```

---

### 3.6 REST Controller & API Contracts

#### ForeclosureController (`Endpoints.V1_FORECLOSURE` = `/api/v1/foreclosures`)
```java
package com.ampta.controller;

import com.ampta.dto.request.RecordPaymentRequest;
import com.ampta.dto.request.RejectSettlementRequest;
import com.ampta.dto.request.SettlementCreateRequest;
import com.ampta.dto.response.SettlementResponse;
import com.ampta.entity.enums.SettlementType;
import com.ampta.service.SettlementService;
import com.ampta.utils.Endpoints;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoints.V1_FORECLOSURE)
@RequiredArgsConstructor
@Slf4j
public class ForeclosureController {

    private final SettlementService settlementService;

    @PostMapping
    public ResponseEntity<SettlementResponse> createForeclosure(@Valid @RequestBody SettlementCreateRequest request) {
        return new ResponseEntity<>(settlementService.createRequest(request, SettlementType.FORECLOSURE), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SettlementResponse>> getAllForeclosures() {
        return ResponseEntity.ok(settlementService.getAllRequests(SettlementType.FORECLOSURE));
    }

    @GetMapping("/loan-account/{loanAccountId}")
    public ResponseEntity<List<SettlementResponse>> getByLoanAccount(@PathVariable Long loanAccountId) {
        return ResponseEntity.ok(settlementService.getRequestsByLoanAccountId(loanAccountId, SettlementType.FORECLOSURE));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<SettlementResponse> getById(@PathVariable Long requestId) {
        return ResponseEntity.ok(settlementService.getRequestById(requestId));
    }

    @PatchMapping("/{requestId}/approve")
    public ResponseEntity<SettlementResponse> approve(@PathVariable Long requestId) {
        return ResponseEntity.ok(settlementService.approveRequest(requestId));
    }

    @PatchMapping("/{requestId}/reject")
    public ResponseEntity<SettlementResponse> reject(@PathVariable Long requestId, @Valid @RequestBody RejectSettlementRequest request) {
        return ResponseEntity.ok(settlementService.rejectRequest(requestId, request));
    }

    @PatchMapping("/{requestId}/pay")
    public ResponseEntity<SettlementResponse> recordPayment(@PathVariable Long requestId, @Valid @RequestBody RecordPaymentRequest request) {
        return ResponseEntity.ok(settlementService.recordPayment(requestId, request));
    }

    @PatchMapping("/{requestId}/close")
    public ResponseEntity<SettlementResponse> complete(@PathVariable Long requestId) {
        return ResponseEntity.ok(settlementService.completeRequest(requestId));
    }
}
```

#### ClosureController (`Endpoints.V1_CLOSURE` = `/api/v1/closures`)
```java
package com.ampta.controller;

import com.ampta.dto.request.RecordPaymentRequest;
import com.ampta.dto.request.RejectSettlementRequest;
import com.ampta.dto.request.SettlementCreateRequest;
import com.ampta.dto.response.SettlementResponse;
import com.ampta.entity.enums.SettlementType;
import com.ampta.service.SettlementService;
import com.ampta.utils.Endpoints;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoints.V1_CLOSURE)
@RequiredArgsConstructor
@Slf4j
public class ClosureController {

    private final SettlementService settlementService;

    @PostMapping
    public ResponseEntity<SettlementResponse> createPartialClosure(@Valid @RequestBody SettlementCreateRequest request) {
        return new ResponseEntity<>(settlementService.createRequest(request, SettlementType.PARTIAL_CLOSURE), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SettlementResponse>> getAllClosures() {
        return ResponseEntity.ok(settlementService.getAllRequests(SettlementType.PARTIAL_CLOSURE));
    }

    @GetMapping("/loan-account/{loanAccountId}")
    public ResponseEntity<List<SettlementResponse>> getByLoanAccount(@PathVariable Long loanAccountId) {
        return ResponseEntity.ok(settlementService.getRequestsByLoanAccountId(loanAccountId, SettlementType.PARTIAL_CLOSURE));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<SettlementResponse> getById(@PathVariable Long requestId) {
        return ResponseEntity.ok(settlementService.getRequestById(requestId));
    }

    @PatchMapping("/{requestId}/approve")
    public ResponseEntity<SettlementResponse> approve(@PathVariable Long requestId) {
        return ResponseEntity.ok(settlementService.approveRequest(requestId));
    }

    @PatchMapping("/{requestId}/reject")
    public ResponseEntity<SettlementResponse> reject(@PathVariable Long requestId, @Valid @RequestBody RejectSettlementRequest request) {
        return ResponseEntity.ok(settlementService.rejectRequest(requestId, request));
    }

    @PatchMapping("/{requestId}/pay")
    public ResponseEntity<SettlementResponse> recordPayment(@PathVariable Long requestId, @Valid @RequestBody RecordPaymentRequest request) {
        return ResponseEntity.ok(settlementService.recordPayment(requestId, request));
    }

    @PatchMapping("/{requestId}/complete")
    public ResponseEntity<SettlementResponse> complete(@PathVariable Long requestId) {
        return ResponseEntity.ok(settlementService.completeRequest(requestId));
    }
}
```

### 3.7 Frontend Alignment (`loan-closure-angular`)
* The `loan-closure-angular` frontend connects directly to these endpoints via its dev proxy (`proxy.conf.json`: `/api/**` -> `http://localhost:8080`).
* Notice that `ForeclosureController` completion is `/close` and `ClosureController` completion is `/complete`—this precisely matches `loan-closure-angular/src/app/shared/services/settlement-api.ts`!

---

# 4. Developer Guide: How Teammates Must Build Their Modules

When handing off the project to the team, share this section with each team member.

### 4.1 Module Ownership Matrix

| Developer | Assigned Modules | Central Endpoint Constants (`Endpoints.java`) | Key Entities |
| :--- | :--- | :--- | :--- |
| **FARAZ** *(Lead)* | Foreclosure, Partial Closure | `Endpoints.V1_FORECLOSURE`<br>`Endpoints.V1_CLOSURE` | `SettlementRequest`, `LoanAccount` |
| **MANISH** | Loan Creation, Sanction Letter, Disbursement | `Endpoints.V1_LOAN`<br>`Endpoints.V1_SANCTION_LETTER`<br>`Endpoints.V1_DISBURSEMENT` | `LoanDeal`, `DealReview`, `LoanAccount` |
| **PRINCE** | Wallets, Payments, Bounce, Penalty, EMI Scheduler | `Endpoints.V1_WALLET`<br>`Endpoints.V1_PAYMENT`<br>`Endpoints.V1_BOUNCE`<br>`Endpoints.V1_PENALTY`<br>`Endpoints.V1_SCHEDULER` | `LoanPayment`, `EmiSchedule`, `PenaltyCharge`, `Wallet` |
| **ANUPAMA** | KYC Documents, Document Management, CIBIL | `Endpoints.V1_KYC`<br>`Endpoints.V1_DOCUMENT`<br>`Endpoints.V1_CIBIL` | `KycDocument`, `CibilReport`, `Customer` |
| **SHIVAM** | Auth, Score Card, Eligibility, Notification, Ticket | `Endpoints.V1_AUTH`<br>`Endpoints.V1_SCORE_CARD`<br>`Endpoints.V1_ELIGIBILITY`<br>`Endpoints.V1_NOTIFICATION`<br>`Endpoints.V1_TICKET` | `User`, `Customer`, `ScoreCard`, `EligibilityResult`, `Notification` |

---

### 4.2 Folder Responsibilities: Where Does Your Code Go?

When implementing your assigned module, follow this strict separation of concerns:

| Directory | What to Put Here | Example File | Rules |
| :--- | :--- | :--- | :--- |
| `com.ampta.entity` | JPA Entities mapped to MySQL tables | `LoanDeal.java` | Extends `BaseEntity`. Use `@Table(name = "snake_case")`. No DTO logic. |
| `com.ampta.entity.enums` | Status, Types, Roles | `LoanStatus.java` | Standard UPPERCASE enums. |
| `com.ampta.repository` | Spring Data Repositories | `LoanDealRepository.java` | Extends `JpaRepository<Entity, ID>`. Use Spring Data method naming. |
| `com.ampta.dto.request` | Incoming request bodies | `LoanCreateRequest.java` | Annotate with Bean Validation (`@NotNull`, `@NotBlank`, `@Positive`). |
| `com.ampta.dto.response` | Outgoing data structures | `LoanResponse.java` | Clean POJOs with `@Data` or `@Builder`. Do NOT expose entity passwords or internal JPA proxies. |
| `com.ampta.service` | Business logic interfaces | `LoanService.java` | Clean Java interface with domain methods. |
| `com.ampta.service.impl` | Service implementation | `LoanServiceImpl.java` | Annotate with `@Service`, `@Transactional`, `@Slf4j`, `@RequiredArgsConstructor`. Inject repos. |
| `com.ampta.controller` | REST API endpoints | `LoanDealController.java` | Annotate with `@RestController`. Use constants from `Endpoints.java`. Validate with `@Valid`. |
| `com.ampta.exception` | Module-specific exceptions | `LoanNotFoundException.java` | Extend `RuntimeException`. Register in `GlobalExceptionHandler.java`. |
| `com.ampta.utils` | Constants, shared utilities | `Endpoints.java` | Centralized paths. |

---

### 4.3 The 7-Step Recipe for Implementing Any Feature

Every developer must follow these exact 7 steps to build their module:

```
Step 1: Entity & Enums ──► Step 2: Repository ──► Step 3: DTOs ──► Step 4: Custom Exceptions
                                                                           │
Step 7: Testing & Verify ◄── Step 6: Controller ◄── Step 5: Service Layer ◄┘
```

#### Step 1: Create Entity in `entity/`
* Extend `BaseEntity` to get `createdAt` and `updatedAt` automatically.
* Map foreign keys using `@ManyToOne(fetch = FetchType.LAZY)`.
* Use Lombok `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`.

#### Step 2: Create Repository in `repository/`
* Extend `JpaRepository<YourEntity, Long>`.
* Add custom query methods if filtering by status, customerId, or accountId.

#### Step 3: Create DTOs in `dto/request/` and `dto/response/`
* Never expose JPA entities directly in `@RequestBody` or `@ResponseBody` (avoids circular references, lazy loading serialization bugs, and leaking secrets).
* Add Jakarta validation annotations (`@NotBlank`, `@NotNull`, `@Min`, `@Max`).

#### Step 4: Create Custom Exceptions in `exception/`
* Create descriptive exceptions:
  ```java
  public class SanctionLetterNotFoundException extends RuntimeException {
      public SanctionLetterNotFoundException(String message) { super(message); }
  }
  ```
* Add handler in `GlobalExceptionHandler.java` mapping it to appropriate HTTP status (`404 NOT_FOUND`, `400 BAD_REQUEST`, etc.).

#### Step 5: Create Service Interface & Implementation
* Write interface in `service/` and implementation in `service/impl/`.
* Put all business rules, validations, calculations, and state changes here.
* Annotate class with `@Transactional`. For read-only methods, use `@Transactional(readOnly = true)`.
* Use `@Slf4j` for structured audit logging:
  ```java
  log.info("Processing disbursement for dealId: {}, amount: {}", dealId, amount);
  ```

#### Step 6: Create REST Controller in `controller/`
* Use base path constant: `@RequestMapping(Endpoints.V1_YOUR_MODULE)`.
* Use proper HTTP verbs (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`).
* For responses that require status messaging, wrap with `ApiResponse<T>`:
  ```java
  return new ResponseEntity<>(new ApiResponse<>(true, "Operation successful", data), HttpStatus.CREATED);
  ```
* Add role guards where appropriate:
  ```java
  @PreAuthorize("hasRole('OFFICER')")
  ```

#### Step 7: Test Your API
* Test all positive paths and negative paths (invalid IDs, unauthorized access, validation errors).
* Verify database changes in MySQL Workbench or cloud database console.

---

### 4.4 Coding Standards & Mandatory Rules

1. **Always Use Plural Nouns for Endpoints**:
   * ✅ `/api/v1/loans`, `/api/v1/customers`, `/api/v1/cibils`
   * ❌ `/api/v1/loan`, `/api/v1/customer`, `/api/v1/cibil`

2. **Always Use kebab-case for Multi-Word Paths**:
   * ✅ `/api/v1/loan-accounts`, `/api/v1/sanction-letters`, `/api/v1/score-cards`
   * ❌ `/api/v1/loanAccounts`, `/api/v1/loan_accounts`

3. **No Verbs in Endpoint URLs**:
   * ✅ `POST /api/v1/cibils/customers/10`
   * ❌ `POST /api/v1/cibil/customer/10/check-score`

4. **Reference Centralized Constants**:
   * Never hardcode path strings like `@RequestMapping("/api/v1/loans")`.
   * Reference `Endpoints.V1_LOAN` from `com.ampta.utils.Endpoints`.

5. **Do Not Trust User ID from Request URL**:
   * Avoid `POST /api/v1/loans/{userId}` where anyone could create a loan on behalf of another user!
   * Extract the authenticated user from the Security context:
     ```java
     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     ```

6. **Standardize Responses**:
   * All responses return clean DTOs or standard `ApiResponse<T>`.

---

### 4.5 Git Workflow & Pull Request Checklist

To prevent merge conflicts and broken builds:

#### 1. Branch Naming Convention
* Format: `feature/<module-name>-<developer-name>`
* Examples:
  * `feature/sanction-letter-manish`
  * `feature/wallet-prince`
  * `feature/kyc-review-anupama`
  * `feature/ticket-system-shivam`

#### 2. Commit Message Standards
Use clear semantic messages:
* `feat(kyc): add document upload and validation service`
* `fix(loan): correct loan account status transition to CLOSED`
* `refactor(auth): move token rotation logic to AuthService`

#### 3. PR Submission Checklist
Before requesting review on GitHub:
- [ ] Code compiles without errors: `./mvnw clean compile`.
- [ ] All package imports match `com.ampta.*`.
- [ ] Endpoints use `Endpoints.java` constants and follow REST plural naming.
- [ ] No hardcoded passwords, personal tokens, or API keys committed.
- [ ] DTO validations are applied on all `@RequestBody` parameters (`@Valid`).
- [ ] Service methods have logging with `@Slf4j`.
