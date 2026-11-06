# Q120: Handling aggregated responses from multiple microservices in a single API call?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A mobile banking dashboard required data from 5 microservices: Account Balances, Recent Transactions, Credit Score, Loan Offers, and Notifications. The original mobile app made 5 separate HTTP calls sequentially. On high-latency mobile networks, loading the dashboard took 4.5 seconds, and partial failures left users with broken, incomplete screens. 

Implementing the **Backend-For-Frontend (BFF) Aggregator Pattern** in Spring Boot using asynchronous parallel execution (`CompletableFuture.allOf()`) reduced dashboard load time to **420ms** and handled non-critical service failures gracefully.

---

### 2. Concept & Architecture

```
┌───────────────────────────────── BFF Aggregator Pattern ─────────────────────────────────┐
│                                                                                          │
│   [ Angular / Mobile Client ]                                                            │
│               │                                                                          │
│               ▼ Single Call: GET /api/v1/dashboard/summary                               │
│   ┌────────────────────────────────────────────────────────┐                             │
│   │ Spring Boot BFF Aggregator                             │                             │
│   │                                                        │                             │
│   │  Fork 3 Asynchronous Worker Threads in Parallel:       │                             │
│   │  ├── Thread 1: AccountService.getBalance() [50ms]      │                             │
│   │  ├── Thread 2: TransactionService.getRecent() [120ms]  │                             │
│   │  └── Thread 3: CreditService.getCreditScore() [380ms]  │                             │
│   │                                                        │                             │
│   │  CompletableFuture.allOf(f1, f2, f3).join()            │                             │
│   │  Aggregate results into typed DashboardSummaryDto      │                             │
│   └────────────────────────────────────────────────────────┘                             │
│               │                                                                          │
│               ▼ Total Latency = Max(f1, f2, f3) ≈ 380ms (NOT 50 + 120 + 380 = 550ms!)    │
│   [ Returns Unified JSON Payload ]                                                       │
│                                                                                          │
└──────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.bff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardAggregatorService {

    private final RestClient restClient;
    private final Executor asyncTaskExecutor;

    public record BalanceDto(BigDecimal totalBalance, String currency) {}
    public record TransactionDto(String id, String description, BigDecimal amount) {}
    public record CreditScoreDto(int score, String rating) {}

    public record DashboardSummary(
        BalanceDto balance,
        List<TransactionDto> recentTransactions,
        CreditScoreDto creditScore
    ) {}

    public DashboardSummary getCustomerDashboard(Long customerId) {
        // 1. Launch concurrent asynchronous calls
        CompletableFuture<BalanceDto> balanceFuture = CompletableFuture.supplyAsync(
            () -> fetchBalance(customerId), asyncTaskExecutor
        );

        CompletableFuture<List<TransactionDto>> transactionsFuture = CompletableFuture.supplyAsync(
            () -> fetchTransactions(customerId), asyncTaskExecutor
        );

        CompletableFuture<CreditScoreDto> creditFuture = CompletableFuture.supplyAsync(
            () -> fetchCreditScore(customerId), asyncTaskExecutor
        ).exceptionally(ex -> {
            log.warn("Credit score service unavailable, providing degraded fallback", ex);
            return new CreditScoreDto(0, "UNAVAILABLE");
        });

        // 2. Wait for all parallel futures to complete
        CompletableFuture.allOf(balanceFuture, transactionsFuture, creditFuture).join();

        // 3. Assemble unified aggregate DTO
        return new DashboardSummary(
            balanceFuture.join(),
            transactionsFuture.join(),
            creditFuture.join()
        );
    }

    private BalanceDto fetchBalance(Long customerId) {
        return restClient.get()
            .uri("http://account-service/api/v1/customers/{id}/balance", customerId)
            .retrieve()
            .body(BalanceDto.class);
    }

    @SuppressWarnings("unchecked")
    private List<TransactionDto> fetchTransactions(Long customerId) {
        return restClient.get()
            .uri("http://transaction-service/api/v1/customers/{id}/recent", customerId)
            .retrieve()
            .body(List.class);
    }

    private CreditScoreDto fetchCreditScore(Long customerId) {
        return restClient.get()
            .uri("http://credit-service/api/v1/customers/{id}/score", customerId)
            .retrieve()
            .body(CreditScoreDto.class);
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, consuming an Aggregated BFF endpoint simplifies UI architecture:
1. Eliminates complex client-side `forkJoin` combinations.
2. Supports graceful rendering of degraded widgets (e.g. if the credit score returned `"UNAVAILABLE"`, the widget displays an alert without breaking balances or transactions).

---

### 2. Production Code (Angular 17+ Standalone)

#### Executive Dashboard Component (`dashboard.component.ts`)
```typescript
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

export interface DashboardSummary {
  balance: { totalBalance: number; currency: string };
  recentTransactions: Array<{ id: string; description: string; amount: number }>;
  creditScore: { score: number; rating: string };
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    @if (isLoading()) {
      <div class="spinner">Loading your consolidated banking portfolio...</div>
    } @else if (dashboard()) {
      <div class="dashboard-grid">
        <!-- Balance Widget -->
        <div class="card balance-card">
          <h4>Total Balance</h4>
          <h2>{{ dashboard()!.balance.totalBalance | currency:dashboard()!.balance.currency }}</h2>
        </div>

        <!-- Credit Score Widget with Fallback Detection -->
        <div class="card score-card">
          <h4>Credit Health</h4>
          @if (dashboard()!.creditScore.rating === 'UNAVAILABLE') {
            <span class="badge badge-warning">Service Temporarily Degraded</span>
          } @else {
            <div class="score">{{ dashboard()!.creditScore.score }} ({{ dashboard()!.creditScore.rating }})</div>
          }
        </div>

        <!-- Transactions Widget -->
        <div class="card transactions-card">
          <h4>Recent Activity</h4>
          <ul>
            @for (tx of dashboard()!.recentTransactions; track tx.id) {
              <li>{{ tx.description }}: {{ tx.amount | currency }}</li>
            }
          </ul>
        </div>
      </div>
    }
  `
})
export class DashboardComponent implements OnInit {
  private readonly http = inject(HttpClient);

  readonly isLoading = signal(true);
  readonly dashboard = signal<DashboardSummary | null>(null);

  ngOnInit(): void {
    // Single consolidated HTTP request to the Spring Boot BFF Aggregator
    this.http.get<DashboardSummary>('/api/v1/dashboard/summary').subscribe({
      next: (data) => {
        this.dashboard.set(data);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }
}
```
