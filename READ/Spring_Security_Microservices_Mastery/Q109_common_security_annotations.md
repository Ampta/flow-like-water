# Q109: What are the common annotations used in Spring Security?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In a healthcare management system, patient diagnoses and billing records were exposed to unauthorized staff because developers wrote manual programmatic `if (!user.hasRole("DOCTOR"))` checks inside REST controllers. Over dozens of endpoints, developers forgot these manual checks or used incorrect string comparisons. 

Standardizing on Spring Security's declarative method-level annotations (`@PreAuthorize`, `@PostAuthorize`, `@Secured`, `@AuthenticationPrincipal`) enforced compile-time clarity, centralized authorization rules, and eliminated authorization bypass vulnerabilities.

---

### 2. Concept & Architecture

```
                                  Method Invocation: orderService.getOrder(orderId)
                                                          │
                                                          ▼
┌──────────────────────────────── Spring AOP Method Security Proxy ────────────────────────────────┐
│                                                                                                  │
│  1. @PreAuthorize("@orderSecurity.canAccess(#orderId)")                                          │
│     └── Evaluates SpEL before method execution. If false ──► Throws AccessDeniedException        │
│                                                                                                  │
│  2. Target Method Execution: Order order = orderRepository.findById(orderId);                    │
│                                                                                                  │
│  3. @PostAuthorize("returnObject.ownerUsername == authentication.name")                          │
│     └── Evaluates SpEL on return value. If false ──► Throws AccessDeniedException               │
│                                                                                                  │
│  4. Method Parameter Injection: @AuthenticationPrincipal CustomUserDetails user                   │
│     └── Injects active authenticated principal directly into method parameter                    │
│                                                                                                  │
└─────────────────────────────────────────────────┬────────────────────────────────────────────────┘
                                                  │
                                                  ▼
                                      Returns result to caller
```

#### Annotation Breakdown:
| Annotation | Purpose & Behavior |
|---|---|
| `@EnableWebSecurity` | Activates Spring Security's web security support and filter chain management. |
| `@EnableMethodSecurity` | Enables `@PreAuthorize`, `@PostAuthorize`, `@PreFilter`, `@PostFilter` (Replaces legacy `@EnableGlobalMethodSecurity`). |
| `@PreAuthorize` | Evaluates SpEL expressions **before** invoking the method (supports complex logic and parameters). |
| `@PostAuthorize` | Evaluates SpEL expressions **after** the method runs, with access to `returnObject`. |
| `@Secured` / `@RolesAllowed` | Legacy/JSR-250 annotations for simple role checks (does not support SpEL). |
| `@AuthenticationPrincipal` | Injects the currently authenticated `Principal` (e.g., custom `UserDetails`) directly into controller arguments. |
| `@PreFilter` / `@PostFilter` | Filters collection arguments before execution or return collection elements after execution based on SpEL conditions. |

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Security Configuration (`MethodSecurityConfig.java`)
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(
    prePostEnabled = true, // Enables @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter
    securedEnabled = true, // Enables @Secured
    jsr250Enabled = true   // Enables @RolesAllowed
)
public class MethodSecurityConfig {
}
```

#### 2. Service Demonstrating Method Annotations (`MedicalRecordService.java`)
```java
package com.enterprise.security.service;

import com.enterprise.security.dto.MedicalRecordDto;
import com.enterprise.security.entity.CustomUserDetails;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    // 1. @PreAuthorize: Evaluates before execution with SpEL
    @PreAuthorize("hasRole('DOCTOR') or hasRole('CHIEF_MEDICAL_OFFICER')")
    public MedicalRecordDto createRecord(MedicalRecordDto dto, 
                                         @AuthenticationPrincipal CustomUserDetails currentUser) {
        dto.setCreatedByDoctorId(currentUser.getId());
        // Save and return record
        return dto;
    }

    // 2. @PostAuthorize: Inspects the returned object before returning to caller
    @PostAuthorize("returnObject.assignedDoctorUsername == authentication.name or hasRole('ADMIN')")
    public MedicalRecordDto getRecordById(Long recordId) {
        // Fetches record; if doctor does not match returnObject, AccessDeniedException is thrown
        return findRecordFromDb(recordId);
    }

    // 3. @PreFilter: Filters collections passed into the method
    @PreFilter("filterObject.departmentId == authentication.principal.departmentId")
    public void batchArchiveRecords(List<MedicalRecordDto> records) {
        // Only records belonging to the user's department will be passed in the list!
    }

    private MedicalRecordDto findRecordFromDb(Long id) {
        return new MedicalRecordDto(id, "dr_smith", 101L, "Patient Diagnosed with Hypertension");
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, equivalent declarative permission gating is implemented using:
1. **Structural Directives (`*hasPermission`)**: Conditionally render elements based on user permissions.
2. **Permission Pipes (`| hasRole`)**: Transform UI elements dynamically (e.g. enabling/disabling buttons).

---

### 2. Production Code (Angular 17+ Standalone)

#### 1. Granular Permission Directive (`has-permission.directive.ts`)
```typescript
import { Directive, Input, TemplateRef, ViewContainerRef, inject, effect } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Directive({
  selector: '[hasPermission]',
  standalone: true
})
export class HasPermissionDirective {
  private readonly templateRef = inject(TemplateRef<unknown>);
  private readonly viewContainer = inject(ViewContainerRef);
  private readonly authService = inject(AuthService);

  private requiredPermission = '';
  private isRendered = false;

  @Input() set hasPermission(permission: string) {
    this.requiredPermission = permission;
    this.evaluatePermission();
  }

  constructor() {
    effect(() => {
      this.authService.currentUser();
      this.evaluatePermission();
    });
  }

  private evaluatePermission(): void {
    const userPermissions = this.authService.currentUser()?.roles || [];
    const hasAccess = userPermissions.includes(this.requiredPermission) || 
                      userPermissions.includes('ROLE_ADMIN');

    if (hasAccess && !this.isRendered) {
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.isRendered = true;
    } else if (!hasAccess && this.isRendered) {
      this.viewContainer.clear();
      this.isRendered = false;
    }
  }
}
```

#### 2. Component Template Usage (`medical-record-detail.component.html`)
```html
<div class="medical-record-card">
  <h3>Diagnosis Details</h3>
  <p>{{ record.diagnosis }}</p>

  <!-- Rendered only if user possesses WRITE_PRESCRIPTION authority -->
  <button *hasPermission="'WRITE_PRESCRIPTION'" (click)="editPrescription()">
    Update Prescription
  </button>

  <!-- Rendered only if user possesses DELETE_RECORD authority -->
  <button *hasPermission="'DELETE_RECORD'" class="btn-danger" (click)="deleteRecord()">
    Archive Record
  </button>
</div>
```
