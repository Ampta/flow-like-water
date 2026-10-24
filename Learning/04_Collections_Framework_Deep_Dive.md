# Chapter 4: Collections Framework Master Guide: Internals, Comparisons & Thread Safety

---

## 1. Java Collection Framework Hierarchy

```
                                [Iterable<T>]
                                      |
                                [Collection<T>]
                        /             |             \
                   [List<T>]      [Set<T>]       [Queue<T>]
                   /    |   \         |   \             \
          ArrayList  Vector  LinkedList  HashSet  TreeSet   Deque / ArrayDeque
                       |                     |
                     Stack             LinkedHashSet

                                  [Map<K, V>]  (Separate hierarchy, not extending Collection)
                                 /     |     \
                          HashMap  TreeMap  Hashtable
                             |                  |
                       LinkedHashMap        Properties
```

---

## 2. List vs Set: Architecture & Differences

| Dimension | `List<E>` | `Set<E>` |
| :--- | :--- | :--- |
| **Duplicates** | 🟢 **Allowed** (Can store duplicate elements). | 🔴 **Forbidden** (Stores strictly unique elements). |
| **Order Preservation** | 🟢 **Maintains Insertion Order** (Index-based access: `get(0)`). | 🔴 **No guarantee** (`HashSet`), unless using `LinkedHashSet` (Insertion order) or `TreeSet` (Sorted order). |
| **Null Elements** | 🟢 **Multiple `null` values allowed**. | `HashSet`/`LinkedHashSet` allow **1 `null`**. `TreeSet` **forbids `null`** (`NullPointerException`). |
| **Positional Access** | 🟢 Has index-based methods (`get(i)`, `set(i, val)`, `indexOf(val)`). | 🔴 **No index-based access** (Must use iterator, enhanced for-loop, or streams). |
| **Underlying Structure**| Dynamic arrays or doubly-linked nodes. | Internally backed by a `Map` instance (e.g. `HashSet` uses `HashMap`). |

### 🟢 Allowed vs 🔴 Forbidden Matrix (List vs Set)
| Operation | `List` (e.g., `ArrayList`) | `Set` (e.g., `HashSet`) |
| :--- | :--- | :--- |
| `add(duplicateItem)` | 🟢 **Allowed** (Returns `true`, adds item) | 🟢 **Allowed syntax**, but returns `false` & does **NOT** add. |
| `get(0)` (Index access) | 🟢 **Allowed** | 🔴 **FORBIDDEN** (Compile Error: `get(int)` does not exist on Set). |
| `add(null)` | 🟢 **Allowed** (Unlimited) | 🟢 **Allowed** (Max 1 in `HashSet`; 🔴 **FORBIDDEN** in `TreeSet`). |

---

## 3. ArrayList vs LinkedList

```
ArrayList (Contiguous Memory Block):
[ Elem 0 ] [ Elem 1 ] [ Elem 2 ] [ Elem 3 ] [ ... ]
  O(1) Random Access via index formula: baseAddress + (index * elementSize)

LinkedList (Doubly Linked Nodes scattered in Heap):
[Node: prev|data|next] <---> [Node: prev|data|next] <---> [Node: prev|data|next]
```

### Performance & Complexity Matrix

| Operation | `ArrayList` | `LinkedList` | Why? |
| :--- | :--- | :--- | :--- |
| `get(index)` | **$O(1)$** (Instant) | **$O(N)$** (Traversal) | ArrayList calculates memory address directly; LinkedList must traverse node-by-node. |
| `add(element)` (at end) | **$O(1)$ amortized** | **$O(1)$** | ArrayList appends to array; LinkedList appends to tail pointer. |
| `add(0, element)` (at head)| **$O(N)$** | **$O(1)$** | ArrayList must shift all elements right via `System.arraycopy`; LinkedList just updates head pointer. |
| `remove(index)` (in middle)| **$O(N)$** | **$O(N)$ to find + $O(1)$ to unhook** | Both take $O(N)$, but ArrayList has less memory overhead. |
| **Memory Overhead** | **Low** (Array storage + unused capacity) | **High** (Every node allocates 24–32 bytes for `prev`, `next`, and `data` pointers). |

> [!TIP]
> **Production Rule:** In 99% of enterprise applications, **`ArrayList` outperforms `LinkedList`** even for insertions, because arrays are contiguous in memory, maximizing **CPU L1/L2 Cache locality** (Hardware spatial prefetching).

---

## 4. `HashMap` vs `Hashtable`

| Feature | `HashMap` | `Hashtable` (Legacy) |
| :--- | :--- | :--- |
| **Thread Safety** | 🔴 **Not Thread-Safe** (Fast, unsynchronized). | 🟢 **Thread-Safe** (Every method is `synchronized`). |
| **Null Keys / Values** | 🟢 Allows **1 `null` key** and multiple `null` values. | 🔴 **FORBIDDEN** (Throws `NullPointerException` on `null` key or value). |
| **Performance** | High throughput. | Low throughput (single lock serializes all threads). |
| **Modern Replacement** | Use `HashMap` for single-thread; use `ConcurrentHashMap` for multi-thread. | **Deprecated in practice** (Do NOT use `Hashtable` in modern code). |

---

## 5. `HashSet` Internals & Why Sets Disallow Duplicates

### Q: How does `HashSet` work internally?
A `HashSet` does **NOT** have its own storage engine! Internally, it is simply a wrapper around a `HashMap`:

```java
public class HashSet<E> implements Set<E> {
    private transient HashMap<E, Object> map;
    
    // Dummy value associated with every key in the backing Map
    private static final Object PRESENT = new Object();

    public HashSet() {
        map = new HashMap<>();
    }

    public boolean add(E e) {
        // Delegates to HashMap.put(key, value)
        return map.put(e, PRESENT) == null;
    }
}
```

### Q: Why doesn't `Set` allow duplicates?
1. When you call `set.add(item)`, it executes `map.put(item, PRESENT)`.
2. In `HashMap.put(K, V)`, if the key already exists (same `hash` and `equals() == true`), it **replaces the value** and returns the **previous value** (`PRESENT`).
3. Since the return value is not `null`, `map.put(e, PRESENT) == null` evaluates to `false`.
4. The item is not duplicated because `HashMap` keys are mathematically unique!

---

## 6. `LinkedHashSet` vs `HashSet` vs `TreeSet`

```
+-----------------------------------------------------------------------------------------+
| Class            | Ordering Guarantee     | Underlying Data Structure    | Time Complexity |
+-----------------------------------------------------------------------------------------+
| `HashSet`        | No ordering guarantee  | `HashMap` (Array + Nodes)     | O(1) add/get    |
| `LinkedHashSet`  | **Insertion Order**    | `LinkedHashMap` (Map + Doubly | O(1) add/get    |
|                  |                        | Linked List through entries)  |                 |
| `TreeSet`        | **Sorted / Natural**   | `TreeMap` (Red-Black Tree)    | O(log N)        |
+-----------------------------------------------------------------------------------------+
```

### Code Example: Ordering Differences
```java
import java.util.*;

public class SetComparisonDemo {
    public static void main(String[] args) {
        Set<String> hashSet = new HashSet<>(List.of("Zebra", "Apple", "Mango"));
        Set<String> linkedHashSet = new LinkedHashSet<>(List.of("Zebra", "Apple", "Mango"));
        Set<String> treeSet = new TreeSet<>(List.of("Zebra", "Apple", "Mango"));

        System.out.println("HashSet (Unpredictable): " + hashSet);
        // Output: [Apple, Zebra, Mango] (or hash-dependent order)

        System.out.println("LinkedHashSet (Insertion Order): " + linkedHashSet);
        // Output: [Zebra, Apple, Mango]

        System.out.println("TreeSet (Sorted Order): " + treeSet);
        // Output: [Apple, Mango, Zebra]
    }
}
```

---

## 7. `Iterator` vs `ListIterator`

| Feature | `Iterator<E>` | `ListIterator<E>` |
| :--- | :--- | :--- |
| **Applicability** | Can traverse **any `Collection`** (`List`, `Set`, `Queue`). | Can traverse **`List` only** (`ArrayList`, `LinkedList`). |
| **Direction** | **Forward only** (`hasNext()`, `next()`). | **Bi-directional** (`hasNext()`, `next()`, `hasPrevious()`, `previous()`). |
| **Modifications** | Can only remove elements (`remove()`). | Can remove (`remove()`), modify (`set(e)`), and insert (`add(e)`). |
| **Index Access** | Cannot get current index. | Can retrieve `nextIndex()` and `previousIndex()`. |

---

## 8. Fail-Fast vs Fail-Safe (Weakly Consistent) Iterators

### How Fail-Fast Works (`modCount`):
Collections like `ArrayList` and `HashMap` maintain an internal counter:
```java
protected transient int modCount = 0;
```
1. Every time you add, remove, or clear elements, `modCount++` is incremented.
2. When an `Iterator` is created, it takes a snapshot: `expectedModCount = modCount`.
3. On every `iterator.next()` call, it checks:
   ```java
   if (modCount != expectedModCount)
       throw new ConcurrentModificationException();
   ```

### Fail-Fast vs Fail-Safe Matrix
| Dimension | Fail-Fast | Fail-Safe / Weakly Consistent |
| :--- | :--- | :--- |
| **Collections** | `ArrayList`, `HashSet`, `HashMap`, `Vector` | `CopyOnWriteArrayList`, `ConcurrentHashMap` |
| **Mechanism** | Checks `modCount == expectedModCount`. | Operates on a **clone/snapshot** or direct volatile nodes. |
| **Exception** | Throws `ConcurrentModificationException` immediately. | **Never** throws `ConcurrentModificationException`. |
| **Memory Overhead** | Low (Zero copy). | Higher (e.g. `CopyOnWriteArrayList` copies entire underlying array on write). |

```java
// 🔴 FAIL-FAST BUG:
List<String> list = new ArrayList<>(List.of("A", "B", "C"));
for (String item : list) {
    if (item.equals("B")) {
        list.remove(item); // RUNTIME ERROR: ConcurrentModificationException!
    }
}

// 🟢 FIX 1: Use Iterator.remove()
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("B")) {
        it.remove(); // Valid! Updates both modCount and expectedModCount
    }
}

// 🟢 FIX 2: Use removeIf (Java 8+)
list.removeIf(item -> item.equals("B"));
```

---

## 9. Thread-Safe Collections in Java

```
                            [Thread-Safe Collections]
                           /            |           \
         [Concurrent Collections]  [Copy-On-Write]  [Synchronized Wrappers]
         - ConcurrentHashMap        - CopyOnWriteArrayList  - Collections.synchronizedList()
         - ConcurrentLinkedQueue    - CopyOnWriteArraySet   - Collections.synchronizedMap()
         - BlockingQueue (Linked/Array)
```

### When to use which thread-safe collection?
1. **`ConcurrentHashMap`**: High concurrent read/write map operations.
2. **`CopyOnWriteArrayList`**: High-read, extremely rare write scenarios (e.g., event listener lists).
3. **`BlockingQueue` (`ArrayBlockingQueue` / `LinkedBlockingQueue`)**: Producer-Consumer patterns with worker threads.
4. **`Collections.synchronizedList(list)`**: Legacy single-lock wrapper (low concurrency).
