# Q122: User Service is down but Order Service depends on it: How to build resilience?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
During a Black Friday traffic surge, the `UserService` crashed due to database connection exhaustion. `OrderService` synchronously called `UserService` on every checkout to retrieve user shipping addresses and VIP discount tiers. Because calls were unshielded, `OrderService` threads blocked waiting for 30-second socket timeouts. Within 2 minutes, `OrderService` exhausted its entire Tomcat thread pool and crashed as well, turning a single-service failure into a total platform outage! 

Implementing **Resilience4j Circuit Breaker, TimeLimiter, and Fallback with Cache-Aside** protected `OrderService`, allowing orders to continue with default discounts and cached addresses.

---

### 2. Concept & Architecture

```
┌──────────────────────────────── Resilience4j Circuit Breaker ────────────────────────────────┐
│                                                                                              │
│   [ Order Service ] ──► Calls UserService.getUserProfile(id)                                 │
│                                                                                              │
│        State: CLOSED (Normal Operation: 99% requests succeed)                                │
│               │ (Error rate exceeds 50% threshold)                                           │
│               ▼                                                                              │
│        State: OPEN (Trip the Circuit!)                                                       │
│               • Fails fast immediately without waiting for HTTP socket timeout!              │
│               • Diverts traffic directly to Fallback Method:                                 │
│                 1. Read profile from Redis Cache                                             │
│                 2. Fallback to default Guest/Standard tier if Redis missed                   │
│               │                                                                              │
│               ▼ (Wait 10 seconds: waitDurationInOpenState)                                   │
│        State: HALF_OPEN (Probe testing: allow 5 test requests)                               │
│               ├── If test calls succeed ──► Returns to CLOSED                                │
│               └── If test calls fail    ──► Re-trips to OPEN                                 │
│                                                                                              │
└──────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Resilient Service with Fallback & Redis Caching (`ResilientOrderService.java`)
```java
package com.enterprise.order.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResilientOrderService {

    private final RestClient restClient;
    private final RedisTemplate<String, UserProfileDto> redisTemplate;

    public record UserProfileDto(Long userId, String tier, BigDecimal discountRate) {}

    @CircuitBreaker(name = "userServiceBreaker", fallbackMethod = "getUserProfileFallback")
    @TimeLimiter(name = "userServiceLimiter")
    public CompletableFuture<UserProfileDto> getUserProfileAsync(Long userId) {
        return CompletableFuture.supplyAsync(() -> 
            restClient.get()
                .uri("http://user-service/api/v1/users/{id}", userId)
                .retrieve()
                .body(UserProfileDto.class)
        );
    }

    /**
     * Fallback method executed immediately when Circuit Breaker is OPEN or call times out
     */
    public CompletableFuture<UserProfileDto> getUserProfileFallback(Long userId, Throwable throwable) {
        log.warn("UserService unavailable ({}), executing resilient fallback for userId: {}", 
                 throwable.getMessage(), userId);

        // 1. Try reading from Redis cache
        UserProfileDto cached = redisTemplate.opsForValue().get("user:profile:" + userId);
        if (cached != null) {
            log.info("Resolved profile from Redis cache for userId: {}", userId);
            return CompletableFuture.completedFuture(cached);
        }

        // 2. Fallback to safe default values (0% discount, STANDARD tier)
        log.info("Redis cache missed, applying default STANDARD tier for userId: {}", userId);
        return CompletableFuture.completedFuture(new UserProfileDto(userId, "STANDARD", BigDecimal.ZERO));
    }
}
```

#### 2. Resilience4j Configuration (`application.yml`)
```yaml
resilience4j:
  circuitbreaker:
    instances:
      userServiceBreaker:
        sliding-window-type: COUNT_BASED
        sliding-window-size: 10
        failure-rate-threshold: 50.0
        wait-duration-in-open-state: 10000ms
        permitted-number-of-calls-in-half-open-state: 3
        automatic-transition-from-open-to-half-open-enabled: true
  timelimiter:
    instances:
      userServiceLimiter:
        timeout-duration: 2000ms # Max 2 seconds wait time
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
When downstream services fail and the backend provides degraded fallbacks:
1. Angular must alert the customer without blocking their purchase.
2. Present a banner: *"VIP discounts are temporarily unavailable. Your standard order will proceed, and any eligible rewards will be credited after checkout."*

---

### 2. Production Code (Angular 17+ Standalone)

#### Checkout Component with Graceful Degradation (`checkout.component.ts`)
```typescript
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

export interface OrderCheckoutResponse {
  orderId: string;
  appliedTier: string;
  isDegradedMode: boolean;
  total: number;
}

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="checkout-card">
      <h3>Checkout Summary</h3>

      @if (isDegraded()) {
        <div class="alert alert-warning">
          <strong>Notice:</strong> Customer Loyalty Services are undergoing scheduled maintenance. 
          Your order will proceed under the standard tier, and loyalty points will be reconciled shortly.
        </div>
      }

      <p>Order Total: {{ orderTotal() | currency }}</p>
      <button (click)="placeOrder()" class="btn btn-success">Confirm Purchase</button>
    </div>
  `
})
export class CheckoutComponent implements OnInit {
  private readonly http = inject(HttpClient);

  readonly isDegraded = signal(false);
  readonly orderTotal = signal(120.00);

  ngOnInit(): void {
    this.http.get<OrderCheckoutResponse>('/api/v1/orders/pre-checkout').subscribe(res => {
      this.isDegraded.set(res.isDegradedMode);
      this.orderTotal.set(res.total);
    });
  }

  placeOrder(): void {
    this.http.post('/api/v1/orders/confirm', {}).subscribe(() => {
      alert('Order placed successfully!');
    });
  }
}
```
