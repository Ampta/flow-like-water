# 🪟 Category 8: Sliding Window, Generators & Infinite Streams

This guide provides exhaustive, production-grade solutions for **12 Sliding Window, Generators & Infinite Streams Problems** (Q134 – Q145). Every problem is presented with an authentic interview scenario, real-world intuitive story, step-by-step strategy, and dual implementation (Normal Java vs. Java Streams).

---

## 📑 Table of Contents
- [Q134: Fixed-Size Sliding Window Average (LeetCode #643)](#q134-fixed-size-sliding-window-average-leetcode-643)
- [Q135: Custom Finite Stream Generator (Stream.generate) (Interview Classic)](#q135-custom-finite-stream-generator-streamgenerate-interview-classic)
- [Q136: Custom Infinite Stream Generator with Stream.iterate (Interview Classic)](#q136-custom-infinite-stream-generator-with-streamiterate-interview-classic)
- [Q137: Stream.takeWhile (Java 9+) (Interview Classic)](#q137-streamtakewhile-java-9-interview-classic)
- [Q138: Stream.dropWhile (Java 9+) (Interview Classic)](#q138-streamdropwhile-java-9-interview-classic)
- [Q139: Max Consecutive Ones (LeetCode #485)](#q139-max-consecutive-ones-leetcode-485)
- [Q140: Sliding Window Maximum Sum of Subarray Size K (Interview Classic)](#q140-sliding-window-maximum-sum-of-subarray-size-k-interview-classic)
- [Q141: Fibonacci Generator using Stream.iterate (Interview Classic)](#q141-fibonacci-generator-using-streamiterate-interview-classic)
- [Q142: Random Number Stream Generator (Interview Classic)](#q142-random-number-stream-generator-interview-classic)
- [Q143: Windowed Sublists Partitioning (Custom Window Collector) (Interview Classic)](#q143-windowed-sublists-partitioning-custom-window-collector-interview-classic)
- [Q144: Batch Stream Processing (Partition Stream into Fixed Batches) (Interview Classic)](#q144-batch-stream-processing-partition-stream-into-fixed-batches-interview-classic)
- [Q145: Infinite Prime Number Stream Generator (Interview Classic)](#q145-infinite-prime-number-stream-generator-interview-classic)

---

### Q134: Fixed-Size Sliding Window Average (LeetCode #643)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums` consisting of `n` elements and an integer `k`, find a contiguous subarray whose length is `k` that has the maximum average value and return this value."*

#### 🛍️ Real-World Intuitive Story
Moving a 7-day temperature frame across a year of weather data to find the hottest week average.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,12,-5,-6,50,3]`, `k = 4` $\rightarrow$ **Output**: `12.75` (Subarray `[12, -5, -6, 50]`)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"max average subarray window k"*.
- **Pattern**: **Sliding Window Sum / IntStream Range Map**.

#### 🛠️ Step-by-Step Strategy
1. Compute initial sum of first $k$ elements.
2. Slide window right, adding new right element and subtracting left element.
3. Track maximum window sum seen and divide by $k.0$.

#### ☕ Solution 1: Normal Java
```java
public class SlidingWindowAvgNormal {
    public static double findMaxAverage(int[] nums, int k) {
        int sum = 0;
        for (int i = 0; i < k; i++) sum += nums[i];
        int maxSum = sum;

        for (int i = k; i < nums.length; i++) {
            sum += nums[i] - nums[i - k];
            maxSum = Math.max(maxSum, sum);
        }
        return (double) maxSum / k;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class SlidingWindowAvgStream {
    public static double findMaxAverage(int[] nums, int k) {
        int[] windowSum = {IntStream.range(0, k).map(i -> nums[i]).sum()};
        int[] max = {windowSum[0]};

        IntStream.range(k, nums.length).forEach(i -> {
            windowSum[0] += nums[i] - nums[i - k];
            max[0] = Math.max(max[0], windowSum[0]);
        });

        return (double) max[0] / k;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: $O(N)$ sliding window loop updating running total.
- **Stream API**: `IntStream.range()` stateful sliding window processing.

---

### Q135: Custom Finite Stream Generator (Stream.generate) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Generate a finite stream of `n` random UUID authorization token strings using `Stream.generate()`."*

#### 🛍️ Real-World Intuitive Story
A ticket dispenser machine printing custom entry passes one at a time on demand.

#### 📥 Input & 📤 Output
- **Input**: `n = 3` $\rightarrow$ **Output**: `List<String>` containing 3 UUID strings.

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"Stream.generate random tokens"*.
- **Pattern**: **Stream.generate(Supplier).limit(n)**.

#### 🛠️ Step-by-Step Strategy
1. Pass `UUID::randomUUID` supplier to `Stream.generate()`.
2. Apply `.limit(n)`.
3. Map to string and collect to list.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class StreamGenerateNormal {
    public static List<String> generateTokens(int n) {
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tokens.add(UUID.randomUUID().toString());
        }
        return tokens;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Stream;

public class StreamGenerateStream {
    public static List<String> generateTokens(int n) {
        return Stream.generate(UUID::randomUUID)
            .map(UUID::toString)
            .limit(n)
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard `for` loop adding generated UUIDs.
- **Stream API**: `Stream.generate(Supplier).limit(n)` functional infinite supplier stream.

---

### Q136: Custom Infinite Stream Generator with Stream.iterate (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Generate an infinite stream of powers of two ($1, 2, 4, 8, 16, 32, ...$) using `Stream.iterate()` and take the first `n` elements."*

#### 🛍️ Real-World Intuitive Story
A conveyor belt generating exponential numbers continuously on demand.

#### 📥 Input & 📤 Output
- **Input**: `n = 5` $\rightarrow$ **Output**: `[1, 2, 4, 8, 16]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"Stream.iterate infinite powers of 2"*.
- **Pattern**: **LongStream.iterate(1, n -> n * 2).limit(count)**.

#### 🛠️ Step-by-Step Strategy
1. Seed `LongStream.iterate` with initial value `1L`.
2. Pass unary operator `n -> n * 2`.
3. Limit stream to `count` elements.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class PowersOfTwoNormal {
    public static List<Long> getPowersOfTwo(int count) {
        List<Long> result = new ArrayList<>();
        long val = 1;
        for (int i = 0; i < count; i++) {
            result.add(val);
            val *= 2;
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.LongStream;

public class PowersOfTwoStream {
    public static List<Long> getPowersOfTwo(int count) {
        return LongStream.iterate(1L, n -> n * 2)
            .limit(count)
            .boxed()
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Iterative multiplication loop.
- **Stream API**: `LongStream.iterate(seed, f).limit()` infinite functional generator.

---

### Q137: Stream.takeWhile (Java 9+) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a sorted array of stock values `int[] prices`, take all stock values while they remain strictly below target value `threshold` using Java 9 `takeWhile()`."*

#### 🛍️ Real-World Intuitive Story
Pouring coffee until the cup gets 80% full, then stopping immediately.

#### 📥 Input & 📤 Output
- **Input**: `[10, 20, 30, 45, 60, 75]`, `threshold = 50` $\rightarrow$ **Output**: `[10, 20, 30, 45]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"takeWhile take elements until predicate fails"*.
- **Pattern**: **stream.takeWhile(predicate)**.

#### 🛠️ Step-by-Step Strategy
1. Stream numbers.
2. Apply `.takeWhile(val -> val < threshold)`.
3. Collect to list.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class TakeWhileNormal {
    public static List<Integer> takeBelow(int[] nums, int threshold) {
        List<Integer> result = new ArrayList<>();
        for (int num : nums) {
            if (num >= threshold) break;
            result.add(num);
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class TakeWhileStream {
    public static List<Integer> takeBelow(int[] nums, int threshold) {
        return Arrays.stream(nums)
            .boxed()
            .takeWhile(val -> val < threshold)
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Loop with conditional `break`.
- **Stream API**: `takeWhile()` automatically aborts processing as soon as predicate returns false (Java 9+).

---

### Q138: Stream.dropWhile (Java 9+) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a sorted list of timestamps `List<Integer>`, discard all preamble initialization headers while timestamp is below start threshold `startThreshold`, taking all remaining timestamps using Java 9 `dropWhile()`."*

#### 🛍️ Real-World Intuitive Story
Skipping opening commercial ads at the start of a video stream and watching everything from the start of the movie onward.

#### 📥 Input & 📤 Output
- **Input**: `[5, 10, 15, 20, 25]`, `startThreshold = 15` $\rightarrow$ **Output**: `[15, 20, 25]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"dropWhile discard prefix until predicate fails"*.
- **Pattern**: **stream.dropWhile(predicate)**.

#### 🛠️ Step-by-Step Strategy
1. Stream timestamps.
2. Call `.dropWhile(t -> t < startThreshold)`.
3. Collect remaining elements into list.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class DropWhileNormal {
    public static List<Integer> dropPreamble(List<Integer> list, int threshold) {
        List<Integer> result = new ArrayList<>();
        boolean dropping = true;
        for (int val : list) {
            if (dropping && val < threshold) {
                continue;
            }
            dropping = false;
            result.add(val);
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class DropWhileStream {
    public static List<Integer> dropPreamble(List<Integer> list, int threshold) {
        return list.stream()
            .dropWhile(val -> val < threshold)
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Boolean state flag loop skipping prefix elements.
- **Stream API**: `dropWhile()` skips prefix elements matching predicate (Java 9+).

---

### Q139: Max Consecutive Ones (LeetCode #485)

#### 📄 Real-World Interview Problem Statement
> *"Given a binary array `nums`, return the maximum number of consecutive `1`s in the array."*

#### 🛍️ Real-World Intuitive Story
Counting the longest streak of consecutive winning games (1s) without a single loss (0).

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,1,0,1,1,1]` $\rightarrow$ **Output**: `3`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"max consecutive ones"*.
- **Pattern**: **Split string by 0 / Stateful Stream max counter**.

#### 🛠️ Step-by-Step Strategy
1. Convert binary array to string.
2. Split by `"0"`.
3. Map segments to `String::length` and find max.

#### ☕ Solution 1: Normal Java
```java
public class MaxConsecutiveOnesNormal {
    public static int findMaxConsecutiveOnes(int[] nums) {
        int max = 0, count = 0;
        for (int num : nums) {
            if (num == 1) {
                count++;
                max = Math.max(max, count);
            } else {
                count = 0;
            }
        }
        return max;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class MaxConsecutiveOnesStream {
    public static int findMaxConsecutiveOnes(int[] nums) {
        return Arrays.stream(
            Arrays.toString(nums).replaceAll("[\\[\\], ]", "").split("0")
        )
        .mapToInt(String::length)
        .max()
        .orElse(0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Single pass tracking current streak and max streak.
- **Stream API**: String splitting by 0 and taking max segment length.

---

### Q140: Sliding Window Maximum Sum of Subarray Size K (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of integers `int[] nums` and window size `k`, calculate the maximum sum of any contiguous subarray of size `k`."*

#### 🛍️ Real-World Intuitive Story
Sliding a magnifying glass over a row of numbers to find which group of `k` consecutive numbers gives the highest total sum.

#### 📥 Input & 📤 Output
- **Input**: `nums = [2, 1, 5, 1, 3, 2]`, `k = 3` $\rightarrow$ **Output**: `9` (Subarray `[5, 1, 3]`)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"max sum subarray size k"*.
- **Pattern**: **IntStream.range(0, n - k + 1).map(i -> windowSum).max()**.

#### 🛠️ Step-by-Step Strategy
1. Stream starting window indices `0` to `n - k`.
2. Compute sum for each window `Arrays.stream(nums, i, i + k).sum()`.
3. Take `.max()`.

#### ☕ Solution 1: Normal Java
```java
public class MaxSubarraySumNormal {
    public static int maxSubarraySum(int[] nums, int k) {
        if (nums == null || nums.length < k) return 0;
        int windowSum = 0;
        for (int i = 0; i < k; i++) windowSum += nums[i];
        int maxSum = windowSum;

        for (int i = k; i < nums.length; i++) {
            windowSum += nums[i] - nums[i - k];
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class MaxSubarraySumStream {
    public static int maxSubarraySum(int[] nums, int k) {
        if (nums == null || nums.length < k) return 0;
        return IntStream.range(0, nums.length - k + 1)
            .map(i -> Arrays.stream(nums, i, i + k).sum())
            .max()
            .orElse(0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: $O(N)$ sliding window subtract-old/add-new algorithm.
- **Stream API**: `IntStream.range()` mapped to window sum streams.

---

### Q141: Fibonacci Generator using Stream.iterate (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Generate an infinite Stream of Fibonacci numbers using `Stream.iterate` and collect the first `n` terms."*

#### 🛍️ Real-World Intuitive Story
A Fibonacci number generator button that prints the next Fibonacci number every time you press it.

#### 📥 Input & 📤 Output
- **Input**: `n = 6` $\rightarrow$ **Output**: `[0, 1, 1, 2, 3, 5]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"Stream.iterate fibonacci"*.
- **Pattern**: **Stream.iterate(new long[]{0, 1}, f -> new long[]{f[1], f[0] + f[1]})**.

#### 🛠️ Step-by-Step Strategy
1. Seed `Stream.iterate` with array pair `[0, 1]`.
2. Pass generator lambda `f -> new long[]{f[1], f[0] + f[1]}`.
3. Map to `f[0]` and limit to `n`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FibGeneratorNormal {
    public static List<Long> generate(int n) {
        List<Long> res = new ArrayList<>();
        long a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            res.add(a);
            long next = a + b;
            a = b;
            b = next;
        }
        return res;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Stream;

public class FibGeneratorStream {
    public static List<Long> generate(int n) {
        return Stream.iterate(new long[]{0, 1}, f -> new long[]{f[1], f[0] + f[1]})
            .limit(n)
            .map(f -> f[0])
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Pair swap loop.
- **Stream API**: `Stream.iterate()` with tuple generator function.

---

### Q142: Random Number Stream Generator (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Generate a list of `count` random integer values between `min` (inclusive) and `max` (exclusive) using `Random.ints()` stream."*

#### 🛍️ Real-World Intuitive Story
Rolling a 6-sided die $N$ times and recording all results.

#### 📥 Input & 📤 Output
- **Input**: `count = 5`, `min = 1`, `max = 7` $\rightarrow$ **Output**: `List<Integer>` of 5 random die roll values (e.g., `[4, 1, 6, 2, 5]`).

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"Random.ints stream generator"*.
- **Pattern**: **new Random().ints(count, min, max)**.

#### 🛠️ Step-by-Step Strategy
1. Instantiate `new Random()`.
2. Call `.ints(count, min, max)`.
3. Box and collect to list.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class RandomGeneratorNormal {
    public static List<Integer> generateRandoms(int count, int min, int max) {
        Random rand = new Random();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(rand.nextInt(max - min) + min);
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class RandomGeneratorStream {
    public static List<Integer> generateRandoms(int count, int min, int max) {
        return new Random().ints(count, min, max)
            .boxed()
            .collect(Collectors.toList());
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Loop calling `rand.nextInt()`.
- **Stream API**: `Random.ints(count, origin, bound)` native stream generator.

---

### Q143: Windowed Sublists Partitioning (Custom Window Collector) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Partition a list of items `List<T>` into overlapping sliding windows of size `windowSize` with step `stepSize`."*

#### 🛍️ Real-World Intuitive Story
Creating rolling 3-day window reports moving 1 day at a time across a month.

#### 📥 Input & 📤 Output
- **Input**: `[1, 2, 3, 4, 5]`, `windowSize = 3`, `stepSize = 1` $\rightarrow$ **Output**: `[[1,2,3], [2,3,4], [3,4,5]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"windowed sublists partition"*.
- **Pattern**: **IntStream.range(0, size - window + 1).mapToObj(i -> subList(i, i + window))**.

#### 🛠️ Step-by-Step Strategy
1. Stream starting indices `0` to `list.size() - windowSize`.
2. Map index `i` to `list.subList(i, i + windowSize)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class WindowedSublistsNormal {
    public static <T> List<List<T>> window(List<T> list, int windowSize) {
        List<List<T>> result = new ArrayList<>();
        if (list == null || list.size() < windowSize) return result;
        for (int i = 0; i <= list.size() - windowSize; i++) {
            result.add(new ArrayList<>(list.subList(i, i + windowSize)));
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.IntStream;

public class WindowedSublistsStream {
    public static <T> List<List<T>> window(List<T> list, int windowSize) {
        if (list == null || list.size() < windowSize) return Collections.emptyList();
        return IntStream.range(0, list.size() - windowSize + 1)
            .mapToObj(i -> list.subList(i, i + windowSize))
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard loop adding sublists.
- **Stream API**: `IntStream.range()` mapped to list slices.

---

### Q144: Batch Stream Processing (Partition Stream into Fixed Batches) (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a batch database writer, partition a large list `List<T>` into non-overlapping batches of fixed size `batchSize`."*

#### 🛍️ Real-World Intuitive Story
Packing 100 items into boxes of 10 items each before loading onto a shipping truck.

#### 📥 Input & 📤 Output
- **Input**: `[1, 2, 3, 4, 5, 6, 7]`, `batchSize = 3` $\rightarrow$ **Output**: `[[1,2,3], [4,5,6], [7]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"partition stream into fixed batches"*.
- **Pattern**: **IntStream.range(0, (size + batch - 1) / batch).mapToObj(slice)**.

#### 🛠️ Step-by-Step Strategy
1. Compute total batch count `(list.size() + batchSize - 1) / batchSize`.
2. Map batch index `i` to subList slice `i * batchSize` to `min((i + 1) * batchSize, size)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class BatchStreamNormal {
    public static <T> List<List<T>> partition(List<T> list, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            batches.add(new ArrayList<>(list.subList(i, Math.min(i + batchSize, list.size()))));
        }
        return batches;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.IntStream;

public class BatchStreamStream {
    public static <T> List<List<T>> partition(List<T> list, int batchSize) {
        int totalBatches = (list.size() + batchSize - 1) / batchSize;
        return IntStream.range(0, totalBatches)
            .mapToObj(i -> list.subList(i * batchSize, Math.min((i + 1) * batchSize, list.size())))
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `for` loop advancing by `batchSize`.
- **Stream API**: `IntStream.range()` over batch counts slicing sublists.

---

### Q145: Infinite Prime Number Stream Generator (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Generate an infinite Stream of prime numbers ($2, 3, 5, 7, 11, 13, ...$) using `IntStream.iterate()` and take the first `n` primes."*

#### 🛍️ Real-World Intuitive Story
A prime number ticker generating candidate numbers sequentially and emitting only those that pass prime inspection.

#### 📥 Input & 📤 Output
- **Input**: `n = 5` $\rightarrow$ **Output**: `[2, 3, 5, 7, 11]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"infinite prime stream generator"*.
- **Pattern**: **IntStream.iterate(2, i -> i + 1).filter(this::isPrime).limit(n)**.

#### 🛠️ Step-by-Step Strategy
1. Seed `IntStream.iterate` with starting value `2`.
2. Pass increment operator `i -> i + 1`.
3. Filter `isPrime` and limit to `n`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class InfinitePrimeNormal {
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static List<Integer> getPrimes(int n) {
        List<Integer> primes = new ArrayList<>();
        int candidate = 2;
        while (primes.size() < n) {
            if (isPrime(candidate)) primes.add(candidate);
            candidate++;
        }
        return primes;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.IntStream;

public class InfinitePrimeStream {
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        return IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(i -> n % i == 0);
    }

    public static List<Integer> getPrimes(int n) {
        return IntStream.iterate(2, i -> i + 1)
            .filter(InfinitePrimeStream::isPrime)
            .limit(n)
            .boxed()
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `while` loop testing candidates until `primes.size() == n`.
- **Stream API**: `IntStream.iterate(2, i -> i + 1).filter(isPrime).limit(n)`.
