# Q139: Propagating JWT Bearer tokens between microservices via Feign RequestInterceptor?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise platform implemented microservices where `OrderService` called `PaymentService` via OpenFeign. While the user sent a valid JWT to `OrderService`, the Feign client made an unauthenticated HTTP call to `PaymentService`. `PaymentService` rejected the internal request with `401 Unauthorized`. 

Developers initially considered passing the JWT manually as a method parameter across 15 layers of code. Implementing a **Feign `RequestInterceptor`** automated token extraction from `RequestContextHolder` and attached it to all outgoing templates transparently.

---

### 2. Concept & Architecture

```
┌──────────────────────── Token Propagation Architecture ────────────────────────┐
│                                                                                │
│  [ Incoming HTTP Request: Authorization: Bearer eyJhbGci... ]                  │
│             │                                                                  │
│             ▼                                                                  │
│  [ RequestContextHolder (ThreadLocal) ] ──► Stores HttpServletRequest          │
│                                                                                │
│  [ Feign Client Invocation ]                                                   │
│             │                                                                  │
│             ▼                                                                  │
│  [ JwtTokenRelayInterceptor.apply(RequestTemplate template) ]                  │
│       ├── 1. Reads incoming 'Authorization' header from RequestContextHolder   │
│       └── 2. template.header("Authorization", bearerToken)                     │
│             │                                                                  │
│             ▼ Outgoing HTTP Request with identical Bearer Token!               │
│  [ Downstream Microservice ] ──► Authenticates identity and enforces RBAC!     │
│                                                                                │
└────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.security.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class JwtTokenRelayInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

            // Relay the incoming Bearer token to the downstream service call
            if (authorization != null && authorization.startsWith("Bearer ")) {
                template.header(HttpHeaders.AUTHORIZATION, authorization);
            }
        }
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, token relay is completely invisible:
- The user authenticates once and receives a JWT.
- Angular attaches the token to API Gateway requests.
- The backend takes care of end-to-end token propagation across the microservices mesh.

---

### 2. Production Code (Angular 17+ Standalone)

#### Global Bearer Interceptor (`auth-bearer.interceptor.ts`)
```typescript
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authBearerInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getAccessToken();

  if (token && !req.headers.has('Authorization')) {
    return next(req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    }));
  }

  return next(req);
};
```
