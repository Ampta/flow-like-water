# Q136: Database Schema Migration between environments (Flyway / Liquibase)?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A software team relied on Hibernate's `spring.jpa.hibernate.ddl-auto=update` in staging and production. During a critical deployment, an automatic entity update altered a column type from `VARCHAR` to `TEXT`, locking a 10-million row database table for 35 minutes and corrupting index statistics. 

Disabling `ddl-auto` and enforcing version-controlled database migrations with **Flyway** guaranteed repeatable, audit-compliant schema evolutions across all environments.

---

### 2. Concept & Architecture

```
┌──────────────────────── Flyway Database Migration Lifecycle ────────────────────────┐
│                                                                                      │
│   src/main/resources/db/migration/                                                   │
│   ├── V1__init_schema.sql                                                            │
│   ├── V2__add_index_on_user_email.sql                                                │
│   └── R__refresh_reporting_view.sql (Repeatable)                                     │
│                                                                                      │
│   Application Startup:                                                               │
│   1. Flyway queries table 'flyway_schema_history'.                                   │
│   2. Calculates SHA-256 checksum of SQL files.                                       │
│   3. Executes unapplied migrations in strict semantic version order within a DB lock. │
│   4. Updates 'flyway_schema_history' on success.                                      │
│                                                                                      │
└──────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Flyway Configuration (`application.yml`)
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate # Strictly validates schema; NEVER alters production tables!
  flyway:
    enabled: true
    baseline-on-migrate: true
    baseline-version: 0
    locations: classpath:db/migration
    validate-on-migrate: true
```

#### 2. Sample Migration Script (`V1__init_schema.sql`)
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
When backend database migrations introduce API contract versioning:
1. The Angular HTTP Interceptor attaches an `Accept-Version` or `X-API-Version` header.
2. Angular detects deprecation headers (`Sunset`, `Deprecation`) sent by the backend and logs alerts or notifies users to reload the application.

---

### 2. Production Code (Angular 17+ Standalone)

#### API Version Interceptor (`api-version.interceptor.ts`)
```typescript
import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { tap } from 'rxjs';

export const apiVersionInterceptor: HttpInterceptorFn = (req, next) => {
  const versionedReq = req.clone({
    setHeaders: {
      'X-API-Version': 'v1'
    }
  });

  return next(versionedReq).pipe(
    tap(event => {
      if (event instanceof HttpResponse) {
        // Detect backend Sunset/Deprecation headers resulting from schema migrations
        const sunsetHeader = event.headers.get('Sunset');
        if (sunsetHeader) {
          console.warn(`[API Deprecation] Endpoint ${req.url} will be sunset on: ${sunsetHeader}`);
        }
      }
    })
  );
};
```
