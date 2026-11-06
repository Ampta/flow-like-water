# Q145: Centralized distributed logging with ELK Stack and Correlation IDs?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A critical transaction failed across an 8-microservice chain. DevOps spent 4 hours manually searching logs across 30 Kubernetes pods, unable to connect a log in `OrderService` with an error in `PaymentService`. 

Implementing **Distributed Tracing with Micrometer Tracing (Brave / OpenTelemetry)** and the **W3C Trace Context (`traceparent`)** injected a unique `traceId` and `spanId` into SLF4J MDC. Formatting logs with `LogstashEncoder` shipped structured JSON directly to the ELK Stack (Elasticsearch, Logstash, Kibana), enabling instant root-cause analysis in seconds.

---

### 2. Concept & Architecture

```
┌────────────────────── Distributed Tracing & Logging Flow ──────────────────────┐
│                                                                                │
│  [ Angular Client ] ──► Header: X-Correlation-Id: abc-123                      │
│            │                                                                   │
│            ▼                                                                   │
│  [ Spring Cloud Gateway ] ──► Generates W3C Trace: traceId=98234a, spanId=01   │
│            │                                                                   │
│            ▼ Propagates traceId via HTTP headers                               │
│  [ Order Service ] ──► Feign Client (relays traceparent) ──► [ Payment Service]│
│            │                                                        │          │
│            ▼ SLF4J MDC: [traceId=98234a, spanId=01]                 ▼          │
│  [ Logback LogstashEncoder: Outputs JSON Logs ] ────────────────────┤          │
│            │                                                                   │
│            ▼                                                                   │
│  [ Elasticsearch & Kibana Dashboard ] ──► Search "traceId: 98234a" displays    │
│    the complete end-to-end transaction journey across all 8 microservices!     │
│                                                                                │
└────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### Logback Configuration (`logback-spring.xml`)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="LOGSTASH_JSON" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <includeMdcKeyName>traceId</includeMdcKeyName>
            <includeMdcKeyName>spanId</includeMdcKeyName>
            <includeMdcKeyName>userId</includeMdcKeyName>
            <customFields>{"application":"order-service"}</customFields>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="LOGSTASH_JSON" />
    </root>
</configuration>
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, distributed tracing starts at the client:
- An HTTP interceptor generates or passes a unique `X-Correlation-Id` on all API requests.
- When an error occurs, the correlation ID is displayed in error dialogs so customers can quote it directly to support agents.

---

### 2. Production Code (Angular 17+ Standalone)

#### Correlation ID Interceptor (`correlation-id.interceptor.ts`)
```typescript
import { HttpInterceptorFn } from '@angular/common/http';

export const correlationIdInterceptor: HttpInterceptorFn = (req, next) => {
  const correlationId = crypto.randomUUID();

  const clonedReq = req.clone({
    setHeaders: {
      'X-Correlation-Id': correlationId
    }
  });

  return next(clonedReq);
};
```
