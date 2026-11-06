# Q138: Feign Client failure resilience using Resilience4j Circuit Breakers?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An OpenFeign client in `AuthService` called a third-party SMS OTP provider. When the SMS vendor suffered an outage and hung requests for 30 seconds, 400 user login requests accumulated in Tomcat's thread pool, starving the authentication microservice of worker threads and crashing the login portal for all customers. 

Integrating **Resilience4j Circuit Breaker with a FallbackFactory** on the Feign client protected the system: when failure rates exceeded 50%, the circuit tripped to `OPEN`, immediately diverting calls to an email OTP fallback without blocking threads.

---

### 2. Concept & Architecture

```
┌───────────────────── Feign + Resilience4j FallbackFactory ─────────────────────┐
│                                                                                │
│  [ OpenFeign Client: SmsClient ]                                               │
│             │                                                                  │
│             ▼ Wrapped by Resilience4j Circuit Breaker                          │
│  State: CLOSED ──(50% calls fail / timeout)──► State: OPEN                     │
│                                                      │                         │
│                                                      ▼ Diverts immediately     │
│  [ SmsFallbackFactory.create(Throwable cause) ]                                │
│       ├── Logs root cause (e.g., SocketTimeoutException)                       │
│       └── Returns Fallback: Sends OTP via Email instead!                       │
│                                                                                │
│  * Result: Sub-10ms response time even when the SMS provider is completely down!│
│                                                                                │
└────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Feign Client with FallbackFactory (`SmsClient.java`)
```java
package com.enterprise.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "sms-service",
    url = "https://api.smsprovider.com",
    fallbackFactory = SmsFallbackFactory.class
)
public interface SmsClient {

    record SmsResponse(boolean delivered, String messageId) {}

    @PostMapping("/send-otp")
    SmsResponse sendOtp(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
```

#### 2. FallbackFactory Implementation (`SmsFallbackFactory.java`)
```java
package com.enterprise.auth.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsFallbackFactory implements FallbackFactory<SmsClient> {

    @Override
    public SmsClient create(Throwable cause) {
        return (phone, code) -> {
            log.warn("SMS Provider failed ({})! Diverting to fallback channel for phone: {}", 
                     cause.getMessage(), phone);
            // Fallback: Notify system to route OTP via Email or voice call
            return new SmsClient.SmsResponse(false, "FALLBACK_TRIGGERED");
        };
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, when the backend circuit breaker trips and returns a fallback response:
- Alert the user: *"SMS verification is currently experiencing delays. We have sent your verification code to your registered email address instead."*
- Provide a button to toggle verification channels manually.

---

### 2. Production Code (Angular 17+ Standalone)

#### Resilient Verification Component (`otp-verify.component.ts`)
```typescript
import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

interface OtpResponse {
  delivered: boolean;
  messageId: string;
}

@Component({
  selector: 'app-otp-verify',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="otp-card">
      <h3>Two-Factor Verification</h3>

      @if (channelNotice()) {
        <div class="alert alert-info">{{ channelNotice() }}</div>
      }

      <button (click)="requestOtp()" [disabled]="isRequesting()" class="btn btn-primary">
        {{ isRequesting() ? 'Sending Code...' : 'Request Verification Code' }}
      </button>
    </div>
  `
})
export class OtpVerifyComponent {
  private readonly http = inject(HttpClient);

  readonly isRequesting = signal(false);
  readonly channelNotice = signal<string | null>(null);

  requestOtp(): void {
    this.isRequesting.set(true);

    this.http.post<OtpResponse>('/api/v1/auth/request-otp', { phone: '+15550192834' }).subscribe({
      next: (res) => {
        this.isRequesting.set(false);
        if (res.messageId === 'FALLBACK_TRIGGERED') {
          this.channelNotice.set('SMS gateway busy. We sent your security code to your registered email instead!');
        } else {
          this.channelNotice.set('Code sent via SMS.');
        }
      },
      error: () => this.isRequesting.set(false)
    });
  }
}
```
