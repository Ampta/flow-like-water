# Q127: Designing Employee-Department entity relationship using JPA?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In a corporate HR platform, an unoptimized bidirectional `@OneToMany` / `@ManyToOne` relationship between `Department` and `Employee` caused two critical production incidents:
1. Returning entities directly in REST controllers triggered an infinite recursion loop in Jackson (`Department -> Employee -> Department...`), causing a **`StackOverflowError`**.
2. Deleting an inactive department triggered an unintended cascading `DELETE CASCADE` that wiped out 450 active employee records because developers misconfigured `CascadeType.ALL` on the collection!

Restructuring the entity mapping with `FetchType.LAZY`, synchronized bidirectional helper methods, safe cascading policies, and immutable DTO projections resolved these issues.

---

### 2. Concept & Architecture

```
┌───────────────────────── JPA Bidirectional Relationship Design ─────────────────────────┐
│                                                                                         │
│  [ Department Entity ] (Inverse Side)                                                   │
│  ├── id: Long                                                                           │
│  ├── name: String                                                                       │
│  └── @OneToMany(mappedBy = "department", cascade = {PERSIST, MERGE}, fetch = LAZY)     │
│      employees: Set<Employee>                                                           │
│      ├── addEmployee(emp)    <── Helper method keeps both sides in memory sync          │
│      └── removeEmployee(emp)                                                            │
│                                                                                         │
│                         ▲                                                               │
│                         │ mappedBy = "department"                                       │
│                         │                                                               │
│  [ Employee Entity ] (Owning Side - Controls Foreign Key)                               │
│  ├── id: Long                                                                           │
│  ├── fullName: String                                                                   │
│  └── @ManyToOne(fetch = FetchType.LAZY)                                                 │
│      @JoinColumn(name = "department_id", nullable = false)                              │
│      department: Department                                                             │
│                                                                                         │
│  * NEVER use CascadeType.REMOVE / CascadeType.ALL on parent if children must be kept!   │
│  * ALWAYS return immutable Java 21 records (DTOs) from REST endpoints!                  │
│                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Department Entity (`Department.java`)
```java
package com.enterprise.hr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // LAZY fetch avoids loading entire department roster when querying department metadata
    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Employee> employees = new HashSet<>();

    // Defensive unmodifiable view
    public Set<Employee> getEmployees() {
        return Collections.unmodifiableSet(employees);
    }

    // Synchronized bidirectional helper methods
    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setDepartment(null);
    }
}
```

#### 2. Employee Entity (`Employee.java`)
```java
package com.enterprise.hr.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_emp_department", columnList = "department_id")
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

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Owning side of the relationship
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
In Angular, Master-Detail views represent the UI counterpart to `@OneToMany` relationships:
- The left pane renders the list of Departments.
- Selecting a department dynamically loads its assigned employees into the detail view without reloading the page.

---

### 2. Production Code (Angular 17+ Standalone)

#### Master-Detail Department Component (`department-master-detail.component.ts`)
```typescript
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

export interface DepartmentSummary {
  id: number;
  name: string;
  employeeCount: number;
}

export interface EmployeeSummary {
  id: number;
  fullName: string;
  email: string;
}

@Component({
  selector: 'app-department-master-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="master-detail-layout">
      <!-- Master: Department List -->
      <aside class="master-pane">
        <h3>Departments</h3>
        <ul>
          @for (dept of departments(); track dept.id) {
            <li [class.selected]="selectedDeptId() === dept.id" (click)="selectDepartment(dept.id)">
              {{ dept.name }} ({{ dept.employeeCount }})
            </li>
          }
        </ul>
      </aside>

      <!-- Detail: Employees in Selected Department -->
      <main class="detail-pane">
        @if (selectedDeptId()) {
          <h3>Department Staff</h3>
          @if (isLoadingEmployees()) {
            <p>Loading roster...</p>
          } @else {
            <table>
              <thead>
                <tr><th>ID</th><th>Full Name</th><th>Email</th></tr>
              </thead>
              <tbody>
                @for (emp of employees(); track emp.id) {
                  <tr>
                    <td>{{ emp.id }}</td>
                    <td>{{ emp.fullName }}</td>
                    <td>{{ emp.email }}</td>
                  </tr>
                }
              </tbody>
            </table>
          }
        } @else {
          <p>Select a department to view assigned personnel.</p>
        }
      </main>
    </div>
  `
})
export class DepartmentMasterDetailComponent implements OnInit {
  private readonly http = inject(HttpClient);

  readonly departments = signal<DepartmentSummary[]>([]);
  readonly selectedDeptId = signal<number | null>(null);
  readonly employees = signal<EmployeeSummary[]>([]);
  readonly isLoadingEmployees = signal(false);

  ngOnInit(): void {
    this.http.get<DepartmentSummary[]>('/api/v1/departments').subscribe(data => {
      this.departments.set(data);
    });
  }

  selectDepartment(deptId: number): void {
    this.selectedDeptId.set(deptId);
    this.isLoadingEmployees.set(true);

    this.http.get<EmployeeSummary[]>(`/api/v1/departments/${deptId}/employees`).subscribe(data => {
      this.employees.set(data);
      this.isLoadingEmployees.set(false);
    });
  }
}
```
