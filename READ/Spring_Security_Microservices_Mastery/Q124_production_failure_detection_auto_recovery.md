# Q124: Production failure detection and auto-recovery in microservices?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A memory leak in a notification microservice caused database connection pool exhaustion. Kubernetes continued sending HTTP requests to the unhealthy pod because the standard `/actuator/health` endpoint reported `UP` (since the JVM process was technically alive). 50% of user requests timed out, until DevOps manually killed the pod. 

Configuring **Kubernetes Liveness and Readiness Probes** via Spring Boot Actuator with custom health indicators enabled Kubernetes to automatically detect connection pool starvation, remove the pod from load balancer routing, and restart it within 30 seconds.

---

### 2. Concept & Architecture

```
┌──────────────────────── Kubernetes Health Probes Architecture ────────────────────────┐
│                                                                                        │
│   Kubernetes Kubelet                                                                   │
│        │                                                                               │
│        ├── 1. Readiness Probe: GET /actuator/health/readiness                          │
│        │   └── Is service ready to accept traffic? (Checks DB, Redis, Kafka queues)    │
│        │       • If DOWN: Pod removed from Service Endpoints. NO traffic routed!       │
│        │                                                                               │
│        └── 2. Liveness Probe: GET /actuator/health/liveness                            │
│            └── Is service deadlocked or frozen?                                        │
│                • If DOWN: Kubelet terminates container and initiates Auto-Recovery pod!│
│                                                                                        │
└────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### Custom Connection Pool Health Indicator (`DatabasePoolHealthIndicator.java`)
```java
package com.enterprise.health;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class DatabasePoolHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    @Override
    public Health health() {
        if (dataSource instanceof HikariDataSource hikari) {
            HikariPoolMXBean pool = hikari.getHikariPoolMXBean();
            int active = pool.getActiveConnections();
            int max = hikari.getMaximumPoolSize();

            // If 95% of connections are exhausted, report service not ready
            if (active >= (max * 0.95)) {
                return Health.down()
                    .withDetail("reason", "HikariCP pool saturation")
                    .withDetail("activeConnections", active)
                    .withDetail("maxPoolSize", max)
                    .build();
            }

            return Health.up()
                .withDetail("activeConnections", active)
                .withDetail("idleConnections", pool.getIdleConnections())
                .build();
        }
        return Health.up().build();
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, failure detection and auto-recovery involve:
1. Detecting network disconnections using `window.addEventListener('online' | 'offline')`.
2. Displaying a non-blocking toast warning.
3. Automatically re-attempting failed requests when connectivity is restored.

---

### 2. Production Code (Angular 17+ Standalone)

#### Auto-Recovery Network Service (`network-recovery.service.ts`)
```typescript
import { Injectable, signal, inject, NgZone } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NetworkRecoveryService {
  private readonly ngZone = inject(NgZone);

  readonly isOnline = signal(navigator.onLine);

  constructor() {
    window.addEventListener('online', () => {
      this.ngZone.run(() => this.isOnline.set(true));
    });

    window.addEventListener('offline', () => {
      this.ngZone.run(() => this.isOnline.set(false));
    });
  }
}
```
