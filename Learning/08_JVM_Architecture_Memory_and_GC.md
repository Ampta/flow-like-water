# Chapter 8: JVM Architecture, Memory Model, Garbage Collection & ClassLoaders

---

## 1. JVM Memory Architecture Overview

```
+---------------------------------------------------------------------------------------+
|                                    JVM Runtime Data Areas                             |
|                                                                                       |
|   +------------------------------------+   +---------------------------------------+  |
|   |            HEAP MEMORY             |   |          METASPACE (Native RAM)       |  |
|   |  - Young Generation (Eden, S0, S1) |   |  - Class Metadata & Bytecode          |  |
|   |  - Old / Tenured Generation        |   |  - Static Variables                   |  |
|   |  - String Constant Pool (SCP)      |   |  - Method Code & Constants            |  |
|   |  (Shared across ALL Threads)       |   |  (Shared across ALL Threads)          |  |
|   +------------------------------------+   +---------------------------------------+  |
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                          PER-THREAD PRIVATE MEMORY                             |  |
|   |  +--------------------+   +--------------------+   +------------------------+  |  |
|   |  |     JVM STACK      |   |    PC REGISTER     |   |  NATIVE METHOD STACK   |  |  |
|   |  |  (Stack Frames:    |   |  (Current Bytecode |   |  (JNI / C++ Calls)     |  |  |
|   |  |   Local Vars, Ops) |   |   Instruction)     |   |                        |  |  |
|   |  +--------------------+   +--------------------+   +------------------------+  |  |
|   +--------------------------------------------------------------------------------+  |
+---------------------------------------------------------------------------------------+
```

---

## 2. Heap vs Stack Memory: Complete Comparison

| Dimension | Stack Memory | Heap Memory |
| :--- | :--- | :--- |
| **What is Stored?** | Method invocation frames, **primitive local variables**, and **object references** (pointers). | **All Objects, Class instances, and Arrays**. |
| **Thread Accessibility** | Private to each thread (Thread-Safe by default). | Shared across all threads (Requires synchronization). |
| **Lifetime / Deallocation** | Automatically pushed on method call and popped on method return (LIFO). | Managed by the **Garbage Collector (GC)**. |
| **Size & Speed** | Small size (typically 1MB per thread), extremely fast CPU access. | Large size (GBs), slower access due to reference indirection. |
| **Error When Full** | `java.lang.StackOverflowError` (e.g. infinite recursion). | `java.lang.OutOfMemoryError: Java heap space`. |

---

## 3. Instance vs Static Variables Memory Allocation

```java
public class MemoryAllocationDemo {
    // 1. Static Variable: Allocated in Metaspace/Class area when class is loaded (1 copy for ALL objects)
    public static String companyName = "Acme Corp";

    // 2. Instance Variable: Allocated on the HEAP inside each new object instance
    private int employeeId;

    public void calculateSalary(int bonus) {
        // 3. Local Variable: Allocated on the Thread's STACK frame (destroyed when method returns)
        int baseSalary = 5000;
        int total = baseSalary + bonus;
    }
}
```

```
+---------------------------------------------------------------------------------------+
| Variable Type       | Memory Location      | Created When?         | Destroyed When?  |
+---------------------------------------------------------------------------------------+
| **Static Variable** | Metaspace / Class    | Class is loaded by    | ClassLoader is   |
|                     | Area                 | ClassLoader           | unloaded         |
| **Instance Field**  | Heap (Inside Object) | `new ClassName()`     | Object is GC'd   |
| **Local Variable**  | Thread Stack Frame   | Method is called      | Method returns   |
+---------------------------------------------------------------------------------------+
```

---

## 4. String Constant Pool (SCP) & `intern()`

### Memory Allocation: String Literal vs `new String()`
- **String Literals (`String s = "Java"`)**: Stored in the **String Constant Pool (SCP)** inside the Heap. If `"Java"` already exists, it reuses the reference (Zero extra memory!).
- **`new String("Java")`**: Creates **TWO objects**:
  1. One in the general Heap.
  2. One in the String Constant Pool (if not already present).

```java
public class StringPoolDemo {
    public static void main(String[] args) {
        String s1 = "Hello";              // Points to SCP
        String s2 = "Hello";              // Reuses SCP reference
        String s3 = new String("Hello");  // New object in Heap outside SCP

        System.out.println(s1 == s2);      // true (Same memory address in SCP)
        System.out.println(s1 == s3);      // false (Different memory addresses)
        System.out.println(s1.equals(s3)); // true (Value equality)

        // intern() returns the canonical reference from the String Constant Pool
        String s4 = s3.intern();
        System.out.println(s1 == s4);      // true! (Now points directly to SCP)
    }
}
```

---

## 5. Garbage Collection & The Generational Hypothesis

The **Weak Generational Hypothesis** observes that **most objects die very shortly after creation** (e.g. local loop variables, temporary DTOs).

```
+---------------------------------------------------------------------------------------+
|                                    HEAP MEMORY                                        |
|                                                                                       |
|   +---------------------------------------------------+   +-----------------------+   |
|   |                 YOUNG GENERATION                  |   |    OLD GENERATION     |   |
|   |  +-----------------+  +--------+  +--------+      |   |  (Tenured Space)      |   |
|   |  |   Eden Space    |  | S0     |  | S1     |      |   |                       |   |
|   |  | (New Objects)   |  | From   |  | To     |      |   | (Long-lived objects   |   |
|   |  |                 |  | (Copy) |  | (Copy) |      |   |  surviving 15 cycles) |   |
|   |  +-----------------+  +--------+  +--------+      |   |                       |   |
|   +---------------------------------------------------+   +-----------------------+   |
|                       Minor GC                                     Major / Full GC    |
+---------------------------------------------------------------------------------------+
```

### GC Lifecycle:
1. All newly instantiated objects land in **Eden Space**.
2. When Eden fills up, **Minor GC** triggers:
   - Live objects are moved to Survivor Space **S0**.
3. Next Minor GC moves surviving objects from Eden and S0 to Survivor Space **S1**, incrementing each object's **age counter**.
4. When an object's age exceeds the threshold (default: **15 GC cycles**), it is promoted (**tenured**) to the **Old Generation**.
5. When Old Generation fills up, a **Major GC / Full GC** triggers (stop-the-world event).

---

## 6. Memory Leaks & `OutOfMemoryError` (OOM) Types

### What is a Java Memory Leak?
A memory leak occurs when objects that are **no longer needed by application logic** are still referenced by a GC Root, preventing the Garbage Collector from freeing their memory.

### Top 4 Causes of Memory Leaks:
1. **Static Collections:** Adding objects to `static List` or `static Map` without eviction.
2. **Unclosed System Resources:** Database connections, sockets, and file streams.
3. **`ThreadLocal` Variables:** In thread pools, worker threads are reused; if `threadLocal.remove()` is not called, data leaks across requests.
4. **Non-Static Inner Classes:** Holds an implicit reference to the outer enclosing class instance.

---

### The 5 Different `OutOfMemoryError` Types:

```
+-----------------------------------------------------------------------------------------------+
| OOM Subtype                             | Root Cause                                          |
+-----------------------------------------------------------------------------------------------+
| `java.lang.OutOfMemoryError: Java heap space` | Heap is full of active reachable objects      |
|                                         | (Fix: Increase `-Xmx` or resolve memory leak).      |
+-----------------------------------------+-----------------------------------------------------+
| `java.lang.OutOfMemoryError: Metaspace`  | Too many loaded classes, CGLIB/Spring proxies.      |
|                                         | (Fix: Increase `-XX:MaxMetaspaceSize`).             |
+-----------------------------------------+-----------------------------------------------------+
| `java.lang.OutOfMemoryError: GC overhead`| JVM spent >98% of CPU time in GC and freed <2% RAM  |
| `limit exceeded`                        | (Fix: Heap size too small for active workload).     |
+-----------------------------------------+-----------------------------------------------------+
| `java.lang.OutOfMemoryError: Unable to` | Operating System cannot allocate native thread stack|
| `create new native thread`              | (Fix: Too many OS platform threads; use pool/virtual)|
+-----------------------------------------+-----------------------------------------------------+
| `java.lang.OutOfMemoryError: Direct`    | Direct ByteBuffer (Netty / Off-heap) allocation     |
| `buffer memory`                         | exceeds `-XX:MaxDirectMemorySize`.                  |
+-----------------------------------------+-----------------------------------------------------+
```

---

## 7. ClassLoader Architecture & Delegation Model

Java uses a hierarchical **Parent-First Delegation Model** for class loading:

```
                  [ Bootstrap ClassLoader ]
                  (Loads core rt.jar / java.base from JDK)
                             ^
                             | (Delegates parent first)
                  [ Platform / Extension ClassLoader ]
                  (Loads extension packages from lib/ext)
                             ^
                             | (Delegates parent first)
                  [ Application / System ClassLoader ]
                  (Loads application classes from CLASSPATH)
                             ^
                             | (Delegates parent first)
                  [ Custom ClassLoader ]
                  (Plugin loaders, OSGi, Spring Boot Loader)
```

### Delegation Rule:
1. When a class request arrives at `ApplicationClassLoader`, it **delegates up** to `PlatformClassLoader`.
2. `PlatformClassLoader` **delegates up** to `BootstrapClassLoader`.
3. If `BootstrapClassLoader` cannot find the class, `PlatformClassLoader` attempts to load it.
4. If it fails, `ApplicationClassLoader` loads the class from your application classpath.
5. If none find it $\rightarrow$ Throws **`ClassNotFoundException`**.
