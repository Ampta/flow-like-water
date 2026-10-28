# 🌐 Top 20 Microservices Interview Questions & Answers

---

### Q1: What is Microservices Architecture, and how does it compare to Monolithic Architecture?
**Answer:**
A **Microservices Architecture** structures an application as a collection of loosely coupled, independently deployable, fine-grained services organized around business capabilities. Each service maintains its own codebase, CI/CD pipeline, and database.

**Monolith vs Microservices Comparison:**
| Dimension | Monolithic Architecture | Microservices Architecture |
| :--- | :--- | :--- |
| **Deployment** | Single large artifact deployed all-at-once | Independent, frequent deployments per service |
| **Scalability** | Horizontal scaling of entire application | Fine-grained scaling of bottleneck services only |
| **Tech Diversity** | Single technology stack | Polyglot (choose right language/DB for the domain) |
| **Fault Isolation** | Memory leak or crash in one module brings down entire app | Failure is isolated to a single service with circuit breakers |
| **Complexity** | High codebase & dependency complexity | High operational, network & distributed data complexity |
| **Consistency** | ACID transactions supported natively | Eventual consistency via BASE model |

---

### Q2: What is Service Discovery, and how does Client-Side vs Server-Side Discovery work?
**Answer:**
In microservices, service instances dynamically spin up and down (e.g., autoscaling in Kubernetes/Cloud), meaning their IP addresses and ports change constantly. **Service Discovery** maintains a dynamic registry of active service instances.

**1. Client-Side Discovery (e.g., Spring Cloud Netflix Eureka / OpenFeign):**
- Client queries the Service Registry (Eureka) to get available instance addresses.
- Client runs load balancing algorithms locally (e.g., Spring Cloud LoadBalancer) and calls the target directly.
- *Pros:* No extra network hop. *Cons:* Client tightly coupled to discovery library.

**2. Server-Side Discovery (e.g., Kubernetes Services, AWS ALB):**
- Client makes requests to a fixed router/load balancer.
- The router queries the registry and forwards traffic to an available backend pod.
- *Pros:* Language-agnostic, simple client. *Cons:* Additional network hop.

---

### Q3: What is the API Gateway pattern, and what functions does Spring Cloud Gateway perform?
**Answer:**
An **API Gateway** acts as the single point of entry for external clients (Mobile, Web, 3rd party), encapsulating the internal system architecture.

**Key Responsibilities:**
1. **Dynamic Routing:** Directs requests to downstream microservices based on paths, headers, or query params.
2. **Authentication & Authorization:** Validates JWT/OAuth2 tokens before requests enter internal network.
3. **Rate Limiting & Throttling:** Protects services from DDoS or overload (e.g., using Redis Token Bucket).
4. **SSL Termination:** Offloads HTTPS decryption at the gateway layer.
5. **Cross-Cutting Concerns:** CORS configuration, request transformation, global response caching.

**Spring Cloud Gateway Example:**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: order-service
          uri: lb://ORDER-SERVICE
          predicates:
            - Path=/api/v1/orders/**
          filters:
            - AddRequestHeader=X-Gateway-Source, Edge
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
```

---

### Q4: Compare Synchronous vs Asynchronous Communication in Microservices.
**Answer:**
| Dimension | Synchronous (REST, gRPC) | Asynchronous (Kafka, RabbitMQ) |
| :--- | :--- | :--- |
| **Protocol** | HTTP/1.1, HTTP/2 | AMQP, Kafka Protocol |
| **Coupling** | Temporal coupling (caller waits for response) | Decoupled (publisher doesn't care when consumer reads) |
| **Latency** | Immediate feedback | Eventual processing |
| **Resilience** | Cascading failure risk if callee is down | High resilience; message queues buffer spikes |
| **Best Used For** | Immediate queries (e.g., user profile, search) | Event notifications, orders, payments, background tasks |

**Rule of Thumb:** Prefer asynchronous event-driven communication across bounded contexts; use synchronous RPC (gRPC / Feign) only when an immediate query response is mandatory.

---

### Q5: Explain the Circuit Breaker Pattern and Resilience4j states.
**Answer:**
The **Circuit Breaker** pattern prevents cascading failures across distributed microservices by stopping calls to a failing downstream service when a failure threshold is exceeded.

**Resilience4j State Machine:**
1. **CLOSED (Normal):** Requests pass through. If failures (HTTP 500, timeouts) exceed threshold (e.g., 50% over 20 calls), circuit transitions to **OPEN**.
2. **OPEN (Tripped):** Requests fail immediately without hitting the downstream service. Fallback method is executed directly.
3. **HALF-OPEN (Testing):** After a configurable wait duration (e.g., 10 seconds), a limited number of test requests are permitted through. If they succeed, circuit transitions back to **CLOSED**; if they fail, returns to **OPEN**.

```java
@Service
public class OrderService {
    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    @Retry(name = "paymentService")
    public PaymentResponse processPayment(OrderRequest request) {
        return paymentClient.executePayment(request);
    }

    public PaymentResponse paymentFallback(OrderRequest request, Throwable t) {
        return new PaymentResponse("QUEUED_FOR_OFFLINE_PROCESSING", request.orderId());
    }
}
```

---

### Q6: What is Distributed Tracing, and how do Trace ID, Span ID, and Micrometer Tracing work?
**Answer:**
A single user request in microservices may traverse 10+ distinct services. **Distributed Tracing** allows developers to trace the entire path, latency, and logs of a request across the fleet.

**Core Concepts:**
- **Trace ID:** A globally unique identifier assigned to the initial request at the API Gateway and propagated via HTTP headers (W3C `traceparent` or B3 propagation: `X-B3-TraceId`).
- **Span ID:** Represents a single unit of contiguous work within a specific microservice.
- **Parent Span ID:** Links child spans to parent spans to form a Directed Acyclic Graph (DAG) tree of calls.

**Spring Boot 3 Implementation:**
Replaced Spring Cloud Sleuth with **Micrometer Tracing** + OpenTelemetry / Zipkin / Jaeger:
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-otel</artifactId>
</dependency>
<dependency>
    <groupId>io.opentelemetry</groupId>
    <artifactId>opentelemetry-exporter-zipkin</artifactId>
</dependency>
```

---

### Q7: What is the Saga Pattern, and how do Orchestration and Choreography Sagas differ?
**Answer:**
Because microservices utilize the Database-per-Service pattern, traditional ACID two-phase commits (2PC) do not scale. The **Saga Pattern** manages distributed transactions as a sequence of local transactions where each local transaction updates the database and emits an event. If a step fails, compensating transactions are executed to undo changes.

**1. Choreography-Based Saga (Decentralized):**
- Services publish domain events to a broker (e.g., Kafka).
- Other services listen and react by executing their local transaction.
- *Pros:* Simple for small workflows, no central point of failure.
- *Cons:* Can quickly become spaghetti; hard to debug or visualize complete workflow.

**2. Orchestration-Based Saga (Centralized Coordinator):**
- A central Orchestrator (e.g., Camunda, Temporal, or custom Spring Boot state machine) sends direct commands to participating services and listens for responses.
- If `PaymentService` succeeds but `InventoryService` fails, the Orchestrator explicitly sends a `RefundPayment` command.
- *Pros:* Clear workflow visualization, centralized business logic, easier error handling.
- *Cons:* Risk of orchestrator becoming overly complex or a single point of failure.

---

### Q8: What is CQRS (Command Query Responsibility Segregation) and Event Sourcing?
**Answer:**
**CQRS:** Segregates read and write operations into separate models and data stores:
- **Command Model (Write):** Highly optimized for transactional writes, business invariant validation, and normalized database tables (e.g., PostgreSQL).
- **Query Model (Read):** Denormalized, read-optimized views cached in Elasticsearch, MongoDB, or Redis for instant sub-millisecond retrieval.
- Synchronized asynchronously via domain events emitted by the command side.

**Event Sourcing:**
- Instead of storing only the *current state* of an entity, the application stores every mutation as an append-only sequence of immutable events in an **Event Store**.
- Current state is reconstructed by replaying all historical events.
- Provides complete, indisputable audit trails and temporal time-travel debugging.

---

### Q9: How does Centralized Configuration work with Spring Cloud Config Server and Dynamic Refresh?
**Answer:**
**Spring Cloud Config Server** centralizes configuration across environments (Dev, QA, Prod) backed by Git, Vault, or SVN.

**Dynamic Reloading (`@RefreshScope`):**
- Microservice beans annotated with `@RefreshScope` re-instantiate when a refresh event is triggered.
- **Spring Cloud Bus:** Uses a lightweight message broker (Kafka or RabbitMQ) to broadcast a single `/actuator/busrefresh` request across all running microservice instances, reloading properties in memory with zero downtime.

```java
@RestController
@RefreshScope
public class DiscountController {
    @Value("${discount.rate:0.05}")
    private double discountRate;

    @GetMapping("/discount")
    public double getDiscount() { return discountRate; }
}
```

---

### Q10: What is the Database-per-Service pattern, and how do you handle cross-service queries?
**Answer:**
Each microservice owns its private database. No other service is allowed direct read/write access to another service's tables. This guarantees loose coupling and independent schema evolution.

**Handling Cross-Service Data Retrieval:**
1. **API Composition / Aggregator Pattern:** The API Gateway or an Aggregator Service queries multiple services in parallel and merges the JSON payloads. (Simple, but potential latency bottleneck).
2. **CQRS with Read Replicas:** The consumer service consumes events from other services and maintains its own denormalized read-model projection.
3. **Data Replication / CDC:** Stream changes asynchronously into an analytics data warehouse or Elasticsearch.

---

### Q11: What is Idempotency, and how do you guarantee it in distributed payment/order APIs?
**Answer:**
An operation is **idempotent** if performing it multiple times produces the exact same outcome as performing it once ($f(f(x)) = f(x)$). Crucial because network timeouts often cause clients to retry requests.

**Implementation Strategy (Idempotency Key):**
1. Client generates a unique UUID `Idempotency-Key` and includes it in HTTP header.
2. Microservice checks Redis / Database:
   - If key exists with status `SUCCESS`, return cached response immediately.
   - If key exists with status `PROCESSING`, reject with `409 Conflict` (concurrent request).
   - If key does not exist, insert key with status `PROCESSING` and a TTL.
3. Process the transaction within a database transaction.
4. Update key status to `SUCCESS` and cache response.

---

### Q12: How do you implement End-to-End Security across Microservices?
**Answer:**
1. **Edge Authentication (OAuth2 / OIDC):**
   - User authenticates against an Identity Provider (Keycloak, Okta, Auth0) and receives a signed JWT.
   - The API Gateway validates JWT signature, issuer, and expiration.
2. **Context Propagation (JWT Forwarding):**
   - Gateway forwards the validated JWT in the `Authorization: Bearer <token>` header downstream.
   - Downstream services parse claims (User ID, Roles, Tenant) without querying the auth server.
3. **Zero-Trust Network & mTLS:**
   - Internal service-to-service communication is encrypted and mutually authenticated via **mTLS** (Mutual TLS) using a Service Mesh (Istio) or SPIFFE/SPIRE.
4. **Service-to-Service Authorization:**
   - Machines authenticate using OAuth2 Client Credentials grant to obtain service-specific tokens.

---

### Q13: What is the Strangler Fig Pattern for monolithic migration?
**Answer:**
The **Strangler Fig Pattern** incrementally replaces parts of a legacy monolithic system with new microservices until the monolith has been completely strangled and can be decommissioned.

**Phases:**
1. **Identify Boundary:** Select a self-contained bounded context with low coupling (e.g., Notification or Review service).
2. **Build Microservice:** Develop the new service using modern architecture.
3. **Intercept Traffic:** Place an API Gateway (or Reverse Proxy like NGINX) in front of both monolith and microservice.
4. **Reroute:** Route specific API paths to the new microservice while sending remaining traffic to the monolith.
5. **Repeat & Retire:** Incrementally migrate domains until the monolith is an empty shell.

---

### Q14: What is the Transactional Outbox Pattern and Change Data Capture (CDC)?
**Answer:**
**The Dual-Write Problem:** Updating a database and publishing a message to Kafka inside a single method cannot be done in a single distributed ACID transaction. If the database commit succeeds but Kafka fails (or server crashes), data becomes permanently inconsistent.

**Transactional Outbox Solution:**
1. Inside the **same local database transaction**, write the business entity to `orders` table AND write the message payload to an `outbox` table.
2. Both succeed or rollback atomically!
3. **Change Data Capture (CDC):** A background process (like **Debezium** reading Postgres WAL / MySQL binlog) detects new rows in the `outbox` table and publishes them reliably to Kafka.
4. Once published, the outbox record is marked processed or deleted.

---

### Q15: What is a Service Mesh, and how does it compare to Spring Cloud?
**Answer:**
A **Service Mesh** (e.g., **Istio**, Linkerd) is a dedicated infrastructure layer that controls service-to-service communication, deployed as **sidecar proxies (Envoy)** next to each application container.

**Comparison:**
| Feature | Spring Cloud | Service Mesh (Istio + Envoy) |
| :--- | :--- | :--- |
| **Architecture** | In-process Java libraries compiled into JAR | Out-of-process sidecar proxy per Pod |
| **Language Support** | Java / JVM only | Polyglot (Go, Python, Node, Java, Rust) |
| **Updates** | Requires code dependency upgrade & redeploy | Independent platform/proxy upgrades |
| **Features** | Eureka, Resilience4j, Sleuth | Traffic shifting (canary), mTLS, Envoy telemetry |
| **Resource Overhead**| Added memory per JVM heap | Extra CPU/RAM per container sidecar |

---

### Q16: How do Kubernetes primitives map to Microservices Architecture?
**Answer:**
- **Pod:** The smallest deployable unit; runs one or more tightly coupled containers (app + logging/mesh sidecar).
- **Deployment:** Manages declarative rolling updates, rollbacks, and replica sets.
- **Service (ClusterIP):** Internal stable IP and DNS entry that load-balances requests across ephemeral Pods.
- **Ingress / Gateway API:** Inbound traffic controller routing external HTTP/HTTPS traffic to internal services.
- **ConfigMap & Secret:** Externalized configuration and credentials mounted into Pods as environment variables or volume files.
- **HPA (Horizontal Pod Autoscaler):** Automatically scales pod replica count based on CPU, memory, or custom Prometheus metrics.

---

### Q17: How do you handle Dead Letter Queues (DLQ) and Poison Pill messages in Event Streams?
**Answer:**
A **poison pill** is a malformed message that consistently causes the consumer to fail or crash, blocking the queue/partition partition head.

**DLQ Architecture:**
1. Configure consumer retry with exponential backoff (e.g., 3 retries: 1s, 2s, 4s).
2. If all retries fail, a `DeadLetterPublishingRecoverer` intercepts the failure.
3. The failed message, along with failure stack trace and metadata headers (`x-exception-message`, `x-original-topic`), is written to a **Dead Letter Queue (DLQ)** topic (e.g., `orders.DLQ`).
4. Consumer commits the offset on the primary topic, unblocking the partition.
5. An automated alerting dashboard or admin tool allows developers to inspect, fix, and re-drive DLQ messages.

---

### Q18: Explain Kubernetes Liveness, Readiness, and Startup Probes with Spring Boot Actuator.
**Answer:**
Spring Boot 2.3+ natively exposes dedicated health groups for Kubernetes probes:
- `/actuator/health/liveness`: Checks if the internal state is valid (`LivenessState.CORRECT`). If this probe fails, Kubernetes **kills and restarts the container**.
- `/actuator/health/readiness`: Checks if the application is ready to accept user traffic (`ReadinessState.ACCEPTING_TRAFFIC`). If this fails, Kubernetes **removes the Pod from the Service load balancer endpoints**.
- `/actuator/health/startup`: Protects slow-starting applications from being killed before initialization completes.

```yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8080
  initialDelaySeconds: 30
readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8080
```

---

### Q19: What is Consumer-Driven Contract Testing (Pact / Spring Cloud Contract)?
**Answer:**
In microservices, end-to-end integration environments are fragile, slow, and expensive. **Consumer-Driven Contract Testing** verifies that services can communicate without needing live downstream environments.

**How it works:**
1. The **Consumer** defines expectations in a contract file (e.g., "When I send `GET /users/1`, I expect `200 OK` with JSON fields `{id, name}`").
2. The contract is stored in a central repository (e.g., Pact Broker).
3. The **Provider** runs unit tests that replay the contract against its real controllers.
4. If a provider developer deletes or renames a field consumed by the client, the provider's CI build fails **before deploying to production**.

---

### Q20: What are Distributed Caching strategies in Microservices?
**Answer:**
1. **Cache-Aside (Lazy Loading):** Application queries cache. On cache miss, reads from database, updates cache, and returns. (Most common).
2. **Write-Through:** Application writes to cache first; cache immediately writes to database synchronously before returning success.
3. **Write-Behind (Write-Back):** Application writes to cache; cache asynchronously writes batches to the database. (High write performance, risk of data loss on crash).
4. **Cache Invalidation on Update:**
   - In microservices, when Service A updates an entity, it emits an event (`UserUpdatedEvent`).
   - Consumers listening to the event invalidate or update their local Redis caches.
   - Always set an explicit **TTL (Time to Live)** to prevent stale ghost records.

