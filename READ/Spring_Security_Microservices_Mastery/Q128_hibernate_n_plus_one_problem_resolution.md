# Q128: Resolving Hibernate N+1 select problem in enterprise services?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise staff directory rendered 100 employees per page. In production, loading this single page executed **101 SQL queries**!
- 1 initial query: `SELECT * FROM employees LIMIT 100;`
- 100 subsequent queries: `SELECT * FROM departments WHERE id = ?;` (one per employee row).
Database CPU spiked to 100%, and API latency surged from 35ms to **6.8 seconds**. 

Resolving the Hibernate N+1 select problem using **`JOIN FETCH`**, **`@EntityGraph`**, and configuring global batch fetching (`hibernate.default_batch_fetch_size=50`) dropped the query count from 101 to **1**, reducing response time to **28ms**.

---

### 2. Concept & Architecture

```
┌────────────────────────── Resolving Hibernate N+1 Queries ──────────────────────────┐
│                                                                                     │
│  PROBLEM (N+1 Select):                                                              │
│  1. SELECT * FROM employees; ──► Returns 100 records                                │
│  2. As code loops through employees, each employee.getDepartment() triggers:        │
│     SELECT * FROM departments WHERE id = 1;                                         │
│     SELECT * FROM departments WHERE id = 2; ... (100 times!)                        │
│                                                                                     │
│  SOLUTION 1: JOIN FETCH (JPQL)                                                      │
│  SELECT e FROM Employee e JOIN FETCH e.department                                   │
│  └── Executes exactly 1 single SQL JOIN query!                                      │
│                                                                                     │
│  SOLUTION 2: @EntityGraph (Declarative)                                             │
│  @EntityGraph(attributePaths = {"department"})                                      │
│  └── Instructs Hibernate to override LAZY fetch dynamically for this query.        │
│                                                                                     │
│  SOLUTION 3: Batch Fetching (Fallback Safety Net)                                   │
│  spring.jpa.properties.hibernate.default_batch_fetch_size=50                         │
│  └── If N+1 occurs, groups queries using SQL IN clause:                             │
│      SELECT * FROM departments WHERE id IN (1, 2, 3, ... 50);                       │
│      Reduces 100 queries to only 2 batch queries!                                   │
│                                                                                     │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Repository with JOIN FETCH & EntityGraph (`EmployeeRepository.java`)
```java
package com.enterprise.hr.repository;

import com.enterprise.hr.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // 1. Explicit JPQL JOIN FETCH: Solves N+1 in a single SQL query
    @Query("SELECT e FROM Employee e JOIN FETCH e.department")
    List<Employee> findAllWithDepartmentJoinFetch();

    // 2. Spring Data @EntityGraph: Overrides LAZY fetching dynamically
    @EntityGraph(attributePaths = {"department"})
    Page<Employee> findAll(Pageable pageable);
}
```

#### 2. Batch Fetching Configuration (`application.yml`)
```yaml
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 50 # Safety net for unshielded LAZY relationships
        format_sql: false
        generate_statistics: false # Enable in dev to verify query counts
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, avoiding data volume bottlenecks involves:
1. Enforcing server-side pagination so the client never asks for 50,000 un-paged records.
2. Implementing **CDK Virtual Scroll (`cdk-virtual-scroll-viewport`)** to render only visible DOM nodes, maintaining 60fps rendering performance.

---

### 2. Production Code (Angular 17+ Standalone)

#### Virtual Scrolling Employee Roster (`employee-virtual-table.component.ts`)
```typescript
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ScrollingModule } from '@angular/cdk/scrolling';

export interface EmployeeDto {
  id: number;
  fullName: string;
  departmentName: string;
  email: string;
}

@Component({
  selector: 'app-employee-virtual-table',
  standalone: true,
  imports: [CommonModule, ScrollingModule],
  template: `
    <div class="virtual-table-container">
      <h3>Enterprise Directory (Loaded in Single SQL Fetch)</h3>

      <cdk-virtual-scroll-viewport itemSize="48" class="viewport">
        <div *cdkVirtualFor="let emp of employeeList()" class="table-row">
          <span class="col id">{{ emp.id }}</span>
          <span class="col name">{{ emp.fullName }}</span>
          <span class="col dept">{{ emp.departmentName }}</span>
          <span class="col email">{{ emp.email }}</span>
        </div>
      </cdk-virtual-scroll-viewport>
    </div>
  `,
  styles: [`
    .viewport { height: 400px; width: 100%; border: 1px solid #ccc; }
    .table-row { height: 48px; display: flex; align-items: center; border-bottom: 1px solid #eee; padding: 0 16px; }
    .col { flex: 1; }
    .col.id { flex: 0.3; }
  `]
})
export class EmployeeVirtualTableComponent implements OnInit {
  private readonly http = inject(HttpClient);

  readonly employeeList = signal<EmployeeDto[]>([]);

  ngOnInit(): void {
    this.http.get<EmployeeDto[]>('/api/v1/employees/directory').subscribe(data => {
      this.employeeList.set(data);
    });
  }
}
```
