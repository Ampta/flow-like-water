# Q114: How do you integrate OAuth2 in Spring Boot application?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise organization with 50+ microservices wanted to decommission its legacy proprietary user database and standardize on an enterprise Identity Provider (Keycloak / Okta / Azure AD) using **OAuth2 and OpenID Connect (OIDC)**. Microservices needed to act as **OAuth2 Resource Servers**, validating cryptographically signed JWTs issued by the Identity Provider via JSON Web Key Sets (JWKS) without making synchronous validation calls to the Auth server for each incoming request.

---

### 2. Concept & Architecture

```
┌──────────────────────────────── OAuth2 / OIDC Architecture ────────────────────────────────┐
│                                                                                            │
│   [ Angular SPA ] ──► (1) Authorization Code + PKCE ──► [ Keycloak / Okta Auth Server ]    │
│          ▲                                                               │                 │
│          │                                                               ▼                 │
│          └────────────── (2) Issues Signed JWT (Access Token) ───────────┘                 │
│                                                                                            │
│   [ Angular SPA ] ──► (3) GET /api/v1/orders (Bearer JWT) ──► [ Spring Boot Resource Server]│
│                                                                       │                    │
│                                           (4) Validates Signature     ▼                    │
│                                           against JWKS cached from Identity Provider       │
│                                           https://auth.corp.com/protocol/openid-connect/certs
│                                                                                            │
└────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Application Configuration (`application.yml`)
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://auth.enterprise.com/realms/production
          jwk-set-uri: https://auth.enterprise.com/realms/production/protocol/openid-connect/certs
```

#### 2. Resource Server Configuration with Keycloak Role Converter (`OAuth2ResourceServerConfig.java`)
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class OAuth2ResourceServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/public/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }

    /**
     * Extracts roles from Keycloak's nested JSON claim: realm_access.roles
     */
    private static class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        @SuppressWarnings("unchecked")
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            if (realmAccess == null || realmAccess.isEmpty()) {
                return Collections.emptyList();
            }
            List<String> roles = (List<String>) realmAccess.get("roles");
            return roles.stream()
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toList());
        }
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
Modern SPAs must authenticate via OAuth2 using the **Authorization Code Flow with Proof Key for Code Exchange (PKCE)**:
1. Generate cryptographic `code_verifier` and SHA-256 `code_challenge`.
2. Redirect user to Keycloak login page.
3. Handle redirect back to Angular with the `code`, and trade it for tokens.

---

### 2. Production Code (Angular 17+ Standalone)

#### Native OAuth2 PKCE Service (`oauth2-pkce.service.ts`)
```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OAuth2PkceService {
  private readonly http = inject(HttpClient);

  private readonly clientId = 'angular-enterprise-app';
  private readonly authEndpoint = 'https://auth.enterprise.com/realms/production/protocol/openid-connect/auth';
  private readonly tokenEndpoint = 'https://auth.enterprise.com/realms/production/protocol/openid-connect/token';
  private readonly redirectUri = window.location.origin + '/auth/callback';

  async initiateLogin(): Promise<void> {
    const verifier = this.generateRandomString(64);
    sessionStorage.setItem('pkce_verifier', verifier);

    const challenge = await this.generateCodeChallenge(verifier);

    const authUrl = `${this.authEndpoint}?response_type=code` +
      `&client_id=${encodeURIComponent(this.clientId)}` +
      `&redirect_uri=${encodeURIComponent(this.redirectUri)}` +
      `&scope=${encodeURIComponent('openid profile email')}` +
      `&code_challenge=${encodeURIComponent(challenge)}` +
      `&code_challenge_method=S256`;

    window.location.href = authUrl;
  }

  handleCallback(code: string): Observable<{ access_token: string; refresh_token: string }> {
    const verifier = sessionStorage.getItem('pkce_verifier') || '';
    
    const body = new HttpParams()
      .set('grant_type', 'authorization_code')
      .set('client_id', this.clientId)
      .set('redirect_uri', this.redirectUri)
      .set('code', code)
      .set('code_verifier', verifier);

    return this.http.post<{ access_token: string; refresh_token: string }>(this.tokenEndpoint, body.toString(), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    }).pipe(
      tap(tokens => {
        sessionStorage.removeItem('pkce_verifier');
        localStorage.setItem('auth_token', tokens.access_token);
      })
    );
  }

  private generateRandomString(length: number): string {
    const charset = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~';
    let result = '';
    const values = new Uint8Array(length);
    crypto.getRandomValues(values);
    for (let i = 0; i < length; i++) {
      result += charset[values[i] % charset.length];
    }
    return result;
  }

  private async generateCodeChallenge(verifier: string): Promise<string> {
    const encoder = new TextEncoder();
    const data = encoder.encode(verifier);
    const digest = await crypto.subtle.digest('SHA-256', data);
    return btoa(String.fromCharCode(...new Uint8Array(digest)))
      .replace(/\+/g, '-')
      .replace(/\//g, '_')
      .replace(/=+$/, '');
  }
}
```
