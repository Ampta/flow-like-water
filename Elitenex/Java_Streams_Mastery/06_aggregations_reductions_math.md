# 📐 Category 6: Aggregations, Reductions & Math

This guide provides exhaustive, production-grade solutions for **20 Aggregations, Reductions & Math Problems** (Q99 – Q118). Every problem is presented with an authentic interview scenario, real-world intuitive story, step-by-step strategy, and dual implementation (Normal Java vs. Java Streams).

---

## 📑 Table of Contents
- [Q99: Sum of Elements (IntStream.sum) (Interview Classic)](#q99-sum-of-elements-intstreamsum-interview-classic)
- [Q100: Average of Numbers (IntStream.average) (Interview Classic)](#q100-average-of-numbers-intstreamaverage-interview-classic)
- [Q101: Product of Array Elements (Stream Reduction) (Interview Classic)](#q101-product-of-array-elements-stream-reduction-interview-classic)
- [Q102: Summary Statistics (DoubleSummaryStatistics) (Interview Classic)](#q102-summary-statistics-doublesummarystatistics-interview-classic)
- [Q103: Custom Accumulator / Reducer (Stream.reduce) (Interview Classic)](#q103-custom-accumulator--reducer-streamreduce-interview-classic)
- [Q104: Find Max Element (Stream.max) (LeetCode #215 Variation)](#q104-find-max-element-streammax-leetcode-215-variation)
- [Q105: Find Min Element (Stream.min) (Interview Classic)](#q105-find-min-element-streammin-interview-classic)
- [Q106: Count Elements Matching Criteria (Stream.count) (Interview Classic)](#q106-count-elements-matching-criteria-streamcount-interview-classic)
- [Q107: Calculate Factorial using Streams (Interview Classic)](#q107-calculate-factorial-using-streams-interview-classic)
- [Q108: Calculate Fibonacci Sequence up to N (LeetCode #509)](#q108-calculate-fibonacci-sequence-up-to-n-leetcode-509)
- [Q109: Sum of Squares of Even Numbers (Interview Classic)](#q109-sum-of-squares-of-even-numbers-interview-classic)
- [Q110: Longest String in List (Interview Classic)](#q110-longest-string-in-list-interview-classic)
- [Q111: Shortest String in List (Interview Classic)](#q111-shortest-string-in-list-interview-classic)
- [Q112: Compute Weighted Average (Interview Classic)](#q112-compute-weighted-average-interview-classic)
- [Q113: Median of Array / List (LeetCode #295 Variation)](#q113-median-of-array--list-leetcode-295-variation)
- [Q114: Mode of Array / List (Interview Classic)](#q114-mode-of-array--list-interview-classic)
- [Q115: Variance and Standard Deviation (Interview Classic)](#q115-variance-and-standard-deviation-interview-classic)
- [Q116: Cumulative Sum / Running Total (Interview Classic)](#q116-cumulative-sum--running-total-interview-classic)
- [Q117: Dot Product of Two Vectors (Interview Classic)](#q117-dot-product-of-two-vectors-interview-classic)
- [Q118: Range / Difference Between Max and Min (Interview Classic)](#q118-range--difference-between-max-and-min-interview-classic)

---

### Q99: Sum of Elements (IntStream.sum) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of transaction values `int[] amounts`, calculate the total sum of all transactions."*

#### 🛍️ Real-World Intuitive Story
Adding up receipts at the end of the day to find total cash revenue.

#### 📥 Input & 📤 Output
- **Input**: `[10, 20, 30, 40]` $\rightarrow$ **Output**: `100`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"sum of array elements"*.
- **Pattern**: **Arrays.stream(nums).sum()**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive integers.
2. Call `.sum()`.

#### ☕ Solution 1: Normal Java
```java
public class SumElementsNormal {
    public static int sum(int[] nums) {
        int total = 0;
        for (int num : nums) total += num;
        return total;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class SumElementsStream {
    public static int sum(int[] nums) {
        return Arrays.stream(nums).sum();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard `for` loop accumulator.
- **Stream API**: `Arrays.stream().sum()` provides direct primitive sum.

---

### Q100: Average of Numbers (IntStream.average) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Calculate the average test score from an array of student marks `int[] scores`."*

#### 🛍️ Real-World Intuitive Story
Summing all student test scores and dividing by total student count to calculate class average.

#### 📥 Input & 📤 Output
- **Input**: `[80, 90, 100]` $\rightarrow$ **Output**: `90.0`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"average score"*.
- **Pattern**: **IntStream.average().orElse(0.0)**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive score values.
2. Call `.average()` returning `OptionalDouble`.

#### ☕ Solution 1: Normal Java
```java
public class AverageNormal {
    public static double average(int[] scores) {
        if (scores == null || scores.length == 0) return 0.0;
        double sum = 0;
        for (int s : scores) sum += s;
        return sum / scores.length;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class AverageStream {
    public static double average(int[] scores) {
        return Arrays.stream(scores)
            .average()
            .orElse(0.0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Sum division loop with length check.
- **Stream API**: `IntStream.average()` returns `OptionalDouble` safely handling empty streams.

---

### Q101: Product of Array Elements (Stream Reduction) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of integer scale factors `int[] factors`, calculate the combined scalar product of all numbers."*

#### 🛍️ Real-World Intuitive Story
Multiplying magnification factors together ($2 \times 3 \times 4 = 24$).

#### 📥 Input & 📤 Output
- **Input**: `[2, 3, 4]` $\rightarrow$ **Output**: `24`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"product of array elements"*, *"reduction multiplication"*.
- **Pattern**: **Arrays.stream(nums).reduce(1, (a, b) -> a * b)**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive integers.
2. Reduce with initial identity `1` and multiplication lambda `(a, b) -> a * b`.

#### ☕ Solution 1: Normal Java
```java
public class ProductNormal {
    public static int product(int[] nums) {
        int p = 1;
        for (int num : nums) p *= num;
        return p;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class ProductStream {
    public static int product(int[] nums) {
        return Arrays.stream(nums)
            .reduce(1, (a, b) -> a * b);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard loop multiplication.
- **Stream API**: `.reduce(1, (a, b) -> a * b)` functional reduction.

---

### Q102: Summary Statistics (DoubleSummaryStatistics) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Generate a complete statistical summary (count, sum, min, max, average) for a list of salary double values `List<Double>`."*

#### 🛍️ Real-World Intuitive Story
Generating an executive payroll report displaying total budget, minimum salary, maximum salary, and average compensation.

#### 📥 Input & 📤 Output
- **Input**: `[5000.0, 8000.0, 12000.0]` $\rightarrow$ **Output**: `DoubleSummaryStatistics{count=3, sum=25000.0, min=5000.0, average=8333.33, max=12000.0}`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"summary statistics double"*.
- **Pattern**: **Collectors.summarizingDouble() / mapToDouble.summaryStatistics()**.

#### 🛠️ Step-by-Step Strategy
1. Stream double values using `mapToDouble(Double::doubleValue)`.
2. Call `.summaryStatistics()`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class SummaryStatsNormal {
    public static DoubleSummaryStatistics getStats(List<Double> list) {
        DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
        for (double d : list) stats.accept(d);
        return stats;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class SummaryStatsStream {
    public static DoubleSummaryStatistics getStats(List<Double> list) {
        return list.stream()
            .mapToDouble(Double::doubleValue)
            .summaryStatistics();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Manual `DoubleSummaryStatistics.accept()` loop.
- **Stream API**: `mapToDouble().summaryStatistics()` computes metrics in a single pass.

---

### Q103: Custom Accumulator / Reducer (Stream.reduce) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Combine a list of string words `List<String>` into a single hyphen-separated string using custom `Stream.reduce()`."*

#### 🛍️ Real-World Intuitive Story
Tying words together with hyphens (`"alpha-beta-gamma"`).

#### 📥 Input & 📤 Output
- **Input**: `["alpha", "beta", "gamma"]` $\rightarrow$ **Output**: `"alpha-beta-gamma"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"custom reduction combination"*.
- **Pattern**: **stream.reduce((a, b) -> a + "-" + b)**.

#### 🛠️ Step-by-Step Strategy
1. Stream non-empty words.
2. Reduce using binary operator `(a, b) -> a + "-" + b`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class CustomReduceNormal {
    public static String hyphenate(List<String> words) {
        if (words == null || words.isEmpty()) return "";
        StringBuilder sb = new StringBuilder(words.get(0));
        for (int i = 1; i < words.size(); i++) {
            sb.append("-").append(words.get(i));
        }
        return sb.toString();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;

public class CustomReduceStream {
    public static String hyphenate(List<String> words) {
        return words.stream()
            .reduce((a, b) -> a + "-" + b)
            .orElse("");
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: StringBuilder loop managing leading hyphen offsets.
- **Stream API**: Functional binary reduction `reduce((a, b) -> a + "-" + b)`.

---

### Q104: Find Max Element (Stream.max) (LeetCode #215 Variation)

#### 📄 Real-World Interview Problem Statement
> *"Find the maximum value in an array of integers `int[] nums`."*

#### 🛍️ Real-World Intuitive Story
Scanning a list of high scores to find the all-time maximum score achieved.

#### 📥 Input & 📤 Output
- **Input**: `[5, 12, 3, 99, 42]` $\rightarrow$ **Output**: `99`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"find maximum value"*.
- **Pattern**: **Arrays.stream(nums).max()**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive integers.
2. Call `.max()` returning `OptionalInt`.

#### ☕ Solution 1: Normal Java
```java
public class FindMaxNormal {
    public static int findMax(int[] nums) {
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
            if (num > max) max = num;
        }
        return max;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class FindMaxStream {
    public static int findMax(int[] nums) {
        return Arrays.stream(nums)
            .max()
            .orElseThrow();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Manual `Integer.MIN_VALUE` comparison loop.
- **Stream API**: `Arrays.stream().max()` declarative primitive extrema finder.

---

### Q105: Find Min Element (Stream.min) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Find the minimum temperature value in an array of daily recordings `double[] temps`."*

#### 🛍️ Real-World Intuitive Story
Checking a list of weather recordings to find the lowest temperature recorded during winter.

#### 📥 Input & 📤 Output
- **Input**: `[15.5, -3.2, 0.0, 12.1]` $\rightarrow$ **Output**: `-3.2`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"find minimum value"*.
- **Pattern**: **Arrays.stream(temps).min()**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive double values.
2. Call `.min()`.

#### ☕ Solution 1: Normal Java
```java
public class FindMinNormal {
    public static double findMin(double[] temps) {
        double min = Double.MAX_VALUE;
        for (double t : temps) {
            if (t < min) min = t;
        }
        return min;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class FindMinStream {
    public static double findMin(double[] temps) {
        return Arrays.stream(temps)
            .min()
            .orElseThrow();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard loop tracking running minimum.
- **Stream API**: `Arrays.stream().min()` returning `OptionalDouble`.

---

### Q106: Count Elements Matching Criteria (Stream.count) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of strings `List<String>`, count how many strings have a length strictly greater than 5."*

#### 🛍️ Real-World Intuitive Story
Counting long words in a document that exceed 5 letters.

#### 📥 Input & 📤 Output
- **Input**: `["apple", "banana", "kiwi", "watermelon"]` $\rightarrow$ **Output**: `2` ("banana", "watermelon")

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"count matching criteria"*.
- **Pattern**: **stream.filter(predicate).count()**.

#### 🛠️ Step-by-Step Strategy
1. Stream words.
2. Filter length $> 5$.
3. Call `.count()`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class CountCriteriaNormal {
    public static long countLongWords(List<String> words) {
        long count = 0;
        for (String w : words) {
            if (w != null && w.length() > 5) count++;
        }
        return count;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;

public class CountCriteriaStream {
    public static long countLongWords(List<String> words) {
        return words.stream()
            .filter(w -> w != null && w.length() > 5)
            .count();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Loop accumulator count variable.
- **Stream API**: Functional `.filter().count()` stream pipeline.

---

### Q107: Calculate Factorial using Streams (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Calculate the factorial ($n!$) of a given integer `n` using Java streams."*

#### 🛍️ Real-World Intuitive Story
Multiplying integers sequentially from 1 to $n$ ($5! = 1 \times 2 \times 3 \times 4 \times 5 = 120$).

#### 📥 Input & 📤 Output
- **Input**: `n = 5` $\rightarrow$ **Output**: `120`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"factorial stream"*.
- **Pattern**: **LongStream.rangeClosed(1, n).reduce(1, (a, b) -> a * b)**.

#### 🛠️ Step-by-Step Strategy
1. Stream range `1` to `n`.
2. Reduce multiplying accumulators.

#### ☕ Solution 1: Normal Java
```java
public class FactorialNormal {
    public static long factorial(int n) {
        long res = 1;
        for (int i = 1; i <= n; i++) res *= i;
        return res;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.LongStream;

public class FactorialStream {
    public static long factorial(int n) {
        return LongStream.rangeClosed(1, n)
            .reduce(1, (a, b) -> a * b);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Iterative multiplication loop.
- **Stream API**: `LongStream.rangeClosed().reduce()` functional factorial.

---

### Q108: Calculate Fibonacci Sequence up to N (LeetCode #509)

#### 📄 Real-World Interview Problem Statement
> *"Generate the first `n` terms of the Fibonacci sequence `[0, 1, 1, 2, 3, 5, 8, ...]` using Stream iteration."*

#### 🛍️ Real-World Intuitive Story
Generating number pairs where each new number is the sum of the previous two numbers.

#### 📥 Input & 📤 Output
- **Input**: `n = 7` $\rightarrow$ **Output**: `[0, 1, 1, 2, 3, 5, 8]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"fibonacci sequence stream generator"*.
- **Pattern**: **Stream.iterate(new long[]{0, 1}, f -> new long[]{f[1], f[0] + f[1]}).limit(n)**.

#### 🛠️ Step-by-Step Strategy
1. Seed `Stream.iterate` with array pair `[0, 1]`.
2. Generate next pair `[f[1], f[0] + f[1]]`.
3. Map to `f[0]` and limit to `n`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FibonacciNormal {
    public static List<Long> generateFib(int n) {
        List<Long> fib = new ArrayList<>();
        if (n <= 0) return fib;
        long a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            fib.add(a);
            long next = a + b;
            a = b;
            b = next;
        }
        return fib;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Stream;

public class FibonacciStream {
    public static List<Long> generateFib(int n) {
        return Stream.iterate(new long[]{0, 1}, f -> new long[]{f[1], f[0] + f[1]})
            .limit(n)
            .map(f -> f[0])
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Iterative variable swap loop.
- **Stream API**: `Stream.iterate()` tuples functional generator.

---

### Q109: Sum of Squares of Even Numbers (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of integers `int[] nums`, filter all even numbers, square each of them, and compute the total sum."*

#### 🛍️ Real-World Intuitive Story
Selecting even numbers from a list ($2, 4$), squaring them ($4, 16$), and adding them up ($4 + 16 = 20$).

#### 📥 Input & 📤 Output
- **Input**: `[1, 2, 3, 4]` $\rightarrow$ **Output**: `20` ($2^2 + 4^2 = 4 + 16$)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"sum of squares of even numbers"*.
- **Pattern**: **Arrays.stream(nums).filter(even).map(n -> n * n).sum()**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive integers.
2. Filter even numbers `n % 2 == 0`.
3. Map to square `n * n` and compute `.sum()`.

#### ☕ Solution 1: Normal Java
```java
public class SumSquaresNormal {
    public static int sumOfSquaresOfEvens(int[] nums) {
        int sum = 0;
        for (int n : nums) {
            if (n % 2 == 0) {
                sum += n * n;
            }
        }
        return sum;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class SumSquaresStream {
    public static int sumOfSquaresOfEvens(int[] nums) {
        return Arrays.stream(nums)
            .filter(n -> n % 2 == 0)
            .map(n -> n * n)
            .sum();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Conditional check + accumulator inside loop.
- **Stream API**: Clean pipeline `.filter().map().sum()`.

---

### Q110: Longest String in List (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of candidate strings `List<String>`, find the string that has the longest character length."*

#### 🛍️ Real-World Intuitive Story
Measuring word banners with a tape measure to pick the longest banner.

#### 📥 Input & 📤 Output
- **Input**: `["cat", "elephant", "dog"]` $\rightarrow$ **Output**: `"elephant"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"longest string in list"*.
- **Pattern**: **stream.max(Comparator.comparingInt(String::length))**.

#### 🛠️ Step-by-Step Strategy
1. Stream strings.
2. Find max using `Comparator.comparingInt(String::length)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class LongestStringNormal {
    public static String findLongest(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        String longest = list.get(0);
        for (String s : list) {
            if (s.length() > longest.length()) {
                longest = s;
            }
        }
        return longest;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class LongestStringStream {
    public static String findLongest(List<String> list) {
        return list.stream()
            .max(Comparator.comparingInt(String::length))
            .orElse(null);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Manual max-length string tracking loop.
- **Stream API**: `.max(Comparator.comparingInt(String::length))`.

---

### Q111: Shortest String in List (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of strings `List<String>`, find the string with the shortest character length."*

#### 🛍️ Real-World Intuitive Story
Scanning a list of names to find the shortest abbreviated name.

#### 📥 Input & 📤 Output
- **Input**: `["cat", "elephant", "ox"]` $\rightarrow$ **Output**: `"ox"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"shortest string in list"*.
- **Pattern**: **stream.min(Comparator.comparingInt(String::length))**.

#### 🛠️ Step-by-Step Strategy
1. Stream string list.
2. Find min using `Comparator.comparingInt(String::length)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class ShortestStringNormal {
    public static String findShortest(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        String shortest = list.get(0);
        for (String s : list) {
            if (s.length() < shortest.length()) {
                shortest = s;
            }
        }
        return shortest;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class ShortestStringStream {
    public static String findShortest(List<String> list) {
        return list.stream()
            .min(Comparator.comparingInt(String::length))
            .orElse(null);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard string length comparison loop.
- **Stream API**: `.min(Comparator.comparingInt(String::length))`.

---

### Q112: Compute Weighted Average (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a list of course items `List<Item>` where each item has a grade score and weight factor, compute the overall weighted average grade."*

#### 🛍️ Real-World Intuitive Story
Computing a final course mark where Exams count for 60% and Homework counts for 40%.

#### 📥 Input & 📤 Output
- **Input**: `[Item(80, 0.6), Item(90, 0.4)]` $\rightarrow$ **Output**: `84.0` ($80 \times 0.6 + 90 \times 0.4 = 48 + 36$)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"weighted average sum product"*.
- **Pattern**: **sum(score * weight) / sum(weight)**.

#### 🛠️ Step-by-Step Strategy
1. Stream items to calculate total weighted product sum.
2. Calculate sum of weights.
3. Divide product sum by weight sum.

#### ☕ Solution 1: Normal Java
```java
import java.util.List;

public class WeightedAverageNormal {
    public record Item(double score, double weight) {}

    public static double computeWeightedAverage(List<Item> items) {
        double totalProduct = 0;
        double totalWeight = 0;
        for (Item item : items) {
            totalProduct += item.score() * item.weight();
            totalWeight += item.weight();
        }
        return totalWeight == 0 ? 0.0 : totalProduct / totalWeight;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.List;

public class WeightedAverageStream {
    public record Item(double score, double weight) {}

    public static double computeWeightedAverage(List<Item> items) {
        double totalProduct = items.stream().mapToDouble(i -> i.score() * i.weight()).sum();
        double totalWeight = items.stream().mapToDouble(Item::weight).sum();
        return totalWeight == 0 ? 0.0 : totalProduct / totalWeight;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Accumulates product and weight in a single pass.
- **Stream API**: Two mapToDouble sum pipelines divided cleanly.

---

### Q113: Median of Array / List (LeetCode #295 Variation)

#### 📄 Real-World Interview Problem Statement
> *"Given an unsorted array of numbers `int[] nums`, compute the statistical median value."*

#### 🛍️ Real-World Intuitive Story
Line up all numbers in ascending order. If odd count, pick the middle element; if even count, average the two middle elements.

#### 📥 Input & 📤 Output
- **Input**: `[3, 1, 2]` $\rightarrow$ **Output**: `2.0`
- **Input**: `[1, 2, 3, 4]` $\rightarrow$ **Output**: `2.5`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"median value of array"*.
- **Pattern**: **Arrays.stream(nums).sorted() -> pick middle indices**.

#### 🛠️ Step-by-Step Strategy
1. Sort input array ascending.
2. Calculate middle index `n / 2`.
3. Return middle value or average of `n/2 - 1` and `n/2`.

#### ☕ Solution 1: Normal Java
```java
import java.util.Arrays;

public class MedianNormal {
    public static double findMedian(int[] nums) {
        if (nums == null || nums.length == 0) return 0.0;
        Arrays.sort(nums);
        int n = nums.length;
        if (n % 2 != 0) {
            return nums[n / 2];
        } else {
            return (nums[n / 2 - 1] + nums[n / 2]) / 2.0;
        }
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class MedianStream {
    public static double findMedian(int[] nums) {
        int[] sorted = Arrays.stream(nums).sorted().toArray();
        int n = sorted.length;
        if (n == 0) return 0.0;
        return n % 2 != 0
            ? sorted[n / 2]
            : (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: In-place sort followed by conditional index selection.
- **Stream API**: Stream sort creates sorted array copy before median index calculation.

---

### Q114: Mode of Array / List (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Find the statistical mode (most frequently occurring number) in an array of integers `int[] nums`."*

#### 🛍️ Real-World Intuitive Story
Counting votes cast for each number to find which number appears most often.

#### 📥 Input & 📤 Output
- **Input**: `[1, 2, 2, 3, 3, 3, 4]` $\rightarrow$ **Output**: `3`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"mode of array"*, *"most frequent value"*.
- **Pattern**: **groupingBy counting -> max by entry value**.

#### 🛠️ Step-by-Step Strategy
1. Build frequency map of elements.
2. Extract key with maximum frequency value.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class ModeNormal {
    public static int findMode(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) map.put(num, map.getOrDefault(num, 0) + 1);

        int mode = nums[0], maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mode = entry.getKey();
            }
        }
        return mode;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModeStream {
    public static int findMode(int[] nums) {
        return Arrays.stream(nums)
            .boxed()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseThrow();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: HashMap tallying and max tracking loop.
- **Stream API**: `groupingBy() -> max(comparingByValue())`.

---

### Q115: Variance and Standard Deviation (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Compute the population standard deviation for an array of numeric readings `double[] data`."*

#### 🛍️ Real-World Intuitive Story
Measuring how widely scattered numbers are from their average value ($\sqrt{\text{variance}}$).

#### 📥 Input & 📤 Output
- **Input**: `[10.0, 12.0, 23.0, 23.0, 16.0, 23.0, 21.0, 16.0]` $\rightarrow$ **Output**: `StdDev ≈ 4.89`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"standard deviation variance stream"*.
- **Pattern**: **mean = average(), variance = mean((x - mean)^2)**.

#### 🛠️ Step-by-Step Strategy
1. Calculate mean of array elements.
2. Stream array to sum squared differences `Math.pow(x - mean, 2)`.
3. Take `Math.sqrt(variance)`.

#### ☕ Solution 1: Normal Java
```java
public class StandardDeviationNormal {
    public static double computeStdDev(double[] data) {
        if (data == null || data.length == 0) return 0.0;
        double sum = 0;
        for (double d : data) sum += d;
        double mean = sum / data.length;

        double sumSqDiff = 0;
        for (double d : data) {
            sumSqDiff += Math.pow(d - mean, 2);
        }
        double variance = sumSqDiff / data.length;
        return Math.sqrt(variance);
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class StandardDeviationStream {
    public static double computeStdDev(double[] data) {
        if (data == null || data.length == 0) return 0.0;
        double mean = Arrays.stream(data).average().orElse(0.0);

        double variance = Arrays.stream(data)
            .map(d -> Math.pow(d - mean, 2))
            .average()
            .orElse(0.0);

        return Math.sqrt(variance);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Two sequential accumulation loops.
- **Stream API**: Two `Arrays.stream()` pipeline calls calculating mean and variance.

---

### Q116: Cumulative Sum / Running Total (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of daily sales `int[] sales`, generate a running total array where `result[i]` is the cumulative sum of sales up to index `i`."*

#### 🛍️ Real-World Intuitive Story
Keeping a running total on a cash register receipt so you see total money accumulated after each purchase.

#### 📥 Input & 📤 Output
- **Input**: `[10, 20, 30]` $\rightarrow$ **Output**: `[10, 30, 60]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"running total cumulative sum"*.
- **Pattern**: **IntStream.range + stateful accumulator map**.

#### 🛠️ Step-by-Step Strategy
1. Maintain running sum state array `int[] sum = {0}`.
2. Stream primitive integers mapping `sum[0] += x`.

#### ☕ Solution 1: Normal Java
```java
public class CumulativeSumNormal {
    public static int[] runningSum(int[] nums) {
        int[] result = new int[nums.length];
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            result[i] = sum;
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class CumulativeSumStream {
    public static int[] runningSum(int[] nums) {
        int[] sum = {0};
        return Arrays.stream(nums)
            .map(x -> sum[0] += x)
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard `for` loop updating running total variable.
- **Stream API**: Stateful `.map(x -> sum[0] += x)` pipeline.

---

### Q117: Dot Product of Two Vectors (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given two integer vectors `int[] v1` and `int[] v2` of equal length, compute their dot product ($v1[0]*v2[0] + v1[1]*v2[1] + ...$)."*

#### 🛍️ Real-World Intuitive Story
Multiplying price per item by quantity purchased for every item on a shopping list and adding the totals.

#### 📥 Input & 📤 Output
- **Input**: `v1 = [1, 2, 3]`, `v2 = [4, 5, 6]` $\rightarrow$ **Output**: `32` ($1 \times 4 + 2 \times 5 + 3 \times 6 = 4 + 10 + 18$)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"dot product of two vectors"*.
- **Pattern**: **IntStream.range(0, v1.length).map(i -> v1[i] * v2[i]).sum()**.

#### 🛠️ Step-by-Step Strategy
1. Stream indices `0` to `v1.length - 1`.
2. Map `i -> v1[i] * v2[i]`.
3. Compute `.sum()`.

#### ☕ Solution 1: Normal Java
```java
public class DotProductNormal {
    public static int dotProduct(int[] v1, int[] v2) {
        int sum = 0;
        for (int i = 0; i < v1.length; i++) {
            sum += v1[i] * v2[i];
        }
        return sum;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.IntStream;

public class DotProductStream {
    public static int dotProduct(int[] v1, int[] v2) {
        return IntStream.range(0, v1.length)
            .map(i -> v1[i] * v2[i])
            .sum();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Single index-based loop.
- **Stream API**: `IntStream.range().map().sum()` clean 1-liner.

---

### Q118: Range / Difference Between Max and Min (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of sensor telemetry values `int[] values`, compute the total range (difference between maximum and minimum value)."*

#### 🛍️ Real-World Intuitive Story
Finding temperature fluctuation during a day by subtracting the lowest temperature from the highest temperature recorded.

#### 📥 Input & 📤 Output
- **Input**: `[10, 50, 2, 90]` $\rightarrow$ **Output**: `88` ($90 - 2$)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"range difference max min"*.
- **Pattern**: **IntSummaryStatistics.getMax() - getMin()**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive values into `IntSummaryStatistics`.
2. Calculate `stats.getMax() - stats.getMin()`.

#### ☕ Solution 1: Normal Java
```java
public class RangeNormal {
    public static int findRange(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int min = nums[0], max = nums[0];
        for (int n : nums) {
            if (n < min) min = n;
            if (n > max) max = n;
        }
        return max - min;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class RangeStream {
    public static int findRange(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        IntSummaryStatistics stats = Arrays.stream(nums).summaryStatistics();
        return stats.getMax() - stats.getMin();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Single pass tracking min and max.
- **Stream API**: `summaryStatistics()` calculates extrema in a single pass.
