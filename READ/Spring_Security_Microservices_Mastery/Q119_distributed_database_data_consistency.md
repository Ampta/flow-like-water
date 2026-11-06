# Q119: Multiple microservices accessing different databases: How to maintain data consistency?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
In a digital fintech platform, a customer initiated a money transfer from Account Service to Investment Service. Account Service debited \$5,000 from the user's balance and committed the transaction. Before Investment Service could credit the portfolio, the network connection dropped. The customer was out \$5,000 with zero shares credited! 

Traditional Two-Phase Commit (2PC / XA) distributed transactions failed under load due to distributed database deadlocks and high latency. Implementing the **Saga Pattern (Orchestration)** with automated compensating rollback transactions ensured eventual data consistency.

---

### 2. Concept & Architecture

```
┌──────────────────────────────── Orchestrated SAGA Workflow ────────────────────────────────┐
│                                                                                            │
│                          [ Saga Orchestrator (TransferSaga) ]                              │
│                                           │                                                │
│         Step 1: Execute                       Step 2: Execute                              │
│         Debit Account                         Credit Investment Portfolio                  │
│         ┌──────────────────────┐              ┌──────────────────────┐                     │
│         │ Account Service      │              │ Investment Service   │                     │
│         │ (PostgreSQL)         │              │ (PostgreSQL)         │                     │
│         └──────────────────────┘              └──────────────────────┘                     │
│                   │                                      │                                 │
│                   ▼ Success                              ▼ FAILURE (e.g. Portfolio Locked) │
│                                                                                            │
│         Step 3: Trigger Compensation                                                       │
│         Refund Debited Account                                                             │
│         ┌──────────────────────┐                                                           │
│         │ Account Service      │ ──► Restores $5,000 balance!                              │
│         └──────────────────────┘                                                           │
│                                                                                            │
│  * Result: Eventual Consistency guaranteed. No two-phase commit lock bottlenecks!         │
│                                                                                            │
└────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### Saga Orchestrator Implementation (`TransferSagaOrchestrator.java`)
```java
package com.enterprise.saga.orchestrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferSagaOrchestrator {

    private final RestClient restClient;

    public record TransferCommand(String transferId, Long sourceAccountId, Long portfolioId, BigDecimal amount) {}

    public void executeTransferSaga(TransferCommand command) {
        log.info("Starting Transfer Saga ID: {}", command.transferId());

        // Step 1: Debit Source Account
        boolean debitSuccess = debitAccount(command.sourceAccountId(), command.amount());
        if (!debitSuccess) {
            log.error("Saga aborted: Failed to debit account: {}", command.sourceAccountId());
            return;
        }

        // Step 2: Credit Investment Portfolio
        boolean creditSuccess = creditPortfolio(command.portfolioId(), command.amount());

        // Step 3: Check for failure and trigger Compensating Transaction
        if (!creditSuccess) {
            log.warn("Step 2 failed! Executing Compensating Transaction: Refunding account {}", command.sourceAccountId());
            refundAccount(command.sourceAccountId(), command.amount());
            throw new IllegalStateException("Saga Failed: Investment deposit rejected. Funds refunded.");
        }

        log.info("Transfer Saga completed successfully for ID: {}", command.transferId());
    }

    private boolean debitAccount(Long accountId, BigDecimal amount) {
        try {
            restClient.post()
                .uri("http://account-service/api/v1/accounts/{id}/debit", accountId)
                .body(amount)
                .retrieve()
                .toBodilessEntity();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean creditPortfolio(Long portfolioId, BigDecimal amount) {
        try {
            restClient.post()
                .uri("http://investment-service/api/v1/portfolios/{id}/credit", portfolioId)
                .body(amount)
                .retrieve()
                .toBodilessEntity();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void refundAccount(Long accountId, BigDecimal amount) {
        restClient.post()
            .uri("http://account-service/api/v1/accounts/{id}/refund", accountId)
            .body(amount)
            .retrieve()
            .toBodilessEntity();
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
Because Saga transactions take time to achieve eventual consistency across multiple services, the frontend must not pretend the operation is instantaneous:
1. Submit transfer and display an **Optimistic Pending State**.
2. Poll or listen to webhooks for transaction completion.
3. If compensating rollback occurs, display clear error recovery instructions to the user.

---

### 2. Production Code (Angular 17+ Standalone)

#### Saga Transaction Monitor (`transfer-wizard.component.ts`)
```typescript
import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { interval, switchMap, takeWhile } from 'rxjs';

@Component({
  selector: 'app-transfer-wizard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="saga-container">
      <h3>Multi-Service Fund Transfer</h3>

      <button (click)="initiateTransfer()" [disabled]="isProcessing()" class="btn btn-primary">
        {{ isProcessing() ? 'Orchestrating Saga...' : 'Transfer $5,000' }}
      </button>

      @if (sagaStatus()) {
        <div class="alert mt-3" [ngClass]="alertClass()">
          Status: <strong>{{ sagaStatus() }}</strong>
          <p>{{ statusMessage() }}</p>
        </div>
      }
    </div>
  `
})
export class TransferWizardComponent {
  private readonly http = inject(HttpClient);

  readonly isProcessing = signal(false);
  readonly sagaStatus = signal<'PENDING' | 'SUCCESS' | 'COMPENSATED_FAILED' | null>(null);
  readonly statusMessage = signal('');

  get alertClass(): string {
    const s = this.sagaStatus();
    if (s === 'SUCCESS') return 'alert-success';
    if (s === 'COMPENSATED_FAILED') return 'alert-danger';
    return 'alert-info';
  }

  initiateTransfer(): void {
    this.isProcessing.set(true);
    this.sagaStatus.set('PENDING');
    this.statusMessage.set('Contacting Account and Investment Services...');

    const transferId = 'TX-' + Date.now();

    this.http.post<{ sagaId: string }>('/api/v1/transfers', {
      transferId,
      sourceAccountId: 101,
      portfolioId: 202,
      amount: 5000
    }).subscribe({
      next: () => this.pollSagaStatus(transferId),
      error: () => {
        this.isProcessing.set(false);
        this.sagaStatus.set('COMPENSATED_FAILED');
        this.statusMessage.set('Transfer failed immediately. No funds were debited.');
      }
    });
  }

  private pollSagaStatus(transferId: string): void {
    interval(1500).pipe(
      switchMap(() => this.http.get<{ status: 'PENDING' | 'SUCCESS' | 'COMPENSATED_FAILED'; detail: string }>(`/api/v1/transfers/${transferId}/status`)),
      takeWhile(res => res.status === 'PENDING', true)
    ).subscribe(result => {
      this.sagaStatus.set(result.status);
      this.statusMessage.set(result.detail);
      if (result.status !== 'PENDING') {
        this.isProcessing.set(false);
      }
    });
  }
}
```
