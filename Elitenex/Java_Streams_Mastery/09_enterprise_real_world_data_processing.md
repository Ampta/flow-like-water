# 🏢 Category 9: Enterprise Real-World Data Processing

This guide provides exhaustive, production-grade solutions for **15 Enterprise Real-World Data Processing Problems** (Q146 – Q160). Every problem is presented with an authentic interview scenario, real-world intuitive story, step-by-step strategy, and dual implementation (Normal Java vs. Java Streams).

---

## 📑 Table of Contents
- [Q146: Complex Multi-Criteria Filtering & Sorting (Interview Classic)](#q146-complex-multi-criteria-filtering--sorting-interview-classic)
- [Q147: Log File Parser & Aggregator (Interview Classic)](#q147-log-file-parser--aggregator-interview-classic)
- [Q148: E-Commerce Basket Total with Tax and Discounts (Interview Classic)](#q148-e-commerce-basket-total-with-tax-and-discounts-interview-classic)
- [Q149: Bank Account Balance Audit & AML Alert (Interview Classic)](#q149-bank-account-balance-audit--aml-alert-interview-classic)
- [Q150: CSV Data Extraction & Transformation (Interview Classic)](#q150-csv-data-extraction--transformation-interview-classic)
- [Q151: Employee Organizational Hierarchy Tree Flattening (Interview Classic)](#q151-employee-organizational-hierarchy-tree-flattening-interview-classic)
- [Q152: Stock Market VWAP Calculator (Interview Classic)](#q152-stock-market-vwap-calculator-interview-classic)
- [Q153: Multi-Threaded Parallel Stream with Custom ForkJoinPool (Interview Classic)](#q153-multi-threaded-parallel-stream-with-custom-forkjoinpool-interview-classic)
- [Q154: Real-Time Event Stream Deduplication (Interview Classic)](#q154-real-time-event-stream-deduplication-interview-classic)
- [Q155: User Session Inactivity Timeout Detector (Interview Classic)](#q155-user-session-inactivity-timeout-detector-interview-classic)
- [Q156: Inventory Reorder Threshold Alert Engine (Interview Classic)](#q156-inventory-reorder-threshold-alert-engine-interview-classic)
- [Q157: Flight Reservation Seat Availability Finder (Interview Classic)](#q157-flight-reservation-seat-availability-finder-interview-classic)
- [Q158: Customer Loyalty Point Tier Calculator (Interview Classic)](#q158-customer-loyalty-point-tier-calculator-interview-classic)
- [Q159: Health Metric Sensor Outlier Detection (Interview Classic)](#q159-health-metric-sensor-outlier-detection-interview-classic)
- [Q160: Financial Portfolio Risk Asset Allocation Summarizer (Interview Classic)](#q160-financial-portfolio-risk-asset-allocation-summarizer-interview-classic)

---

### Q146: Complex Multi-Criteria Filtering & Sorting (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a hotel booking platform, filter `List<Hotel>` for active hotels rated $\ge 4.0$ stars with price $\le \$200$/night. Sort matching hotels primary by rating descending, secondary by price ascending."*

#### 🛍️ Real-World Intuitive Story
Filtering hotel search results on Booking.com: check 4-star+ box, set max price slider to $200, and click 'Sort by Best Rating'.

#### 📥 Input & 📤 Output
- **Input**: `[Hotel("H1", 4.5, 180), Hotel("H2", 3.5, 100), Hotel("H3", 4.5, 150)]`
- **Output**: `[Hotel("H3", 4.5, 150), Hotel("H1", 4.5, 180)]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"multi-criteria filter rating price sort"*.
- **Pattern**: **filter(active && rating >= 4 && price <= 200).sorted(ratingDesc.thenComparing(priceAsc))**.

#### 🛠️ Step-by-Step Strategy
1. Stream `Hotel` objects.
2. Filter active status, rating $\ge 4.0$, and price $\le 200$.
3. Sort using `Comparator.comparingDouble(Hotel::rating).reversed().thenComparingDouble(Hotel::price)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class HotelFilterNormal {
    public record Hotel(String name, boolean active, double rating, double price) {}

    public static List<Hotel> filterHotels(List<Hotel> hotels) {
        List<Hotel> filtered = new ArrayList<>();
        for (Hotel h : hotels) {
            if (h.active() && h.rating() >= 4.0 && h.price() <= 200.0) {
                filtered.add(h);
            }
        }
        filtered.sort((h1, h2) -> {
            int r = Double.compare(h2.rating(), h1.rating());
            if (r != 0) return r;
            return Double.compare(h1.price(), h2.price());
        });
        return filtered;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class HotelFilterStream {
    public record Hotel(String name, boolean active, double rating, double price) {}

    public static List<Hotel> filterHotels(List<Hotel> hotels) {
        return hotels.stream()
            .filter(Hotel::active)
            .filter(h -> h.rating() >= 4.0)
            .filter(h -> h.price() <= 200.0)
            .sorted(Comparator.comparingDouble(Hotel::rating).reversed()
                .thenComparingDouble(Hotel::price))
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Explicit `if` logic inside loop + manual multi-step comparator.
- **Stream API**: Modern composable filter and multi-field comparator pipeline.

---

### Q147: Log File Parser & Aggregator (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"You are given a list of web server log lines `List<String>` formatted as `'TIMESTAMP LEVEL IP MESSAGE'`. Parse lines, filter level `'ERROR'`, and count total error occurrences per IP address."*

#### 🛍️ Real-World Intuitive Story
Reading server error logs to identify which IP addresses are generating broken API calls.

#### 📥 Input & 📤 Output
- **Input**: `["10:00 ERROR 192.168.1.1 500", "10:01 INFO 10.0.0.1 200", "10:02 ERROR 192.168.1.1 500"]`
- **Output**: `{192.168.1.1=2}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"log parser count error per IP"*.
- **Pattern**: **filter(line -> line.contains("ERROR")).map(extractIP).groupingBy(counting)**.

#### 🛠️ Step-by-Step Strategy
1. Stream log line strings.
2. Filter lines containing `"ERROR"`.
3. Extract IP address string (3rd space-separated token) and count frequencies.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class LogParserNormal {
    public static Map<String, Integer> countErrorsPerIp(List<String> logs) {
        Map<String, Integer> errorMap = new HashMap<>();
        for (String log : logs) {
            String[] parts = log.split("\\s+");
            if (parts.length >= 3 && "ERROR".equals(parts[1])) {
                String ip = parts[2];
                errorMap.put(ip, errorMap.getOrDefault(ip, 0) + 1);
            }
        }
        return errorMap;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LogParserStream {
    public static Map<String, Long> countErrorsPerIp(List<String> logs) {
        return logs.stream()
            .map(log -> log.split("\\s+"))
            .filter(parts -> parts.length >= 3 && "ERROR".equals(parts[1]))
            .map(parts -> parts[2])
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: String splitting inside `for` loop updating error map.
- **Stream API**: Stream map-split-filter-grouping pipeline.

---

### Q148: E-Commerce Basket Total with Tax and Discounts (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a customer checkout cart `List<CartItem>`, calculate final order total applying item discounts, 10% VAT tax, and free shipping if total exceeds `$100.00`."*

#### 🛍️ Real-World Intuitive Story
Calculating total shopping cart price at checkout: subtract coupon discounts, add state sales tax, and check if eligible for free shipping.

#### 📥 Input & 📤 Output
- **Input**: `[Item("Shirt", 50.0, 0.1), Item("Pants", 60.0, 0.0)]`
- **Output**: `115.5` ($45 + 60 = 105 \times 1.10 = 115.5$, shipping free)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"e-commerce basket tax discount shipping total"*.
- **Pattern**: **mapToDouble(price * (1 - disc)).sum() -> apply tax**.

#### 🛠️ Step-by-Step Strategy
1. Stream cart items to compute discounted subtotal.
2. Apply 10% tax rate.
3. Add $10 shipping fee if subtotal $< \$100$.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class CartTotalNormal {
    public record CartItem(String name, double price, double discount) {}

    public static double computeTotal(List<CartItem> cart) {
        double subtotal = 0;
        for (CartItem item : cart) {
            subtotal += item.price() * (1.0 - item.discount());
        }
        double tax = subtotal * 0.10;
        double shipping = subtotal >= 100.0 ? 0.0 : 10.0;
        return subtotal + tax + shipping;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;

public class CartTotalStream {
    public record CartItem(String name, double price, double discount) {}

    public static double computeTotal(List<CartItem> cart) {
        double subtotal = cart.stream()
            .mapToDouble(item -> item.price() * (1.0 - item.discount()))
            .sum();

        double tax = subtotal * 0.10;
        double shipping = subtotal >= 100.0 ? 0.0 : 10.0;
        return subtotal + tax + shipping;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard subtotal accumulation loop.
- **Stream API**: `mapToDouble()` subtotal calculation.

---

### Q149: Bank Account Balance Audit & AML Alert (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a banking compliance service, scan `List<Account>` for accounts where net total transaction sum exceeds `$50,000` or single deposit exceeds `$10,000` (Anti-Money Laundering threshold)."*

#### 🛍️ Real-World Intuitive Story
Flagging suspicious bank accounts for regulatory review whenever large cash deposits are made.

#### 📥 Input & 📤 Output
- **Input**: `[Acc("A1", [12000.0]), Acc("A2", [500.0])]` $\rightarrow$ **Output**: `["A1"]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"AML bank account fraud alert"*.
- **Pattern**: **filter(acc -> sum > 50000 || txns.anyMatch(x -> x > 10000))**.

#### 🛠️ Step-by-Step Strategy
1. Stream bank account records.
2. Filter accounts where any single deposit $> 10000$ or total sum $> 50000$.
3. Collect account numbers to list.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class BankAMLNormal {
    public record Account(String accNo, List<Double> txns) {}

    public static List<String> findFlaggedAccounts(List<Account> accounts) {
        List<String> flagged = new ArrayList<>();
        for (Account acc : accounts) {
            double total = 0;
            boolean hasLargeSingle = false;
            for (double t : acc.txns()) {
                total += t;
                if (t > 10000.0) hasLargeSingle = true;
            }
            if (total > 50000.0 || hasLargeSingle) {
                flagged.add(acc.accNo());
            }
        }
        return flagged;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class BankAMLStream {
    public record Account(String accNo, List<Double> txns) {}

    public static List<String> findFlaggedAccounts(List<Account> accounts) {
        return accounts.stream()
            .filter(acc -> {
                double total = acc.txns().stream().mapToDouble(Double::doubleValue).sum();
                boolean largeSingle = acc.txns().stream().anyMatch(t -> t > 10000.0);
                return total > 50000.0 || largeSingle;
            })
            .map(Account::accNo)
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Single loop checking total sum and max single deposit condition.
- **Stream API**: Declarative filter predicate using inner stream aggregations.

---

### Q150: CSV Data Extraction & Transformation (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Read raw CSV string lines representing user profiles `'ID,NAME,AGE,CITY'`, parse valid rows into `User` objects, filtering out invalid header rows and users aged under 18."*

#### 🛍️ Real-World Intuitive Story
Parsing a downloaded CSV spreadsheet file into Java DTO objects for database ingestion.

#### 📥 Input & 📤 Output
- **Input**: `["ID,NAME,AGE,CITY", "1,Alice,25,NY", "2,Bob,16,LA"]` $\rightarrow$ **Output**: `[User(1, "Alice", 25, "NY")]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"CSV string line extraction transform object"*.
- **Pattern**: **filter(!isHeader).map(split).filter(age >= 18).map(User::new)**.

#### 🛠️ Step-by-Step Strategy
1. Stream CSV line strings.
2. Filter header line `!line.startsWith("ID,")`.
3. Parse fields, filter age $\ge 18$, and map to `User` objects.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class CSVParserNormal {
    public record User(int id, String name, int age, String city) {}

    public static List<User> parseCSV(List<String> lines) {
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("ID,")) continue;
            String[] parts = line.split(",");
            if (parts.length == 4) {
                int age = Integer.parseInt(parts[2]);
                if (age >= 18) {
                    users.add(new User(Integer.parseInt(parts[0]), parts[1], age, parts[3]));
                }
            }
        }
        return users;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class CSVParserStream {
    public record User(int id, String name, int age, String city) {}

    public static List<User> parseCSV(List<String> lines) {
        return lines.stream()
            .filter(line -> !line.startsWith("ID,"))
            .map(line -> line.split(","))
            .filter(parts -> parts.length == 4)
            .filter(parts -> Integer.parseInt(parts[2]) >= 18)
            .map(parts -> new User(
                Integer.parseInt(parts[0]),
                parts[1],
                Integer.parseInt(parts[2]),
                parts[3]
            ))
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard line parsing loop with `continue` header skips.
- **Stream API**: Modern line transformation stream pipeline.

---

### Q151: Employee Organizational Hierarchy Tree Flattening (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a root manager employee object `Employee` who contains a `List<Employee> directReports`, recursively flatten the organizational tree into a single flat list of all company employees."*

#### 🛍️ Real-World Intuitive Story
Unrolling an organizational chart from the CEO down to entry-level interns into a single company directory list.

#### 📥 Input & 📤 Output
- **Input**: `Manager("CEO", [Manager("VP", [Emp("Dev")])])`
- **Output**: `["CEO", "VP", "Dev"]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"flatten organizational tree direct reports"*.
- **Pattern**: **Stream.concat(Stream.of(root), directReports.flatMap(this::flatten))**.

#### 🛠️ Step-by-Step Strategy
1. Stream root employee.
2. Recursively `flatMap` direct reports stream.
3. Collect all employees to list.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class OrgHierarchyNormal {
    public record Employee(String name, List<Employee> reports) {}

    public static List<Employee> flattenOrg(Employee root) {
        List<Employee> result = new ArrayList<>();
        if (root == null) return result;
        result.add(root);
        for (Employee r : root.reports()) {
            result.addAll(flattenOrg(r));
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Stream;

public class OrgHierarchyStream {
    public record Employee(String name, List<Employee> reports) {}

    public static List<Employee> flattenOrg(Employee root) {
        if (root == null) return Collections.emptyList();
        return Stream.concat(
            Stream.of(root),
            root.reports().stream().flatMap(r -> flattenOrg(r).stream())
        ).toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard recursive tree traversal helper.
- **Stream API**: Recursive `Stream.concat()` and `flatMap()` hierarchy builder.

---

### Q152: Stock Market VWAP Calculator (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Calculate the Volume Weighted Average Price (VWAP) for a stock given a list of trade executions `List<Trade>`, where $\text{VWAP} = \frac{\sum (\text{price} \times \text{volume})}{\sum \text{volume}}$."*

#### 🛍️ Real-World Intuitive Story
Calculating fair market value of stock trades weighted by trade volume size.

#### 📥 Input & 📤 Output
- **Input**: `[Trade(100.0, 10), Trade(105.0, 20)]` $\rightarrow$ **Output**: `103.33` ($1000 + 2100 = 3100 / 30$)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"VWAP volume weighted average price"*.
- **Pattern**: **sum(price * volume) / sum(volume)**.

#### 🛠️ Step-by-Step Strategy
1. Calculate sum of `price * volume` products.
2. Calculate total volume sum.
3. Return `totalProduct / totalVolume`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class VWAPNormal {
    public record Trade(double price, long volume) {}

    public static double calculateVWAP(List<Trade> trades) {
        double totalValue = 0;
        long totalVolume = 0;
        for (Trade t : trades) {
            totalValue += t.price() * t.volume();
            totalVolume += t.volume();
        }
        return totalVolume == 0 ? 0.0 : totalValue / totalVolume;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;

public class VWAPStream {
    public record Trade(double price, long volume) {}

    public static double calculateVWAP(List<Trade> trades) {
        double totalValue = trades.stream().mapToDouble(t -> t.price() * t.volume()).sum();
        long totalVolume = trades.stream().mapToLong(Trade::volume).sum();
        return totalVolume == 0 ? 0.0 : totalValue / totalVolume;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Single loop calculating trade value and volume sums.
- **Stream API**: Stream double and long mapping pipelines.

---

### Q153: Multi-Threaded Parallel Stream with Custom ForkJoinPool (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Execute a heavy batch transformation on `List<String>` in parallel using a dedicated custom `ForkJoinPool` with 4 worker threads to avoid polluting the common thread pool."*

#### 🛍️ Real-World Intuitive Story
Hiring a private team of 4 dedicated workers to process order packages without slowing down public factory operations.

#### 📥 Input & 📤 Output
- **Input**: `["item1", "item2"]` $\rightarrow$ **Output**: `["ITEM1", "ITEM2"]` processed on custom thread pool workers.

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"parallelStream custom ForkJoinPool"*.
- **Pattern**: **forkJoinPool.submit(() -> list.parallelStream().map(...).collect()).get()**.

#### 🛠️ Step-by-Step Strategy
1. Create custom `ForkJoinPool(4)`.
2. Execute parallel stream inside `customPool.submit(...).get()`.
3. Shutdown pool.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;
import java.util.concurrent.*;

public class CustomThreadPoolNormal {
    public static List<String> processParallel(List<String> items) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<String>> futures = new ArrayList<>();
        for (String item : items) {
            futures.add(executor.submit(() -> item.toUpperCase()));
        }
        List<String> result = new ArrayList<>();
        for (Future<String> f : futures) {
            result.add(f.get());
        }
        executor.shutdown();
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.concurrent.*;

public class CustomThreadPoolStream {
    public static List<String> processParallel(List<String> items) throws Exception {
        ForkJoinPool customPool = new ForkJoinPool(4);
        try {
            return customPool.submit(() ->
                items.parallelStream()
                    .map(String::toUpperCase)
                    .toList()
            ).get();
        } finally {
            customPool.shutdown();
        }
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `ExecutorService` submitting task futures manually.
- **Stream API**: `ForkJoinPool.submit()` isolates parallelStream execution safely.

---

### Q154: Real-Time Event Stream Deduplication (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a real-time event streaming pipeline, filter duplicate incoming telemetry events `List<Event>` based on event ID, keeping only the first occurrence seen."*

#### 🛍️ Real-World Intuitive Story
Filtering out duplicate network packet re-transmissions so only unique telemetry events enter the database.

#### 📥 Input & 📤 Output
- **Input**: `[Event(101, "A"), Event(101, "B"), Event(102, "C")]`
- **Output**: `[Event(101, "A"), Event(102, "C")]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"event stream deduplication by ID"*.
- **Pattern**: **filter(distinctByKey(Event::id))**.

#### 🛠️ Step-by-Step Strategy
1. Build stateful predicate `distinctByKey(keyExtractor)` backed by a `ConcurrentHashMap`.
2. Filter stream matching distinct key.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class EventDeduplicationNormal {
    public record Event(int id, String payload) {}

    public static List<Event> deduplicate(List<Event> events) {
        Set<Integer> seen = new HashSet<>();
        List<Event> unique = new ArrayList<>();
        for (Event e : events) {
            if (seen.add(e.id())) {
                unique.add(e);
            }
        }
        return unique;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class EventDeduplicationStream {
    public record Event(int id, String payload) {}

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static List<Event> deduplicate(List<Event> events) {
        return events.stream()
            .filter(distinctByKey(Event::id))
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `Set.add()` check inside loop.
- **Stream API**: Reusable stateful `distinctByKey()` stream predicate.

---

### Q155: User Session Inactivity Timeout Detector (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of user session heartbeats `List<UserSession>`, identify all users whose last activity timestamp exceeds the maximum allowed inactivity window (`30 minutes`)."*

#### 🛍️ Real-World Intuitive Story
Logging out idle online banking users after 30 minutes of inactivity to protect account security.

#### 📥 Input & 📤 Output
- **Input**: `[Session("Alice", 40), Session("Bob", 15)]`, `maxInactivity = 30` $\rightarrow$ **Output**: `["Alice"]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"user session inactivity timeout"*.
- **Pattern**: **filter(s -> s.idleMinutes > 30).map(Session::user)**.

#### 🛠️ Step-by-Step Strategy
1. Stream session records.
2. Filter idle minutes $> 30$.
3. Map to user string and collect to list.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class InactivityDetectorNormal {
    public record UserSession(String username, long idleMinutes) {}

    public static List<String> findTimedOutUsers(List<UserSession> sessions, long maxInactivity) {
        List<String> timedOut = new ArrayList<>();
        for (UserSession s : sessions) {
            if (s.idleMinutes() > maxInactivity) {
                timedOut.add(s.username());
            }
        }
        return timedOut;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class InactivityDetectorStream {
    public record UserSession(String username, long idleMinutes) {}

    public static List<String> findTimedOutUsers(List<UserSession> sessions, long maxInactivity) {
        return sessions.stream()
            .filter(s -> s.idleMinutes() > maxInactivity)
            .map(UserSession::username)
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard conditional loop filter.
- **Stream API**: Declarative `.filter().map()` pipeline.

---

### Q156: Inventory Reorder Threshold Alert Engine (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Scan warehouse inventory items `List<InventoryItem>` and return a reorder report map `Map<String, Integer>` listing items where current stock falls below reorder threshold point."*

#### 🛍️ Real-World Intuitive Story
Automatically sending restock orders to suppliers when store stock falls below minimum safety stock levels.

#### 📥 Input & 📤 Output
- **Input**: `[Item("Milk", 5, 10), Item("Bread", 20, 15)]` $\rightarrow$ **Output**: `{Milk=5}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"inventory reorder threshold alert"*.
- **Pattern**: **filter(item.quantity < item.threshold).collect(toMap)**.

#### 🛠️ Step-by-Step Strategy
1. Stream inventory items.
2. Filter quantity $<$ threshold.
3. Collect to map keying name to current stock.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class InventoryAlertNormal {
    public record InventoryItem(String name, int quantity, int threshold) {}

    public static Map<String, Integer> getReorderList(List<InventoryItem> items) {
        Map<String, Integer> reorder = new HashMap<>();
        for (InventoryItem item : items) {
            if (item.quantity() < item.threshold()) {
                reorder.put(item.name(), item.quantity());
            }
        }
        return reorder;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class InventoryAlertStream {
    public record InventoryItem(String name, int quantity, int threshold) {}

    public static Map<String, Integer> getReorderList(List<InventoryItem> items) {
        return items.stream()
            .filter(item -> item.quantity() < item.threshold())
            .collect(Collectors.toMap(InventoryItem::name, InventoryItem::quantity));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: HashMap populate loop.
- **Stream API**: `filter().collect(toMap())`.

---

### Q157: Flight Reservation Seat Availability Finder (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In an airline reservation engine, given a list of passenger seats `List<Seat>`, find all available aisle seats located in Emergency Exit rows."*

#### 🛍️ Real-World Intuitive Story
Filtering seat map on an airline app for extra legroom emergency exit aisle seats.

#### 📥 Input & 📤 Output
- **Input**: `[Seat("12A", true, true, false), Seat("12C", true, false, true)]` $\rightarrow$ **Output**: `["12C"]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"airline seat availability filter"*.
- **Pattern**: **filter(available && isExitRow && isAisle)**.

#### 🛠️ Step-by-Step Strategy
1. Stream seat records.
2. Filter available, exit row, and aisle flags.
3. Map to seat number string.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FlightSeatFinderNormal {
    public record Seat(String seatNo, boolean available, boolean window, boolean aisle) {}

    public static List<String> findExitAisleSeats(List<Seat> seats) {
        List<String> result = new ArrayList<>();
        for (Seat s : seats) {
            if (s.available() && s.aisle()) {
                result.add(s.seatNo());
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class FlightSeatFinderStream {
    public record Seat(String seatNo, boolean available, boolean window, boolean aisle) {}

    public static List<String> findExitAisleSeats(List<Seat> seats) {
        return seats.stream()
            .filter(Seat::available)
            .filter(Seat::aisle)
            .map(Seat::seatNo)
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Conditional boolean check loop.
- **Stream API**: Clean chained filter predicates.

---

### Q158: Customer Loyalty Point Tier Calculator (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a customer purchase history `List<Customer>`, classify customers into Loyalty Tiers: `'PLATINUM'` ($\ge \$10,000$), `'GOLD'` ($\ge \$5,000$), `'SILVER'` ($\ge \$1,000$), or `'BRONZE'`."*

#### 🛍️ Real-World Intuitive Story
Assigning airline frequent flyer status badges based on total money spent.

#### 📥 Input & 📤 Output
- **Input**: `[Cust("Alice", 12000), Cust("Bob", 6000)]`
- **Output**: `{PLATINUM=["Alice"], GOLD=["Bob"]}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"customer loyalty point tier calculator"*.
- **Pattern**: **groupingBy(this::calculateTier)**.

#### 🛠️ Step-by-Step Strategy
1. Define tier calculator method mapping spend to status string.
2. Stream customers and collect via `Collectors.groupingBy(tier, mapping(name, toList()))`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class CustomerTierNormal {
    public record Customer(String name, double totalSpend) {}

    private static String getTier(double spend) {
        if (spend >= 10000) return "PLATINUM";
        if (spend >= 5000) return "GOLD";
        if (spend >= 1000) return "SILVER";
        return "BRONZE";
    }

    public static Map<String, List<String>> groupCustomerTiers(List<Customer> customers) {
        Map<String, List<String>> map = new HashMap<>();
        for (Customer c : customers) {
            String tier = getTier(c.totalSpend());
            map.computeIfAbsent(tier, k -> new ArrayList<>()).add(c.name());
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class CustomerTierStream {
    public record Customer(String name, double totalSpend) {}

    private static String getTier(double spend) {
        if (spend >= 10000) return "PLATINUM";
        if (spend >= 5000) return "GOLD";
        if (spend >= 1000) return "SILVER";
        return "BRONZE";
    }

    public static Map<String, List<String>> groupCustomerTiers(List<Customer> customers) {
        return customers.stream()
            .collect(Collectors.groupingBy(
                c -> getTier(c.totalSpend()),
                Collectors.mapping(Customer::name, Collectors.toList())
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `computeIfAbsent()` tier bucket population loop.
- **Stream API**: `groupingBy` with downstream `mapping` to list.

---

### Q159: Health Metric Sensor Outlier Detection (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a medical monitoring platform, filter heart rate readings `double[] bpm` to identify statistical outliers falling outside $3$ standard deviations from the mean."*

#### 🛍️ Real-World Intuitive Story
Filtering out erratic noise spikes from heart rate monitors to alert doctors of actual cardiac anomalies.

#### 📥 Input & 📤 Output
- **Input**: `[70, 72, 71, 73, 220]` $\rightarrow$ **Output**: `[220.0]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"health sensor outlier detection stddev"*.
- **Pattern**: **filter(Math.abs(x - mean) > 3 * stdDev)**.

#### 🛠️ Step-by-Step Strategy
1. Compute mean and standard deviation of readings.
2. Filter readings where `Math.abs(r - mean) > 3 * stdDev`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class SensorOutlierNormal {
    public static List<Double> findOutliers(double[] readings) {
        if (readings == null || readings.length == 0) return Collections.emptyList();
        double sum = 0;
        for (double r : readings) sum += r;
        double mean = sum / readings.length;

        double sumSqDiff = 0;
        for (double r : readings) sumSqDiff += Math.pow(r - mean, 2);
        double stdDev = Math.sqrt(sumSqDiff / readings.length);

        List<Double> outliers = new ArrayList<>();
        for (double r : readings) {
            if (Math.abs(r - mean) > 3 * stdDev) {
                outliers.add(r);
            }
        }
        return outliers;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class SensorOutlierStream {
    public static List<Double> findOutliers(double[] readings) {
        if (readings == null || readings.length == 0) return Collections.emptyList();
        double mean = Arrays.stream(readings).average().orElse(0.0);
        double variance = Arrays.stream(readings).map(r -> Math.pow(r - mean, 2)).average().orElse(0.0);
        double stdDev = Math.sqrt(variance);

        return Arrays.stream(readings)
            .filter(r -> Math.abs(r - mean) > 3 * stdDev)
            .boxed()
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard mean, stdDev, and outlier filtering loops.
- **Stream API**: Clean sequence of streams calculating statistics and filtering outliers.

---

### Q160: Financial Portfolio Risk Asset Allocation Summarizer (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an investment portfolio `List<Asset>`, group holdings by asset class (`'EQUITY'`, `'FIXED_INCOME'`, `'CRYPTO'`), calculate total value per class, and return percentage allocation breakdown map."*

#### 🛍️ Real-World Intuitive Story
Generating a pie chart summary on Robinhood/E*TRADE showing what percentage of your total wealth is invested in Stocks vs Bonds vs Crypto.

#### 📥 Input & 📤 Output
- **Input**: `[Asset("EQUITY", 6000), Asset("CRYPTO", 4000)]`
- **Output**: `{EQUITY=60.0%, CRYPTO=40.0%}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"financial portfolio asset allocation percentage"*.
- **Pattern**: **groupingBy(type, summingDouble) -> map values to percentage**.

#### 🛠️ Step-by-Step Strategy
1. Calculate total portfolio value across all assets.
2. Group by asset type summing values.
3. Transform group dollar totals into percentage values.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class PortfolioAllocationNormal {
    public record Asset(String type, double value) {}

    public static Map<String, Double> calculateAllocation(List<Asset> assets) {
        double totalPortfolio = 0;
        Map<String, Double> totalsMap = new HashMap<>();
        for (Asset a : assets) {
            totalPortfolio += a.value();
            totalsMap.put(a.type(), totalsMap.getOrDefault(a.type(), 0.0) + a.value());
        }

        Map<String, Double> percentageMap = new HashMap<>();
        for (Map.Entry<String, Double> entry : totalsMap.entrySet()) {
            percentageMap.put(entry.getKey(), (entry.getValue() / totalPortfolio) * 100.0);
        }
        return percentageMap;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class PortfolioAllocationStream {
    public record Asset(String type, double value) {}

    public static Map<String, Double> calculateAllocation(List<Asset> assets) {
        double totalPortfolio = assets.stream().mapToDouble(Asset::value).sum();
        if (totalPortfolio == 0) return Collections.emptyMap();

        return assets.stream()
            .collect(Collectors.groupingBy(
                Asset::type,
                Collectors.summingDouble(Asset::value)
            ))
            .entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> (e.getValue() / totalPortfolio) * 100.0
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Two sequential accumulation loops calculating totals and percentage ratios.
- **Stream API**: `groupingBy` with downstream `summingDouble` followed by ratio mapping stream.
