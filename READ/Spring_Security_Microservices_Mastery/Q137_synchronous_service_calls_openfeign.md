# Q137: Implementing synchronous service-to-service calls using OpenFeign?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In a microservices HR system, `EmployeeService` called `DepartmentService` and `PayrollService` using raw `RestTemplate`. Over time, code was duplicated across 20 classes with hardcoded URLs, string concatenations, and fragmented error handling. When `DepartmentService` returned a `404 Not Found`, `EmployeeService` crashed with an unhandled `HttpClientErrorException`, returning a generic `500 Internal Server Error` to the user. 

Adopting **Spring Cloud OpenFeign** replaced the boilerplate with clean declarative interfaces, centralized connection timeouts, and a custom **`ErrorDecoder`** that maps downstream errors into domain-specific exceptions.

---

### 2. Concept & Architecture

```
┌─────────────────────────── Spring Cloud OpenFeign ───────────────────────────┐
│                                                                              │
│  [ Employee Service: Controller / Service ]                                  │
│             │                                                                │
│             ▼ Invokes Java Interface Method: departmentClient.getDept(42)    │
│  [ OpenFeign Dynamic Proxy ]                                                 │
│       ├── 1. RequestTemplate: Resolves URL "http://department-service/api/..."│
│       ├── 2. RequestInterceptor: Injects Headers (e.g. Bearer Token)         │
│       ├── 3. LoadBalancer (Spring Cloud LoadBalancer): Selects instance IP  │
│       ├── 4. HTTP Client (Apache HttpClient / OkHttp): Executes HTTP call    │
│       └── 5. ErrorDecoder: If status >= 400 ──► Maps to Domain Exception!    │
│             │                                                                │
│             ▼                                                                │
│  [ Department Service (Port 8082) ]                                          │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Feign Client Interface (`DepartmentClient.java`)
```java
package com.enterprise.employee.client;

import com.enterprise.employee.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "department-service",
    path = "/api/v1/departments",
    configuration = FeignConfig.class
)
public interface DepartmentClient {

    record DepartmentDto(Long id, String name, String costCenter) {}

    @GetMapping("/{id}")
    DepartmentDto getDepartmentById(@PathVariable("id") Long id);
}
```

#### 2. Custom ErrorDecoder & Feign Configuration (`FeignConfig.java`)
```java
package com.enterprise.employee.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public Request.Options requestOptions() {
        // Connect timeout: 2 seconds, Read timeout: 5 seconds
        return new Request.Options(2000, TimeUnit.MILLISECONDS, 5000, TimeUnit.MILLISECONDS, true);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }

    public static class CustomFeignErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultDecoder = new Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() == 404) {
                return new IllegalArgumentException("Downstream resource not found for call: " + methodKey);
            } else if (response.status() >= 500) {
                return new IllegalStateException("Downstream service error. Please retry later.");
            }
            return defaultDecoder.decode(methodKey, response);
        }
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, consuming endpoints that coordinate OpenFeign calls requires resilient error handling:
- If a downstream service fails and the backend maps it to a structured error, the Angular interceptor parses the error message and notifies the user with a dismissible notification.

---

### 2. Production Code (Angular 17+ Standalone)

#### Department Client Service (`department.service.ts`)
```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';

export interface DepartmentDto {
  id: number;
  name: string;
  costCenter: string;
}

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {
  private readonly http = inject(HttpClient);

  getDepartment(id: number): Observable<DepartmentDto> {
    return this.http.get<DepartmentDto>(`/api/v1/departments/${id}`).pipe(
      catchError((error: HttpErrorResponse) => {
        let userMsg = 'Failed to load department details.';
        if (error.status === 404) {
          userMsg = 'Department record does not exist.';
        }
        return throwError(() => new Error(userMsg));
      })
    );
  }
}
```
