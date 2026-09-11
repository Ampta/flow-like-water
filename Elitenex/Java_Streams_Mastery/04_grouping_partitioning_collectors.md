# 📊 Category 4: Grouping, Partitioning & Collectors

This guide provides exhaustive, production-grade solutions for **20 Grouping, Partitioning & Advanced Collectors Problems** (Q59 – Q78). Every problem is presented with an authentic interview scenario, real-world intuitive story, step-by-step strategy, and dual implementation (Normal Java vs. Java Streams).

---

## 📑 Table of Contents
- [Q59: Group Anagrams (LeetCode #49)](#q59-group-anagrams-leetcode-49)
- [Q60: Partition Array into Even and Odd (Interview Classic)](#q60-partition-array-into-even-and-odd-interview-classic)
- [Q61: Custom Collector for String Joining with Prefix/Suffix (Interview Classic)](#q61-custom-collector-for-string-joining-with-prefixsuffix-interview-classic)
- [Q62: Group Employees by Department and Find Max Salary (Interview Classic)](#q62-group-employees-by-department-and-find-max-salary-interview-classic)
- [Q63: Counting Word Frequencies in a List of Sentences (Interview Classic)](#q63-counting-word-frequencies-in-a-list-of-sentences-interview-classic)
- [Q64: Partition Prime and Composite Numbers (Interview Classic)](#q64-partition-prime-and-composite-numbers-interview-classic)
- [Q65: Group Transaction Amounts by Currency (Interview Classic)](#q65-group-transaction-amounts-by-currency-interview-classic)
- [Q66: Collect Stream into Unmodifiable Map (Java 10+) (Interview Classic)](#q66-collect-stream-into-unmodifiable-map-java-10-interview-classic)
- [Q67: Multi-Level Grouping: Dept -> Role -> Employees (Interview Classic)](#q67-multi-level-grouping-dept---role---employees-interview-classic)
- [Q68: Collect to Custom Map with Key Collisions (Interview Classic)](#q68-collect-to-custom-map-with-key-collisions-interview-classic)
- [Q69: Partition Pass/Fail Students by Score Threshold (Interview Classic)](#q69-partition-passfail-students-by-score-threshold-interview-classic)
- [Q70: Collectors.teeing() Dual Aggregation (Java 12+) (Interview Classic)](#q70-collectorsteeing-dual-aggregation-java-12-interview-classic)
- [Q71: Find All Duplicates in an Array (LeetCode #442)](#q71-find-all-duplicates-in-an-array-leetcode-442)
- [Q72: Group Words by Length (Interview Classic)](#q72-group-words-by-length-interview-classic)
- [Q73: Collect Stream to LinkedHashMap Preserving Order (Interview Classic)](#q73-collect-stream-to-linkedhashmap-preserving-order-interview-classic)
- [Q74: Partition Strings by Vowel Starting Letter (Interview Classic)](#q74-partition-strings-by-vowel-starting-letter-interview-classic)
- [Q75: Group Orders by Status and Sum Order Values (Interview Classic)](#q75-group-orders-by-status-and-sum-order-values-interview-classic)
- [Q76: Find Most Frequent Element in List (LeetCode #169 Variation)](#q76-find-most-frequent-element-in-list-leetcode-169-variation)
- [Q77: Collect Stream to Immutable List/Set (Java 10+) (Interview Classic)](#q77-collect-stream-to-immutable-listset-java-10-interview-classic)
- [Q78: Group Customers by Country and List Cities (Interview Classic)](#q78-group-customers-by-country-and-list-cities-interview-classic)

---

### Q59: Group Anagrams (LeetCode #49)

#### 📄 Real-World Interview Problem Statement
> *"You are building a search query deduplication engine for a web browser. Given an array of search query strings `strs`, group all words that are anagrams of each other into sublists. You can return the answer in any order."*

#### 🛍️ Real-World Intuitive Story
Imagine sorting letters into mailbox slots. Each word is sorted alphabetically to form a unique key (e.g., `"eat"` $\rightarrow$ `"aet"`, `"tea"` $\rightarrow$ `"aet"`). All words that share the exact same sorted key drop into the exact same mailbox slot.

#### 📥 Input & 📤 Output
- **Input**: `strs = ["eat","tea","tan","ate","nat","bat"]`
- **Output**: `[["bat"],["nat","tan"],["ate","eat","tea"]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"group anagrams"*, *"words with same letter combinations"*.
- **Pattern**: **Collectors.groupingBy with Sorted String Key**.

#### 🛠️ Step-by-Step Strategy
1. For each string, convert to character array, sort it, and convert back to string key.
2. Group original strings into a `Map<String, List<String>>` using the sorted key.
3. Collect the map values as the final result list of sublists.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class GroupAnagramsNormal {
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String sortedKey = new String(chars);
            map.computeIfAbsent(sortedKey, k -> new ArrayList<>()).add(str);
        }
        return new ArrayList<>(map.values());
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class GroupAnagramsStream {
    public static List<List<String>> groupAnagrams(String[] strs) {
        return new ArrayList<>(
            Arrays.stream(strs)
                .collect(Collectors.groupingBy(str -> {
                    char[] chars = str.toCharArray();
                    Arrays.sort(chars);
                    return new String(chars);
                }))
                .values()
        );
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `map.computeIfAbsent()` builds the hash map buckets explicitly.
- **Stream API**: `Collectors.groupingBy()` creates multi-valued list buckets in one functional expression.

---

### Q60: Partition Array into Even and Odd (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"You are designing a thread-pool job balancer. Given an array of numeric task IDs `int[] nums`, partition the task IDs into two distinct lists: one containing all even task IDs (`true` key) and the other containing all odd task IDs (`false` key)."*

#### 🛍️ Real-World Intuitive Story
Think of sorting mail into two bins based on house numbers: Bin 1 for even house numbers, Bin 2 for odd house numbers.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1, 2, 3, 4, 5, 6]` $\rightarrow$ **Output**: `{true=[2, 4, 6], false=[1, 3, 5]}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"partition into two groups"*, *"even and odd split"*.
- **Pattern**: **Collectors.partitioningBy(predicate)**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive integer array.
2. Apply `Collectors.partitioningBy(n -> n % 2 == 0)`.
3. Returns a `Map<Boolean, List<Integer>>` splitting elements into matching vs. non-matching lists.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class PartitionEvenOddNormal {
    public static Map<Boolean, List<Integer>> partition(int[] nums) {
        Map<Boolean, List<Integer>> result = new HashMap<>();
        result.put(true, new ArrayList<>());
        result.put(false, new ArrayList<>());

        for (int num : nums) {
            result.get(num % 2 == 0).add(num);
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class PartitionEvenOddStream {
    public static Map<Boolean, List<Integer>> partition(int[] nums) {
        return Arrays.stream(nums)
            .boxed()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Pre-fills boolean map keys explicitly.
- **Stream API**: `Collectors.partitioningBy()` guarantees a 2-bucket boolean map efficiently.

---

### Q61: Custom Collector for String Joining with Prefix/Suffix (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of database column names `List<String>`, format them into an SQL `SELECT` clause formatted as `SELECT (col1, col2, col3)` using a custom collector or `Collectors.joining()`."*

#### 🛍️ Real-World Intuitive Story
Imagine putting a row of pearls onto a necklace, starting with a clasp prefix `"SELECT ("`, putting commas `", "` between pearls, and closing with a clasp suffix `")"`.

#### 📥 Input & 📤 Output
- **Input**: `["id", "name", "email"]` $\rightarrow$ **Output**: `"SELECT (id, name, email)"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"join strings with delimiter, prefix, suffix"*.
- **Pattern**: **Collectors.joining(delimiter, prefix, suffix)**.

#### 🛠️ Step-by-Step Strategy
1. Stream string column names.
2. Collect via `Collectors.joining(", ", "SELECT (", ")")`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class StringJoiningNormal {
    public static String joinColumns(List<String> cols) {
        StringBuilder sb = new StringBuilder("SELECT (");
        for (int i = 0; i < cols.size(); i++) {
            sb.append(cols.get(i));
            if (i < cols.size() - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;
import java.util.stream.Collectors;

public class StringJoiningStream {
    public static String joinColumns(List<String> cols) {
        return cols.stream()
            .collect(Collectors.joining(", ", "SELECT (", ")"));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Manual `StringBuilder` index checking for trailing commas.
- **Stream API**: `Collectors.joining()` formats delimiter, prefix, and suffix in 1 clean line.

---

### Q62: Group Employees by Department and Find Max Salary (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a company payroll system, given `List<Employee>`, group employees by their Department and compute the highest paid employee record in each department."*

#### 🛍️ Real-World Intuitive Story
Imagine rooming employees by department. In each department room, ask everyone to line up by salary and pick the person at the very head of the line.

#### 📥 Input & 📤 Output
- **Input**: `[Emp("Eng", 9000), Emp("Eng", 12000), Emp("HR", 7000)]`
- **Output**: `{Eng=Optional[Emp("Eng", 12000)], HR=Optional[Emp("HR", 7000)]}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"group by department and find max"*, *"downstream maxBy collector"*.
- **Pattern**: **Collectors.groupingBy + Collectors.maxBy**.

#### 🛠️ Step-by-Step Strategy
1. Stream `Employee` objects.
2. Collect using `Collectors.groupingBy(Employee::dept, Collectors.maxBy(Comparator.comparingDouble(Employee::salary)))`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class DeptMaxSalaryNormal {
    public record Employee(String dept, double salary) {}

    public static Map<String, Employee> getMaxSalaryByDept(List<Employee> employees) {
        Map<String, Employee> map = new HashMap<>();
        for (Employee e : employees) {
            if (!map.containsKey(e.dept()) || e.salary() > map.get(e.dept()).salary()) {
                map.put(e.dept(), e);
            }
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class DeptMaxSalaryStream {
    public record Employee(String dept, double salary) {}

    public static Map<String, Optional<Employee>> getMaxSalaryByDept(List<Employee> employees) {
        return employees.stream()
            .collect(Collectors.groupingBy(
                Employee::dept,
                Collectors.maxBy(Comparator.comparingDouble(Employee::salary))
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: HashMap update loop keeping the highest salary seen.
- **Stream API**: Composable `groupingBy` with downstream `maxBy` collector.

---

### Q63: Counting Word Frequencies in a List of Sentences (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"You are developing a search engine indexer. Given a list of sentence strings `List<String>`, split all sentences into individual words, normalize to lowercase, and return a word frequency map `Map<String, Long>`."*

#### 🛍️ Real-World Intuitive Story
Imagine opening several books, cutting out every word, dropping all words into a big pile, and tallying up how many times each word appears.

#### 📥 Input & 📤 Output
- **Input**: `["Hello world", "hello Java world"]` $\rightarrow$ **Output**: `{hello=2, world=2, java=1}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"word frequency across sentences"*, *"flatMap words"*.
- **Pattern**: **Stream flatMap(Arrays::stream) + Collectors.groupingBy + counting**.

#### 🛠️ Step-by-Step Strategy
1. Stream sentence strings.
2. FlatMap sentences into lowercase word streams using `s.toLowerCase().split("\\s+")`.
3. Group by identity and collect counting.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class WordFrequencySentencesNormal {
    public static Map<String, Integer> countWords(List<String> sentences) {
        Map<String, Integer> counts = new HashMap<>();
        for (String sentence : sentences) {
            String[] words = sentence.toLowerCase().split("\\s+");
            for (String word : words) {
                if (!word.isEmpty()) {
                    counts.put(word, counts.getOrDefault(word, 0) + 1);
                }
            }
        }
        return counts;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordFrequencySentencesStream {
    public static Map<String, Long> countWords(List<String> sentences) {
        return sentences.stream()
            .flatMap(sentence -> Arrays.stream(sentence.toLowerCase().split("\\s+")))
            .filter(w -> !w.isEmpty())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Nested `for` loop with array splits.
- **Stream API**: `flatMap` seamlessly flattens sentence arrays into a continuous word stream pipeline.

---

### Q64: Partition Prime and Composite Numbers (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `int[] numbers`, partition the numbers into prime numbers (`true`) and non-prime/composite numbers (`false`)."*

#### 🛍️ Real-World Intuitive Story
Think of inspecting numbers with a mathematical magnifying glass. If a number can only be divided by 1 and itself, send it to the Prime bin; otherwise, send it to the Composite bin.

#### 📥 Input & 📤 Output
- **Input**: `[2, 3, 4, 5, 6, 7, 8, 9, 10]` $\rightarrow$ **Output**: `{true=[2, 3, 5, 7], false=[4, 6, 8, 9, 10]}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"partition prime numbers"*, *"predicate partitioning"*.
- **Pattern**: **Collectors.partitioningBy(this::isPrime)**.

#### 🛠️ Step-by-Step Strategy
1. Helper method `isPrime(n)` checks if $n > 1$ and has no divisors up to $\sqrt{n}$.
2. Stream numbers and collect via `Collectors.partitioningBy(this::isPrime)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class PartitionPrimeNormal {
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static Map<Boolean, List<Integer>> partitionPrimes(int[] nums) {
        Map<Boolean, List<Integer>> map = new HashMap<>();
        map.put(true, new ArrayList<>());
        map.put(false, new ArrayList<>());

        for (int num : nums) {
            map.get(isPrime(num)).add(num);
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PartitionPrimeStream {
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        return IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(i -> n % i == 0);
    }

    public static Map<Boolean, List<Integer>> partitionPrimes(int[] nums) {
        return Arrays.stream(nums)
            .boxed()
            .collect(Collectors.partitioningBy(PartitionPrimeStream::isPrime));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard `for` loop prime check.
- **Stream API**: `IntStream.rangeClosed().noneMatch()` combined with `Collectors.partitioningBy()`.

---

### Q65: Group Transaction Amounts by Currency (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a global banking platform, process a list of bank transactions `List<Transaction>`. Group transactions by currency code (e.g. `"USD"`, `"EUR"`, `"GBP"`) and compute the total transaction sum for each currency."*

#### 🛍️ Real-World Intuitive Story
Imagine sorting coins into cash register drawers labelled by currency: USD drawer, EUR drawer, GBP drawer. Count the total cash amount inside each drawer.

#### 📥 Input & 📤 Output
- **Input**: `[Txn("USD", 100), Txn("EUR", 50), Txn("USD", 200)]`
- **Output**: `{USD=300.0, EUR=50.0}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"group by currency"*, *"sum transaction amounts"*.
- **Pattern**: **Collectors.groupingBy + Collectors.summingDouble**.

#### 🛠️ Step-by-Step Strategy
1. Stream transaction objects.
2. Group by currency string using `Collectors.groupingBy(Transaction::currency, Collectors.summingDouble(Transaction::amount))`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class GroupCurrencySumNormal {
    public record Transaction(String currency, double amount) {}

    public static Map<String, Double> sumByCurrency(List<Transaction> txns) {
        Map<String, Double> map = new HashMap<>();
        for (Transaction t : txns) {
            map.put(t.currency(), map.getOrDefault(t.currency(), 0.0) + t.amount());
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class GroupCurrencySumStream {
    public record Transaction(String currency, double amount) {}

    public static Map<String, Double> sumByCurrency(List<Transaction> txns) {
        return txns.stream()
            .collect(Collectors.groupingBy(
                Transaction::currency,
                Collectors.summingDouble(Transaction::amount)
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Manual map updates with `getOrDefault()`.
- **Stream API**: Composable `groupingBy` with downstream `summingDouble`.

---

### Q66: Collect Stream into Unmodifiable Map (Java 10+) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Construct an immutable system configuration map from a list of config key-value pairs `List<ConfigParam>`, ensuring no further modifications can be made to the returned map."*

#### 🛍️ Real-World Intuitive Story
Imagine printing system settings onto a metal plaque. Once engraved, no one can add, remove, or modify any setting on the plaque.

#### 📥 Input & 📤 Output
- **Input**: `[Config("timeout", "3000"), Config("env", "prod")]`
- **Output**: Immutable `Map` `{timeout=3000, env=prod}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"unmodifiable map"*, *"Collectors.toUnmodifiableMap"*.
- **Pattern**: **Collectors.toUnmodifiableMap()**.

#### 🛠️ Step-by-Step Strategy
1. Stream configuration items.
2. Collect via `Collectors.toUnmodifiableMap(Config::key, Config::val)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class UnmodifiableMapNormal {
    public record Config(String key, String val) {}

    public static Map<String, String> getImmutableConfig(List<Config> configs) {
        Map<String, String> map = new HashMap<>();
        for (Config c : configs) {
            map.put(c.key(), c.val());
        }
        return Collections.unmodifiableMap(map);
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class UnmodifiableMapStream {
    public record Config(String key, String val) {}

    public static Map<String, String> getImmutableConfig(List<Config> configs) {
        return configs.stream()
            .collect(Collectors.toUnmodifiableMap(Config::key, Config::val));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `Collections.unmodifiableMap(map)` wrapper.
- **Stream API**: `Collectors.toUnmodifiableMap()` directly creates an unmodifiable map instance (Java 10+).

---

### Q67: Multi-Level Grouping: Dept -> Role -> Employees (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given `List<Employee>`, construct a two-level nested map `Map<String, Map<String, List<Employee>>>` grouping employees first by Department and then by Job Role."*

#### 🛍️ Real-World Intuitive Story
Imagine filing documents in a cabinet: Cabinet Drawer 1 (Department) contains folders for each Job Role (e.g. Engineering $\rightarrow$ Developer folder $\rightarrow$ employee files).

#### 📥 Input & 📤 Output
- **Input**: `[Emp("Eng", "Dev", "Alice"), Emp("Eng", "QA", "Bob")]`
- **Output**: `{Eng={Dev=[Emp("Alice")], QA=[Emp("Bob")]}}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"multi-level grouping"*, *"nested groupingBy"*.
- **Pattern**: **Collectors.groupingBy(Dept, Collectors.groupingBy(Role))**.

#### 🛠️ Step-by-Step Strategy
1. Stream `Employee` objects.
2. Collect with outer `groupingBy(Employee::dept)` and inner downstream `groupingBy(Employee::role)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class MultiLevelGroupingNormal {
    public record Employee(String dept, String role, String name) {}

    public static Map<String, Map<String, List<Employee>>> groupEmployees(List<Employee> employees) {
        Map<String, Map<String, List<Employee>>> map = new HashMap<>();
        for (Employee e : employees) {
            map.computeIfAbsent(e.dept(), k -> new HashMap<>())
               .computeIfAbsent(e.role(), k -> new ArrayList<>())
               .add(e);
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class MultiLevelGroupingStream {
    public record Employee(String dept, String role, String name) {}

    public static Map<String, Map<String, List<Employee>>> groupEmployees(List<Employee> employees) {
        return employees.stream()
            .collect(Collectors.groupingBy(
                Employee::dept,
                Collectors.groupingBy(Employee::role)
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Double `computeIfAbsent()` call chain.
- **Stream API**: Clean nested `groupingBy` collectors hierarchy.

---

### Q68: Collect to Custom Map with Key Collisions (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of products `List<Product>` where duplicate product IDs may exist, collect elements into a `Map<Integer, Product>` resolving key collisions by keeping the product with the higher price."*

#### 🛍️ Real-World Intuitive Story
Imagine updating price tags on store shelves. If two price tags arrive for product ID #101 ($50 and $70), overwrite with the higher price ($70).

#### 📥 Input & 📤 Output
- **Input**: `[Prod(101, 50.0), Prod(101, 70.0)]` $\rightarrow$ **Output**: `{101=Prod(101, 70.0)}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"toMap merge function"*, *"handle key collision"*.
- **Pattern**: **Collectors.toMap(keyMapper, valueMapper, mergeFunction)**.

#### 🛠️ Step-by-Step Strategy
1. Stream product list.
2. Collect via `Collectors.toMap(Product::id, Function.identity(), (p1, p2) -> p1.price() > p2.price() ? p1 : p2)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class MapKeyCollisionNormal {
    public record Product(int id, double price) {}

    public static Map<Integer, Product> collectProducts(List<Product> products) {
        Map<Integer, Product> map = new HashMap<>();
        for (Product p : products) {
            if (!map.containsKey(p.id()) || p.price() > map.get(p.id()).price()) {
                map.put(p.id(), p);
            }
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapKeyCollisionStream {
    public record Product(int id, double price) {}

    public static Map<Integer, Product> collectProducts(List<Product> products) {
        return products.stream()
            .collect(Collectors.toMap(
                Product::id,
                Function.identity(),
                (p1, p2) -> p1.price() > p2.price() ? p1 : p2
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Explicit `containsKey` price comparison inside loop.
- **Stream API**: `Collectors.toMap()` 3rd parameter merge function handles duplicate key collisions gracefully.

---

### Q69: Partition Pass/Fail Students by Score Threshold (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of student records `List<Student>`, partition students into Passing (`score >= 60`) and Failing (`score < 60`) lists."*

#### 🛍️ Real-World Intuitive Story
Imagine a grading machine inspecting exam sheets. If a student scored 60 or higher, put their sheet into the 'Passed' pile; otherwise, put it into the 'Retake Required' pile.

#### 📥 Input & 📤 Output
- **Input**: `[Student("Alice", 85), Student("Bob", 45)]`
- **Output**: `{true=[Student("Alice", 85)], false=[Student("Bob", 45)]}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"pass fail partition"*, *"score threshold"*.
- **Pattern**: **Collectors.partitioningBy(s -> s.score >= 60)**.

#### 🛠️ Step-by-Step Strategy
1. Stream `Student` records.
2. Collect via `Collectors.partitioningBy(s -> s.score() >= 60)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class PartitionPassFailNormal {
    public record Student(String name, int score) {}

    public static Map<Boolean, List<Student>> partitionStudents(List<Student> students) {
        Map<Boolean, List<Student>> map = new HashMap<>();
        map.put(true, new ArrayList<>());
        map.put(false, new ArrayList<>());

        for (Student s : students) {
            map.get(s.score() >= 60).add(s);
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class PartitionPassFailStream {
    public record Student(String name, int score) {}

    public static Map<Boolean, List<Student>> partitionStudents(List<Student> students) {
        return students.stream()
            .collect(Collectors.partitioningBy(s -> s.score() >= 60));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard boolean map pre-filling.
- **Stream API**: `Collectors.partitioningBy()` provides clear code readability.

---

### Q70: Collectors.teeing() Dual Aggregation (Java 12+) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `int[] numbers`, calculate both the Min element and Max element in a single stream pass, merging the results into a custom `MinMaxRecord(min, max)` using Java 12 `Collectors.teeing()`."*

#### 🛍️ Real-World Intuitive Story
Imagine a stream of water splitting into two pipes: Pipe A finds the lowest depth, Pipe B finds the highest height. Both pipes pour their findings into a single summary box at the end.

#### 📥 Input & 📤 Output
- **Input**: `[10, 2, 45, 8, 99]` $\rightarrow$ **Output**: `MinMaxRecord[min=2, max=99]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"dual collector in single pass"*, *"Collectors.teeing"*.
- **Pattern**: **Collectors.teeing(collector1, collector2, merger)**.

#### 🛠️ Step-by-Step Strategy
1. Stream `Integer` elements.
2. Collect using `Collectors.teeing(minBy, maxBy, mergerFunction)` (Java 12+).

#### ☕ Solution 1: Normal Java
```java
public class TeeingNormal {
    public record MinMaxRecord(int min, int max) {}

    public static MinMaxRecord findMinMax(int[] nums) {
        if (nums == null || nums.length == 0) return null;
        int min = nums[0], max = nums[0];
        for (int num : nums) {
            if (num < min) min = num;
            if (num > max) max = num;
        }
        return new MinMaxRecord(min, max);
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class TeeingStream {
    public record MinMaxRecord(int min, int max) {}

    public static MinMaxRecord findMinMax(int[] nums) {
        return Arrays.stream(nums)
            .boxed()
            .collect(Collectors.teeing(
                Collectors.minBy(Integer::compareTo),
                Collectors.maxBy(Integer::compareTo),
                (minOpt, maxOpt) -> new MinMaxRecord(minOpt.orElse(0), maxOpt.orElse(0))
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard simultaneous min/max loop tracking.
- **Stream API**: `Collectors.teeing()` combines two independent downstream collectors into a unified record (Java 12+).

---

### Q71: Find All Duplicates in an Array (LeetCode #442)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums` of length `n` where elements appear once or twice, return an array of all elements that appear twice."*

#### 🛍️ Real-World Intuitive Story
Imagine scanning concert tickets. If a ticket number appears for the second time, flag it immediately as a duplicate entry ticket.

#### 📥 Input & 📤 Output
- **Input**: `nums = [4,3,2,7,8,2,3,1]` $\rightarrow$ **Output**: `[2, 3]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"find all duplicates"*, *"frequency > 1 filter"*.
- **Pattern**: **Collectors.groupingBy + filter count > 1**.

#### 🛠️ Step-by-Step Strategy
1. Stream `nums` array.
2. Group elements by count.
3. Filter entries where count equals 2 and map to key.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FindDuplicatesNormal {
    public static List<Integer> findDuplicates(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        List<Integer> duplicates = new ArrayList<>();
        for (int num : nums) {
            if (!seen.add(num)) {
                duplicates.add(num);
            }
        }
        return duplicates;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FindDuplicatesStream {
    public static List<Integer> findDuplicates(int[] nums) {
        return Arrays.stream(nums)
            .boxed()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(Map.Entry::getKey)
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `Set.add()` returning `false` detects duplicates in $O(N)$ time.
- **Stream API**: Declarative frequency map filter pipeline.

---

### Q72: Group Words by Length (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of dictionary words `List<String>`, group words by their word length into a `Map<Integer, List<String>>`."*

#### 🛍️ Real-World Intuitive Story
Imagine sorting books onto library shelves based on their thickness or height.

#### 📥 Input & 📤 Output
- **Input**: `["cat", "dog", "apple", "bear"]` $\rightarrow$ **Output**: `{3=["cat", "dog"], 4=["bear"], 5=["apple"]}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"group words by length"*.
- **Pattern**: **Collectors.groupingBy(String::length)**.

#### 🛠️ Step-by-Step Strategy
1. Stream list of words.
2. Collect via `Collectors.groupingBy(String::length)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class GroupByLengthNormal {
    public static Map<Integer, List<String>> groupByLength(List<String> words) {
        Map<Integer, List<String>> map = new HashMap<>();
        for (String word : words) {
            map.computeIfAbsent(word.length(), k -> new ArrayList<>()).add(word);
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class GroupByLengthStream {
    public static Map<Integer, List<String>> groupByLength(List<String> words) {
        return words.stream()
            .collect(Collectors.groupingBy(String::length));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Map bucket creation loop using `computeIfAbsent`.
- **Stream API**: Extremely clean 1-liner collector.

---

### Q73: Collect Stream to LinkedHashMap Preserving Order (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Collect elements of a stream into a map where insertion order must be strictly preserved using a `LinkedHashMap` map implementation supplier."*

#### 🛍️ Real-World Intuitive Story
Imagine assigning ticket seating numbers in the exact chronological order customers arrive at the gate.

#### 📥 Input & 📤 Output
- **Input**: `["apple", "banana", "cherry"]` $\rightarrow$ **Output**: `LinkedHashMap` preserving `apple`, then `banana`, then `cherry`.

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"preserve insertion order in map"*, *"LinkedHashMap supplier"*.
- **Pattern**: **Collectors.toMap(key, val, merger, LinkedHashMap::new)**.

#### 🛠️ Step-by-Step Strategy
1. Stream string elements.
2. Collect using `Collectors.toMap(s -> s, String::length, (oldVal, newVal) -> oldVal, LinkedHashMap::new)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class PreserveOrderMapNormal {
    public static Map<String, Integer> collectOrder(List<String> items) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (String item : items) {
            map.putIfAbsent(item, item.length());
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PreserveOrderMapStream {
    public static Map<String, Integer> collectOrder(List<String> items) {
        return items.stream()
            .collect(Collectors.toMap(
                Function.identity(),
                String::length,
                (oldVal, newVal) -> oldVal,
                LinkedHashMap::new
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Directly instantiates `new LinkedHashMap<>()`.
- **Stream API**: Uses Map Supplier parameter `LinkedHashMap::new` inside `Collectors.toMap()`.

---

### Q74: Partition Strings by Vowel Starting Letter (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of words `List<String>`, partition them into words that start with a vowel (`true`) and words that start with a consonant (`false`)."*

#### 🛍️ Real-World Intuitive Story
Imagine sorting letters into two boxes: Box A for words starting with A, E, I, O, U, and Box B for all other words.

#### 📥 Input & 📤 Output
- **Input**: `["apple", "banana", "orange", "grape"]`
- **Output**: `{true=["apple", "orange"], false=["banana", "grape"]}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"partition vowel start"*.
- **Pattern**: **Collectors.partitioningBy(w -> "aeiouAEIOU".indexOf(w.charAt(0)) != -1)**.

#### 🛠️ Step-by-Step Strategy
1. Stream words.
2. Check if first letter is in vowel string `"aeiouAEIOU"`.
3. Collect using `Collectors.partitioningBy()`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class PartitionVowelNormal {
    private static boolean startsWithVowel(String word) {
        if (word == null || word.isEmpty()) return false;
        char c = Character.toLowerCase(word.charAt(0));
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    public static Map<Boolean, List<String>> partitionVowels(List<String> words) {
        Map<Boolean, List<String>> map = new HashMap<>();
        map.put(true, new ArrayList<>());
        map.put(false, new ArrayList<>());

        for (String w : words) {
            map.get(startsWithVowel(w)).add(w);
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class PartitionVowelStream {
    public static Map<Boolean, List<String>> partitionVowels(List<String> words) {
        return words.stream()
            .collect(Collectors.partitioningBy(w -> 
                w != null && !w.isEmpty() && "aeiouAEIOU".indexOf(w.charAt(0)) != -1
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Character equality helper method inside loop.
- **Stream API**: `Collectors.partitioningBy()` with inline character lookup.

---

### Q75: Group Orders by Status and Sum Order Values (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In an e-commerce dashboard backend, group customer order objects `List<Order>` by status (`"PENDING"`, `"SHIPPED"`, `"DELIVERED"`) and compute the total revenue sum per status."*

#### 🛍️ Real-World Intuitive Story
Think of sorting receipts into three stacks based on delivery status, then adding up dollar totals on all receipts in each stack.

#### 📥 Input & 📤 Output
- **Input**: `[Order("PENDING", 100), Order("DELIVERED", 250), Order("PENDING", 50)]`
- **Output**: `{PENDING=150.0, DELIVERED=250.0}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"group by status and sum"*.
- **Pattern**: **Collectors.groupingBy(Order::status, Collectors.summingDouble(Order::total))**.

#### 🛠️ Step-by-Step Strategy
1. Stream `Order` records.
2. Group by status string and collect summing total price.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class GroupOrdersSumNormal {
    public record Order(String status, double total) {}

    public static Map<String, Double> sumByStatus(List<Order> orders) {
        Map<String, Double> map = new HashMap<>();
        for (Order o : orders) {
            map.put(o.status(), map.getOrDefault(o.status(), 0.0) + o.total());
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class GroupOrdersSumStream {
    public record Order(String status, double total) {}

    public static Map<String, Double> sumByStatus(List<Order> orders) {
        return orders.stream()
            .collect(Collectors.groupingBy(
                Order::status,
                Collectors.summingDouble(Order::total)
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Frequency map accumulator pattern.
- **Stream API**: Declarative `groupingBy` with downstream double collector.

---

### Q76: Find Most Frequent Element in List (LeetCode #169 Variation)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of strings `List<String>`, find the single string element that occurs with the highest frequency."*

#### 🛍️ Real-World Intuitive Story
Imagine counting election votes. The candidate who collects the single largest stack of vote ballots wins.

#### 📥 Input & 📤 Output
- **Input**: `["apple", "banana", "apple", "cherry", "apple"]` $\rightarrow$ **Output**: `"apple"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"most frequent string"*, *"mode element"*.
- **Pattern**: **groupingBy counting + max(Map.Entry.comparingByValue())**.

#### 🛠️ Step-by-Step Strategy
1. Group strings by count using `Collectors.groupingBy(Function.identity(), Collectors.counting())`.
2. Find max map entry using `max(Map.Entry.comparingByValue())`.
3. Extract key.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class MostFrequentStringNormal {
    public static String findMostFrequent(List<String> list) {
        Map<String, Integer> counts = new HashMap<>();
        for (String s : list) {
            counts.put(s, counts.getOrDefault(s, 0) + 1);
        }

        String mostFrequent = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }
        return mostFrequent;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MostFrequentStringStream {
    public static String findMostFrequent(List<String> list) {
        return list.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard frequency map iteration tracking `maxCount`.
- **Stream API**: `max(Map.Entry.comparingByValue())` simplifies top frequency extraction.

---

### Q77: Collect Stream to Immutable List/Set (Java 10+) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Collect filtered customer IDs from a stream directly into an unmodifiable immutable `List` or `Set` using standard modern Java collectors."*

#### 🛍️ Real-World Intuitive Story
Imagine sealing an approved list of VIP guest passes inside a glass case so no additions or removals can be made.

#### 📥 Input & 📤 Output
- **Input**: `[101, 102, 103]` $\rightarrow$ **Output**: Immutable `List` `[101, 102, 103]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"Collectors.toUnmodifiableList"*, *"immutable collection stream"*.
- **Pattern**: **Collectors.toUnmodifiableList() / toUnmodifiableSet()**.

#### 🛠️ Step-by-Step Strategy
1. Stream customer IDs.
2. Collect via `Collectors.toUnmodifiableList()`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class ImmutableCollectNormal {
    public static List<Integer> collectImmutable(List<Integer> ids) {
        List<Integer> list = new ArrayList<>(ids);
        return Collections.unmodifiableList(list);
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class ImmutableCollectStream {
    public static List<Integer> collectImmutable(List<Integer> ids) {
        return ids.stream()
            .collect(Collectors.toUnmodifiableList());
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `Collections.unmodifiableList()` wrapper.
- **Stream API**: `Collectors.toUnmodifiableList()` provides direct immutability in Java 10+.

---

### Q78: Group Customers by Country and List Cities (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of customer profiles `List<Customer>`, group customers by Country code and map down to a list of distinct city names `Map<String, Set<String>>` where customers reside."*

#### 🛍️ Real-World Intuitive Story
Imagine a world atlas index. Under country heading `"USA"`, list unique cities (`"New York"`, `"San Francisco"`) where customers live.

#### 📥 Input & 📤 Output
- **Input**: `[Cust("USA", "NY"), Cust("USA", "SF"), Cust("USA", "NY")]`
- **Output**: `{USA=["NY", "SF"]}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"group country map downstream set of cities"*.
- **Pattern**: **Collectors.groupingBy(Customer::country, Collectors.mapping(Customer::city, Collectors.toSet()))**.

#### 🛠️ Step-by-Step Strategy
1. Stream customer objects.
2. Group by country string, mapping downstream city names into a `Set`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class GroupCustomerCitiesNormal {
    public record Customer(String country, String city) {}

    public static Map<String, Set<String>> getCitiesByCountry(List<Customer> customers) {
        Map<String, Set<String>> map = new HashMap<>();
        for (Customer c : customers) {
            map.computeIfAbsent(c.country(), k -> new HashSet<>()).add(c.city());
        }
        return map;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class GroupCustomerCitiesStream {
    public record Customer(String country, String city) {}

    public static Map<String, Set<String>> getCitiesByCountry(List<Customer> customers) {
        return customers.stream()
            .collect(Collectors.groupingBy(
                Customer::country,
                Collectors.mapping(Customer::city, Collectors.toSet())
            ));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `computeIfAbsent()` adding city strings into set buckets.
- **Stream API**: Composable `groupingBy` with downstream `mapping` collector to `toSet()`.
