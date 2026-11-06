# Q134: Handling distributed transaction rollback across multiple database operations?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A flight booking system debited 50,000 loyalty points from Customer DB, reserved seat 12B in Flight DB, and attempted to charge the remaining \$200 to Stripe. The credit card was declined. Because the microservices each had their own database, standard Spring `@Transactional` could not roll back the remote loyalty points or release the seat reservation, resulting in revenue loss and manual support escalations. 

Implementing the **Saga Execution Coordinator (SEC)** pattern with explicit **Compensating Transactions** guaranteed automated distributed rollbacks.

---

### 2. Concept & Architecture

```
┌──────────────────────── Distributed Rollback (SAGA Pattern) ────────────────────────┐
│                                                                                     │
│  Forward Execution:                                                                 │
│  [ Step 1: Debit Points ] ──► Success                                               │
│  [ Step 2: Reserve Seat ] ──► Success                                               │
│  [ Step 3: Charge Card  ] ──► FAILED (Card Declined!)                               │
│                                                                                     │
│  Compensating Backward Rollback:                                                    │
│  [ Comp 1: Release Seat ] ──► Calls FlightService.releaseSeat()                     │
│  [ Comp 2: Refund Points] ──► Calls LoyaltyService.creditPointsBack()               │
│                                                                                     │
│  * Result: System state cleanly restored without Two-Phase Commit distributed locks!│
│                                                                                     │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.booking.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightBookingSagaCoordinator {

    private final RestClient restClient;

    public void bookTrip(String bookingId, Long customerId, int points, String seatNumber) {
        log.info("Starting distributed booking transaction for: {}", bookingId);

        // Step 1: Debit Points
        boolean pointsDebited = executeCall("http://loyalty-service/api/v1/points/debit", customerId, points);
        if (!pointsDebited) throw new IllegalStateException("Failed to debit loyalty points");

        // Step 2: Reserve Seat
        boolean seatReserved = executeCall("http://flight-service/api/v1/seats/reserve", bookingId, seatNumber);
        if (!seatReserved) {
            log.warn("Seat reservation failed. Rolling back Step 1 (Refunding points)...");
            executeCompensation("http://loyalty-service/api/v1/points/refund", customerId, points);
            throw new IllegalStateException("Seat reservation failed. Loyalty points refunded.");
        }

        // Step 3: Charge Payment
        boolean paymentSuccess = executeCall("http://payment-service/api/v1/charges/execute", bookingId, 200);
        if (!paymentSuccess) {
            log.warn("Payment declined! Rolling back Step 2 and Step 1...");
            executeCompensation("http://flight-service/api/v1/seats/release", bookingId, seatNumber);
            executeCompensation("http://loyalty-service/api/v1/points/refund", customerId, points);
            throw new IllegalStateException("Payment declined. Seat released and points refunded.");
        }

        log.info("Distributed booking completed successfully!");
    }

    private boolean executeCall(String url, Object... params) {
        try {
            restClient.post().uri(url, params).retrieve().toBodilessEntity();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void executeCompensation(String url, Object... params) {
        try {
            restClient.post().uri(url, params).retrieve().toBodilessEntity();
        } catch (Exception e) {
            log.error("CRITICAL: Manual intervention required! Compensation failed at: {}", url, e);
        }
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, a multi-step checkout wizard guides the customer through distributed operations:
- Displaying clear progress indicators for each step.
- In the event of a compensation rollback, showing an explanatory message without wiping the customer's form inputs, enabling them to retry with an alternate credit card.

---

### 2. Production Code (Angular 17+ Standalone)

#### Booking Wizard Component (`booking-wizard.component.ts`)
```typescript
import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-booking-wizard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="wizard-box">
      <h3>Flight Reservation Checkout</h3>

      @if (rollbackMessage()) {
        <div class="alert alert-danger">
          <strong>Transaction Rolled Back:</strong> {{ rollbackMessage() }}
        </div>
      }

      <button (click)="confirmBooking()" [disabled]="isProcessing()" class="btn btn-primary">
        {{ isProcessing() ? 'Processing Distributed Reservation...' : 'Complete Reservation ($200 + 50k pts)' }}
      </button>
    </div>
  `
})
export class BookingWizardComponent {
  private readonly http = inject(HttpClient);

  readonly isProcessing = signal(false);
  readonly rollbackMessage = signal<string | null>(null);

  confirmBooking(): void {
    this.isProcessing.set(true);
    this.rollbackMessage.set(null);

    this.http.post('/api/v1/bookings/saga', {
      bookingId: 'BK-' + Date.now(),
      customerId: 42,
      points: 50000,
      seatNumber: '12B'
    }).subscribe({
      next: () => {
        this.isProcessing.set(false);
        alert('Booking successfully confirmed!');
      },
      error: (err: HttpErrorResponse) => {
        this.isProcessing.set(false);
        this.rollbackMessage.set(err.error?.message || 'Payment failed. All loyalty points have been refunded.');
      }
    });
  }
}
```
