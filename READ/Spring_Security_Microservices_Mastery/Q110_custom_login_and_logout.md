# Q110: How do you configure custom login and logout in Spring Security?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A mobile banking and web portal suffered from a severe security vulnerability: when users clicked "Logout" in the Angular web app, the client merely removed the JWT token from `localStorage`. However, the refresh token and JWT remained valid on the server. If an attacker had intercepted the token or accessed a public library workstation after the user left, they could continue executing financial transactions. 

Implementing a true stateless **RESTful Logout Handler** in Spring Security that revokes refresh tokens in Redis, clears the `SecurityContextHolder`, invalidates session cookies, and registers token fingerprints on a blacklist resolved the vulnerability.

---

### 2. Concept & Architecture

```
┌──────────────────────────────── RESTful Custom Login & Logout Flow ────────────────────────────────┐
│                                                                                                    │
│  LOGIN:                                                                                            │
│  Client POST /api/v1/auth/login { username, password }                                             │
│       │                                                                                            │
│       ▼                                                                                            │
│  AuthenticationManager.authenticate() ──► Success                                                  │
│       │                                                                                            │
│       ▼                                                                                            │
│  Generate Tokens:                                                                                  │
│  - Access Token (short-lived JWT returned in response JSON)                                        │
│  - Refresh Token (stored in Redis: key="refresh:" + username, value=UUID, TTL=7 days)            │
│  - Set HttpOnly, Secure, SameSite=Strict cookie for Refresh Token                                  │
│                                                                                                    │
│  LOGOUT:                                                                                           │
│  Client POST /api/v1/auth/logout                                                                   │
│       │                                                                                            │
│       ▼                                                                                            │
│  SecurityFilterChain invokes CustomLogoutHandler:                                                  │
│  1. Extract Bearer Access Token & blacklist remaining validity in Redis (TTL = remaining seconds)  │
│  2. Delete Refresh Token from Redis                                                                │
│  3. Clear SecurityContextHolder.clearContext()                                                     │
│  4. Overwrite Refresh Cookie with Max-Age=0 (Immediate browser deletion)                           │
│  5. Return HTTP 204 No Content via LogoutSuccessHandler                                            │
│                                                                                                    │
└────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Custom Stateless Logout Handler (`StatelessLogoutHandler.java`)
```java
package com.enterprise.security.handler;

import com.enterprise.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatelessLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtService.extractUsername(jwt);
            long remainingTime = jwtService.getRemainingValidityMs(jwt);

            // 1. Blacklist Access Token in Redis for its remaining lifetime
            if (remainingTime > 0) {
                redisTemplate.opsForValue().set("blacklist:" + jwt, "revoked", remainingTime, TimeUnit.MILLISECONDS);
            }

            // 2. Remove stored Refresh Token
            if (username != null) {
                redisTemplate.delete("refresh:" + username);
            }
            log.info("Successfully revoked tokens for user: {}", username);
        }

        // 3. Clear HttpOnly Cookie
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/api/v1/auth");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0); // Deletes cookie immediately
        response.addCookie(cookie);

        // 4. Clear SecurityContext
        SecurityContextHolder.clearContext();
    }
}
```

#### 2. Registering in SecurityFilterChain (`SecurityConfig.java`)
```java
package com.enterprise.security.config;

import com.enterprise.security.handler.StatelessLogoutHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final StatelessLogoutHandler statelessLogoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(statelessLogoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                })
            )
            .build();
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
In Angular, logging out requires an orchestrated sequence:
1. Triggering the backend `/api/v1/auth/logout` endpoint to invalidate server-side tokens.
2. In case of network errors or server downtime, client-side session cleanup must still execute reliably (`finalize`).
3. Resetting all reactive signals, clearing local storage, and navigating to `/login` with an informative toast message.

---

### 2. Production Code (Angular 17+ Standalone)

#### Robust Logout Service (`auth-logout.service.ts`)
```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthLogoutService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);

  logout(): void {
    // 1. Send logout request to backend to blacklist tokens and delete HttpOnly cookies
    this.http.post('/api/v1/auth/logout', {})
      .pipe(
        // Guarantee local state cleanup whether network call succeeds or fails
        finalize(() => {
          this.performLocalCleanup();
        })
      )
      .subscribe({
        next: () => console.log('Server session terminated successfully.'),
        error: (err) => console.warn('Server logout failed, proceeding with local cleanup:', err)
      });
  }

  private performLocalCleanup(): void {
    // 2. Clear state in Angular Signals and Storage
    this.authService.clearSession();

    // 3. Navigate to login with clean query parameters
    this.router.navigate(['/auth/login'], {
      queryParams: { loggedOut: 'true' },
      replaceUrl: true
    });
  }
}
```
