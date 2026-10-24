# Chapter 5: Multithreading, Concurrency, Thread Pools & Modern Async Architecture

---

## 1. Thread Lifecycle & States

Every Java thread transitions through 6 distinct states defined in `java.lang.Thread.State`:

```
               (Thread.start())
  [ NEW ] ------------------------> [ RUNNABLE ] <--------------------------+
                                       |      ^                              |
                  (Enters sync block   |      | (Lock acquired)              |
                   waiting for lock)   v      |                              |
                                   [ BLOCKED ]                               |
                                                                             |
                   (wait() / join() / LockSupport.park())                    |
                                       |      ^                              |
                                       v      | (notify() / join finished)   |
                                  [ WAITING ] |                              |
                                              |                              |
                   (sleep(t) / wait(t) / join(t))                            |
                                       |      ^                              |
                                       v      | (Timeout elapsed)            |
                                [ TIMED_WAITING ]                            |
                                       |                                     |
                                       +-------------------------------------+
                                       |
                              (run() completes)
                                       v
                                 [ TERMINATED ]
```

---

## 2. `volatile` vs `synchronized`

| Feature | `volatile` | `synchronized` |
| :--- | :--- | :--- |
| **Scope** | Variables only (instance/static fields). | Methods and code blocks. |
| **Visibility Guarantee** | 🟢 **YES** (Direct main memory read/write). | 🟢 **YES** (Acquires/releases memory monitor). |
| **Mutual Exclusion (Locking)**| 🔴 **NO** (No locks; threads never block). | 🟢 **YES** (Only 1 thread executes at a time). |
| **Atomicity** | 🔴 **NO** (`count++` is not atomic). | 🟢 **YES** (Entire block executes atomically). |
| **Performance Overhead** | Very low (Zero context switching). | Higher (Monitor lock contention & thread state changes). |

---

## 3. `wait()` vs `sleep()` & `notify()` vs `notifyAll()`

```
+-----------------------------------------------------------------------------------------------+
| Dimension          | `Thread.sleep(ms)`                | `Object.wait()`                      |
+-----------------------------------------------------------------------------------------------+
| Origin             | `java.lang.Thread` static method  | `java.lang.Object` instance method   |
| Lock Release       | 🔴 **Does NOT release locks!**    | 🟢 **Releases monitor lock!**        |
| Context            | Can be called anywhere.           | **Must** be inside `synchronized`.   |
| Waking Condition   | Time expires or `interrupt()`.    | `notify()`, `notifyAll()`, or timeout.|
+-----------------------------------------------------------------------------------------------+
```

### `notify()` vs `notifyAll()`
- **`notify()`**: Wakes up a **single arbitrary thread** from the object's wait set. If multiple threads are waiting for different conditions, `notify()` might wake up the wrong thread, resulting in a **lost signal / deadlock**!
- **`notifyAll()`**: Wakes up **ALL threads** in the wait set. All awakened threads compete for the lock, and each re-evaluates its `while` condition.
- **Production Best Practice**: Always use `notifyAll()` and always wait inside a `while` loop:
  ```java
  synchronized (lock) {
      while (!condition) { // While loop prevents Spurious Wakeups!
          lock.wait();
      }
      // Execute logic
  }
  ```

---

## 4. `ExecutorService` & `ThreadPoolExecutor` Architecture

```
                                  [Task Submitted]
                                         |
                                         v
                      (Is Running Threads < CorePoolSize?)
                               /                  \
                        (YES) /                    \ (NO)
                             v                      v
                   [Create Core Worker]    (Is WorkQueue Full?)
                                               /             \
                                        (NO)  /               \ (YES)
                                             v                 v
                                     [Queue Task]   (Is Threads < MaxPoolSize?)
                                                        /             \
                                                 (YES) /               \ (NO)
                                                      v                 v
                                            [Create Max Worker]  [Trigger RejectionHandler]
```

### 4 Rejection Policies (`RejectedExecutionHandler`):
1. **`AbortPolicy` (Default):** Throws `RejectedExecutionException`.
2. **`CallerRunsPolicy`:** The task is executed by the **calling thread** (throttles task submission).
3. **`DiscardPolicy`:** Silently drops the rejected task.
4. **`DiscardOldestPolicy`:** Drops the oldest unhandled task in the queue and retries submission.

```java
import java.util.concurrent.*;

public class ThreadPoolCustomDemo {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,                              // corePoolSize
            4,                              // maximumPoolSize
            60L, TimeUnit.SECONDS,          // keepAliveTime
            new ArrayBlockingQueue<>(10),   // workQueue
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy() // Rejection Policy
        );

        for (int i = 1; i <= 15; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Processing Task " + taskId + " on " + Thread.currentThread().getName());
            });
        }
        executor.shutdown();
    }
}
```

---

## 5. `FutureTask` vs `CompletableFuture` (Asynchronous Pipelines)

### `FutureTask` (Blocking & Limited)
Wraps a `Callable` or `Runnable`. You can only inspect completion status or call `get()` (which blocks the calling thread).

### `CompletableFuture` (Non-Blocking & Composable - Java 8+)
Allows building non-blocking event-driven asynchronous pipelines:

```java
import java.util.concurrent.*;

public class CompletableFutureDemo {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> pipeline = CompletableFuture
            // 1. Asynchronous supply
            .supplyAsync(() -> {
                System.out.println("Fetching user on: " + Thread.currentThread().getName());
                return 101L;
            })
            // 2. Non-blocking Transformation
            .thenApply(userId -> "UserData_for_" + userId)
            // 3. Compose another async operation
            .thenCompose(userData -> CompletableFuture.supplyAsync(() -> userData + "_ENRICHED"))
            // 4. Handle exceptions gracefully
            .exceptionally(ex -> "Fallback_Data");

        // Blocking get for demonstration
        System.out.println("Pipeline Result: " + pipeline.get());
    }
}
```

---

## 6. Deadlock & How to Programmatically Detect It

### What causes Deadlock? (4 Coffman Conditions):
1. **Mutual Exclusion:** Exclusive resource access.
2. **Hold and Wait:** Holding a lock while waiting for another.
3. **No Preemption:** Cannot forcibly revoke a held lock.
4. **Circular Wait:** Thread 1 waits for Lock B (held by Thread 2), while Thread 2 waits for Lock A (held by Thread 1).

### Programmatic Detection via `ThreadMXBean`:
```java
import java.lang.management.*;

public class DeadlockDetector {
    public static void checkDeadlocks() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreadIds = bean.findDeadlockedThreads();

        if (deadlockedThreadIds != null) {
            System.err.println("🚨 DEADLOCK DETECTED! Affected Thread IDs:");
            ThreadInfo[] infos = bean.getThreadInfo(deadlockedThreadIds);
            for (ThreadInfo info : infos) {
                System.err.println("Thread: " + info.getThreadName() + " blocked on lock: " + info.getLockName());
            }
        } else {
            System.out.println("No deadlocks detected.");
        }
    }
}
```

> [!TIP]
> **CLI Detection in Production:** Run `jstack <PID>` in terminal. The JVM thread dump will automatically analyze and print `"Found 1 deadlock."` with the full stack trace of conflicting threads.

---

## 7. Java 21 Virtual Threads (Project Loom)

Virtual threads are lightweight, user-mode threads managed entirely by the JVM on top of carrier platform OS threads.

```
[Virtual Thread 1]  [Virtual Thread 2]  ... [Virtual Thread 100,000]
        \                  |                     /
         \                 |                    / (Managed by JVM Scheduler)
          v                v                   v
            [Carrier OS Platform Thread 1, 2, 3, 4] (Pinned to physical CPU cores)
```

```java
// Running 10,000 concurrent Virtual Threads effortlessly
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10_000; i++) {
        final int id = i;
        executor.submit(() -> {
            Thread.sleep(1000); // Unmounts virtual thread from carrier thread!
            return "Result " + id;
        });
    }
} // Auto-waits for all virtual threads
```
