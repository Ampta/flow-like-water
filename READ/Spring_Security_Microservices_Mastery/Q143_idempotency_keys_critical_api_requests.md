# Q143: Ensuring Idempotency for critical API requests (Payments / Bookings)?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A customer submitted an online credit card payment of \$250. Due to a momentary network timeout, the browser re-sent the HTTP `POST /api/v1/payments` request. Because the payment API lacked idempotency protection, the backend processed both transactions, charging the customer's card **twice** (\$500 total). 

Implementing the **Idempotency Key Pattern** with Redis atomic locks (`SET key NX EX 86400`) guaranteed that duplicate requests return the cached original response without re-executing the payment.

---

### 2. Concept & Architecture

```
┌──────────────────────── Idempotency Key Workflow ────────────────────────┐
│                                                                          │
│   Client sends POST /api/v1/payments                                     │
│   Header: Idempotency-Key: 7b84f2c0-82a1-432d-9b62-0e9f1a23c4d5           │
│                 │                                                        │
│                 ▼                                                        │
│   [ Spring Boot IdempotencyFilter ]                                      │
│   Redis: SET idempotency:7b84f2c0... "PROCESSING" NX EX 120              │
│                 │                                                        │
│        ┌────────┴────────────────────────┐                               │
│        ▼ Key set successfully            ▼ Key ALREADY exists!           │
│   Proceed with Payment Logic             1. If "PROCESSING": Return 409  │
│   Cache response in Redis                2. If "COMPLETED": Return cached│
│   Return 200 OK with Receipt                receipt immediately!         │
│                                                                          │
└──────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.payment.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;
    private static final String IDEMPOTENCY_HEADER = "Idempotency-Key";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!"POST".equalsIgnoreCase(request.getMethod()) || !request.getRequestURI().startsWith("/api/v1/payments")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idempotencyKey = request.getHeader(IDEMPOTENCY_HEADER);
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("{\"error\": \"Missing required Idempotency-Key header\"}");
            return;
        }

        String redisKey = "idempotency:" + idempotencyKey;
        // Atomic SET if Not Exists with 24h expiration
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(redisKey, "PROCESSING", Duration.ofHours(24));

        if (Boolean.FALSE.equals(isNew)) {
            response.setStatus(HttpStatus.CONFLICT.value());
            response.getWriter().write("{\"error\": \"Duplicate request detected. Transaction is already processing or completed.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, preventing duplicate submissions requires:
1. Generating a unique UUID `Idempotency-Key` when the payment form is opened.
2. A custom directive `[disableOnSubmit]` that disables the button the instant it is clicked.
3. If a network timeout occurs, re-submitting with the **same** idempotency key so the server recognizes the retry.

---

### 2. Production Code (Angular 17+ Standalone)

#### Idempotent Payment Button Component (`payment-submit.component.ts`)
```typescript
import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-payment-submit',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="payment-box">
      <button (click)="submitPayment()" [disabled]="isSubmitting()" class="btn btn-success">
        {{ isSubmitting() ? 'Processing Payment...' : 'Pay $250.00' }}
      </button>

      @if (receipt()) {
        <div class="alert alert-success mt-3">Receipt ID: {{ receipt() }}</div>
      }
    </div>
  `
})
export class PaymentSubmitComponent {
  private readonly http = inject(HttpClient);

  readonly isSubmitting = signal(false);
  readonly receipt = signal<string | null>(null);

  // Generate unique idempotency key per checkout session
  private idempotencyKey = crypto.randomUUID();

  submitPayment(): void {
    this.isSubmitting.set(true);

    this.http.post<{ transactionId: string }>(
      '/api/v1/payments/charge',
      { amount: 250.00 },
      { headers: { 'Idempotency-Key': this.idempotencyKey } }
    ).subscribe({
      next: (res) => {
        this.receipt.set(res.transactionId);
        this.isSubmitting.set(false);
      },
      error: () => this.isSubmitting.set(false)
    });
  }
}
```
