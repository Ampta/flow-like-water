# Q116: What are common ways to secure REST endpoints?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A loan origination REST API was targeted by a multi-vector attack: automated credential stuffing, parameter tampering (negative loan amounts), and denial-of-service via massive JSON payloads. Relying only on authentication was insufficient. 

Securing enterprise REST endpoints required a **Defense-in-Depth** strategy combining:
1. Strict DTO validation (`@Valid`).
2. Rate Limiting via Token Bucket algorithm (Bucket4j / Redis).
3. Payload size caps.
4. Standardized exception masking via RFC 7807 `ProblemDetail`.

---

### 2. Concept & Architecture

```
                                  [ Incoming Client REST Request ]
                                                  │
                                                  ▼
┌────────────────────────────────────── DEFENSE IN DEPTH ──────────────────────────────────────┐
│                                                                                              │
│  Layer 1: Network & TLS 1.3 Inspection                                                       │
│  Layer 2: Rate Limiting Filter (Bucket4j / Redis: Max 100 req/min per IP) ──► 429 Too Many   │
│  Layer 3: Spring Security FilterChain (JWT Verification & CORS) ─────────────► 401 / 403     │
│  Layer 4: Payload Size Enforcer (Max 1MB) ───────────────────────────────────► 413 Payload   │
│  Layer 5: Hibernate Validator (@Valid, @Pattern, @Min) ──────────────────────► 400 Bad Req   │
│  Layer 6: Method Security (@PreAuthorize) ───────────────────────────────────► 403 Forbidden │
│                                                                                              │
└─────────────────────────────────────────────────┬────────────────────────────────────────────┘
                                                  │
                                                  ▼
                                      [ Core Business Logic ]
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Rate Limiting Filter using Bucket4j (`RateLimitingFilter.java`)
```java
package com.enterprise.security.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // Allow 50 requests per minute with burst capacity of 50
        Bandwidth limit = Bandwidth.classic(50, Refill.greedy(50, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(clientIp, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Rate limit exceeded. Try again in 60 seconds.\"}");
        }
    }
}
```

#### 2. Validated REST Controller (`LoanController.java`)
```java
package com.enterprise.security.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    public record LoanApplicationRequest(
        @NotNull(message = "SSN is mandatory")
        @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{4}$", message = "Invalid SSN format")
        String ssn,

        @NotNull(message = "Loan amount is required")
        @DecimalMin(value = "1000.00", message = "Minimum loan amount is $1,000.00")
        BigDecimal requestedAmount
    ) {}

    @PostMapping("/apply")
    @PreAuthorize("hasRole('BORROWER')")
    public ResponseEntity<String> submitLoanApplication(@Valid @RequestBody LoanApplicationRequest request) {
        // Business logic execution
        return ResponseEntity.ok("Application submitted successfully");
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, securing REST interaction involves:
1. **Debounced User Inputs:** Preventing rate-limit exhaustion by applying `debounceTime(300)` and `distinctUntilChanged()` on form searches.
2. **Client-side Form Validation:** Validating inputs before transmission to avoid unnecessary roundtrips.
3. **PII Masking:** Preventing sensitive data leakage on screen.

---

### 2. Production Code (Angular 17+ Standalone)

#### Loan Application Component (`loan-application.component.ts`)
```typescript
import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-loan-application',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="loan-card">
      <h2>Apply for a Loan</h2>

      @if (serverError()) {
        <div class="alert alert-danger">{{ serverError() }}</div>
      }

      <form [formGroup]="loanForm" (ngSubmit)="submit()">
        <div class="form-group">
          <label>Social Security Number (SSN)</label>
          <input type="text" formControlName="ssn" placeholder="XXX-XX-XXXX" class="form-control" />
          @if (loanForm.controls.ssn.touched && loanForm.controls.ssn.errors) {
            <span class="text-danger">Valid SSN required (XXX-XX-XXXX).</span>
          }
        </div>

        <div class="form-group">
          <label>Requested Amount ($)</label>
          <input type="number" formControlName="requestedAmount" class="form-control" />
          @if (loanForm.controls.requestedAmount.touched && loanForm.controls.requestedAmount.errors) {
            <span class="text-danger">Minimum amount is $1,000.00.</span>
          }
        </div>

        <button type="submit" [disabled]="loanForm.invalid || isSubmitting()" class="btn btn-primary">
          {{ isSubmitting() ? 'Submitting...' : 'Apply Now' }}
        </button>
      </form>
    </div>
  `
})
export class LoanApplicationComponent {
  private readonly fb = inject(FormBuilder);
  private readonly http = inject(HttpClient);

  readonly isSubmitting = signal(false);
  readonly serverError = signal<string | null>(null);

  readonly loanForm = this.fb.nonNullable.group({
    ssn: ['', [Validators.required, Validators.pattern(/^\d{3}-\d{2}-\d{4}$/)]],
    requestedAmount: [5000, [Validators.required, Validators.min(1000)]]
  });

  submit(): void {
    if (this.loanForm.invalid) return;

    this.isSubmitting.set(true);
    this.serverError.set(null);

    this.http.post('/api/v1/loans/apply', this.loanForm.getRawValue()).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        alert('Loan application submitted!');
      },
      error: (err: HttpErrorResponse) => {
        this.isSubmitting.set(false);
        if (err.status === 429) {
          this.serverError.set('Too many requests. Please wait a minute before trying again.');
        } else {
          this.serverError.set(err.error?.message || 'Submission error');
        }
      }
    });
  }
}
```
