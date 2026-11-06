# Q112: How do you implement method-level security in Spring?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In a B2B SaaS invoice management system, endpoints like `GET /api/v1/invoices/{id}` were secured at the URL level with `hasRole('ACCOUNTANT')`. However, an accountant from Company A altered the URL parameter to view invoices belonging to Company B (an Insecure Direct Object Reference / IDOR flaw). URL matching alone could not verify whether the requesting user owned that specific invoice record!

Implementing **Method-Level Security** using `@PreAuthorize` with custom Spring Expression Language (SpEL) beans allowed the application to inspect method parameters, query domain ownership in the database, and deny unauthorized access at the business service boundary.

---

### 2. Concept & Architecture

```
                              Client GET /api/v1/invoices/9924
                                             │
                                             ▼
                             [ InvoiceController.getInvoice(9924) ]
                                             │
                                             ▼
┌─────────────────────────── Spring AOP Method Interceptor ───────────────────────────┐
│                                                                                     │
│  Method Annotation:                                                                 │
│  @PreAuthorize("@invoiceSecurity.isInvoiceOwner(authentication, #invoiceId)")       │
│                                                                                     │
│  Evaluation:                                                                        │
│  1. Extract current authentication context: principal = "user_corp_a"              │
│  2. Delegate to Spring Bean: invoiceSecurity.isInvoiceOwner(auth, 9924)            │
│  3. Database query: Does invoice 9924 belong to organization of "user_corp_a"?      │
│     ├── YES: Proceed to execute invoiceService.getInvoice(9924)                     │
│     └── NO:  Throw AccessDeniedException ──► Handled as HTTP 403 Forbidden          │
│                                                                                     │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Custom Security Evaluator Bean (`InvoiceSecurityEvaluator.java`)
```java
package com.enterprise.security.evaluator;

import com.enterprise.security.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("invoiceSecurity")
@RequiredArgsConstructor
public class InvoiceSecurityEvaluator {

    private final InvoiceRepository invoiceRepository;

    public boolean isInvoiceOwner(Authentication authentication, Long invoiceId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Admins bypass ownership checks
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        String username = authentication.getName();
        return invoiceRepository.existsByIdAndOwnerUsername(invoiceId, username);
    }
}
```

#### 2. Service Enforcing Method Security (`InvoiceService.java`)
```java
package com.enterprise.security.service;

import com.enterprise.security.dto.InvoiceDto;
import com.enterprise.security.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Transactional(readOnly = true)
    @PreAuthorize("@invoiceSecurity.isInvoiceOwner(authentication, #invoiceId)")
    public InvoiceDto getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
            .map(inv -> new InvoiceDto(inv.getId(), inv.getAmount(), inv.getStatus()))
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @invoiceSecurity.isInvoiceOwner(authentication, #invoiceId)")
    public void cancelInvoice(Long invoiceId) {
        invoiceRepository.updateStatus(invoiceId, "CANCELLED");
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, we mirror method-level constraints by evaluating permissions before initiating operations:
1. Provide responsive UX by disabling or hiding buttons (e.g., "Cancel Invoice") if the user is not the owner or lacks the required role.
2. Intercept any `403 Forbidden` response and present a contextual error explaining why the operation was denied.

---

### 2. Production Code (Angular 17+ Standalone)

#### Invoice Detail Component (`invoice-detail.component.ts`)
```typescript
import { Component, Input, inject, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

export interface Invoice {
  id: number;
  amount: number;
  status: string;
  ownerUsername: string;
}

@Component({
  selector: 'app-invoice-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="invoice-container">
      <h3>Invoice #{{ invoice().id }}</h3>
      <p>Amount: {{ invoice().amount | currency }}</p>
      <p>Status: {{ invoice().status }}</p>

      @if (errorMessage()) {
        <div class="alert alert-danger">{{ errorMessage() }}</div>
      }

      <!-- Conditionally enabled based on ownership or admin privilege -->
      <button 
        [disabled]="!canCancelInvoice()" 
        (click)="cancelInvoice()" 
        class="btn btn-warning">
        Cancel Invoice
      </button>
    </div>
  `
})
export class InvoiceDetailComponent {
  private readonly http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  readonly invoice = signal<Invoice>({ id: 9924, amount: 1500, status: 'PENDING', ownerUsername: 'user_corp_a' });
  readonly errorMessage = signal<string | null>(null);

  // Computed permission: Current user is owner OR user has ROLE_ADMIN
  readonly canCancelInvoice = computed(() => {
    const user = this.authService.currentUser();
    if (!user) return false;
    const isAdmin = user.roles.includes('ROLE_ADMIN');
    const isOwner = user.username === this.invoice().ownerUsername;
    return isAdmin || isOwner;
  });

  cancelInvoice(): void {
    this.http.post(`/api/v1/invoices/${this.invoice().id}/cancel`, {}).subscribe({
      next: () => {
        this.invoice.update(inv => ({ ...inv, status: 'CANCELLED' }));
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 403) {
          this.errorMessage.set('Permission Denied: You do not have ownership rights to cancel this invoice.');
        } else {
          this.errorMessage.set('Failed to cancel invoice.');
        }
      }
    });
  }
}
```
