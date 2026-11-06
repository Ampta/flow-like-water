# Q106: How do you configure role-based access control in Spring Security?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise ERP application had three tiers of users: `EMPLOYEE`, `MANAGER`, and `SUPER_ADMIN`. Over time, developers wrote repetitive `@PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('SUPER_ADMIN')")` annotations across hundreds of controller endpoints. In several newly created payroll services, developers inadvertently omitted `hasRole('SUPER_ADMIN')`, causing access denied errors for company executives. 

Implementing a formal **Role Hierarchy** (`RoleHierarchyImpl`) and clean **Role-Based Access Control (RBAC)** eliminated this boilerplate: granting `SUPER_ADMIN` higher privileges automatically inherited all rights of `MANAGER` and `EMPLOYEE`.

---

### 2. Concept & Architecture

```
                                 [ Role Hierarchy Tree ]
                                            │
                                            ▼
                                  ┌───────────────────┐
                                  │  ROLE_SUPER_ADMIN │
                                  └─────────┬─────────┘
                                            │ implies
                                            ▼
                                  ┌───────────────────┐
                                  │   ROLE_MANAGER    │
                                  └─────────┬─────────┘
                                            │ implies
                                            ▼
                                  ┌───────────────────┐
                                  │   ROLE_EMPLOYEE   │
                                  └───────────────────┘
```

#### Roles vs Authorities:
- **Authority**: A granular permission string (e.g., `INVOICE_READ`, `USER_DELETE`).
- **Role**: A coarse-grained container of authorities, canonically prefixed with `ROLE_` (e.g., `ROLE_ADMIN`).
- In Spring Security:
  - `hasRole("ADMIN")` automatically prepends `ROLE_` and matches against `ROLE_ADMIN`.
  - `hasAuthority("ADMIN")` performs a strict string comparison against `ADMIN` (without prefix).

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Security Configuration with RoleHierarchy (`RbacSecurityConfig.java`)
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class RbacSecurityConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(
            "ROLE_SUPER_ADMIN > ROLE_MANAGER
" +
            "ROLE_MANAGER > ROLE_EMPLOYEE"
        );
        return hierarchy;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/admin/**").hasRole("SUPER_ADMIN")
                .requestMatchers("/api/v1/management/**").hasRole("MANAGER")
                // Because of RoleHierarchy, both MANAGER and SUPER_ADMIN can access /api/v1/employee/**
                .requestMatchers("/api/v1/employee/**").hasRole("EMPLOYEE")
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

#### 2. Method-Level Protected Service (`PayrollService.java`)
```java
package com.enterprise.security.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {

    // Inherited by SUPER_ADMIN automatically via RoleHierarchy
    @PreAuthorize("hasRole('MANAGER')")
    public void approveTimesheet(Long timesheetId) {
        // Business logic for approving timesheets
    }

    // Accessible ONLY to SUPER_ADMIN
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void disburseSalaries() {
        // High-security treasury disbursement
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
In the Angular frontend, RBAC dictates the visibility of navigation items and control actions. 
Instead of hardcoding isolated role checks everywhere, the Angular application maps the backend's role hierarchy into a centralized `MenuService` and guards child routes hierarchically.

---

### 2. Production Code (Angular 17+ Standalone)

#### 1. Dynamic RBAC Navigation Menu Service (`menu.service.ts`)
```typescript
import { Injectable, computed, inject } from '@angular/core';
import { AuthService } from './auth.service';

export interface MenuItem {
  title: string;
  route: string;
  icon: string;
  requiredRole: 'ROLE_EMPLOYEE' | 'ROLE_MANAGER' | 'ROLE_SUPER_ADMIN';
}

@Injectable({
  providedIn: 'root'
})
export class MenuService {
  private readonly authService = inject(AuthService);

  private readonly allMenuItems: MenuItem[] = [
    { title: 'My Timesheets', route: '/timesheets', icon: 'clock', requiredRole: 'ROLE_EMPLOYEE' },
    { title: 'Team Approval', route: '/team-approval', icon: 'users', requiredRole: 'ROLE_MANAGER' },
    { title: 'Payroll Disburse', route: '/payroll-disbursement', icon: 'dollar-sign', requiredRole: 'ROLE_SUPER_ADMIN' }
  ];

  // Role hierarchy mapping (mirrors Spring Security's RoleHierarchyImpl)
  private readonly hierarchyMap: Record<string, string[]> = {
    'ROLE_SUPER_ADMIN': ['ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE'],
    'ROLE_MANAGER': ['ROLE_MANAGER', 'ROLE_EMPLOYEE'],
    'ROLE_EMPLOYEE': ['ROLE_EMPLOYEE']
  };

  readonly allowedMenuItems = computed(() => {
    const userRoles = this.authService.userRoles();
    // Resolve all inherited roles for the active user
    const effectiveRoles = new Set<string>();
    userRoles.forEach(role => {
      const inherited = this.hierarchyMap[role] || [role];
      inherited.forEach(r => effectiveRoles.add(r));
    });

    return this.allMenuItems.filter(item => effectiveRoles.has(item.requiredRole));
  });
}
```

#### 2. Sidebar Navigation Component (`sidebar-nav.component.ts`)
```typescript
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MenuService } from './menu.service';

@Component({
  selector: 'app-sidebar-nav',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <nav class="sidebar-nav">
      <ul>
        @for (item of menuService.allowedMenuItems(); track item.route) {
          <li>
            <a [routerLink]="item.route" routerLinkActive="active">
              <span class="icon">{{ item.icon }}</span>
              <span class="title">{{ item.title }}</span>
            </a>
          </li>
        }
      </ul>
    </nav>
  `
})
export class SidebarNavComponent {
  readonly menuService = inject(MenuService);
}
```
