# Chapter 5: Multithreading, Concurrency, and Memory Keywords

---

## 1. Memory Keywords: `transient` & `volatile`

### A. The `transient` Keyword
- **Purpose:** Used in serialization to instruct Java **NOT to serialize / persist** a specific field to byte streams.
- **Typical Use Cases:** Passwords, API tokens, transient calculated caches, open database sockets/threads.

```java
import java.io.*;

class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;                    // Serialized normally
    private transient String rawPassword;       // NOT serialized (becomes null upon deserialization)
    private static String globalSecret;         // NOT serialized (static belongs to class, not instance)
    private transient final int token = 12345;  // Inlined constant; behaves as constant value

    public UserSession(String username, String rawPassword) {
        this.username = username;
        this.rawPassword = rawPassword;
    }
}
```

---

### B. The `volatile` Keyword & Java Memory Model (JMM)

In modern multicore CPUs, each CPU core has its own **L1/L2 hardware cache**. When a thread writes to a variable, the change initially sits in the local CPU cache and is **not immediately visible** to threads running on other CPU cores.

```
+--------------------+            +--------------------+
|     CPU Core 1     |            |     CPU Core 2     |
| [L1/L2 Cache] flag |            | [L1/L2 Cache] flag |
+--------------------+            +--------------------+
          \                                /
           \                              /
        +------------------------------------+
        |         Main Memory (RAM)          |
        |              flag = false          |
        +------------------------------------+
```

### What `volatile` guarantees:
1. **Visibility:** Writes to a volatile variable are immediately flushed to Main Memory, and reads always fetch directly from Main Memory.
2. **Instruction Reordering Prevention:** Enforces a **happens-before** memory barrier, preventing the compiler and CPU from reordering instructions.

### What `volatile` does NOT guarantee: **Atomicity!**
Operations like `count++` are **3 distinct CPU instructions**:
1. Read `count` from memory
2. Increment `count` by 1
3. Write `count` back to memory

If two threads execute `count++` concurrently on a `volatile int count`, both can read the same value simultaneously, causing lost updates.

```java
// 🔴 WRONG for concurrent increments:
private volatile int count = 0; 
public void increment() { count++; } // RACE CONDITION!

// 🟢 CORRECT for thread-safe atomicity without locks:
private AtomicInteger count = new AtomicInteger(0);
public void increment() { count.incrementAndGet(); } // Lock-free CAS operation
```

### Keyword Allowed vs Forbidden Matrix:
| Keyword / Action | Allowed / Forbidden | Consequence / Reason |
| :--- | :--- | :--- |
| `volatile` on instance / static fields | 🟢 **ALLOWED** | Ensures immediate visibility in main RAM across CPU caches. |
| `volatile` on method local variables | 🔴 **FORBIDDEN** | **Compile Error:** Local variables live on thread's stack (never shared). |
| `volatile` on `final` variables | 🔴 **FORBIDDEN** | **Compile Error:** `final` variables cannot change value, so visibility sync is meaningless. |
| Expecting `volatile int count; count++;` to be atomic | 🔴 **FORBIDDEN (Bug)** | **Race Condition:** `count++` is 3 CPU instructions. Use `AtomicInteger`. |
| `transient` on `static` variables | 🟢 **Allowed (Redundant)**| Static fields belong to the class, not object state; never serialized anyway. |
| `transient` on `final` compile-time constants | 🟢 **Allowed (Ignored)** | Inlined by the compiler at compile-time directly into bytecode. |

---

## 2. Process vs Thread & Why We Create Threads

| Dimension | Process | Thread |
| :--- | :--- | :--- |
| **Definition** | An independent executing program in the OS. | A lightweight execution unit within a process. |
| **Memory** | Has its own dedicated, isolated memory address space. | Shares the Process's **Heap and Metaspace**, but maintains its own **Stack, PC register, and Local variables**. |
| **Context Switching** | Very heavy and costly (OS page table reload). | Much lighter and faster. |
| **Communication** | IPC (Inter-Process Communication: Sockets, Pipes). | Direct memory access via shared variables. |

---

## 3. Creating Threads: `Thread` vs `Runnable` vs `Callable<V>`

```
                                [Thread Creation]
                                /       |       \
               [extends Thread]  [implements Runnable]  [implements Callable<V>]
```

| Feature | `extends Thread` | `implements Runnable` | `implements Callable<V>` |
| :--- | :--- | :--- | :--- |
| **Method** | `public void run()` | `public void run()` | `public V call() throws Exception` |
| **Return Value** | `void` (No return) | `void` (No return) | Returns generic type `V` |
| **Exception Handling** | Cannot throw checked exceptions | Cannot throw checked exceptions | Can throw any checked exception |
| **Multiple Inheritance**| Consumes class inheritance slot | Preserves inheritance slot | Preserves inheritance slot |
| **ThreadPool Support** | Inefficient | Fully supported | Fully supported (returns `Future<V>`) |

### Code Comparison

```java
import java.util.concurrent.*;

// 1. Runnable (Preferred for tasks with no return)
Runnable task = () -> {
    System.out.println("Runnable running on: " + Thread.currentThread().getName());
};

// 2. Callable (Preferred for tasks producing results or throwing exceptions)
Callable<String> callableTask = () -> {
    System.out.println("Callable running on: " + Thread.currentThread().getName());
    return "Task Output Data";
};

public class ThreadDemo {
    public static void main(String[] args) throws Exception {
        // Starting Runnable
        Thread t = new Thread(task);
        t.start(); // Spawns a new native OS thread

        // Executing Callable via ExecutorService
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(callableTask);
        
        // Blocking read of Callable result
        String result = future.get(); 
        System.out.println("Received: " + result);
        
        executor.shutdown();
    }
}
```

> [!IMPORTANT]
> **Why `t.start()` instead of `t.run()`?**
> - Calling `t.run()` directly is just a regular method call executing synchronously inside the **current thread**. No new thread is spawned!
> - Calling `t.start()` instructs the JVM to allocate a new OS thread stack, transition the state to `RUNNABLE`, and asynchronously trigger the `run()` method on the new thread.

---

## 4. Thread Lifecycle & Control Methods

```
  [NEW] ---> (start()) ---> [RUNNABLE] <-----------------------+
                               |    ^                           |
          (Wait for lock)      |    | (Lock acquired)           |
          v                    v    |                           |
       [BLOCKED]            [WAITING / TIMED_WAITING]           |
                               (sleep / wait / join)            |
                               |                                |
                               +---> (Time elapsed / notify) ---+
                               |
                        (Run completes)
                               v
                         [TERMINATED]
```

### Deep Dive: `yield()`, `sleep()`, `join()`, `wait()`

| Method | Belongs To | Does it Release Locks? | Purpose |
| :--- | :--- | :--- | :--- |
| **`Thread.yield()`** | `Thread` class | **NO** | Gives a hint to the OS thread scheduler that the current thread is willing to give up its CPU time slice to other threads of equal priority. |
| **`Thread.sleep(ms)`**| `Thread` class | **NO** | Pauses the current thread for the specified duration. Keeps all acquired monitors/locks. |
| **`Thread.join()`** | `Thread` class | **NO** | Causes the calling thread to pause until the thread on which `join()` was called finishes execution. |
| **`Object.wait()`** | `Object` class | **YES (Releases monitor lock!)** | Causes the thread to wait until another thread invokes `notify()` or `notifyAll()` on the **same monitor object**. Must be called inside a `synchronized` block! |

### Thread Methods Allowed vs Forbidden:
| Invocation | Allowed / Forbidden | Consequence / Mechanism |
| :--- | :--- | :--- |
| Calling `t.start()` on an already started / terminated thread | 🔴 **FORBIDDEN** | **Runtime Exception:** Throws `IllegalThreadStateException`. |
| Calling `t.run()` directly | 🟢 **Allowed (Synchronous)** | Executes on the **current calling thread**, does **NOT** spawn a new thread. |
| Calling `object.wait()` outside a `synchronized` block | 🔴 **FORBIDDEN** | **Runtime Exception:** Throws `IllegalMonitorStateException`. |
| Calling `object.notify()` outside a `synchronized` block | 🔴 **FORBIDDEN** | **Runtime Exception:** Throws `IllegalMonitorStateException`. |
| Expecting `Thread.sleep(ms)` to release locks | 🔴 **FORBIDDEN (Does NOT release)** | Thread pauses execution but **keeps holding all acquired locks**. |
| Expecting `Object.wait()` to keep locks | 🔴 **FORBIDDEN (Releases lock)** | Current thread enters wait-set and **temporarily releases the monitor lock**. |
| Expecting `Thread.yield()` to guarantee execution order | 🔴 **FORBIDDEN (Unreliable)** | Yield is only a heuristic **hint** to the OS scheduler; it may be completely ignored. |

---

## 5. Synchronization, Race Conditions & Deadlocks

### What is a Race Condition?
A race condition occurs when multiple threads concurrently read and write shared mutable state without proper synchronization, causing indeterminate results.

```java
// Thread-safe bank account with synchronized block
class BankAccount {
    private double balance = 1000.0;
    private final Object lock = new Object();

    public void withdraw(double amount) {
        synchronized (lock) { // Critical Section protected by lock
            if (balance >= amount) {
                System.out.println(Thread.currentThread().getName() + " is withdrawing $" + amount);
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + " new balance: $" + balance);
            } else {
                System.out.println(Thread.currentThread().getName() + " - Insufficient funds!");
            }
        }
    }
}
```

---

### What is a Deadlock?
A Deadlock occurs when two or more threads are permanently blocked, each holding a lock that the other thread requires.

```
Thread 1 holds [Lock A] and waits for [Lock B]
Thread 2 holds [Lock B] and waits for [Lock A]  --> DEADLOCK!
```

### The 4 Coffman Conditions for Deadlock:
1. **Mutual Exclusion:** Resources cannot be shared.
2. **Hold and Wait:** Thread holds a lock while waiting for another.
3. **No Preemption:** Locks cannot be forcibly taken away.
4. **Circular Wait:** Thread A waits for B, B waits for A.

### How to Avoid Deadlocks:
1. **Lock Ordering (Primary Rule):** Always acquire multiple locks in the exact same predefined global order across all threads.
2. **`tryLock()` with Timeout:** Use `java.util.concurrent.locks.ReentrantLock.tryLock(timeout)` instead of indefinite `synchronized` blocking.

### Lock Types & Synchronization Matrix:
| Lock Target | Safe / Anti-Pattern | Reason |
| :--- | :--- | :--- |
| `private final Object lock = new Object();` | 🟢 **BEST PRACTICE** | Dedicated instance lock, immune to outside interference. |
| `synchronized(this)` | 🟡 **Acceptable** | Locks instance, but external code holding reference can also lock it. |
| `synchronized(MyClass.class)` | 🟢 **Class-level Lock** | Protects static shared state across all instances. |
| `synchronized(new Object())` | 🔴 **USELESS BUG** | Creates a new lock object on every invocation; provides **ZERO** mutual exclusion! |
| `synchronized("STRING_LITERAL")` | 🔴 **DANGEROUS ANTI-PATTERN** | String literals are shared across JVM via String Constant Pool. |
| `synchronized(null)` | 🔴 **FORBIDDEN** | Throws `NullPointerException` at runtime. |

---

## 6. Thread Pools (`ThreadPoolExecutor`) & Virtual Threads (Java 21)

### The Problem with creating `new Thread()`:
- Creating OS platform threads is expensive (~1MB of stack memory per thread, OS context-switching overhead).
- Spawning 10,000 platform threads will crash the JVM with `OutOfMemoryError: unable to create native thread`.

### How `ThreadPoolExecutor` Works:
1. If running threads < `corePoolSize`, creates a new worker thread.
2. If running threads >= `corePoolSize`, places the task into the `BlockingQueue`.
3. If `BlockingQueue` is full, spawns extra threads up to `maximumPoolSize`.
4. If queue is full and threads == `maximumPoolSize`, triggers the `RejectedExecutionHandler` (e.g. `AbortPolicy`, `CallerRunsPolicy`).

### Virtual Threads (Java 21 Project Loom)
Virtual threads are extremely lightweight user-mode threads managed by the JVM rather than the OS kernel.
- **Cost:** A few hundred bytes of memory (millions can run concurrently).
- **Non-blocking I/O:** When a virtual thread performs blocking I/O (e.g., HTTP request, Database query), the JVM automatically unmounts it from its underlying carrier OS thread and mounts another virtual thread.

```java
// Spawning 100,000 Virtual Threads effortlessly in Java 21:
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 100_000; i++) {
        final int taskId = i;
        executor.submit(() -> {
            Thread.sleep(1000); // Does NOT block an OS thread!
            return taskId;
        });
    }
} // Automatically waits for all 100,000 tasks to finish!
```
