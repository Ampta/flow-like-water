# Q102: Explain how authentication and authorization work in Spring Security.

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In a healthcare management SaaS, a critical vulnerability was exposed: any logged-in user could view private medical charts by simply passing another patient's ID into `/api/v1/patients/98234/medical-records`. The developers had implemented **Authentication** (verifying that the caller was a valid hospital employee with an active JWT), but completely neglected **Authorization** (verifying that the caller held the `ROLE_DOCTOR` or had an assigned relationship to that specific patient). 

The hospital incurred a substantial regulatory fine due to HIPAA violations. Resolving this required establishing a clear separation between **Authentication** ("Who is the caller?") and **Authorization** ("Does the caller have permission to access this resource or execute this operation?").

---

### 2. Concept & Architecture

```
                                      [ INCOMING HTTP REQUEST ]
                                                  │
                                                  ▼
┌────────────────────────────────────────── AUTHENTICATION ──────────────────────────────────────────┐
│                                                                                                    │
│  1. Extract Credentials (e.g., Bearer Token or Username/Password)                                 │
│  2. Create unauthenticated Authentication token (e.g., UsernamePasswordAuthenticationToken)        │
│  3. AuthenticationManager.authenticate(unauthenticatedToken)                                      │
│     └── Delegated to AuthenticationProvider (e.g., DaoAuthenticationProvider)                      │
│         ├── UserDetailsService.loadUserByUsername() ──► Retrieves UserDetails from Database       │
│         └── PasswordEncoder.matches() ──────────────► Compares raw pass with BCrypt hash           │
│  4. If Valid: Returns fully authenticated Authentication object containing:                        │
│         • Principal (UserDetails)                                                                 │
│         • Credentials (nullified for security)                                                    │
│         • Authorities (Collection of GrantedAuthority, e.g., "ROLE_DOCTOR")                       │
│  5. SecurityContextHolder.getContext().setAuthentication(authenticatedToken);                      │
│                                                                                                    │
└─────────────────────────────────────────────────┬──────────────────────────────────────────────────┘
                                                  │
                                                  ▼ Authenticated SecurityContext Active
┌─────────────────────────────────────────── AUTHORIZATION ──────────────────────────────────────────┐
│                                                                                                    │
│  6. Request proceeds to AuthorizationFilter (URL-level) or MethodSecurityInterceptor (Method)    │
│  7. AuthorizationManager.check(Supplier<Authentication>, Object secureObject)                     │
│  8. Evaluates security expressions:                                                               │
│     • hasRole('DOCTOR')                                                                           │
│     • @patientSecurityExpression.canAccessPatient(#patientId)                                     │
│  9. Decision:                                                                                     │
│     ├── Granted: Request dispatched to Controller / Service Method                                │
│     └── Denied: Throws AccessDeniedException ──► ExceptionTranslationFilter returns 403 Forbidden │
│                                                                                                    │
└────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

#### Comparison Matrix:
| Feature | Authentication (AuthN) | Authorization (AuthZ) |
|---|---|---|
| **Core Question** | "Who are you?" | "What are you permitted to do?" |
| **Primary Interface** | `AuthenticationManager`, `AuthenticationProvider` | `AuthorizationManager<T>`, `@PreAuthorize` |
| **Input Data** | Username, Password, JWT, OAuth2 code | Authenticated `GrantedAuthority`, Requested Path, SpEL |
| **Failure Exception** | `AuthenticationException` (e.g. `BadCredentialsException`) | `AccessDeniedException` |
| **HTTP Status Code** | `401 Unauthorized` | `403 Forbidden` |

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Custom Method Authorization Evaluator (`PatientSecurityEvaluator.java`)
```java
package com.enterprise.security.authorization;

import com.enterprise.security.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("patientSecurity")
@RequiredArgsConstructor
public class PatientSecurityEvaluator {

    private final PatientRepository patientRepository;

    public boolean isAssignedDoctorOrAdmin(Authentication authentication, Long patientId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // 1. Super Admin bypasses assignment checks
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return true;
        }

        // 2. Verify doctor assignment in database
        String doctorUsername = authentication.getName();
        return patientRepository.existsByIdAndAssignedDoctorUsername(patientId, doctorUsername);
    }
}
```

#### 2. Controller Demonstrating Both URL & Method-Level Checks (`PatientController.java`)
```java
package com.enterprise.security.controller;

import com.enterprise.security.dto.MedicalRecordDto;
import com.enterprise.security.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final MedicalRecordService medicalRecordService;

    // Authentication verified by SecurityFilterChain;
    // Authorization verified dynamically via Spring Expression Language (SpEL)
    @GetMapping("/{patientId}/medical-records")
    @PreAuthorize("hasRole('DOCTOR') and @patientSecurity.isAssignedDoctorOrAdmin(authentication, #patientId)")
    public ResponseEntity<List<MedicalRecordDto>> getPatientRecords(@PathVariable Long patientId) {
        List<MedicalRecordDto> records = medicalRecordService.getRecordsForPatient(patientId);
        return ResponseEntity.ok(records);
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
In an Angular application:
1. **Authentication** is handled by verifying whether a token is present, unexpired, and verifiable, handled by an `AuthGuard` implementing `CanActivateFn`.
2. **Authorization** is enforced by inspecting the user's roles and permissions to guard specific routes (`RoleGuard`), disabling or hiding buttons and navigation items via a structural directive (`*hasRole`).

---

### 2. Production Code (Angular 17+ Standalone)

#### 1. Functional Role Authorization Guard (`role.guard.ts`)
```typescript
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // 1. Verify Authentication
  if (!authService.isAuthenticated()) {
    router.navigate(['/auth/login'], { queryParams: { returnUrl: route.url.join('/') } });
    return false;
  }

  // 2. Verify Authorization (Role matching)
  const expectedRoles: string[] = route.data['roles'] || [];
  const userRoles = authService.userRoles();

  const hasRequiredRole = expectedRoles.length === 0 || 
    expectedRoles.some(role => userRoles.includes(role));

  if (!hasRequiredRole) {
    // Authenticated, but unauthorized for this route
    router.navigate(['/error/forbidden']);
    return false;
  }

  return true;
};
```

#### 2. Declarative Authorization Directive (`has-role.directive.ts`)
```typescript
import { Directive, Input, TemplateRef, ViewContainerRef, inject, effect } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Directive({
  selector: '[hasRole]',
  standalone: true
})
export class HasRoleDirective {
  private readonly templateRef = inject(TemplateRef<unknown>);
  private readonly viewContainer = inject(ViewContainerRef);
  private readonly authService = inject(AuthService);

  private requiredRoles: string[] = [];
  private isVisible = false;

  @Input() set hasRole(roles: string | string[]) {
    this.requiredRoles = Array.isArray(roles) ? roles : [roles];
    this.updateView();
  }

  constructor() {
    // Reactively re-evaluate visibility whenever the authenticated user's roles change
    effect(() => {
      this.authService.userRoles(); // Register dependency
      this.updateView();
    });
  }

  private updateView(): void {
    const userRoles = this.authService.userRoles();
    const hasPermission = this.requiredRoles.some(role => userRoles.includes(role));

    if (hasPermission && !this.isVisible) {
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.isVisible = true;
    } else if (!hasPermission && this.isVisible) {
      this.viewContainer.clear();
      this.isVisible = false;
    }
  }
}
```

#### 3. Patient Component Template Usage (`patient-view.component.html`)
```html
<div class="patient-card">
  <h2>Patient Chart: {{ patient().fullName }}</h2>

  <!-- Visible to all authenticated staff -->
  <p>Vitals: Heart Rate {{ patient().vitals.heartRate }} bpm</p>

  <!-- Authorization restricted strictly to DOCTOR or ADMIN -->
  <div *hasRole="['ROLE_DOCTOR', 'ROLE_ADMIN']" class="restricted-prescription-section">
    <h3>Prescription Management</h3>
    <button (click)="openPrescribeModal()">Authorize Narcotics Prescription</button>
  </div>
</div>
```
