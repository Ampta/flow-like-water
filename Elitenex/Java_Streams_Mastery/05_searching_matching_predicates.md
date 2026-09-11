# 🔎 Category 5: Searching, Matching & Predicates

This guide provides exhaustive, production-grade solutions for **20 Searching, Matching & Predicates Problems** (Q79 – Q98). Every problem is presented with an authentic interview scenario, real-world intuitive story, step-by-step strategy, and dual implementation (Normal Java vs. Java Streams).

---

## 📑 Table of Contents
- [Q79: First Non-Repeating Character (LeetCode #387)](#q79-first-non-repeating-character-leetcode-387)
- [Q80: Find Any Match vs Find First (Interview Classic)](#q80-find-any-match-vs-find-first-interview-classic)
- [Q81: Check if All Elements Satisfy Predicate (allMatch) (Interview Classic)](#q81-check-if-all-elements-satisfy-predicate-allmatch-interview-classic)
- [Q82: Check if Any Element Satisfies Predicate (anyMatch) (Interview Classic)](#q82-check-if-any-element-satisfies-predicate-anymatch-interview-classic)
- [Q83: Check if No Element Satisfies Predicate (noneMatch) (Interview Classic)](#q83-check-if-no-element-satisfies-predicate-nonematch-interview-classic)
- [Q84: Linear Search using Stream API (Interview Classic)](#q84-linear-search-using-stream-api-interview-classic)
- [Q85: Find First Even Number in Stream (Interview Classic)](#q85-find-first-even-number-in-stream-interview-classic)
- [Q86: Search for Element in 2D Array (LeetCode #74 Variation)](#q86-search-for-element-in-2d-array-leetcode-74-variation)
- [Q87: Find Maximum Difference Between Two Elements (LeetCode #121 Variation)](#q87-find-maximum-difference-between-two-elements-leetcode-121-variation)
- [Q88: Search for Employee by Id Optional (Interview Classic)](#q88-search-for-employee-by-id-optional-interview-classic)
- [Q89: Search First Missing Positive Number (LeetCode #41)](#q89-search-first-missing-positive-number-leetcode-41)
- [Q90: Search Element Greater Than Threshold (Interview Classic)](#q90-search-element-greater-than-threshold-interview-classic)
- [Q91: Check Contains Substring Case-Insensitive (Interview Classic)](#q91-check-contains-substring-case-insensitive-interview-classic)
- [Q92: Find First Duplicate Number (LeetCode #287)](#q92-find-first-duplicate-number-leetcode-287)
- [Q93: Search Range / Pair Summing to Target (LeetCode #1)](#q93-search-range--pair-summing-to-target-leetcode-1)
- [Q94: Find First Element Starting With Specific Prefix (Interview Classic)](#q94-find-first-element-starting-with-specific-prefix-interview-classic)
- [Q95: Search for Valid IP Address Strings (LeetCode #93 Variation)](#q95-search-for-valid-ip-address-strings-leetcode-93-variation)
- [Q96: Find Last Element in Stream (Interview Classic)](#q96-find-last-element-in-stream-interview-classic)
- [Q97: Search Prime Number in List (Interview Classic)](#q97-search-prime-number-in-list-interview-classic)
- [Q98: Find First Negative Number in Window (Sliding Window Basic)](#q98-find-first-negative-number-in-window-sliding-window-basic)

---

### Q79: First Non-Repeating Character (LeetCode #387)

#### 📄 Real-World Interview Problem Statement
> *"Given a string `s`, find the first non-repeating character in it and return its index. If it does not exist, return `-1`."*

#### 🛍️ Real-World Intuitive Story
Imagine standing in a line of people holding letters. You scan from left to right and find the very first person holding a letter that nobody else in the room possesses.

#### 📥 Input & 📤 Output
- **Input**: `s = "leetcode"` $\rightarrow$ **Output**: `0` ('l' appears only once)
- **Input**: `s = "loveleetcode"` $\rightarrow$ **Output**: `2` ('v' appears only once)

#### 6. 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"first unique character"*, *"first non-repeating index"*.
- **Pattern**: **LinkedHashMap Frequency Map / IntStream Index FindFirst**.

#### 🛠️ Step-by-Step Strategy
1. Build character frequency map preserving insertion order.
2. Stream indices `0` to `s.length() - 1`.
3. Find first index `i` where character count equals 1.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FirstUniqueCharNormal {
    public static int firstUniqChar(String s) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : s.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < s.length(); i++) {
            if (counts.get(s.charAt(i)) == 1) return i;
        }
        return -1;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FirstUniqueCharStream {
    public static int firstUniqChar(String s) {
        Map<Character, Long> counts = s.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));

        return IntStream.range(0, s.length())
            .filter(i -> counts.get(s.charAt(i)) == 1)
            .findFirst()
            .orElse(-1);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard frequency map followed by sequential index scan.
- **Stream API**: `LinkedHashMap` collector paired with `IntStream.range().findFirst()`.

---

### Q80: Find Any Match vs Find First (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a parallel processing microservice searching for any available server instance `Server`, compare `findFirst()` (which preserves deterministic order) vs `findAny()` (which returns any matching element faster in parallel streams)."*

#### 🛍️ Real-World Intuitive Story
Imagine asking a classroom of students to raise their hands if they speak Spanish. `findFirst()` insists on picking the student closest to the front door, while `findAny()` picks whichever student speaks up first.

#### 📥 Input & 📤 Output
- **Input**: `[Server("S1"), Server("S2"), Server("S3")]`
- **Output**: `findFirst()` $\rightarrow$ `S1`, `findAny()` $\rightarrow$ `S1` or `S2` (in parallel mode)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"findFirst vs findAny"*, *"parallel stream performance search"*.
- **Pattern**: **Stream.findFirst() vs Stream.findAny()**.

#### 🛠️ Step-by-Step Strategy
1. Stream server list.
2. Filter active servers.
3. Call `.findFirst()` for order-bound result, or `.findAny()` for parallel performance.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class FindFirstVsAnyNormal {
    public record Server(String name, boolean active) {}

    public static Server findFirstActive(List<Server> servers) {
        for (Server s : servers) {
            if (s.active()) return s;
        }
        return null;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;
import java.util.Optional;

public class FindFirstVsAnyStream {
    public record Server(String name, boolean active) {}

    public static Optional<Server> findFirstActive(List<Server> servers) {
        return servers.stream()
            .filter(Server::active)
            .findFirst();
    }

    public static Optional<Server> findAnyActiveParallel(List<Server> servers) {
        return servers.parallelStream()
            .filter(Server::active)
            .findAny();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Linear loop returns first active instance sequentially.
- **Stream API**: `findAny()` in parallel streams achieves faster short-circuit performance.

---

### Q81: Check if All Elements Satisfy Predicate (allMatch) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"You are designing a data compliance validator. Verify whether all transactions in `List<Transaction>` have an amount strictly greater than `$0.00`."*

#### 🛍️ Real-World Intuitive Story
Imagine a security guard inspecting passports at border control. If even a single passport is invalid, the guard rejects the whole group.

#### 📥 Input & 📤 Output
- **Input**: `[10.0, 50.0, 100.0]` $\rightarrow$ **Output**: `true`
- **Input**: `[10.0, -5.0, 100.0]` $\rightarrow$ **Output**: `false`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"all elements satisfy predicate"*, *"allMatch"*.
- **Pattern**: **Stream.allMatch(predicate)**.

#### 🛠️ Step-by-Step Strategy
1. Stream double values.
2. Call `stream.allMatch(val -> val > 0.0)`. Short-circuits on first `false`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class AllMatchNormal {
    public static boolean allPositive(List<Double> amounts) {
        for (double val : amounts) {
            if (val <= 0.0) return false;
        }
        return true;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;

public class AllMatchStream {
    public static boolean allPositive(List<Double> amounts) {
        return amounts.stream()
            .allMatch(val -> val > 0.0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Early return `false` inside `for` loop.
- **Stream API**: `allMatch()` provides declarative short-circuit boolean evaluation.

---

### Q82: Check if Any Element Satisfies Predicate (anyMatch) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a fraud detection engine, verify if any transaction in `List<Transaction>` exceeds `$10,000.00` to trigger a security alert."*

#### 🛍️ Real-World Intuitive Story
Imagine scanning a room full of luggage for contraband. The moment the scanner detects one illegal item, an alarm sounds immediately.

#### 📥 Input & 📤 Output
- **Input**: `[100.0, 500.0, 15000.0]` $\rightarrow$ **Output**: `true`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"check if any element matches"*, *"anyMatch"*.
- **Pattern**: **Stream.anyMatch(predicate)**.

#### 🛠️ Step-by-Step Strategy
1. Stream amounts.
2. Call `stream.anyMatch(val -> val > 10000.0)`. Short-circuits on first `true`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class AnyMatchNormal {
    public static boolean hasFraud(List<Double> amounts) {
        for (double val : amounts) {
            if (val > 10000.0) return true;
        }
        return false;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;

public class AnyMatchStream {
    public static boolean hasFraud(List<Double> amounts) {
        return amounts.stream()
            .anyMatch(val -> val > 10000.0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Sequential `for` loop with early return `true`.
- **Stream API**: `anyMatch()` short-circuits evaluation as soon as a single match is found.

---

### Q83: Check if No Element Satisfies Predicate (noneMatch) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a user registration portal, verify that a newly selected username is unique by ensuring no existing user in `List<User>` matches the requested username."*

#### 🛍️ Real-World Intuitive Story
Imagine checking a guest list at a private party. If nobody on the list matches your name, you are clear to create a new registration.

#### 📥 Input & 📤 Output
- **Input**: `existing = ["alice", "bob"]`, `newUsername = "charlie"` $\rightarrow$ **Output**: `true`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"ensure no element matches"*, *"noneMatch"*.
- **Pattern**: **Stream.noneMatch(predicate)**.

#### 🛠️ Step-by-Step Strategy
1. Stream existing usernames.
2. Call `stream.noneMatch(u -> u.equalsIgnoreCase(newUsername))`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class NoneMatchNormal {
    public static boolean isUnique(List<String> usernames, String target) {
        for (String u : usernames) {
            if (u.equalsIgnoreCase(target)) return false;
        }
        return true;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;

public class NoneMatchStream {
    public static boolean isUnique(List<String> usernames, String target) {
        return usernames.stream()
            .noneMatch(u -> u.equalsIgnoreCase(target));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard string comparison loop with early return.
- **Stream API**: `noneMatch()` evaluates absence declaratively.

---

### Q84: Linear Search using Stream API (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Search an unsorted list of integers `List<Integer>` for target value `target`. Return the 0-based index position if found, or `-1` if absent."*

#### 🛍️ Real-World Intuitive Story
Imagine walking along a row of lockers looking for locker number 42. You inspect each locker door one by one until you find it.

#### 📥 Input & 📤 Output
- **Input**: `nums = [10, 25, 42, 88]`, `target = 42` $\rightarrow$ **Output**: `2`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"linear search stream index"*.
- **Pattern**: **IntStream.range + filter + findFirst**.

#### 🛠️ Step-by-Step Strategy
1. Stream indices `0` to `list.size() - 1`.
2. Filter where `list.get(i) == target`.
3. Return `findFirst().orElse(-1)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class LinearSearchNormal {
    public static int search(List<Integer> list, int target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == target) return i;
        }
        return -1;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;
import java.util.stream.IntStream;

public class LinearSearchStream {
    public static int search(List<Integer> list, int target) {
        return IntStream.range(0, list.size())
            .filter(i -> list.get(i) == target)
            .findFirst()
            .orElse(-1);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `for` loop with index comparison.
- **Stream API**: `IntStream.range()` provides functional indexed searching.

---

### Q85: Find First Even Number in Stream (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of integers `int[] nums`, search for and return the first even number present in the array."*

#### 🛍️ Real-World Intuitive Story
Imagine drawing raffle tickets from a box. As soon as you draw a ticket with an even number, you stop and declare it the winner.

#### 📥 Input & 📤 Output
- **Input**: `[1, 3, 7, 4, 9]` $\rightarrow$ **Output**: `OptionalInt[4]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"find first even number"*.
- **Pattern**: **IntStream.filter(n -> n % 2 == 0).findFirst()**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive integers.
2. Filter numbers where `n % 2 == 0`.
3. Return `.findFirst()`.

#### ☕ Solution 1: Normal Java
```java
public class FirstEvenNormal {
    public static Integer findFirstEven(int[] nums) {
        for (int num : nums) {
            if (num % 2 == 0) return num;
        }
        return null;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.OptionalInt;

public class FirstEvenStream {
    public static OptionalInt findFirstEven(int[] nums) {
        return Arrays.stream(nums)
            .filter(n -> n % 2 == 0)
            .findFirst();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Loop returning nullable Integer.
- **Stream API**: `OptionalInt` safely handles absence without `null`.

---

### Q86: Search for Element in 2D Array (LeetCode #74 Variation)

#### 📄 Real-World Interview Problem Statement
> *"Given a 2D integer matrix `int[][] matrix` and a target value `target`, return `true` if target exists anywhere in the matrix."*

#### 🛍️ Real-World Intuitive Story
Imagine searching a building with multiple floors (rows) and rooms (columns) for a specific guest.

#### 📥 Input & 📤 Output
- **Input**: `matrix = [[1,3],[5,7]]`, `target = 5` $\rightarrow$ **Output**: `true`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"search 2D matrix stream"*.
- **Pattern**: **Arrays.stream(matrix).flatMapToInt.anyMatch**.

#### 🛠️ Step-by-Step Strategy
1. Stream 2D rows.
2. FlatMap rows into primitive `IntStream`.
3. Check `.anyMatch(x -> x == target)`.

#### ☕ Solution 1: Normal Java
```java
public class MatrixSearchNormal {
    public static boolean searchMatrix(int[][] matrix, int target) {
        for (int[] row : matrix) {
            for (int val : row) {
                if (val == target) return true;
            }
        }
        return false;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class MatrixSearchStream {
    public static boolean searchMatrix(int[][] matrix, int target) {
        return Arrays.stream(matrix)
            .flatMapToInt(Arrays::stream)
            .anyMatch(val -> val == target);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Nested `for` loop matrix search.
- **Stream API**: `flatMapToInt` flattens matrix to single stream for `anyMatch`.

---

### Q87: Find Maximum Difference Between Two Elements (LeetCode #121 Variation)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of stock prices `int[] prices`, find the maximum profit (difference) achievable by buying on one day and selling on a subsequent day."*

#### 🛍️ Real-World Intuitive Story
Track the lowest stock price seen so far. At each step, compare potential profit by subtracting the lowest price from current price.

#### 📥 Input & 📤 Output
- **Input**: `prices = [7,1,5,3,6,4]` $\rightarrow$ **Output**: `5` (Buy at 1, sell at 6)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"max difference buy sell stock"*.
- **Pattern**: **Single pass min tracking / IntStream reduction**.

#### 🛠️ Step-by-Step Strategy
1. Track running minimum price.
2. Calculate max difference at each index.

#### ☕ Solution 1: Normal Java
```java
public class MaxProfitNormal {
    public static int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;
            } else if (price - minPrice > maxProfit) {
                maxProfit = price - minPrice;
            }
        }
        return maxProfit;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class MaxProfitStream {
    public static int maxProfit(int[] prices) {
        int[] min = {Integer.MAX_VALUE};
        return Arrays.stream(prices)
            .map(price -> {
                min[0] = Math.min(min[0], price);
                return price - min[0];
            })
            .max()
            .orElse(0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: $O(N)$ single pass tracking min price and max profit.
- **Stream API**: State-mapping stream pipeline finding max profit.

---

### Q88: Search for Employee by Id Optional (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a company directory repository, search `List<Employee>` for an employee with matching ID. Return an `Optional<Employee>` to handle missing entries cleanly."*

#### 🛍️ Real-World Intuitive Story
Imagine looking up an ID badge number in an HR file binder. If found, hand over the file; otherwise, hand back an empty folder.

#### 📥 Input & 📤 Output
- **Input**: `[Emp(101, "Alice")], targetId = 101` $\rightarrow$ **Output**: `Optional[Emp(101, "Alice")]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"search object by id optional"*.
- **Pattern**: **stream.filter(e -> e.id() == target).findFirst()**.

#### 🛠️ Step-by-Step Strategy
1. Stream `Employee` records.
2. Filter matching ID.
3. Return `findFirst()`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FindEmployeeNormal {
    public record Employee(int id, String name) {}

    public static Optional<Employee> findById(List<Employee> list, int targetId) {
        for (Employee e : list) {
            if (e.id() == targetId) return Optional.of(e);
        }
        return Optional.empty();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class FindEmployeeStream {
    public record Employee(int id, String name) {}

    public static Optional<Employee> findById(List<Employee> list, int targetId) {
        return list.stream()
            .filter(e -> e.id() == targetId)
            .findFirst();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Manual `for` loop returning `Optional.of(e)`.
- **Stream API**: `filter().findFirst()` returns `Optional` natively.

---

### Q89: Search First Missing Positive Number (LeetCode #41)

#### 📄 Real-World Interview Problem Statement
> *"Given an unsorted integer array `nums`, return the smallest missing positive integer."*

#### 🛍️ Real-World Intuitive Story
Imagine taking attendance numbers `1, 2, 3, 5`. The first student missing in counting order is `#4`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,2,0]` $\rightarrow$ **Output**: `3`
- **Input**: `nums = [3,4,-1,1]` $\rightarrow$ **Output**: `2`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"first missing positive integer"*.
- **Pattern**: **Set Lookup + IntStream Range**.

#### 🛠️ Step-by-Step Strategy
1. Store array elements into a `Set`.
2. Stream integers `1` to `nums.length + 1`.
3. Filter first integer missing from `Set`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FirstMissingPositiveNormal {
    public static int firstMissingPositive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) if (num > 0) set.add(num);

        for (int i = 1; i <= nums.length + 1; i++) {
            if (!set.contains(i)) return i;
        }
        return 1;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FirstMissingPositiveStream {
    public static int firstMissingPositive(int[] nums) {
        Set<Integer> set = Arrays.stream(nums)
            .filter(x -> x > 0)
            .boxed()
            .collect(Collectors.toSet());

        return IntStream.rangeClosed(1, nums.length + 1)
            .filter(i -> !set.contains(i))
            .findFirst()
            .orElse(1);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Set accumulation + `for` loop lookup.
- **Stream API**: `IntStream.rangeClosed().filter(!set::contains).findFirst()`.

---

### Q90: Search Element Greater Than Threshold (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a stream of sensor readings `double[] readings`, return the first reading that breaches a critical alert threshold value `threshold`."*

#### 🛍️ Real-World Intuitive Story
Imagine a pressure sensor monitoring a pipeline. As soon as a reading exceeds 100 PSI, trigger an alert immediately.

#### 📥 Input & 📤 Output
- **Input**: `[45.2, 88.0, 105.4, 90.1]`, `threshold = 100.0` $\rightarrow$ **Output**: `105.4`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"first element greater than threshold"*.
- **Pattern**: **DoubleStream.filter(v -> v > threshold).findFirst()**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive double readings.
2. Filter values greater than threshold.
3. Return `findFirst()`.

#### ☕ Solution 1: Normal Java
```java
import java.util.OptionalDouble;

public class ThresholdSearchNormal {
    public static OptionalDouble findBreach(double[] readings, double threshold) {
        for (double r : readings) {
            if (r > threshold) return OptionalDouble.of(r);
        }
        return OptionalDouble.empty();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.OptionalDouble;

public class ThresholdSearchStream {
    public static OptionalDouble findBreach(double[] readings, double threshold) {
        return Arrays.stream(readings)
            .filter(r -> r > threshold)
            .findFirst();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard `for` loop search returning `OptionalDouble`.
- **Stream API**: `DoubleStream.filter().findFirst()`.

---

### Q91: Check Contains Substring Case-Insensitive (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of product titles `List<String>`, check if any title contains a search query substring regardless of letter case."*

#### 🛍️ Real-World Intuitive Story
Search a library catalog for `"java"`. Return true if any book title contains `"Java"`, `"JAVA"`, or `"JavaScript"`.

#### 📥 Input & 📤 Output
- **Input**: `["Python 101", "Java Programming"]`, `query = "java"` $\rightarrow$ **Output**: `true`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"contains substring case-insensitive"*.
- **Pattern**: **stream.anyMatch(s -> s.toLowerCase().contains(query.toLowerCase()))**.

#### 🛠️ Step-by-Step Strategy
1. Normalize search query to lowercase.
2. Stream titles and call `.anyMatch(t -> t.toLowerCase().contains(query))`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class SubstringSearchNormal {
    public static boolean containsQuery(List<String> titles, String query) {
        if (query == null) return false;
        String q = query.toLowerCase();
        for (String title : titles) {
            if (title != null && title.toLowerCase().contains(q)) return true;
        }
        return false;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;

public class SubstringSearchStream {
    public static boolean containsQuery(List<String> titles, String query) {
        if (query == null) return false;
        String q = query.toLowerCase();
        return titles.stream()
            .filter(t -> t != null)
            .anyMatch(t -> t.toLowerCase().contains(q));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Sequential lower-case conversion search loop.
- **Stream API**: Modern functional `anyMatch` query filter.

---

### Q92: Find First Duplicate Number (LeetCode #287)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of integers `nums` containing `n + 1` integers where each integer is between `1` and `n`, find the first duplicate number."*

#### 🛍️ Real-World Intuitive Story
As you scan numbers, drop them into a set. The very first number that fails to be added to the set is declared the first duplicate.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,3,4,2,2]` $\rightarrow$ **Output**: `2`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"first duplicate number"*, *"Set.add failure"*.
- **Pattern**: **Set.add predicate inside filter**.

#### 🛠️ Step-by-Step Strategy
1. Create a `Set<Integer> seen`.
2. Stream `nums` and filter using `!seen.add(num)`.
3. Pick `.findFirst()`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FirstDuplicateNormal {
    public static int findDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (!seen.add(num)) return num;
        }
        return -1;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class FirstDuplicateStream {
    public static int findDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        return Arrays.stream(nums)
            .filter(n -> !seen.add(n))
            .findFirst()
            .orElse(-1);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `HashSet.add()` loop returning immediately on `false`.
- **Stream API**: State-based filter `!seen.add(n)` returning `findFirst()`.

---

### Q93: Search Range / Pair Summing to Target (LeetCode #1)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`."*

#### 🛍️ Real-World Intuitive Story
Imagine holding price tags. For each item priced `$X`, you check if a complimenting item priced `$(Target - X)` has already been scanned.

#### 📥 Input & 📤 Output
- **Input**: `nums = [2,7,11,15]`, `target = 9` $\rightarrow$ **Output**: `[0, 1]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"two sum target indices"*.
- **Pattern**: **Map Complement Lookup / IntStream Index Pair Filter**.

#### 🛠️ Step-by-Step Strategy
1. Maintain a map storing `(elementValue -> index)`.
2. Iterate through array, checking if `map.containsKey(target - current)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class TwoSumNormal {
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[0];
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.IntStream;

public class TwoSumStream {
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        return IntStream.range(0, nums.length)
            .filter(i -> {
                int complement = target - nums[i];
                if (map.containsKey(complement)) return true;
                map.put(nums[i], i);
                return false;
            })
            .mapToObj(i -> new int[]{map.get(target - nums[i]), i})
            .findFirst()
            .orElse(new int[0]);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: $O(N)$ hash map single-pass solution.
- **Stream API**: `IntStream.range()` filtering map complement matches.

---

### Q94: Find First Element Starting With Specific Prefix (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Search a list of customer names `List<String>` for the first name starting with prefix `"Mc"`."*

#### 🛍️ Real-World Intuitive Story
Scanning a physical file cabinet labeled alphabetically. Stop at the very first folder starting with `"Mc"`.

#### 📥 Input & 📤 Output
- **Input**: `["Smith", "McDonald", "McGregor"]` $\rightarrow$ **Output**: `"McDonald"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"first name with prefix"*.
- **Pattern**: **stream.filter(s -> s.startsWith(prefix)).findFirst()**.

#### 🛠️ Step-by-Step Strategy
1. Stream string list.
2. Filter strings where `s.startsWith(prefix)`.
3. Return `findFirst().orElse(null)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class PrefixSearchNormal {
    public static String findFirstPrefix(List<String> names, String prefix) {
        for (String name : names) {
            if (name != null && name.startsWith(prefix)) return name;
        }
        return null;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;

public class PrefixSearchStream {
    public static String findFirstPrefix(List<String> names, String prefix) {
        return names.stream()
            .filter(name -> name != null && name.startsWith(prefix))
            .findFirst()
            .orElse(null);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard `startsWith()` loop check.
- **Stream API**: `filter().findFirst()` declarative search.

---

### Q95: Search for Valid IP Address Strings (LeetCode #93 Variation)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of IP candidate strings `List<String>`, filter out all valid IPv4 address strings (`4` octets separated by dots, each octet `0-255`)."*

#### 🛍️ Real-World Intuitive Story
Inspecting network traffic logs to keep only correctly formatted IP addresses (e.g. `192.168.1.1`).

#### 📥 Input & 📤 Output
- **Input**: `["192.168.1.1", "256.0.0.1", "abc.def"]` $\rightarrow$ **Output**: `["192.168.1.1"]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"filter valid IPv4 strings"*.
- **Pattern**: **stream.filter(this::isValidIPv4)**.

#### 🛠️ Step-by-Step Strategy
1. Define validator checking 4 numeric octets between 0 and 255 without leading zeros.
2. Filter stream matching valid IPv4 rules.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class ValidIPNormal {
    private static boolean isValidIPv4(String ip) {
        if (ip == null) return false;
        String[] parts = ip.split("\\.", -1);
        if (parts.length != 4) return false;
        for (String p : parts) {
            if (p.isEmpty() || p.length() > 3) return false;
            if (p.length() > 1 && p.startsWith("0")) return false;
            try {
                int val = Integer.parseInt(p);
                if (val < 0 || val > 255) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public static List<String> filterIPs(List<String> ips) {
        List<String> result = new ArrayList<>();
        for (String ip : ips) {
            if (isValidIPv4(ip)) result.add(ip);
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class ValidIPStream {
    private static boolean isValidIPv4(String ip) {
        if (ip == null) return false;
        String[] parts = ip.split("\\.", -1);
        if (parts.length != 4) return false;
        return Arrays.stream(parts).allMatch(p -> {
            if (p.isEmpty() || p.length() > 3) return false;
            if (p.length() > 1 && p.startsWith("0")) return false;
            try {
                int val = Integer.parseInt(p);
                return val >= 0 && val <= 255;
            } catch (NumberFormatException e) {
                return false;
            }
        });
    }

    public static List<String> filterIPs(List<String> ips) {
        return ips.stream()
            .filter(ValidIPStream::isValidIPv4)
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard string splitting and numeric validation loop.
- **Stream API**: Functional validator method reference with `allMatch()`.

---

### Q96: Find Last Element in Stream (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a stream of audit log entries `List<Log>`, retrieve the very last log entry added to the stream."*

#### 🛍️ Real-World Intuitive Story
Reading a physical logbook to see the final entry recorded at the bottom of the last page.

#### 📥 Input & 📤 Output
- **Input**: `["Log1", "Log2", "Log3"]` $\rightarrow$ **Output**: `"Log3"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"find last element stream"*.
- **Pattern**: **reduce((first, second) -> second)**.

#### 🛠️ Step-by-Step Strategy
1. Stream items.
2. Reduce stream keeping the accumulator's second argument: `.reduce((first, second) -> second)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class FindLastNormal {
    public static <T> T findLast(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        return list.get(list.size() - 1);
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;
import java.util.Optional;

public class FindLastStream {
    public static <T> Optional<T> findLast(List<T> list) {
        return list.stream()
            .reduce((first, second) -> second);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `list.get(list.size() - 1)` direct index access.
- **Stream API**: `.reduce((first, second) -> second)` extracts the terminal element.

---

### Q97: Search Prime Number in List (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of numbers `List<Integer>`, find the first prime number present."*

#### 🛍️ Real-World Intuitive Story
Testing numbers in a list one by one for primality until you find the first prime.

#### 📥 Input & 📤 Output
- **Input**: `[4, 6, 8, 9, 11, 15]` $\rightarrow$ **Output**: `11`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"find first prime number"*.
- **Pattern**: **stream.filter(this::isPrime).findFirst()**.

#### 🛠️ Step-by-Step Strategy
1. Helper method `isPrime(n)`.
2. Stream numbers, filter `isPrime`, and return `findFirst()`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class SearchPrimeNormal {
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static Integer findFirstPrime(List<Integer> list) {
        for (int num : list) {
            if (isPrime(num)) return num;
        }
        return null;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class SearchPrimeStream {
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        return IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(i -> n % i == 0);
    }

    public static Optional<Integer> findFirstPrime(List<Integer> list) {
        return list.stream()
            .filter(SearchPrimeStream::isPrime)
            .findFirst();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Loop with prime math helper function.
- **Stream API**: `IntStream.rangeClosed().noneMatch()` combined with `filter().findFirst()`.

---

### Q98: Find First Negative Number in Window (Sliding Window Basic)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of integers `int[] arr` and window size `k`, find the first negative integer in every sliding window of size `k`."*

#### 🛍️ Real-World Intuitive Story
Slide a viewing frame of width `k` across an array. For each frame position, look for the first negative number inside the frame.

#### 📥 Input & 📤 Output
- **Input**: `arr = [12, -1, -7, 8, -15, 30, 16, 28]`, `k = 3`
- **Output**: `[-1, -1, -7, -15, -15, 0]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"first negative in sliding window"*.
- **Pattern**: **IntStream.range + sub-stream filter findFirst**.

#### 🛠️ Step-by-Step Strategy
1. Stream window start indices `0` to `arr.length - k`.
2. For each window index `i`, check sub-range `i` to `i + k`.
3. Find first negative or return 0.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FirstNegativeWindowNormal {
    public static int[] firstNegative(int[] arr, int k) {
        int n = arr.length;
        int[] result = new int[n - k + 1];

        for (int i = 0; i <= n - k; i++) {
            int firstNeg = 0;
            for (int j = i; j < i + k; j++) {
                if (arr[j] < 0) {
                    firstNeg = arr[j];
                    break;
                }
            }
            result[i] = firstNeg;
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class FirstNegativeWindowStream {
    public static int[] firstNegative(int[] arr, int k) {
        return IntStream.range(0, arr.length - k + 1)
            .map(i -> Arrays.stream(arr, i, i + k)
                .filter(x -> x < 0)
                .findFirst()
                .orElse(0)
            )
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Outer sliding window loop with inner break loop.
- **Stream API**: `IntStream.range()` mapped to sliced `Arrays.stream(arr, i, i + k)` filter.
