# Q130: Implementing Soft Deletes in JPA entities?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A user on a fintech investment portal accidentally deleted their investment portfolio. Because the database executed a raw `DELETE FROM portfolios WHERE id = ?`, all associated trade histories, tax records, and compliance audit records were irrevocably destroyed, violating SEC financial record retention compliance laws. 

Implementing **Soft Deletes** in Hibernate using `@SQLDelete` and Hibernate 6.4's `@SQLRestriction` transformed hard deletes into safe status updates (`deleted = true`) while automatically filtering archived records from standard business queries.

---

### 2. Concept & Architecture

```
┌────────────────────────────── Soft Delete Architecture ──────────────────────────────┐
│                                                                                      │
│   JPA Method Call: repository.deleteById(101);                                       │
│          │                                                                           │
│          ▼ Intercepted by @SQLDelete                                                 │
│   SQL Executed: UPDATE accounts SET is_deleted = true, deleted_at = NOW() WHERE id = ?│
│                                                                                      │
│   JPA Method Call: repository.findAll();                                             │
│          │                                                                           │
│          ▼ Filtered automatically by @SQLRestriction                                 │
│   SQL Executed: SELECT * FROM accounts WHERE is_deleted = false;                     │
│                                                                                      │
│  * Records remain in the database for compliance and can be restored!                │
│                                                                                      │
└──────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.fintech.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@Entity
@Table(name = "portfolios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Intercepts repository.delete() and executes soft delete SQL update
@SQLDelete(sql = "UPDATE portfolios SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
// Automatically appends filter condition to all SELECT queries
@SQLRestriction("is_deleted = false")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String portfolioName;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, soft deletion enables superior UX:
1. **Undo Actions:** When a user deletes a card, display an immediate snackbar with a 5-second countdown containing an **"Undo"** button.
2. **Archived Filter Toggle:** Provide managers with a toggle to view and restore soft-deleted items.

---

### 2. Production Code (Angular 17+ Standalone)

#### Undoable Portfolio Card Component (`portfolio-item.component.ts`)
```typescript
import { Component, Input, Output, EventEmitter, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-portfolio-item',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="portfolio-card" [class.archived]="isDeleted()">
      <h4>{{ title }}</h4>

      @if (isDeleted()) {
        <div class="undo-banner">
          Archived. <button (click)="restore()">Undo</button>
        </div>
      } @else {
        <button (click)="softDelete()" class="btn-danger">Archive Portfolio</button>
      }
    </div>
  `
})
export class PortfolioItemComponent {
  @Input({ required: true }) id!: number;
  @Input({ required: true }) title!: string;
  @Output() deleted = new EventEmitter<number>();

  private readonly http = inject(HttpClient);
  readonly isDeleted = signal(false);

  softDelete(): void {
    // Triggers backend @SQLDelete
    this.http.delete(`/api/v1/portfolios/${this.id}`).subscribe(() => {
      this.isDeleted.set(true);
      this.deleted.emit(this.id);
    });
  }

  restore(): void {
    this.http.post(`/api/v1/portfolios/${this.id}/restore`, {}).subscribe(() => {
      this.isDeleted.set(false);
    });
  }
}
```
