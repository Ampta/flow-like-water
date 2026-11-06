# Q115: How do you handle stateless authentication in Spring Security?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A cloud-native flight reservation platform deployed behind an AWS Application Load Balancer with round-robin traffic routing experienced erratic session drops. Users would add flights to their itinerary on Instance A, but their next checkout request hit Instance B, which threw a `401 Unauthorized` because the user's session existed only in Instance A's local JVM heap. 

Converting the microservices to **100% Stateless Authentication** using `SessionCreationPolicy.STATELESS` decoupled application state from server memory, enabling seamless horizontal auto-scaling from 2 to 50 nodes without sticky sessions.

---

### 2. Concept & Architecture

```
┌──────────────────────────────── Stateful vs Stateless Session ────────────────────────────────┐
│                                                                                               │
│  STATEFUL (Legacy):                                                                           │
│  [ Client ] ──► [ Load Balancer ] ──► [ Instance A (Stores JSESSIONID in Heap) ]              │
│                                  ──► [ Instance B ] (401 Unauthorized - Session Missing!)     │
│                                                                                               │
│  STATELESS (Cloud-Native):                                                                    │
│  [ Client ] ──► Sends cryptographically self-contained Bearer JWT in HTTP Header             │
│                       │                                                                       │
│                       ▼                                                                       │
│               [ Load Balancer ]                                                               │
│                /             \                                                               │
│               ▼               ▼                                                               │
│        [ Instance A ]   [ Instance B ]                                                        │
│        Both instances validate JWT cryptographically using the shared public key/secret       │
│        Zero memory cross-talk required! Horizontally scalable!                                │
│                                                                                               │
└───────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.security.config;

import com.enterprise.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class StatelessSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // 1. Disable CSRF since Bearer tokens are immune to ambient CSRF attacks
            .csrf(csrf -> csrf.disable())
            
            // 2. Instruct Spring Security NEVER to create or use an HttpSession
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 3. Security context is loaded fresh per request from the JWT Bearer header
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
In a stateless architecture, the client must manage token persistence responsibly.
- Storing access tokens in JavaScript memory (via an Angular service signal) protects them against XSS token harvesting.
- The refresh token is kept in an `HttpOnly` cookie or persisted in local storage only when offline persistence is required.

---

### 2. Production Code (Angular 17+ Standalone)

#### In-Memory Stateless Token Storage Service (`token-storage.service.ts`)
```typescript
import { Injectable, signal, computed } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  // Access token held strictly in memory - immune to XSS local storage theft!
  private readonly accessTokenSignal = signal<string | null>(null);

  readonly hasToken = computed(() => !!this.accessTokenSignal());

  getAccessToken(): string | null {
    return this.accessTokenSignal();
  }

  setAccessToken(token: string): void {
    this.accessTokenSignal.set(token);
  }

  clear(): void {
    this.accessTokenSignal.set(null);
  }
}
```
