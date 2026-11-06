# Q125: Service Discovery & Client-Side Load Balancing (Eureka / Spring Cloud Gateway)?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
During seasonal autoscaling, an e-commerce platform dynamically launched 12 new instances of the `CatalogService`. However, `OrderService` continued routing requests exclusively to the original 2 instances because IP addresses were statically hardcoded in configuration files. The 2 original instances crashed under 100% CPU load while the 12 new instances sat completely idle. 

Implementing **Service Discovery (Netflix Eureka / Consul)** combined with **Spring Cloud LoadBalancer** enabled instances to self-register upon boot and allowed callers to distribute traffic dynamically via round-robin client-side load balancing.

---

### 2. Concept & Architecture

```
┌──────────────────────── Service Discovery & Load Balancing ────────────────────────┐
│                                                                                    │
│                  [ Eureka Service Registry (Port 8761) ]                           │
│                  Maintains dynamic registry of healthy instance IP:Port mappings   │
│                             ▲                        ▲                             │
│       Registers on Boot     │                        │ Registers on Boot           │
│       [ Catalog Service A ] │                        │ [ Catalog Service B ]       │
│       (10.0.1.14:8080)      │                        │ (10.0.1.15:8080)            │
│                             │                        │                             │
│                             └───────────┬────────────┘                             │
│                                         │ Heartbeat every 30s                      │
│                                         ▼                                          │
│                        [ Spring Cloud Gateway / Feign ]                            │
│                        Discovers instances via lb://catalog-service                │
│                        Applies Round-Robin Client-Side Load Balancing              │
│                                                                                    │
└────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayLoadBalancingConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Dynamically load balances across all registered catalog-service instances
            .route("catalog-route", r -> r.path("/api/v1/catalog/**")
                .uri("lb://catalog-service"))
            .route("order-route", r -> r.path("/api/v1/orders/**")
                .uri("lb://order-service"))
            .build();
    }

    @Bean
    @LoadBalanced // Enables Spring Cloud LoadBalancer on WebClient
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
The Angular application points exclusively to the **API Gateway URL** (e.g., `https://api.enterprise.com`). 
Client code does not need to know microservice instance counts, IP addresses, or load balancing topologies; the API Gateway acts as the unified reverse proxy.

---

### 2. Production Code (Angular 17+ Standalone)

#### Gateway Proxy Configuration (`proxy.conf.json`)
```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "debug"
  }
}
```
