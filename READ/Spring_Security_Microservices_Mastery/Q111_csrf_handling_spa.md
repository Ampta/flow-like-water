# Q111: What is CSRF and how is it handled in Spring Security?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A digital banking web application used browser cookies to persist user sessions. An attacker tricked an authenticated user into visiting a malicious phishing website containing a hidden auto-submitting form:
```html
<form action="https://bank.com/api/v1/transfers" method="POST">
  <input type="hidden" name="recipient" value="attacker_account" />
  <input type="hidden" name="amount" value="50000" />
</form>
<script>document.forms[0].submit();</script>
```
Because the user's browser automatically attached the ambient session cookies, the bank processed the unauthorized transfer! 

Implementing **CSRF Protection** via the **Synchronizer Token Pattern** and configuring Spring Security to generate a secure `XSRF-TOKEN` cookie readable by Angular defeated this exploit completely.

---

### 2. Concept & Architecture

```
┌───────────────────────────────────── CSRF Defense Architecture ─────────────────────────────────────┐
│                                                                                                     │
│  1. Angular loads initial page ──► Spring Security generates CSRF token.                           │
│  2. CookieCsrfTokenRepository sets cookie: 'XSRF-TOKEN' (HttpOnly=false so Angular can read it).    │
│  3. Angular HttpClient automatically reads cookie 'XSRF-TOKEN' and attaches header 'X-XSRF-TOKEN'  │
│     on mutating HTTP requests (POST, PUT, DELETE, PATCH).                                           │
│  4. CsrfFilter compares:                                                                            │
│       Cookie Token  <====== MUST MATCH ======>  HTTP Header 'X-XSRF-TOKEN'                          │
│                                                                                                     │
│  * If an attacker hosts a malicious form, the browser cannot read the cross-origin XSRF cookie     │
│    due to the Same-Origin Policy (SOP), and cannot populate the required X-XSRF-TOKEN header!       │
│                                                                                                     │
└─────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

#### When to Disable CSRF:
- **Stateless REST APIs using Authorization: Bearer <token>** stored in memory or Web Storage DO NOT require CSRF protection because browsers DO NOT automatically attach Bearer headers across origins.
- **APIs using Cookies (Session Cookies or HttpOnly Refresh Tokens)** MUST have CSRF enabled!

---

### 3. Production Code (Spring Boot 3.x)

#### Cookie-Based CSRF Configuration for SPAs (`CsrfSecurityConfig.java`)
```java
package com.enterprise.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.*;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.Supplier;

@Configuration
public class CsrfSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        tokenRepository.setPath("/");

        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
        // Set to null to resolve raw tokens without XOR masking for SPA header compatibility
        delegate.setCsrfRequestAttributeName(null);

        return http
            .csrf(csrf -> csrf
                .csrfTokenRepository(tokenRepository)
                .csrfTokenRequestHandler(delegate)
            )
            // Register filter to ensure the CSRF cookie is populated on first GET request
            .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .build();
    }

    /**
     * Spring Security 6 defers loading the CSRF token until it is requested.
     * This filter forces token resolution so the cookie is sent to Angular on the first request.
     */
    private static class CsrfCookieFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            if (csrfToken != null) {
                // Accessing the token forces Spring to persist it to the response cookie
                csrfToken.getToken();
            }
            filterChain.doFilter(request, response);
        }
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
Angular has **first-class native support** for CSRF/XSRF:
- Angular's `HttpClient` automatically looks for a cookie named `XSRF-TOKEN`.
- On all mutating requests (`POST`, `PUT`, `DELETE`, `PATCH`), it copies that value into an HTTP header named `X-XSRF-TOKEN`.
- When connecting across subdomains, we configure `withXsrfConfiguration()` explicitly in `app.config.ts`.

---

### 2. Production Code (Angular 17+ Standalone)

#### 1. Configuring Angular HttpClient with XSRF Support (`app.config.ts`)
```typescript
import { ApplicationConfig } from '@angular/core';
import { provideHttpClient, withXsrfConfiguration } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',      // Cookie name set by Spring Boot CookieCsrfTokenRepository
        headerName: 'X-XSRF-TOKEN'    // Header expected by Spring Security CsrfFilter
      })
    )
  ]
};
```

#### 2. Transfer Service Executing Mutating Calls (`transfer.service.ts`)
```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TransferRequest {
  recipientAccount: string;
  amount: number;
}

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  private readonly http = inject(HttpClient);

  executeTransfer(request: TransferRequest): Observable<{ transactionId: string }> {
    // Angular automatically extracts the 'XSRF-TOKEN' cookie and sets 'X-XSRF-TOKEN' header!
    return this.http.post<{ transactionId: string }>('/api/v1/transfers', request);
  }
}
```
