# Q121: Securing internal downstream microservices using JWT tokens?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise platform relied on "perimeter security": only the API Gateway validated JWTs, while internal microservices communicated unauthenticated over plain HTTP. An attacker exploited a file-upload vulnerability in a public CMS service to run a Server-Side Request Forgery (SSRF) attack, querying internal microservices (`http://payment-service:8080/api/v1/payments`) and draining accounts without needing valid credentials. 

Adopting a **Zero Trust Architecture** with the **Token Relay Pattern** via Spring Cloud OpenFeign ensured every internal service-to-service call carries and verifies the authenticated caller's cryptographic JWT.

---

### 2. Concept & Architecture

```
┌──────────────────────────────── Token Relay Pattern ────────────────────────────────┐
│                                                                                     │
│  [ Angular Client ] ──► (Authorization: Bearer <token>) ──► [ Spring Cloud Gateway ] │
│                                                                      │              │
│                                                                      ▼ Relay JWT    │
│                                                            [ Service A (Orders) ]   │
│                                                                      │              │
│                           Feign Client + RequestInterceptor          │              │
│                           Copies incoming JWT to outbound request    ▼ Relay JWT    │
│                                                            [ Service B (Payments) ] │
│                                                                      │              │
│                                                            Validates signature &    │
│                                                            evaluates @PreAuthorize  │
│                                                                                     │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Feign Token Relay Interceptor (`FeignClientInterceptor.java`)
```java
package com.enterprise.security.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authorization = request.getHeader(AUTHORIZATION_HEADER);

            // Propagate incoming Bearer token to downstream service
            if (authorization != null && authorization.startsWith("Bearer ")) {
                template.header(AUTHORIZATION_HEADER, authorization);
            }
        }
    }
}
```

#### 2. Downstream Security Configuration (`DownstreamSecurityConfig.java`)
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class DownstreamSecurityConfig {

    @Bean
    public SecurityFilterChain downstreamFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
            .build();
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, Zero Trust is completely transparent:
- Angular attaches the user's JWT to all API Gateway requests.
- The Gateway and downstream Feign clients propagate the token.
- If a downstream service rejects the relayed token due to role restrictions, the error propagates back as `403 Forbidden`, triggering Angular's security boundary.

---

### 2. Production Code (Angular 17+ Standalone)

#### Transparent Token Relay Interceptor (`token-relay.interceptor.ts`)
```typescript
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const tokenRelayInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getAccessToken();

  // If token exists, attach to outbound gateway request
  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }

  return next(req);
};
```
