# Q118: Microservice communication: REST vs OpenFeign vs Message Brokers?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
During a flash-sale event on an e-commerce platform, the checkout API chained 5 synchronous REST calls: `OrderService` -> `PaymentService` -> `InventoryService` -> `EmailService` -> `AnalyticsService`. When the third-party email provider experienced a 10-second latency spike, the entire order checkout thread pool was blocked waiting for HTTP sockets. Over 5,000 checkout requests timed out, crashing the order system. 

The architecture had to be split: **Synchronous (OpenFeign)** strictly for real-time transactional reads (checking stock level), and **Asynchronous Message Brokers (Apache Kafka)** for state mutations and notifications.

---

### 2. Concept & Architecture

```
┌─────────────────────────── Synchronous vs Asynchronous Communication ───────────────────────────┐
│                                                                                                 │
│  SYNCHRONOUS (OpenFeign / REST):                                                                │
│  [ Order Service ] ──► (HTTP GET /inventory/101) ──► [ Inventory Service ]                      │
│  • Blocking request/response. Client thread waits for response.                                 │
│  • Best for: Real-time queries, immediate validation before state mutation.                    │
│                                                                                                 │
│  ASYNCHRONOUS (Message Broker: Kafka / RabbitMQ):                                               │
│  [ Order Service ] ──► (Publishes "OrderPlacedEvent") ──► [ Kafka Topic: "orders" ]             │
│                                                                   │                             │
│                    ┌──────────────────────────────────────────────┴──────────────┐              │
│                    ▼                                                             ▼              │
│           [ Payment Service ]                                           [ Email Service ]       │
│           (Consumes event & charges card)                               (Consumes & sends email)│
│  • Non-blocking, highly resilient, decouples throughput and eliminates cascading failures.     │
│                                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────────────────────┘
```

#### Comparison Matrix:
| Feature | REST (WebClient) | Spring Cloud OpenFeign | Message Broker (Kafka) |
|---|---|---|---|
| **Protocol** | HTTP/1.1 or HTTP/2 | HTTP/1.1 or HTTP/2 | TCP / Binary Protocol |
| **Model** | Synchronous Non-Blocking | Synchronous Declarative | Asynchronous Event-Driven |
| **Coupling** | Temporal & Spatial coupling | Temporal & Spatial coupling | Completely Decoupled |
| **Backpressure** | Reactive streams (Mono/Flux) | Difficult (requires CircuitBreaker) | Built-in via Consumer Groups |
| **Primary Use** | Real-time reads / Aggregation | Fast internal RPC service queries | State change events, SAGAs |

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Synchronous Client with OpenFeign (`InventoryClient.java`)
```java
package com.enterprise.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service", path = "/api/v1/inventory")
public interface InventoryClient {

    record StockStatus(Long productId, boolean available, int quantity) {}

    @GetMapping("/{productId}/stock")
    StockStatus checkStock(@PathVariable("productId") Long productId);
}
```

#### 2. Asynchronous Event Publisher with Kafka (`OrderEventProducer.java`)
```java
package com.enterprise.order.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    private static final String TOPIC = "orders-topic";

    public record OrderPlacedEvent(String orderId, Long customerId, BigDecimal totalAmount) {}

    public void publishOrderPlaced(OrderPlacedEvent event) {
        log.info("Emitting OrderPlacedEvent for order: {}", event.orderId());
        // Asynchronous non-blocking publish with callback
        kafkaTemplate.send(TOPIC, event.orderId(), event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Order event delivered successfully to partition: {}", result.getRecordMetadata().partition());
                } else {
                    log.error("Failed to deliver order event for orderId: {}", event.orderId(), ex);
                }
            });
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
When interacting with asynchronous message-driven microservices:
1. The user places an order: Angular sends `POST /api/v1/orders`. The backend immediately returns `202 Accepted` with an `orderId` and status `"PENDING"`.
2. Angular establishes a real-time connection via **Server-Sent Events (SSE)** or **WebSockets** to display live saga progress (e.g., "Payment processing..." -> "Inventory reserved..." -> "Order Confirmed!").

---

### 2. Production Code (Angular 17+ Standalone)

#### Real-Time Order Status Tracker (`order-tracker.component.ts`)
```typescript
import { Component, OnInit, OnDestroy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface OrderProgressUpdate {
  orderId: string;
  step: 'PAYMENT_PENDING' | 'INVENTORY_RESERVED' | 'ORDER_CONFIRMED' | 'FAILED';
  message: string;
}

@Component({
  selector: 'app-order-tracker',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="tracker-box">
      <h3>Order #{{ currentOrderId }} Live Status</h3>
      
      <div class="progress-step" [ngClass]="currentStatus()">
        Status: <strong>{{ currentStatus() }}</strong>
      </div>
      <p>{{ statusMessage() }}</p>

      @if (currentStatus() === 'ORDER_CONFIRMED') {
        <div class="alert alert-success">Order completed successfully!</div>
      }
    </div>
  `
})
export class OrderTrackerComponent implements OnInit, OnDestroy {
  readonly currentOrderId = 'ORD-98421';
  readonly currentStatus = signal<string>('SUBMITTED');
  readonly statusMessage = signal<string>('Connecting to live updates...');

  private eventSource: EventSource | null = null;

  ngOnInit(): void {
    // Connect to Server-Sent Events (SSE) endpoint populated by backend Kafka consumer
    this.eventSource = new EventSource(`/api/v1/orders/${this.currentOrderId}/live-status`);

    this.eventSource.onmessage = (event) => {
      const update: OrderProgressUpdate = JSON.parse(event.data);
      this.currentStatus.set(update.step);
      this.statusMessage.set(update.message);

      if (update.step === 'ORDER_CONFIRMED' || update.step === 'FAILED') {
        this.eventSource?.close();
      }
    };

    this.eventSource.onerror = () => {
      this.statusMessage.set('Live connection interrupted. Reconnecting...');
    };
  }

  ngOnDestroy(): void {
    this.eventSource?.close();
  }
}
```
