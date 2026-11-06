# 🏢 Company-Wise Technical Interview & Architecture Master Guide
> **Comprehensive Enterprise Full-Stack Reference: Java 17/21, Spring Boot 3.x, Microservices, Angular 17+, Distributed Systems & Real-World Project Scenarios (LOS & AIGForgeHub)**
> **Compiled from Real Production Interviews across 30+ Enterprise Companies (Pages 1 to 57)**

---

## 📑 Master Table of Contents
1. [Section 1: Pages 1 – 10 (Paramatrix, Zycus, C-EDGE, NuSummit, Inferyx, Applied Cloud, ParaMatrix LOS, Teams Computers, Bizinso)](#section-1-pages-1--10)
2. [Section 2: Pages 11 – 20 (Infibeam Avenues, Team Computer, Bajaj, FinQuest, Nimap Infotech AIGForgeHub, Axis Bank, Shreeji Avenue, Transbnk, Ameya Infovision, Pycraft, FYN Tune, SBI)](#section-2-pages-11--20)
3. [Section 3: Pages 21 – 30 (Tansbank Angular & Concurrency, Thermolab, Paycraft, C2L BIZ, Wipro, Rebit, NeoSoft, Cbt Infotech, Aurionpro, In Solutions Global)](#section-3-pages-21--30)
4. [Section 4: Pages 31 – 40 (In Solutions Global, Servosys, Smart e, Bajaj Finserv, C2L, NCS Soft, NeoSoft)](#section-4-pages-31--40)
5. [Section 5: Pages 41 – 50 (Servosys, Qtech, ISG, NuSummit, Furation Tech SBI, V2STech, NuSummit UAT/Deployment, Vizpay, Fable Fintech)](#section-5-pages-41--50)
6. [Section 6: Pages 51 – 57 (Mirae Asset Rate Limiting, Transbnk R1 & R2, Crisil, Applied Cloud Computer, V2STech RightDigit, Applied Cloud Computing F2F, Saint Gobain)](#section-6-pages-51--57)

---

# Section 1: Pages 1 – 10

---

## 🏢 Company: Paramatrix Technologies (Page 1)

### Q1. Can we override the main method in Java?
- **Direct Answer:** **No**, we cannot override the `main` method in Java. However, we can **shadow (hide)** it or **overload** it.
- **Architectural & JVM Explanation:**
  - Method overriding in Java is based on **Dynamic Method Dispatch (runtime polymorphism)**, which resolves methods using the runtime object instance via the virtual method table (`vtable`).
  - The `main` method is declared `static`: `public static void main(String[] args)`. Static methods belong to the **Class (metaspace)**, not to an object instance on the heap.
  - If a child class defines a method with the exact same signature `public static void main(String[] args)`, this is **Method Hiding**, not overriding. The method invoked depends strictly on the compile-time reference type.
```java
class Parent {
    public static void main(String[] args) {
        System.out.println("Parent main");
    }
}
class Child extends Parent {
    // This is method hiding, NOT overriding
    public static void main(String[] args) {
        System.out.println("Child main");
    }
}
```

---

### Q2. Method overloading with example
- **Concept:** Overloading (Compile-time / Static Polymorphism) occurs when two or more methods in the same class share the exact same name but have **different parameter lists** (different parameter count, data types, or sequence). The return type alone cannot be used to distinguish overloaded methods.
- **Production Code Example (Loan Origination System - LOS Calculator):**
```java
public class LoanCalculatorService {
    // Overload 1: Standard personal loan
    public BigDecimal calculateMonthlyEmi(BigDecimal principal, double annualRate, int tenureMonths) {
        double monthlyRate = annualRate / 12 / 100;
        double emi = (principal.doubleValue() * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths)) 
                     / (Math.pow(1 + monthlyRate, tenureMonths) - 1);
        return BigDecimal.valueOf(emi).setScale(2, RoundingMode.HALF_UP);
    }

    // Overload 2: Loan with processing fee waiver and custom credit tier discount
    public BigDecimal calculateMonthlyEmi(BigDecimal principal, double annualRate, int tenureMonths, double creditScoreDiscount) {
        double adjustedRate = Math.max(annualRate - creditScoreDiscount, 5.0);
        return calculateMonthlyEmi(principal, adjustedRate, tenureMonths);
    }
}
```

---

### Q3. How will you write a list of arrays of student type?
- In Java, you can declare a `List<Student[]>`:
```java
record Student(Long id, String name, double gpa) {}

public class StudentListArrayDemo {
    public static void main(String[] args) {
        // Declaration: List containing arrays of Student
        List<Student[]> studentBatches = new ArrayList<>();

        Student[] batchA = new Student[]{
            new Student(101L, "Aryan", 3.8),
            new Student(102L, "Neha", 3.9)
        };
        Student[] batchB = new Student[]{
            new Student(201L, "Rahul", 3.7),
            new Student(202L, "Pooja", 4.0)
        };

        studentBatches.add(batchA);
        studentBatches.add(batchB);

        // Flattening & processing using Stream flatMap
        List<Student> allStudents = studentBatches.stream()
            .flatMap(Arrays::stream)
            .toList();
    }
}
```

---

### Q4. What are the different ways to pass parameters to a method inside another method?
1. **Pass-by-Value (Primitives):** Copies the raw primitive value (e.g. `int`, `double`). Changes inside the method do not affect the caller.
2. **Pass-by-Value of Object Reference:** Java is strictly **pass-by-value**. For objects, the copy of the *reference pointer* is passed. Mutating object fields alters the heap object, but reassigning the reference variable does not reassign the caller's reference.
3. **Varargs (`T... args`):** Allows passing 0 or multiple arguments, treated internally as an array.
4. **Method References & Functional Lambdas:** Passing behavior (`Function<T, R>`, `Predicate<T>`, `Supplier<T>`).
5. **DTO / Parameter Object Pattern:** Encapsulating multiple related parameters into a single Java 21 `record` or class.

---

## 🏢 Company: Zycus Infotech Pvt. Ltd (Page 1)

### Q1. How are exceptions handled in distributed systems?
- **Real-World Architectural Problem:** In microservices, Service A calls Service B, which calls Service C. If Service C fails with an SQL exception, how do we prevent Service A and B from hanging, returning inconsistent 500 errors, or leaking raw stack traces?
- **Production Solution Architecture:**
  1. **Standardized RFC 7807 `ProblemDetail`:** All microservices expose errors using the standard RFC 7807 JSON structure containing `type`, `title`, `status`, `detail`, `instance`, and `correlationId`.
  2. **Feign `ErrorDecoder`:** When Feign receives HTTP 4xx/5xx from downstream, `ErrorDecoder` translates it into strongly typed domain exceptions (`DownstreamServiceException`, `ResourceNotFoundException`) rather than generic `FeignException`.
  3. **Circuit Breakers & Fallbacks (Resilience4j):** If Service C fails repeatedly, the circuit trips to `OPEN`, immediately returning cached or degraded default data instead of propagating timeouts.
  4. **Distributed Tracing (W3C / OpenTelemetry):** A unique `traceId` injected into MDC logging is attached to the error payload so customer support can locate the root-cause log in Kibana immediately.

---

### Q2. What is system design?
- **Concept:** System Design is the process of defining the architecture, modules, interfaces, and data models of a distributed software system to satisfy both **Functional Requirements** (what the system does: e.g. Book a ride, process payment) and **Non-Functional Requirements** (how well it performs: Scalability, Availability, Latency, Fault Tolerance, Consistency, Security).
- **Core Pillars:**
  - **Scalability:** Horizontal (Scale-Out) vs Vertical (Scale-Up).
  - **Availability:** 99.99% ("Four Nines"), active-active multi-region failover.
  - **Consistency:** ACID vs BASE (Eventual Consistency), CAP Theorem (Consistency vs Availability under Partition).
  - **Performance:** Caching (Redis), CDN, Database Indexing, Read Replicas, Asynchronous Queues (Kafka).

---

### Q3. How do you design a high-level architecture for an application? What are the steps?
- **The 5-Step Enterprise Architecture Framework:**
  1. **Requirements & Scope Clarification:** Establish functional scope (user workflows) and non-functional requirements (DAU, Peak TPS, read/write ratio, acceptable latency < 200ms).
  2. **Capacity Estimation (Back-of-the-envelope):** Compute bandwidth, storage growth over 5 years, memory cache size (80/20 Pareto rule), and database IOPS.
  3. **Domain Modeling & Bounded Contexts:** Apply Domain-Driven Design (DDD) to partition services (e.g. Identity Service, Loan Application Service, Underwriting Service, Disbursement Service).
  4. **High-Level Component Diagram:** Place API Gateway (rate limiting, auth), Load Balancers, Stateless Application Pods, Caches (Redis), Databases (PostgreSQL/MongoDB), and Message Brokers (Kafka).
  5. **Deep Dive & Failure Modes:** Address single points of failure (SPOFs), replication lag, circuit breakers, idempotency keys, and zero-downtime deployment pipelines.

---

### Q4. What are the steps to implement transaction management in Spring Boot?
1. **Enable Transaction Management:** `@EnableTransactionManagement` (auto-configured by `spring-boot-starter-data-jpa`).
2. **Apply `@Transactional` at Service Layer:** Define rollback rules (e.g. `@Transactional(rollbackFor = Exception.class)`).
3. **Configure Propagation & Isolation:**
   - Propagation: `REQUIRED` (joins existing or creates new), `REQUIRES_NEW` (suspends existing, creates independent transaction for auditing/logging).
   - Isolation: `READ_COMMITTED` (prevents dirty reads, default for Postgres/Oracle).
4. **Read-Only Optimization:** Use `@Transactional(readOnly = true)` on query methods to tell Hibernate to skip dirty-checking snapshots, cutting CPU and memory overhead by 40%.
5. **Beware of Self-Invocation:** Calling a `@Transactional` method from another method within the same class bypasses the Spring CGLIB proxy, disabling transaction interception! Inject `self` or separate into another `@Service` bean.

---

## 🏢 Company: C-EDGE (Page 1)

### Q1. What is `@Controller` and `@ControllerAdvice`?
- **`@Controller`:** Standard Spring MVC stereotype annotation used to define web request handlers returning view templates (JSP, Thymeleaf) or JSON (if combined with `@ResponseBody`). `@RestController` is a convenience shorthand combining `@Controller` + `@ResponseBody`.
- **`@ControllerAdvice`:** A component specialization for **cross-cutting concerns** (global exception handling, model attributes, and request/response body advice) across all `@Controller` / `@RestController` classes via Spring AOP.
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }
}
```

---

### Q2. `@Component` vs `@Bean` annotations
| Feature | `@Component` | `@Bean` |
|---|---|---|
| **Target** | Class level (`@Target(ElementType.TYPE)`) | Method level (`@Target(ElementType.METHOD)`) |
| **Discovery** | Auto-detected via classpath scanning (`@ComponentScan`) | Explicitly declared inside `@Configuration` classes |
| **Use Case** | Your own application classes (Services, Repositories) | Third-party library classes (e.g. `ObjectMapper`, `RestClient`, `SecurityFilterChain`) |
| **Custom Initialization** | Via `@PostConstruct` | Full programmatic control over constructor arguments & logic |

---

### Q3. Second Largest Number in Array (Java Streams & Imperative)
```java
public class SecondLargestNumber {
    // Approach 1: Java Stream API (Handles duplicates properly)
    public static OptionalInt findSecondLargestStream(int[] arr) {
        return Arrays.stream(arr)
            .distinct()
            .boxed()
            .sorted(Comparator.reverseOrder())
            .skip(1)
            .mapToInt(Integer::intValue)
            .findFirst();
    }

    // Approach 2: O(N) Single-Pass Imperative (Highest Performance)
    public static int findSecondLargestImperative(int[] arr) {
        int first = Integer.MIN_VALUE;
        int second = Integer.MIN_VALUE;
        for (int num : arr) {
            if (num > first) {
                second = first;
                first = num;
            } else if (num > second && num != first) {
                second = num;
            }
        }
        if (second == Integer.MIN_VALUE) throw new IllegalArgumentException("No second largest distinct element");
        return second;
    }
}
```

---

### Q4. What is a Stream in Java?
- A **Stream** (introduced in Java 8) is a sequence of elements supporting functional-style declarative operations (filtering, mapping, reducing, sorting).
- **Key Characteristics:**
  - **Not a Data Structure:** It does not store elements; it carries values from a source (Collection, Array, I/O channel) through a pipeline.
  - **Laziness:** Intermediate operations (`filter`, `map`) are not evaluated until a terminal operation (`collect`, `count`, `forEach`) is invoked.
  - **Consumable Once:** A stream cannot be reused once traversed.

---

### Q5. What is `parallelStream()` vs `stream()`?
- `stream()` executes sequentially on the calling thread.
- `parallelStream()` partitions the workload into chunks executed concurrently across multiple threads using the shared **ForkJoinPool.commonPool()**.
- **When to use `parallelStream()`:**
  - Massive datasets ($N > 50,000$).
  - CPU-bound computations (math, encryption, hashing).
- **When to AVOID `parallelStream()`:**
  - I/O bound operations (REST calls, DB queries) — blocks threads in the shared common pool, starving the entire JVM.
  - ThreadLocal-dependent contexts (e.g. `SecurityContextHolder` is not propagated to child ForkJoin threads!).

---

### Q6. What is `ConcurrentModificationException`?
- Thrown when a thread modifies a Collection structurally (adding or removing elements) while another thread (or an Iterator on the same thread) is iterating over it using a **Fail-Fast** iterator (e.g. `ArrayList`, `HashMap`, `HashSet`).
- **How to avoid it:**
  1. Use `Iterator.remove()` instead of `collection.remove()`.
  2. Use concurrent collections with **Fail-Safe / Weakly Consistent** iterators: `CopyOnWriteArrayList`, `ConcurrentHashMap`.
  3. Use Java 8 `removeIf(predicate)`.

---

### Q7. What are the Stereotype annotations in Spring?
- `@Component` (Root stereotype).
  - `@Service`: Marks business logic layer.
  - `@Repository`: Marks persistence layer (enables automatic persistence exception translation from JDBC/JPA into Spring's `DataAccessException`).
  - `@Controller` / `@RestController`: Marks web presentation layer.
  - `@Configuration`: Marks bean definition factory class (CGLIB proxied).

---

### Q8. How many types of View Resolvers are there in Spring MVC?
- `InternalResourceViewResolver`: Resolves JSP files (`/WEB-INF/views/` + name + `.jsp`).
- `ThymeleafViewResolver`: Integrates Thymeleaf HTML templates.
- `FreeMarkerViewResolver`: Resolves FreeMarker templates.
- `ContentNegotiatingViewResolver`: Resolves views dynamically based on the client's `Accept` HTTP header (JSON, XML, or HTML).

---

### Q9. How can you configure 2 databases in one `application.properties` file?
- **Concept:** Declare two distinct data sources with unique prefixes, designate one as `@Primary`, and configure separate `EntityManagerFactory` and `PlatformTransactionManager` beans for each.
```properties
# Primary DB (Orders)
spring.datasource.orders.jdbc-url=jdbc:postgresql://localhost:5432/orders_db
spring.datasource.orders.username=postgres
spring.datasource.orders.password=secret

# Secondary DB (Inventory)
spring.datasource.inventory.jdbc-url=jdbc:postgresql://localhost:5432/inventory_db
spring.datasource.inventory.username=postgres
spring.datasource.inventory.password=secret
```
```java
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.enterprise.orders.repository",
    entityManagerFactoryRef = "ordersEntityManagerFactory",
    transactionManagerRef = "ordersTransactionManager"
)
public class OrdersDbConfig {
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.orders")
    public DataSourceProperties ordersDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource ordersDataSource() {
        return ordersDataSourceProperties().initializeDataSourceBuilder().build();
    }
}
```

---

### Q10. Why was `default` method introduced in interfaces if `static` method was already introduced?
- **Primary Reason:** **Backward Compatibility for Collections Framework in Java 8.**
- Java 8 added lambda and stream methods like `stream()`, `parallelStream()`, and `forEach()` to the `java.util.Collection` and `java.lang.Iterable` interfaces.
- If interfaces only allowed abstract methods, adding `stream()` to `Collection` would instantly **break every third-party library on earth** (Hibernate, Guava, Apache Commons) that implemented `Collection`.
- `default` methods allow interface designers to add new methods with an implementation that implementing classes automatically inherit without breaking compilation.
- **Why not `static` alone?** `static` methods cannot be overridden by implementing classes and cannot operate polymorphism on the object instance (`this`). `default` methods are virtual and polymorphic.

---

## 🏢 Company: NUSUMMIT (Page 2)

### Q1. What are the core features of Java 8?
1. **Lambda Expressions:** Anonymous functions enabling functional programming paradigms.
2. **Stream API (`java.util.stream`):** Declarative data pipelining with lazy evaluation.
3. **Functional Interfaces (`@FunctionalInterface`):** Single Abstract Method (SAM) interfaces (`Predicate`, `Function`, `Consumer`, `Supplier`).
4. **Default and Static Methods in Interfaces:** Enabling interface evolution without breaking backward compatibility.
5. **Optional Class (`java.util.Optional`):** Container object to avoid `NullPointerException`.
6. **New Date and Time API (`java.time`):** Thread-safe, immutable classes (`LocalDate`, `LocalDateTime`, `ZonedDateTime`, `Instant`).
7. **Nashorn JavaScript Engine:** Modernized JS runtime on the JVM (later deprecated in Java 11/15).
8. **Method References (`Class::method`):** Compact syntactic sugar for calling existing methods via lambdas.

---

### Q2. Difference between `stream()` and `parallelStream()` API
- `stream()` operates sequentially on the current thread, ideal for small-to-medium datasets ($N < 10,000$) or I/O-bound operations.
- `parallelStream()` decomposes the task into sub-tasks executed across the common `ForkJoinPool.commonPool()`.
- **Performance Caution:** In a web application handling 500 concurrent HTTP requests, running `parallelStream()` inside request threads can saturate the shared `ForkJoinPool`, blocking all other requests and causing severe latency spikes.

---

### Q3. Explain Spring Security
- Spring Security is a declarative framework operating as a chain of servlet filters (`SecurityFilterChain`) executed ahead of the Spring MVC `DispatcherServlet`.
- **Core Architecture:**
  - `DelegatingFilterProxy` bridges servlet container filters to Spring's `FilterChainProxy`.
  - `Authentication`: Verifies user credentials via `AuthenticationManager` and `DaoAuthenticationProvider`.
  - `Authorization`: Verifies role-based permissions (`ROLE_ADMIN`, `hasAuthority`) via `AuthorizationFilter`.
  - Provides built-in defenses against CSRF, CORS, Session Fixation, and Clickjacking (`X-Frame-Options`).

---

### Q4. SQL Query: Second highest salary in SQL
```sql
-- Solution 1: Using Subquery & MAX (Works across all SQL databases)
SELECT MAX(salary) AS second_highest_salary
FROM employees
WHERE salary < (SELECT MAX(salary) FROM employees);

-- Solution 2: Using DENSE_RANK() Window Function (Enterprise Standard)
WITH RankedSalaries AS (
    SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) as rank_num
    FROM employees
)
SELECT salary FROM RankedSalaries WHERE rank_num = 2 LIMIT 1;
```

---

### Q5. Why do we use Stream API?
1. **Declarative & Readable:** Eliminates nested `for-loops` and `if-else` boilerplate.
2. **Laziness & Efficiency:** Elements are computed only when the terminal operation is executed, allowing short-circuit optimizations (`findFirst()`, `anyMatch()`).
3. **Immutability & Thread Safety:** Streams do not mutate their backing source collections.
4. **Easy Parallelization:** Switching to parallel execution requires only calling `.parallel()`.

---

### Q6. What is a Functional Interface?
- An interface that contains **exactly one abstract method**.
- Annotated with `@FunctionalInterface` (compiler check to prevent adding extra abstract methods).
- Can contain any number of `default` and `static` methods.
- Serves as the target type for **Lambda Expressions** and **Method References**.

---

## 🏢 Company: Inferyx Hinjewadi (Page 2)

### Q1. Difference between Shallow Copy & Deep Copy
- **Shallow Copy:** Copies primitive fields and duplicate the reference pointer for object fields. Both original and copied objects point to the **same internal heap object**. Modifying a nested object in the copy mutates the original!
- **Deep Copy:** Recursively clones all nested objects, creating completely independent object graphs in heap memory.
```java
// Deep copy via copy constructor
public class DeepEmployee {
    private String name;
    private Department dept;

    public DeepEmployee(DeepEmployee other) {
        this.name = other.name;
        this.dept = new Department(other.dept.getId(), other.dept.getName()); // Deep copy
    }
}
```

---

### Q2. What is the `volatile` keyword?
- `volatile` guarantees **Visibility** and **Ordering (prevents instruction reordering)** in multithreaded programs.
- It forces the JVM to read and write the variable directly from/to **Main Memory (RAM)** rather than caching it in CPU L1/L2 registers.
- **Limitation:** `volatile` does **NOT** guarantee atomicity (e.g., `count++` is a compound read-modify-write operation and is NOT thread-safe with `volatile` alone; use `AtomicInteger`).

---

### Q3. Difference between `volatile` and `synchronized`
| Feature | `volatile` | `synchronized` |
|---|---|---|
| **Scope** | Variables only | Methods and code blocks |
| **Visibility** | Yes (flushes to RAM) | Yes (flushes to RAM upon exiting monitor) |
| **Atomicity** | No (cannot protect compound operations) | Yes (mutual exclusion via monitor locks) |
| **Thread Blocking**| Never blocks threads (Lock-free) | Blocks threads (puts into BLOCKED state) |

---

### Q4. What is `FutureTask`?
- A concrete class implementing `RunnableFuture<V>`, which extends both `Runnable` and `Future<V>`.
- Can be submitted to an `ExecutorService` or passed directly to a new `Thread(futureTask).start()`.
- Provides cancellation, status queries (`isDone()`), and blocking result retrieval (`get()`).

---

### Q5. Thread Locker & `ThreadLocal` Memory Safety
- **`ThreadLocal`:** Provides thread-isolated state where each accessing thread gets its own independent copy of a variable.
- **Enterprise Hazard (Memory Leaks in Thread Pools):** In Tomcat/Jetty web servers, threads are reused from a pool. If a request sets data in a `ThreadLocal` (e.g. `SecurityContextHolder`, `TenantContextHolder`) and fails to call `.remove()` in a `finally` block, the thread retains that object forever in its `threadLocals` map, causing memory leaks and cross-request data corruption!

---

### Q6. `Runnable` vs `Callable`
- `Runnable`: `public void run()` — Cannot return a value, cannot throw checked exceptions.
- `Callable<V>`: `public V call() throws Exception` — Returns a parameterized value `V` and can throw checked exceptions.

---

### Q7. Where do you use `Runnable` and `Callable` in your project?
- **`Callable<V>`:** Used when querying downstream services asynchronously in parallel (e.g. In Loan Origination System, calling CIBIL credit score API and Aadhaar KYC verification concurrently via `CompletableFuture.supplyAsync()` or `executorService.submit(callable)` to aggregate data).
- **`Runnable`:** Used for fire-and-forget background tasks (e.g. asynchronously appending an audit trail log, sending an email confirmation via `@Async`).

---

### Q8. Memory leak reasons and troubleshooting: How do you handle this?
- **Root Causes:**
  1. Static collections (`static Map<String, Object> cache`) holding references without eviction.
  2. Unclosed I/O streams, database connections, or ResultSet cursors.
  3. Uncleared `ThreadLocal` variables in pooled threads.
  4. Non-static inner classes holding implicit references to outer enclosing classes.
- **Production Troubleshooting Steps:**
  1. Generate heap dump on OOM: `-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/dumps/oom.hprof`.
  2. Analyze dump using **Eclipse Memory Analyzer (MAT)** or JProfiler to inspect Leak Suspects and Dominator Trees.
  3. Fix the leak (e.g. apply WeakReferences, Caffeine cache with TTL, or enforce `try-with-resources`).

---

### Q9. HashMap internal structure & mechanics
- An array of `Node<K,V>[] table` (Buckets) with default initial capacity 16 and load factor 0.75.
- **Put Operation:**
  1. Calculates hash: `hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16)`.
  2. Index calculation: `index = (n - 1) & hash`.
  3. If collision occurs, appends to a singly linked list.
  4. **Treeification (Java 8):** When a bucket has $\ge 8$ nodes and total table capacity $\ge 64$, the linked list converts to a **Red-Black Balanced Binary Search Tree (`TreeNode`)**, reducing search time from $O(N)$ to $O(\log N)$.

---

### Q10. How do you handle a High CPU alert in production?
1. **Identify problematic PID:** Run `top` or `htop` to identify the high-CPU Java process ID.
2. **Find offending Thread ID:** Run `top -H -p <PID>` to see top threads consuming CPU. Note the Thread ID (e.g., 24915).
3. **Convert Thread ID to Hexadecimal:** `printf '%x
' 24915` $ightarrow$ `0x6153`.
4. **Capture Thread Dumps:** Run `jcmd <PID> Thread.print > threaddump.txt` or `jstack <PID> > threaddump.txt`.
5. **Analyze Stack Trace:** Grep the hex thread ID `grep -A 30 "0x6153" threaddump.txt`. Look for infinite loops, runaway regex, or heavy GC thread activity (`VM Thread`).

---

### Q11 to Q14. Spark API, Big Data Technologies, Kafka & Consumer Mechanics
- **Spark API:** In-memory distributed computing engine utilizing Resilient Distributed Datasets (RDDs) and DataFrames/Datasets for batch and micro-batch stream processing.
- **Apache Kafka:** A distributed, partitioned, replicated append-only commit log.
- **How data comes from Kafka:**
  - Producers write records partitioned by key (`murmur2(key) % partitionCount`).
  - Consumers join a **Consumer Group** and pull data via a long-polling loop (`consumer.poll(Duration.ofMillis(100))`).
  - Offsets are committed periodically (`enable.auto.commit` or manual `ackMode.MANUAL_IMMEDIATE`).

---

## 🏢 Company: Applied Cloud Computing (Page 2)

### Q1. Count file extensions map using Java Streams
- **Problem:** Given `List<String> files = List.of("a.txt", "b.pdf", "c.txt", "d.pdf", "e.pdf", "f.docx")`, create a `Map<String, Long>` with extension counts.
```java
public class FileExtensionCounter {
    public static Map<String, Long> countExtensions(List<String> fileNames) {
        return fileNames.stream()
            .filter(name -> name.contains("."))
            .map(name -> name.substring(name.lastIndexOf('.') + 1).toLowerCase())
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            ));
    }
}
// Output: {txt=2, pdf=3, docx=1}
```

---

### Q2. What are the advantages of Hibernate?
1. **Object-Relational Mapping (ORM):** Maps Java POJOs directly to database tables, eliminating JDBC SQL boilerplate.
2. **Database Portability:** Uses Hibernate Dialects (`PostgreSQLDialect`, `MySQLDialect`) to generate vendor-specific SQL without altering Java code.
3. **Caching Architecture:** First-Level Cache (Session level) and Second-Level Cache (SessionFactory level via Redis/Ehcache) reduce database query load.
4. **Lazy Loading:** Fetches associated collections and entities only when accessed.
5. **Automatic Schema Generation:** Generates DDL schemas from entity annotations (`validate`, `update`).

---

### Q3. Program to remove duplicates from an array/list
```java
public class RemoveDuplicates {
    public static List<Integer> removeDuplicatesStream(List<Integer> list) {
        return list.stream().distinct().toList();
    }

    public static int[] removeDuplicatesFromSortedArray(int[] nums) {
        if (nums.length == 0) return nums;
        int writeIndex = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[i - 1]) {
                nums[writeIndex++] = nums[i];
            }
        }
        return Arrays.copyOf(nums, writeIndex);
    }
}
```

---

### Q4. What is the N+1 problem in JPA?
- When fetching $N$ records from a parent table, executing 1 query for the parent results in $N$ additional queries to fetch the child association:
  - Query 1: `SELECT * FROM employees;` (returns 100 rows).
  - Queries 2-101: `SELECT * FROM departments WHERE id = ?;` (executed 100 times!).
- **Resolution:** Use `JOIN FETCH` in JPQL, `@EntityGraph(attributePaths = {"department"})`, or set `hibernate.default_batch_fetch_size=50`.

---

### Q5. What is Lazy Loading?
- Delaying the initialization of an associated entity or collection until it is explicitly accessed in code (`FetchType.LAZY`).
- Implemented using ByteBuddy dynamic proxies. If accessed after the Hibernate session is closed, it throws `LazyInitializationException`.

---

### Q6. What microservice architecture is used in your project?
- **Enterprise Reference Architecture (Loan Origination System - LOS):**
  - **API Gateway (Spring Cloud Gateway):** Single entry point handling TLS termination, rate-limiting, edge JWT authentication, and global CORS.
  - **Service Registry (Netflix Eureka / Consul):** Dynamic microservice registration and discovery.
  - **Core Domain Services:**
    - `Customer-Service`: KYC, customer identity, PAN/Aadhaar details.
    - `Loan-Application-Service`: Application lifecycle management.
    - `Underwriting-Service`: CIBIL credit scoring and eligibility calculation.
    - `Disbursement-Service`: Bank payment release.
  - **Communication:** Synchronous OpenFeign with Resilience4j circuit breakers for reads; Apache Kafka for async event transitions.
  - **Distributed Tracing:** Micrometer Tracing with W3C Trace IDs shipped to ELK.

---

### Q7. What is `@Entity`?
- A Jakarta Persistence JPA annotation marking a Java class as a persistent database entity mapped to a database table.
- Requirements: Must have a no-arg constructor, must have a primary key marked with `@Id`, and cannot be final.

---

## 🏢 Company: ParaMatrix (Page 3) — Real-World Project: Loan Origination System (LOS)

### Q1 & Q2. Project Architecture (LOS) & Services Involved
- **Domain Overview:** A Loan Origination System (LOS) processes commercial and retail loan applications from lead generation, KYC verification, underwriting, risk scoring, document verification, to disbursement.
```
┌───────────────────────────────── LOS Architecture ─────────────────────────────────┐
│                                                                                    │
│   [ Angular 17+ Client ] ──► HTTPS ──► [ Spring Cloud Gateway (Port 8080) ]        │
│                                                   │                                │
│        ┌────────────────────────┬─────────────────┴───────────────┬───────────┐    │
│        ▼                        ▼                                 ▼           ▼    │
│  [ Auth Service ]     [ Customer Service ]           [ Loan Service ]   [ Underwriting ]
│  (JWT / Keycloak)     (KYC, Aadhaar / PAN)           (Application DTO)  (CIBIL Engine)
│                                 │                                 │           │    │
│                                 └────────► [ Kafka Topic: "loan-events" ] ◄───┘    │
│                                                           │                        │
│                                                           ▼                        │
│                                                [ Notification Service ]            │
│                                                                                    │
└────────────────────────────────────────────────────────────────────────────────────┘
```

---

### Q3 & Q4. OOPs in Java & `==` vs `.equals()`
- **OOP Pillars:** Encapsulation (data hiding), Abstraction (hiding implementation details via interfaces), Inheritance (code reuse), Polymorphism (overriding/overloading).
- **`==` vs `.equals()`:**
  - `==` compares **memory references** (checks if both pointers point to the same heap address).
  - `.equals()` checks for **content / logical equality** when overridden (e.g. `String`, `Integer`).

---

### Q5. Abstract Class vs Interface (Java 8 - 21)
| Feature | Interface | Abstract Class |
|---|---|---|
| **Multiple Inheritance** | Can implement multiple interfaces | Single inheritance only (`extends`) |
| **State / Fields** | Only `public static final` constants | Can have instance variables with state |
| **Constructors** | No constructors | Can have constructors (called via `super()`) |
| **Methods** | Abstract, `default`, `static`, `private` (Java 9) | Abstract and concrete instance methods |

---

### Q7. `String` vs `StringBuffer` vs `StringBuilder`
- `String`: Immutable. Stored in String Constant Pool (SCP) or heap.
- `StringBuffer`: Mutable, **Thread-Safe** (methods are `synchronized`), slower.
- `StringBuilder`: Mutable, **Not Thread-Safe**, faster, best for single-threaded string concatenation.

---

### Q8. What is a Deadlock and how to detect it?
- A condition where two or more threads are blocked forever, each waiting on a lock held by the other.
- **Four Coffman Conditions:** Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait.
- **Detection:** `jcmd <PID> Thread.print` or JConsole highlights "Found 1 deadlock".
- **Prevention:** Always acquire locks in a consistent, global order.

---

### Q9 & Q10. Explain Spring Boot & `application.properties` vs `.yml`
- Spring Boot provides opinionated, production-ready defaults with embedded servlet containers (Tomcat), starter dependencies, and auto-configuration.
- `application.yml` provides hierarchical, indented key-value structures supporting arrays, maps, and cleaner readability compared to flat `application.properties`.

---

### Q11. What is an API Gateway?
- The single reverse-proxy entry point for client applications. Handles cross-cutting concerns: SSL termination, CORS, central rate limiting, edge authentication, response compression, and routing requests to downstream microservice instances.

---

### Q12 to Q14. Primary Key, Unique Key, Foreign Key & SQL JOINs
- **Primary Key:** Uniquely identifies a record, cannot contain `NULL`.
- **Unique Key:** Guarantees uniqueness, can accept a single `NULL` value in standard SQL.
- **Foreign Key:** References the Primary Key of another table, enforcing referential integrity.
- **JOIN Types:**
  - `INNER JOIN`: Returns records with matching values in both tables.
  - `LEFT OUTER JOIN`: Returns all records from left table and matched from right.
  - `FULL OUTER JOIN`: Returns all records when there is a match in either table.

---

### Q15. SQL Query: Deleting duplicate data from a table
```sql
-- Deleting duplicates while keeping the record with the lowest ID
DELETE FROM customers
WHERE id NOT IN (
    SELECT min_id FROM (
        SELECT MIN(id) AS min_id
        FROM customers
        GROUP BY email
    ) AS temp
);
```

---

### Q16 to Q18. Project Challenges, Production Issues & Team Collaboration
- **Challenge:** Managing distributed data consistency during loan disbursement. Solved by implementing an orchestrated Saga pattern with compensating refund transactions.
- **Production Issue:** High latency on customer search API. Solved by creating a compound index `(last_name, phone_number)` on PostgreSQL and caching frequent lookups in Redis with a 15-minute TTL.
- **Team Work:** Agile Scrum framework, daily standups, code reviews with SonarQube quality gates, and automated CI/CD via Jenkins & ArgoCD.

---

## 🏢 Company: Teams Computers (Page 3 & 4)

### Q1. How do you manage load on an API?
1. **API Rate Limiting:** Implement Token Bucket algorithm using Bucket4j and Redis to limit clients to 100 requests/minute.
2. **Horizontal Autoscaling (HPA):** Scale Kubernetes pods based on CPU/Memory and custom Prometheus metrics.
3. **Caching Layer:** Cache high-frequency read endpoints using Redis.
4. **Asynchronous Offloading:** Shift heavy write requests to Apache Kafka or RabbitMQ queues.
5. **Database Connection Pooling:** Tune HikariCP pool (`maximumPoolSize=25`).

---

### Q2. Solve Palindrome using Stream API
```java
public class StreamPalindrome {
    public static boolean isPalindrome(String str) {
        String clean = str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return IntStream.range(0, clean.length() / 2)
            .allMatch(i -> clean.charAt(i) == clean.charAt(clean.length() - i - 1));
    }
}
```

---

### Q3. Solve Anagram using Stream API
```java
public class StreamAnagram {
    public static boolean isAnagram(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        
        Map<Character, Long> freq1 = s1.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<Character, Long> freq2 = s2.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return freq1.equals(freq2);
    }
}
```

---

### Q4. What is a Virtual Thread (Java 21)?
- Lightweight threads managed by the **JVM** rather than the OS kernel.
- Unlike platform threads (which consume ~1MB stack and are capped at ~5,000 per JVM), millions of virtual threads can run concurrently.
- When a virtual thread performs blocking I/O (e.g. database query, HTTP call), the JVM unmounts it from the carrier OS thread, freeing the carrier thread to run other work.

---

### Q5. What are the features of Java 17 (LTS)?
1. **Sealed Classes (`sealed`, `permits`):** Restricts which classes can extend or implement them.
2. **Records (`record`):** Transparent, immutable data-carrier classes.
3. **Pattern Matching for `switch`:** Evaluates switch branches based on type and predicates.
4. **Text Blocks (`"""`):** Multiline strings without escaping quotes.
5. **Enhanced Pseudo-Random Number Generators.**
6. **Removal of RMI Activation and Deprecation of Applet API.**

---

### Q6. What is `@Transactional`?
- Spring annotation managing declarative database transactions. Wraps the target method in an interceptor that creates a transaction, commits upon successful return, and rolls back if an unhandled `RuntimeException` or specified `rollbackFor` exception occurs.

---

### Q7. What are the types of Cache?
1. **In-Memory Local Cache:** Ehcache, Caffeine, Guava (fastest, JVM heap, non-distributed).
2. **Distributed Cache:** Redis, Memcached, Hazelcast (shared across all microservice instances).
3. **Database Caching:** First-level cache (Hibernate Session), Second-level cache (SessionFactory).
4. **HTTP / CDN Caching:** Cloudflare, Akamai, browser `Cache-Control` headers.

---

## 🏢 Company: Inferyx Hinjewadi — Coding Problems (Page 4)

### Q1 to Q3. Even at even place and odd at odd place in sorted order (No in-built sort)
- **Problem:** Array input `[5, 10, 9, 14, 7, 12]`. Output: even indices have even numbers sorted, odd indices have odd numbers sorted: `[10, 5, 12, 7, 14, 9]`.
```java
public class EvenOddArrangement {
    public static void arrange(int[] arr) {
        List<Integer> evens = new ArrayList<>();
        List<Integer> odds = new ArrayList<>();
        
        for (int num : arr) {
            if (num % 2 == 0) evens.add(num);
            else odds.add(num);
        }

        // Manual Sort (Bubble sort without built-in libraries)
        manualSort(evens);
        manualSort(odds);

        int e = 0, o = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i % 2 == 0 && e < evens.size()) {
                arr[i] = evens.get(e++);
            } else if (o < odds.size()) {
                arr[i] = odds.get(o++);
            }
        }
    }

    private static void manualSort(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
}
```

---

### Q4. Reverse array in-place without additional array or built-in functions
```java
public class InPlaceArrayReverse {
    public static void reverse(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}
```

---

### Q5. Count duplicate words in a String into `Map<String, Integer>`
```java
public class WordFrequencyCounter {
    public static void countWords(String input) {
        String[] words = input.toLowerCase().split("\\s+");
        Map<String, Integer> wordMap = new HashMap<>();

        for (String word : words) {
            wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```

---

## 🏢 Company: Applied Cloud Computing (Page 4 & 5)

### Q1 & Q2. Exception Handling Output Puzzle
```java
try {
    // some code throwing IOException
} catch(Exception exception) {
    System.out.println(exception);
    throw exception;
} catch(IOException ioException) {
    System.out.println(ioException);
    throw ioException;
}
```
- **Output / Result:** **Compile-Time Error!**
- **Reason:** `IOException` is a subclass of `Exception`. Because `catch(Exception exception)` appears first, it catches all subclasses. The second block `catch(IOException)` is unreachable code, causing `error: exception IOException has already been caught`.

---

### Q4. Stream Map Output Puzzle (Page 5)
```java
List<Integer> list = new ArrayList<>();
list.add(4);
list.add(4);
list.add(4);
list.add(4);
list.stream().map(i -> i * i);
for(Integer num: list){
    System.out.println(num);
}
```
- **Output:**
```
4
4
4
4
```
- **Reason:** Streams are **lazy**. `list.stream().map(...)` is an intermediate operation with no terminal operation attached. It was never executed! Furthermore, streams never mutate the underlying source list.

---

### Q5. Nth Highest Price Product from Map (Page 5)
```java
public class NthHighestProduct {
    public static Optional<Map.Entry<String, Double>> findNthHighest(Map<String, String> map, int n) {
        return map.entrySet().stream()
            .map(e -> Map.entry(e.getKey(), Double.parseDouble(e.getValue())))
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .skip(n - 1)
            .findFirst();
    }
}
```

---

### Q6. Custom Exceptions in Java
```java
// Unchecked Business Exception
public class InsufficientLoanBalanceException extends RuntimeException {
    private final String errorCode;

    public InsufficientLoanBalanceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
```

---

## 🏢 Company: Teams Computers & Paramatrix (Kotak) (Pages 6 & 7)

### Q. Aadhaar and PAN Data Storage Security in Database
- **Compliance Requirement:** Storing Aadhaar and PAN data unencrypted violates RBI, UIDAI, and GDPR compliance regulations.
- **Enterprise Solution:**
  1. **Field-Level Encryption (AES-256-GCM):** Encrypt PAN/Aadhaar strings before writing to DB using Spring Data JPA `@Convert(converter = AesGcmCryptoConverter.class)`.
  2. **Data Masking in UI & Logs:** Display only the last 4 digits (`XXXXXXXX1234`).
  3. **Cryptographic Salted Hashing (SHA-256) for Searchability:** Maintain a hash column (`aadhaar_hash`) for index lookups without decrypting entire tables.

---

### Q. How is CIBIL Credit Score Checked in LOS?
1. `LoanApplicationService` issues an asynchronous HTTP request via OpenFeign to the external TransUnion / CIBIL credit bureau gateway.
2. The request carries borrower PAN, DOB, Name, and an HMAC cryptographic signature.
3. Bureau responds with CIBIL score (300 to 900), active credit lines, and default history.
4. **Underwriting Rules Engine:**
   - Score $\ge 750$: Pre-approved / Fast-track.
   - Score $650 - 749$: Manual Underwriting review.
   - Score $< 650$: Immediate rejection.

---

### Q. Why is `String` Immutable in Java?
1. **String Constant Pool (SCP):** Sharing string literals saves massive heap memory. Immutability guarantees that modifying a reference for one thread does not corrupt another thread's string.
2. **Security:** Strings carry sensitive data (passwords, network socket URLs, DB connection strings). Immutability prevents tampering.
3. **Thread Safety:** Automatically thread-safe without synchronization.
4. **Safe HashMap Keys:** The hash code is computed and cached on creation. If mutable, changing the string after storing in a HashMap would permanently lose the entry!

---

# Section 2: Pages 11 – 20

---

## 🏢 Company: Infibeam Avenues Ltd. (Pages 11 & 12)

### Q1. Main difference between Java 7 and Java 8
- **Java 7:** Imperative programming, ForkJoinPool, diamond operator `<>`, try-with-resources, strings in switch.
- **Java 8:** Introduced functional programming with Lambdas, Streams, Functional Interfaces, Default/Static interface methods, Optional, and `java.time` API.

---

### Q7 & Q8. Spring Security Architecture & JWT in Details
- Operates via `SecurityFilterChain`.
- **JWT (JSON Web Token):** Standard RFC 7519 compact, URL-safe container format consisting of three parts separated by dots:
  `Header.Payload.Signature`
  - **Header:** Algorithm and token type (`{"alg": "HS256", "typ": "JWT"}`).
  - **Payload (Claims):** Subject (`sub`), roles, expiration (`exp`), issuer (`iss`).
  - **Signature:** `HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secretKey)`.

---

### Q9. If JWT is exposed to a hacker, how will you protect the system?
1. **Short-Lived Access Tokens:** Keep access token lifetime very short (e.g., 10–15 minutes).
2. **Token Revocation Blacklist (Redis):** When a breach or logout occurs, record the token in Redis with a TTL equal to its remaining lifespan. `JwtAuthenticationFilter` checks this blacklist on every request.
3. **Refresh Token Rotation (RTR):** Store refresh tokens in `HttpOnly`, `SameSite=Strict` cookies. Every refresh issues a new refresh token and invalidates the previous one. If a reused refresh token is presented, revoke all sessions immediately!
4. **Token Fingerprinting / DPoP (Demonstrating Proof-of-Possession):** Bind the token cryptographically to the client's TLS certificate or public key.

---

### Q15. Service A calls B, B calls C, but C is down and C calls D. What happens and how to keep the program running smoothly?
- **Problem (Cascading Failure):** Without protection, threads in B block waiting for C to time out. B's thread pool exhausts, causing calls from A to B to stall, taking down the entire system.
- **Resilience Architecture:**
  1. **Circuit Breaker (Resilience4j):** Place a Circuit Breaker on B's client to C. If C fails repeatedly, B trips to `OPEN` and executes a local fallback without waiting for socket timeouts.
  2. **Short Timeouts:** Set aggressive connect (1s) and read (2s) timeouts on Feign/WebClient.
  3. **Bulkhead Pattern:** Isolate thread pools so that C's failure cannot consume threads dedicated to other services.
  4. **Asynchronous Decoupling (Kafka):** If the call is an update/mutation, B publishes an event to Kafka. C consumes it when it recovers.

---

### Q28 & Q29. Global Exception Handler & `@ExceptionHandler`
```java
@RestControllerAdvice
public class GlobalApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> 
            fieldErrors.put(err.getField(), err.getDefaultMessage()));
        problem.setProperty("invalidFields", fieldErrors);
        return ResponseEntity.badRequest().body(problem);
    }
}
```

---

### Q30 & Q33. How to get a fresh Prototype Bean from a Singleton Bean (`@Lookup`)?
- **Problem:** If a Singleton bean injects a Prototype bean via `@Autowired`, the Prototype bean is injected **only once** during startup. Subsequent calls will reuse the same instance!
- **Solution:** Use Spring's **`@Lookup`** method injection:
```java
@Component
public class OrderProcessingService {

    // Spring CGLIB overrides this method at runtime to fetch a new prototype bean from ApplicationContext
    @Lookup
    public OrderTokenGenerator getFreshTokenGenerator() {
        return null;
    }

    public void processOrder() {
        OrderTokenGenerator generator = getFreshTokenGenerator();
        generator.generate();
    }
}

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderTokenGenerator {
    // New instance created on every invocation of getFreshTokenGenerator()
}
```

---

### Q45. Where do you store JWT on the frontend and why?
- **Best Practice (Enterprise Security):**
  - **Access Token:** Stored in **Application Memory (JavaScript Signal / Variable)**. Immune to Cross-Site Scripting (XSS) persistent storage theft.
  - **Refresh Token:** Stored in an **`HttpOnly`, `Secure`, `SameSite=Strict` Cookie**. JavaScript cannot access it, rendering it immune to XSS theft!

---

### Q49. If I write two terminal methods on a Stream one by one, what will happen?
- **Result:** **`java.lang.IllegalStateException: stream has already been operated upon or closed`**.
- A Stream pipeline can only be consumed once. To perform multiple operations, create a new stream from the source collection.

---

## 🏢 Company: Team Computer (Pages 12 & 13)

### Q5. Write Java Code to Simulate and Reproduce a Deadlock
```java
public class DeadlockSimulation {
    private static final Object LockA = new Object();
    private static final Object LockB = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (LockA) {
                System.out.println("Thread 1: Holding Lock A...");
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                System.out.println("Thread 1: Waiting for Lock B...");
                synchronized (LockB) {
                    System.out.println("Thread 1: Acquired Lock B");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (LockB) {
                System.out.println("Thread 2: Holding Lock B...");
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                System.out.println("Thread 2: Waiting for Lock A...");
                synchronized (LockA) {
                    System.out.println("Thread 2: Acquired Lock A");
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
```

---

### Q8 & Q10. Sort Map by Values & Find Key from Value
```java
public class MapOperations {
    // Sort Map by Values
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }

    // Find Key for a given Value
    public static <K, V> Optional<K> findKeyByValue(Map<K, V> map, V value) {
        return map.entrySet().stream()
            .filter(entry -> Objects.equals(entry.getValue(), value))
            .map(Map.Entry::getKey)
            .findFirst();
    }
}
```

---

### Q15. StudentDto Stream Filter: Age > 18, City = Mumbai, Ascending by Grade
```java
public record StudentDto(String name, String email, String city, String grade, int age) {}

public class StudentFilterDemo {
    public static List<String> getEligibleStudentEmails(List<StudentDto> students) {
        return students.stream()
            .filter(s -> s.age() > 18)
            .filter(s -> "Mumbai".equalsIgnoreCase(s.city()))
            .sorted(Comparator.comparing(StudentDto::grade))
            .map(StudentDto::email)
            .toList();
    }
}
```

---

## 🏢 Company: Paramatrix & Bajaj (Page 13)

### Q1. Print Prime Numbers between 1 to 100
```java
public class PrimeNumbers {
    public static void main(String[] args) {
        List<Integer> primes = IntStream.rangeClosed(2, 100)
            .filter(PrimeNumbers::isPrime)
            .boxed()
            .toList();
        System.out.println("Primes: " + primes);
    }

    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        return IntStream.rangeClosed(2, (int) Math.sqrt(n))
            .noneMatch(i -> n % i == 0);
    }
}
```

---

### Q12. What is the 12-Factor App Methodology in Microservices?
1. **Codebase:** One codebase tracked in revision control, many deploys.
2. **Dependencies:** Explicitly declare and isolate dependencies (Maven/Gradle).
3. **Config:** Store configuration in the environment (Spring Cloud Config, env vars).
4. **Backing Services:** Treat backing services (databases, caches) as attached resources.
5. **Build, Release, Run:** Strictly separate build and run stages.
6. **Processes:** Execute the app as one or more stateless processes.
7. **Port Binding:** Export services via port binding (embedded Tomcat).
8. **Concurrency:** Scale out via the process model (Kubernetes replicas).
9. **Disposability:** Maximize robustness with fast startup and graceful shutdown.
10. **Dev/Prod Parity:** Keep development, staging, and production as similar as possible.
11. **Logs:** Treat logs as event streams (stdout $ightarrow$ Logstash $ightarrow$ Elasticsearch).
12. **Admin Processes:** Run administrative/management tasks as one-off processes.

---

## 🏢 Company: FinQuest & Team Computer (Page 14)

### Q5 & Q6. Maximum Sum Subarray having Length > 3 (Optimized Sliding Window)
```java
public class MaxSumSubarray {
    public static int findMaxSumLengthGreaterThanThree(int[] nums) {
        if (nums.length < 4) throw new IllegalArgumentException("Array length must be >= 4");
        
        int maxSum = Integer.MIN_VALUE;

        // Try every possible subarray length k from 4 to nums.length
        for (int k = 4; k <= nums.length; k++) {
            int windowSum = 0;
            for (int i = 0; i < k; i++) {
                windowSum += nums[i];
            }
            maxSum = Math.max(maxSum, windowSum);

            // Slide window of size k
            for (int i = k; i < nums.length; i++) {
                windowSum += nums[i] - nums[i - k];
                maxSum = Math.max(maxSum, windowSum);
            }
        }
        return maxSum;
    }
}
```

---

### Q7 & Q8. What comes under `@SpringBootApplication` and `@ComponentScan`?
- `@SpringBootApplication` is a composite annotation combining:
  1. `@SpringBootConfiguration`: Marks class as a source of bean definitions.
  2. `@EnableAutoConfiguration`: Scans classpath jars to auto-configure beans (HikariCP, Jackson, DispatcherServlet).
  3. `@ComponentScan`: Scans the current package and sub-packages for `@Component`, `@Service`, `@Repository`, `@Controller`.

---

### Q12 & Q13. Remove Duplicate Letters & Count Letter Frequencies via Stream
```java
public class StringStreamAnalysis {
    public static String removeDuplicateLetters(List<String> list) {
        return list.stream()
            .flatMap(str -> str.chars().mapToObj(c -> (char) c))
            .distinct()
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

    public static Map<Character, Long> countLetterFrequencies(List<String> list) {
        return list.stream()
            .flatMap(str -> str.chars().mapToObj(c -> (char) c))
            .filter(Character::isLetter)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
```

---

## 🏢 Company: Nimap Infotech (Page 15) — Real-World Project: AIGForgeHub (B2B RFQ Bidding Platform)

### Q1 to Q4. Project Overview: AIGForgeHub Platform
- **Domain:** A high-volume B2B e-procurement marketplace where industrial manufacturing buyers publish **Requests for Quotation (RFQs)**, and suppliers submit real-time competitive bids.
- **Key Modules & Microservices:**
  1. `RFQ-Service`: Allows enterprise buyers to create technical procurement tenders with CAD blueprints and spec sheets.
  2. `Bidding-Engine`: Low-latency bidding service where suppliers submit quotes. Handles race conditions and concurrent bid submissions.
  3. `Evaluation-Service`: Automatically evaluates supplier bids based on cost, delivery time, and vendor SLA score.
  4. `Notification-Service`: Notifies suppliers of outbid alerts via WebSockets and SMS.

---

### Q5. What is Eureka Server, its alternatives, and why use Eureka?
- **Eureka Server:** Service registry for client-side load balancing. Microservices register upon boot with their IP and port, sending heartbeats every 30 seconds.
- **Alternatives:** HashiCorp Consul, Apache Zookeeper, Kubernetes CoreDNS (native cloud service discovery).
- **Why Eureka?** Native deep integration with Spring Cloud LoadBalancer and OpenFeign; highly available AP (Availability/Partition tolerance) architecture where registry state is replicated peer-to-peer.

---

### Q9 & Q10. `@Autowired` Internal Working & Dependency Injection
- During application startup, `AutowiredAnnotationBeanPostProcessor` inspects all classes for `@Autowired` constructors, fields, and setter methods.
- Spring queries the `DefaultListableBeanFactory` to find a matching bean by **Type**.
- If multiple candidates exist, it evaluates `@Primary` or matches the field name with `@Qualifier("beanName")`.
- It uses reflection (`field.setAccessible(true)`) to inject the bean reference into the target object.

---

### Q12. Range Union Algorithm: `[8,10]` and `[1,12]`
- Given intervals `[[8,10], [1,12]]`, find their union/merge.
```java
public class IntervalUnion {
    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals.length <= 1) return intervals;
        
        // Sort by start times
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        List<int[]> merged = new ArrayList<>();
        int[] current = intervals[0];
        merged.add(current);

        for (int[] interval : intervals) {
            if (interval[0] <= current[1]) {
                // Overlapping: extend end time
                current[1] = Math.max(current[1], interval[1]);
            } else {
                current = interval;
                merged.add(current);
            }
        }
        return merged.toArray(new int[merged.size()][]);
    }
}
// Input: [[8,10], [1,12]] -> Output: [[1, 12]]
```

---

### Q15 to Q18. Hashing vs Encryption & BCrypt "Unhashing" Misconception
- **Encryption:** A **Two-Way** cryptographic function. Plaintext is encrypted with a key into ciphertext, and can be decrypted back to plaintext using the corresponding key (e.g. AES-256).
- **Hashing:** A **One-Way** mathematical digest function. Plaintext is converted into a fixed-length string (e.g. SHA-256, BCrypt). It **CANNOT be decrypted or unhashed**.
- **The BCrypt Login Check:** Spring Security **never unhashes** a password. During login, it hashes the raw input password using the *same salt* extracted from the stored hash and checks:
  `matches = constantTimeEquals(computedHash, storedHash)`.

---

### Q19. How does the browser know a JWT is expired?
- A JWT contains an `exp` (Unix epoch timestamp) claim in its payload.
- The Angular client decodes the base64 payload:
  `const isExpired = Date.now() >= JSON.parse(atob(jwt.split('.')[1])).exp * 1000;`
- If expired, Angular redirects to `/login` or triggers a silent refresh before making outbound HTTP calls.

---

## 🏢 Company: Axis Bank & Shreeji Avenue (Page 16)

### Q1. Database Sharding vs Partitioning
- **Partitioning (Vertical/Horizontal on Single Database):** Dividing a single database table into smaller physical partitions (e.g., partitioning `transactions` table by `year` or `hash(account_id)`) residing on the **same server engine**.
- **Sharding (Horizontal Partitioning across Multiple Servers):** Splitting data across **multiple distinct database server nodes**, each holding a subset of rows (e.g., Accounts 1–10M on DB Server A, 10M–20M on DB Server B). Sharding requires a routing key.

---

### Q10. What to do when a client is bombarding an API (DDoS / Brute Force)?
1. **API Gateway Rate Limiting:** Enforce Token Bucket rate limiting via Redis (`Bucket4j`) rejecting requests exceeding 100/min with `HTTP 429 Too Many Requests`.
2. **IP Whitelisting & WAF:** Configure Cloudflare / AWS WAF (Web Application Firewall) to identify IP anomalies and issue CAPTCHA challenges or drop traffic.
3. **API Key Quotas:** Require an `X-API-KEY` header with tiered consumption quotas.
4. **Exponential Backoff & Account Lockout:** Temporarily lock accounts after 5 failed authentication attempts.

---

## 🏢 Company: Transbnk & Simple Logic (Pages 17 & 18)

### Q1 to Q4. Storing Sensitive Documents (Aadhaar / PAN) in the Cloud
- **Architecture:**
  1. Never store raw documents on the database filesystem. Upload files to encrypted Object Storage (AWS S3 with SSE-KMS customer managed keys).
  2. Generate a non-deterministic UUID object key: `s3://secure-kyc-bucket/documents/{tenantId}/{uuid}.enc`.
  3. Compute a cryptographic **SHA-256 Checksum / HMAC** of the document upon upload and store it in PostgreSQL.
  4. **Tamper Detection (Page 18 Scenario):** If a rogue admin modifies the Aadhaar card directly on S3, when the reviewer downloads it, the backend computes the SHA-256 hash of the downloaded file and compares it with the immutable database record hash. If hashes mismatch, an integrity alert triggers and the file download is blocked!

---

### Q2. Java 17 Sealed Classes with Example
- Restricts which other classes or interfaces may extend or implement them using `sealed` and `permits`.
```java
public sealed interface LoanApplication permits HomeLoan, PersonalLoan, VehicleLoan {
    BigDecimal getPrincipal();
}

public final class HomeLoan implements LoanApplication {
    private final BigDecimal principal;
    public HomeLoan(BigDecimal principal) { this.principal = principal; }
    @Override public BigDecimal getPrincipal() { return principal; }
}

public final class PersonalLoan implements LoanApplication {
    private final BigDecimal principal;
    public PersonalLoan(BigDecimal principal) { this.principal = principal; }
    @Override public BigDecimal getPrincipal() { return principal; }
}

public final class VehicleLoan implements LoanApplication {
    private final BigDecimal principal;
    public VehicleLoan(BigDecimal principal) { this.principal = principal; }
    @Override public BigDecimal getPrincipal() { return principal; }
}
```

---

## 🏢 Company: Ameya Infovision Pvt Ltd. (Pages 18 & 19)

### Q1. Terminate both outer and inner loops without third variable when `j == 5`
- Use **Labeled Break**:
```java
public class LabeledBreakDemo {
    public static void main(String[] args) {
        outerLoop:
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (j == 5) {
                    System.out.println("Terminating both loops at i=" + i + ", j=" + j);
                    break outerLoop; // Exits both loops immediately!
                }
            }
        }
    }
}
```

---

### Q2. How to process millions of records using Stream without `OutOfMemoryError`?
1. **Never use `collection.stream()` on millions of pre-loaded objects:** Loading 5 million rows into a `List` exhausts heap RAM.
2. **Cursor-based JDBC / JPA Streaming:** Use `Stream<T>` returned directly by Spring Data JPA with `@Transactional(readOnly = true)`.
3. **Periodic Entity Detachment:** Call `entityManager.detach(entity)` every 500 rows to flush Hibernate 1st-level cache.
4. **Reactive Streams:** Use Project Reactor `Flux<T>` to stream rows asynchronously with backpressure.

---

### Q3. Difference between `map()` and `mapMulti()` (Java 16+)
- `map(Function<T, R>)`: Performs a strict **1-to-1** transformation.
- `flatMap(Function<T, Stream<R>>)`: Performs **1-to-N** transformation, but creates an intermediate `Stream` object for every element, adding GC allocation overhead.
- `mapMulti(BiConsumer<T, Consumer<R>>)`: Performs **1-to-N** or **1-to-0** transformation without creating intermediate Stream objects, resulting in higher throughput and zero object allocations.

---

### Q14. Schema Changes in Microservices Database (Zero Downtime)
- Apply the **Expand-Contract (Parallel Run) Pattern**:
  - **Expand:** Add new column `customer_full_name`. Do NOT delete old `first_name` and `last_name`.
  - **Dual-Write:** Deploy version 1.1 writing to both old and new columns.
  - **Backfill:** Run background job copying legacy data to new columns.
  - **Contract:** Deploy version 2.0 reading from new column only. Drop legacy columns in a subsequent release.

---

# Section 3: Pages 21 – 30

---

## 🏢 Company: Tansbank (Pages 21 & 22) — Angular & Concurrency Focus

### Q1. `HashMap` vs `ConcurrentHashMap`
| Feature | `HashMap` | `ConcurrentHashMap` |
|---|---|---|
| **Thread Safety** | Non-thread-safe | Fully thread-safe |
| **Locking Mechanism** | None | Lock-striping via synchronized nodes & CAS (Compare-And-Swap) |
| **Null Keys/Values** | Allows 1 null key, multiple null values | **No null keys or null values allowed!** |
| **Iterators** | Fail-Fast (`ConcurrentModificationException`) | Fail-Safe / Weakly Consistent |

---

### Q2 & Q3. During deployment 500 errors occur: What will you do?
1. **Inspect Ingress & Gateway:** Check if the API Gateway can route to newly spawned pods or if routes are misconfigured.
2. **Examine Container Logs:** Run `kubectl logs -f <pod-name> --tail=100 -n production`. Search for `BeanCreationException`, `FlywayException`, or `DatasourceConnectionException`.
3. **Inspect Pod Health Probes:** Check `kubectl describe pod <pod-name>`. Verify if Liveness or Readiness probes are failing.
4. **Immediate Recovery:** If root cause is a code bug, trigger instant rolling rollback to previous revision: `kubectl rollout undo deployment/order-service`.

---

### Q4. How to debug a slow query and optimize it?
1. **Enable Query Logging & Execution Plan:** Run `EXPLAIN ANALYZE SELECT ...` in PostgreSQL/MySQL. Look for **Sequential Scans (`Seq Scan`)** on large tables.
2. **Add Targeted Index:** Create B-Tree or Compound indexes on columns used in `WHERE`, `JOIN`, and `ORDER BY` clauses.
3. **Avoid `SELECT *`:** Select only the required columns to enable **Index-Only Scans**.
4. **Fix Cartesian Products:** Replace multi-collection `JOIN FETCH` with DTO constructor expressions or batch fetching (`hibernate.default_batch_fetch_size=50`).

---

### Q5 & Q6. Angular Component Lifecycle & `@ViewChild`
- **Component Lifecycle Sequence:**
  1. `ngOnChanges`: Triggered when `@Input` properties update.
  2. `ngOnInit`: Component initialized; best place for HTTP data fetching.
  3. `ngDoCheck`: Custom change detection checks.
  4. `ngAfterContentInit` & `ngAfterContentChecked`: Projected content initialized (`<ng-content>`).
  5. `ngAfterViewInit` & `ngAfterViewChecked`: Component's view and child views initialized.
  6. `ngOnDestroy`: Cleanup step; unsubscribe from RxJS observables to prevent memory leaks.
- **`@ViewChild`:** Injects a reference to a child component, DOM element (`ElementRef`), or directive into the parent class:
```typescript
@Component({
  template: `<input #searchBox type="text" />`
})
export class SearchComponent implements AfterViewInit {
  @ViewChild('searchBox') searchInput!: ElementRef<HTMLInputElement>;

  ngAfterViewInit(): void {
    this.searchInput.nativeElement.focus();
  }
}
```

---

### Q7 & Q8. Component Communication in Angular
1. **Parent-to-Child:** `@Input()` property binding.
2. **Child-to-Parent:** `@Output()` with `EventEmitter`.
3. **Unrelated / Sibling Components:** **Shared Injectable Service** using RxJS `BehaviorSubject` or Angular Signals (`signal<T>()`).

---

### Q9 & Q10. Ahead-Of-Time (AOT) Compilation & Angular Optimization
- **AOT Compilation:** Compiles Angular HTML templates and TypeScript code into clean JavaScript during the build process (`ng build`), rather than in the browser at runtime (JIT).
- **Advantages:** Faster initial rendering, smaller bundle size, template error detection at build time, enhanced security (mitigates template injection).
- **Angular Performance Tuning:**
  1. ChangeDetectionStrategy: `OnPush`.
  2. Native Angular Signals (`signal()`, `computed()`).
  3. Image optimization via `NgOptimizedImage`.
  4. Deferrable views (`@defer (on viewport)`).
  5. CDK Virtual Scrolling.

---

### Q11 & Q12. Debounce API Call & Angular `HttpInterceptorFn`
```typescript
// 1. Debounce API call using RxJS
searchControl.valueChanges.pipe(
  debounceTime(300),
  distinctUntilChanged(),
  switchMap(query => this.searchService.search(query))
).subscribe(results => this.results.set(results));

// 2. Modern Functional HttpInterceptor
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).getAccessToken();
  return next(token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req);
};
```

---

## 🏢 Company: Paycraft Solutions (Page 23)

### Q2 & Q3. Can `private` or `final` methods be Overloaded or Overridden?
- **Overloading:** **YES**, both `private` and `final` methods can be **overloaded** within the same class, as long as parameter signatures differ.
- **Overriding:**
  - `private` methods **CANNOT be overridden**. Subclasses cannot even see private methods; defining a method with the same name in a child class is a completely independent new method!
  - `final` methods **CANNOT be overridden**. The compiler enforces immutability of the implementation with an error.

---

### Q4. Can Constructors be Overloaded and Overridden?
- **Constructor Overloading:** **YES** (e.g. `public Employee()`, `public Employee(String name)`).
- **Constructor Overriding:** **NO**. Constructors belong exclusively to the class in which they are declared and cannot be inherited or overridden by child classes.

---

### Q8 & Q9. Student Stream Calculations: Average Marks & Grouping by Buckets
```java
public record Student(String name, int marks) {}

public class StudentStreamAnalytics {
    // 1. Find Average Marks
    public static double calculateAverage(List<Student> students) {
        return students.stream()
            .mapToInt(Student::marks)
            .average()
            .orElse(0.0);
    }

    // 2. Group into buckets: 10-20, 20-30, ... 90-100
    public static Map<String, List<String>> groupStudentsByScoreRanges(List<Student> students) {
        return students.stream()
            .collect(Collectors.groupingBy(
                s -> {
                    int lower = (s.marks() / 10) * 10;
                    int upper = lower + 10;
                    return lower + "-" + upper;
                },
                Collectors.mapping(Student::name, Collectors.toList())
            ));
    }
}
```

---

## 🏢 Company: Team Computers (Page 24 & 25)

### Q7. Multi-Operation Transactions: Commit previous operations when a step throws an exception (`REQUIRES_NEW`)
- **Problem:** Step 1 saves Order, Step 2 charges Card, Step 3 calls external Loyalty API. If Step 3 throws an exception, we want Step 1 and Step 2 to remain committed!
- **Solution:** Wrap Step 3 in a separate service method annotated with `@Transactional(propagation = Propagation.REQUIRES_NEW)` and handle the exception in the caller:
```java
@Service
public class OrderService {
    @Autowired private AuditLogService auditLogService;

    @Transactional
    public void processOrder(Order order) {
        orderRepository.save(order);
        try {
            // Suspends existing transaction, runs in an isolated independent transaction!
            auditLogService.recordLoyaltyAsync(order);
        } catch (Exception ex) {
            log.warn("Loyalty logging failed, but order remains committed: {}", ex.getMessage());
        }
    }
}
```

---

### Q17 & Q18. Stream Filter: IT Department, Salary > 25,000, Sort by Name Descending
```java
public record Employee(Long id, String name, String dept, double salary, String location) {}

public class EmployeeStreamQuery {
    public static List<String> getHighEarningItEmployees(List<Employee> employees) {
        return employees.stream()
            .filter(e -> "IT".equalsIgnoreCase(e.dept()))
            .filter(e -> e.salary() > 25000.0)
            .sorted(Comparator.comparing(Employee::name).reversed())
            .map(Employee::name) // Transforms stream from Employee object to String name
            .toList();
    }
}
```

---

### Q25. Why is `ArrayList` fast?
1. **Contiguous Memory Allocation:** Array elements are placed sequentially in RAM, maximizing **CPU Cache Line Locality** (L1/L2 cache prefetching).
2. **$O(1)$ Random Access:** Locating an element at index $i$ requires a trivial pointer calculation: `base_address + i * element_size`.
3. In contrast, `LinkedList` scatters nodes across the heap with pointer references, causing frequent CPU cache misses.

---

## 🏢 Company: C2L BIZ Solutions Pvt Ltd. (Page 25)

### Q5 to Q7. Employee Class Salary Summation (No Static Methods)
```java
public class Employee {
    private String name;
    private double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public double getSalary() { return salary; }

    // Instance method computing total salaries of a list
    public double computeTotalSalary(List<Employee> employees) {
        return employees.stream()
            .mapToDouble(Employee::getSalary)
            .sum();
    }

    public static void main(String[] args) {
        Employee e1 = new Employee("Alice", 60000);
        Employee e2 = new Employee("Bob", 75000);
        Employee e3 = new Employee("Charlie", 90000);

        List<Employee> list = List.of(e1, e2, e3);

        // Invoked using an instance since method is non-static
        double total = e1.computeTotalSalary(list);
        System.out.println("Total Salary: " + total); // 225000.0
    }
}
```

---

### Q17. Delete entire table data without disturbing table structure
- **Answer:** `TRUNCATE TABLE employees;`
- **Why TRUNCATE over DELETE?**
  - `TRUNCATE` deallocates data pages in the database engine instead of scanning and removing rows one by one.
  - Generates minimal transaction logs (DDL operation), resets identity sequences, and executes orders of magnitude faster than `DELETE FROM table;`.

---

## 🏢 Company: rebit / Rebit(RBI) (Pages 26 & 27)

### Q. Managing Multiple Roles in Loan Origination System (LOS)
- **Role Hierarchy Implementation:**
  - `ROLE_SUPER_ADMIN` > `ROLE_BRANCH_MANAGER` > `ROLE_UNDERWRITER` > `ROLE_LOAN_OFFICER` > `ROLE_CUSTOMER`.
- Enforce role validation using `@PreAuthorize("hasRole('UNDERWRITER')")` on sanctioning endpoints, and propagate user roles across microservices using Feign `RequestInterceptor`.

---

### Q. JavaScript Console Log Variable Scope Puzzle (Page 27)
```javascript
var x = 2;
var y = x;
x = 5;
console.log(y);
```
- **Output:** `2`
- **Reason:** In JavaScript, primitive types (number, string, boolean) are assigned and copied **by value**. Modifying `x` to `5` does not mutate `y`.

---

## 🏢 Company: TransBnk (Page 28) — REST API Architecture & Validation

### Q1 to Q4. Designing an Endpoint with Field Constraints & Uniqueness Checks
- **Requirements:** `firstName` (mandatory, max 50), `age` (optional numerical < 60), `username` (mandatory, must be unique in DB).
```java
// 1. DTO Record with Hibernate Validator constraints
public record CreateUserRequest(
    @NotBlank(message = "First name is mandatory")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    String firstName,

    @Min(value = 18, message = "Minimum age is 18")
    @Max(value = 59, message = "Age must be less than 60")
    Integer age, // Wrapped Integer allows null if skipped!

    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "Username must be alphanumeric (4-20 chars)")
    String username,

    @NotBlank(message = "Email is mandatory")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    String email
) {}
```
- **Uniqueness Check Implementation:**
  - Annotations alone cannot check database uniqueness without costly custom validator queries.
  - **Best Practice:** Place a `UNIQUE` constraint in the database table (`idx_users_username`), query `userRepository.existsByUsername(username)` in the Service layer, and catch `DataIntegrityViolationException` to return an RFC 7807 `409 Conflict` error.

---

## 🏢 Company: NeoSoft & Cbt Infotech (REBIT) (Pages 28 & 29)

### Q1. Convert Integer to Binary String without Predefined Methods (NeoSoft)
```java
public class IntegerToBinary {
    public static String toBinary(int n) {
        if (n == 0) return "0";
        StringBuilder sb = new StringBuilder();
        int num = n;
        while (num > 0) {
            sb.append(num % 2);
            num /= 2;
        }
        return sb.reverse().toString();
    }
}
// Input: 8 -> Output: 1000
```

---

### Q2 & Q3. HashMap Return Value Puzzle with Hardcoded `hashCode() = 1` (Cbt Infotech)
- **Question Scenario:** `EMP1` (Dhanshree) and `EMP2` (Dhiraj). We `map.put("E1", EMP1)` and `map.put("E2", EMP2)`. Then we modify the `Employee` class so `hashCode()` returns hardcoded `1`. What will `map.get("E1")` return?
- **Answer:** It will return **`EMP1` (Dhanshree)**!
- **Deep Technical Reason:**
  - In `map.put("E1", EMP1)` and `map.get("E1")`, the key is **`"E1"` (a `String`)**, NOT the `Employee` object!
  - `String.hashCode()` was NOT modified! `"E1"` and `"E2"` hash to different bucket indices.
  - Even if the `Employee` object were the key with hardcoded `hashCode() = 1`, both keys would map to the same bucket linked list, but `equals()` would distinguish `"E1"` from `"E2"`, still retrieving the correct corresponding value!

---

### Q4. Find the First Duplicate Word in a Sentence
```java
public class FirstDuplicateWord {
    public static Optional<String> findFirstDuplicate(String sentence) {
        String[] words = sentence.toLowerCase().split("\\s+");
        Set<String> seen = new HashSet<>();
        for (String word : words) {
            if (!seen.add(word)) { // add returns false if element was already present!
                return Optional.of(word);
            }
        }
        return Optional.empty();
    }
}
```

---

### Q5. Gender-wise & Designation-wise Employee Count via Stream
```java
public record EmployeeRecord(String id, String name, String gender, String designation) {}

public class EmployeeAggregation {
    public static Map<String, Map<String, Long>> getGenderDesignationCount(List<EmployeeRecord> list) {
        return list.stream()
            .collect(Collectors.groupingBy(
                EmployeeRecord::designation,
                Collectors.groupingBy(
                    EmployeeRecord::gender,
                    Collectors.counting()
                )
            ));
    }
}
// Output: { "SQL Developer": { "Female": 2, "Male": 1 }, "Java Developer": { "Male": 3 } }
```

---

# Section 4: Pages 31 – 40

---

## 🏢 Company: In Solutions Global & Servosys Solutions (Pages 31 & 32)

### Q12 & Q13. Microservice Database Independence vs Inter-Service Queries
- **Can two microservices share one database?**
  - Technically possible, but an **Architectural Anti-Pattern**. Sharing a database couples schemas, enables cross-boundary table locks, and prevents independent deployments.
- **How does User Service get Department Count from Department Service's DB?**
  - **Option 1 (Synchronous RPC via OpenFeign):** `departmentClient.getDepartmentCount()`. Fast, clean, cached in Redis.
  - **Option 2 (Asynchronous Event Sourcing via Kafka):** Whenever Department Service creates or deletes a department, it publishes `DepartmentChangedEvent`. User Service consumes and updates a local read-model counter.

---

### Q21 & Q22. Reverse words in a string: "hello world" $ightarrow$ "olleh dlrow"
```java
public class WordReversal {
    // Approach 1: Using StringBuilder.reverse()
    public static String reverseWordsBuiltIn(String str) {
        return Arrays.stream(str.split(" "))
            .map(word -> new StringBuilder(word).reverse().toString())
            .collect(Collectors.joining(" "));
    }

    // Approach 2: Without built-in reverse methods (Two-Pointer In-Place)
    public static String reverseWordsManual(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();

        for (int w = 0; w < words.length; w++) {
            char[] chars = words[w].toCharArray();
            int left = 0, right = chars.length - 1;
            while (left < right) {
                char temp = chars[left];
                chars[left] = chars[right];
                chars[right] = temp;
                left++;
                right--;
            }
            result.append(new String(chars));
            if (w < words.length - 1) result.append(" ");
        }
        return result.toString();
    }
}
```

---

### Q27 & Q28. Thread-Safe Singleton Implementation
```java
// Production Standard: Double-Checked Locking Singleton
public class DatabaseConnectionPool {
    private static volatile DatabaseConnectionPool instance;

    private DatabaseConnectionPool() {
        if (instance != null) {
            throw new IllegalStateException("Reflection attack prevented!");
        }
    }

    public static DatabaseConnectionPool getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionPool.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionPool();
                }
            }
        }
        return instance;
    }
}
```

---

### Q. Servosys Map Size Code Puzzle (Page 32)
```java
Map<String, String> map = new HashMap<>();
map.put(null, null);
map.put(null, null);
map.put(null, null);
System.out.println(map.size());
```
- **Output:** `1`
- **Reason:** `HashMap` allows exactly **one `null` key**. Subsequent calls to `map.put(null, ...)` overwrite the existing null-key entry in bucket 0, so the map size remains 1.

---

## 🏢 Company: Smart e & Servosys (Pages 33 & 34)

### Q. String vs StringBuilder Code Puzzle (Page 33)
```java
String s1 = "hello";
StringBuilder s2 = new StringBuilder("hello");
System.out.println(s1 == s2);
System.out.println(s1.equals(s2));
```
- **Line 3 (`s1 == s2`):** **Compile-Time Error!** `Incomparable types: java.lang.String and java.lang.StringBuilder`.
- **Line 4 (`s1.equals(s2)`):** Evaluates to **`false`**. `String.equals()` first checks `if (anObject instanceof String)`. Since `StringBuilder` is not a `String`, it returns `false` without checking character contents.

---

### Q. String Comparison Output Puzzle with Nulls (Page 33)
```java
String s1 = null; 
String s2 = "sample"; 
String s3 = new String("sample"); 

System.out.println(s1 == s2);        // false (null != reference)
System.out.println(s2 == s3);        // false (SCP literal != heap object)
System.out.println(s1.equals(s3));   // Throws java.lang.NullPointerException!
```

---

### Q. Count Rows for each `department_id` without WHERE clause (Page 33)
```sql
-- NULL department_id should NOT be counted, but NO WHERE clause allowed!
SELECT department_id, COUNT(*) AS employee_count
FROM employees
GROUP BY department_id
HAVING department_id IS NOT NULL;
```

---

### Q. Servosys Name Character Frequency Puzzle (Page 34)
```java
public class CharacterFrequency {
    public static void printCharFrequency(String name) {
        // Ignores spaces, case-insensitive
        name.toLowerCase().chars()
            .mapToObj(c -> (char) c)
            .filter(c -> c != ' ')
            .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
            .forEach((k, v) -> System.out.println(k + " = " + v));
    }
}
// Time Complexity: O(N) where N is length of string
// Space Complexity: O(K) where K is number of unique characters (max 26 for English alphabet)
```

---

## 🏢 Company: Bajaj Finserv & C2L (Pages 34 & 35)

### Q1 & Q2. Word Frequency Preserving Insertion Order (Bajaj Finserv)
- **Input:** `"India is my country, I love my country"`
- **Expected Output:** `India=1, is=1, my=2, country=2, I=1, love=1`
```java
public class WordOrderFrequency {
    public static void countWordsPreservingOrder(String statement) {
        String[] words = statement.replaceAll("[^a-zA-Z\\s]", "").split("\\s+");

        Map<String, Long> wordCount = Arrays.stream(words)
            .collect(Collectors.groupingBy(
                Function.identity(),
                LinkedHashMap::new, // Preserves original first-seen appearance order!
                Collectors.counting()
            ));

        wordCount.forEach((word, count) -> System.out.println(word + " equal to " + count));
    }
}
```

---

### Q14 & Q15. Relational Database Design: Employee & Multiple Addresses (C2L)
```sql
-- 1. Employees Table
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    date_of_joining DATE NOT NULL
);

-- 2. Addresses Table (One-to-Many Relationship)
CREATE TABLE addresses (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    address_line VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    address_type VARCHAR(20) NOT NULL -- 'HOME', 'OFFICE', 'PERMANENT'
);

-- 3. Query: Employee ID, Name, and Home Address details
SELECT e.id, e.name, a.address_line, a.city, a.postal_code
FROM employees e
INNER JOIN addresses a ON e.id = a.employee_id
WHERE a.address_type = 'HOME';
```

---

## 🏢 Company: NCS Soft Solutions (Page 38)

### Q1. `WHERE` clause vs `HAVING` clause
- **`WHERE`:** Filters individual rows **before** grouping and aggregations. Cannot contain aggregate functions (`WHERE salary > 50000`).
- **`HAVING`:** Filters grouped summary rows **after** `GROUP BY`. Operates on aggregate functions (`HAVING COUNT(*) > 5`).

---

### Q6. `ClassNotFoundException` vs `NoClassDefFoundError`
- **`ClassNotFoundException` (Checked Exception):** Occurs at runtime when an application attempts to load a class by string name (e.g. `Class.forName("com.mysql.cj.jdbc.Driver")`) but the `.class` file is not on the classpath.
- **`NoClassDefFoundError` (Fatal JVM Error):** Occurs when a class was **present at compile time**, but at runtime the classloader fails to find it or its static initializer failed with an exception.

---

### Q24 & Q25. `DELETE` vs `TRUNCATE` vs `DROP`
| Feature | `DELETE` | `TRUNCATE` | `DROP` |
|---|---|---|---|
| **Type** | DML (Data Manipulation) | DDL (Data Definition) | DDL (Data Definition) |
| **Rollback** | Yes (logs row deletions) | Fast rollback or non-rollback depending on DB | Cannot be rolled back |
| **Speed** | Slow (row-by-row scanning) | Extremely Fast (deallocates pages) | Instant |
| **Table Structure**| Retains table structure | Retains table structure | **Destroys entire table & schema!** |
| **WHERE Clause** | Yes (`WHERE id = 5`) | No (all rows truncated) | N/A |

---

## 🏢 Company: NeoSoft (Pages 40 & 41) — Banking Reactive Form Dynamic Validation & JPA Entity States

### Q8. Complex Banking Form: Dynamic Regex Auto-Validation in Angular (Aadhaar, PAN, Passport)
- **Scenario:** The user selects a document type from a dropdown. Depending on the selection, the input field must automatically enforce the corresponding strict regex and reject invalid characters as the user types.
```typescript
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-kyc-document-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <form [formGroup]="kycForm" class="kyc-form">
      <label>Select Document Type:</label>
      <select formControlName="documentType">
        <option value="PAN">PAN Card</option>
        <option value="AADHAAR">Aadhaar Card</option>
        <option value="PASSPORT">Passport</option>
      </select>

      <label>Document Number:</label>
      <input type="text" formControlName="documentNumber" [placeholder]="placeholderText" />

      @if (docNumberControl.touched && docNumberControl.invalid) {
        <span class="error-msg">{{ errorMessage }}</span>
      }
    </form>
  `
})
export class KycDocumentFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);

  readonly kycForm = this.fb.group({
    documentType: ['PAN', Validators.required],
    documentNumber: ['', [Validators.required, Validators.pattern(/^[A-Z]{5}[0-9]{4}[A-Z]{1}$/)]]
  });

  get docNumberControl() { return this.kycForm.controls.documentNumber; }
  get placeholderText() {
    const type = this.kycForm.controls.documentType.value;
    if (type === 'PAN') return 'ABCDE1234F';
    if (type === 'AADHAAR') return '12-digit Aadhaar number';
    return 'Passport number';
  }
  get errorMessage() {
    const type = this.kycForm.controls.documentType.value;
    if (type === 'PAN') return 'Invalid PAN: Must be 5 letters, 4 digits, 1 letter.';
    if (type === 'AADHAAR') return 'Invalid Aadhaar: Must be exactly 12 digits.';
    return 'Invalid Passport number format.';
  }

  ngOnInit(): void {
    // Dynamically swap validators when document type changes!
    this.kycForm.controls.documentType.valueChanges.subscribe(type => {
      this.docNumberControl.clearValidators();
      this.docNumberControl.reset();

      if (type === 'PAN') {
        this.docNumberControl.setValidators([Validators.required, Validators.pattern(/^[A-Z]{5}[0-9]{4}[A-Z]{1}$/)]);
      } else if (type === 'AADHAAR') {
        this.docNumberControl.setValidators([Validators.required, Validators.pattern(/^[2-9]{1}[0-9]{11}$/)]);
      } else if (type === 'PASSPORT') {
        this.docNumberControl.setValidators([Validators.required, Validators.pattern(/^[A-PR-WYa-pr-wy][1-9]\d\s?\d{4}[1-9]$/)]);
      }
      this.docNumberControl.updateValueAndValidity();
    });
  }
}
```

---

### Q1. The 4 JPA Entity States
1. **Transient (New):** Object instantiated via `new Customer()`. Not associated with any Hibernate Session; has no database representation.
2. **Managed (Persistent):** Associated with an active Hibernate Session (via `persist()`, `find()`). Any changes to fields are automatically detected via dirty-checking and written to DB on transaction commit!
3. **Detached:** Was once managed, but the Hibernate Session closed or `entityManager.detach()` was called. Changes will not be written to DB unless `merge()` is invoked.
4. **Removed:** Marked for deletion via `entityManager.remove()`. A SQL `DELETE` executes on commit.

---

### Q5. Circular Dependency in Spring Boot: How to Prevent & Resolve?
- **Root Cause:** Bean A injects Bean B, and Bean B injects Bean A (A $ightarrow$ B $ightarrow$ A). Spring's ApplicationContext fails to construct either bean.
- **How to Prevent/Fix:**
  1. **Refactor Code (Best Practice):** Extract common business logic into a third service (Bean C) that both A and B inject.
  2. **Use `@Lazy` Injection:** Annotate one injection point with `@Lazy` so Spring creates a dynamic CGLIB proxy instead of eagerly loading the circular bean.
  3. **Method-level Injection:** Pass the dependency as a method parameter instead of a class-level constructor argument.

---

# Section 5: Pages 41 – 50

---

## 🏢 Company: Servosys & In Solutions (Pages 41 & 42)

### Q. Handling Long-Running Operations in a REST API
- **Problem:** A client requests an asynchronous credit risk report that takes 45 seconds to generate. Holding an open HTTP socket causes gateway timeouts (504 Gateway Timeout).
- **Production Solution (Asynchronous Polling / Webhook Pattern):**
  1. Client sends `POST /api/v1/credit-reports`.
  2. The server generates a unique `jobId`, queues the job in Kafka or an asynchronous thread pool, and **immediately returns `HTTP 202 Accepted`** with header `Location: /api/v1/credit-reports/jobs/JOB-9824`.
  3. Client polls `GET /api/v1/credit-reports/jobs/JOB-9824` (or receives a real-time event via Server-Sent Events / WebSockets) until status changes from `"PROCESSING"` to `"COMPLETED"`.

---

### Q. Database Queries: JOIN vs Subquery — Which is Preferred?
- **Production Standard:** **`JOIN` is generally preferred** over correlated subqueries.
- **Why?**
  - Modern query planners (Postgres, Oracle, MySQL) optimize `JOIN` operations using Hash Joins or Merge Joins.
  - Correlated subqueries can execute in $O(M 	imes N)$ time because the inner query runs once for every row of the outer query.
  - However, when checking for existence without returning columns, `WHERE EXISTS (SELECT 1 ...)` is faster than a `LEFT JOIN ... WHERE right.id IS NOT NULL` because `EXISTS` short-circuits on the first match.

---

### Q. Microservice Choreography vs Orchestration
| Dimension | Choreography | Orchestration |
|---|---|---|
| **Coordination** | Decentralized (Event-Driven via Kafka) | Centralized (Saga Orchestrator State Machine) |
| **Coupling** | Loosely coupled; services react to domain events | Tightly coupled to the orchestrator service |
| **Visibility** | Difficult to trace complete workflow state | Single dashboard visibility into workflow execution |
| **Best For** | Simple workflows (2-4 steps) | Complex multi-step business transactions (Sagas) |

---

## 🏢 Company: Qtech Software Solution & In Solution Global (Pages 43 & 44)

### Q10. String Compression Puzzle (Qtech)
- **Problem:** Given `Input S = "aabbccdd"`, compress character runs into `Output = "a2b2c2d2"`.
```java
public class StringRunLengthEncoder {
    public static String compress(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        int count = 1;

        for (int i = 0; i < s.length(); i++) {
            if (i + 1 < s.length() && s.charAt(i) == s.charAt(i + 1)) {
                count++;
            } else {
                sb.append(s.charAt(i)).append(count);
                count = 1;
            }
        }
        return sb.toString();
    }
}
// Input: "aabbccdd" -> Output: "a2b2c2d2"
```

---

### Q5. What happens internally when `RestTemplate` makes an HTTP call?
1. `RestTemplate` delegates to `ClientHttpRequestFactory` (e.g. `HttpComponentsClientHttpRequestFactory` using Apache HttpClient).
2. It iterates through its registered `HttpMessageConverter` list (e.g. `MappingJackson2HttpMessageConverter`) to serialize the Java request body into JSON.
3. Opens an HTTP socket connection to the target URI.
4. Reads the response status code:
   - If status $\ge 400$, it invokes `ResponseErrorHandler`, which throws `HttpClientErrorException` or `HttpServerErrorException`.
   - If status is $200$, it uses message converters to deserialize JSON back into the target Java DTO.

---

### Q17. JAR vs WAR: Which one do you use and why?
- **Modern Choice: Embedded JAR (`.jar`).**
- **Why?**
  - Cloud-native standard: Ships with an embedded Tomcat servlet engine.
  - Runs with a simple command: `java -jar application.jar`.
  - Simplifies Docker containerization and Kubernetes scaling (no external Tomcat cluster management required).
  - WAR is only required when deploying multiple legacy applications to a shared standalone WebLogic, JBoss, or external Tomcat server.

---

## 🏢 Company: NuSummit & Servosys (Pages 46 & 47)

### Q13. Shift all Zeros to the Right Side of an Array in $O(N)$
- **Input:** `[1, 0, 5, 7, 0, 6, 4, 0, 2, 0, 8, 0]`
- **Output:** `[1, 5, 7, 6, 4, 2, 8, 0, 0, 0, 0, 0]`
```java
public class ShiftZerosRight {
    public static void shiftZeros(int[] nums) {
        int nonZeroIndex = 0;

        // Step 1: Move all non-zero elements forward
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[nonZeroIndex++] = nums[i];
            }
        }

        // Step 2: Fill remaining indices with zeros
        while (nonZeroIndex < nums.length) {
            nums[nonZeroIndex++] = 0;
        }
    }
}
```

---

### Q. What is SQL Injection and how to prevent it in REST APIs?
- **Exploit:** An attacker inputs malicious SQL payloads (e.g. `' OR '1'='1`) into form inputs to manipulate queries and bypass authentication or dump database tables.
- **Prevention:**
  1. **Parameterized Queries / PreparedStatements:** Always use `PreparedStatement` or Spring Data JPA repositories with bind parameters (`:email`).
  2. **Hibernate / JPA:** ORM uses bind parameters by default, mitigating raw SQL concatenation.
  3. **Input Validation:** Use `@Pattern` regex validation to reject special SQL characters before processing.

---

## 🏢 Company: V2STech & NuSummit (Pages 48 & 49)

### Q. Heap Memory vs Stack Memory in terms of Threads
- **Stack Memory:**
  - Allocated per thread (private to each thread).
  - Stores local primitive variables, method execution frames, and reference pointers to heap objects.
  - Automatically deallocated when the method frame finishes.
  - Size configured via `-Xss` (default ~1MB per thread). Fast access.
- **Heap Memory:**
  - Shared across **all threads** in the JVM.
  - Stores all instantiated objects, JRE classes, and arrays.
  - Managed by the Garbage Collector (GC). Size configured via `-Xms` and `-Xmx`.
  - Requires synchronization when multiple threads access shared mutable heap objects!

---

### Q20 & Q21. Java Record Classes vs Project Lombok
| Feature | Java Records (`record`) | Project Lombok (`@Data`) |
|---|---|---|
| **Language Support** | Official Java language keyword (Java 16+) | Third-party compiler annotation processor |
| **Immutability** | Strictly immutable (all fields `private final`) | Can be mutable or immutable (`@Value`) |
| **Inheritance** | Extends `java.lang.Record`; cannot extend classes | Normal class inheritance |
| **Reflection / Serialization** | Native JVM security; bypasses reflection mutation | Standard bytecode modification |

---

# Section 6: Pages 51 – 57

---

## 🏢 Company: Mirae Asset (Page 52) — API Rate Limiting Architecture

### Q3 to Q5. Rate Limiting Architecture & Implementation at API Gateway
- **Enterprise Solution (Token Bucket Algorithm via Redis & Bucket4j):**
```
┌──────────────────────── Token Bucket Rate Limiter ────────────────────────┐
│                                                                           │
│   Incoming Client Request (IP: 192.168.1.50)                              │
│                 │                                                         │
│                 ▼                                                         │
│   [ Spring Cloud Gateway GlobalFilter ]                                   │
│   Redis Key: "rate:ip:192.168.1.50" (Capacity: 100, Refill: 100/min)     │
│                 │                                                         │
│        ┌────────┴────────────────────────┐                                │
│        ▼ Token Available                 ▼ Bucket Exhausted (0 Tokens)    │
│   Consume 1 Token                        Reject immediately!              │
│   Forward to Downstream Microservice     Return HTTP 429 Too Many Requests│
│                                          Header: Retry-After: 30          │
│                                                                           │
└───────────────────────────────────────────────────────────────────────────┘
```
```java
@Component
public class RedisRateLimitingFilter implements GlobalFilter, Ordered {

    private final StringRedisTemplate redisTemplate;

    public RedisRateLimitingFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        String redisKey = "rate_limit:" + clientIp;

        Long currentCount = redisTemplate.opsForValue().increment(redisKey);
        if (currentCount == 1) {
            redisTemplate.expire(redisKey, Duration.ofMinutes(1));
        }

        if (currentCount > 100) { // Max 100 requests per minute
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().add("Retry-After", "60");
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() { return -200; }
}
```

---

### Q8 & Q9. Why `CompletableFuture` if Multithreading already existed?
- Prior to Java 8, `Future` provided asynchronous results, but calling `future.get()` was **blocking**!
- `CompletableFuture`:
  1. Enables **Non-Blocking Reactive Composition**: Chaining asynchronous stages via `.thenApply()`, `.thenCompose()`, `.thenAccept()`.
  2. Combines multiple futures in parallel: `CompletableFuture.allOf(f1, f2, f3).join()`.
  3. Built-in exception fallback handling: `.exceptionally(fallbackFn)`.

---

## 🏢 Company: Crisil (Pages 53 & 55)

### Q1. Count City Names Having the Same Number of Characters
- **Input:** `["Delhi", "Shanghai", "Dhaka", "SaoPaulo", "MexicoCity", "Beijing", "Mumbai", "Osaka"]`
- **Output:** Count of cities grouped by string length.
```java
public class CityNameLengthAnalysis {
    public static void countCitiesByLength(List<String> cities) {
        Map<Integer, Long> lengthCountMap = cities.stream()
            .collect(Collectors.groupingBy(
                String::length,
                TreeMap::new, // Sorted by word length
                Collectors.counting()
            ));

        lengthCountMap.forEach((len, count) -> 
            System.out.println("Length " + len + " characters: " + count + " cities"));
    }

    public static void main(String[] args) {
        List<String> cities = List.of(
            "Delhi", "Shanghai", "Dhaka", "SaoPaulo", "MexicoCity", "Beijing", "Mumbai", "Osaka"
        );
        countCitiesByLength(cities);
    }
}
// Length 5 characters: 3 cities (Delhi, Dhaka, Osaka)
// Length 6 characters: 1 city (Mumbai)
// Length 7 characters: 1 city (Beijing)
// Length 8 characters: 2 cities (Shanghai, SaoPaulo)
// Length 10 characters: 1 city (MexicoCity)
```

---

## 🏢 Company: Servosys (Page 54)

### Q6. Find Longest Consecutive Increasing Subarray with Index
- **Input:** `arr = {1, 5, 6, 7, 9, 10, 11, 12, 13}`
- **Output:** `9, 10, 11, 12, 13` (Length 5, starting at index 4)
```java
public class LongestConsecutiveSubarray {
    public static int[] findLongestConsecutiveIncreasing(int[] arr) {
        if (arr.length == 0) return new int[0];

        int maxLen = 1, currentLen = 1;
        int maxStart = 0, currentStart = 0;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1] + 1) { // Consecutive increasing: differs by +1
                currentLen++;
                if (currentLen > maxLen) {
                    maxLen = currentLen;
                    maxStart = currentStart;
                }
            } else {
                currentLen = 1;
                currentStart = i;
            }
        }

        return Arrays.copyOfRange(arr, maxStart, maxStart + maxLen);
    }
}
```

---

## 🏢 Company: V2STech Solutions (Page 54)

### Q. Rightmost Digits Algorithm (`rightDigit`)
- **Test Cases:**
  - `rightDigit([1, 22, 93])` $ightarrow$ `[1, 2, 3]`
  - `rightDigit([16, 8, 886, 8, 1])` $ightarrow$ `[6, 8, 6, 8, 1]`
  - `rightDigit([10, 0])` $ightarrow$ `[0, 0]`
```java
public class RightDigitExtractor {
    public static List<Integer> rightDigit(List<Integer> nums) {
        return nums.stream()
            .map(n -> n % 10)
            .toList();
    }
}
```

---

## 🏢 Company: Applied Cloud Computing (F2F) (Pages 55 & 56)

### Q11 & Q12. API takes 2ms locally but takes 2 seconds in production: What could be the problem?
1. **Network Latency & Round Trips:** Microservice is in AWS us-east-1, while database is in on-premise datacenter or different VPC region without VPC Peering.
2. **Database Connection Pool Starvation:** In dev, 1 user connects. In prod, 500 concurrent threads exhaust HikariCP connections (`connectionTimeout=30000ms`), causing requests to wait in line.
3. **Missing Indexes on Large Datasets:** Dev database has 100 rows (sequential scan takes 1ms); Prod has 20 million rows (sequential scan takes 2.5 seconds!).
4. **Third-Party API Sync Calls:** Synchronously calling external credit bureaus or SMS gateways without timeouts.

---

### Q15. Designing Pagination for 10 Million Records
- **Problem:** `SELECT * FROM transactions ORDER BY id LIMIT 20 OFFSET 5000000;`
  - The database must scan, sort, and discard 5 million rows before returning 20 records, taking 8 seconds!
- **Architectural Solution: Keyset (Cursor-Based) Pagination:**
```sql
-- Keyset pagination using indexed primary key: O(1) constant time!
SELECT * FROM transactions
WHERE id > 5000000
ORDER BY id ASC
LIMIT 20;
```
- In Spring Data JPA: Use `Slice<T>` instead of `Page<T>` to avoid executing `SELECT COUNT(*)` on 10 million rows!

---

### Q26 to Q29. Docker & Core Kubernetes Components
- **Docker:** OS-level virtualization packaging application code, JRE runtime, and system dependencies into an immutable container image.
- **Core Kubernetes (K8s) Components:**
  - **Pod:** Smallest deployable compute unit enclosing 1 or more containers.
  - **Deployment:** Manages declarative rolling updates and replica scaling.
  - **Service (ClusterIP, NodePort, LoadBalancer):** Static internal virtual IP directing traffic to dynamic pod endpoints.
  - **Ingress Controller:** External HTTP/HTTPS reverse proxy routing traffic into K8s services.
  - **ConfigMap & Secret:** Externalized configuration and encrypted keys mounted into pods.

---

## 🏢 Company: Saint Gobain (Pages 56 & 57) — Angular & Spring Boot Focus

### Q4 & Q5. What is a Hook in Angular? Real-World Scenario
- **Angular Lifecycle Hooks:** Interfaces allowing developers to execute logic at specific stages of component existence (`OnInit`, `OnChanges`, `AfterViewInit`, `OnDestroy`).
- **Scenario:** In an enterprise dashboard displaying live IoT manufacturing telemetry, opening the component subscribes to a WebSocket / SSE feed inside `ngOnInit()`.
- **Critical Requirement:** If the user navigates away, failing to unsubscribe inside **`ngOnDestroy()`** causes an **Angular Memory Leak**, where the background WebSocket continues receiving data and consuming browser RAM!
```typescript
@Component({ ... })
export class IotTelemetryComponent implements OnInit, OnDestroy {
  private subscription!: Subscription;

  ngOnInit(): void {
    this.subscription = this.telemetryService.streamData().subscribe();
  }

  ngOnDestroy(): void {
    // Unsubscribe to prevent memory leaks!
    this.subscription?.unsubscribe();
  }
}
```

---

### Q9. Multiply All Integers in a List using Stream API (`reduce`)
- **Input:** `List<Integer> list = List.of(1, 2, 3, 4, 5);`
- **Output:** $1 	imes 2 	imes 3 	imes 4 	imes 5 = 120$.
```java
public class StreamMultiply {
    public static int multiplyAll(List<Integer> list) {
        return list.stream()
            .reduce(1, (a, b) -> a * b);
    }

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        int product = multiplyAll(list);
        System.out.println("Product: " + product); // 120
    }
}
```

---

### Q12. What is a Database Trigger?
- A stored procedure automatically invoked by the database engine whenever a specific DML event (`INSERT`, `UPDATE`, `DELETE`) occurs on a table.
- **Use Case:** Maintaining immutable audit history tables (`AFTER UPDATE ON accounts FOR EACH ROW EXECUTE PROCEDURE log_account_audit()`).
