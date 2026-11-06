# Q131: Dynamic filtering across multiple tables using JPA Criteria API & Specification?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise logistics CRM required searching shipments across 12 optional criteria (e.g., origin city, destination city, min weight, max weight, courier company, delivery date range, fragile flag). Developers originally attempted writing distinct JPQL query methods for every combination, resulting in an unmaintainable explosion of 48 query methods. 

Implementing **Spring Data JPA Specifications** with the **JPA Criteria API** allowed the backend to dynamically construct SQL queries with `WHERE` clauses assembled only for the filters actually supplied by the user.

---

### 2. Concept & Architecture

```
┌───────────────────────── JPA Criteria API & Specification ─────────────────────────┐
│                                                                                     │
│  User Search Request:                                                               │
│  GET /api/v1/shipments?destination=Chicago&minWeight=50.0                           │
│                 │                                                                   │
│                 ▼                                                                   │
│  [ ShipmentSpecificationBuilder ]                                                   │
│       ├── If destination != null ──► cb.equal(root.get("destinationCity"), "Chicago")│
│       ├── If minWeight != null   ──► cb.greaterThanOrEqualTo(root.get("weight"), 50)│
│       └── If courierId == null   ──► Skipped! Not added to query predicates.        │
│                 │                                                                   │
│                 ▼                                                                   │
│  [ cb.and(predicates.toArray()) ]                                                   │
│                 │                                                                   │
│                 ▼ Dynamic SQL Generated                                             │
│  SELECT * FROM shipments WHERE destination_city = 'Chicago' AND weight >= 50.0;     │
│                                                                                     │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.logistics.specification;

import com.enterprise.logistics.entity.Shipment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShipmentSpecification {

    public record ShipmentFilterCriteria(
        String destinationCity,
        BigDecimal minWeight,
        BigDecimal maxWeight,
        String courierName
    ) {}

    public static Specification<Shipment> filterBy(ShipmentFilterCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(criteria.destinationCity())) {
                predicates.add(cb.equal(root.get("destinationCity"), criteria.destinationCity()));
            }

            if (criteria.minWeight() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("weight"), criteria.minWeight()));
            }

            if (criteria.maxWeight() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("weight"), criteria.maxWeight()));
            }

            if (StringUtils.hasText(criteria.courierName())) {
                // Cross-table join with Courier entity
                predicates.add(cb.equal(root.join("courier").get("name"), criteria.courierName()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
In Angular, dynamic search filters require:
1. A Reactive Form synchronized with browser URL query parameters (`router.navigate([], { queryParams })`).
2. Debouncing inputs (`debounceTime(400)`) to avoid triggering searches on every keystroke.

---

### 2. Production Code (Angular 17+ Standalone)

#### Search Filter Bar Component (`shipment-filter.component.ts`)
```typescript
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
  selector: 'app-shipment-filter',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <form [formGroup]="filterForm" class="filter-bar">
      <input type="text" formControlName="destinationCity" placeholder="Destination City" />
      <input type="number" formControlName="minWeight" placeholder="Min Weight (kg)" />
      <input type="text" formControlName="courierName" placeholder="Courier Name" />
    </form>
  `
})
export class ShipmentFilterComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly filterForm = this.fb.group({
    destinationCity: [''],
    minWeight: [null],
    courierName: ['']
  });

  ngOnInit(): void {
    // 1. Sync from URL on load
    this.route.queryParams.subscribe(params => {
      this.filterForm.patchValue(params, { emitEvent: false });
    });

    // 2. Debounce and sync back to URL query parameters
    this.filterForm.valueChanges.pipe(
      debounceTime(400),
      distinctUntilChanged()
    ).subscribe(filters => {
      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: filters,
        queryParamsHandling: 'merge'
      });
    });
  }
}
```
