# Chapter 11: Microservices, Distributed Systems & System Design

This guide covers real-world enterprise architecture questions asked across **Zycus, Paramatrix, In Solutions Global, Transbnk, Mirae Asset, Axis Bank, Inferyx, NuSummit, and Thermolab**.

---

## 📑 Table of Contents
1. [Monolithic vs Microservices Architecture: Tradeoffs & 12-Factor App](#1-monolithic-vs-microservices-architecture-tradeoffs--12-factor-app)
2. [Inter-Service Communication: Feign Client vs RestTemplate vs Message Queues](#2-inter-service-communication-feign-client-vs-resttemplate-vs-message-queues)
3. [Service Discovery & Registry: Eureka Internals & Cloud Native Alternatives](#3-service-discovery--registry-eureka-internals--cloud-native-alternatives)
4. [API Gateway Architecture & Rate Limiting (Token Bucket / Redis)](#4-api-gateway-architecture--rate-limiting-token-bucket--redis)
5. [Cascading Failures & Resilience: Circuit Breaker, Retry & Fallbacks (Resilience4j)](#5-cascading-failures--resilience-circuit-breaker-retry--fallbacks-resilience4j)
6. [Distributed Transactions: 2PC vs Saga Pattern (Orchestration vs Choreography)](#6-distributed-transactions-2pc-vs-saga-pattern-orchestration-vs-choreography)
7. [Handling 1,000+ Req/Sec, API Latency Spikes (2ms to 2s) & High CPU Alerts](#7-handling-1000-reqsec-api-latency-spikes-2ms-to-2s--high-cpu-alerts)

---

## 1. Monolithic vs Microservices Architecture: Tradeoffs & 12-Factor App

### 1. Previous Knowledge Required
- Single deployable WAR/JAR vs independently deployable distributed containers.
- Shared single database vs Database-per-service pattern.

### 2. Logic & Theory (Deep-Dive)

| Criterion | Monolithic Architecture | Microservices Architecture |
| :--- | :--- | :--- |
| **Deployment** | Single artifact (JAR/WAR); one deploy updates everything. | Independently deployable services via Docker & Kubernetes. |
| **Data Storage** | Single shared database (ACID joins across tables). | **Database-per-Service**; cross-service queries require APIs or events. |
| **Scaling** | Vertical scaling or replicating the entire monolith. | Horizontal scaling of only resource-intensive services. |
| **Fault Isolation** | A memory leak or deadlock in one module crashes the entire JVM. | Failure in one service is isolated using Circuit Breakers. |
| **Complexity** | Simple operational overhead; easy debugging and local dev. | High operational complexity: distributed tracing, network latency, eventual consistency. |

#### Key Principles of the 12-Factor App:
1. **Codebase**: One codebase tracked in revision control, many deploys.
2. **Dependencies**: Explicitly declare and isolate dependencies (Maven/Gradle).
3. **Config**: Store configuration in the environment (Spring Cloud Config, Env variables), not hardcoded in code.
4. **Backing Services**: Treat databases, caches, queues as attached resources over network URLs.
5. **Build, Release, Run**: Strict separation between build stages.
6. **Processes**: Execute the app as one or more stateless processes (state offloaded to Redis/DB).
7. **Port Binding**: Export services via port binding (embedded Tomcat/Netty).
8. **Concurrency**: Scale out via the process model (horizontal scaling).
9. **Disposability**: Maximize robustness with fast startup and graceful shutdown.
10. **Dev/Prod Parity**: Keep development, staging, and production as similar as possible.
11. **Logs**: Treat logs as event streams (stdout into ELK / Grafana Loki).
12. **Admin Processes**: Run admin/management tasks as one-off processes.

### 3. How to Answer in an Interview
> *"A monolith bundles all modules and database tables into a single deployable artifact. While simple to develop and deploy initially, it becomes a bottleneck as teams grow—a failure in one module can crash the whole process, and scaling requires replicating the entire application.*
>
> *Microservices decouple business capabilities into bounded contexts with dedicated databases, allowing independent deployments, granular scaling, and tech-stack flexibility. However, microservices introduce distributed system challenges: network latency, distributed transactions, and data consistency, which require patterns like API Gateways, Saga, and Circuit Breakers."*

---

## 2. Inter-Service Communication: Feign Client vs RestTemplate vs Message Queues

### 1. Previous Knowledge Required
- HTTP 1.1 / HTTP/2 synchronous requests vs Asynchronous message brokering.
- Client-side load balancing (Spring Cloud LoadBalancer).

### 2. Logic & Theory (Deep-Dive)

#### Synchronous vs Asynchronous Communication:
1. **Synchronous (HTTP REST / gRPC)**: Caller waits for the response. Best for query operations where the UI needs data immediately (e.g., `fetchUserProfile`).
   - `RestTemplate`: Imperative client requiring boilerplate URL formatting and response mapping.
   - `OpenFeign`: Declarative REST client using interfaces and annotations. Spring dynamically generates the implementation at runtime, integrating out-of-the-box with Eureka and Spring Cloud LoadBalancer.
2. **Asynchronous (Event-Driven: Kafka / RabbitMQ)**: Fire-and-forget. The producer emits an event (`LoanApplicationSubmittedEvent`) and continues immediately. Highly decoupled, resilient to downstream outages.

### 3. Practical Code Example: OpenFeign Declarative Client
```java
// 1. Enable Feign in Application class: @EnableFeignClients

// 2. Declarative Feign Client Interface
@FeignClient(name = "credit-score-service", url = "${credit.service.url:http://credit-score-service}")
public interface CreditScoreClient {

    @GetMapping("/api/v1/scores/{panNumber}")
    CreditScoreResponse getScoreByPan(@PathVariable("panNumber") String panNumber);
}

// 3. Service Usage: Clean, no RestTemplate boilerplate!
@Service
public class LoanEvaluationService {

    private final CreditScoreClient creditScoreClient;

    public LoanEvaluationService(CreditScoreClient creditScoreClient) {
        this.creditScoreClient = creditScoreClient;
    }

    public boolean isEligible(String panNumber) {
        CreditScoreResponse response = creditScoreClient.getScoreByPan(panNumber);
        return response.score() >= 750;
    }
}
```

---

## 3. Service Discovery & Registry: Eureka Internals & Cloud Native Alternatives

### 1. Previous Knowledge Required
- Dynamic IP addresses and ephemeral container ports in cloud environments.
- Heartbeats and client-side load balancing.

### 2. Logic & Theory (Deep-Dive)

```
        +-----------------------------------------+
        |        Eureka Discovery Server          |
        |  Registry:                              |
        |  "loan-service"    -> 10.0.1.5:8080     |
        |  "payment-service" -> 10.0.1.9:8081     |
        +-----------------------------------------+
             ^                                 ^
             | 1. Heartbeat (Every 30s)        | 2. Fetch Registry
             |                                 |
    [Loan Microservice] --------------> [Payment Microservice]
                                3. Direct HTTP call (Client-side Load Balanced)
```

#### How Eureka Works:
1. **Registration**: At startup, a microservice sends a REST call to Eureka registering its service ID, IP, and port.
2. **Heartbeats**: Every 30 seconds, instances send a heartbeat (`PUT /eureka/apps/{appID}/{instanceID}`). If no heartbeat is received for 90 seconds, Eureka de-registers the instance.
3. **Client-Side Cache**: Microservices fetch and cache the registry locally, so if Eureka temporarily goes down, services continue communicating without disruption!

#### Modern Alternatives to Eureka:
- **Kubernetes ClusterIP & CoreDNS**: In modern Kubernetes deployments, Eureka is redundant! Kubernetes natively provides internal DNS (`http://payment-service.default.svc.cluster.local:8080`) and performs L4 load balancing across pods automatically.

---

## 4. API Gateway Architecture & Rate Limiting (Token Bucket / Redis)

### 1. Previous Knowledge Required
- Single point of entry (Reverse Proxy).
- Rate Limiting algorithms (Token Bucket, Leaky Bucket, Sliding Window Counter).

### 2. Logic & Theory (Deep-Dive)

#### The Role of an API Gateway (Spring Cloud Gateway):
- **Routing**: Maps external paths (`/loans/**`) to internal microservices.
- **Cross-Cutting Security**: Validates JWT tokens once at the perimeter so downstream microservices don't have to re-authenticate on every internal call.
- **Rate Limiting**: Protects backend services against DDoS attacks or runaway client loops.

#### Token Bucket Rate Limiting with Redis:
```
Refill Rate: 10 tokens/sec
Bucket Capacity: 20 tokens
               |
         +-----+-----+
         | O O O O O | (Tokens in Bucket)
         +-----+-----+
               |
  Incoming Request ---> Consumes 1 Token ---> Forwarded to Microservice
  Incoming Request ---> Bucket Empty (0)  ---> Return HTTP 429 Too Many Requests
```

### 3. Practical Code Example: Spring Cloud Gateway Route + Redis Rate Limiter
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: loan_origination_route
          uri: lb://LOAN-SERVICE
          predicates:
            - Path=/api/v1/loans/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10 # 10 requests per second
                redis-rate-limiter.burstCapacity: 20  # Max burst of 20
                key-resolver: "#{@userKeyResolver}"
```

```java
@Configuration
public class RateLimiterConfig {

    // Rate limit per client IP or authenticated User ID
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(
            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"))
                    .orElse(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress())
        );
    }
}
```

---

## 5. Cascading Failures & Resilience: Circuit Breaker, Retry & Fallbacks (Resilience4j)

### 1. Previous Knowledge Required
- Thread starvation: If Service C hangs, threads in Service B and Service A wait indefinitely until the server's thread pool is completely exhausted, crashing the entire system.

### 2. Logic & Theory (Deep-Dive)

#### The Circuit Breaker State Machine:
```
         +---------------------------------------+
         |                                       |
         v                                       |
    [ CLOSED ] ----(Failure rate > 50%)----> [ OPEN ]
     (Calls pass)                           (Fail fast immediately,
         ^                                    return Fallback)
         |                                       |
         |                                (Wait 10s cooldown)
         |                                       |
         +------(Success rate > 80%)----- [ HALF-OPEN ]
                                         (Test with few calls)
```

#### The Question from Infibeam:
*"Suppose Service A calls B, B calls C, and C calls D. C is down. What will happen, and how do you ensure the system runs smoothly?"*
1. **Without Protection**: Threads in A and B block on network socket timeouts. Tomcat thread pool (default 200 threads) saturates in seconds. All upstream APIs crash with HTTP 504/500 errors.
2. **With Resilience4j**:
   - We set a **Socket Timeout** (e.g., 2 seconds).
   - If C fails repeatedly, the Circuit Breaker opens.
   - Calls to C **fail fast immediately** (sub-millisecond) without blocking worker threads.
   - The caller triggers a **Fallback Method** (returns cached results or a queued status).

### 3. Practical Code Example
```java
@Service
public class PaymentGatewayService {

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    @Retry(name = "paymentService")
    public PaymentResponse makePayment(PaymentRequest request) {
        // External network call to 3rd party bank
        return externalBankApi.charge(request);
    }

    // Fallback executed when circuit is OPEN or call fails
    public PaymentResponse paymentFallback(PaymentRequest request, Throwable ex) {
        log.warn("Payment service degraded. Fallback triggered: {}", ex.getMessage());
        return new PaymentResponse(request.getTxId(), "PENDING", "Payment queued for processing.");
    }
}
```

---

## 6. Distributed Transactions: 2PC vs Saga Pattern (Orchestration vs Choreography)

### 1. Previous Knowledge Required
- ACID transactions fail across multiple independent databases.
- CAP Theorem (Consistency, Availability, Partition Tolerance).

### 2. Logic & Theory (Deep-Dive)

#### Why 2-Phase Commit (2PC) Fails in Cloud Microservices:
2PC is a blocking protocol requiring an XA coordinator to lock records across all databases until all nodes vote to commit. High network latency, cloud unreliability, and database locks make 2PC unacceptable for modern scalable systems.

#### The Saga Pattern (Compensating Transactions):
A sequence of local transactions where each microservice updates its own database and publishes an event. If a subsequent step fails, the Saga executes **Compensating Transactions** in reverse order to undo the changes.

```
       Happy Path:
       [Order Service]   ---> Create Order (Local Tx)
              |
              v
       [Payment Service] ---> Charge Card (Local Tx)
              |
              v
       [Inventory Service]-> Reserve Items (Local Tx)

       Failure Scenario (Inventory Out of Stock):
       [Inventory Service]-> Emits InventoryOutOfStockEvent
              |
              v
       [Payment Service] ---> Refund Card (Compensating Tx)
              |
              v
       [Order Service]   ---> Mark Order as CANCELLED (Compensating Tx)
```

#### Orchestration vs Choreography:
- **Choreography**: Each service listens to events and decides when to act. Simple for 2-3 services, but difficult to monitor for complex workflows.
- **Orchestration**: A centralized **Saga Orchestrator** (e.g., Temporal, Camunda, or a custom Spring state machine) explicitly tells each service what step to execute next. Highly recommended for complex enterprise workflows like Loan Origination.

---

## 7. Handling 1,000+ Req/Sec, API Latency Spikes (2ms to 2s) & High CPU Alerts

### 1. Previous Knowledge Required
- OS Threads vs JVM Heap vs Database Connection Pools (HikariCP).
- CPU profiling tools (`jcmd`, `jstack`, `async-profiler`).

### 2. Logic & Theory (Deep-Dive)

#### Interview Question: *"An API normally takes 2ms, but in production it spikes to 2 seconds. What could be the cause, and how do you diagnose it?"*
1. **Database Connection Pool Exhaustion**: HikariCP default pool is 10 connections. If 200 concurrent requests arrive, threads wait in a queue for a free connection (`connection-timeout` reached).
2. **Missing Database Indexes**: With 1,000 records, a full table scan takes 2ms; with 5,000,000 records, the same unindexed query takes 2 seconds.
3. **Frequent Full GC Pauses**: Heap memory pressure triggering Stop-The-World (STW) Garbage Collection pauses.
4. **Diagnostic Workflow**:
   - Check APM metrics (Datadog / Dynatrace / Prometheus).
   - Inspect slow SQL query logs (`EXPLAIN ANALYZE`).
   - Run thread dump: `jcmd <PID> Thread.print` to check if threads are blocked on DB connections or locks.

#### Interview Question: *"How do you diagnose and handle a High CPU alert (100% CPU) in production?"*
```bash
# Step 1: Find the exact OS Thread ID consuming high CPU
top -H -p <PID>

# Step 2: Convert the top Thread ID (e.g. 14210) to Hexadecimal
printf "%x\n" 14210  # Output: 3782

# Step 3: Take a JVM Thread Dump and grep for the nid (Native Thread ID)
jstack <PID> | grep -A 30 "nid=0x3782"
```
- **Most Common Causes**:
  - Infinite loops or busy-spin conditions in application code.
  - Excessive JVM GC activity (`GC Overhead Limit Exceeded`) when heap is 99% full and the garbage collector runs continuously.
  - JSON serialization of massive unpaginated datasets (e.g., returning 500,000 entities in one HTTP response).

---

*Continue to [Chapter 12: Database Performance, SQL Queries, Hibernate & JPA Internals](file:///Users/apple/Workspace/Home/Javascript/Learning/12_Database_SQL_Hibernate_JPA_Performance.md)*
