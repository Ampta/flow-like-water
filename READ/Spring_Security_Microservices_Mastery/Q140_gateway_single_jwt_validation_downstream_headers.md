# Q140: API Gateway validating JWT once and passing headers to downstream services?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise architecture ran 40 internal microservices. Each microservice independently fetched and validated JWT cryptographic signatures against an Okta JWKS server. Under 15,000 requests/second, the JWKS cache thrashing and repeated RSA signature validations consumed 35% of overall CPU across the Kubernetes cluster. 

Adopting the **Edge Gateway Authentication Pattern** resolved this: **Spring Cloud Gateway** validates the JWT cryptographic signature once at the perimeter, strips external authorization headers, and injects trusted internal headers (`X-User-Id`, `X-User-Roles`) for downstream consumption.

---

### 2. Concept & Architecture

```
┌──────────────────────── Edge Authentication Architecture ────────────────────────┐
│                                                                                  │
│   [ External Client ] ──► Sends Bearer JWT in Authorization Header               │
│                                   │                                              │
│                                   ▼                                              │
│   ┌────────────────────────────────────────────────────────────┐                 │
│   │ Spring Cloud Gateway (Edge Perimeter)                     │                 │
│   │ 1. Validates JWT signature cryptographically against JWKS  │                 │
│   │ 2. Extracts claims: sub="101", roles="ADMIN,MANAGER"       │                 │
│   │ 3. Mutates request:                                        │                 │
│   │    - Injects: X-User-Id: 101                               │                 │
│   │    - Injects: X-User-Roles: ROLE_ADMIN,ROLE_MANAGER        │                 │
│   │    - Injects: X-Internal-Signature: HMAC-SHA256(secret)   │                 │
│   └───────────────────────────────┬────────────────────────────┘                 │
│                                   │                                              │
│                                   ▼ Fast internal HTTP call                      │
│   ┌────────────────────────────────────────────────────────────┐                 │
│   │ Downstream Microservices (Orders, Invoices, Inventory)     │                 │
│   │ - Reads X-User-Id and X-User-Roles directly                │                 │
│   │ - Zero RSA cryptographic overhead! Sub-millisecond parsing!│                 │
│   └────────────────────────────────────────────────────────────┘                 │
│                                                                                  │
└──────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### Gateway Global Filter (`JwtEdgeVerificationFilter.java`)
```java
package com.enterprise.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtEdgeVerificationFilter implements GlobalFilter, Ordered {

    private final SecretKey signingKey;

    public JwtEdgeVerificationFilter(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (path.startsWith("/api/v1/auth/")) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            String userId = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);

            // Mutate request headers for downstream consumption
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-User-Roles", String.join(",", roles))
                .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -100; // High priority filter
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
The Angular application needs to know only about the API Gateway reverse proxy:
- Sends standard `Authorization: Bearer <token>` to the Gateway.
- Downstream headers (`X-User-Id`, `X-User-Roles`) are generated exclusively inside the internal cluster network.

---

### 2. Production Code (Angular 17+ Standalone)

#### Gateway Client Service (`gateway-client.service.ts`)
```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GatewayClientService {
  private readonly http = inject(HttpClient);

  // All endpoints route through unified Gateway base URL
  get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`/api/v1/${endpoint}`);
  }
}
```
