# Q141: Managing Cross-Origin Resource Sharing (CORS) in Spring Boot Microservices?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise platform deployed an Angular SPA on `https://portal.enterprise.com` calling microservices through a Spring Cloud Gateway. Developers configured `@CrossOrigin(origins = "*")` inside each downstream microservice controller, while also configuring CORS at the Gateway. 
Browser preflight `OPTIONS` requests failed with:
`The 'Access-Control-Allow-Origin' header contains multiple values 'https://portal.enterprise.com, *', but only one is allowed.` 

The architectural rule for microservices is: **Manage CORS exclusively at the API Gateway level**, and strip duplicate CORS response headers before returning responses to the browser.

---

### 2. Concept & Architecture

```
┌──────────────────────── Centralized CORS at API Gateway ────────────────────────┐
│                                                                                  │
│   Browser: Preflight OPTIONS https://api.enterprise.com/api/v1/orders            │
│   Headers: Origin: https://portal.enterprise.com                                 │
│            Access-Control-Request-Method: POST                                   │
│                 │                                                                │
│                 ▼                                                                │
│   ┌────────────────────────────────────────────────────────────┐                 │
│   │ Spring Cloud Gateway (globalcors configuration)            │                 │
│   │ 1. Verifies Origin against allowed list                     │                 │
│   │ 2. Handles OPTIONS preflight immediately (Returns 200 OK)  │                 │
│   │ 3. DedupeResponseHeader filter removes any duplicate       │                 │
│   │    Access-Control-Allow-* headers from downstream services │                 │
│   └────────────────────────────────────────────────────────────┘                 │
│                                                                                  │
└──────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

#### Gateway YAML Configuration (`application.yml`)
```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowed-origins:
              - "https://portal.enterprise.com"
              - "http://localhost:4200"
            allowed-methods:
              - GET
              - POST
              - PUT
              - DELETE
              - PATCH
              - OPTIONS
            allowed-headers:
              - Authorization
              - Content-Type
              - X-Correlation-Id
              - X-Requested-With
            allow-credentials: true
            max-age: 3600
      default-filters:
        # Prevents duplicate headers if downstream microservices accidentally write CORS headers
        - DedupeResponseHeader=Access-Control-Allow-Origin Access-Control-Allow-Credentials, RETAIN_FIRST
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
On the Angular frontend, managing CORS involves:
1. **Local Development Proxy:** Using `proxy.conf.json` so Angular CLI serves requests from `localhost:4200/api` through a local reverse proxy, completely avoiding CORS during local debugging.
2. **Production Requests:** Setting `withCredentials: true` when communicating with cookie-authenticated gateways.

---

### 2. Production Code (Angular 17+ Standalone)

#### Angular CLI Development Proxy (`proxy.conf.json`)
```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "info"
  }
}
```
