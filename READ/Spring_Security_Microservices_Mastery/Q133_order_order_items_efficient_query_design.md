# Q133: Designing efficient queries for Order -> @OneToMany OrderItem?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An e-commerce order management screen queried 1,000 orders with line items. When developers executed a `JOIN FETCH` across multiple `@OneToMany` relationships (`Order -> OrderItem` and `Order -> Shipment`), Hibernate generated a massive **Cartesian Product**: 1,000 orders multiplied by 20 items multiplied by 3 shipments created a result set of **60,000 rows**! The database exhausted its temporary buffer pool and response times reached 12 seconds. 

Replacing the Cartesian join with **JPQL Constructor Projections** and **Split Query fetching** reduced network payload by 92%.

---

### 2. Concept & Architecture

```
┌──────────────────────── Cartesian Product vs Split Queries ────────────────────────┐
│                                                                                    │
│  PROBLEM (Cartesian Product):                                                      │
│  SELECT o FROM Order o JOIN FETCH o.items JOIN FETCH o.shipments                   │
│  Row Count = (Orders * Items * Shipments) ──► Explodes into 60,000 rows!           │
│                                                                                    │
│  SOLUTION: JPQL DTO Constructor Projection (Summary View)                          │
│  SELECT new com.enterprise.dto.OrderSummaryDto(o.id, o.orderDate, COUNT(i))        │
│  FROM Order o LEFT JOIN o.items i GROUP BY o.id, o.orderDate                       │
│  Row Count = Exactly 1,000 rows!                                                   │
│                                                                                    │
└────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderSummaryRepository extends JpaRepository<Order, Long> {

    public record OrderSummaryDto(Long orderId, Instant orderDate, long itemCount) {}

    // Efficient single-query projection without Cartesian collection joins
    @Query("SELECT new com.enterprise.order.repository.OrderSummaryRepository$OrderSummaryDto(o.id, o.orderDate, COUNT(i)) " +
           "FROM Order o LEFT JOIN o.items i GROUP BY o.id, o.orderDate ORDER BY o.orderDate DESC")
    List<OrderSummaryDto> findRecentOrderSummaries();
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, we match this query optimization with an **Accordion Lazy-Loading Pattern**:
- Load the lightweight Order Summaries list upfront.
- Only fetch line items when the user clicks to expand a specific order accordion row.

---

### 2. Production Code (Angular 17+ Standalone)

#### Lazy-Loaded Order Accordion (`order-accordion.component.ts`)
```typescript
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

export interface OrderSummary {
  orderId: number;
  orderDate: string;
  itemCount: number;
}

export interface LineItem {
  id: number;
  productName: string;
  quantity: number;
}

@Component({
  selector: 'app-order-accordion',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="accordion">
      @for (order of orders(); track order.orderId) {
        <div class="accordion-item">
          <div class="accordion-header" (click)="toggleOrder(order.orderId)">
            Order #{{ order.orderId }} ({{ order.itemCount }} items) - {{ order.orderDate | date }}
          </div>

          @if (expandedOrderId() === order.orderId) {
            <div class="accordion-body">
              @if (isLoadingItems()) {
                <p>Loading items...</p>
              } @else {
                <ul>
                  @for (item of activeItems(); track item.id) {
                    <li>{{ item.productName }} x {{ item.quantity }}</li>
                  }
                </ul>
              }
            </div>
          }
        </div>
      }
    </div>
  `
})
export class OrderAccordionComponent implements OnInit {
  private readonly http = inject(HttpClient);

  readonly orders = signal<OrderSummary[]>([]);
  readonly expandedOrderId = signal<number | null>(null);
  readonly activeItems = signal<LineItem[]>([]);
  readonly isLoadingItems = signal(false);

  ngOnInit(): void {
    this.http.get<OrderSummary[]>('/api/v1/orders/summaries').subscribe(data => {
      this.orders.set(data);
    });
  }

  toggleOrder(orderId: number): void {
    if (this.expandedOrderId() === orderId) {
      this.expandedOrderId.set(null);
      return;
    }

    this.expandedOrderId.set(orderId);
    this.isLoadingItems.set(true);

    // On-demand fetch of nested line items
    this.http.get<LineItem[]>(`/api/v1/orders/${orderId}/items`).subscribe(items => {
      this.activeItems.set(items);
      this.isLoadingItems.set(false);
    });
  }
}
```
