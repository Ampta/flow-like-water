# Q132: Handling LazyInitializationException when returning entities via REST?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In an e-commerce order microservice, customer order receipts threw intermittent errors:
`org.hibernate.LazyInitializationException: could not initialize proxy [com.enterprise.Order#101] - no Session`. 

Developers had returned the raw `@Entity Order` from their `@RestController`. When the `@Transactional` service boundary closed, the Hibernate session was terminated. As Jackson attempted to serialize `order.getOrderItems()` into JSON, it touched the uninitialized ByteBuddy proxy outside an active session. 

Enabling `open-in-view=true` or `enable_lazy_load_no_trans=true` was rejected because it causes database connection pool starvation. The architectural fix was enforcing **Java 21 Record DTO projections** mapped within the transactional boundary.

---

### 2. Concept & Architecture

```
┌────────────────────── Eliminating LazyInitializationException ──────────────────────┐
│                                                                                     │
│  ANTI-PATTERN: Returning Entities                                                   │
│  [ Service (@Transactional) ] ──► Returns Order Entity (Items = Lazy Proxy)         │
│               │ (Transaction finishes, Hibernate Session CLOSED!)                   │
│               ▼                                                                     │
│  [ Jackson Serializer ] ──► Calls getOrderItems() ──► LazyInitializationException!  │
│                                                                                     │
│  ARCHITECTURAL FIX: Immutable DTO Records                                           │
│  [ Service (@Transactional readOnly=true) ]                                         │
│       ├── Fetches Order with @EntityGraph / JOIN FETCH                              │
│       └── Maps to immutable Java Record: OrderResponseDto                           │
│               │                                                                     │
│               ▼ Session Closes Safely                                               │
│  [ Jackson Serializer ] ──► Serializes plain Java Record seamlessly!                │
│                                                                                     │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.order.service;

import com.enterprise.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public record OrderItemDto(Long id, String productName, int quantity, BigDecimal unitPrice) {}
    public record OrderResponseDto(Long orderId, String customerEmail, List<OrderItemDto> items) {}

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderDetails(Long orderId) {
        return orderRepository.findByIdWithItems(orderId)
            .map(order -> new OrderResponseDto(
                order.getId(),
                order.getCustomerEmail(),
                order.getItems().stream()
                    .map(item -> new OrderItemDto(item.getId(), item.getProductName(), item.getQuantity(), item.getUnitPrice()))
                    .toList()
            ))
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, receiving clean, strongly-typed DTOs guarantees predictable rendering and avoids `TypeError: Cannot read properties of undefined` in HTML templates.

---

### 2. Production Code (Angular 17+ Standalone)

#### Order Card Component (`order-card.component.ts`)
```typescript
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface OrderResponseDto {
  orderId: number;
  customerEmail: string;
  items: Array<{
    id: number;
    productName: string;
    quantity: number;
    unitPrice: number;
  }>;
}

@Component({
  selector: 'app-order-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="order-receipt">
      <h3>Order #{{ order.orderId }}</h3>
      <p>Customer: {{ order.customerEmail }}</p>

      <ul class="item-list">
        @for (item of order.items; track item.id) {
          <li>{{ item.productName }} x {{ item.quantity }} ({{ item.unitPrice | currency }})</li>
        }
      </ul>
    </div>
  `
})
export class OrderCardComponent {
  @Input({ required: true }) order!: OrderResponseDto;
}
```
