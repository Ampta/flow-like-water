# Q146: Monitoring microservice health and metrics in production (Prometheus + Grafana)?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A silent memory leak in a microservice led to severe Garbage Collection degradation over a 5-day period until the service suffered an unrecoverable crash on Monday morning. The engineering team had no historical visibility into JVM memory pools, thread states, or HTTP latency spikes. 

Configuring **Spring Boot Actuator with Micrometer and Prometheus Registry** exposed real-time operational metrics at `/actuator/prometheus`, allowing Prometheus to scrape golden signals every 15 seconds and trigger automated alerts in Grafana before outages occur.

---

### 2. Concept & Architecture

```
┌───────────────────── Prometheus + Grafana Observability ─────────────────────┐
│                                                                              │
│  [ Spring Boot Microservice ]                                                │
│  ├── Micrometer Core: Collects JVM, HikariCP, Tomcat metrics                 │
│  └── Custom Business Metrics:                                                │
│      • Counter: orders_placed_total                                          │
│      • Timer: checkout_duration_seconds                                      │
│      • Gauge: active_websocket_connections                                   │
│             │                                                                │
│             ▼ Exposes /actuator/prometheus                                   │
│  [ Prometheus Server ] ──(Scrapes metrics every 15s)                         │
│             │                                                                │
│             ▼                                                                │
│  [ Grafana Dashboard & Alertmanager ]                                        │
│  - Visualizes P99 HTTP latency, GC pauses, and heap saturation               │
│  - Triggers PagerDuty alerts if error rate > 1%                              │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.order.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

@Service
public class OrderMetricsService {

    private final Counter orderCounter;
    private final Timer checkoutTimer;

    public OrderMetricsService(MeterRegistry registry) {
        // Business Counter tracking total order volume by status
        this.orderCounter = Counter.builder("orders.placed.total")
            .description("Total number of orders successfully submitted")
            .tag("channel", "web")
            .register(registry);

        // Timer measuring execution latency percentiles (P95, P99)
        this.checkoutTimer = Timer.builder("order.checkout.duration")
            .description("Time taken to process complete checkout")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
    }

    public void incrementOrdersPlaced() {
        orderCounter.increment();
    }

    public <T> T recordCheckoutTime(Callable<T> operation) throws Exception {
        return checkoutTimer.recordCallable(operation);
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, production observability entails measuring **Core Web Vitals** (Largest Contentful Paint, First Input Delay, Cumulative Layout Shift) and shipping telemetry to the Prometheus/Grafana monitoring gateway.

---

### 2. Production Code (Angular 17+ Standalone)

#### Frontend Web Vitals Telemetry Service (`web-vitals.service.ts`)
```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface WebVitalMetric {
  name: 'LCP' | 'FID' | 'CLS';
  value: number;
  rating: 'good' | 'needs-improvement' | 'poor';
}

@Injectable({
  providedIn: 'root'
})
export class WebVitalsTelemetryService {
  private readonly http = inject(HttpClient);

  recordMetric(metric: WebVitalMetric): void {
    // Sends client-side performance metrics to telemetry ingestion endpoint
    this.http.post('/api/v1/telemetry/web-vitals', metric).subscribe({
      error: () => console.warn('Telemetry delivery suppressed.')
    });
  }
}
```
