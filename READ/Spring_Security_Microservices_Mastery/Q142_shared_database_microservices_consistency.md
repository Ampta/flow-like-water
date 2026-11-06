# Q142: Maintaining consistency when multiple microservices access a shared database?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise e-commerce platform had `OrderService` and `InventoryService` querying the same physical PostgreSQL database. When the Order team ran a database migration renaming column `stock_qty` to `available_units`, `InventoryService` instantly crashed in production. Furthermore, database deadlocks between both services caused high transaction abort rates. 

Eliminating the **Shared Database Anti-Pattern** by enforcing strict **Database-per-Service** isolation and synchronizing inventory deductions via **Change Data Capture (CDC with Debezium)** and Kafka domain events restored system independence.

---

### 2. Concept & Architecture

```
┌──────────────────── Shared Database vs Database-per-Service ────────────────────┐
│                                                                                  │
│  ANTI-PATTERN: Shared Database                                                   │
│  [ Order Service ]     [ Inventory Service ]                                     │
│          │                       │                                               │
│          └───────────┬───────────┘                                               │
│                      ▼ Direct Table Joins & Shared Locks                         │
│           [ Shared Monolithic DB ] ──► Schema changes break other services!      │
│                                                                                  │
│  RECOMMENDED ARCHITECTURE: Database-per-Service                                  │
│  [ Order Service ] ──► [ Order DB ]                                              │
│          │                                                                       │
│          ▼ Emits "OrderPlacedEvent"                                              │
│  [ Kafka Topic: orders ]                                                         │
│          │                                                                       │
│          ▼ Asynchronously consumes event                                         │
│  [ Inventory Service ] ──► [ Inventory DB ] (Deducts stock independently!)       │
│                                                                                  │
└──────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.inventory.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final InventoryRepository inventoryRepository;

    public record OrderPlacedEvent(String orderId, Long productId, int quantity) {}

    @Transactional
    @KafkaListener(topics = "orders-topic", groupId = "inventory-group")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Received OrderPlacedEvent for order: {}. Deducting stock...", event.orderId());
        
        inventoryRepository.deductStock(event.productId(), event.quantity());
        log.info("Stock deducted successfully for product: {}", event.productId());
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, database separation reflects in decoupled screen modules:
- The UI doesn't rely on massive multi-table joins.
- Separate services query distinct domain APIs, communicating via signals for modular component reactivity.

---

### 2. Production Code (Angular 17+ Standalone)

#### Inventory Stock Status Badge (`stock-badge.component.ts`)
```typescript
import { Component, Input, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-stock-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="badge" [ngClass]="badgeClass()">
      {{ inStock() ? 'In Stock (' + availableCount() + ')' : 'Out of Stock' }}
    </span>
  `
})
export class StockBadgeComponent implements OnInit {
  @Input({ required: true }) productId!: number;

  private readonly http = inject(HttpClient);
  readonly availableCount = signal(0);
  readonly inStock = signal(true);

  get badgeClass(): string {
    return this.inStock() ? 'badge-success' : 'badge-danger';
  }

  ngOnInit(): void {
    this.http.get<{ stock: number }>(`/api/v1/inventory/${this.productId}/stock`).subscribe(res => {
      this.availableCount.set(res.stock);
      this.inStock.set(res.stock > 0);
    });
  }
}
```
