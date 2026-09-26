# Spring Security & Microservices Master Interview Guide
> **Comprehensive Enterprise Deep-Dive with Security Filter Chains, Stateless JWT Authentication, Resilience4j Circuit Breakers, Distributed Tracing, Saga Patterns & Production Microservice Architecture Scenarios**

---

# Table of Contents
1. [Module 4: Spring Security, Auth & RBAC (Q101 – Q116)](#module-4-spring-security-auth--rbac)
   - [Q101. What is Spring Security and why is it used?](#q101-what-is-spring-security-and-why-is-it-used)
   - [Q102. Explain how authentication and authorization work in Spring Security.](#q102-explain-how-authentication-and-authorization-work-in-spring-security)
   - [Q103. What is the difference between AuthenticationManager and UserDetailsService?](#q103-what-is-the-difference-between-authenticationmanager-and-userdetailsservice)
   - [Q104. What is a SecurityContext and SecurityContextHolder?](#q104-what-is-a-securitycontext-and-securitycontextholder)
   - [Q105. What is the purpose of PasswordEncoder in Spring Security?](#q105-what-is-the-purpose-of-passwordencoder-in-spring-security)
   - [Q106. How do you configure role-based access control in Spring Security?](#q106-how-do-you-configure-role-based-access-control-in-spring-security)
   - [Q107. What are filters in Spring Security and how are they used?](#q107-what-are-filters-in-spring-security-and-how-are-they-used)
   - [Q108. How do you secure REST APIs using JWT in Spring Security?](#q108-how-do-you-secure-rest-apis-using-jwt-in-spring-security)
   - [Q109. What are the common annotations used in Spring Security?](#q109-what-are-the-common-annotations-used-in-spring-security)
   - [Q110. How do you configure custom login and logout in Spring Security?](#q110-how-do-you-configure-custom-login-and-logout-in-spring-security)
   - [Q111. What is CSRF and how is it handled in Spring Security?](#q111-what-is-csrf-and-how-is-it-handled-in-spring-security)
   - [Q112. How do you implement method-level security in Spring?](#q112-how-do-you-implement-method-level-security-in-spring)
   - [Q113. What is the use of @EnableWebSecurity annotation?](#q113-what-is-the-use-of-enablewebsecurity-annotation)
   - [Q114. How do you integrate OAuth2 in Spring Boot application?](#q114-how-do-you-integrate-oauth2-in-spring-boot-application)
   - [Q115. How do you handle stateless authentication in Spring Security?](#q115-how-do-you-handle-stateless-authentication-in-spring-security)
   - [Q116. What are common ways to secure REST endpoints?](#q116-what-are-common-ways-to-secure-rest-endpoints)
2. [Module 5: Microservices Real-World Scenarios & Architecture (Q117 – Q146)](#module-5-microservices-real-world-scenarios--architecture)
   - [Q117. How to structure an Employee Management Microservice (Employee, Department, Project)?](#q117-how-to-structure-an-employee-management-microservice-employee-department-project)
   - [Q118. Microservice communication: REST vs OpenFeign vs Message Brokers?](#q118-microservice-communication-rest-vs-openfeign-vs-message-brokers)
   - [Q119. Multiple microservices accessing different databases: How to maintain data consistency?](#q119-multiple-microservices-accessing-different-databases-how-to-maintain-data-consistency)
   - [Q120. Handling aggregated responses from multiple microservices in a single API call?](#q120-handling-aggregated-responses-from-multiple-microservices-in-a-single-api-call)
   - [Q121. Securing internal downstream microservices using JWT tokens?](#q121-securing-internal-downstream-microservices-using-jwt-tokens)
   - [Q122. User Service is down but Order Service depends on it: How to build resilience?](#q122-user-service-is-down-but-order-service-depends-on-it-how-to-build-resilience)
   - [Q123. Centralized configuration across microservices (Spring Cloud Config Server)?](#q123-centralized-configuration-across-microservices-spring-cloud-config-server)
   - [Q124. Production failure detection and auto-recovery in microservices?](#q124-production-failure-detection-and-auto-recovery-in-microservices)
   - [Q125. Service Discovery & Client-Side Load Balancing (Eureka / Spring Cloud Gateway)?](#q125-service-discovery--client-side-load-balancing-eureka--spring-cloud-gateway)
   - [Q126. Designing microservices for Zero Downtime Deployment (Blue-Green / Rolling)?](#q126-designing-microservices-for-zero-downtime-deployment-blue-green--rolling)
   - [Q127. Designing Employee-Department entity relationship using JPA?](#q127-designing-employee-department-entity-relationship-using-jpa)
   - [Q128. Resolving Hibernate N+1 select problem in enterprise services?](#q128-resolving-hibernate-n1-select-problem-in-enterprise-services)
   - [Q129. Handling large data fetching (10,000+ records) without memory spikes?](#q129-handling-large-data-fetching-10000-records-without-memory-spikes)
   - [Q130. Implementing Soft Deletes in JPA entities?](#q130-implementing-soft-deletes-in-jpa-entities)
   - [Q131. Dynamic filtering across multiple tables using JPA Criteria API & Specification?](#q131-dynamic-filtering-across-multiple-tables-using-jpa-criteria-api--specification)
   - [Q132. Handling LazyInitializationException when returning entities via REST?](#q132-handling-lazyinitializationexception-when-returning-entities-via-rest)
   - [Q133. Designing efficient queries for Order -> @OneToMany OrderItem?](#q133-designing-efficient-queries-for-order---onetomany-orderitem)
   - [Q134. Handling distributed transaction rollback across multiple database operations?](#q134-handling-distributed-transaction-rollback-across-multiple-database-operations)
   - [Q135. High-throughput Batch Inserts and Updates in JPA?](#q135-high-throughput-batch-inserts-and-updates-in-jpa)
   - [Q136. Database Schema Migration between environments (Flyway / Liquibase)?](#q136-database-schema-migration-between-environments-flyway--liquibase)
   - [Q137. Implementing synchronous service-to-service calls using OpenFeign?](#q137-implementing-synchronous-service-to-service-calls-using-openfeign)
   - [Q138. Feign Client failure resilience using Resilience4j Circuit Breakers?](#q138-feign-client-failure-resilience-using-resilience4j-circuit-breakers)
   - [Q139. Propagating JWT Bearer tokens between microservices via Feign RequestInterceptor?](#q139-propagating-jwt-bearer-tokens-between-microservices-via-feign-requestinterceptor)
   - [Q140. API Gateway validating JWT once and passing headers to downstream services?](#q140-api-gateway-validating-jwt-once-and-passing-headers-to-downstream-services)
   - [Q141. Managing Cross-Origin Resource Sharing (CORS) in Spring Boot Microservices?](#q141-managing-cross-origin-resource-sharing-cors-in-spring-boot-microservices)
   - [Q142. Maintaining consistency when multiple microservices access a shared database?](#q142-maintaining-consistency-when-multiple-microservices-access-a-shared-database)
   - [Q143. Ensuring Idempotency for critical API requests (Payments / Bookings)?](#q143-ensuring-idempotency-for-critical-api-requests-payments--bookings)
   - [Q144. Centralized Global Exception Handling across all microservices?](#q144-centralized-global-exception-handling-across-all-microservices)
   - [Q145. Centralized distributed logging with ELK Stack and Correlation IDs?](#q145-centralized-distributed-logging-with-elk-stack-and-correlation-ids)
   - [Q146. Monitoring microservice health and metrics in production (Prometheus + Grafana)?](#q146-monitoring-microservice-health-and-metrics-in-production-prometheus--grafana)

---

# Module 4: Spring Security, Auth & RBAC

---

### Q101. What is Spring Security and why is it used?

#### 1. Concept & Interview Answer
- **Spring Security** is a powerful, highly customizable authentication and access-control framework that is the de-facto standard for securing Spring-based enterprise applications.
- It operates as a series of **Servlet Filters** (`SecurityFilterChain`) executing ahead of the Spring MVC `DispatcherServlet`.
- **Core Capabilities:**
  1. **Authentication:** Verifies user identity (Username/Password, JWT, OAuth2/OIDC, SAML, LDAP, Biometrics).
  2. **Authorization (RBAC & ABAC):** Enforces fine-grained URL and method-level access permissions.
  3. **Protection Against Common Attacks:** Built-in defenses against **CSRF**, **CORS**, **Session Fixation**, **Clickjacking (FrameOptions)**, and **XSS**.
  4. **Cryptographic Security:** Standardized password hashing (`BCrypt`, `Argon2`, `PBKDF2`).

#### 2. Architecture: SecurityFilterChain Pipeline
```
[ Incoming HTTP Request ]
           │
           ▼
[ DelegatingFilterProxy ] ──► (Bridge from Servlet Container to Spring Context)
           │
           ▼
[ FilterChainProxy (SecurityFilterChain) ]
   ├── 1. CorsFilter
   ├── 2. CsrfFilter
   ├── 3. HeaderWriterFilter (Sets HSTS, X-Frame-Options, CSP)
   ├── 4. LogoutFilter
   ├── 5. JwtAuthenticationFilter / UsernamePasswordAuthenticationFilter
   ├── 6. SecurityContextHolderAwareRequestFilter
   ├── 7. AnonymousAuthenticationFilter
   ├── 8. ExceptionTranslationFilter (Catches Auth Exceptions -> 401 / 403)
   └── 9. AuthorizationFilter (Enforces URL path permissions)
           │
           ▼ (If Authorized)
[ DispatcherServlet ] ──► [ @RestController ]
```

#### 3. Production Code Example (Spring Security 6.x / Spring Boot 3.x)
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ModernSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Disabled for stateless JWT REST APIs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/actuator/health").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .build();
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An un-secured internal microservice was compromised when an attacker performed a Clickjacking attack by rendering the banking admin dashboard inside a transparent malicious iframe. Integrating Spring Security automatically injected `X-Frame-Options: DENY` and `Content-Security-Policy: frame-ancestors 'none'` headers, rendering Clickjacking framing impossible.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** define explicit `SecurityFilterChain` beans using lambda DSL syntax (`http.authorizeHttpRequests(...)`) introduced in Spring Security 6.
- ❌ **DON'T** extend deprecated `WebSecurityConfigurerAdapter` (removed in Spring Security 6 / Spring Boot 3).

---

### Q102. Explain how authentication and authorization work in Spring Security.

#### 1. Concept & Interview Answer
- **Authentication ("Who are you?"):**
  - The process of verifying a principal's identity (e.g., verifying a username and password or validating a cryptographic JWT signature).
  - Coordinated by `AuthenticationManager`, which delegates to one or more `AuthenticationProvider` implementations.
  - Produces an authenticated **`Authentication`** object stored in the `SecurityContextHolder`.
- **Authorization ("What are you allowed to do?"):**
  - The process of determining whether an already authenticated user has permission to access a specific resource or execute a method.
  - Evaluated by `AuthorizationFilter` (for HTTP URLs) or `MethodSecurityInterceptor` (for `@PreAuthorize`), comparing user's `GrantedAuthority` roles (`ROLE_ADMIN`, `ROLE_MANAGER`) against the required security constraints.

#### 2. Authentication Lifecycle Flow Diagram
```
1. Client POST /login {username, password}
          │
          ▼
2. UsernamePasswordAuthenticationFilter creates unauthenticated UsernamePasswordAuthenticationToken(user, pass)
          │
          ▼
3. AuthenticationManager.authenticate(token)
          │
          ▼
4. DaoAuthenticationProvider
   ├── Calls UserDetailsService.loadUserByUsername(user) ──► Loads UserDetails from DB
   └── Calls PasswordEncoder.matches(rawPass, dbHash)     ──► Compares BCrypt Hash
          │
          ▼ (If matches)
5. Returns fully authenticated Authentication object (containing Principal + GrantedAuthorities)
          │
          ▼
6. SecurityContextHolder.getContext().setAuthentication(auth);
```

#### 3. Production Code Example (Custom Authentication Provider)
```java
package com.enterprise.security.auth;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EnterpriseAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public EnterpriseAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!user.isEnabled() || !user.isAccountNonLocked()) {
            throw new LockedException("User account is locked or disabled");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A legacy application returned a generic HTTP 200 OK with `{"error": "bad credentials"}` on failed logins, allowing credential-stuffing bots to bypass security monitoring. Standardizing on Spring Security's `AuthenticationManager` threw `BadCredentialsException`, allowing the application to return RFC-compliant `401 Unauthorized` and increment rate-limiting IP failure counters in Redis.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** separate Authentication (identity verification) from Authorization (permission checking) in your architectural design.
- ❌ **DON'T** return detailed login failure messages like "User does not exist" vs "Password incorrect"; always return generic "Invalid credentials" to prevent account enumeration attacks.

---

### Q103. What is the difference between AuthenticationManager and UserDetailsService?

#### 1. Concept & Interview Answer
- **`AuthenticationManager`**:
  - The master interface responsible for **processing an authentication request**.
  - Standard implementation is `ProviderManager`, which iterates through a list of `AuthenticationProvider`s.
  - Takes an unauthenticated `Authentication` object, performs credential validation, and returns a fully populated, authenticated `Authentication` object (or throws `AuthenticationException`).
- **`UserDetailsService`**:
  - A core **data access interface** with a single method: `UserDetails loadUserByUsername(String username) throws UsernameNotFoundException`.
  - It has **no authentication logic**; its sole job is to fetch user records (username, password hash, roles/authorities) from the database or identity store.

#### 2. Collaboration Architecture Diagram
```
[ AuthenticationManager (ProviderManager) ]
                     │
                     ▼
        [ DaoAuthenticationProvider ]
         │                        │
         │ (1) Fetches UserData   │ (2) Validates Hash
         ▼                        ▼
[ UserDetailsService ]   [ PasswordEncoder ]
         │
         ▼
[ PostgreSQL Database ]
```

#### 3. Production Code Example (Custom UserDetailsService with JPA)
```java
package com.enterprise.security.service;

import com.enterprise.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.enterprise.domain.entity.User appUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        var authorities = appUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());

        return new User(
                appUser.getEmail(),
                appUser.getPasswordHash(),
                appUser.isActive(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                !appUser.isLocked(),
                authorities
        );
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In a high-traffic authentication service, user roles were mapped with `FetchType.LAZY`. Calling `loadUserByUsername()` outside a transaction caused `LazyInitializationException` when loading user authorities. Wrapping `loadUserByUsername()` with `@Transactional(readOnly = true)` or using `JOIN FETCH` on roles resolved the exception and ensured complete `UserDetails` creation.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** implement `UserDetailsService` to decouple your JPA domain entities from Spring Security's `UserDetails` contract.
- ✅ **DO** prefix role authorities with `ROLE_` (e.g., `ROLE_ADMIN`) when populating `SimpleGrantedAuthority` so that `.hasRole("ADMIN")` resolves seamlessly.

---

### Q104. What is a SecurityContext and SecurityContextHolder?

#### 1. Concept & Interview Answer
- **`SecurityContext`**: An interface that holds the current **`Authentication`** object containing details of the currently authenticated principal (user identity, credentials, and granted authorities).
- **`SecurityContextHolder`**: A static helper class providing access to the current `SecurityContext`.
- **Storage Strategies (ThreadLocal):**
  - `MODE_THREADLOCAL` (Default): Stores context in a `ThreadLocal` variable, making the authenticated user accessible anywhere on the current request thread.
  - `MODE_INHERITABLETHREADLOCAL`: Inherits security context to child threads spawned by the request thread.
  - `MODE_GLOBAL`: Static context shared across all threads (used in standalone desktop apps).

#### 2. ThreadLocal Security Context Model
```
HTTP Request Thread-1 ──► SecurityContextHolder (ThreadLocal-1) ──► SecurityContext ──► Authentication (User: Alice)
HTTP Request Thread-2 ──► SecurityContextHolder (ThreadLocal-2) ──► SecurityContext ──► Authentication (User: Bob)
```

#### 3. Production Code Example (Extracting Current User Context)
```java
package com.enterprise.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    public static Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        return Optional.of(authentication.getName());
    }

    public static boolean hasRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;

        String expectedRole = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
        return authentication.getAuthorities().stream()
                .anyMatch(granted -> granted.getAuthority().equals(expectedRole));
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An asynchronous audit method annotated with `@Async` called `SecurityContextHolder.getContext().getAuthentication()`, which returned `null` because the async task executed on a new thread from the worker pool. Setting `SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)` or configuring a `DelegatingSecurityContextAsyncTaskExecutor` propagated the authenticated user context to background threads.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `DelegatingSecurityContextExecutorService` when passing security context across thread pool boundaries.
- ✅ **DO** always verify `authentication.isAuthenticated()` and check for `"anonymousUser"` before casting `getPrincipal()`.

---

### Q105. What is the purpose of PasswordEncoder in Spring Security?

#### 1. Concept & Interview Answer
- **`PasswordEncoder`** (`org.springframework.security.crypto.password.PasswordEncoder`) is the core cryptographic interface used for securely hashing and verifying passwords.
- **Why it is mandatory:** Passwords must **never** be stored in plain text or using fast/broken hash functions (MD5, SHA-1, SHA-256).
- **Core Interface Methods:**
  - `String encode(CharSequence rawPassword)`: Generates a cryptographically salted one-way hash.
  - `boolean matches(CharSequence rawPassword, String encodedPassword)`: Verifies whether raw password matches the stored hash (extracts salt from the hash itself).
  - `boolean upgradeEncoding(String encodedPassword)`: Checks if the hash was created with an outdated work factor and needs rehashing.
- **Standard Implementation:** **`BCryptPasswordEncoder`** (adaptive slow hashing algorithm resistant to GPU brute-force attacks).

#### 2. BCrypt Salt & Hash Structure
```
$2a$12$e8kZ1q6W5QJ... (60 Characters)
├── $2a$  ──► BCrypt Algorithm Identifier
├── 12$   ──► Work Factor / Cost parameter (2^12 = 4,096 rounds)
├── 22 chars ──► Randomly generated 128-bit Salt
└── 31 chars ──► Ciphertext Hash
```

#### 3. Production Code Example
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt with strength 12 (balanced for security & ~100ms CPU execution time)
        return new BCryptPasswordEncoder(12);
    }
}
```

```java
// Service demonstrating password encoding & matching
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void registerUser(String email, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword); // Generates unique salt per call
        userRepository.save(new User(email, hashedPassword));
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A legacy application stored passwords hashed with standard `SHA-256` without salting. During a security audit, the company failed PCI-DSS compliance because unsalted SHA-256 hashes are vulnerable to rainbow table lookups. Migrating to `BCryptPasswordEncoder` with work factor 12 fulfilled compliance criteria and rendered stolen database hashes immune to rainbow table attacks.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use `BCryptPasswordEncoder` (work factor 12) or `Argon2PasswordEncoder`.
- ❌ **DON'T** use `NoOpPasswordEncoder` (plain text) or fast hashing algorithms like `MD5`/`SHA-256` for password storage.

---

### Q106. How do you configure role-based access control in Spring Security?

#### 1. Concept & Interview Answer
- **Role-Based Access Control (RBAC)** restricts access to resources based on assigned user roles (`ADMIN`, `USER`, `MANAGER`).
- **Configuration Levels:**
  1. **URL-Level Security:** Configured in `SecurityFilterChain` using `.requestMatchers("/admin/**").hasRole("ADMIN")` or `.hasAnyRole("ADMIN", "MANAGER")`.
  2. **Method-Level Security:** Configured using `@PreAuthorize("hasRole('ADMIN')")` on service or controller methods.
- **Role vs Authority:**
  - `hasAuthority("DELETE_ORDER")`: Matches the exact authority string `"DELETE_ORDER"`.
  - `hasRole("ADMIN")`: Automatically prepends the `ROLE_` prefix and checks for authority `"ROLE_ADMIN"`.

#### 2. Production Code Example (URL & Method Level RBAC)
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // Activates @PreAuthorize & @Secured
public class RbacSecurityConfig {

    @Bean
    public SecurityFilterChain rbacFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public Endpoints
                .requestMatchers("/api/v1/auth/**", "/public/**").permitAll()
                // Role-Restricted Endpoints
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/finance/**").hasAnyRole("FINANCE", "ADMIN")
                // Authority-Restricted Endpoints
                .requestMatchers("/api/v1/reports/**").hasAuthority("SCOPE_read:reports")
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

```java
// Method-Level RBAC with SpEL Expressions
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeManagementController {

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('PERMISSION_DELETE_USER')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/salary")
    @PreAuthorize("hasRole('HR_MANAGER') or #id == authentication.principal.id")
    public ResponseEntity<SalaryDto> getSalary(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getSalary(id));
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** An internal API allowed junior managers to approve expense claims belonging to other departments. Adding SpEL-based method security `@PreAuthorize("hasRole('MANAGER') and #deptId == authentication.principal.departmentId")` enforced strict departmental boundary checks dynamically at the service layer.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** prefer method-level security (`@PreAuthorize`) over long lists of regex URL matchers in `SecurityFilterChain` for fine-grained business logic.
- ✅ **DO** follow the principle of least privilege (deny by default, explicitly permit required roles).

---

### Q107. What are filters in Spring Security and how are they used?

#### 1. Concept & Interview Answer
- Spring Security is fundamentally built on a **Chain of Servlet Filters** (`SecurityFilterChain`).
- When an HTTP request enters the servlet container, it passes through the `DelegatingFilterProxy`, which hands execution to `FilterChainProxy`.
- Each filter in the chain performs a specific security task (e.g., extracting authentication tokens, validating CSRF tokens, checking session validity, enforcing CORS, and evaluating authorization rules).
- Developers can insert custom filters into specific positions in the chain using:
  - `addFilterBefore(customFilter, TargetFilter.class)`
  - `addFilterAfter(customFilter, TargetFilter.class)`
  - `addFilterAt(customFilter, TargetFilter.class)`

#### 2. Key Built-in Filters in Execution Order
| Filter Name | Responsibility |
| :--- | :--- |
| **`CorsFilter`** | Handles cross-origin preflight `OPTIONS` requests |
| **`CsrfFilter`** | Generates and validates CSRF anti-forgery tokens |
| **`HeaderWriterFilter`** | Injects security headers (`X-Frame-Options`, `HSTS`, `CSP`) |
| **`UsernamePasswordAuthenticationFilter`** | Processes form-based username/password login POST requests |
| **`BearerTokenAuthenticationFilter` / JWT** | Extracts and validates OAuth2 / JWT bearer tokens |
| **`ExceptionTranslationFilter`** | Catches `AuthenticationException` (401) and `AccessDeniedException` (403) |
| **`AuthorizationFilter`** | Final filter checking if current principal has access to requested URL |

#### 3. Production Code Example (Custom API Key Authentication Filter)
```java
package com.enterprise.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-Enterprise-API-Key";
    private final String validApiKey;

    public ApiKeyAuthenticationFilter(String validApiKey) {
        this.validApiKey = validApiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestApiKey = request.getHeader(API_KEY_HEADER);

        if (validApiKey.equals(requestApiKey)) {
            var auth = new UsernamePasswordAuthenticationToken(
                "InternalServiceClient",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_INTERNAL_SERVICE"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response); // Continue filter chain
    }
}
```

```java
// Registering filter in SecurityFilterChain
http.addFilterBefore(new ApiKeyAuthenticationFilter(apiKey), UsernamePasswordAuthenticationFilter.class);
```

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** extend `OncePerRequestFilter` for custom filters to guarantee execution exactly once per request.
- ❌ **DON'T** declare custom security filters as Spring `@Component` beans if you are also registering them via `.addFilterBefore()`, otherwise the Servlet container will register the filter **twice** (once in container chain, once in Spring Security chain).

---

### Q108. How do you secure REST APIs using JWT in Spring Security?

#### 1. Concept & Interview Answer
Securing REST APIs with JSON Web Tokens (JWT) involves:
1. **Stateless Sessions:** Configure `SessionCreationPolicy.STATELESS` (server stores no session in RAM).
2. **Login Endpoint:** Client sends credentials; server validates them and issues a signed JWT token containing claims (`sub`, `roles`, `exp`, `iat`).
3. **JWT Authentication Filter:** Extends `OncePerRequestFilter` to intercept all requests, extract `Authorization: Bearer <token>`, validate cryptographic HMAC/RSA signature, and populate `SecurityContextHolder`.
4. **Token Expiry & Refresh:** Access tokens are short-lived (e.g., 15 minutes); Refresh tokens are long-lived (e.g., 7 days) and stored securely.

#### 2. JWT Authentication Architecture Flow
```
[ Client ] ──(1) POST /api/v1/auth/login {user, pass}──► [ AuthController ]
[ Client ] ◄──(2) Returns { "accessToken": "eyJhbG..." }──┘
    │
    │ (3) GET /api/v1/orders (Header: Authorization: Bearer eyJhbG...)
    ▼
[ JwtAuthenticationFilter ]
    ├── Validates HMAC-SHA256 / RSA-256 Signature
    ├── Checks 'exp' expiration timestamp
    ├── Extracts Username & "ROLE_ADMIN" claims
    └── Sets SecurityContextHolder.getContext().setAuthentication(authToken)
    │
    ▼
[ OrderRestController (Protected Resource) ]
```

#### 3. Production Code Example (JWT Token Provider Service)
```java
package com.enterprise.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long tokenValidityInMilliseconds = 15 * 60 * 1000; // 15 minutes

    public JwtTokenProvider(@Value("${app.jwt.secret:defaultSecretKeyWithAtLeast256BitsLengthForHMACSHA256}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload();
        return claims.get("roles", List.class);
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A high-traffic retail API suffered severe CPU latency during flash sales because symmetric HMAC token validation bottlenecked backend authentication servers. Migrating to **Asymmetric RSA (RS256 - Public/Private Key pair)** allowed the centralized Auth Service to sign tokens with the private key, while 40 downstream microservices validated tokens locally using only the cached public key with zero auth service network dependencies.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use Asymmetric Key pairs (RS256) for multi-service microservice architectures.
- ✅ **DO** keep access token lifespans short (10–15 minutes) and issue refresh tokens with sliding expiration.
- ❌ **DON'T** store sensitive PII (passwords, social security numbers) inside JWT claims; JWT payloads are merely Base64-encoded and readable by anyone.

---

### Q109. What are the common annotations used in Spring Security?

#### 1. Concept & Interview Answer
- **Configuration & Setup:**
  - `@EnableWebSecurity`: Activates Spring Security web support.
  - `@EnableMethodSecurity`: Enables method-level security annotations (`@PreAuthorize`, `@PostAuthorize`, `@Secured`, `@RolesAllowed`).
- **Method-Level Authorization:**
  - `@PreAuthorize("hasRole('ADMIN')")`: Evaluates SpEL expression **before** method execution.
  - `@PostAuthorize("returnObject.owner == authentication.name")`: Evaluates SpEL expression **after** method execution, having access to `returnObject`.
  - `@Secured("ROLE_ADMIN")`: Legacy role-checking annotation (does not support SpEL).
  - `@RolesAllowed("ROLE_ADMIN")`: JSR-250 Java standard role annotation.
- **Principal Injection:**
  - `@AuthenticationPrincipal`: Injects the currently authenticated `UserDetails` or custom principal directly into controller method parameters.

#### 2. Production Code Example
```java
@RestController
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    // Injecting authenticated principal directly into method parameter
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMyProfile(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return ResponseEntity.ok(new UserProfileDto(principal.getId(), principal.getEmail(), principal.getDepartment()));
    }

    // PostAuthorize: Checks if returned entity belongs to current user
    @GetMapping("/documents/{id}")
    @PostAuthorize("returnObject.body.authorEmail == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<DocumentDto> getDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.findDocument(id));
    }
}
```

---

### Q110. How do you configure custom login and logout in Spring Security?

#### 1. Concept & Interview Answer
- For traditional Server-Side Rendered (SSR) web applications:
  - Custom Login: `.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/perform_login").defaultSuccessUrl("/dashboard", true).failureUrl("/login?error=true"))`.
  - Custom Logout: `.logout(logout -> logout.logoutUrl("/perform_logout").logoutSuccessUrl("/login?logout=true").deleteCookies("JSESSIONID").invalidateHttpSession(true))`.
- For REST APIs:
  - Custom login is handled by a dedicated `@PostMapping("/api/v1/auth/login")` in a `@RestController` that validates credentials via `AuthenticationManager` and returns a JWT response payload.

#### 2. Production Code Example (Form Login + Logout Config)
```java
@Bean
public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/css/**", "/js/**").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")               // Custom login HTML view
            .loginProcessingUrl("/do-login")   // POST submit URL
            .defaultSuccessUrl("/home", true)
            .failureUrl("/login?error=true")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/do-logout")
            .logoutSuccessUrl("/login?logout=true")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        )
        .build();
}
```

---

### Q111. What is CSRF and how is it handled in Spring Security?

#### 1. Concept & Interview Answer
- **CSRF (Cross-Site Request Forgery)** is an attack where a malicious website tricks an authenticated user's browser into executing unwanted actions (fund transfer, password change) on a trusted site where the user is currently logged in via session cookies.
- **How Spring Security handles CSRF:**
  - Uses the **Synchronizer Token Pattern**: A random, unique cryptographic token (`CsrfToken`) is generated per user session.
  - On every state-modifying request (`POST`, `PUT`, `DELETE`, `PATCH`), Spring Security verifies that the incoming request header (`X-CSRF-TOKEN` or `_csrf` form field) matches the session token.
- **When to Disable CSRF:**
  - **Disable CSRF only on stateless REST APIs** that authenticate via HTTP `Authorization: Bearer <JWT>` headers (since browsers do not auto-attach Bearer headers).
  - **Never disable CSRF on browser-based applications that authenticate via HTTP cookies / sessions.**

#### 2. CSRF Attack vs Synchronizer Token Defense
```
CSRF ATTACK (Vulnerable without CSRF Token):
Victim logs into bank.com ──► Bank sets 'JSESSIONID' cookie in browser
Victim visits evil.com ──► Evil script triggers: POST bank.com/transfer?to=hacker
Browser automatically attaches 'JSESSIONID' cookie ──► Bank executes transfer!

SPRING SECURITY DEFENSE:
Bank requires header: X-CSRF-TOKEN: random_crypto_token_xyz
Evil.com script cannot read or forge the custom header ──► Spring rejects request (HTTP 403 Forbidden)!
```

#### 3. Production Code Example (SPA Cookie CSRF Repository)
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class CsrfSecurityConfig {

    @Bean
    public SecurityFilterChain csrfFilterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

        return http
            // Configures Cookie CSRF for Single-Page Applications (Angular/React)
            .csrf(csrf -> csrf
                .csrfTokenRepository(tokenRepository)
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                .ignoringRequestMatchers("/api/v1/auth/**") // Exclude public auth endpoints
            )
            .build();
    }
}
```

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** keep CSRF enabled for all session/cookie-authenticated applications.
- ✅ **DO** use `CookieCsrfTokenRepository.withHttpOnlyFalse()` so frontend JavaScript frameworks can read the `XSRF-TOKEN` cookie and send it in the `X-XSRF-TOKEN` header.

---

### Q112. How do you implement method-level security in Spring?

#### 1. Concept & Interview Answer
- Method-level security allows enforcing authorization rules directly on Java methods in the Service or Controller layer.
- Enabled by adding **`@EnableMethodSecurity(prePostEnabled = true)`** on a `@Configuration` class (in Spring Security 6 / Spring Boot 3).
- **Core Annotations:**
  - `@PreAuthorize("hasRole('ADMIN')")`: Checks condition before method executes.
  - `@PostAuthorize("returnObject.userId == authentication.principal.id")`: Checks condition after method executes.
  - `@PreFilter` and `@PostFilter`: Filters collection elements before or after method execution based on SpEL criteria.

#### 2. Production Code Example
```java
package com.enterprise.service;

import com.enterprise.domain.entity.Document;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentManagementService {

    @PreAuthorize("hasRole('MANAGER') or hasAuthority('DOC_CREATE')")
    public void createDocument(String title) {
        // Business logic...
    }

    @PostAuthorize("returnObject.authorEmail == authentication.name or hasRole('ADMIN')")
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id).orElseThrow();
    }

    // PreFilter: Filters out items from the input list that do not belong to current tenant
    @PreFilter("filterObject.tenantId == authentication.principal.tenantId")
    public void batchProcessDocuments(List<Document> documents) {
        // Only tenant-owned documents survive the filter
    }
}
```

---

### Q113. What is the use of @EnableWebSecurity annotation?

#### 1. Concept & Interview Answer
- **`@EnableWebSecurity`** is a marker annotation applied on `@Configuration` classes that activates Spring Security's web security support and registers the `FilterChainProxy` in the Spring `ApplicationContext`.
- In standard Spring Boot with `spring-boot-starter-security`, `@EnableWebSecurity` is applied automatically via auto-configuration (`SecurityAutoConfiguration`).
- However, explicitly adding `@EnableWebSecurity` is enterprise best practice when declaring custom `SecurityFilterChain` beans.

---

### Q114. How do you integrate OAuth2 in Spring Boot application?

#### 1. Concept & Interview Answer
Integrating OAuth2 / OpenID Connect (OIDC) in Spring Boot is achieved using:
1. **OAuth2 Login / Client (`spring-boot-starter-oauth2-client`):** Allows users to log in using third-party Identity Providers (Google, GitHub, Okta, Keycloak, Auth0).
2. **OAuth2 Resource Server (`spring-boot-starter-oauth2-resource-server`):** Secures REST API microservices by validating incoming JWT access tokens issued by an external Authorization Server (Keycloak / Okta / Azure AD) via JWKS (JSON Web Key Set) public keys.

#### 2. Production Code Example (OAuth2 Resource Server with JWT Validation)
```yaml
# application.yml (Resource Server)
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://auth.enterprise.com/realms/enterprise-realm
          jwk-set-uri: https://auth.enterprise.com/realms/enterprise-realm/protocol/openid-connect/certs
```

```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2ResourceServerConfig {

    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasAuthority("SCOPE_admin")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt()) // Auto-validates JWT signature against JWK Set URI
            .build();
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In an enterprise with 50 microservices, every microservice was independently querying an internal user database to validate credentials. Migrating to an **OAuth2 Resource Server architecture with Keycloak** centralized identity management. Microservices validated tokens statelessly via Keycloak's public JWKS certificates, eliminating 100,000 internal database auth queries per minute.

---

### Q115. How do you handle stateless authentication in Spring Security?

#### 1. Concept & Interview Answer
Stateless authentication means the server retains **zero session state** in memory or database between requests.
Every incoming request must provide its own credentials (typically a signed JWT Bearer token).
**Implementation Steps:**
1. Configure `SessionCreationPolicy.STATELESS` in `SecurityFilterChain`.
2. Disable CSRF (`http.csrf(csrf -> csrf.disable())`).
3. Intercept every request using a custom `OncePerRequestFilter` to validate the JWT and populate `SecurityContextHolder`.
4. Ensure the `SecurityContext` is automatically cleared at the end of each request thread dispatch.

---

### Q116. What are common ways to secure REST endpoints?

#### 1. Concept & Interview Answer
Enterprise REST API security checklist:
1. **Transport Security:** Enforce **HTTPS / TLS 1.3** across all endpoints.
2. **Stateless Authentication:** Use **JWT (RS256)** or **OAuth2 Bearer Tokens**.
3. **Role-Based Access Control (RBAC):** Restrict endpoint paths via `.hasRole()` and `@PreAuthorize`.
4. **Rate Limiting & Throttling:** Protect against DDoS/brute-force using Redis Token Bucket (Bucket4j) at API Gateway.
5. **Input Validation:** Strict Jakarta `@Valid` constraints (`@NotNull`, `@Size`, `@Pattern`) on all DTOs to block injection attacks.
6. **CORS Hardening:** Whitelist only authorized frontend domains.
7. **Security Headers:** Inforce `HSTS`, `X-Content-Type-Options: nosniff`, `X-Frame-Options: DENY`.
8. **Sanitized Error Responses:** Use RFC 7807 `ProblemDetail` without leaking stack traces.

---

# Module 5: Microservices Real-World Scenarios & Architecture

---

### Q117. How to structure an Employee Management Microservice (Employee, Department, Project)?

#### 1. Concept & Interview Answer
In a Domain-Driven Design (DDD) microservice ecosystem, bounded contexts dictate service boundaries:
- **Employee Service:** Manages employee profiles, onboarding, compensation, and reporting hierarchies. (Owns `employee_db`).
- **Department Service:** Manages business units, cost centers, and departmental structures. (Owns `department_db`).
- **Project Service:** Manages project lifecycles, deliverables, and resource allocations. (Owns `project_db`).
- **Database-Per-Service Pattern:** Each microservice strictly owns its own dedicated database schema to ensure loose coupling and independent deployability.
- **Inter-Service Associations:** Stored as foreign IDs (e.g., `departmentId: Long` stored in Employee entity, NOT a JPA `@ManyToOne Department` object reference).

#### 2. Architecture Diagram: Microservice Bounded Contexts
```
[ API Gateway (Spring Cloud Gateway) ]
          │
    ┌─────┼──────────────────────────────────┐
    ▼     ▼                                  ▼
[ Employee Service ]               [ Department Service ]         [ Project Service ]
  ├── Employee Entity                ├── Department Entity          ├── Project Entity
  └── DB: employee_db (PostgreSQL)   └── DB: dept_db (PostgreSQL)   └── DB: project_db (PostgreSQL)
```

#### 3. Production Code Example (Employee Entity in Microservice)
```java
package com.enterprise.employee.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    // In Microservices: Store ID only, NOT a direct JPA @ManyToOne Department entity!
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    private LocalDate hireDate;
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** A team built three microservices (Employee, Department, Project) but pointed all three at a single shared MySQL database. When the Department service ran a schema migration renaming a column, the Employee service crashed in production due to broken JPA entity bindings. Enforcing the **Database-per-Service** pattern decoupled deployment lifecycles and eliminated shared-database schema corruption.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** store foreign microservice references as scalar IDs (`Long departmentId`, `UUID customerId`), never as JPA object associations (`@ManyToOne Department`).
- ❌ **DON'T** share database instances or schemas across different microservices.

---

### Q118. Microservice communication: REST vs OpenFeign vs Message Brokers?

#### 1. Concept & Interview Answer
- **Synchronous Communication (Request-Response):**
  - **REST / OpenFeign:** Best for immediate read queries where the client cannot proceed without the response (e.g., querying real-time inventory balance before displaying a checkout button).
  - **Drawback:** Tight runtime coupling; if downstream service is slow or down, the upstream service stalls (cascading failure).
- **Asynchronous Communication (Event-Driven):**
  - **Message Brokers (Apache Kafka, RabbitMQ, AWS SQS):** Best for state-changing workflows, commands, and events (e.g., `OrderCreatedEvent` -> `PaymentService`, `InventoryService`, `EmailService`).
  - **Benefits:** High throughput, temporal decoupling, automatic retries, and zero cascading outages.

#### 2. Communication Tradeoff Matrix
| Dimension | REST / OpenFeign | Message Broker (Kafka / RabbitMQ) |
| :--- | :--- | :--- |
| **Coupling** | High (Temporal coupling) | Loose (Event-driven) |
| **Failure Impact** | Cascading timeouts unless protected by Circuit Breaker | Safe (Messages buffered in queue/topic until consumer recovers) |
| **Throughput** | Moderate | Extremely High (Millions of msgs/sec in Kafka) |
| **Best Used For** | Real-time Querying / Aggregation | Asynchronous mutations, notifications, Saga steps |

---

### Q119. Multiple microservices accessing different databases: How to maintain data consistency?

#### 1. Concept & Interview Answer
In distributed systems, traditional 2-Phase Commit (2PC / XA Transactions) is an anti-pattern that does not scale.
Instead, enterprise microservices maintain **Eventual Consistency** using:
1. **The Saga Pattern:** A sequence of local database transactions. Each step executes a local transaction and publishes an event/message triggering the next step. If a step fails, **Compensating Transactions** execute in reverse order to undo changes.
2. **Transactional Outbox Pattern:** Ensures reliable event publishing by writing the domain entity and an outbox event record into the **same local database transaction**, then polling or tailing the database WAL (Debezium / CDC) to publish to Kafka.

#### 2. Saga Pattern (Choreography Flow)
```
1. OrderService (Tx 1: Creates PENDING Order) ──Publishes OrderCreatedEvent──► [ Kafka Topic ]
                                                                                       │
2. PaymentService (Tx 2: Debits Card) ◄────────────────────────────────────────────────┘
   ├── If Success: Publishes PaymentSuccessEvent ──► OrderService sets Order = CONFIRMED
   └── If Failed:  Publishes PaymentFailedEvent  ──► OrderService (Compensating Tx: Cancels Order)
```

#### 3. Production Code Example (Transactional Outbox Pattern)
```java
@Service
public class OrderCreationService {

    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxRepository;

    public OrderCreationService(OrderRepository orderRepository, OutboxEventRepository outboxRepository) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
    }

    @Transactional // Single local ACID transaction guarantees entity and event are saved atomically
    public Order createOrder(CreateOrderDto dto) {
        // 1. Save business entity
        Order order = new Order(dto.customerId(), dto.amount(), "PENDING");
        Order savedOrder = orderRepository.save(order);

        // 2. Save event in Outbox table within same DB transaction
        OutboxEvent event = new OutboxEvent(
            "ORDER_EVENTS",
            savedOrder.getId().toString(),
            "OrderCreated",
            asJson(savedOrder)
        );
        outboxRepository.save(event);

        return savedOrder;
        // Background CDC (Debezium) or poller reads Outbox table and publishes to Kafka!
    }
}
```

#### 4. Real-World Project Scenario
> **Incident / Scenario:** In an e-commerce platform, `orderService` saved an order to the database and then directly called `kafkaTemplate.send()`. When the Kafka broker crashed right after the database commit, the order existed in the database but was never processed for payment, resulting in lost revenue. Implementing the **Transactional Outbox Pattern** guaranteed 100% reliable event delivery without dual-write race conditions.

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** use the **Transactional Outbox Pattern** to prevent dual-write inconsistencies between database commits and message broker dispatches.
- ✅ **DO** implement idempotent consumers in all downstream event listeners.

---

### Q120. Handling aggregated responses from multiple microservices in a single API call?

#### 1. Concept & Interview Answer
To aggregate data from multiple downstream services (e.g., Building a customer dashboard requiring data from `CustomerService`, `OrderService`, and `RewardService`):
1. **API Gateway Aggregation Pattern (BFF - Backend for Frontend):** A dedicated aggregation layer calls downstream services concurrently.
2. **Asynchronous Non-Blocking Execution:** Using **`CompletableFuture.allOf()`** or Spring WebFlux **`Mono.zip()`** to fetch all downstream data in parallel rather than sequentially.
3. **GraphQL Federation:** GraphQL gateway resolving subgraphs across multiple microservices.

#### 2. Parallel Aggregation Execution Flow
```
Sequential (Slow: 300ms + 400ms + 200ms = 900ms):
Gateway ──► CustomerService (300ms) ──► OrderService (400ms) ──► RewardService (200ms)

Parallel (Fast: Max(300ms, 400ms, 200ms) = 400ms):
Gateway ──┬──► CustomerService (300ms) ──┐
          ├──► OrderService (400ms)    ──┼──► CompletableFuture.allOf() ──► Combined Response (400ms!)
          └──► RewardService (200ms)   ──┘
```

#### 3. Production Code Example (Parallel Aggregation with CompletableFuture)
```java
package com.enterprise.bff.service;

import com.enterprise.bff.dto.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CustomerDashboardAggregatorService {

    private final CustomerServiceClient customerClient;
    private final OrderServiceClient orderClient;
    private final RewardServiceClient rewardClient;

    public CustomerDashboardAggregatorService(CustomerServiceClient c, OrderServiceClient o, RewardServiceClient r) {
        this.customerClient = c;
        this.orderClient = o;
        this.rewardClient = r;
    }

    public DashboardSummaryDto getCustomerDashboard(Long customerId) {
        // Trigger all 3 HTTP calls concurrently in parallel
        CompletableFuture<CustomerDto> customerFuture = CompletableFuture.supplyAsync(() -> customerClient.getCustomer(customerId));
        CompletableFuture<List<OrderSummaryDto>> ordersFuture = CompletableFuture.supplyAsync(() -> orderClient.getRecentOrders(customerId));
        CompletableFuture<RewardPointsDto> rewardsFuture = CompletableFuture.supplyAsync(() -> rewardClient.getPoints(customerId));

        // Wait for all 3 futures to complete concurrently
        CompletableFuture.allOf(customerFuture, ordersFuture, rewardsFuture).join();

        return new DashboardSummaryDto(
            customerFuture.join(),
            ordersFuture.join(),
            rewardsFuture.join()
        );
    }
}
```

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** execute downstream REST queries in parallel using `CompletableFuture` or `WebClient` to reduce overall user response latency.
- ✅ **DO** provide fallback default values for non-critical downstream calls (e.g., if `RewardService` fails, return 0 points instead of failing the entire dashboard).

---

### Q121. Securing internal downstream microservices using JWT tokens?

#### 1. Concept & Interview Answer
- When a client sends a request to the **API Gateway**, the Gateway validates the incoming user JWT token once.
- **Internal Service-to-Service Propagation:**
  1. The API Gateway forwards the validated token or extracts claims into internal headers (`X-User-Id`, `X-User-Roles`).
  2. For downstream Feign / WebClient calls between internal services, a **`RequestInterceptor`** intercepts outgoing HTTP calls and automatically attaches `Authorization: Bearer <token>` from the current `SecurityContextHolder`.
  3. Internal services validate the JWT signature using shared public keys (JWKS).
  4. Mutual TLS (mTLS) via Service Mesh (Istio / Linkerd) is layered on top for transport encryption and service identity verification.

#### 2. Token Propagation via OpenFeign RequestInterceptor
```java
package com.enterprise.client.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignJwtTokenRelayInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If current request has credentials (JWT), propagate it to downstream service
        if (authentication != null && authentication.getCredentials() != null) {
            String token = authentication.getCredentials().toString();
            template.header("Authorization", "Bearer " + token);
        }
    }
}
```

---

### Q122. User Service is down but Order Service depends on it: How to make your system resilient?

#### 1. Concept & Interview Answer
To prevent cascading outages when a dependent downstream microservice fails:
1. **Resilience4j Circuit Breaker:** Detects downstream failures and "opens" the circuit, instantly returning fallback responses instead of waiting for timeouts.
2. **Local Caching (Redis / Caffeine):** Cache frequently accessed user profile data in the Order Service.
3. **Data Replication via Events:** Order Service listens to `UserUpdatedEvent` from Kafka and keeps a minimal read-only local replica (`userId`, `email`, `shippingAddress`) in its own database.
4. **Fallback Degradation:** Return graceful degraded responses (e.g., "User details temporarily unavailable").

#### 2. Resilience4j Circuit Breaker State Machine
```
           [ CLOSED ] (Normal Operation: Calls flow through)
               │
               │ (Failure Rate > 50%)
               ▼
           [ OPEN ]   (Fast-Fail: Calls instantly redirected to Fallback)
               │
               │ (Wait Duration: 10 seconds expires)
               ▼
         [ HALF-OPEN ] (Sends trial calls to test if downstream recovered)
          ├── Success: Returns to [ CLOSED ]
          └── Failure: Re-opens [ OPEN ]
```

#### 3. Production Code Example (Resilience4j Circuit Breaker with Fallback)
```java
package com.enterprise.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResilientOrderService {

    private static final Logger log = LoggerFactory.getLogger(ResilientOrderService.class);
    private final UserServiceClient userClient;

    public ResilientOrderService(UserServiceClient userClient) {
        this.userClient = userClient;
    }

    @CircuitBreaker(name = "userServiceBreaker", fallbackMethod = "getUserFallback")
    @Retry(name = "userServiceRetry")
    public UserSummaryDto fetchUserDetails(Long userId) {
        return userClient.getUserById(userId); // Remote Feign Call
    }

    // Fallback executed when UserService is down or Circuit is OPEN
    public UserSummaryDto getUserFallback(Long userId, Throwable ex) {
        log.warn("UserService unavailable. Executing fallback for userId {}. Error: {}", userId, ex.getMessage());
        return new UserSummaryDto(userId, "Guest / Cached User", "N/A", false);
    }
}
```

```yaml
# application.yml
resilience4j:
  circuitbreaker:
    instances:
      userServiceBreaker:
        sliding-window-size: 10
        failure-rate-threshold: 50       # Open circuit if 5 out of 10 calls fail
        wait-duration-in-open-state: 10s # Wait 10s before testing recovery
        permitted-number-of-calls-in-half-open-state: 3
```

#### 5. Best Practices & Enterprise Recommendations
- ✅ **DO** define fallbacks for all synchronous inter-service calls.
- ✅ **DO** prefer event-driven data replication for critical dependencies so services can operate 100% autonomously during network partitions.

---

### Q123. How do you handle configuration across multiple microservices (Spring Cloud Config Server)?

#### 1. Concept & Interview Answer
- **Spring Cloud Config Server** provides centralized, externalized configuration management for distributed microservices.
- **How it works:**
  1. Configurations for all microservices (`order-service.yml`, `payment-service.yml`) are stored in a centralized Git repository, HashiCorp Vault, or AWS S3.
  2. Config Server acts as an HTTP server serving these configurations.
  3. Microservices pull their environment configurations on startup (`spring.config.import: "configserver:http://..."`).
  4. Configurations can be dynamically refreshed at runtime using **`@RefreshScope`** and `POST /actuator/refresh` (or automated via Spring Cloud Bus + Kafka).

---

### Q124. Production failure detection and auto-recovery in microservices?

#### 1. Concept & Interview Answer
1. **Health Probes:** Expose Spring Boot Actuator `/actuator/health/liveness` and `/actuator/health/readiness`.
2. **Kubernetes Self-Healing:**
   - **Liveness Probe Failure:** K8s automatically restarts the deadlocked container pod.
   - **Readiness Probe Failure:** K8s removes the pod from load balancer routing until it recovers.
3. **Automated Rollbacks:** If new deployment triggers error spikes, CI/CD canary or blue-green pipelines automatically roll back to the previous stable release.

---

### Q125. Service Discovery & Client-Side Load Balancing (Eureka / Spring Cloud Gateway)?

#### 1. Concept & Interview Answer
- **Service Registry (Netflix Eureka / HashiCorp Consul):** Microservices register their dynamic IP addresses and hostnames on startup (`@EnableDiscoveryClient`) and send heartbeats every 30 seconds.
- **Client-Side Load Balancing (Spring Cloud LoadBalancer):** Instead of routing through a single hardware load balancer bottleneck, clients fetch the list of available healthy service instances from Eureka and distribute requests across them using **Round Robin** or **Weighted Response Time** algorithms.

---

### Q126. Designing microservices for Zero Downtime Deployment (Blue-Green / Rolling)?

#### 1. Concept & Interview Answer
1. **Rolling Deployments (Kubernetes):** Gradually replaces old pods with new pods one by one. Pods only receive traffic after the Readiness Probe turns green (`maxUnavailable: 0`, `maxSurge: 1`).
2. **Blue-Green Deployments:** Deploys new version (Green) alongside live version (Blue). Router / ALB flips 100% of traffic to Green instantly after smoke tests pass.
3. **Backward-Compatible Database Migrations (Expand-and-Contract Pattern):**
   - Step 1 (Expand): Add new columns/tables via Flyway without deleting old columns.
   - Step 2 (Deploy): Deploy new microservice code writing to both old and new columns.
   - Step 3 (Contract): After 100% deployment, drop deprecated columns in a subsequent migration.

---

### Q127 to Q136: Rapid Scenario & Persistence Review

- **Q127 (Employee-Department JPA Design):** Use `@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "dept_id")` on `Employee` (owning side) and `@OneToMany(mappedBy = "department")` on `Department` with synchronized helper methods.
- **Q128 (N+1 Resolution):** Apply `JOIN FETCH` or `@EntityGraph` on repository queries, and configure `hibernate.default_batch_fetch_size=50`.
- **Q129 (Large Data Fetching 10k records):** Use Spring Data `Pageable` or streaming query `Stream<T>` within `@Transactional(readOnly = true)` processing rows with `entityManager.detach()`.
- **Q130 (Soft Deletes in JPA):** Use `@SQLDelete(sql = "UPDATE table SET deleted = true WHERE id = ?")` and `@SQLRestriction("deleted = false")` (or `@SoftDelete` in Hibernate 6.4+).
- **Q131 (Dynamic Filters with Specifications):** Use `JpaSpecificationExecutor<T>` with `Specification<T>` combining predicates dynamically (`cb.equal()`, `cb.like()`, `cb.between()`).
- **Q132 (LazyLoadingException in REST):** Return immutable DTO records instead of JPA entities; initialize associations in service queries via `@EntityGraph`.
- **Q133 (Order -> OrderItem Query Design):** Write custom JPQL DTO projection: `SELECT new OrderSummaryDto(o.id, o.number, COUNT(i)) FROM Order o JOIN o.items i GROUP BY o.id, o.number`.
- **Q134 (Distributed Rollback):** Implement **Compensating Transactions** in the Saga pattern (e.g., calling `paymentService.refund()` if inventory reservation fails).
- **Q135 (Batch Inserts in JPA):** Use `GenerationType.SEQUENCE`, configure `hibernate.jdbc.batch_size=50`, and regularly call `flush()` and `clear()`.
- **Q136 (DB Schema Migration):** Use version-controlled migration scripts managed by **Flyway** in `src/main/resources/db/migration/`.

---

### Q137. Implementing synchronous service-to-service calls using OpenFeign?

#### 1. Production Code Example
```java
package com.enterprise.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${app.services.customer-url}")
public interface CustomerFeignClient {

    @GetMapping("/api/v1/customers/{id}")
    CustomerDto getCustomerById(@PathVariable("id") Long customerId);

    record CustomerDto(Long id, String name, String email) {}
}
```

---

### Q138. Feign Client failure resilience using Resilience4j Circuit Breaker?

#### 1. Production Code Example
```java
@FeignClient(
    name = "customer-service",
    url = "${app.services.customer-url}",
    fallback = CustomerFeignFallback.class // Fallback bean when service fails
)
public interface ResilientCustomerFeignClient {
    @GetMapping("/api/v1/customers/{id}")
    CustomerDto getCustomerById(@PathVariable("id") Long id);
}

@Component
class CustomerFeignFallback implements ResilientCustomerFeignClient {
    @Override
    public CustomerDto getCustomerById(Long id) {
        return new CustomerDto(id, "Unknown Customer (Fallback)", "support@enterprise.com");
    }
}
```

---

### Q139 to Q146: Advanced Microservice Architecture Standards

- **Q139 (JWT Token Relay):** Implement a Feign `RequestInterceptor` that grabs the `Authorization` header from the current incoming `HttpServletRequest` and injects it into outgoing Feign templates.
- **Q140 (Gateway Single JWT Validation):** API Gateway (Spring Cloud Gateway) verifies the JWT cryptographic signature and sets downstream headers: `X-Auth-UserId: 101`, `X-Auth-Roles: ADMIN`. Internal services read these trusted headers directly.
- **Q141 (CORS Handling):** Configure CORS globally at the API Gateway level via `spring.cloud.gateway.globalcors` so individual microservices don't require duplicate CORS policies.
- **Q142 (Shared DB Consistency):** Enforce strict database schema separation per microservice; eliminate shared database patterns.
- **Q143 (Idempotency in Payments):** Require clients to pass an `Idempotency-Key: UUID` header. The server stores the key in Redis with `SET key result NX EX 86400`. If duplicate key arrives, return cached response immediately without re-executing payment.
- **Q144 (Centralized Global Exceptions):** Create a shared Maven library containing a `@RestControllerAdvice` extending `ResponseEntityExceptionHandler` that formats all errors into RFC 7807 `ProblemDetail` with correlation IDs.
- **Q145 (Centralized Logging):** Use Logback with `LogstashEncoder` to output JSON logs. Use Micrometer Tracing (Brave/OpenTelemetry) to inject `traceId` and `spanId` into SLF4J MDC, shipped to Elasticsearch/Logstash and visualized in Kibana.
- **Q146 (Production Monitoring):** Expose Prometheus metrics via `/actuator/prometheus`, scrape with Prometheus Server every 15s, and build operational alerting dashboards in Grafana.

---
