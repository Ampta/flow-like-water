# Q108: How do you secure REST APIs using JWT in Spring Security?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An e-commerce API suffered a security incident where revoked employee credentials continued accessing admin endpoints for 48 hours. The system used long-lived (24-hour) JWT access tokens without any refresh mechanism or revocation capability. 

The architecture had to be overhauled to use a dual-token system: **short-lived Access Tokens (15 minutes)** for stateless authorization, paired with **long-lived Refresh Tokens (7 days)** stored in Redis with revocation capabilities, coordinated through a custom `JwtAuthenticationFilter`.

---

### 2. Concept & Architecture

```
┌────────────────────────────────────── JWT Architecture ──────────────────────────────────────┐
│                                                                                              │
│   [ Client POST /api/v1/auth/login ]                                                         │
│               │                                                                              │
│               ▼                                                                              │
│   [ Backend validates credentials ] ──► Returns:                                             │
│       • Access Token (15 min expiry, signed HMAC-SHA256, contains roles & sub)              │
│       • Refresh Token (7 days expiry, opaque UUID saved in Redis)                            │
│                                                                                              │
│   [ Subsequent Requests: Authorization: Bearer <Access Token> ]                              │
│               │                                                                              │
│               ▼                                                                              │
│   [ JwtAuthenticationFilter ]                                                                │
│       ├── 1. Extract Bearer token from Authorization Header                                  │
│       ├── 2. Validate cryptographic signature via JJWT library                               │
│       ├── 3. Extract claims: username, roles, expiration                                     │
│       ├── 4. Check Redis token revocation blacklist                                          │
│       └── 5. Create UsernamePasswordAuthenticationToken and set SecurityContextHolder        │
│                                                                                              │
└──────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. JWT Service Implementation (`JwtService.java`)
```java
package com.enterprise.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long jwtExpirationMs;

    public JwtService(
        @Value("${application.security.jwt.secret-key}") String secretKey,
        @Value("${application.security.jwt.expiration-ms:900000}") long jwtExpirationMs // 15 mins
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        return Jwts.builder()
            .subject(userDetails.getUsername())
            .claim("roles", roles)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(signingKey)
            .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claimsResolver.apply(claims);
    }
}
```

#### 2. JWT Authentication Filter (`JwtAuthenticationFilter.java`)
```java
package com.enterprise.security.filter;

import com.enterprise.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
In Angular, managing JWTs requires a **Silent Refresh Queue**:
1. When an access token expires, API requests return `401 Unauthorized`.
2. Rather than immediately booting the user out, the Angular HTTP Interceptor captures the 401, locks outgoing requests using an RxJS `BehaviorSubject`, and triggers a refresh token call.
3. Once the new access token is obtained, all queued requests are replayed seamlessly.

---

### 2. Production Code (Angular 17+ Standalone)

#### JWT Refresh Interceptor with Request Locking (`jwt-auth.interceptor.ts`)
```typescript
import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { BehaviorSubject, Observable, catchError, filter, switchMap, take, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const jwtAuthInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<any> => {
  const authService = inject(AuthService);
  const token = authService.getAccessToken();

  let authReq = req;
  if (token) {
    authReq = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }

  return next(authReq).pipe(
    catchError((error) => {
      if (error instanceof HttpErrorResponse && error.status === 401 && !req.url.includes('/auth/refresh')) {
        return handle401Error(authReq, next, authService);
      }
      return throwError(() => error);
    })
  );
};

function handle401Error(req: HttpRequest<unknown>, next: HttpHandlerFn, authService: AuthService): Observable<any> {
  if (!isRefreshing) {
    isRefreshing = true;
    refreshTokenSubject.next(null);

    return authService.refreshToken().pipe(
      switchMap((newTokens) => {
        isRefreshing = false;
        authService.saveTokens(newTokens.accessToken, newTokens.refreshToken);
        refreshTokenSubject.next(newTokens.accessToken);
        return next(req.clone({ setHeaders: { Authorization: `Bearer ${newTokens.accessToken}` } }));
      }),
      catchError((err) => {
        isRefreshing = false;
        authService.logout();
        return throwError(() => err);
      })
    );
  } else {
    // If refresh already in progress, wait until new token is published
    return refreshTokenSubject.pipe(
      filter((token) => token !== null),
      take(1),
      switchMap((jwt) => next(req.clone({ setHeaders: { Authorization: `Bearer ${jwt}` } })))
    );
  }
}
```
