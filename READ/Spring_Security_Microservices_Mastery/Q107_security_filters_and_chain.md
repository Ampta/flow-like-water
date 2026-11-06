# Q107: What are filters in Spring Security and how are they used?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In a B2B multi-tenant logistics platform, tenant organization IDs were passed in an HTTP header (`X-Tenant-ID`). Initially, developers parsed this header inside each `@RestController` method. A newly added billing endpoint forgot the manual extraction, causing customer data from Tenant A to be queried alongside Tenant B. 

By implementing a custom Spring Security filter extending **`OncePerRequestFilter`**, the system intercepted every incoming request at the perimeter, validated the tenant against the database, populated a thread-local `TenantContextHolder`, and rejected unauthorized tenant requests before they ever reached the MVC `DispatcherServlet`.

---

### 2. Concept & Architecture

```
                                  [ Incoming HTTP Request ]
                                              │
                                              ▼
┌───────────────────────────────── SecurityFilterChain ─────────────────────────────────┐
│                                                                                       │
│  [ Custom TenantFilter ] (Registered via .addFilterBefore(..., UsernamePassword...))  │
│  - Parses 'X-Tenant-ID' Header                                                        │
│  - Sets TenantContextHolder                                                           │
│  - Finally: TenantContextHolder.clear()                                               │
│             │                                                                         │
│             ▼                                                                         │
│  [ JwtAuthenticationFilter ]                                                          │
│  - Validates Bearer Token                                                             │
│  - Sets SecurityContextHolder                                                         │
│             │                                                                         │
│             ▼                                                                         │
│  [ AuthorizationFilter ]                                                              │
│  - Checks RequestMatcher & Authorities                                                │
│                                                                                       │
└─────────────────────────────────────────────┬─────────────────────────────────────────┘
                                              │ (Passes down chain)
                                              ▼
                                     [ DispatcherServlet ]
```

#### Why `OncePerRequestFilter`?
Standard Servlet `Filter.doFilter()` can be triggered multiple times during a single HTTP request lifecycle (for example, during internal request dispatches, error dispatches, or forward operations). `OncePerRequestFilter` guarantees **exactly one execution per incoming request**, making it the premier base class for authentication, logging, and security context enrichment.

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Custom Tenant Resolution Filter (`TenantContextFilter.java`)
```java
package com.enterprise.security.filter;

import com.enterprise.security.context.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class TenantContextFilter extends OncePerRequestFilter {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        
        // Skip public or health check endpoints
        if (path.startsWith("/actuator") || path.startsWith("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String tenantId = request.getHeader(TENANT_HEADER);

        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Missing X-Tenant-ID header for request to: {}", path);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            response.getWriter().write("{"error": "Missing required X-Tenant-ID header"}");
            return;
        }

        try {
            TenantContextHolder.setTenantId(tenantId);
            // Proceed to next filter in the SecurityFilterChain
            filterChain.doFilter(request, response);
        } finally {
            // CRITICAL: Always clean up ThreadLocal in finally block to avoid thread pool pollution!
            TenantContextHolder.clear();
        }
    }
}
```

#### 2. Registering Filter in SecurityFilterChain (`SecurityConfig.java`)
```java
package com.enterprise.security.config;

import com.enterprise.security.filter.TenantContextFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TenantContextFilter tenantContextFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            // Position custom filter right before standard authentication filter
            .addFilterBefore(tenantContextFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, the counterpart to Spring's `OncePerRequestFilter` is an **Angular HTTP Interceptor (`HttpInterceptorFn`)**. 
Every outbound request must be intercepted to attach the `X-Tenant-ID` header, trace execution latency, and handle tenancy errors.

---

### 2. Production Code (Angular 17+ Standalone)

#### Tenant Header Interceptor (`tenant.interceptor.ts`)
```typescript
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TenantService } from '../services/tenant.service';

export const tenantInterceptor: HttpInterceptorFn = (req, next) => {
  const tenantService = inject(TenantService);
  const activeTenantId = tenantService.currentTenantId();

  // Clone the request and inject the required tenant header
  const modifiedReq = activeTenantId
    ? req.clone({
        setHeaders: {
          'X-Tenant-ID': activeTenantId
        }
      })
    : req;

  return next(modifiedReq);
};
```

#### Tenant State Management Service (`tenant.service.ts`)
```typescript
import { Injectable, signal, computed } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TenantService {
  private readonly tenantSignal = signal<string>(this.resolveInitialTenant());

  readonly currentTenantId = computed(() => this.tenantSignal());

  setTenant(tenantId: string): void {
    localStorage.setItem('active_tenant_id', tenantId);
    this.tenantSignal.set(tenantId);
  }

  private resolveInitialTenant(): string {
    return localStorage.getItem('active_tenant_id') || 'tenant-default';
  }
}
```
