# Q144: Centralized Global Exception Handling across all microservices?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise platform had 30 microservices developed by different teams. Error responses were completely inconsistent: Service A returned `{ "error": "Not Found" }`, Service B returned `{ "errorMessage": "Invalid", "code": 400 }`, and Service C leaked raw Java stack traces containing database credentials! Angular frontend error parsers broke constantly. 

Standardizing on the **RFC 7807 `ProblemDetail` specification** (supported natively in Spring Boot 3.x) via a shared `@RestControllerAdvice` library unified all error responses across the microservices mesh with correlation IDs and timestamps.

---

### 2. Concept & Architecture

```
┌──────────────────────── Centralized Exception Handling ────────────────────────┐
│                                                                                │
│  [ Custom Exception Thrown: ResourceNotFoundException("Order 101 not found") ] │
│                                      │                                         │
│                                      ▼                                         │
│  [ Shared GlobalExceptionHandler: @RestControllerAdvice ]                     │
│       ├── 1. Generates RFC 7807 ProblemDetail instance                         │
│       ├── 2. Attaches timestamp & correlation ID                               │
│       └── 3. Returns standard JSON:                                            │
│              {                                                                 │
│                "type": "https://api.enterprise.com/errors/not-found",          │
│                "title": "Resource Not Found",                                  │
│                "status": 404,                                                  │
│                "detail": "Order 101 not found",                                │
│                "instance": "/api/v1/orders/101",                               │
│                "correlationId": "c4d5-82a1-9b62"                               │
│              }                                                                 │
│                                                                                │
└────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.common.exception;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Invalid Request Parameter");
        problem.setType(URI.create("https://api.enterprise.com/errors/bad-request"));
        enrichProblemDetail(problem);
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed for fields.");
        problem.setTitle("Validation Constraint Violation");
        problem.setType(URI.create("https://api.enterprise.com/errors/validation-error"));
        enrichProblemDetail(problem);
        return problem;
    }

    private void enrichProblemDetail(ProblemDetail problem) {
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("correlationId", MDC.get("correlationId"));
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, a single **Global HTTP Interceptor** parses RFC 7807 `ProblemDetail` structures, surfacing human-readable messages and correlation support codes to the user.

---

### 2. Production Code (Angular 17+ Standalone)

#### RFC 7807 Error Interceptor (`problem-detail.interceptor.ts`)
```typescript
import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export interface ProblemDetail {
  type: string;
  title: string;
  status: number;
  detail: string;
  correlationId?: string;
}

export const problemDetailInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.error && error.error.title && error.error.detail) {
        const problem: ProblemDetail = error.error;
        console.error(`[RFC 7807 Error] ${problem.title}: ${problem.detail} (Ref: ${problem.correlationId})`);
        alert(`${problem.title}: ${problem.detail}\n\nSupport Reference: ${problem.correlationId || 'N/A'}`);
      }
      return throwError(() => error);
    })
  );
};
```
