# Java Developer Ultra-Memorable Reference Guide (3+ Years Experience)

> 🎯 **Goal**: Designed for **Instant Retention** using 1-Line Memory Hooks, Visual ASCII Flowcharts, Bad vs Good Code Traps, and 5-Second Interview Answers.

---

## ⚡ Quick Navigation & Memory Matrix

| Module | 🧠 1-Line Memory Hook / Formula | 🎯 5-Second Interview Answer |
| :--- | :--- | :--- |
| **1. JVM & Memory** | **H-M-S-P-N**: Heap (Objects), Meta (Class info), Stack (Local vars), PC (Next line), Native (C/C++). | *"Heap stores shared objects managed by GC; Stack stores thread-local execution frames; Metaspace holds class metadata off-heap."* |
| **2. OOP & Fundamentals** | **Overload** = Compile-time (Params diff); **Override** = Runtime (Params same); Never use `double` for money! | *"Use `BigDecimal("0.1")` constructed from String for exact currency operations; floating point double causes binary precision loss."* |
| **3. Exceptions** | **Checked** = Recoverable compile-time; **Unchecked** = Runtime code bugs; Spring defaults rollback to Unchecked. | *"Checked exceptions force caller handling; Unchecked extend RuntimeException. Spring `@Transactional` needs `rollbackFor=Exception.class` for checked ones."* |
| **4. Collections** | **HashMap Rule 8 & 64**: Bucket list $\rightarrow$ Red-Black Tree when size $\ge 8$ and table capacity $\ge 64$. | *"ConcurrentHashMap locks only individual bucket heads via synchronized nodes, unlike Hashtable which locks the entire table."* |
| **5. Streams** | `map` = $1 \to 1$ transformation; `flatMap` = $1 \to N$ unboxing / flattening nested collections. | *"Use `flatMap` when each element maps to a Stream of items that needs flattening into a single continuous pipeline."* |
| **6. Modern Java** | `record` = Immutable DTO; `sealed` = Restricted inheritance hierarchy via `permits`. | *"Sealed classes give class authors compile-time control over which subtypes can extend a class, enabling safe exhaustive switch pattern matching."* |
| **7. Concurrency** | `volatile` = VISIBILITY (CPU cache flush), NOT Atomicity; Deadlock = **M-H-N-C**. | *"Volatile forces reads/writes directly to main memory preventing CPU cache staleness, but atomic compound ops need AtomicInteger or locks."* |
| **8. I/O & Serialization** | `transient` = Skip field in serialization; `serialVersionUID` = Class structure fingerprint. | *"NIO is channel/buffer non-blocking I/O ideal for scalable networking; standard IO is stream-based blocking."* |
| **9. Database & JPA** | **N+1 Problem**: 1 parent query + N child queries. **Fix**: `JOIN FETCH` or `@EntityGraph`. | *"N+1 query problem occurs when accessing lazy collections in a loop. Solved by fetching parents and children together via JPQL `JOIN FETCH`."* |
| **10. Spring Boot** | **Self-Invocation Trap**: `this.method()` bypasses Spring CGLIB Proxy $\rightarrow$ No `@Transactional` boundary! | *"Spring `@Transactional` works via CGLIB proxies. Internal self-invocations bypass the proxy, causing transaction annotations to be ignored."* |
| **11. Design Patterns** | **Strategy Pattern**: Replace ugly `if-else` blocks with Spring `Map<String, Strategy>` auto-injection. | *"Strategy pattern encapsulates algorithms behind an interface, allowing runtime selection without violating the Open-Closed Principle."* |

---

## Module 1: JVM Architecture & Memory Management

### 🧠 Memory Hook: The "H-M-S-P-N" Formula
Think of JVM memory as a **High-Security Building**:
* **H**eap: The **Lobby** (Shared by everyone. All objects created with `new` sit here).
* **M**etaspace: The **Archive Room** (Shared class blueprints, method metadata).
* **S**tack: Private **Desk** for each worker (Thread local variables, method call steps).
* **P**C Register: The **Bookmark** (Remembers the exact instruction line the thread is executing).
* **N**ative Stack: The **External Hotline** (Calls C/C++ libraries).

```
 +-------------------------------------------------------------------------+
 |                           JVM RUNTIME MEMORY                            |
 |                                                                         |
 |   +-----------------------------------------------------------------+   |
 |   | SHARED MEMORY (App Lifespan)                                    |   |
 |   |  +---------------------------+  +----------------------------+  |   |
 |   |  | HEAP (Objects, Arrays)    |  | METASPACE (Class Metadata) |  |   |
 |   |  | Young (Eden, S0, S1)      |  | Off-heap native memory     |  |   |
 |   |  | Old Gen (Tenured)         |  |                            |  |   |
 |   |  +---------------------------+  +----------------------------+  |   |
 |   +-----------------------------------------------------------------+   |
 |                                                                         |
 |   +-----------------------------------------------------------------+   |
 |   | PER-THREAD MEMORY (Thread Lifespan)                            |   |
 |   |  +------------------+  +------------------+  +---------------+  |   |
 |   |  | JVM Stack        |  | PC Register      |  | Native Stack  |  |   |
 |   |  | (Frames, Locals) |  | (Instruction ptr)|  | (C/C++ calls) |  |   |
 |   |  +------------------+  +------------------+  +---------------+  |   |
 |   +-----------------------------------------------------------------+   |
 +-------------------------------------------------------------------------+
```

---

### 💡 Visual Memory Trap vs Fix: Unbounded Memory Leak

```java
// ❌ THE MEMORY LEAK TRAP (Hard to remember why it crashes?)
// Rule: STATIC collections NEVER die automatically because GC Roots point to them forever!
public class BadCache {
    private static final Map<String, byte[]> LEAK_MAP = new HashMap<>();
    
    public void add(String id) {
        LEAK_MAP.put(id, new byte[1024 * 1024]); // 1MB per call -> OutOfMemoryError!
    }
}

// 💡 THE 3-LINE FIX (Easy to remember)
// Rule: Use WeakHashMap or Caffeine Cache with TTL eviction!
public class GoodCache {
    // Keys wrapped in WeakReference are cleaned automatically on next GC when unused!
    private final Map<String, byte[]> safeMap = Collections.synchronizedMap(new WeakHashMap<>());
}
```

---

### 🎯 5-Second Interview Cheat Sheet: JVM & GC
* **Q: Difference between Minor, Major, and Full GC?**
  * *Answer*: **Minor GC** cleans Young Gen (fast). **Major GC** cleans Old Gen. **Full GC** cleans Heap + Metaspace (causes Stop-The-World latency spikes).
* **Q: How to diagnose OutOfMemoryError in production?**
  * *Answer*: Take heap dump (`jcmd <pid> GC.heap_dump /tmp/dump.hprof`) and inspect Dominator Tree in **Eclipse MAT**.

---

## Module 2: Core Java & Object-Oriented Design (OOP)

### 🧠 Memory Hook: Overloading vs Overriding vs Method Hiding

| Feature | Where it happens | Method Signatures | Bound At |
| :--- | :--- | :--- | :--- |
| **Overloading** | Same class | Same name, **Different params** | **Compile-time** (Static) |
| **Overriding** | Subclass (`@Override`) | Same name, **Exact same params** | **Runtime** (Dynamic Dispatch) |
| **Method Hiding** | Subclass (`static`) | Same name, **Static method** | **Compile-time** (Reference type) |

```java
class Parent {
    public void greet() { System.out.println("Hello Parent"); }
    public static void stat() { System.out.println("Static Parent"); }
}
class Child extends Parent {
    @Override
    public void greet() { System.out.println("Hello Child"); }
    public static void stat() { System.out.println("Static Child"); } // Hiding!
}

public class OOPTest {
    public static void main(String[] args) {
        Parent p = new Child();
        p.greet(); // Prints "Hello Child" (Dynamic Method Dispatch)
        p.stat();  // Prints "Static Parent" (Bound to Reference Type 'Parent'!)
    }
}
```

---

### 🧠 Financial Golden Rule: `BigDecimal` vs `double`
> ⚠️ **NEVER** write `new BigDecimal(0.1)` with double! ALWAYS use String: `new BigDecimal("0.1")`.

```java
// ❌ WRONG (Preserves double inaccuracy!)
BigDecimal bad = new BigDecimal(0.1); 
// Output: 0.1000000000000000055511151231257827021181583404541015625

// 💡 RIGHT (Exact Math!)
BigDecimal good = new BigDecimal("0.1"); 
// Output: 0.1
```

---

## Module 3: Exception Handling & Robustness

### 🧠 Memory Hook: The Exception Hierarchy Map

```
                     Throwable
                         |
        +----------------+----------------+
        |                                 |
      Error                           Exception (Checked)
  (JVM Failure)                           |
  - OutOfMemoryError                      +-------------------+
  - StackOverflowError                    |                   |
                                    IOException        RuntimeException (Unchecked)
                                    SQLException       - NullPointerException
                                                       - IllegalArgumentException
```

### 💡 Visual Code Cheat Sheet: Try-With-Resources

```java
// ❌ OLD WAY (Messy, risk of unclosed file streams)
FileReader fr = null;
try {
    fr = new FileReader("file.txt");
} finally {
    if (fr != null) fr.close(); // Catch nested exception needed!
}

// 💡 MODERN WAY (Auto-closeable in reverse order)
try (FileReader fr = new FileReader("file.txt");
     BufferedReader br = new BufferedReader(fr)) {
    System.out.println(br.readLine());
} // Auto-closed automatically here!
```

---

## Module 4: Java Collections Framework & Internals

### 🧠 Memory Hook: HashMap Treeification (The 8 & 64 Rule)
> *"Buckets start as Linked Lists. When a bucket gets **8 elements** AND total table capacity is $\ge \mathbf{64}$, it converts to a **Red-Black Tree** ($O(\log N)$ search). When it shrinks to **6**, it reverts to Linked List."*

```
Buckets Array
  [0] -> Node1 -> Node2
  [1] -> [Red-Black Tree (8+ nodes)]  <-- Treeified for O(log N) speed!
  [2] -> null
```

---

### 🧠 Hashtable vs ConcurrentHashMap (The Locking Trick)

```
Hashtable (Slow):
[  LOCK ENTIRE MAP  ] --> Every thread blocks every other thread!

ConcurrentHashMap (Fast):
Bucket [0] (Unlocked)
Bucket [1] [🔒 Locked Node] --> Thread A works on Bucket 1
Bucket [2] (Unlocked)       --> Thread B works on Bucket 2 simultaneously!
```

---

## Module 5: Java 8+ Functional Programming & Streams

### 🧠 Memory Hook: The 4 Core Functional Interfaces

```
 1. Function<T, R>   : T  ---> R       (Transform: e.g., String to Integer)
 2. Predicate<T>     : T  ---> boolean (Filter   : e.g., age > 18)
 3. Supplier<T>      : () ---> T       (Factory  : e.g., () -> new User())
 4. Consumer<T>      : T  ---> void    (Action   : e.g., System.out.println)
```

---

### 💡 Stream `map` vs `flatMap` Cheat Sheet

```java
List<List<String>> nested = List.of(List.of("A", "B"), List.of("C", "D"));

// ❌ map keeps nested structure: List<Stream<String>>
nested.stream().map(list -> list.stream()); 

// 💡 flatMap flattens into single stream: ["A", "B", "C", "D"]
List<String> flat = nested.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
```

---

## Module 6: Modern Java Features (JDK 8 to 21)

### 🧠 Memory Hook: Records & Sealed Classes

```java
// 💡 RECORD (JDK 16+): 1-Line Immutable DTO (Generates fields, getters, equals, hashCode, toString)
public record EmployeeDTO(Long id, String name, double salary) {}

// 💡 SEALED CLASS (JDK 17+): Restricted Hierarchy (Only permitted classes can extend)
public sealed interface Shape permits Circle, Square {}
public final class Circle implements Shape {}
public final class Square implements Shape {}

// 💡 SWITCH PATTERN MATCHING (JDK 17+): Compiler checks exhaustiveness!
public String getAreaFormula(Shape shape) {
    return switch (shape) {
        case Circle c -> "PI * r^2";
        case Square s -> "side ^ 2";
    }; // No default case required because Shape is sealed!
}
```

---

## Module 7: Multithreading & Concurrency

### 🧠 Memory Hook: `volatile` vs `synchronized` vs `AtomicInteger`

| Tool | Guarantees Visibility? | Guarantees Atomicity? | Lock-Free? |
| :--- | :--- | :--- | :--- |
| **`volatile`** | **YES** (Flushes to Main Memory) | ❌ NO (Compound ops fail) | **YES** |
| **`synchronized`** | **YES** | **YES** | ❌ NO (Blocks threads) |
| **`AtomicInteger`** | **YES** | **YES** (via CPU CAS instructions) | **YES** |

---

### 🧠 Deadlock 4 Conditions (M-H-N-C Acronym)
1. **M**utual Exclusion (Resource locked exclusively).
2. **H**old and Wait (Holds A, waits for B).
3. **N**o Preemption (Cannot steal lock).
4. **C**ircular Wait (Thread 1 $\rightarrow$ Thread 2 $\rightarrow$ Thread 1).
> 💡 **Fix**: Always acquire locks in the **exact same alphabetical order** across all threads!

---

### 🚀 Asynchronous Parallel Execution: `CompletableFuture`

```java
// 💡 Execute 3 microservice calls in PARALLEL in ~1 second total (instead of 3 seconds sequential)
CompletableFuture<String> userFuture  = CompletableFuture.supplyAsync(() -> callUserService());
CompletableFuture<String> orderFuture = CompletableFuture.supplyAsync(() -> callOrderService());

// Wait for all to finish concurrently
CompletableFuture.allOf(userFuture, orderFuture).join();

String user = userFuture.join();
String order = orderFuture.join();
```

---

## Module 8: Java I/O & Serialization

### 🧠 Memory Hook: `transient` & `serialVersionUID`
* **`transient`**: *"Skip me during serialization!"* Use for passwords or cached data.
* **`serialVersionUID`**: Class version fingerprint. If you change a class without matching this ID, deserialization throws `InvalidClassException`.

---

## Module 9: Enterprise Data Access (SQL & JPA N+1 Problem)

### 🧠 The JPA N+1 Problem Demystified

```
Scenario: Fetching 100 Departments and their Employees.

❌ BAD (N+1 Queries):
Query 1: SELECT * FROM department;                         (Returns 100 departments)
Query 2..101: SELECT * FROM employee WHERE dept_id = ?;   (Executes 100 SEPARATE queries in a loop!)
Total SQL Calls: 101 queries! (Kills Database Performance)

💡 GOOD (1 Query via JOIN FETCH):
Query 1: SELECT d FROM Department d JOIN FETCH d.employees;
Total SQL Calls: 1 SINGLE JOIN query!
```

```java
// 💡 Spring Data JPA Repository Fix:
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT DISTINCT d FROM Department d JOIN FETCH d.employees")
    List<Department> findAllDepartmentsWithEmployees();
}
```

---

## Module 10: Spring Boot & `@Transactional`

### 🧠 Memory Hook: The Spring Proxy Self-Invocation Trap

```
Spring Container
 +-------------------------------------------------------+
 |  CGLIB Proxy Object                                   |
 |    | Starts @Transactional Boundary                   |
 |    v                                                  |
 |  Target OrderService Bean                             |
 |    | processPublic()                                  |
 |    |   └─-> this.saveTx()  <-- ❌ SELF-INVOCATION!   |
 |    |                           Bypasses Proxy!       |
 |    |                           No Transaction started|
 +-------------------------------------------------------+
```

```java
@Service
public class OrderService {

    public void processPublic() {
        // ❌ BUG: Internal call bypasses Spring Proxy! @Transactional is IGNORED!
        this.saveTx(); 
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveTx() {
        // DB save...
    }
}

// 💡 FIX: Put saveTx() inside a separate injected Service class so calls go through the Proxy!
```

---

## Module 11: Design Patterns & Strategy Pattern

### 🧠 Strategy Pattern: Eliminating Ugly `if-else`

```java
// 💡 1. Define Strategy Contract
public interface PaymentStrategy {
    void pay(double amount);
}

@Component("CREDIT_CARD")
class CreditCardPay implements PaymentStrategy {
    public void pay(double amt) { System.out.println("Paid CC: " + amt); }
}

@Component("UPI")
class UpiPay implements PaymentStrategy {
    public void pay(double amt) { System.out.println("Paid UPI: " + amt); }
}

// 💡 2. Spring Auto-injects ALL strategies into a Map automatically!
@Service
public class PaymentService {
    private final Map<String, PaymentStrategy> paymentStrategies;

    public PaymentService(Map<String, PaymentStrategy> paymentStrategies) {
        this.paymentStrategies = paymentStrategies;
    }

    public void execute(String type, double amount) {
        // Zero if-else statements! Clean Open-Closed Principle!
        paymentStrategies.get(type).pay(amount);
    }
}
```

---

## 🎯 Final 10-Second Self-Test Flashcards

1. **Where are class metadata stored in Java 8+?**
   * *Answer*: Metaspace (Off-heap native memory).
2. **What triggers HashMap Treeification?**
   * *Answer*: Bucket length $\ge 8$ AND table capacity $\ge 64$.
3. **Does `volatile` guarantee atomicity for `count++`?**
   * *Answer*: No. `volatile` only guarantees visibility. Use `AtomicInteger` for atomicity.
4. **How do you fix JPA N+1 queries in Spring Data JPA?**
   * *Answer*: Use `JOIN FETCH` in JPQL or `@EntityGraph`.
5. **Why does Spring `@Transactional` fail on internal method calls?**
   * *Answer*: Self-invocation calls `this.method()` directly, bypassing the Spring CGLIB proxy wrapper.
