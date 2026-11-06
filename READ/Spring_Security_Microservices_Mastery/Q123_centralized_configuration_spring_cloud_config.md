# Q123: Centralized configuration across microservices (Spring Cloud Config Server)?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
During a payment gateway outage, the DevOps team needed to switch 25 microservices from Stripe to PayPal backup API endpoints. Because URLs and secret credentials were baked into container environment variables, switching required editing Kubernetes deployment yamls, running CI/CD pipelines, and restarting all 25 container pods, causing 45 minutes of complete payment downtime. 

Adopting **Spring Cloud Config Server** paired with **`@RefreshScope`** and Spring Cloud Bus allowed the team to push configuration changes to a central Git repository and trigger an immediate zero-downtime refresh across all 25 microservices via a single webhook.

---

### 2. Concept & Architecture

```
┌──────────────────────── Centralized Configuration Architecture ────────────────────────┐
│                                                                                        │
│   [ Git Repository / Vault ] (Encrypted application-prod.yml, payment.gateway.url)     │
│                 │                                                                      │
│                 ▼                                                                      │
│   [ Spring Cloud Config Server (Port 8888) ]                                           │
│                 │                                                                      │
│                 ▼ Push notification via Spring Cloud Bus (Kafka / RabbitMQ)            │
│        ┌────────┴────────────────────────┬────────────────────────┐                    │
│        ▼                                 ▼                        ▼                    │
│   [ Payment Service ]              [ Order Service ]       [ Invoice Service ]         │
│   @RefreshScope reloads properties in memory without JVM restart!                      │
│                                                                                        │
└────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### Dynamic Configuration Bean (`PaymentProperties.java`)
```java
package com.enterprise.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "payment.gateway")
@RefreshScope // Reloads bean when /actuator/refresh or /actuator/busrefresh is called
public class PaymentGatewayProperties {

    private String providerName; // "STRIPE" or "PAYPAL"
    private String apiUrl;
    private int timeoutMs;
    private boolean maintenanceMode;
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
Angular applications must also avoid environment-specific rebuilds. 
Rather than hardcoding API URLs into `environment.prod.ts`, use an **`APP_INITIALIZER`** pattern to fetch an un-cached `config.json` at application bootstrap. This allows a single Docker container image to be promoted across Dev, QA, Staging, and Production.

---

### 2. Production Code (Angular 17+ Standalone)

#### 1. Runtime AppConfig Service (`app-config.service.ts`)
```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

export interface AppRuntimeConfig {
  apiBaseUrl: string;
  paymentGatewayKey: string;
  enableFeatureChat: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AppConfigService {
  private readonly http = inject(HttpClient);
  private configData: AppRuntimeConfig | null = null;

  async loadConfig(): Promise<void> {
    // Fetches static config file from public assets folder at runtime
    this.configData = await firstValueFrom(
      this.http.get<AppRuntimeConfig>('/assets/config/app-config.json')
    );
  }

  get apiBaseUrl(): string {
    return this.configData?.apiBaseUrl || 'https://api.enterprise.com';
  }

  get isChatEnabled(): boolean {
    return this.configData?.enableFeatureChat || false;
  }
}

export function initializeAppConfig(appConfigService: AppConfigService) {
  return () => appConfigService.loadConfig();
}
```

#### 2. Provider in `app.config.ts`
```typescript
import { ApplicationConfig, APP_INITIALIZER } from '@angular/core';
import { AppConfigService, initializeAppConfig } from './core/services/app-config.service';

export const appConfig: ApplicationConfig = {
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeAppConfig,
      deps: [AppConfigService],
      multi: true
    }
  ]
};
```
