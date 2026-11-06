# Q101: What is Spring Security and why is it used?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In a FinTech core banking platform, an internal administrative microservice was compromised via a **Clickjacking** and **Session Hijacking** attack. An attacker embedded the sensitive bank-transfer portal inside an invisible `<iframe>` on a malicious external site. Simultaneously, because the legacy application lacked automated defense-in-depth headers and relied on ad-hoc servlet filters, requests lacked `Content-Security-Policy` (CSP), `X-Frame-Options`, and Strict-Transport-Security (`HSTS`). Furthermore, API authentication logic was scattered across controllers, leading to authorization bypasses whenever junior developers forgot manual permission checks.

Integrating **Spring Security** solved this by establishing a centralized, declarative **Security Filter Chain** that executes before any request hits the application logic, guaranteeing standardized authentication, robust role-based authorization, and automatic injection of enterprise-grade security headers.

---

### 2. Concept & Architecture
Spring Security is a comprehensive, modular framework that provides authentication, authorization, and protection against common web exploits for Java applications. At its core, Spring Security relies on standard Servlet **Filter** specifications, coordinated via a design pattern called the **DelegatingFilterProxy**.

```
[ Incoming HTTP Client Request ]
             │
             ▼
┌───────────────────────────── Servlet Container (Tomcat / Jetty) ─────────────────────────────┐
│                                                                                              │
│   [ DelegatingFilterProxy ]  (Standard Servlet Filter registered in web.xml / Spring Boot)  │
│              │                                                                               │
└──────────────┼───────────────────────────────────────────────────────────────────────────────┘
               │ Delegates request to Spring-managed ApplicationContext
               ▼
┌───────────────────────────── Spring Application Context ─────────────────────────────────────┐
│                                                                                              │
│   [ FilterChainProxy ] (Spring Bean: "springSecurityFilterChain")                            │
│              │                                                                               │
│              ▼                                                                               │
│   [ SecurityFilterChain ] (Ordered list of Security Filters)                                 │
│       ├── 1. DisableEncodeUrlFilter                                                         │
│       ├── 2. WebAsyncManagerIntegrationFilter                                               │
│       ├── 3. HeaderWriterFilter (Injects HSTS, X-Frame-Options: DENY, CSP)                   │
│       ├── 4. CorsFilter (Validates Origin against Allowed Origins)                           │
│       ├── 5. CsrfFilter (Validates CSRF Token for stateful mutating requests)                │
│       ├── 6. LogoutFilter                                                                    │
│       ├── 7. JwtAuthenticationFilter (Custom filter: parses Bearer token)                    │
│       ├── 8. UsernamePasswordAuthenticationFilter (Evaluates login credentials)              │
│       ├── 9. SecurityContextHolderAwareRequestFilter                                         │
│       ├── 10. AnonymousAuthenticationFilter (Assigns anonymous role if unauthenticated)     │
│       ├── 11. ExceptionTranslationFilter (Converts Security Exceptions to 401/403)           │
│       └── 12. AuthorizationFilter (Evaluates endpoint permissions via RequestMatcher)        │
│              │                                                                               │
└──────────────┼───────────────────────────────────────────────────────────────────────────────┘
               │ Passes request down the chain if authorized
               ▼
┌──────────────────────────────── DispatcherServlet ───────────────────────────────────────────┐
│   [ HandlerMapping ] ──► [ @RestController / HandlerAdapter ]                                │
└──────────────────────────────────────────────────────────────────────────────────────────────┘
```

#### Core Components:
1. **DelegatingFilterProxy**: An ambient Servlet filter that bridges standard servlet container lifecycles to Spring's `ApplicationContext`, delegating the HTTP request to the `FilterChainProxy`.
2. **FilterChainProxy**: Coordinates one or multiple `SecurityFilterChain` beans, selecting which chain applies based on matching URL patterns.
3. **SecurityFilterChain**: An ordered sequence of security filters.
4. **SecurityContextHolder**: Holds the security details of the current execution thread, including the authenticated `Principal` and granted `Authorities`.

---

### 3. Production Code (Spring Boot 3.x)
In Spring Boot 3.x and Spring Security 6.x, the legacy `WebSecurityConfigurerAdapter` is completely removed. Configuration is declared functionally via a `@Bean` returning `SecurityFilterChain`.

```java
package com.enterprise.security.config;

import com.enterprise.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class EnterpriseSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // 1. Disable CSRF for stateless REST APIs using JWT
            .csrf(csrf -> csrf.disable())
            
            // 2. Enable strict CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 3. Configure HTTP security headers (Defense in Depth)
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny()) // Mitigates Clickjacking
                .xssProtection(xss -> xss.disable()) // Modern browsers use Content-Security-Policy
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; frame-ancestors 'none';"))
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                )
                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            )
            
            // 4. Enforce Stateless Session Policy
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 5. Authorize Request Matchers
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/actuator/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/management/**").hasAnyRole("ADMIN", "MANAGER")
                .anyRequest().authenticated()
            )
            
            // 6. Register Custom JWT Filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://banking.enterprise.com"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "X-Correlation-Id"));
        configuration.setExposedHeaders(List.of("X-Correlation-Id", "Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
An Angular Single Page Application (SPA) integrates with Spring Security by establishing an HTTP boundary. The frontend does not manage user session state on the server; instead, it coordinates:
1. Sending credentials to the backend authentication endpoint (`/api/v1/auth/authenticate`).
2. Intercepting every outbound HTTP request via an Angular `HttpInterceptorFn` to attach the Bearer token.
3. Catching `401 Unauthorized` and `403 Forbidden` responses globally to redirect to login or show an access denied notification.

```
┌───────────────────────────────── Angular SPA ─────────────────────────────────┐
│                                                                               │
│  [ UI Component ] ──► [ AuthService ] ──► [ HttpClient ]                      │
│                                                  │                            │
│                                                  ▼                            │
│                                       [ AuthInterceptorFn ]                   │
│                                       (Attaches Bearer Token)                 │
│                                                  │                            │
└──────────────────────────────────────────────────┼────────────────────────────┘
                                                   │ HTTPS Request with Bearer Token
                                                   ▼
┌─────────────────────────────── Spring Boot API ───────────────────────────────┐
│  [ SecurityFilterChain ] ──► [ JwtAuthenticationFilter ] ──► [ Controller ]   │
└───────────────────────────────────────────────────────────────────────────────┘
```

---

### 2. Production Code (Angular 17+ Standalone)

#### 1. Global Application Configuration (`app.config.ts`)
```typescript
import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { securityErrorInterceptor } from './core/interceptors/security-error.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(
      withInterceptors([authInterceptor, securityErrorInterceptor])
    )
  ]
};
```

#### 2. Security Error Interceptor (`security-error.interceptor.ts`)
```typescript
import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const securityErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        // Backend SecurityFilterChain rejected token / unauthenticated
        authService.clearSession();
        router.navigate(['/auth/login'], { queryParams: { sessionExpired: 'true' } });
      } else if (error.status === 403) {
        // Principal authenticated, but lacks required Role/Authority
        router.navigate(['/error/forbidden']);
      }
      return throwError(() => error);
    })
  );
};
```

#### 3. Core Authentication State Service (`auth.service.ts`)
```typescript
import { Injectable, signal, computed } from '@angular/core';

export interface UserSession {
  username: string;
  roles: string[];
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly sessionSignal = signal<UserSession | null>(this.loadPersistedSession());

  // Public readonly signals for reactive UI binding
  readonly currentUser = computed(() => this.sessionSignal());
  readonly isAuthenticated = computed(() => !!this.sessionSignal());
  readonly userRoles = computed(() => this.sessionSignal()?.roles ?? []);

  setSession(session: UserSession): void {
    localStorage.setItem('auth_token', session.token);
    this.sessionSignal.set(session);
  }

  clearSession(): void {
    localStorage.removeItem('auth_token');
    this.sessionSignal.set(null);
  }

  hasRole(requiredRole: string): boolean {
    return this.userRoles().includes(requiredRole);
  }

  private loadPersistedSession(): UserSession | null {
    const token = localStorage.getItem('auth_token');
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        username: payload.sub,
        roles: payload.roles || [],
        token
      };
    } catch {
      localStorage.removeItem('auth_token');
      return null;
    }
  }
}
```
