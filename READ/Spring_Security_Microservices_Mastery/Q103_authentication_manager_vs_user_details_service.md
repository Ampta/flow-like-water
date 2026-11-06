# Q103: What is the difference between AuthenticationManager and UserDetailsService?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
During a Black Friday spike in an e-commerce platform, the authentication service crashed. Investigation revealed that developers had placed database updates, credential hashing, login-attempt counters, and account lockout logic directly inside `UserDetailsService.loadUserByUsername()`. Because `loadUserByUsername()` is only supposed to retrieve read-only user data, mixing business verification logic caused transaction deadlocks, connection pool starvation, and false password mismatch alerts. 

Separating concerns by delegating verification to `AuthenticationManager` and restricting `UserDetailsService` strictly to fetching principal metadata restored stability and sub-10ms authentication latency.

---

### 2. Concept & Architecture

```
                                [ Authentication Request (username, rawPassword) ]
                                                        │
                                                        ▼
┌───────────────────────────────────────── AuthenticationManager ─────────────────────────────────────────┐
│                                                                                                         │
│  The Orchestrator / Engine: Evaluates credentials, checks account state, and issues authenticated token │
│  Interface: Authentication authenticate(Authentication authentication) throws AuthenticationException; │
│                                                                                                         │
│  Common Implementation: ProviderManager                                                                 │
│  Iterates over configured AuthenticationProviders:                                                      │
│                                                                                                         │
│        ┌───────────────────────── DaoAuthenticationProvider ─────────────────────────┐                  │
│        │                                                                             │                  │
│        │  Step 1: Retrieve user details                                              │                  │
│        │          UserDetailsService.loadUserByUsername(username)                    │                  │
│        │                   │                                                         │                  │
│        │                   ▼                                                         │                  │
│        │          Returns UserDetails (User entity, hashed password, authorities)    │                  │
│        │                                                                             │                  │
│        │  Step 2: Pre-authentication account checks                                  │                  │
│        │          - isAccountNonLocked()                                             │                  │
│        │          - isEnabled()                                                      │                  │
│        │          - isAccountNonExpired()                                            │                  │
│        │                                                                             │                  │
│        │  Step 3: Credential verification                                            │                  │
│        │          passwordEncoder.matches(rawPassword, userDetails.getPassword())    │                  │
│        │                                                                             │                  │
│        │  Step 4: Post-authentication checks                                         │                  │
│        │          - isCredentialsNonExpired()                                        │                  │
│        │                                                                             │                  │
│        │  Step 5: Return fully-authenticated UsernamePasswordAuthenticationToken     │                  │
│        └─────────────────────────────────────────────────────────────────────────────┘                  │
│                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

#### Comparison Matrix:
| Dimension | `AuthenticationManager` | `UserDetailsService` |
|---|---|---|
| **Role** | The decision engine / orchestrator. | The data access adapter. |
| **Method** | `authenticate(Authentication auth)` | `loadUserByUsername(String username)` |
| **Responsibilities** | Validates password, account lock status, expiry, MFA. | Strictly queries the database/LDAP to return a `UserDetails` object. |
| **Exceptions Thrown** | `BadCredentialsException`, `LockedException`, `DisabledException` | `UsernameNotFoundException` |
| **Modifies State?** | Can trigger audit events, record failed login counts. | Strictly read-only data loader. |

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Read-Only Data Loader (`JpaUserDetailsService.java`)
```java
package com.enterprise.security.service;

import com.enterprise.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameWithRoles(username)
            .map(user -> new User(
                user.getUsername(),
                user.getPasswordHash(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                    .collect(Collectors.toSet())
            ))
            .orElseThrow(() -> new UsernameNotFoundException("Principal not found: " + username));
    }
}
```

#### 2. Authentication Configuration (`AuthenticationConfig.java`)
```java
package com.enterprise.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(true); // Mitigates user enumeration attacks
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
The frontend interacts directly with the consequences of this separation:
- If `UserDetailsService` fails to find a user or `AuthenticationManager` detects wrong credentials, the backend responds with a unified `401 Unauthorized` (mitigating user enumeration).
- If the account is locked or expired, the backend returns a specific RFC 7807 `ProblemDetail` response. The Angular application must parse this payload and present the user with an actionable message (e.g. "Account locked: contact IT support" or "Password expired: redirecting to reset").

---

### 2. Production Code (Angular 17+ Standalone)

#### 1. Login Component with ProblemDetail Handling (`login.component.ts`)
```typescript
import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../core/services/auth.service';

interface ProblemDetail {
  type: string;
  title: string;
  status: number;
  detail: string;
  errorCode?: 'BAD_CREDENTIALS' | 'ACCOUNT_LOCKED' | 'PASSWORD_EXPIRED';
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="login-container">
      <h2>Enterprise Portal Login</h2>

      @if (errorMessage()) {
        <div class="alert alert-danger" [class.alert-warning]="isAccountLocked()">
          {{ errorMessage() }}
        </div>
      }

      <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
        <div class="form-group">
          <label for="username">Username / Email</label>
          <input id="username" type="text" formControlName="username" class="form-control" />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input id="password" type="password" formControlName="password" class="form-control" />
        </div>

        <button type="submit" [disabled]="loginForm.invalid || isLoading()" class="btn btn-primary">
          {{ isLoading() ? 'Verifying...' : 'Sign In' }}
        </button>
      </form>
    </div>
  `
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly http = inject(HttpClient);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly isLoading = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly isAccountLocked = signal(false);

  readonly loginForm = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.isLoading.set(true);
    this.errorMessage.set(null);
    this.isAccountLocked.set(false);

    this.http.post<{ token: string; username: string; roles: string[] }>(
      '/api/v1/auth/authenticate',
      this.loginForm.getRawValue()
    ).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        this.authService.setSession(response);
        this.router.navigate(['/dashboard']);
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        const problem: ProblemDetail = error.error;

        if (problem?.errorCode === 'ACCOUNT_LOCKED') {
          this.isAccountLocked.set(true);
          this.errorMessage.set('Account is locked due to multiple failed attempts. Please contact Security Admin.');
        } else if (problem?.errorCode === 'PASSWORD_EXPIRED') {
          this.router.navigate(['/auth/reset-password'], { queryParams: { user: this.loginForm.controls.username.value } });
        } else {
          this.errorMessage.set('Invalid credentials provided.');
        }
      }
    });
  }
}
```
