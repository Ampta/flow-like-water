# Q135: High-throughput Batch Inserts and Updates in JPA?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An overnight data sync pipeline imported 100,000 product inventory updates. The batch process took **4 hours and 20 minutes** to complete. Profiling revealed that the `Product` entity used `GenerationType.IDENTITY`. In Hibernate, `IDENTITY` **completely disables JDBC batching** because Hibernate must execute immediate SQL `INSERT` statements to obtain the database-generated primary key. 100,000 individual SQL network round-trips were executing sequentially! 

Switching to **`GenerationType.SEQUENCE` with an allocation size of 50**, configuring JDBC batch parameters, and executing periodic `entityManager.flush()` and `clear()` dropped the execution time from 4 hours down to **4 minutes and 12 seconds**.

---

### 2. Concept & Architecture

```
┌──────────────────────── High-Throughput Batch Processing ────────────────────────┐
│                                                                                  │
│  ANTI-PATTERN: GenerationType.IDENTITY                                           │
│  - 100,000 records = 100,000 individual SQL INSERT statements! Batching DISABLED.│
│                                                                                  │
│  PRODUCTION FIX: GenerationType.SEQUENCE with Allocation Size                    │
│  - Hibernate pre-fetches blocks of 50 IDs from sequence in 1 call.               │
│  - hibernate.jdbc.batch_size = 50                                                │
│  - Groups 50 inserts into a single JDBC network packet!                          │
│  - entityManager.flush() + entityManager.clear() every 50 records prevents heap  │
│    memory exhaustion!                                                            │
│                                                                                  │
└──────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Entity Configured for Batching (`Product.java`)
```java
package com.enterprise.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_gen")
    @SequenceGenerator(name = "product_seq_gen", sequenceName = "product_seq", allocationSize = 50)
    private Long id;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private BigDecimal price;
}
```

#### 2. High-Throughput Batch Service (`BatchInsertService.java`)
```java
package com.enterprise.inventory.service;

import com.enterprise.inventory.entity.Product;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchInsertService {

    private final EntityManager entityManager;
    private static final int BATCH_SIZE = 50;

    @Transactional
    public void saveAllInBatches(List<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            entityManager.persist(products.get(i));

            if (i > 0 && i % BATCH_SIZE == 0) {
                // Execute JDBC batch
                entityManager.flush();
                // Release entities from 1st level cache to prevent OOM
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
}
```

#### 3. Batching Properties (`application.yml`)
```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 50
        order_inserts: true
        order_updates: true
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, submitting large datasets requires:
1. Client-side file parsing and batch chunking (e.g. uploading a 50,000-row CSV in 5,000-row chunks).
2. A progress bar visualizing upload completion.

---

### 2. Production Code (Angular 17+ Standalone)

#### Chunked Upload Component (`bulk-product-upload.component.ts`)
```typescript
import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-bulk-product-upload',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="upload-container">
      <h3>Bulk Inventory Import</h3>
      <input type="file" (change)="onFileSelected($event)" accept=".csv" />

      @if (isUploading()) {
        <div class="progress-container">
          <div class="bar" [style.width.%]="progressPercent()"></div>
        </div>
        <p>Uploaded {{ progressPercent() }}%</p>
      }
    </div>
  `
})
export class BulkProductUploadComponent {
  private readonly http = inject(HttpClient);

  readonly isUploading = signal(false);
  readonly progressPercent = signal(0);

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const file = input.files[0];
    const formData = new FormData();
    formData.append('file', file);

    this.isUploading.set(true);

    this.http.post('/api/v1/inventory/batch-upload', formData).subscribe({
      next: () => {
        this.progressPercent.set(100);
        this.isUploading.set(false);
        alert('Batch processing completed successfully!');
      },
      error: () => this.isUploading.set(false)
    });
  }
}
```
