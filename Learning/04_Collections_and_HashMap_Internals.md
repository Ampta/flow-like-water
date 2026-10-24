# Chapter 4: Collections Framework, HashMap Internals & Sorting

---

## 1. Internal Working of `HashMap` (Java 8+)

### Core Architecture & Memory Structure
A `HashMap` stores key-value pairs inside an internal array of nodes called `table`:
```java
transient Node<K,V>[] table;
```
- **Default Initial Capacity:** 16 (`1 << 4`)
- **Default Load Factor:** 0.75
- **Threshold for Resizing:** $\text{capacity} \times \text{loadFactor}$ (e.g., $16 \times 0.75 = 12$)

```
Buckets (table array)
[ 0 ] ---> [ Node: "Apple"=10 ] -> [ Node: "Banana"=20 ] (Collision / Linked List)
[ 1 ] ---> null
[ 2 ] ---> [ Red-Black Tree Node (when >= 8 nodes and capacity >= 64) ]
[ 3 ] ---> [ Node: "Cat"=30 ]
...
[ 15] ---> null
```

---

### Step-by-Step: What happens during `map.put(key, value)`?

1. **Calculate Hash:**
   Java computes the hash using the key's `hashCode()` and applies a bitwise XOR spreader function:
   ```java
   static final int hash(Object key) {
       int h;
       return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
   }
   ```
   *(The `h >>> 16` XORs the higher 16 bits with lower 16 bits to prevent collisions when table size is small).*

2. **Calculate Bucket Index:**
   ```java
   int index = (table.length - 1) & hash;
   ```
   *(Bitwise AND `&` is mathematically identical to `hash % table.length` when length is a power of 2, but executes in 1 CPU cycle).*

3. **Handle Insertion & Collisions:**
   - **Case A (Bucket is empty):** A new `Node<K,V>` is created and placed at `table[index]`.
   - **Case B (Bucket has existing nodes - Collision):**
     Java traverses the bucket nodes checking:
     ```java
     if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
     ```
     - **If Duplicate Key is found:** The old value is swapped/overwritten with the new value, and the old value is returned (`Key` is NOT replaced).
     - **If Key is different (Hash Collision):** The new node is appended to the **end** of the Linked List (Java 8 uses tail insertion; Java 7 used head insertion).

4. **Treeification (Converting List to Red-Black Tree):**
   - If the number of nodes in a single bucket reaches **`TREEIFY_THRESHOLD = 8`**:
     - If `table.length < 64` (`MIN_TREEIFY_CAPACITY`), it simply **resizes (doubles)** the table to redistribute entries across more buckets.
     - If `table.length >= 64`, the bucket converts from `LinkedList` ($O(N)$ lookup) into a **Red-Black Tree** ($O(\log N)$ lookup).
   - If during resizing the nodes in a tree bucket decrease to **`UNTREEIFY_THRESHOLD = 6`**, the tree reverts back to a `LinkedList`.

---

### `HashMap` Allowed vs Forbidden Matrix
| Action | Allowed / Forbidden | Details |
| :--- | :--- | :--- |
| Storing `null` key | 🟢 **ALLOWED (Max 1)** | Stored in bucket index `0` because `hash(null) == 0`. |
| Storing `null` values | 🟢 **ALLOWED (Unlimited)**| Any key can hold a `null` value. |
| Duplicate keys | 🟢 **ALLOWED** | Overwrites the previous value and returns the old value. |
| Concurrent modifications from multiple threads | 🔴 **FORBIDDEN (Not Thread-Safe)** | Data corruption, missing updates, or corrupted linked lists during resize. |
| Modifying map via `map.remove()` inside enhanced for-loop | 🔴 **FORBIDDEN** | **Runtime Failure:** Throws `ConcurrentModificationException` (**Fail-Fast Iterator**). |
| Modifying map via `iterator.remove()` | 🟢 **ALLOWED** | Safe way to delete elements during iteration. |
| Using mutable keys whose fields change after `put()` | 🔴 **DANGEROUS ANTI-PATTERN** | Object cannot be retrieved later via `map.get()` because its hashcode changed. |

---

## 2. The `equals()` and `hashCode()` Contract

### The 3 Rules of the Contract:
1. If `o1.equals(o2) == true`, then `o1.hashCode() == o2.hashCode()` **MUST** always be true.
2. If `o1.hashCode() == o2.hashCode()`, `o1.equals(o2)` is **NOT** required to be true (This is simply a hash collision).
3. Consistent execution: Multiple invocations of `hashCode()` on the same object must return the same integer provided no fields change.

### What breaks if you override only `equals()`?

```java
import java.util.*;

class Employee {
    private int id;
    private String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee emp = (Employee) o;
        return id == emp.id && Objects.equals(name, emp.name);
    }
    // OOPS! Forgot to override hashCode()!
}

public class ContractViolationDemo {
    public static void main(String[] args) {
        Map<Employee, String> map = new HashMap<>();
        
        Employee e1 = new Employee(101, "Alice");
        Employee e2 = new Employee(101, "Alice");

        map.put(e1, "Software Engineer");

        // e1.equals(e2) is TRUE, BUT their default Object.hashCode() are DIFFERENT!
        // map.get(e2) calculates e2's hash, goes to a DIFFERENT bucket, and returns NULL!
        System.out.println("Result: " + map.get(e2)); // PRINTS: null (BUG!)
    }
}
```

> [!CAUTION]
> **Why Keys Must Be Immutable (e.g., `String`):** If you use a mutable object as a `HashMap` key and modify its fields after inserting, its `hashCode()` changes. When you later try `map.get(key)`, it checks the wrong bucket index and **cannot find the object**, creating a silent memory leak!

---

## 3. `HashMap` vs `ConcurrentHashMap` vs `Collections.synchronizedMap`

```
+---------------------------------------------------------------------------------------+
| Feature               | HashMap            | Collections.synchronizedMap | ConcurrentHashMap  |
+---------------------------------------------------------------------------------------+
| Thread Safe?          | NO                 | YES (Coarse-grained)        | YES (Fine-grained) |
| Locking Mechanism     | None               | Single lock on whole map    | CAS + Bucket Lock  |
| Null Keys/Values?     | 1 null key allowed | Allowed                     | STRICTLY FORBIDDEN |
| Iterator Type         | Fail-Fast          | Fail-Fast (Must sync block) | Weakly Consistent  |
| Concurrency Level     | Low / Single Thread| Low (All threads queue up)  | Extremely High     |
+---------------------------------------------------------------------------------------+
```

### How `ConcurrentHashMap` Works Internally (Java 8+):
1. **Empty Bucket Insertion:** Uses non-blocking **CAS (Compare-And-Swap)** instructions via `Unsafe` to insert the first node without taking any lock.
2. **Collision Insertion:** Takes a `synchronized` lock **only on the First Node of that specific bucket**. Other buckets remain completely unlocked for other threads.
3. **Reads (`get()`):** **Lock-free!** Values and node pointers are marked `volatile`, guaranteeing immediate visibility across CPU cores without locking overhead.

### Why are `null` keys and values prohibited in `ConcurrentHashMap`?
In single-threaded `HashMap`, if `map.get(key)` returns `null`, you can call `map.containsKey(key)` to check if the key was mapped to `null` or absent.
In a multi-threaded `ConcurrentHashMap`, another thread could add, delete, or modify the key between `get()` and `containsKey()`, creating an **irreconcilable race condition (ambiguity)**. Hence, `null` is forbidden.

### `ConcurrentHashMap` Allowed vs Forbidden Matrix:
| Action | `HashMap` | `ConcurrentHashMap` |
| :--- | :--- | :--- |
| Insert `null` key | 🟢 **ALLOWED** | 🔴 **FORBIDDEN (`NullPointerException`)** |
| Insert `null` value | 🟢 **ALLOWED** | 🔴 **FORBIDDEN (`NullPointerException`)** |
| Multi-threaded reads/writes | 🔴 **Unsafe** | 🟢 **Fully Thread-Safe (Lock-free reads + Fine-grained bucket sync)** |
| Iterating while another thread mutates | 🔴 **`ConcurrentModificationException`** | 🟢 **ALLOWED (Weakly Consistent / Fail-Safe)** |
| Atomic read-and-modify (`computeIfAbsent`) | 🔴 **Unsafe** | 🟢 **Atomic & Safe** |

---

## 4. `Comparable` vs `Comparator`

### Comparison Table

| Aspect | `Comparable<T>` | `Comparator<T>` |
| :--- | :--- | :--- |
| **Package** | `java.lang` | `java.util` |
| **Method** | `public int compareTo(T o)` | `public int compare(T o1, T o2)` |
| **Sorting Logic** | **Natural / Default ordering** embedded inside the domain class. | **Custom / Multiple ordering strategies** external to the class. |
| **Modification** | Must modify the source class. | Does not require modifying the source class. |
| **Invocation** | `Collections.sort(list)` | `Collections.sort(list, comparator)` or `list.sort(comparator)` |

---

### Code Example: Production Sorting & Java 8 Chaining

```java
import java.util.*;

class Product implements Comparable<Product> {
    private String name;
    private double price;
    private int rating;

    public Product(String name, double price, int rating) {
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getRating() { return rating; }

    // Natural ordering: Sort by Price ascending
    @Override
    public int compareTo(Product other) {
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return name + " ($" + price + ", " + rating + "★)";
    }
}

public class SortingDemo {
    public static void main(String[] args) {
        List<Product> products = new ArrayList<>(List.of(
            new Product("Laptop", 1200.0, 5),
            new Product("Mouse", 25.0, 4),
            new Product("Keyboard", 75.0, 4),
            new Product("Monitor", 300.0, 5),
            new Product("Headphones", 75.0, 5)
        ));

        // 1. Natural Sort via Comparable (Price Ascending)
        Collections.sort(products);
        System.out.println("Sorted by Price: " + products);

        // 2. Custom Sort via Comparator: Rating Descending, then Price Ascending, then Name Alphabetical
        Comparator<Product> multiComparator = Comparator
            .comparingInt(Product::getRating).reversed()
            .thenComparingDouble(Product::getPrice)
            .thenComparing(Product::getName);

        products.sort(multiComparator);
        System.out.println("Multi-Field Sorted: " + products);
    }
}
```

> [!WARNING]
> **The Integer Subtraction Overflow Bug:**
> Never write `return o1.id - o2.id;` for comparison. If `o1.id` is `Integer.MIN_VALUE` (-2,147,483,648) and `o2.id` is positive, integer overflow will wrap around and return a positive number, completely corrupting the sorting order!
> **Always use:** `Integer.compare(o1.id, o2.id)`.

### `Comparable` & `Comparator` Allowed vs Forbidden Matrix:
| Capability | `Comparable<T>` | `Comparator<T>` |
| :--- | :--- | :--- |
| Defining multiple sorting strategies for same class | 🔴 **FORBIDDEN (Only 1 `compareTo`)** | 🟢 **ALLOWED (Unlimited Comparators)** |
| Sorting without modifying original class source code | 🔴 **FORBIDDEN** | 🟢 **ALLOWED (External Comparator)** |
| Chaining sort rules (`thenComparing`) | 🔴 **Not supported** | 🟢 **ALLOWED (`Comparator.comparing(...).thenComparing(...)`)** |
| Using lambda expressions / method references | 🔴 **No** | 🟢 **ALLOWED (`Comparator.comparing(Student::getName)`)** |
| Direct integer subtraction (`a - b`) | 🔴 **FORBIDDEN (Overflow Bug)** | 🔴 **FORBIDDEN (Use `Integer.compare()`)** |
