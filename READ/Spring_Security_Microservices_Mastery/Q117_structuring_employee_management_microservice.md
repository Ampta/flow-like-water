# Q117: How to structure an Employee Management Microservice (Employee, Department, Project)?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In a corporate enterprise, an Employee Management application was initially designed as a monolithic database where `Employee`, `Department`, and `Project` tables were connected via tight foreign keys. During quarterly performance reviews, the payroll team ran intense salary computations that locked the `Employee` table. Because `Project` and `Department` were tightly bound in the same database transactions, software engineers could not log project hours and HR could not update department budgets. 

Restructuring the domain into **Domain-Driven Design (DDD) Bounded Contexts** with decoupled microservices and database-per-service architecture eliminated cross-table locking and allowed each domain to scale independently.

---

### 2. Concept & Architecture

```
┌───────────────────────────────── Bounded Context Architecture ─────────────────────────────────┐
│                                                                                                │
│  [ API Gateway: Spring Cloud Gateway ]                                                         │
│           │                               │                              │                     │
│           ▼                               ▼                              ▼                     │
│  ┌──────────────────┐           ┌──────────────────┐           ┌──────────────────┐            │
│  │ Employee Service │           │Department Service│           │ Project Service  │            │
│  │ (Port 8081)      │           │ (Port 8082)      │           │ (Port 8083)      │            │
│  └────────┬─────────┘           └────────┬─────────┘           └────────┬─────────┘            │
│           │                              │                              │                      │
│           ▼                              ▼                              ▼                      │
│  ┌──────────────────┐           ┌──────────────────┐           ┌──────────────────┐            │
│  │  Employee DB     │           │  Department DB   │           │   Project DB     │            │
│  │  (PostgreSQL)    │           │  (PostgreSQL)    │           │  (PostgreSQL)    │            │
│  └──────────────────┘           └──────────────────┘           └──────────────────┘            │
│                                                                                                │
│  * Inter-service references use LOOSE IDENTIFIERS (e.g. employee.departmentId = 42)            │
│  * Never use @ManyToOne across microservice database boundaries!                               │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Employee Entity with Loose Identifiers (`Employee.java`)
```java
package com.enterprise.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_emp_dept", columnList = "department_id"),
    @Index(name = "idx_emp_email", columnList = "email", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private BigDecimal salary;

    // Loose foreign key referencing Department Microservice
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    // Loose collection of project IDs managed by Project Microservice
    @ElementCollection
    @CollectionTable(name = "employee_project_ids", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "project_id")
    @Builder.Default
    private Set<Long> projectIds = new HashSet<>();
}
```

#### 2. DTO Contract & REST Controller (`EmployeeController.java`)
```java
package com.enterprise.employee.controller;

import com.enterprise.employee.entity.Employee;
import com.enterprise.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public record EmployeeSummaryDto(Long id, String fullName, String email, Long departmentId, Set<Long> projectIds) {}

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeSummaryDto> getEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id)
            .map(emp -> new EmployeeSummaryDto(emp.getId(), emp.getFullName(), emp.getEmail(), emp.getDepartmentId(), emp.getProjectIds()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, microservice decomposition is matched by **Feature Modules / Micro-Frontends**:
- `EmployeeFeature`: Manages employee listings, profile edits, and salary reviews.
- When rendering an employee profile, the Angular component coordinates fetching employee details from `/api/v1/employees/101`, and then asynchronously loads department and project details using child components or combined observables.

---

### 2. Production Code (Angular 17+ Standalone)

#### Composite Employee Detail Component (`employee-detail.component.ts`)
```typescript
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';

export interface Employee {
  id: number;
  fullName: string;
  email: string;
  departmentId: number;
  projectIds: number[];
}

export interface Department {
  id: number;
  name: string;
  costCenter: string;
}

export interface Project {
  id: number;
  title: string;
  deadline: string;
}

@Component({
  selector: 'app-employee-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    @if (isLoading()) {
      <div class="spinner">Loading profile...</div>
    } @else if (employee()) {
      <div class="employee-card">
        <h2>{{ employee()!.fullName }}</h2>
        <p>Email: {{ employee()!.email }}</p>
        
        <div class="department-badge">
          <strong>Department:</strong> {{ department()?.name || 'Unassigned' }}
        </div>

        <h3>Active Projects ({{ projects().length }})</h3>
        <ul>
          @for (proj of projects(); track proj.id) {
            <li>{{ proj.title }} (Due: {{ proj.deadline }})</li>
          }
        </ul>
      </div>
    }
  `
})
export class EmployeeDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly http = inject(HttpClient);

  readonly isLoading = signal(true);
  readonly employee = signal<Employee | null>(null);
  readonly department = signal<Department | null>(null);
  readonly projects = signal<Project[]>([]);

  ngOnInit(): void {
    const employeeId = this.route.snapshot.paramMap.get('id');
    
    // 1. Fetch Employee record
    this.http.get<Employee>(`/api/v1/employees/${employeeId}`).subscribe(emp => {
      this.employee.set(emp);

      // 2. Concurrently fetch related microservice resources (Department & Projects)
      const dept$ = this.http.get<Department>(`/api/v1/departments/${emp.departmentId}`);
      const proj$ = this.http.post<Project[]>('/api/v1/projects/batch', { ids: emp.projectIds });

      forkJoin([dept$, proj$]).subscribe({
        next: ([dept, projs]) => {
          this.department.set(dept);
          this.projects.set(projs);
          this.isLoading.set(false);
        },
        error: () => this.isLoading.set(false)
      });
    });
  }
}
```
