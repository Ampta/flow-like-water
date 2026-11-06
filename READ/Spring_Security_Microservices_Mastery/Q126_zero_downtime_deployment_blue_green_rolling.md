# Q126: Designing microservices for Zero Downtime Deployment (Blue-Green / Rolling)?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
During a production release, the database team dropped a deprecated column that was renamed in version 2.0. Because active version 1.0 pods were still handling traffic when the database migration ran, hundreds of user checkout transactions failed with SQL `ColumnNotFoundException`. 

Achieving **Zero Downtime Deployments** required:
1. **Graceful Shutdown** in Spring Boot 3.x (`server.shutdown=graceful`) to complete active transactions before pod termination.
2. The **Database Expand-Contract Pattern** (Parallel Run) using Flyway, ensuring database schema changes remain forward and backward compatible across rolling updates.

---

### 2. Concept & Architecture

```
┌──────────────────────── Database Expand-Contract Pattern ────────────────────────┐
│                                                                                  │
│  Phase 1: EXPAND (Version 1.0 running)                                           │
│  - Add new column 'full_name'. Keep old column 'name'.                           │
│  - Database Trigger or Dual-Write keeps both in sync. Both V1 and V2 compatible. │
│                                                                                  │
│  Phase 2: ROLLING UPDATE (Version 1.0 -> Version 2.0)                            │
│  - Kubernetes gradually replaces V1 pods with V2 pods.                           │
│  - Graceful Shutdown: V1 pods finish inflight requests (30s grace period).       │
│                                                                                  │
│  Phase 3: CONTRACT (All pods running Version 2.0)                                │
│  - Remove database trigger and safely drop old column 'name'.                    │
│                                                                                  │
└──────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```yaml
# application.yml
server:
  port: 8080
  shutdown: graceful # Waits for inflight requests to finish before killing JVM

spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s # Maximum time allowed for active requests
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
During a zero-downtime frontend deployment, the CDN replaces lazy-loaded JavaScript chunks with new hashed filenames (e.g. `main.a83f.js` -> `main.b92d.js`). If a user has the application open, navigating to a new route can trigger a **ChunkLoadError** because the old chunk was deleted from the server. 
A global Angular error handler must catch this and trigger a seamless, non-disruptive reload.

---

### 2. Production Code (Angular 17+ Standalone)

#### Chunk Load Auto-Recovery Error Handler (`chunk-error.handler.ts`)
```typescript
import { ErrorHandler, Injectable } from '@angular/core';

@Injectable()
export class ChunkErrorRecoveryHandler implements ErrorHandler {
  handleError(error: any): void {
    const chunkFailedMessage = /Loading chunk [\d]+ failed/;

    if (error?.name === 'ChunkLoadError' || chunkFailedMessage.test(error?.message)) {
      console.warn('New deployment detected! Reloading application to load fresh chunks...');
      window.location.reload();
      return;
    }

    console.error('Unhandled Application Error:', error);
  }
}
```
