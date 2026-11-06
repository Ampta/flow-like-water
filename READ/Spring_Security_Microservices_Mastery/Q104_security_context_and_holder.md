# Q104: What is a SecurityContext and SecurityContextHolder?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In a financial services application, an asynchronous batch job (`@Async`) was triggered to generate monthly customer audit statements. The audit service queried `SecurityContextHolder.getContext().getAuthentication().getName()` to log which officer triggered the generation. In production, this batch job threw intermittent `NullPointerExceptions` because `@Async` runs on a separate thread from the HTTP request thread, where the `ThreadLocal` storage of `SecurityContextHolder` was empty! Worse, when threads were returned to the thread pool without clearing the context, officer credentials leaked across unrelated jobs. 

Understanding the relationship between `SecurityContext`, `SecurityContextHolder`, and thread propagation strategies was essential to fixing this critical compliance defect.

---

### 2. Concept & Architecture

```
┌────────────────────────────────────────────── SecurityContextHolder ──────────────────────────────────────────────┐
│                                                                                                                  │
│  Strategy: ThreadLocalSecurityContextHolderStrategy (Default)                                                    │
│  Stores details in a ThreadLocal<SecurityContext> object specific to the current thread.                         │
│                                                                                                                  │
│    Thread-1 (HTTP Worker)                                                                                        │
│    ┌─────────────────────────────────── SecurityContext ───────────────────────────────────┐                     │
│    │                                                                                       │                     │
│    │   Authentication Object:                                                              │                     │
│    │   ├── Principal: UserDetails ("officer_john")                                         │                     │
│    │   ├── Credentials: [PROTECTED / NULL]                                                 │                     │
│    │   ├── Authorities: [ROLE_AUDITOR, ROLE_USER]                                          │                     │
│    │   └── Authenticated: true                                                             │                     │
│    │                                                                                       │                     │
│    └───────────────────────────────────────────────────────────────────────────────────────┘                     │
│                                                                                                                  │
└──────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

#### Storage Strategies:
1. `MODE_THREADLOCAL` (Default): Stores security context in the executing thread's `ThreadLocal`. Isolated, secure, but does not propagate to child threads.
2. `MODE_INHERITABLETHREADLOCAL`: Propagates security context to child threads spawned via `Thread.start()`. However, it fails with pooled executor services (e.g. `ThreadPoolTaskExecutor`) because pooled threads are reused rather than spawned per task!
3. `MODE_GLOBAL`: All threads share a single `SecurityContext` (useful strictly for standalone desktop Swing/JavaFX apps, never web servers).

#### Thread Propagation in Async Web Applications:
To propagate context to pooled `@Async` or `CompletableFuture` tasks safely, wrap the `TaskExecutor` in a `DelegatingSecurityContextAsyncTaskExecutor`.

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Async Configuration with SecurityContext Propagation (`AsyncSecurityConfig.java`)
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
public class AsyncSecurityConfig {

    @Bean(name = "auditTaskExecutor")
    public TaskExecutor auditTaskExecutor() {
        ThreadPoolTaskExecutor delegate = new ThreadPoolTaskExecutor();
        delegate.setCorePoolSize(10);
        delegate.setMaxPoolSize(25);
        delegate.setQueueCapacity(200);
        delegate.setThreadNamePrefix("audit-async-");
        delegate.initialize();

        // Wraps the executor to automatically copy SecurityContext to worker threads
        // and clean it up when the task finishes!
        return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
    }
}
```

#### 2. Thread-Safe Security Context Accessor Utility (`SecurityUtils.java`)
```java
package com.enterprise.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return Optional.empty();
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return Optional.of(userDetails.getUsername());
        } else if (principal instanceof String username) {
            return Optional.of(username);
        }

        return Optional.empty();
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
The frontend equivalent of the `SecurityContextHolder` is a **Global Client Session Context**. 
In an enterprise Angular application, the user's authenticated context must:
1. Be stored in memory using Angular Signals to ensure reactive view updates.
2. Be synchronized across multiple browser tabs (e.g. if the user clicks "Log Out" in Tab 1, Tab 2 should immediately invalidate its local context without requiring a manual refresh).
3. Be accessible to all services and guards without circular dependencies.

---

### 2. Production Code (Angular 17+ Standalone)

#### Multi-Tab Synchronized Session Context Service (`user-session-context.service.ts`)
```typescript
import { Injectable, signal, computed, inject, NgZone, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

export interface UserContext {
  userId: string;
  username: string;
  roles: string[];
  departmentId: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserSessionContextService implements OnDestroy {
  private readonly router = inject(Router);
  private readonly ngZone = inject(NgZone);

  // BroadcastChannel allows inter-tab communication within the same origin
  private readonly broadcastChannel = new BroadcastChannel('enterprise_security_channel');

  private readonly contextSignal = signal<UserContext | null>(this.restoreInitialContext());

  readonly userContext = computed(() => this.contextSignal());
  readonly isAuthenticated = computed(() => !!this.contextSignal());
  readonly username = computed(() => this.contextSignal()?.username ?? 'Anonymous');

  constructor() {
    this.initCrossTabSynchronization();
  }

  setContext(context: UserContext, authToken: string): void {
    localStorage.setItem('auth_token', authToken);
    localStorage.setItem('user_context', JSON.stringify(context));
    this.contextSignal.set(context);

    // Notify other open tabs
    this.broadcastChannel.postMessage({ type: 'LOGIN', context });
  }

  clearContext(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user_context');
    this.contextSignal.set(null);

    // Notify other open tabs
    this.broadcastChannel.postMessage({ type: 'LOGOUT' });
  }

  private initCrossTabSynchronization(): void {
    this.broadcastChannel.onmessage = (event) => {
      this.ngZone.run(() => {
        if (event.data?.type === 'LOGOUT') {
          this.contextSignal.set(null);
          this.router.navigate(['/auth/login'], { queryParams: { loggedOutOtherTab: 'true' } });
        } else if (event.data?.type === 'LOGIN') {
          this.contextSignal.set(event.data.context);
        }
      });
    };
  }

  private restoreInitialContext(): UserContext | null {
    const raw = localStorage.getItem('user_context');
    if (!raw) return null;
    try {
      return JSON.parse(raw) as UserContext;
    } catch {
      return null;
    }
  }

  ngOnDestroy(): void {
    this.broadcastChannel.close();
  }
}
```
