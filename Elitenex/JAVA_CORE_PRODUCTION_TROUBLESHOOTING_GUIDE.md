# Java Core, Production Troubleshooting, SQL & Microservices Ecosystem Guide
> **Comprehensive Enterprise Deep-Dive with Java 8/11/17/21 Modern Features, JVM Memory Leak Analysis, Production Troubleshooting, Database Tuning, REST Standards & Advanced Microservices Patterns**

---

# Table of Contents
1. [Module 6: Modern Core Java (8, 11, 17, 21) (Q147 – Q166)](#module-6-modern-core-java-8-11-17-21)
   - [Q147. What are the main features introduced in Java 8?](#q147-what-are-the-main-features-introduced-in-java-8)
   - [Q148. What are functional interfaces in Java?](#q148-what-are-functional-interfaces-in-java)
   - [Q149. What is a lambda expression and how is it used?](#q149-what-is-a-lambda-expression-and-how-is-it-used)
   - [Q150. What is the Stream API in Java 8?](#q150-what-is-the-stream-api-in-java-8)
   - [Q151. What is the difference between intermediate and terminal operations in streams?](#q151-what-is-the-difference-between-intermediate-and-terminal-operations-in-streams)
   - [Q152. How do you use Optional in Java 8?](#q152-how-do-you-use-optional-in-java-8)
   - [Q153. What is the purpose of default and static methods in interfaces?](#q153-what-is-the-purpose-of-default-and-static-methods-in-interfaces)
   - [Q154. What is the difference between map() and flatMap()?](#q154-what-is-the-difference-between-map-and-flatmap)
   - [Q155. What are new features introduced in Java 11 and Java 17 LTS?](#q155-what-are-new-features-introduced-in-java-11-and-java-17-lts)
   - [Q156. How do you use var keyword introduced in JDK 10?](#q156-how-do-you-use-var-keyword-introduced-in-jdk-10)
   - [Q157. What is the difference between record and class in Java 16+?](#q157-what-is-the-difference-between-record-and-class-in-java-16)
   - [Q158. How do you use switch expressions in Java 14+?](#q158-how-do-you-use-switch-expressions-in-java-14)
   - [Q159. What are sealed classes in Java 17?](#q159-what-are-sealed-classes-in-java-17)
   - [Q160. What is the difference between LocalDate, LocalDateTime, and ZonedDateTime?](#q160-what-is-the-difference-between-localdate-localdatetime-and-zoneddatetime)
   - [Q161. How do you use Predicate, Function, and Consumer interfaces?](#q161-how-do-you-use-predicate-function-and-consumer-interfaces)
   - [Q162. What is a method reference in Java?](#q162-what-is-a-method-reference-in-java)
   - [Q163. What are the advantages of Stream API over loops?](#q163-what-are-the-advantages-of-stream-api-over-loops)
   - [Q164. How do you parallelize stream operations?](#q164-how-do-you-parallelize-stream-operations)
   - [Q165. What is the difference between Optional.of() and Optional.ofNullable()?](#q165-what-is-the-difference-between-optionalof-and-optionalofnullable)
   - [Q166. How do you filter and collect stream data into a Map?](#q166-how-do-you-filter-and-collect-stream-data-into-a-map)
2. [Module 7: Production Troubleshooting & Real-World Scenarios (Q167 – Q191)](#module-7-production-troubleshooting--real-world-scenarios)
   - [Q167. Your REST API is returning data very slowly — how would you debug and fix it?](#q167-your-rest-api-is-returning-data-very-slowly--how-would-you-debug-and-fix-it)
   - [Q168. A Hibernate query is triggering 50 SQL queries for one API — what's wrong?](#q168-a-hibernate-query-is-triggering-50-sql-queries-for-one-api--whats-wrong)
   - [Q169. How do you handle circular dependencies between microservices?](#q169-how-do-you-handle-circular-dependencies-between-microservices)
   - [Q170. How would you design an Employee-Department microservice architecture?](#q170-how-would-you-design-an-employee-department-microservice-architecture)
   - [Q171. How do you handle lazy loading exceptions in REST responses?](#q171-how-do-you-handle-lazy-loading-exceptions-in-rest-responses)
   - [Q172. Your DB connection pool is exhausted — what steps will you take?](#q172-your-db-connection-pool-is-exhausted--what-steps-will-you-take)
   - [Q173. How do you monitor and analyze memory leaks in a Spring Boot app?](#q173-how-do-you-monitor-and-analyze-memory-leaks-in-a-spring-boot-app)
   - [Q174. How do you migrate a monolithic application into microservices?](#q174-how-do-you-migrate-a-monolithic-application-into-microservices)
   - [Q175. How do you ensure fault tolerance between microservices?](#q175-how-do-you-ensure-fault-tolerance-between-microservices)
   - [Q176. How do you handle data synchronization across multiple services?](#q176-how-do-you-handle-data-synchronization-across-multiple-services)
   - [Q177. What is your deployment process for microservices (CI/CD)?](#q177-what-is-your-deployment-process-for-microservices-cicd)
   - [Q178. How do you implement role-based access control (RBAC) using Spring Security?](#q178-how-do-you-implement-role-based-access-control-rbac-using-spring-security)
   - [Q179. How do you implement request logging for debugging?](#q179-how-do-you-implement-request-logging-for-debugging)
   - [Q180. How do you cache frequently accessed data using Spring Cache or Redis?](#q180-how-do-you-cache-frequently-accessed-data-using-spring-cache-or-redis)
   - [Q181. How do you secure sensitive configurations in production (e.g., DB credentials)?](#q181-how-do-you-secure-sensitive-configurations-in-production-eg-db-credentials)
   - [Q182. How do you handle version upgrades (JDK 8 -> 17 -> 21) in your projects?](#q182-how-do-you-handle-version-upgrades-jdk-8---17---21-in-your-projects)
   - [Q183. How do you reduce startup time of a Spring Boot microservice?](#q183-how-do-you-reduce-startup-time-of-a-spring-boot-microservice)
   - [Q184. How do you handle database migrations between environments (dev -> prod)?](#q184-how-do-you-handle-database-migrations-between-environments-dev---prod)
   - [Q185. How do you test integration between microservices locally?](#q185-how-do-you-test-integration-between-microservices-locally)
   - [Q186. How do you use Docker for microservice deployment?](#q186-how-do-you-use-docker-for-microservice-deployment)
   - [Q187. How do you handle API backward compatibility?](#q187-how-do-you-handle-api-backward-compatibility)
   - [Q188. How do you detect and fix Hibernate performance bottlenecks?](#q188-how-do-you-detect-and-fix-hibernate-performance-bottlenecks)
   - [Q189. What monitoring tools do you use for production (Prometheus, Grafana, ELK)?](#q189-what-monitoring-tools-do-you-use-for-production-prometheus-grafana-elk)
   - [Q190. How do you implement rate limiting in APIs?](#q190-how-do-you-implement-rate-limiting-in-apis)
   - [Q191. Describe a recent project where you optimized microservice performance.](#q191-describe-a-recent-project-where-you-optimized-microservice-performance)
3. [Module 8: SQL & Database Fundamentals (Q192 – Q206)](#module-8-sql--database-fundamentals)
   - [Q192. What are ACID properties in a relational database?](#q192-what-are-acid-properties-in-a-relational-database)
   - [Q193. What is normalization? Explain 1NF, 2NF, 3NF.](#q193-what-is-normalization-explain-1nf-2nf-3nf)
   - [Q194. What is the difference between INNER JOIN and LEFT JOIN?](#q194-what-is-the-difference-between-inner-join-and-left-join)
   - [Q195. How do you optimize a slow SQL query?](#q195-how-do-you-optimize-a-slow-sql-query)
   - [Q196. What are indexes? When should you use or avoid them?](#q196-what-are-indexes-when-should-you-use-or-avoid-them)
   - [Q197. What is a foreign key constraint and why is it important?](#q197-what-is-a-foreign-key-constraint-and-why-is-it-important)
   - [Q198. How do you manage database migrations in Spring Boot (Flyway/Liquibase)?](#q198-how-do-you-manage-database-migrations-in-spring-boot-flywayliquibase)
   - [Q199. What's the difference between clustered and non-clustered indexes?](#q199-whats-the-difference-between-clustered-and-non-clustered-indexes)
   - [Q200. How do you handle transactions across multiple tables?](#q200-how-do-you-handle-transactions-across-multiple-tables)
   - [Q201. What is the difference between CHAR and VARCHAR data types?](#q201-what-is-the-difference-between-char-and-varchar-data-types)
   - [Q202. How do you prevent SQL injection in Spring Boot applications?](#q202-how-do-you-prevent-sql-injection-in-spring-boot-applications)
   - [Q203. What are stored procedures and how do you call them from JPA?](#q203-what-are-stored-procedures-and-how-do-you-call-them-from-jpa)
   - [Q204. How do you implement soft deletes in a table?](#q204-how-do-you-implement-soft-deletes-in-a-table)
   - [Q205. How do you handle connection pooling in Spring Boot JPA?](#q205-how-do-you-handle-connection-pooling-in-spring-boot-jpa)
   - [Q206. How do you configure multiple data sources in a single Spring Boot project?](#q206-how-do-you-configure-multiple-data-sources-in-a-single-spring-boot-project)
4. [Module 9: REST APIs & Design Best Practices (Q207 – Q229)](#module-9-rest-apis--design-best-practices)
   - [Q207. What is REST architecture and its core principles?](#q207-what-is-rest-architecture-and-its-core-principles)
   - [Q208. What is an idempotent operation in REST?](#q208-what-is-an-idempotent-operation-in-rest)
   - [Q209. How do you handle exception responses in REST APIs?](#q209-how-do-you-handle-exception-responses-in-rest-apis)
   - [Q210. What are the best practices for designing RESTful APIs?](#q210-what-are-the-best-practices-for-designing-restful-apis)
   - [Q211. What are HTTP status codes and common examples?](#q211-what-are-http-status-codes-and-common-examples)
   - [Q212. How do you handle validation in REST APIs?](#q212-how-do-you-handle-validation-in-rest-apis)
   - [Q213. What is the difference between path variable and request parameter?](#q213-what-is-the-difference-between-path-variable-and-request-parameter)
   - [Q214. How do you secure REST APIs?](#q214-how-do-you-secure-rest-apis)
   - [Q215. What is JWT and how is it used for authentication?](#q215-what-is-jwt-and-how-is-it-used-for-authentication)
   - [Q216. How do you handle file uploads in a REST API?](#q216-how-do-you-handle-file-uploads-in-a-rest-api)
   - [Q217. What are HATEOAS principles?](#q217-what-are-hateoas-principles)
   - [Q218. How do you handle pagination and sorting in REST APIs?](#q218-how-do-you-handle-pagination-and-sorting-in-rest-apis)
   - [Q219. How do you test REST APIs using Postman or Swagger?](#q219-how-do-you-test-rest-apis-using-postman-or-swagger)
   - [Q220. What is Swagger/OpenAPI and how do you integrate it with Spring Boot?](#q220-what-is-swaggeropenapi-and-how-do-you-integrate-it-with-spring-boot)
   - [Q221. How do you version REST APIs?](#q221-how-do-you-version-rest-apis)
   - [Q222. How do you handle request and response logging in REST APIs?](#q222-how-do-you-handle-request-and-response-logging-in-rest-apis)
   - [Q223. How do you return consistent API responses?](#q223-how-do-you-return-consistent-api-responses)
   - [Q224. What is the difference between synchronous and asynchronous REST APIs?](#q224-what-is-the-difference-between-synchronous-and-asynchronous-rest-apis)
   - [Q225. How do you handle large data responses in REST APIs?](#q225-how-do-you-handle-large-data-responses-in-rest-apis)
   - [Q226. How do you consume a REST API in Spring Boot using RestTemplate or WebClient?](#q226-how-do-you-consume-a-rest-api-in-spring-boot-using-resttemplate-or-webclient)
   - [Q227. What is the difference between RestTemplate and WebClient?](#q227-what-is-the-difference-between-resttemplate-and-webclient)
   - [Q228. What is content negotiation in REST APIs?](#q228-what-is-content-negotiation-in-rest-apis)
   - [Q229. How do you handle rate limiting and throttling in REST APIs?](#q229-how-do-you-handle-rate-limiting-and-throttling-in-rest-apis)
5. [Module 10: Advanced Microservices Ecosystem & Architecture (Q230 – Q269)](#module-10-advanced-microservices-ecosystem--architecture)
   - [Q230. What is a microservice architecture?](#q230-what-is-a-microservice-architecture)
   - [Q231. What are the advantages and disadvantages of microservices?](#q231-what-are-the-advantages-and-disadvantages-of-microservices)
   - [Q232. How is microservice architecture different from monolithic architecture?](#q232-how-is-microservice-architecture-different-from-monolithic-architecture)
   - [Q233. What are the main components of a microservice ecosystem?](#q233-what-are-the-main-components-of-a-microservice-ecosystem)
   - [Q234. How do microservices communicate with each other?](#q234-how-do-microservices-communicate-with-each-other)
   - [Q235. What is the role of Spring Cloud in microservices?](#q235-what-is-the-role-of-spring-cloud-in-microservices)
   - [Q236. What is service discovery and how is it implemented using Eureka or Consul?](#q236-what-is-service-discovery-and-how-is-it-implemented-using-eureka-or-consul)
   - [Q237. How do you implement API Gateway in microservices?](#q237-how-do-you-implement-api-gateway-in-microservices)
   - [Q238. What is the role of Spring Cloud Gateway or Zuul?](#q238-what-is-the-role-of-spring-cloud-gateway-or-zuul)
   - [Q239. How do you secure communication between microservices?](#q239-how-do-you-secure-communication-between-microservices)
   - [Q240. How does JWT token work across multiple microservices?](#q240-how-does-jwt-token-work-across-multiple-microservices)
   - [Q241. What is configuration management and why is Spring Cloud Config used?](#q241-what-is-configuration-management-and-why-is-spring-cloud-config-used)
   - [Q242. How do you handle distributed tracing in microservices?](#q242-how-do-you-handle-distributed-tracing-in-microservices)
   - [Q243. What tools are used for tracing (Zipkin, Sleuth, Jaeger)?](#q243-what-tools-are-used-for-tracing-zipkin-sleuth-jaeger)
   - [Q244. What is load balancing in microservices and how is it achieved?](#q244-what-is-load-balancing-in-microservices-and-how-is-it-achieved)
   - [Q245. What is the role of Feign Client in microservices?](#q245-what-is-the-role-of-feign-client-in-microservices)
   - [Q246. How do you handle circuit breakers using Resilience4j or Hystrix?](#q246-how-do-you-handle-circuit-breakers-using-resilience4j-or-hystrix)
   - [Q247. What is service registry and discovery pattern?](#q247-what-is-service-registry-and-discovery-pattern)
   - [Q248. How do you handle centralized logging in microservices?](#q248-how-do-you-handle-centralized-logging-in-microservices)
   - [Q249. How do you manage database per service in microservice architecture?](#q249-how-do-you-manage-database-per-service-in-microservice-architecture)
   - [Q250. How do you achieve transaction management across microservices?](#q250-how-do-you-achieve-transaction-management-across-microservices)
   - [Q251. What is eventual consistency and how do you achieve it?](#q251-what-is-eventual-consistency-and-how-do-you-achieve-it)
   - [Q252. How do you version microservices APIs?](#q252-how-do-you-version-microservices-apis)
   - [Q253. How do you test and deploy microservices independently?](#q253-how-do-you-test-and-deploy-microservices-independently)
   - [Q254. What is a sidecar pattern in microservices?](#q254-what-is-a-sidecar-pattern-in-microservices)
   - [Q255. How do you containerize and deploy microservices using Docker?](#q255-how-do-you-containerize-and-deploy-microservices-using-docker)
   - [Q256. How do you orchestrate microservices using Kubernetes?](#q256-how-do-you-orchestrate-microservices-using-kubernetes)
   - [Q257. How do you handle inter-service communication failures?](#q257-how-do-you-handle-inter-service-communication-failures)
   - [Q258. What is a distributed cache and how is it used in microservices?](#q258-what-is-a-distributed-cache-and-how-is-it-used-in-microservices)
   - [Q259. How do you handle fault tolerance and resilience?](#q259-how-do-you-handle-fault-tolerance-and-resilience)
   - [Q260. What is API Gateway throttling?](#q260-what-is-api-gateway-throttling)
   - [Q261. How do you secure microservices using OAuth2?](#q261-how-do-you-secure-microservices-using-oauth2)
   - [Q262. How do you handle synchronous vs asynchronous communication between services?](#q262-how-do-you-handle-synchronous-vs-asynchronous-communication-between-services)
   - [Q263. How do you share common libraries across multiple microservices?](#q263-how-do-you-share-common-libraries-across-multiple-microservices)
   - [Q264. What is a saga pattern in microservices?](#q264-what-is-a-saga-pattern-in-microservices)
   - [Q265. How do you handle schema evolution in databases across microservices?](#q265-how-do-you-handle-schema-evolution-in-databases-across-microservices)
   - [Q266. What monitoring tools do you use for microservices (Prometheus, Grafana, ELK)?](#q266-what-monitoring-tools-do-you-use-for-microservices-prometheus-grafana-elk)
   - [Q267. How do you handle deployment rollbacks in microservice architecture?](#q267-how-do-you-handle-deployment-rollbacks-in-microservice-architecture)
   - [Q268. How do you use Circuit Breaker with Feign Client?](#q268-how-do-you-use-circuit-breaker-with-feign-client)
   - [Q269. What are some real challenges you faced working with microservices in production?](#q269-what-are-some-real-challenges-you-faced-working-with-microservices-in-production)

---

# Module 6: Modern Core Java (8, 11, 17, 21)

---

### Q147. What are the main features introduced in Java 8?

#### 1. Concept & Interview Answer
Java 8 is the most revolutionary release in Java's history, introducing functional programming paradigms to the language:
1. **Lambda Expressions:** Anonymous functions enabling concise, readable code: `(a, b) -> a + b`.
2. **Functional Interfaces:** Interfaces with exactly one abstract method (e.g., `Predicate<T>`, `Function<T, R>`, `Consumer<T>`, `Supplier<T>`).
3. **Stream API (`java.util.stream`):** Declarative, functional data processing pipelines for filtering, mapping, and reducing collections in memory.
4. **`Optional<T>`:** A container object used to prevent `NullPointerException` and force explicit null checks.
5. **Default & Static Methods in Interfaces:** Allows adding new methods to interfaces without breaking existing implementing classes.
6. **New Date-Time API (`java.time`):** Immutable, thread-safe date/time classes replacing buggy legacy `java.util.Date` and `Calendar` (`LocalDate`, `LocalDateTime`, `ZonedDateTime`, `Instant`).
7. **Method References (`Class::method`):** Compact syntactic sugar for calling existing methods as lambdas.
8. **CompletableFuture:** Asynchronous, non-blocking promise-based concurrency pipelines.

#### 2. Evolution of Java Paradigms
```
Java 7 (Imperative / Boilerplate):
List<String> names = new ArrayList<>();
for (User u : users) {
    if (u.isActive()) {
        names.add(u.getName().toUpperCase());
    }
}

Java 8 (Declarative / Functional Streams):
List<String> names = users.stream()
    .filter(User::isActive)
    .map(u -> u.getName().toUpperCase())
    .toList();
```

---

### Q150. What is the Stream API in Java 8?

#### 1. Concept & Interview Answer
- The **Stream API** is a sequence of elements supporting sequential and parallel aggregate operations.
- Streams do **not** store elements in memory (they are not data structures); they compute elements on-demand through a pipeline.
- **Three Pipeline Stages:**
  1. **Source:** `list.stream()`, `Arrays.stream(array)`, `Stream.of(...)`.
  2. **Intermediate Operations (Lazy):** `filter()`, `map()`, `flatMap()`, `sorted()`, `distinct()`, `limit()`.
  3. **Terminal Operation (Eager):** `collect()`, `forEach()`, `reduce()`, `count()`, `findFirst()`, `anyMatch()`.

#### 2. Stream Execution Mechanics (Lazy Evaluation & Fusion)
```
[ Collection ] ──► stream()
                        │
                        ├──► filter(x -> x > 10) ──┐ (Operations are fused into a single pass!
                        ├──► map(x -> x * 2)     ──┤  No intermediate collections are created!)
                        │
                        ▼
                [ collect(Collectors.toList()) ] ──► (Triggers terminal execution)
```

---

### Q154. What is the difference between map() and flatMap()?

#### 1. Concept & Interview Answer
- **`map(Function<T, R>)` (1-to-1 Transformation):**
  - Transforms each element of a stream into another single element.
  - Takes a stream `Stream<T>` and returns `Stream<R>`.
- **`flatMap(Function<T, Stream<R>>)` (1-to-Many Transformation + Flattening):**
  - Transforms each element into a **stream of elements** and then **flattens** all the individual sub-streams into a single composite stream.
  - Takes `Stream<List<T>>` or `Stream<Stream<T>>` and flattens it to `Stream<T>`.

#### 2. Visual Representation
```
map() Transformation:
["hello", "world"] ──► map(String::toUpperCase) ──► ["HELLO", "WORLD"]

flatMap() Flattening:
[["apple", "banana"], ["orange", "mango"]] ──► flatMap(List::stream) ──► ["apple", "banana", "orange", "mango"]
```

#### 3. Production Code Example
```java
// Example: Extracting all unique phone numbers from a list of customers where each customer has List<String> phoneNumbers
List<Customer> customers = getCustomers();

List<String> allPhoneNumbers = customers.stream()
    .flatMap(c -> c.getPhoneNumbers().stream()) // Flattens List<String> from each customer
    .distinct()
    .sorted()
    .toList();
```

---

### Q155. What are new features introduced in Java 11 and Java 17 LTS?

#### 1. Concept & Interview Answer
- **Java 11 LTS Major Features:**
  - **Standardized HTTP Client (`java.net.http.HttpClient`):** Modern HTTP/2 and WebSocket asynchronous client.
  - **Local-Variable Syntax for Lambda Parameters:** `(var x, var y) -> x + y`.
  - **String Helper Methods:** `isBlank()`, `lines()`, `strip()`, `repeat(n)`.
  - **Files Methods:** `Files.readString(path)`, `Files.writeString(path, str)`.
  - **Run Java source files directly:** `java App.java` without manual `javac`.
- **Java 17 LTS Major Features:**
  - **Records (`record Name(...) {}`):** Immutable, compact data carrier classes.
  - **Sealed Classes (`sealed class ... permits ...`):** Restricts class inheritance hierarchies.
  - **Pattern Matching for `instanceof`:** `if (obj instanceof String s) { s.length(); }`.
  - **Text Blocks (`""" ... """`):** Multi-line formatted strings for SQL and JSON.
  - **Switch Expressions:** Clean arrow syntax with `yield` values.

---

### Q157. What is the difference between record and class in Java 16+?

#### 1. Concept & Interview Answer
- A **`record`** is a special type of class in Java designed purely to act as an **immutable data carrier**.
- The compiler automatically generates:
  - Private `final` fields for each component.
  - Canonical constructor initializing all fields.
  - Public accessor methods (`name()`, `price()`, without `get` prefix).
  - `equals()` and `hashCode()` based on all component states.
  - `toString()` displaying all field values.
- Records are implicitly `final` (cannot be extended) and cannot extend other classes (they implicitly extend `java.lang.Record`), but can implement interfaces.

#### 2. Code Comparison: Class vs Record
```java
// Traditional Class: 50 Lines of Boilerplate
public final class UserDto {
    private final Long id;
    private final String email;
    public UserDto(Long id, String email) { this.id = id; this.email = email; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    @Override public boolean equals(Object o) { ... }
    @Override public int hashCode() { ... }
    @Override public String toString() { ... }
}

// Java 16+ Record: 1 Line!
public record UserDto(Long id, String email) {
    // Compact Constructor for validation
    public UserDto {
        Objects.requireNonNull(email, "Email must not be null");
    }
}
```

---

### Q159. What are sealed classes in Java 17?

#### 1. Concept & Interview Answer
- **Sealed Classes** (`sealed class / interface`) allow a class or interface to **explicitly declare and restrict which subclasses are permitted to extend or implement it**.
- Specified using the **`sealed`** modifier and the **`permits`** clause.
- Permitted subclasses must be declared as:
  - `final`: Cannot be extended any further.
  - `sealed`: Extends the sealed hierarchy with its own permitted subclasses.
  - `non-sealed`: Opens up inheritance to any class.
- **Why use Sealed Classes:** Enables **Algebraic Data Types (ADTs)** and exhaustive pattern matching in `switch` without needing a `default` branch.

#### 2. Production Code Example
```java
// Domain Payment Method Hierarchy
public sealed interface PaymentMethod permits CreditCardPayment, UpiPayment, CryptoPayment {}

public final class CreditCardPayment implements PaymentMethod {
    private final String cardNumber;
    public CreditCardPayment(String cardNumber) { this.cardNumber = cardNumber; }
}

public final class UpiPayment implements PaymentMethod {
    private final String vpa;
    public UpiPayment(String vpa) { this.vpa = vpa; }
}

public final class CryptoPayment implements PaymentMethod {
    private final String walletAddress;
    public CryptoPayment(String walletAddress) { this.walletAddress = walletAddress; }
}

// Exhaustive Pattern Matching in Switch (Java 17/21): No "default:" branch required!
public String processPayment(PaymentMethod payment) {
    return switch (payment) {
        case CreditCardPayment cc -> "Charging card: " + cc.getCardNumber();
        case UpiPayment upi       -> "Routing via UPI VPA: " + upi.getVpa();
        case CryptoPayment crypto -> "Transferring to wallet: " + crypto.getWalletAddress();
    };
}
```

---

# Module 7: Production Troubleshooting & Real-World Scenarios

---

### Q167. Your REST API is returning data very slowly — how would you debug and fix it?

#### 1. Step-by-Step Production Troubleshooting Playbook
1. **Trace Request Latency with APM (Datadog / Dynatrace / Zipkin):**
   - Identify whether the bottleneck is in the **Network**, **Application CPU/Memory**, or **Database Query**.
2. **Inspect Database Execution (Slow Query Logs & EXPLAIN ANALYZE):**
   - Check if queries are doing Sequential Table Scans (`Seq Scan`) instead of Index Scans (`Index Scan`).
   - Look for Hibernate N+1 select queries.
3. **Analyze JVM Threads & CPU (Thread Dumps):**
   - Use `jstack <pid>` or Actuator `/actuator/threaddump` to check for thread contention, deadlocks, or blocked I/O threads.
4. **Check Connection Pool Metrics (HikariCP):**
   - Check if threads are waiting on `HikariCP - Connection is not available, request timed out after 30000ms`.
5. **Resolution Strategies:**
   - Add missing B-Tree composite indexes on filtered/sorted DB columns.
   - Introduce Redis caching (`@Cacheable`) for read-heavy static data.
   - Replace heavy entity queries with DTO projections.
   - Increase HikariCP pool size or optimize long-running transactions.

---

### Q172. Your DB connection pool is exhausted — what steps will you take?

#### 1. Root Causes & Immediate Fixes
- **Cause 1: Long-Running External HTTP Calls inside `@Transactional`:**
  - *Fix:* Never make external network/REST calls while holding an open database transaction. Extract HTTP calls outside `@Transactional`.
- **Cause 2: Connection Leaks (Unclosed Connections or Open-In-View):**
  - *Fix:* Set `spring.jpa.open-in-view=false`. Enable Hikari leak detection: `spring.datasource.hikari.leak-detection-threshold=2000` (logs a stack trace if a connection is held longer than 2 seconds).
- **Cause 3: Slow Un-Indexed SQL Queries Holding Connections:**
  - *Fix:* Add missing indexes and set SQL query timeouts (`@Transactional(timeout = 5)`).
- **Cause 4: Undersized Pool for High Concurrency:**
  - *Fix:* Tune Hikari pool size using PostgreSQL formula: `maximum-pool-size = (CPU_CORES * 2) + DISK_COUNT`.

---

### Q173. How do you monitor and analyze memory leaks in a Spring Boot app?

#### 1. Troubleshooting Playbook
1. **Capture Heap Dump on OOM:** Configure JVM flags: `-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/dumps/oom.hprof`.
2. **Analyze Heap Dump with Eclipse MAT (Memory Analyzer Tool) or JProfiler:**
   - Look at the **Dominator Tree** and **Leak Suspects Report** to identify which object class occupies 80%+ of heap memory.
3. **Common Spring Boot Leak Culprits:**
   - `ThreadLocal` variables not cleared after request completion (`ThreadLocal.remove()`).
   - Static collections (`static Map<String, Object> cache = new HashMap<>()`) growing unboundedly without eviction.
   - Unclosed `InputStream` / `ResultSet` streams.
   - Hibernate First-Level Cache holding 50,000 entities in batch operations without `entityManager.clear()`.

---

# Module 8: SQL & Database Fundamentals

---

### Q192. What are ACID properties in a relational database?

#### 1. Concept & Interview Answer
- **Atomicity ("All or Nothing"):** All operations in a transaction complete successfully, or the entire transaction is rolled back.
- **Consistency ("Valid State to Valid State"):** Database constraints, foreign keys, and unique rules are never violated.
- **Isolation ("Independent Execution"):** Concurrent transactions execute without interfering with one another. (Controlled by Isolation Levels: `READ_COMMITTED`, `REPEATABLE_READ`, `SERIALIZABLE`).
- **Durability ("Permanent Survival"):** Once a transaction commits, its changes survive system crashes or power failures (persisted to Write-Ahead Logs / Disk).

---

### Q195. How do you optimize a slow SQL query?

#### 1. Optimization Checklist
1. **Run `EXPLAIN (ANALYZE, BUFFERS)`:** Look for `Seq Scan` on large tables and high `Cost` numbers.
2. **Add Targeted Indexes:** Create B-Tree indexes on columns used in `WHERE`, `JOIN ON`, and `ORDER BY`.
3. **Avoid Functions on Indexed Columns:** `WHERE LOWER(email) = '...'` invalidates standard indexes; use a Functional Index: `CREATE INDEX idx_email_lower ON users(LOWER(email))`.
4. **Avoid `SELECT *`:** Select only the required columns to enable **Covering Indexes** and minimize disk I/O.
5. **Optimize Pagination:** Replace deep `OFFSET 1000000 LIMIT 20` with **Seek Method / Keyset Pagination** (`WHERE id > :lastSeenId ORDER BY id LIMIT 20`).

---

### Q199. What's the difference between clustered and non-clustered indexes?

#### 1. Concept & Interview Answer
| Feature | Clustered Index | Non-Clustered (Secondary) Index |
| :--- | :--- | :--- |
| **Physical Data Order** | Dictates the physical storage order of rows on disk | Separate lookup structure pointing back to table rows |
| **Count per Table** | **Exactly 1** per table (Primary Key) | **Multiple** allowed per table (e.g., on `email`, `created_at`) |
| **Leaf Node Content** | Contains the actual data row columns | Contains indexed column value + pointer (Row ID or PK) |
| **Lookup Speed** | Fastest (Direct page retrieval) | Requires 2 lookups (Index scan -> Table row lookup) |

---

# Module 9: RESTful API Design & Best Practices

---

### Q207. What is REST architecture and its core principles?

#### 1. Concept & Interview Answer
**REST (Representational State Transfer)** is an architectural style defined by Roy Fielding based on 6 core constraints:
1. **Uniform Interface:** Standardized URIs (`/orders/123`), HTTP verbs (`GET`, `POST`, `PUT`, `DELETE`), and standard media types (`application/json`).
2. **Statelessness:** Every request contains all information needed to process it; server retains no client session context.
3. **Client-Server Separation:** UI concerns are decoupled from data storage concerns.
4. **Cacheability:** Responses explicitly declare whether they are cacheable (`Cache-Control: max-age=3600`).
5. **Layered System:** Client connects through intermediaries (Gateways, Load Balancers, CDNs) without needing to know backend topology.
6. **Code on Demand (Optional):** Servers can transfer executable code (e.g., JavaScript).

---

### Q211. What are HTTP status codes and common examples?

#### 1. Core Status Codes Reference Table
| Code & Name | Meaning | Standard Usage |
| :--- | :--- | :--- |
| **`200 OK`** | Request succeeded | `GET`, `PUT`, `PATCH` success returning a body |
| **`201 Created`** | Resource successfully created | `POST` requests (includes `Location` header) |
| **`204 No Content`** | Request succeeded, no body returned | `DELETE` operations or state toggles |
| **`400 Bad Request`** | Malformed syntax or `@Valid` failure | Request validation errors |
| **`401 Unauthorized`** | Authentication missing or invalid | Missing or expired JWT token |
| **`403 Forbidden`** | Authenticated, but lacks permission | Role permission check failed (`@PreAuthorize`) |
| **`404 Not Found`** | Resource does not exist | Invalid URL or entity ID not found |
| **`409 Conflict`** | State conflict (e.g., Unique Key violation) | Duplicate registration, Optimistic Lock failure |
| **`429 Too Many Requests`**| Rate limit exceeded | Token bucket rate limiting active |
| **`500 Internal Error`**| Uncaught server crash | Bug, unhandled `NullPointerException` |
| **`503 Service Unavailable`**| Server overloaded or down | Circuit Breaker open, downstream outage |

---

# Module 10: Advanced Microservices Ecosystem & Architecture

---

### Q242. How do you handle distributed tracing in microservices?

#### 1. Concept & Interview Answer
- In a microservice ecosystem, a single user click triggers a cascading chain of HTTP and messaging calls across 10+ services.
- **Distributed Tracing** tracks the entire request lifecycle using:
  1. **Trace ID:** A globally unique ID generated at the API Gateway and passed across all services representing the entire transaction chain.
  2. **Span ID:** Represents a single unit of work within a specific microservice.
  3. **Parent Span ID:** Links sub-tasks to parent tasks.
- **Tools:** **Micrometer Tracing** (Spring Boot 3) integrating with **OpenTelemetry**, **Zipkin**, and **Jaeger**.

#### 2. Trace Context Propagation Pipeline
```
[ Client Request ] ──► [ API Gateway ] (Generates TraceId: 4bf92f3577b34da6)
                            │
                            ├── Header: traceparent: 00-4bf92f3577b34da6-00f067aa0ba902b7-01
                            ▼
                   [ Order Service ] (Span 1: 120ms)
                            │
                            ├── OpenFeign Call (Propagates traceparent)
                            ▼
                  [ Payment Service ] (Span 2: 85ms)
                            │
                            ▼
                  [ Zipkin / Jaeger UI ] ──► Visualizes complete end-to-end waterfall timeline!
```

---

### Q254. What is a sidecar pattern in microservices?

#### 1. Concept & Interview Answer
- The **Sidecar Pattern** deploys a helper container alongside the main application container within the same Kubernetes **Pod**.
- Both containers share the same network namespace (`localhost`) and storage volumes.
- **Use Cases:**
  1. **Service Mesh (Istio Envoy Proxy):** Handles mTLS encryption, traffic routing, circuit breaking, and telemetry transparently without touching Java application code.
  2. **Log Forwarders (Fluentd / Promtail):** Tails application log files from a shared volume and ships them to Elasticsearch/Loki.
  3. **Secret Vault Sidecars:** Injects dynamic database credentials into local configuration files.

#### 2. Kubernetes Pod Sidecar Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                      Kubernetes Pod                         │
│  ┌───────────────────────────┐ ┌──────────────────────────┐ │
│  │ Primary Container         │ │ Sidecar Container        │ │
│  │ (Spring Boot Java App)    │ │ (Envoy Proxy / Istio)    │ │
│  │                           │ │                          │ │
│  │ Port: 8080                │ │ Port: 15001              │ │
│  └─────────────┬─────────────┘ └────────────┬─────────────┘ │
│                │                            │               │
│                └──────── Shared Localhost ──┘               │
└─────────────────────────────────────────────────────────────┘
```

---

### Q264. What is a saga pattern in microservices?

#### 1. Concept & Interview Answer
- The **Saga Pattern** manages distributed transactions across multiple microservices without locking resources via 2-Phase Commit.
- It consists of a series of local transactions where each microservice updates its own database and publishes an event.
- **Two Implementation Styles:**
  1. **Choreography (Event-Driven):** Services listen to Kafka topics and decide what to do autonomously without a central coordinator. (Best for simple workflows with 2–4 services).
  2. **Orchestration (Command-Driven):** A dedicated **Saga Orchestrator** service sends commands to participant services and monitors their execution. (Best for complex enterprise workflows with 5+ steps).
- **Compensating Transactions:** If step 4 fails, the orchestrator issues compensating commands in reverse order (Step 3 rollback -> Step 2 rollback -> Step 1 rollback) to achieve eventual consistency.

---

### Q269. What are some real challenges you faced working with microservices in production?

#### 1. Senior Engineer War Stories & Production Solutions
1. **Challenge 1: Distributed Data Consistency & Dual-Write Inconsistencies:**
   - *Problem:* Updating a database and publishing to Kafka resulted in lost events when network dropped mid-flight.
   - *Solution:* Implemented the **Transactional Outbox Pattern** with Debezium CDC for guaranteed at-least-once event delivery.
2. **Challenge 2: Cascading Timeouts under High Concurrency:**
   - *Problem:* A slow downstream Payment gateway blocked all Tomcat threads in upstream Order Service, exhausting thread pools.
   - *Solution:* Implemented **Resilience4j Circuit Breakers** and converted synchronous REST calls to asynchronous Kafka messaging.
3. **Challenge 3: Distributed Tracing & Debugging in Production:**
   - *Problem:* Finding the root cause of a 500 error spanning 8 microservices took hours of grep searching across separate log files.
   - *Solution:* Standardized on **Micrometer Tracing + OpenTelemetry**, injecting unified `traceId`s into SLF4J MDC and visualizing distributed traces in Grafana Tempo.
4. **Challenge 4: Zero-Downtime Database Schema Migrations:**
   - *Problem:* Renaming a column in production caused downtime while services were updating.
   - *Solution:* Adopted the **Expand-and-Contract (Parallel Run) Pattern** using Flyway scripts.

---
