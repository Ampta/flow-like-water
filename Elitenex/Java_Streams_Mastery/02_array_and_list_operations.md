# 🔢 Category 2: Arrays & List Operations

This guide provides exhaustive, dual-method solutions for **20 Core Array & List Operation Problems**.

Each problem includes:
1. 📄 **Interview Problem Statement** (Realistic wordy paragraph format from real interviews/OAs)
2. 🛍️ **Real-World Intuitive Story** (Everyday simple mental model)
3. 📥 **Input & 📤 Output** (Sample test cases)
4. 🔍 **Interview Clues & Trigger Keywords** (How to decode the paragraph in seconds)
5. 🛠️ **Step-by-Step Strategy**
6. ☕ **Solution 1: Normal Java** (Imperative using loops & maps)
7. ⚡ **Solution 2: Java Stream API** (Declarative using Streams & Collectors)
8. ⚖️ **Comparison & Key Takeaways**

---

## 📑 Table of Contents
- [Q19: Two Sum (LeetCode #1)](#q19-two-sum-leetcode-1)
- [Q20: Contains Duplicate (LeetCode #217)](#q20-contains-duplicate-leetcode-217)
- [Q21: Contains Duplicate II (LeetCode #219)](#q21-contains-duplicate-ii-leetcode-219)
- [Q22: Single Number (LeetCode #136)](#q22-single-number-leetcode-136)
- [Q23: Missing Number (LeetCode #268)](#q23-missing-number-leetcode-268)
- [Q24: Intersection of Two Arrays (LeetCode #349)](#q24-intersection-of-two-arrays-leetcode-349)
- [Q25: Intersection of Two Arrays II (LeetCode #350)](#q25-intersection-of-two-arrays-ii-leetcode-350)
- [Q26: Find All Numbers Disappeared in an Array (LeetCode #448)](#q26-find-all-numbers-disappeared-in-an-array-leetcode-448)
- [Q27: Find All Duplicates in an Array (LeetCode #442)](#q27-find-all-duplicates-in-an-array-leetcode-442)
- [Q28: Move Zeroes (LeetCode #283)](#q28-move-zeroes-leetcode-283)
- [Q29: Product of Array Except Self (LeetCode #238)](#q29-product-of-array-except-self-leetcode-238)
- [Q30: Rotate Array (LeetCode #189)](#q30-rotate-array-leetcode-189)
- [Q31: Third Maximum Number (LeetCode #414)](#q31-third-maximum-number-leetcode-414)
- [Q32: Sort Array By Parity (LeetCode #905)](#q32-sort-array-by-parity-leetcode-905)
- [Q33: Sort Array By Parity II (LeetCode #922)](#q33-sort-array-by-parity-ii-leetcode-922)
- [Q34: Squares of a Sorted Array (LeetCode #977)](#q34-squares-of-a-sorted-array-leetcode-977)
- [Q35: Find Peak Element (LeetCode #162)](#q35-find-peak-element-leetcode-162)
- [Q36: Plus One (LeetCode #66)](#q36-plus-one-leetcode-66)
- [Q37: Remove Element (LeetCode #27)](#q37-remove-element-leetcode-27)
- [Q38: Remove Duplicates from Sorted Array (LeetCode #26)](#q38-remove-duplicates-from-sorted-array-leetcode-26)

---

### Q19: Two Sum (LeetCode #1)

#### 📄 Real-World Interview Problem Statement
> *"You are building a financial transaction checkout engine. Given an array of item prices `nums` and a target dollar balance `target`, find the indices of the two items such that their combined prices add up exactly to `target`. You may assume that each input would have exactly one solution, and you may not use the same element twice. Return the indices in any order."*

#### 🛍️ Real-World Intuitive Story
Imagine walking into a store with a \$9 gift card. You pick up a shirt costing \$7. You instantly calculate: *"I need an item costing \$9 - \$7 = \$2 to use the full gift card!"* As you check items on the shelf, you look for that \$2 item in your memory store.

#### 📥 Input & 📤 Output
- **Input**: `nums = [2,7,11,15]`, `target = 9` $\rightarrow$ **Output**: `[0,1]`
- **Input**: `nums = [3,2,4]`, `target = 6` $\rightarrow$ **Output**: `[1,2]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"two numbers that sum up to target"*, *"return indices of pair"*.
- **Pattern**: **HashMap Complement Lookup (`target - current`)**.

#### 🛠️ Step-by-Step Strategy
1. Create a HashMap storing `value -> index`.
2. For each element `nums[i]`, calculate `complement = target - nums[i]`.
3. If complement exists in map, return `[map.get(complement), i]`. Otherwise store `nums[i]` and `i`.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashMap;
import java.util.Map;

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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class TwoSumStream {
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        return IntStream.range(0, nums.length)
            .filter(i -> {
                int complement = target - nums[i];
                if (map.containsKey(complement)) {
                    return true;
                }
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
- **Normal Java**: Standard loop is cleaner for side-effect map updates.
- **Stream API**: `IntStream.range()` avoids manual loop index management.

---

### Q20: Contains Duplicate (LeetCode #217)

#### 📄 Real-World Interview Problem Statement
> *"You are reviewing user registration IDs `nums` submitted to an API service. To ensure account integrity, return `true` if any user ID value appears at least twice in the array, and return `false` if every user ID is distinct."*

#### 🛍️ Real-World Intuitive Story
Imagine checking in guests at a hotel. If 10 people stand in line and you collect 10 room key cards, but only 8 names are unique on the guest list, you know at least one guest checked in twice!

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,2,3,1]` $\rightarrow$ **Output**: `true`
- **Input**: `nums = [1,2,3,4]` $\rightarrow$ **Output**: `false`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"check if any value appears at least twice"*, *"duplicate detection"*.
- **Pattern**: **Distinct Count Comparison (`distinct().count() != nums.length`)**.

#### 🛠️ Step-by-Step Strategy
1. Compare original array length vs stream `distinct().count()`.
2. If counts differ, duplicates exist.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashSet;
import java.util.Set;

public class ContainsDuplicateNormal {
    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (!set.add(num)) return true;
        }
        return false;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class ContainsDuplicateStream {
    public static boolean containsDuplicate(int[] nums) {
        return Arrays.stream(nums).distinct().count() != nums.length;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Stream API**: Single line elegant check comparing original length vs distinct count.

---

### Q21: Contains Duplicate II (LeetCode #219)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums` and an integer `k`, return `true` if there are two distinct indices `i` and `j` in the array such that `nums[i] == nums[j]` and the absolute index difference `|i - j| <= k`."*

#### 🛍️ Real-World Intuitive Story
Think of security camera timestamps. If the same car license plate number `nums[i]` appears on camera twice within a window of $k = 3$ minutes, sound an alert!

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,2,3,1]`, `k = 3` $\rightarrow$ **Output**: `true`
- **Input**: `nums = [1,2,3,1,2,3]`, `k = 2` $\rightarrow$ **Output**: `false`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"duplicate values within distance k"*, *"nearby duplicates"*.
- **Pattern**: **Map Index Tracking (`i - prevIndex <= k`)**.

#### 🛠️ Step-by-Step Strategy
1. Store last seen index of each element in a Map.
2. If `nums[i]` was seen previously and `i - prevIndex <= k`, return `true`.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashMap;
import java.util.Map;

public class ContainsDuplicateIINormal {
    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i]) && i - map.get(nums[i]) <= k) {
                return true;
            }
            map.put(nums[i], i);
        }
        return false;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ContainsDuplicateIIStream {
    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        return IntStream.range(0, nums.length)
            .anyMatch(i -> {
                Integer prevIndex = map.put(nums[i], i);
                return prevIndex != null && i - prevIndex <= k;
            });
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- `Map.put()` returns previous value associated with key, allowing `anyMatch()` stream short-circuiting.

---

### Q22: Single Number (LeetCode #136)

#### 📄 Real-World Interview Problem Statement
> *"Given a non-empty array of integers `nums`, every element appears twice except for one. Find that single element. You must implement a solution with a linear runtime complexity and use only constant extra space."*

#### 🛍️ Real-World Intuitive Story
Imagine a party where everyone comes in pairs wearing matching costumes `[4, 1, 2, 1, 2]`. If every pair holds hands and leaves the dance floor, the only person standing alone on the dance floor is number `4`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [2,2,1]` $\rightarrow$ **Output**: `1`
- **Input**: `nums = [4,1,2,1,2]` $\rightarrow$ **Output**: `4`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"every element appears twice except for one"*, *"constant space"*.
- **Pattern**: **Bitwise XOR (`^`) Reduction (`reduce(0, (a, b) -> a ^ b)`)**.

#### 🛠️ Step-by-Step Strategy
1. Apply Bitwise XOR (`^`) reduction across array stream.
2. Duplicate numbers cancel out ($x \oplus x = 0$), leaving single number.

#### ☕ Solution 1: Normal Java
```java
public class SingleNumberNormal {
    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class SingleNumberStream {
    public static int singleNumber(int[] nums) {
        return Arrays.stream(nums)
            .reduce(0, (a, b) -> a ^ b);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Both solutions achieve $O(N)$ time and $O(1)$ space. Stream API `reduce()` expresses bitwise XOR aggregation cleanly.

---

### Q23: Missing Number (LeetCode #268)

#### 📄 Real-World Interview Problem Statement
> *"Given an array `nums` containing `n` distinct numbers in the range `[0, n]`, return the only number in the range that is missing from the array."*

#### 🛍️ Real-World Intuitive Story
Imagine numbered raffle tickets from `0` to `3`. Total expected sum is $0 + 1 + 2 + 3 = 6$. You draw tickets `[3, 0, 1]` whose sum is `4`. The missing ticket is $6 - 4 = 2$.

#### 📥 Input & 📤 Output
- **Input**: `nums = [3,0,1]` $\rightarrow$ **Output**: `2`
- **Input**: `nums = [0,1]` $\rightarrow$ **Output**: `2`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"range 0 to n missing number"*, *"find missing element"*.
- **Pattern**: **Expected Range Sum minus Actual Stream Sum**.

#### 🛠️ Step-by-Step Strategy
1. Calculate expected sum $\frac{n(n+1)}{2}$ using `IntStream.rangeClosed(0, n).sum()`.
2. Subtract actual array stream sum `Arrays.stream(nums).sum()`.

#### ☕ Solution 1: Normal Java
```java
public class MissingNumberNormal {
    public static int missingNumber(int[] nums) {
        int n = nums.length;
        int expectedSum = n * (n + 1) / 2;
        int actualSum = 0;
        for (int num : nums) {
            actualSum += num;
        }
        return expectedSum - actualSum;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class MissingNumberStream {
    public static int missingNumber(int[] nums) {
        int expectedSum = IntStream.rangeClosed(0, nums.length).sum();
        int actualSum = Arrays.stream(nums).sum();
        return expectedSum - actualSum;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Using `IntStream.rangeClosed().sum()` vs `Arrays.stream().sum()` demonstrates mathematical stream operations.

---

### Q24: Intersection of Two Arrays (LeetCode #349)

#### 📄 Real-World Interview Problem Statement
> *"Given two integer arrays `nums1` and `nums2`, return an array of their intersection. Each element in the result must be unique and you may return the result in any order."*

#### 🛍️ Real-World Intuitive Story
Imagine two friends listing their favorite movies. Friend A likes `[Inception, Avatar, Matrix]`. Friend B likes `[Avatar, Matrix, Interstellar]`. The unique movies common to both lists are `[Avatar, Matrix]`.

#### 📥 Input & 📤 Output
- **Input**: `nums1 = [1,2,2,1]`, `nums2 = [2,2]` $\rightarrow$ **Output**: `[2]`
- **Input**: `nums1 = [4,9,5]`, `nums2 = [9,4,9,8,4]` $\rightarrow$ **Output**: `[9,4]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"intersection of two arrays"*, *"unique common elements"*.
- **Pattern**: **Set Filtering + `distinct()` Stream**.

#### 🛠️ Step-by-Step Strategy
1. Collect `nums1` into a HashSet.
2. Filter `nums2` elements present in the set and deduplicate with `distinct()`.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashSet;
import java.util.Set;

public class IntersectionNormal {
    public static int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set1 = new HashSet<>();
        for (int num : nums1) set1.add(num);

        Set<Integer> resultSet = new HashSet<>();
        for (int num : nums2) {
            if (set1.contains(num)) {
                resultSet.add(num);
            }
        }

        int[] result = new int[resultSet.size()];
        int idx = 0;
        for (int num : resultSet) result[idx++] = num;
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class IntersectionStream {
    public static int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set1 = Arrays.stream(nums1).boxed().collect(Collectors.toSet());

        return Arrays.stream(nums2)
            .filter(set1::contains)
            .distinct()
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Stream API converts array conversion, filtering, deduplication, and primitive array output into 4 readable lines.

---

### Q25: Intersection of Two Arrays II (LeetCode #350)

#### 📄 Real-World Interview Problem Statement
> *"Given two integer arrays `nums1` and `nums2`, return an array of their intersection including duplicates. Each element in the result must appear as many times as it shows in both arrays."*

#### 🛍️ Real-World Intuitive Story
Imagine inventory lists from two stores. Store 1 has 2 laptops. Store 2 has 3 laptops. The common quantity available in both stores is 2 laptops.

#### 📥 Input & 📤 Output
- **Input**: `nums1 = [1,2,2,1]`, `nums2 = [2,2]` $\rightarrow$ **Output**: `[2,2]`
- **Input**: `nums1 = [4,9,5]`, `nums2 = [9,4,9,8,4]` $\rightarrow$ **Output**: `[4,9]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"intersection including duplicates"*, *"frequency matching"*.
- **Pattern**: **Frequency Map Count Tracking**.

#### 🛠️ Step-by-Step Strategy
1. Build frequency map for `nums1`.
2. Filter `nums2` elements checking frequency map and decrement count.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class IntersectionIINormal {
    public static int[] intersect(int[] nums1, int[] nums2) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int num : nums1) counts.put(num, counts.getOrDefault(num, 0) + 1);

        List<Integer> result = new ArrayList<>();
        for (int num : nums2) {
            if (counts.getOrDefault(num, 0) > 0) {
                result.add(num);
                counts.put(num, counts.get(num) - 1);
            }
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IntersectionIIStream {
    public static int[] intersect(int[] nums1, int[] nums2) {
        Map<Integer, Long> freqMap = Arrays.stream(nums1)
            .boxed()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return Arrays.stream(nums2)
            .filter(num -> {
                if (freqMap.getOrDefault(num, 0L) > 0) {
                    freqMap.put(num, freqMap.get(num) - 1);
                    return true;
                }
                return false;
            })
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Stream API direct `.toArray()` eliminates manual List-to-array conversion loops.

---

### Q26: Find All Numbers Disappeared in an Array (LeetCode #448)

#### 📄 Real-World Interview Problem Statement
> *"Given an array `nums` of `n` integers where `nums[i]` is in the range `[1, n]`, return an array of all the integers in the range `[1, n]` that do not appear in `nums`."*

#### 🛍️ Real-World Intuitive Story
Imagine student roll numbers `1` to `8`. On attendance sheet `[4, 3, 2, 7, 8, 2, 3, 1]`, students `5` and `6` were missing from the roll call!

#### 📥 Input & 📤 Output
- **Input**: `nums = [4,3,2,7,8,2,3,1]` $\rightarrow$ **Output**: `[5,6]`
- **Input**: `nums = [1,1]` $\rightarrow$ **Output**: `[2]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"numbers in range 1 to n that do not appear"*, *"disappeared numbers"*.
- **Pattern**: **Range Stream Filter against Set Lookup (`IntStream.rangeClosed`)**.

#### 🛠️ Step-by-Step Strategy
1. Collect array into a HashSet.
2. Stream expected range `[1, n]` using `IntStream.rangeClosed(1, n)`.
3. Filter numbers not present in the set.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class DisappearedNumbersNormal {
    public static List<Integer> findDisappearedNumbers(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) set.add(num);

        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= nums.length; i++) {
            if (!set.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DisappearedNumbersStream {
    public static List<Integer> findDisappearedNumbers(int[] nums) {
        Set<Integer> set = Arrays.stream(nums).boxed().collect(Collectors.toSet());

        return IntStream.rangeClosed(1, nums.length)
            .filter(i -> !set.contains(i))
            .boxed()
            .collect(Collectors.toList());
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- `IntStream.rangeClosed(1, n)` cleanly generates expected consecutive sequence without manual loops.

---

### Q27: Find All Duplicates in an Array (LeetCode #442)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums` of length `n` where all integers are in range `[1, n]` and each integer appears once or twice, return an array of all the integers that appear twice."*

#### 🛍️ Real-World Intuitive Story
Imagine checking ticket ID submissions `[4, 3, 2, 7, 8, 2, 3, 1]`. Tickets `2` and `3` were submitted twice.

#### 📥 Input & 📤 Output
- **Input**: `nums = [4,3,2,7,8,2,3,1]` $\rightarrow$ **Output**: `[2,3]`
- **Input**: `nums = [1,1,2]` $\rightarrow$ **Output**: `[1]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"integers that appear twice"*, *"duplicate detection"*.
- **Pattern**: **`groupingBy()` Frequency Filtering (`count == 2`)**.

#### 🛠️ Step-by-Step Strategy
1. Count frequencies using `Collectors.groupingBy()`.
2. Filter map entries where count equals 2.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FindDuplicatesNormal {
    public static List<Integer> findDuplicates(int[] nums) {
        List<Integer> result = new ArrayList<>();
        Map<Integer, Integer> counts = new HashMap<>();
        for (int num : nums) {
            counts.put(num, counts.getOrDefault(num, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            if (entry.getValue() == 2) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FindDuplicatesStream {
    public static List<Integer> findDuplicates(int[] nums) {
        return Arrays.stream(nums)
            .boxed()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .filter(entry -> entry.getValue() == 2)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Stream grouping & entry filtering pipeline handles array duplicate retrieval declaratively.

---

### Q28: Move Zeroes (LeetCode #283)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums`, move all `0`s to the end of it while maintaining the relative order of the non-zero elements."*

#### 🛍️ Real-World Intuitive Story
Imagine sorting apples and empty boxes on a conveyer belt `[0, 1, 0, 3, 12]`. Push all real apples `[1, 3, 12]` to the front and stack all empty zero boxes `[0, 0]` at the back.

#### 📥 Input & 📤 Output
- **Input**: `nums = [0,1,0,3,12]` $\rightarrow$ **Output**: `[1,3,12,0,0]`
- **Input**: `nums = [0]` $\rightarrow$ **Output**: `[0]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"move zeroes to end"*, *"maintain relative order"*.
- **Pattern**: **Stream Concat (`nonZerosStream` + `zerosStream`)**.

#### 🛠️ Step-by-Step Strategy
1. Filter non-zero elements stream.
2. Filter zero elements stream.
3. Concatenate both streams with `IntStream.concat()`.

#### ☕ Solution 1: Normal Java
```java
public class MoveZeroesNormal {
    public static void moveZeroes(int[] nums) {
        int insertPos = 0;
        for (int num : nums) {
            if (num != 0) {
                nums[insertPos++] = num;
            }
        }
        while (insertPos < nums.length) {
            nums[insertPos++] = 0;
        }
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class MoveZeroesStream {
    public static int[] moveZeroes(int[] nums) {
        return IntStream.concat(
            Arrays.stream(nums).filter(x -> x != 0),
            Arrays.stream(nums).filter(x -> x == 0)
        ).toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Traditional two-pointer in-place mutation uses $O(1)$ extra memory; Stream API creates new concatenated array.

---

### Q29: Product of Array Except Self (LeetCode #238)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums`, return an array `answer` such that `answer[i]` is equal to the product of all the elements of `nums` except `nums[i]`. The algorithm must run in $O(n)$ time and without using the division operation."*

#### 🛍️ Real-World Intuitive Story
Imagine multiplying numbers `[1, 2, 3, 4]`. For item index 2 (value `3`), multiply all numbers to its left (`1 * 2 = 2`) by all numbers to its right (`4`), resulting in $2 \times 4 = 8$.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,2,3,4]` $\rightarrow$ **Output**: `[24,12,8,6]`
- **Input**: `nums = [-1,1,0,-3,3]` $\rightarrow$ **Output**: `[0,0,9,0,0]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"product of array except self"*, *"without using division"*.
- **Pattern**: **Prefix and Suffix Running Product Accumulation**.

#### 🛠️ Step-by-Step Strategy
1. Compute prefix running products.
2. Compute suffix running products.
3. Multiply prefix and suffix values.

#### ☕ Solution 1: Normal Java
```java
public class ProductExceptSelfNormal {
    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];

        res[0] = 1;
        for (int i = 1; i < n; i++) {
            res[i] = res[i - 1] * nums[i - 1];
        }

        int right = 1;
        for (int i = n - 1; i >= 0; i--) {
            res[i] *= right;
            right *= nums[i];
        }
        return res;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class ProductExceptSelfStream {
    public static int[] productExceptSelf(int[] nums) {
        int totalProduct = Arrays.stream(nums).filter(x -> x != 0).reduce(1, (a, b) -> a * b);
        long zeroCount = Arrays.stream(nums).filter(x -> x == 0).count();

        return Arrays.stream(nums)
            .map(x -> {
                if (zeroCount > 1) return 0;
                if (zeroCount == 1) return x == 0 ? totalProduct : 0;
                return totalProduct / x;
            })
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Zero count filtering handles single and double zero edge cases cleanly in Stream mapping logic.

---

### Q30: Rotate Array (LeetCode #189)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums`, rotate the array to the right by `k` steps, where `k` is non-negative."*

#### 🛍️ Real-World Intuitive Story
Imagine a round carousel wheel of numbers `[1, 2, 3, 4, 5, 6, 7]`. Rotating right by $k = 3$ shifts the tail `[5, 6, 7]` to the front, producing `[5, 6, 7, 1, 2, 3, 4]`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,2,3,4,5,6,7]`, `k = 3` $\rightarrow$ **Output**: `[5,6,7,1,2,3,4]`
- **Input**: `nums = [-1,-100,3,99]`, `k = 2` $\rightarrow$ **Output**: `[3,99,-1,-100]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"rotate array right by k steps"*, *"array rotation"*.
- **Pattern**: **Stream Slice Concatenation (`IntStream.concat(tailStream, headStream)`)**.

#### 🛠️ Step-by-Step Strategy
1. Normalize offset `k = k % n`.
2. Slice tail stream `[n - k, n]` and head stream `[0, n - k]`.
3. Concatenate tail before head.

#### ☕ Solution 1: Normal Java
```java
public class RotateArrayNormal {
    public static void rotate(int[] nums, int k) {
        k %= nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    private static void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start++] = nums[end];
            nums[end--] = temp;
        }
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class RotateArrayStream {
    public static int[] rotate(int[] nums, int k) {
        int n = nums.length;
        int offset = n - (k % n);

        return IntStream.concat(
            Arrays.stream(nums, offset, n),
            Arrays.stream(nums, 0, offset)
        ).toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- `Arrays.stream(array, start, end)` range slicing makes stream rotation simple.

---

### Q31: Third Maximum Number (LeetCode #414)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums`, return the third distinct maximum number in this array. If the third maximum does not exist, return the maximum number."*

#### 🛍️ Real-World Intuitive Story
Imagine an athletic race with times `[3, 2, 1]`. The bronze medal goes to 3rd place `1`. If only two runners finished `[2, 1]`, award the gold medal `2`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [3,2,1]` $\rightarrow$ **Output**: `1`
- **Input**: `nums = [1,2]` $\rightarrow$ **Output**: `2`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"third distinct maximum"*, *"fallback to max"*.
- **Pattern**: **`distinct().sorted(reverseOrder()).skip(2)`**.

#### 🛠️ Step-by-Step Strategy
1. Box array, deduplicate via `distinct()`, and sort descending.
2. If size $\ge 3$, return element at index 2 (skip 2); else return element at index 0.

#### ☕ Solution 1: Normal Java
```java
import java.util.TreeSet;

public class ThirdMaxNormal {
    public static int thirdMax(int[] nums) {
        TreeSet<Integer> set = new TreeSet<>();
        for (int num : nums) {
            set.add(num);
            if (set.size() > 3) {
                set.pollFirst();
            }
        }
        return set.size() == 3 ? set.first() : set.last();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ThirdMaxStream {
    public static int thirdMax(int[] nums) {
        List<Integer> sortedDistinct = Arrays.stream(nums)
            .boxed()
            .distinct()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());

        return sortedDistinct.size() >= 3 ? sortedDistinct.get(2) : sortedDistinct.get(0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Stream API `distinct().sorted(reverseOrder())` expresses finding k-th distinct maximum succinctly.

---

### Q32: Sort Array By Parity (LeetCode #905)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums`, move all the even integers at the beginning of the array followed by all the odd integers. Return any array that satisfies this condition."*

#### 🛍️ Real-World Intuitive Story
Imagine sorting numbers into two lines: even numbers `[2, 4]` go to line 1; odd numbers `[3, 1]` go to line 2. Merge line 1 before line 2: `[2, 4, 3, 1]`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [3,1,2,4]` $\rightarrow$ **Output**: `[2,4,3,1]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"even integers precede odd integers"*, *"sort by parity"*.
- **Pattern**: **`IntStream.concat(evenStream, oddStream)`**.

#### 🛠️ Step-by-Step Strategy
1. Filter even elements stream.
2. Filter odd elements stream.
3. Concatenate both streams.

#### ☕ Solution 1: Normal Java
```java
public class SortArrayByParityNormal {
    public static int[] sortArrayByParity(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            if (nums[left] % 2 > nums[right] % 2) {
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
            }
            if (nums[left] % 2 == 0) left++;
            if (nums[right] % 2 != 0) right--;
        }
        return nums;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class SortArrayByParityStream {
    public static int[] sortArrayByParity(int[] nums) {
        return IntStream.concat(
            Arrays.stream(nums).filter(x -> x % 2 == 0),
            Arrays.stream(nums).filter(x -> x % 2 != 0)
        ).toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Stream concatenation provides readable parity partitioning.

---

### Q33: Sort Array By Parity II (LeetCode #922)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of integers `nums` half of which are odd and half of which are even, sort the array so that whenever `nums[i]` is odd, `i` is odd; and whenever `nums[i]` is even, `i` is even."*

#### 🛍️ Real-World Intuitive Story
Imagine seating guests in alternating chairs: Chair 0 (even) gets an even guest `4`, Chair 1 (odd) gets an odd guest `5`, Chair 2 (even) gets an even guest `2`, Chair 3 (odd) gets an odd guest `7`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [4,2,5,7]` $\rightarrow$ **Output**: `[4,5,2,7]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"even index even value, odd index odd value"*, *"alternating parity"*.
- **Pattern**: **Interleaved Index Range Mapping (`evens[i/2]` vs `odds[i/2]`)**.

#### 🛠️ Step-by-Step Strategy
1. Separate evens array and odds array.
2. Map `IntStream.range()`: if index `i` is even, pick `evens[i / 2]`; else pick `odds[i / 2]`.

#### ☕ Solution 1: Normal Java
```java
public class SortArrayByParityIINormal {
    public static int[] sortArrayByParityII(int[] nums) {
        int i = 0, j = 1;
        int n = nums.length;
        while (i < n && j < n) {
            while (i < n && nums[i] % 2 == 0) i += 2;
            while (j < n && nums[j] % 2 != 0) j += 2;
            if (i < n && j < n) {
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
            }
        }
        return nums;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class SortArrayByParityIIStream {
    public static int[] sortArrayByParityII(int[] nums) {
        int[] evens = Arrays.stream(nums).filter(x -> x % 2 == 0).toArray();
        int[] odds = Arrays.stream(nums).filter(x -> x % 2 != 0).toArray();

        return IntStream.range(0, nums.length)
            .map(i -> i % 2 == 0 ? evens[i / 2] : odds[i / 2])
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- `IntStream.range()` mapping using ternary index arithmetic `i % 2 == 0 ? evens[i/2] : odds[i/2]` handles alternating streams cleanly.

---

### Q34: Squares of a Sorted Array (LeetCode #977)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums` sorted in non-decreasing order, return an array of the squares of each number sorted in non-decreasing order."*

#### 🛍️ Real-World Intuitive Story
Imagine squaring numbers `[-4, -1, 0, 3, 10]` $\rightarrow$ `[16, 1, 0, 9, 100]`. Negative numbers become positive when squared. Sort the resulting squared numbers: `[0, 1, 9, 16, 100]`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [-4,-1,0,3,10]` $\rightarrow$ **Output**: `[0,1,9,16,100]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"squares of numbers sorted in non-decreasing order"*.
- **Pattern**: **`map(x -> x * x).sorted()`**.

#### 🛠️ Step-by-Step Strategy
1. Map each element `x` to `x * x`.
2. Apply `sorted()`.

#### ☕ Solution 1: Normal Java
```java
public class SquaresSortedArrayNormal {
    public static int[] sortedSquares(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        int left = 0, right = n - 1;
        int idx = n - 1;

        while (left <= right) {
            int leftSquare = nums[left] * nums[left];
            int rightSquare = nums[right] * nums[right];
            if (leftSquare > rightSquare) {
                result[idx--] = leftSquare;
                left++;
            } else {
                result[idx--] = rightSquare;
                right--;
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class SquaresSortedArrayStream {
    public static int[] sortedSquares(int[] nums) {
        return Arrays.stream(nums)
            .map(x -> x * x)
            .sorted()
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Two-pointer approach is $O(N)$.
- **Stream API**: `map().sorted()` is concise $O(N \log N)$ 1-liner.

---

### Q35: Find Peak Element (LeetCode #162)

#### 📄 Real-World Interview Problem Statement
> *"A peak element is an element that is strictly greater than its neighbors. Given a 0-indexed integer array `nums`, find a peak element, and return its index."*

#### 🛍️ Real-World Intuitive Story
Imagine walking up and down mountain peaks `[1, 2, 3, 1]`. At peak height `3` (index 2), the left neighbor `2` is lower and right neighbor `1` is lower.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,2,3,1]` $\rightarrow$ **Output**: `2`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"strictly greater than neighbors"*, *"find peak element"*.
- **Pattern**: **Index Range Neighbor Filter**.

#### 🛠️ Step-by-Step Strategy
1. Filter index stream where `nums[i]` is strictly greater than left and right neighbors.

#### ☕ Solution 1: Normal Java
```java
public class FindPeakElementNormal {
    public static int findPeakElement(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[mid + 1]) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.IntStream;

public class FindPeakElementStream {
    public static int findPeakElement(int[] nums) {
        int n = nums.length;
        return IntStream.range(0, n)
            .filter(i -> (i == 0 || nums[i] > nums[i - 1]) && 
                         (i == n - 1 || nums[i] > nums[i + 1]))
            .findFirst()
            .orElse(0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Normal Java Binary Search is $O(\log N)$; Stream index search is $O(N)$ linear filter.

---

### Q36: Plus One (LeetCode #66)

#### 📄 Real-World Interview Problem Statement
> *"You are given a large integer represented as an integer array `digits`, where each `digits[i]` is the $i$-th digit of the integer. Increment the large integer by 1 and return the resulting array of digits."*

#### 🛍️ Real-World Intuitive Story
Imagine an odometer counter `[1, 2, 9]`. Incrementing by 1 rolls `9` over to `0` and carries `1` forward to `2`, producing `[1, 3, 0]`.

#### 📥 Input & 📤 Output
- **Input**: `digits = [1,2,3]` $\rightarrow$ **Output**: `[1,2,4]`
- **Input**: `digits = [9,9]` $\rightarrow$ **Output**: `[1,0,0]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"array representing integer digits, increment by 1"*.
- **Pattern**: **BigInteger Stream Conversion**.

#### 🛠️ Step-by-Step Strategy
1. Join digits into BigInteger string, add 1.
2. Convert BigInteger string back to IntStream digit array.

#### ☕ Solution 1: Normal Java
```java
public class PlusOneNormal {
    public static int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }
            digits[i] = 0;
        }

        int[] result = new int[digits.length + 1];
        result[0] = 1;
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PlusOneStream {
    public static int[] plusOne(int[] digits) {
        String numStr = Arrays.stream(digits)
            .mapToObj(String::valueOf)
            .collect(Collectors.joining());

        BigInteger incremented = new BigInteger(numStr).add(BigInteger.ONE);

        return incremented.toString()
            .chars()
            .map(c -> c - '0')
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Stream + `BigInteger` eliminates custom carry logic completely.

---

### Q37: Remove Element (LeetCode #27)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums` and an integer `val`, remove all occurrences of `val` in `nums` in-place. Return the number of elements in `nums` which are not equal to `val`."*

#### 🛍️ Real-World Intuitive Story
Imagine filtering bad coins out of a jar `[3, 2, 2, 3]`. Remove all counterfeit coin values `3`, leaving valid coins `[2, 2]`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [3,2,2,3]`, `val = 3` $\rightarrow$ **Output**: `[2,2]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"remove occurrences of target value"*.
- **Pattern**: **`filter(x -> x != val)`**.

#### 🛠️ Step-by-Step Strategy
1. Filter out stream elements equal to `val`.

#### ☕ Solution 1: Normal Java
```java
public class RemoveElementNormal {
    public static int removeElement(int[] nums, int val) {
        int k = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[k++] = nums[i];
            }
        }
        return k;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class RemoveElementStream {
    public static int[] removeElement(int[] nums, int val) {
        return Arrays.stream(nums)
            .filter(x -> x != val)
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Stream API returns new array without mutating input array in-place.

---

### Q38: Remove Duplicates from Sorted Array (LeetCode #26)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums` sorted in non-decreasing order, remove the duplicates in-place such that each unique element appears only once."*

#### 🛍️ Real-World Intuitive Story
Imagine sorting a stack of receipts `[1, 1, 2]`. Throw away duplicate receipt copies so only unique receipt values remain `[1, 2]`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,1,2]` $\rightarrow$ **Output**: `[1,2]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"remove duplicates from sorted array"*.
- **Pattern**: **`distinct().toArray()`**.

#### 🛠️ Step-by-Step Strategy
1. Apply `distinct()` on array stream.

#### ☕ Solution 1: Normal Java
```java
public class RemoveDuplicatesNormal {
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class RemoveDuplicatesStream {
    public static int[] removeDuplicates(int[] nums) {
        return Arrays.stream(nums)
            .distinct()
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Stream API**: `distinct()` reduces 10+ lines of duplicate pointer logic into a single method call.
