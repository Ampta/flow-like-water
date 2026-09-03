# 🎯 Group Discussion & Technical Interview Master Blueprint
## Comprehensive Guide: Java, Spring Boot, Microservices, System Design & Concurrency
> **Designed for Beginners to Advanced Learners**. Explaining every topic from first principles with real-world analogies, step-by-step code, and Group Discussion (GD) speaking points.

---

## 📑 Table of Contents
1. [Java Core & OOP Deep Dive](#1-java-core--oop-deep-dive)
   - 1.1 Can we override the main method?
   - 1.2 Default methods vs Static methods in Interfaces
   - 1.3 Functional Interfaces & Why use Streams?
   - 1.4 Shallow Copy vs Deep Copy
   - 1.5 Java 17 and Java 18 Key Features
2. [Java Collections & Data Structures Internals](#2-java-collections--data-structures-internals)
   - 2.1 HashMap Internal Working & Treeification Mechanics
   - 2.2 How HashMap checks duplicate keys (`hashCode()` and `equals()`)
   - 2.3 HashMap vs Hashtable
   - 2.4 ConcurrentModificationException (Fail-Fast vs Fail-Safe)
3. [Multithreading & Concurrency Control](#3-multithreading--concurrency-control)
   - 3.1 What is `volatile` keyword?
   - 3.2 `volatile` vs `synchronized`
   - 3.3 `Runnable` vs `Callable` & Which to Choose
   - 3.4 `Future` and `FutureTask`
   - 3.5 Thread Locker & `ThreadLocal` Memory Safety
   - 3.6 Java ExecutorService & Thread Pools
   - 3.7 Stream vs Parallel Stream (Performance & Tuning)
4. [Spring Boot & Spring Framework Architecture](#4-spring-boot--spring-framework-architecture)
   - 4.1 `@Component` vs `@Bean` & `@Configuration` vs `@Bean` (CGLIB Proxying)
   - 4.2 `@Controller` vs `@RestController`
   - 4.3 `@ControllerAdvice` & Global Exception Handling
   - 4.4 Stereotype Annotations Hierarchy
   - 4.5 The 3 Core Annotations inside `@SpringBootApplication`
   - 4.6 View Resolvers in Spring MVC
   - 4.7 How to Configure Two Databases in One Spring Boot App
   - 4.8 Spring Boot Transaction Management (`@Transactional`)
   - 4.9 Spring Security Architecture & Filter Chain
   - 4.10 HikariCP Connection Pooling Tuning
5. [Database, JPA & Hibernate Persistence](#5-database-jpa--hibernate-persistence)
   - 5.1 What is N+1 Select Problem & 4 Ways to Rectify It?
   - 5.2 Fetch Types: `LAZY` vs `EAGER` Loading
   - 5.3 YAML File Structure vs `.properties`
6. [System Design, Microservices & High-Availability Architecture](#6-system-design-microservices--high-availability-architecture)
   - 6.1 What is System Design?
   - 6.2 How to Design High-Level Architecture (5-Step Framework)
   - 6.3 Exception Handling in Distributed Systems
   - 6.4 Event-Driven Architecture (EDA)
   - 6.5 Circuit Breaker Pattern (Broken Circuit) & Resilience4j
   - 6.6 What is Apache Kafka and How it Works?
7. [Production Troubleshooting & Design Patterns](#7-production-troubleshooting--design-patterns)
   - 7.1 Memory Leak Reasons & Troubleshooting Guide
   - 7.2 How to Handle High CPU Alert in Production
   - 7.3 Design Patterns Quick Reference & Real-World Selection

---

## 1. Java Core & OOP Deep Dive

### 1.1 Can we override the `main` method in Java?

#### 💡 Simple Analogy
Think of a static method like a **building's main entrance sign**. It belongs to the physical building (the class), not to an individual apartment (an object instance inside). You can't "override" a sign attached to the building structure; you can only attach a new sign in front of a child building, which **hides** the parent sign.

#### 📌 GD Speaking Pitch
> *"No, we cannot override the `main` method in Java because it is declared `static`. In Java, static methods belong to the class itself, not to object instances. Overriding relies on dynamic runtime polymorphism (virtual method invocation). When you define a main method in a subclass with the same signature, it is called **Method Hiding**, not method overriding."*

#### 🔬 Detailed Technical Explanation
1. **Method Overriding Requirement**: Overriding requires runtime method dispatch (late binding) based on the runtime type of the object instance.
2. **Static Binding**: Static methods are resolved at **compile-time** (early binding) based on the reference type.
3. **Overloading vs Overriding**:
   - You **can overload** `main` (e.g., `main(int a)`, `main(String[] args, int b)`).
   - However, the JVM launcher will *only* execute `public static void main(String[] args)` as the application entry point.

#### 💻 Code Example
```java
class Parent {
    public static void main(String[] args) {
        System.out.println("Parent Main");
    }
}

class Child extends Parent {
    // This HIDDEN parent's main method, it does NOT override it!
    public static void main(String[] args) {
        System.out.println("Child Main");
    }
}

public class Test {
    public static void main(String[] args) {
        Parent p = new Child();
        p.main(args); // Prints "Parent Main" because static methods resolve by reference type (Parent)!
    }
}
```

#### 🌐 Real-World Application
Frameworks like Spring Boot rely on static entry points (`SpringApplication.run(Application.class, args)`) to bootstrap the Spring IoC Container before any application objects exist in memory.

---

### 1.2 Why were `default` methods introduced in Java 8 when `static` methods were already available?

#### 💡 Simple Analogy
Imagine a electrical wall plug standard across a country (Interface). If the power company upgrades the grid to require an extra grounding pin, legacy appliances won't plug in unless the outlet adapter (Default Method) provides backward compatibility built right into the interface!

#### 📌 GD Speaking Pitch
> *"Default methods were introduced in Java 8 primarily for **backward compatibility**. They allow developers to add new methods to existing interfaces without breaking legacy code that implements those interfaces. Static methods couldn't solve this because static methods belong strictly to the interface and cannot be overridden by implementing classes."*

#### 🔬 Detailed Technical Explanation
- **The Java 7 Problem**: Oracle wanted to introduce the Streams API to Java 8. To do this, they needed to add `stream()` to the `Collection` interface. If they added a standard abstract method, **every custom library in the world implementing `Collection` would fail to compile!**
- **Why Static Methods Weren't Enough**:
  - `static` methods inside interfaces **cannot be overridden** by implementing classes (e.g., `ArrayList`).
  - `default` methods allow an interface to provide a default implementation while allowing implementing classes like `ArrayList` or `LinkedList` to **override** it with a faster, specialized implementation.

#### 💻 Code Example
```java
public interface Vehicle {
    void start(); // Abstract method

    // Default method added in Java 8
    default void alarmOn() {
        System.out.println("Turning on vehicle alarm");
    }
}

public class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("Car starting...");
    }
    // Car inherits alarmOn() automatically without breaking existing code!
}
```

---

### 1.3 Functional Interfaces & Why use Streams?

#### 💡 Simple Analogy
- **Functional Interface**: A single-button remote control. It has exactly one action to press.
- **Streams**: An automated factory assembly line. Instead of manually inspecting items one by one in a loop (imperative), you set up conveyor belts, filters, and sorters (declarative stream pipeline), and press start.

#### 📌 GD Speaking Pitch
> *"A Functional Interface is an interface containing exactly one abstract method (Single Abstract Method or SAM), serving as the foundation for Lambda Expressions. We use Streams because they provide a declarative, functional approach to bulk data processing with built-in lazy evaluation, optimization, and simple parallel processing capability."*

#### 🔬 Detailed Technical Explanation
1. **Core Functional Interfaces in `java.util.function`**:
   - `Function<T, R>`: Takes input `T`, returns result `R` (`R apply(T t)`).
   - `Predicate<T>`: Takes input `T`, returns `boolean` (`boolean test(T t)`).
   - `Supplier<T>`: Takes no input, produces `T` (`T get()`).
   - `Consumer<T>`: Takes input `T`, returns nothing (`void accept(T t)`).

2. **Why Streams over Traditional `for` Loops?**:
   - **Declarative Code**: Focuses on *what* to do, not *how* to iterate.
   - **Lazy Evaluation**: Intermediate operations (`filter`, `map`) do NOT process items until a terminal operation (`collect`, `count`, `findFirst`) is called.
   - **Pipelining**: Combines multiple transformations into a single iteration pass.

#### 💻 Code Example
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

// Filter names starting with 'C', convert to uppercase, collect into list
List<String> result = names.stream()
    .filter(name -> name.startsWith("C")) // Predicate
    .map(String::toUpperCase)              // Function
    .collect(Collectors.toList());        // Terminal operation

System.out.println(result); // [CHARLIE]
```

---

### 1.4 Shallow Copy vs Deep Copy

#### 💡 Simple Analogy
- **Shallow Copy**: Copying a house key folder. You have a new folder, but the keys inside still open the exact same house locks. Changing the lock affects both folder holders.
- **Deep Copy**: Building an exact 1-to-1 clone of the entire house and all contents in a new town. Changing furniture in the new house does not affect the original house!

#### 📌 GD Speaking Pitch
> *"A shallow copy creates a new object instance but copies references to nested objects, meaning modifications to inner objects affect both copies. A deep copy recursively clones the parent object and all nested child objects, producing a 100% independent memory clone."*

#### 🔬 Detailed Comparison Table

| Feature | Shallow Copy | Deep Copy |
| :--- | :--- | :--- |
| **Nested Objects** | Shared references | Completely cloned instances |
| **Independence** | Outer object independent, inner fields shared | Fully independent |
| **Performance** | Fast & low memory | Slower, higher GC overhead |
| **Creation Method** | `Object.clone()`, `new ArrayList<>(list)` | Copy constructors, JSON serialization, manual recursion |

---

### 1.5 Java 17 (LTS) and Java 18 Key Features

#### 📌 GD Speaking Pitch
> *"Java 17 is a Long-Term Support (LTS) release featuring Sealed Classes, Records, Text Blocks, and Pattern Matching. Java 18 introduced UTF-8 by default and the Simple Web Server for rapid testing."*

#### 🔬 Features Breakdown
1. **Sealed Classes (`sealed`, `permits`)** (Java 17): Restricts which classes can extend a parent class, providing strict domain model boundary control.
```java
public sealed class Shape permits Circle, Square {}
public final class Circle extends Shape {}
public final class Square extends Shape {}
```
2. **Records (`record`)** (Java 17): Immutable data carrier objects that auto-generate constructors, getters, `equals()`, `hashCode()`, and `toString()`.
```java
public record UserDto(String username, String email) {}
```
3. **Text Blocks (`"""`)** (Java 17): Multi-line string literals without hideous manual `\n` escaping.
4. **UTF-8 by Default** (Java 18): Standard Java APIs default to UTF-8 across all operating systems.

---

## 2. Java Collections & Data Structures Internals

### 2.1 HashMap Internal Working & Treeification Mechanics

#### 💡 Simple Analogy
Imagine an apartment building with mailboxes (buckets). When mail arrives, the mail carrier computes a mailbox number from the recipient's name. If multiple people share a mailbox, their letters are stacked in a pile (LinkedList). If a mailbox pile grows higher than 8 letters, the post office replaces the pile with an indexed filing cabinet (Red-Black Tree) for faster searching!

#### 📌 GD Speaking Pitch
> *"HashMap operates on an array of buckets (`Node<K,V>[]`). It calculates key indices using `hash(key) & (n - 1)`. Collisions are handled via linked lists, which convert into Red-Black Trees (Treeification) when a single bucket exceeds 8 elements and overall map capacity is at least 64."*

#### 🔬 Step-by-Step Internal Mechanics
1. **Hash Calculation**: Computes `hash(key) = key.hashCode() ^ (h >>> 16)` to spread high-order bits and reduce collisions.
2. **Bucket Index**: Calculates `index = hash & (capacity - 1)`.
3. **Collision Handling**:
   - Stores entries as linked list nodes (`Node<K,V>`).
   - If bucket size reaches **8 (`TREEIFY_THRESHOLD`)** AND capacity $\ge$ **64**, the bucket converts from $O(N)$ Linked List to $O(\log N)$ **Red-Black Tree**.
   - If bucket size drops below **6 (`UNTREEIFY_THRESHOLD`)**, it converts back to a linked list during resize.
4. **Load Factor & Resizing**: Default load factor is **0.75**. When `size > capacity * 0.75`, the map doubles its array size and rehashes entries.

---

### 2.2 How HashMap Checks Duplicate Keys (`hashCode()` and `equals()`)

#### 📌 GD Speaking Pitch
> *"HashMap checks duplicate keys in a two-step process: First, it compares the integer `hashCode()` of the keys. If the hashes match, it performs a second check using `==` identity OR `.equals()` equality. If both pass, the key is considered a duplicate and its value is overwritten."*

#### 🔬 The Contract Rules
1. **Rule 1**: If `a.equals(b)` is `true`, `a.hashCode()` **MUST** equal `b.hashCode()`.
2. **Rule 2**: If `a.hashCode() == b.hashCode()`, `a.equals(b)` may be `true` or `false` (Hash Collision).

```java
// How HashMap checks matching keys inside a bucket:
if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
    // Duplicate Key Found! Overwrite value.
}
```

---

### 2.3 HashMap vs Hashtable

| Feature | HashMap | Hashtable | ConcurrentHashMap |
| :--- | :--- | :--- | :--- |
| **Thread Safety** | Non-thread-safe | Thread-safe (Global method locking) | Thread-safe (CAS + Bucket locks) |
| **Null Keys/Values** | Allows 1 null key, multiple null values | Throws `NullPointerException` | Throws `NullPointerException` |
| **Performance** | Highest ($O(1)$) | Slow (Thread bottleneck) | High concurrent throughput |

---

### 2.4 ConcurrentModificationException (Fail-Fast vs Fail-Safe)

#### 📌 GD Speaking Pitch
> *"ConcurrentModificationException occurs when a thread modifies a collection structurally (adding/removing items) while iterating over it without using the iterator's own remove method. It works via an internal `modCount` modification counter."*

- **Fail-Fast Iterators (e.g., ArrayList, HashMap)**: Check `modCount != expectedModCount`. If mismatched, throw `ConcurrentModificationException` immediately.
- **Fail-Safe Iterators (e.g., CopyOnWriteArrayList, ConcurrentHashMap)**: Iterate over a snapshot clone of the collection. Never throw exception.

---

## 3. Multithreading & Concurrency Control

### 3.1 What is the `volatile` keyword?

#### 💡 Simple Analogy
In a multi-core CPU, each CPU core has its own ultra-fast local cache (L1/L2 cache), like a personal notepad. Without `volatile`, Core 1 writes a variable update to its notepad, but Core 2 reads from main RAM and never sees Core 1's change! `volatile` forces all reads and writes to go directly to **Main RAM**.

#### 📌 GD Speaking Pitch
> *"The `volatile` keyword in Java guarantees **memory visibility** and prevents instruction reordering by compiler and CPU hardware. It ensures every read/write to a variable is flushed directly to Main RAM, making updates immediately visible across all CPU threads."*

---

### 3.2 `volatile` vs `synchronized`

| Feature | `volatile` | `synchronized` |
| :--- | :--- | :--- |
| **Applies To** | Variables only | Methods and block statements |
| **Visibility Guarantee** | Yes | Yes |
| **Atomicity Guarantee** | **No** (e.g., `count++` is non-atomic) | **Yes** (Mutual exclusion lock) |
| **Thread Blocking** | Non-blocking | Blocks threads (Context switch) |

---

### 3.3 `Runnable` vs `Callable` & Which to Choose

#### 📌 GD Speaking Pitch
> *"Use `Runnable` for fire-and-forget tasks that do not return a result or throw checked exceptions. Use `Callable<V>` when your asynchronous background task needs to return a value (`Future<V>`) or handle checked exceptions."*

```java
// Runnable (No return value, no checked exceptions)
Runnable r = () -> System.out.println("Processing background log...");

// Callable (Returns String, can throw Exception)
Callable<String> c = () -> {
    if (error) throw new SQLException("Database Error");
    return "Query Finished";
};
```

---

### 3.4 `Future` and `FutureTask`

#### 📌 GD Speaking Pitch
> *"A `Future` represents the pending result of an asynchronous computation. `FutureTask` is a concrete class implementing both `Runnable` and `Future`, allowing it to be executed by an `ExecutorService` or standalone `Thread`."*

- **`future.get()`**: Blocking call that waits for task completion.
- **`future.get(5, TimeUnit.SECONDS)`**: Non-blocking wait with a strict timeout boundary.

---

### 3.5 Thread Locker & `ThreadLocal` Memory Safety

#### 💡 Simple Analogy
`ThreadLocal` is like a personal locker assigned to each worker thread in a factory. Anything stored in the locker belongs strictly to that worker thread.

#### 📌 GD Speaking Pitch
> *"ThreadLocal provides thread-isolated state storage. However, in web servers using Thread Pools (like Tomcat), failing to call `.remove()` after request completion causes severe memory leaks and data leakage across HTTP requests!"*

#### ⚠️ The Golden Rule
Always clear `ThreadLocal` in a `finally` block:
```java
try {
    userContext.set(currentUser);
    chain.doFilter(request, response);
} finally {
    userContext.remove(); // CRITICAL to prevent thread pollution & memory leaks!
}
```

---

### 3.6 Java ExecutorService & Thread Pools

#### 📌 GD Speaking Pitch
> *"ExecutorService manages pool worker thread lifecycles and task queues, preventing OS thread creation overhead. Never use `Executors.newCachedThreadPool()` in production because it allows unbounded thread creation leading to OutOfMemoryError."*

#### ThreadPoolExecutor Rejection Policies (When Queue is Full):
1. `AbortPolicy` (Default): Throws `RejectedExecutionException`.
2. `CallerRunsPolicy`: Executing thread runs the task itself (provides natural backpressure).
3. `DiscardPolicy`: Silently drops task.
4. `DiscardOldestPolicy`: Drops oldest task in queue.

---

### 3.7 Stream vs Parallel Stream (Performance & Tuning)

#### 📌 GD Speaking Pitch
> *"Parallel streams split workloads across CPU cores using the shared `ForkJoinPool.commonPool()`. They excel at large, CPU-bound data operations, but cause severe performance degradation if used for blocking database or HTTP network I/O operations!"*

---

## 4. Spring Boot & Spring Framework Architecture

### 4.1 `@Component` vs `@Bean` & `@Configuration` vs `@Bean`

#### 📌 GD Speaking Pitch
> *"`@Component` is used on custom classes for automatic component scanning. `@Bean` is used on factory methods inside `@Configuration` classes to register 3rd-party library classes into the Spring container. `@Configuration` enables CGLIB proxying so calling a `@Bean` method internally returns the cached singleton instance."*

---

### 4.2 `@Controller` vs `@RestController`

- `@Controller`: Used for classic Spring MVC web applications returning HTML views (e.g. Thymeleaf, JSP).
- `@RestController`: Meta-annotation combining `@Controller` + `@ResponseBody`. Automatically serializes return objects into JSON/XML HTTP responses.

---

### 4.3 `@ControllerAdvice` & Global Exception Handling

#### 📌 GD Speaking Pitch
> *"`@ControllerAdvice` provides global, centralized exception handling across all Spring REST controllers using `@ExceptionHandler` methods, ensuring consistent standard error responses across microservices."*

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("USER_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

---

### 4.4 Stereotype Annotation Hierarchy

All Spring stereotypes (`@Service`, `@Repository`, `@Controller`) are specialized meta-annotations of `@Component`:
- `@Component`: Generic Spring bean.
- `@Service`: Business logic layer.
- `@Repository`: DAO / Persistence layer; automatically translates SQL exceptions into Spring `DataAccessException`.
- `@Controller` / `@RestController`: Presentation HTTP REST layer.

---

### 4.5 The 3 Core Annotations inside `@SpringBootApplication`

1. `@SpringBootConfiguration`: Specialized `@Configuration` for Spring Boot.
2. `@EnableAutoConfiguration`: Automatically configures embedded Tomcat, Spring MVC, and DB defaults based on classpath JAR dependencies.
3. `@ComponentScan`: Scans the current package and sub-packages for Spring components.

---

### 4.6 View Resolvers in Spring MVC

ViewResolvers map logical view names returned by controllers to physical template rendering files (e.g. `InternalResourceViewResolver` for JSP, `ThymeleafViewResolver`, `ContentNegotiatingViewResolver`).

---

### 4.7 How to Configure Two Databases in One Spring Boot App

1. Define database properties in `application.yml` under distinct prefixes (`spring.datasource.primary`, `spring.datasource.secondary`).
2. Create two `@Configuration` classes. Annotate one with `@Primary`.
3. Explicitly define `DataSource`, `EntityManagerFactory`, and `PlatformTransactionManager` beans linked to their respective repository packages (`@EnableJpaRepositories(basePackages = "...")`).

---

### 4.8 Spring Boot Transaction Management (`@Transactional`)

- **Propagation Types**:
  - `REQUIRED` (Default): Joins active transaction or creates a new one.
  - `REQUIRES_NEW`: Suspends current transaction and opens a new isolated transaction.
- **Rollback Behavior**: Automatically rolls back on Unchecked (`RuntimeException`). Requires `rollbackFor = Exception.class` to roll back on Checked exceptions.

---

### 4.9 Spring Security Architecture & Filter Chain

Spring Security executes via a chain of Servlet Filters (`DelegatedFilterProxy` -> `SecurityFilterChain`). Incoming HTTP requests pass through `JwtAuthenticationFilter`, extract JWT tokens, validate credentials, and store authentication in `SecurityContextHolder.getContext().setAuthentication(auth)`.

---

### 4.10 HikariCP Connection Pooling Tuning

- `maximumPoolSize`: Maximum total DB connections in pool. Recommended formula: $\text{Connections} = (\text{CPU Cores} \times 2) + \text{Disk Spindles}$.
- `minimumIdle`: Minimum idle connections maintained.
- `connectionTimeout`: Max milliseconds client waits for connection before throwing error.

---

## 5. Database, JPA & Hibernate Persistence

### 5.1 What is N+1 Select Problem & 4 Ways to Rectify It?

#### 📌 GD Speaking Pitch
> *"The N+1 problem occurs when fetching 1 parent entity results in Hibernate executing N additional SQL queries to fetch child entities. We fix it using JOIN FETCH, `@EntityGraph`, `@BatchSize`, or DTO Projections."*

1. **JPQL `JOIN FETCH`**: `SELECT a FROM Author a JOIN FETCH a.books`
2. **JPA `@EntityGraph`**: `@EntityGraph(attributePaths = {"books"})`
3. **Hibernate `@BatchSize(size = 20)`**: Batches child queries using SQL `IN (?, ?, ...)` clause.
4. **DTO Constructor Projections**: Direct SQL selection of required fields.

---

### 5.2 Fetch Types: `LAZY` vs `EAGER` Loading

- `EAGER`: Loads child relationships immediately along with parent.
- `LAZY`: Creates a proxy and defers loading until the child collection is accessed.
- **Rule of Thumb**: Always default to `LAZY` loading to prevent massive unwanted memory consumption!

---

### 5.3 YAML File Structure vs `.properties`

YAML (`application.yml`) provides hierarchical nested structures with native array support, eliminating repetitive key prefix boilerplate present in `.properties` files.

---

## 6. System Design, Microservices & High-Availability Architecture

### 6.1 What is System Design?

#### 📌 GD Speaking Pitch
> *"System Design is the process of defining the architecture, components, data models, storage, and communication interfaces for a software application to satisfy both functional requirements (features) and non-functional requirements (scalability, availability, latency, fault tolerance)."*

---

### 6.2 How to Design High-Level Architecture (5-Step Framework)

1. **Step 1: Requirements & Constraints**: Estimate DAU/MAU, Read/Write ratio, SLA (99.99% uptime).
2. **Step 2: API & Data Model**: REST/gRPC endpoints, SQL vs NoSQL.
3. **Step 3: Core Architecture**: CDN -> API Gateway -> Stateless Microservices (Kubernetes).
4. **Step 4: Database & Caching**: Redis Cluster for caching, Read Replicas for database scaling.
5. **Step 5: Decoupling & Fault Tolerance**: Apache Kafka message streaming, Resilience4j Circuit Breakers.

---

### 6.3 Exception Handling in Distributed Systems

- **Correlation ID (Trace ID)**: Passed in HTTP header `X-Correlation-ID` across services to trace log execution across Zipkin/ELK.
- **Idempotency Keys**: Ensures retrying failed HTTP payment requests does not double-charge users.
- **Dead Letter Queue (DLQ)**: Diverts unprocessable messages to a DLQ for manual inspection.

---

### 6.4 Event-Driven Architecture (EDA)

Decouples services into Event Producers and Event Consumers via a broker (Kafka/RabbitMQ). When an event occurs (e.g. `OrderPlacedEvent`), subscribers consume the event asynchronously without blocking the main transaction.

---

### 6.5 Circuit Breaker Pattern (Broken Circuit) & Resilience4j

#### 📌 GD Speaking Pitch
> *"Circuit Breakers prevent cascading failures in microservices. When downstream service failure rate exceeds a threshold, the circuit transitions from CLOSED to OPEN, immediately returning fallback responses without waiting for network timeouts."*

- **CLOSED**: Normal operation.
- **OPEN**: Requests fail fast immediately; fallback invoked.
- **HALF-OPEN**: Trial requests sent to test if downstream service recovered.

---

### 6.6 What is Apache Kafka and How it Works?

#### 📌 GD Speaking Pitch
> *"Apache Kafka is a distributed event streaming platform built for high-throughput log append storage. Topics are divided into Partitions distributed across Brokers, where Consumer Groups consume messages sequentially via Offsets."*

---

## 7. Production Troubleshooting & Design Patterns

### 7.1 Memory Leak Reasons & Troubleshooting Guide

- **Common Causes**: Unremoved `ThreadLocal` variables in thread pools, unclosed DB connections/streams, static collections holding objects.
- **Troubleshooting Steps**:
  1. Generate Heap Dump on OutOfMemoryError (`-XX:+HeapDumpOnOutOfMemoryError`).
  2. Open `.hprof` file in **Eclipse Memory Analyzer Tool (MAT)** or **JProfiler**.
  3. Analyze **Dominator Tree** to locate leak suspect objects retaining memory.

---

### 7.2 How to Handle High CPU Alert in Production

1. Identify offending process ID (`top`).
2. Identify high-CPU thread ID (`top -H -p <PID>`).
3. Convert thread ID to hexadecimal (`printf "%x\n" <TID>`).
4. Generate Thread Dump (`jstack <PID> > thread_dump.txt`).
5. Search for hex thread ID in dump to locate exact line of code or JVM GC thrashing!

---

### 7.3 Design Patterns Quick Reference & Real-World Selection

| Pattern | Category | Real-World Use Case |
| :--- | :--- | :--- |
| **Singleton** | Creational | Shared Spring Beans, DB Connection Manager |
| **Factory** | Creational | Dynamic Payment Processor (`StripeProcessor`, `PayPalProcessor`) |
| **Builder** | Creational | Creating complex immutable DTOs (`User.builder().build()`) |
| **Strategy** | Behavioral | Dynamic Shipping Cost Calculation based on courier |
| **Observer** | Behavioral | Kafka Pub-Sub messaging / Event Listeners |
| **Decorator** | Structural | Java I/O Streams (`new BufferedReader(new FileReader(f))`) |
| **Adapter** | Structural | Converting legacy 3rd-party XML API to modern JSON |
