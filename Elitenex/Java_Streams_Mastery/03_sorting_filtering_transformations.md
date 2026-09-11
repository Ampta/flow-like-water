# 🔄 Category 3: Sorting, Filtering & Transformations

This guide provides exhaustive, production-grade solutions for **20 Core Sorting, Filtering & Data Transformation Problems** (Q39 – Q58). Every problem is presented with an authentic interview scenario, real-world intuitive story, step-by-step strategy, and dual implementation (Normal Java vs. Java Streams).

---

## 📑 Table of Contents
- [Q39: Top K Frequent Elements (LeetCode #347)](#q39-top-k-frequent-elements-leetcode-347)
- [Q40: Kth Largest Element in an Array (LeetCode #215)](#q40-kth-largest-element-in-an-array-leetcode-215)
- [Q41: Kth Smallest Element in a Sorted Matrix (LeetCode #378)](#q41-kth-smallest-element-in-a-sorted-matrix-leetcode-378)
- [Q42: Sort Colors / Dutch National Flag (LeetCode #75)](#q42-sort-colors--dutch-national-flag-leetcode-75)
- [Q43: Relative Sort Array (LeetCode #1122)](#q43-relative-sort-array-leetcode-1122)
- [Q44: Flatten Deeply Nested List (LeetCode #341 / Custom)](#q44-flatten-deeply-nested-list-leetcode-341--custom)
- [Q45: Merge Intervals Processing (LeetCode #56)](#q45-merge-intervals-processing-leetcode-56)
- [Q46: Custom Object Multi-Field Sorting (Interview Classic)](#q46-custom-object-multi-field-sorting-interview-classic)
- [Q47: Filter Null and Empty Values (Interview Classic)](#q47-filter-null-and-empty-values-interview-classic)
- [Q48: Map Transformation of DTOs (Interview Classic)](#q48-map-transformation-of-dtos-interview-classic)
- [Q49: Find Max & Min Simultaneously (Interview Classic)](#q49-find-max--min-simultaneously-interview-classic)
- [Q50: Maximum Product of Three Numbers (LeetCode #628)](#q50-maximum-product-of-three-numbers-leetcode-628)
- [Q51: Sort Matrix by Diagonals (LeetCode #1329)](#q51-sort-matrix-by-diagonals-leetcode-1329)
- [Q52: Majority Element (LeetCode #169)](#q52-majority-element-leetcode-169)
- [Q53: Majority Element II (LeetCode #229)](#q53-majority-element-ii-leetcode-229)
- [Q54: Find Target Indices After Sorting Array (LeetCode #2089)](#q54-find-target-indices-after-sorting-array-leetcode-2089)
- [Q55: Count Elements With Strictly Smaller and Greater Elements (LeetCode #2148)](#q55-count-elements-with-strictly-smaller-and-greater-elements-leetcode-2148)
- [Q56: Find N Unique Integers Sum up to Zero (LeetCode #1304)](#q56-find-n-unique-integers-sum-up-to-zero-leetcode-1304)
- [Q57: Divide Array Into Equal Pairs (LeetCode #2206)](#q57-divide-array-into-equal-pairs-leetcode-2206)
- [Q58: Keep Multiplying Found Values by Two (LeetCode #2154)](#q58-keep-multiplying-found-values-by-two-leetcode-2154)

---

### Q39: Top K Frequent Elements (LeetCode #347)

#### 📄 Real-World Interview Problem Statement
> *"You are monitoring an e-commerce platform during Black Friday. The analytics system logs thousands of item purchase IDs per second into an array `nums`. To display the 'Trending Products' widget on the homepage, your engine needs to retrieve the `k` most frequently purchased product IDs. Return these `k` IDs in any order."*

#### 🛍️ Real-World Intuitive Story
Think of a supermarket checkout register counting items. First, you tally how many times each item appears in customers' carts (e.g., milk: 10, bread: 7, apples: 2). Then, you sort items by their tally count and pick the top `k` most popular items to put on display.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,1,1,2,2,3]`, `k = 2` $\rightarrow$ **Output**: `[1, 2]`
- **Input**: `nums = [1]`, `k = 1` $\rightarrow$ **Output**: `[1]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"most frequent items"*, *"top k elements"*.
- **Pattern**: **Frequency Map + Heap / Stream Sorting**.

#### 🛠️ Step-by-Step Strategy
1. Build a frequency count map of all elements in `nums`.
2. Sort map entries descending by frequency.
3. Pick the top `k` keys from the sorted entries.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class TopKFrequentNormal {
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) map.put(num, map.getOrDefault(num, 0) + 1);

        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = 
            new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            minHeap.offer(entry);
            if (minHeap.size() > k) minHeap.poll();
        }

        int[] result = new int[k];
        int idx = 0;
        while (!minHeap.isEmpty()) {
            result[idx++] = minHeap.poll().getKey();
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TopKFrequentStream {
    public static int[] topKFrequent(int[] nums, int k) {
        return Arrays.stream(nums)
            .boxed()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
            .limit(k)
            .mapToInt(Map.Entry::getKey)
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Uses a Min-Heap (`PriorityQueue`) maintaining size $k$, offering optimal $O(N \log k)$ runtime performance.
- **Stream API**: Declarative 1-liner pipeline using `groupingBy()`, `counting()`, `sorted()`, and `limit()`.

---

### Q40: Kth Largest Element in an Array (LeetCode #215)

#### 📄 Real-World Interview Problem Statement
> *"In a gaming leaderboard application, scores are streamed into an unsorted array `nums`. You are asked to find the $k$-th highest score recorded without fully sorting the entire array if possible."*

#### 🛍️ Real-World Intuitive Story
Imagine a podium for competition winners. If you want to find the 3rd highest score, you only need to keep track of the top 3 highest scores seen so far. Every time a new score comes in, if it's larger than the smallest score on your top-3 podium, you replace it.

#### 📥 Input & 📤 Output
- **Input**: `nums = [3,2,1,5,6,4]`, `k = 2` $\rightarrow$ **Output**: `5`
- **Input**: `nums = [3,2,3,1,2,4,5,5,6]`, `k = 4` $\rightarrow$ **Output**: `4`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"kth largest element"*, *"unsorted score list"*.
- **Pattern**: **Min-Heap / Sorting Stream**.

#### 🛠️ Step-by-Step Strategy
1. Box integer array into an Object Stream.
2. Sort in descending order or box into a Min-Heap.
3. Skip the first $k-1$ elements and pick the next element.

#### ☕ Solution 1: Normal Java
```java
import java.util.PriorityQueue;

public class KthLargestNormal {
    public static int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.peek();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.Comparator;

public class KthLargestStream {
    public static int findKthLargest(int[] nums, int k) {
        return Arrays.stream(nums)
            .boxed()
            .sorted(Comparator.reverseOrder())
            .skip(k - 1)
            .findFirst()
            .orElseThrow();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `PriorityQueue` takes $O(N \log k)$ time and $O(k)$ extra space.
- **Stream API**: `sorted(Comparator.reverseOrder()).skip(k - 1)` is extremely concise and elegant.

---

### Q41: Kth Smallest Element in a Sorted Matrix (LeetCode #378)

#### 📄 Real-World Interview Problem Statement
> *"You are given an `n x n` matrix where each row and column is sorted in non-decreasing order. Write a service function to compute the $k$-th smallest element across the entire matrix structure."*

#### 🛍️ Real-World Intuitive Story
Imagine a grid of student test scores where each row represents students from left to right with increasing marks, and top to bottom also increases. To find the 5th lowest score overall, you can flatten all student scores into one single list and pick the 5th value.

#### 📥 Input & 📤 Output
- **Input**: `matrix = [[1,5,9],[10,11,13],[12,13,15]]`, `k = 8` $\rightarrow$ **Output**: `13`
- **Input**: `matrix = [[-5]]`, `k = 1` $\rightarrow$ **Output**: `-5`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"kth smallest in matrix"*, *"flatten 2D array"*.
- **Pattern**: **Stream FlatMap + Sorting**.

#### 🛠️ Step-by-Step Strategy
1. Stream all rows of the 2D matrix.
2. FlatMap rows into a single stream of integers.
3. Sort ascending, skip $k-1$ elements, and pick the first element.

#### ☕ Solution 1: Normal Java
```java
import java.util.PriorityQueue;

public class KthSmallestMatrixNormal {
    public static int kthSmallest(int[][] matrix, int k) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int[] row : matrix) {
            for (int val : row) {
                maxHeap.offer(val);
                if (maxHeap.size() > k) maxHeap.poll();
            }
        }
        return maxHeap.peek();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class KthSmallestMatrixStream {
    public static int kthSmallest(int[][] matrix, int k) {
        return Arrays.stream(matrix)
            .flatMapToInt(Arrays::stream)
            .sorted()
            .skip(k - 1)
            .findFirst()
            .orElseThrow();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Max-Heap maintains size $k$, ideal for memory constrained large matrices.
- **Stream API**: `flatMapToInt(Arrays::stream)` flattens 2D arrays directly into a sorted Primitive Stream.

---

### Q42: Sort Colors / Dutch National Flag (LeetCode #75)

#### 📄 Real-World Interview Problem Statement
> *"An automated warehouse sorting robot has packages categorized into three priority levels: Red (0 - High), White (1 - Medium), and Blue (2 - Low). Given an array `nums` representing unsorted package codes, sort them in-place or transform them into prioritized order `[0, 0, ..., 1, 1, ..., 2, 2]`."*

#### 🛍️ Real-World Intuitive Story
Imagine sorting laundry into three piles: whites (0), darks (1), and colors (2). You line up all clothes so all whites are on the far left, darks in the middle, and colors on the far right.

#### 📥 Input & 📤 Output
- **Input**: `nums = [2,0,2,1,1,0]` $\rightarrow$ **Output**: `[0,0,1,1,2,2]`
- **Input**: `nums = [2,0,1]` $\rightarrow$ **Output**: `[0,1,2]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"0, 1, and 2"*, *"sort colors"*, *"in-place partition"*.
- **Pattern**: **Three-Pointer Partitioning / Primitive IntStream Sorting**.

#### 🛠️ Step-by-Step Strategy
1. Stream `nums` integer array.
2. Sort primitive values ascending using `Arrays.stream(nums).sorted()`.
3. Collect back into array or modify input array.

#### ☕ Solution 1: Normal Java
```java
public class SortColorsNormal {
    public static void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;
        while (mid <= high) {
            if (nums[mid] == 0) {
                int temp = nums[low];
                nums[low++] = nums[mid];
                nums[mid++] = temp;
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                int temp = nums[mid];
                nums[mid] = nums[high];
                nums[high--] = temp;
            }
        }
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class SortColorsStream {
    public static int[] sortColors(int[] nums) {
        return Arrays.stream(nums)
            .sorted()
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Dutch National Flag algorithm performs in-place sorting in $O(N)$ time with $O(1)$ extra space.
- **Stream API**: `Arrays.stream(nums).sorted().toArray()` creates a sorted functional copy cleanly.

---

### Q43: Relative Sort Array (LeetCode #1122)

#### 📄 Real-World Interview Problem Statement
> *"You are designing a custom search result ranker. You have array `arr1` of search items and `arr2` defining a custom priority ordering. Sort elements of `arr1` such that relative ordering matches `arr2`. Elements not present in `arr2` must appear at the end in ascending order."*

#### 🛍️ Real-World Intuitive Story
Think of a playlist organizer. You have a master song collection (`arr1`) and a custom top-favorites list (`arr2`). You put all favorite songs in exact custom order first, and append all leftover songs at the end alphabetically sorted.

#### 📥 Input & 📤 Output
- **Input**: `arr1 = [2,3,1,3,2,4,6,7,9,2,19]`, `arr2 = [2,1,4,3,9,6]`
- **Output**: `[2,2,2,1,4,3,3,9,6,7,19]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"custom relative ordering"*, *"prioritize by index map"*.
- **Pattern**: **Custom Comparator using Index Lookup Map**.

#### 🛠️ Step-by-Step Strategy
1. Build an index lookup map for elements in `arr2`.
2. Define a custom Comparator comparing `arr2` index rank.
3. If neither element is in `arr2`, fall back to natural integer comparison.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class RelativeSortNormal {
    public static int[] relativeSortArray(int[] arr1, int[] arr2) {
        Map<Integer, Integer> rankMap = new HashMap<>();
        for (int i = 0; i < arr2.length; i++) rankMap.put(arr2[i], i);

        List<Integer> list = new ArrayList<>();
        for (int num : arr1) list.add(num);

        list.sort((a, b) -> {
            if (rankMap.containsKey(a) && rankMap.containsKey(b)) {
                return rankMap.get(a) - rankMap.get(b);
            } else if (rankMap.containsKey(a)) {
                return -1;
            } else if (rankMap.containsKey(b)) {
                return 1;
            } else {
                return Integer.compare(a, b);
            }
        });

        return list.stream().mapToInt(Integer::intValue).toArray();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;

public class RelativeSortStream {
    public static int[] relativeSortArray(int[] arr1, int[] arr2) {
        Map<Integer, Integer> rank = new HashMap<>();
        for (int i = 0; i < arr2.length; i++) rank.put(arr2[i], i);

        return Arrays.stream(arr1)
            .boxed()
            .sorted((a, b) -> {
                int rankA = rank.getOrDefault(a, 1000 + a);
                int rankB = rank.getOrDefault(b, 1000 + b);
                return Integer.compare(rankA, rankB);
            })
            .mapToInt(Integer::intValue)
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Expressive conditional checks for relative rank map containment.
- **Stream API**: Modern custom comparator mapping missing keys to dynamic offsets (`1000 + value`) to auto-sort remaining elements.

---

### Q44: Flatten Deeply Nested List (LeetCode #341 / Custom)

#### 📄 Real-World Interview Problem Statement
> *"You receive a nested JSON catalog payload containing categories and subcategories of product IDs, formatted as a nested list structure `List<Object>`. Flatten this arbitrary multi-level nested list hierarchy into a single 1D flat list of integers."*

#### 🛍️ Real-World Intuitive Story
Imagine unpacking nested russian nesting dolls (matryoshka dolls) or nested boxes inside boxes. To list all items, you open every box recursively until all items lie flat on a single table.

#### 📥 Input & 📤 Output
- **Input**: `[[1,1], 2, [1,1]]` $\rightarrow$ **Output**: `[1, 1, 2, 1, 1]`
- **Input**: `[1, [4, [6]]]` $\rightarrow$ **Output**: `[1, 4, 6]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"nested list structure"*, *"flatten hierarchy"*.
- **Pattern**: **Recursive FlatMap Stream**.

#### 🛠️ Step-by-Step Strategy
1. Stream outer list elements.
2. Check if element is an `Integer` or nested `List`.
3. Recursively flatMap nested sublists into a unified Integer Stream.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FlattenNestedNormal {
    @SuppressWarnings("unchecked")
    public static List<Integer> flatten(List<Object> nestedList) {
        List<Integer> result = new ArrayList<>();
        for (Object obj : nestedList) {
            if (obj instanceof Integer) {
                result.add((Integer) obj);
            } else if (obj instanceof List) {
                result.addAll(flatten((List<Object>) obj));
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Stream;

public class FlattenNestedStream {
    @SuppressWarnings("unchecked")
    public static List<Integer> flatten(List<Object> nestedList) {
        return nestedList.stream()
            .flatMap(obj -> obj instanceof List 
                ? flatten((List<Object>) obj).stream() 
                : Stream.of((Integer) obj))
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard recursive helper function with explicit `instanceof` checks.
- **Stream API**: Recursive `flatMap()` elegantly collapses nested streams inline.

---

### Q45: Merge Intervals Processing (LeetCode #56)

#### 📄 Real-World Interview Problem Statement
> *"You are developing a meeting room calendar management engine. Given an array of meeting time `intervals` where `intervals[i] = [start_i, end_i]`, merge all overlapping meeting time slots and return an array of non-overlapping intervals covering all reservations."*

#### 🛍️ Real-World Intuitive Story
Imagine booking a room for 1:00 PM – 3:00 PM and another team booking 2:00 PM – 4:00 PM. Since they overlap, the room is occupied continuously from 1:00 PM – 4:00 PM.

#### 📥 Input & 📤 Output
- **Input**: `intervals = [[1,3],[2,6],[8,10],[15,18]]` $\rightarrow$ **Output**: `[[1,6],[8,10],[15,18]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"merge overlapping intervals"*, *"calendar time slots"*.
- **Pattern**: **Sort by Start Time + Reduce / Imperative Accumulation**.

#### 🛠️ Step-by-Step Strategy
1. Sort intervals by start time ascending.
2. Iterate through intervals and compare current start time with previous end time.
3. If overlapping, merge by extending end time; otherwise, add new interval slot.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class MergeIntervalsNormal {
    public static int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) return intervals;

        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> merged = new ArrayList<>();
        int[] current = intervals[0];
        merged.add(current);

        for (int[] interval : intervals) {
            if (interval[0] <= current[1]) {
                current[1] = Math.max(current[1], interval[1]);
            } else {
                current = interval;
                merged.add(current);
            }
        }
        return merged.toArray(new int[merged.size()][]);
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Stream;

public class MergeIntervalsStream {
    public static int[][] merge(int[][] intervals) {
        List<int[]> sorted = Arrays.stream(intervals)
            .sorted(Comparator.comparingInt(a -> a[0]))
            .toList();

        List<int[]> merged = new ArrayList<>();
        for (int[] interval : sorted) {
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(interval);
            } else {
                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], interval[1]);
            }
        }
        return merged.toArray(new int[0][]);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: In-place sorting and sequential list tracking is standard and memory efficient.
- **Stream API**: Stream sorts input declaratively before performing state reduction.

---

### Q46: Custom Object Multi-Field Sorting (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"An HR management tool processes employee records `List<Employee>`. Sort the employee list primary by Department ascending, secondary by Salary descending, and tertiary by Employee Name alphabetically."*

#### 🛍️ Real-World Intuitive Story
Think of sorting a phonebook or roster. First, group by department name (Engineering before Sales). Within Engineering, put highest paid engineers first. If two engineers earn identical salaries, sort them alphabetically by name.

#### 📥 Input & 📤 Output
- **Input**: `[Emp("HR", 5000, "Alice"), Emp("Eng", 8000, "Bob"), Emp("Eng", 8000, "Adam")]`
- **Output**: `[Emp("Eng", 8000, "Adam"), Emp("Eng", 8000, "Bob"), Emp("HR", 5000, "Alice")]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"multi-field comparator"*, *"sort by X then Y then Z"*.
- **Pattern**: **Comparator Chaining (`thenComparing`)**.

#### 🛠️ Step-by-Step Strategy
1. Build a multi-tier `Comparator` using `Comparator.comparing()`.
2. Chain reverse ordering for numeric salary and natural ordering for names.
3. Apply `.sorted(comparator)` on the stream pipeline.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class MultiFieldSortNormal {
    public record Employee(String dept, double salary, String name) {}

    public static List<Employee> sortEmployees(List<Employee> employees) {
        List<Employee> sorted = new ArrayList<>(employees);
        sorted.sort((e1, e2) -> {
            int deptComp = e1.dept().compareTo(e2.dept());
            if (deptComp != 0) return deptComp;

            int salaryComp = Double.compare(e2.salary(), e1.salary());
            if (salaryComp != 0) return salaryComp;

            return e1.name().compareTo(e2.name());
        });
        return sorted;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class MultiFieldSortStream {
    public record Employee(String dept, double salary, String name) {}

    public static List<Employee> sortEmployees(List<Employee> employees) {
        return employees.stream()
            .sorted(Comparator.comparing(Employee::dept)
                .thenComparing(Comparator.comparingDouble(Employee::salary).reversed())
                .thenComparing(Employee::name))
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Requires nested `if` statements and manual string/double comparison boilerplate.
- **Stream API**: `Comparator.comparing().thenComparing()` is standard modern Java code style.

---

### Q47: Filter Null and Empty Values (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"You are processing dirty input form data containing a list of customer email strings `List<String>`. Filter out all null references, blank spaces, and empty strings, returning only non-null, non-blank valid email strings."*

#### 🛍️ Real-World Intuitive Story
Imagine sifting through physical paper mail. Throw away blank envelopes, torn scraps, and unaddressed junk, keeping only valid, clearly written addresses.

#### 📥 Input & 📤 Output
- **Input**: `["alice@test.com", null, "   ", "bob@test.com", ""]` $\rightarrow$ **Output**: `["alice@test.com", "bob@test.com"]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"sanitize inputs"*, *"filter null and blank values"*.
- **Pattern**: **Objects::nonNull + String::isBlank Predicate**.

#### 🛠️ Step-by-Step Strategy
1. Stream customer string input list.
2. Filter out null values using `Objects::nonNull`.
3. Filter out empty or whitespace-only strings using `!s.isBlank()`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class FilterNullEmptyNormal {
    public static List<String> sanitize(List<String> list) {
        List<String> result = new ArrayList<>();
        if (list == null) return result;
        for (String str : list) {
            if (str != null && !str.trim().isEmpty()) {
                result.add(str);
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class FilterNullEmptyStream {
    public static List<String> sanitize(List<String> list) {
        return Optional.ofNullable(list)
            .orElse(Collections.emptyList())
            .stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Straightforward `null` check + `trim().isEmpty()` inside loop.
- **Stream API**: Modern functional data sanitization pipeline handling null inputs seamlessly.

---

### Q48: Map Transformation of DTOs (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"In a REST API backend microservice, transform a list of database entity models `UserEntity` into light frontend response objects `UserDTO` containing formatted display names and masked phone numbers."*

#### 🛍️ Real-World Intuitive Story
Think of converting raw passport documents into public security badges. You map internal DB fields into user-facing DTO representations.

#### 📥 Input & 📤 Output
- **Input**: `[UserEntity(1, "John", "Doe", "1234567890")]`
- **Output**: `[UserDTO(1, "John Doe", "******7890")]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"transform DTO"*, *"map entity to DTO"*.
- **Pattern**: **Stream `.map(MapperFunction)`**.

#### 🛠️ Step-by-Step Strategy
1. Stream DB entity records.
2. Pass each record through a transformation lambda or mapper function.
3. Collect transformed DTO objects into a new list.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class MapDTOTransformationNormal {
    public record UserEntity(long id, String firstName, String lastName, String phone) {}
    public record UserDTO(long id, String fullName, String maskedPhone) {}

    public static List<UserDTO> convertToDTO(List<UserEntity> entities) {
        List<UserDTO> dtos = new ArrayList<>();
        for (UserEntity e : entities) {
            String fullName = e.firstName() + " " + e.lastName();
            String masked = "******" + e.phone().substring(Math.max(0, e.phone().length() - 4));
            dtos.add(new UserDTO(e.id(), fullName, masked));
        }
        return dtos;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class MapDTOTransformationStream {
    public record UserEntity(long id, String firstName, String lastName, String phone) {}
    public record UserDTO(long id, String fullName, String maskedPhone) {}

    public static List<UserDTO> convertToDTO(List<UserEntity> entities) {
        return entities.stream()
            .map(e -> new UserDTO(
                e.id(),
                e.firstName() + " " + e.lastName(),
                "******" + e.phone().substring(Math.max(0, e.phone().length() - 4))
            ))
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard list iteration and instantiation.
- **Stream API**: `.map()` provides explicit 1-to-1 object mapping.

---

### Q49: Find Max & Min Simultaneously (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a log of daily stock prices `int[] prices`, calculate both the maximum price and minimum price recorded during the trading session in a single functional pass."*

#### 🛍️ Real-World Intuitive Story
Imagine walking down a row of temperature meters. You hold two sticky notes: 'Hottest' and 'Coldest'. As you inspect each meter, you update both notes dynamically.

#### 📥 Input & 📤 Output
- **Input**: `prices = [120, 450, 80, 950, 310]` $\rightarrow$ **Output**: `Min: 80, Max: 950`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"min and max in single pass"*, *"stock extrema"*.
- **Pattern**: **Collectors.summarizingInt / Custom Reducer**.

#### 🛠️ Step-by-Step Strategy
1. Stream primitive stock price values.
2. Collect summary statistics via `Collectors.summarizingInt()`.
3. Extract min and max values directly from `IntSummaryStatistics`.

#### ☕ Solution 1: Normal Java
```java
public class FindMinMaxNormal {
    public static void findMinMax(int[] prices) {
        if (prices == null || prices.length == 0) return;
        int min = prices[0];
        int max = prices[0];
        for (int p : prices) {
            if (p < min) min = p;
            if (p > max) max = p;
        }
        System.out.println("Min: " + min + ", Max: " + max);
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class FindMinMaxStream {
    public static void findMinMax(int[] prices) {
        IntSummaryStatistics stats = Arrays.stream(prices)
            .summaryStatistics();

        System.out.println("Min: " + stats.getMin() + ", Max: " + stats.getMax());
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Manual variable tracking inside loop.
- **Stream API**: `summaryStatistics()` calculates count, sum, min, average, and max simultaneously in one efficient pass.

---

### Q50: Maximum Product of Three Numbers (LeetCode #628)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums`, find three numbers whose product is maximum and return the maximum product value."*

#### 🛍️ Real-World Intuitive Story
If all numbers are positive, the maximum product comes from multiplying the 3 largest numbers. However, if there are negative numbers, multiplying 2 very large negative numbers yields a positive number! Thus, compare `largest1 * largest2 * largest3` against `smallest1 * smallest2 * largest1`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,2,3,4]` $\rightarrow$ **Output**: `24`
- **Input**: `nums = [-10,-10,5,2]` $\rightarrow$ **Output**: `500`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"max product of 3 numbers"*, *"accounting for negative numbers"*.
- **Pattern**: **Sort Array + Compare Boundary Products**.

#### 🛠️ Step-by-Step Strategy
1. Sort input array in ascending order.
2. Calculate Product A: product of 3 largest numbers at the end.
3. Calculate Product B: product of 2 smallest numbers (start) and largest number (end).
4. Return `Math.max(Product A, Product B)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.Arrays;

public class MaxProductThreeNormal {
    public static int maximumProduct(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int option1 = nums[n - 1] * nums[n - 2] * nums[n - 3];
        int option2 = nums[0] * nums[1] * nums[n - 1];
        return Math.max(option1, option2);
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class MaxProductThreeStream {
    public static int maximumProduct(int[] nums) {
        int[] sorted = Arrays.stream(nums).sorted().toArray();
        int n = sorted.length;
        return Math.max(
            sorted[n - 1] * sorted[n - 2] * sorted[n - 3],
            sorted[0] * sorted[1] * sorted[n - 1]
        );
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `Arrays.sort()` modifies array in-place.
- **Stream API**: `Arrays.stream(nums).sorted()` creates a new sorted stream cleanly.

---

### Q51: Sort Matrix by Diagonals (LeetCode #1329)

#### 📄 Real-World Interview Problem Statement
> *"Given an `m x n` matrix of integers, sort each matrix diagonal running from top-left to bottom-right in ascending order and return the resulting matrix."*

#### 🛍️ Real-World Intuitive Story
Imagine a checkerboard where diagonals from top-left to bottom-right are colored paths. Pull out all numbers sitting on each diagonal path, sort them, and put them back in place.

#### 📥 Input & 📤 Output
- **Input**: `mat = [[3,3,1,1],[2,2,1,2],[1,1,1,2]]`
- **Output**: `[[1,1,1,1],[1,2,2,2],[1,2,3,2]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"sort matrix diagonals"*, *"diagonal constant difference `i - j`"*.
- **Pattern**: **HashMap Keyed by `i - j` with PriorityQueue**.

#### 🛠️ Step-by-Step Strategy
1. Note that every cell `(i, j)` on the same diagonal shares the exact same index difference `i - j`.
2. Group all values into a `Map<Integer, PriorityQueue<Integer>>` keyed by `i - j`.
3. Re-populate the matrix in diagonal order by polling from the sorted PriorityQueues.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class SortMatrixDiagonalsNormal {
    public static int[][] diagonalSort(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        Map<Integer, PriorityQueue<Integer>> diagonals = new HashMap<>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                diagonals.computeIfAbsent(i - j, k -> new PriorityQueue<>()).add(mat[i][j]);
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = diagonals.get(i - j).poll();
            }
        }
        return mat;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.IntStream;

public class SortMatrixDiagonalsStream {
    public static int[][] diagonalSort(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        Map<Integer, PriorityQueue<Integer>> map = new HashMap<>();

        IntStream.range(0, m).forEach(i ->
            IntStream.range(0, n).forEach(j ->
                map.computeIfAbsent(i - j, k -> new PriorityQueue<>()).add(mat[i][j])
            )
        );

        IntStream.range(0, m).forEach(i ->
            IntStream.range(0, n).forEach(j ->
                mat[i][j] = map.get(i - j).poll()
            )
        );

        return mat;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Nested `for` loops are simple and clean.
- **Stream API**: `IntStream.range()` iterates over matrix coordinates functionally.

---

### Q52: Majority Element (LeetCode #169)

#### 📄 Real-World Interview Problem Statement
> *"Given an array `nums` of size `n`, return the majority element that appears more than `⌊n / 2⌋` times."*

#### 🛍️ Real-World Intuitive Story
Imagine a political election where one candidate receives more than 50% of all votes cast. Find the winning candidate.

#### 📥 Input & 📤 Output
- **Input**: `nums = [3,2,3]` $\rightarrow$ **Output**: `3`
- **Input**: `nums = [2,2,1,1,1,2,2]` $\rightarrow$ **Output**: `2`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"majority element (> n/2)"*, *"Boyer-Moore voting"*.
- **Pattern**: **Boyer-Moore Voting Algorithm / Grouping Frequency Map Stream**.

#### 🛠️ Step-by-Step Strategy
1. Count element frequencies using a frequency map or Boyer-Moore voting logic.
2. Find entry with frequency $> n / 2$.

#### ☕ Solution 1: Normal Java
```java
public class MajorityElementNormal {
    public static int majorityElement(int[] nums) {
        int count = 0, candidate = 0;
        for (int num : nums) {
            if (count == 0) candidate = num;
            count += (num == candidate) ? 1 : -1;
        }
        return candidate;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MajorityElementStream {
    public static int majorityElement(int[] nums) {
        return Arrays.stream(nums)
            .boxed()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .filter(e -> e.getValue() > nums.length / 2)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Boyer-Moore Voting algorithm achieves $O(N)$ time and $O(1)$ space.
- **Stream API**: Declarative frequency filter matching exact mathematical definition.

---

### Q53: Majority Element II (LeetCode #229)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer array `nums` of size `n`, find all elements that appear strictly more than `⌊n / 3⌋` times."*

#### 🛍️ Real-World Intuitive Story
Imagine an election where candidates win a seat if they secure strictly more than 1/3 of the total vote. There can be at most 2 such candidates.

#### 📥 Input & 📤 Output
- **Input**: `nums = [3,2,3]` $\rightarrow$ **Output**: `[3]`
- **Input**: `nums = [1,2]` $\rightarrow$ **Output**: `[1,2]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"more than n/3 times"*, *"multiple majority winners"*.
- **Pattern**: **Grouping Frequency Stream + Threshold Filter**.

#### 🛠️ Step-by-Step Strategy
1. Build frequency map of elements in array.
2. Filter keys whose frequency values exceed `nums.length / 3`.
3. Collect matching keys into a list.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class MajorityElementIINormal {
    public static List<Integer> majorityElement(int[] nums) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int num : nums) counts.put(num, counts.getOrDefault(num, 0) + 1);

        List<Integer> result = new ArrayList<>();
        int threshold = nums.length / 3;
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > threshold) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MajorityElementIIStream {
    public static List<Integer> majorityElement(int[] nums) {
        return Arrays.stream(nums)
            .boxed()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .filter(entry -> entry.getValue() > nums.length / 3)
            .map(Map.Entry::getKey)
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: HashMap counting with threshold filtering loop.
- **Stream API**: Functional pipeline `groupingBy() -> filter() -> toList()`.

---

### Q54: Find Target Indices After Sorting Array (LeetCode #2089)

#### 📄 Real-World Interview Problem Statement
> *"You are given a 0-indexed integer array `nums` and a target element `target`. Return a list of all index positions where `nums[i] == target` after sorting `nums` in non-decreasing order."*

#### 🛍️ Real-World Intuitive Story
Line up students in order of height. Find all positions in the line where a student of exact target height (e.g. 170cm) is standing.

#### 📥 Input & 📤 Output
- **Input**: `nums = [1,2,5,2,3]`, `target = 2` $\rightarrow$ **Output**: `[1, 2]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"target indices after sorting"*.
- **Pattern**: **IntStream Indexed Range Filter**.

#### 🛠️ Step-by-Step Strategy
1. Sort input array ascending.
2. Stream indices `0` to `sorted.length - 1`.
3. Filter indices where `sorted[i] == target`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class TargetIndicesNormal {
    public static List<Integer> targetIndices(int[] nums, int target) {
        Arrays.sort(nums);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                result.add(i);
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.IntStream;

public class TargetIndicesStream {
    public static List<Integer> targetIndices(int[] nums, int target) {
        int[] sorted = Arrays.stream(nums).sorted().toArray();
        return IntStream.range(0, sorted.length)
            .filter(i -> sorted[i] == target)
            .boxed()
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Sort + standard `for` loop index check.
- **Stream API**: `IntStream.range()` creates clean index stream processing.

---

### Q55: Count Elements With Strictly Smaller and Greater Elements (LeetCode #2148)

#### 📄 Real-World Interview Problem Statement
> *"Given an array of integers `nums`, return the count of elements that have both a strictly smaller element and a strictly greater element present in `nums`."*

#### 🛍️ Real-World Intuitive Story
Imagine a group of athletes rated by score. Count how many athletes are neither the absolute overall winner (maximum score) nor the absolute overall loser (minimum score).

#### 📥 Input & 📤 Output
- **Input**: `nums = [11, 7, 2, 15]` $\rightarrow$ **Output**: `2` (7 and 11 have both smaller and larger)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"strictly smaller and greater"*, *"exclude absolute min and max"*.
- **Pattern**: **Min/Max Extrema Filtering Stream**.

#### 🛠️ Step-by-Step Strategy
1. Find the global minimum and maximum in the array.
2. Stream array elements and count those strictly between `min` and `max`.

#### ☕ Solution 1: Normal Java
```java
public class CountElementsNormal {
    public static int countElements(int[] nums) {
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int num : nums) {
            if (num < min) min = num;
            if (num > max) max = num;
        }

        int count = 0;
        for (int num : nums) {
            if (num > min && num < max) count++;
        }
        return count;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class CountElementsStream {
    public static int countElements(int[] nums) {
        IntSummaryStatistics stats = Arrays.stream(nums).summaryStatistics();
        int min = stats.getMin();
        int max = stats.getMax();

        return (int) Arrays.stream(nums)
            .filter(x -> x > min && x < max)
            .count();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Two simple sequential loops.
- **Stream API**: `summaryStatistics()` paired with `.filter().count()`.

---

### Q56: Find N Unique Integers Sum up to Zero (LeetCode #1304)

#### 📄 Real-World Interview Problem Statement
> *"Given an integer `n`, return an array containing `n` unique integers such that their sum equals `0`."*

#### 🛍️ Real-World Intuitive Story
Think of balancing a seesaw scale. For every positive weight `+i`, place an equal negative weight `-i`. If `n` is odd, add a weight of `0` in the center.

#### 📥 Input & 📤 Output
- **Input**: `n = 5` $\rightarrow$ **Output**: `[-2, -1, 0, 1, 2]`
- **Input**: `n = 4` $\rightarrow$ **Output**: `[-2, -1, 1, 2]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"n unique integers sum to zero"*, *"symmetric pairs"*.
- **Pattern**: **IntStream Generation with Symmetric Index Offset**.

#### 🛠️ Step-by-Step Strategy
1. Generate sequence from `0` to `n - 1`.
2. Map each index `i` to value `i * 2 - n + 1`.

#### ☕ Solution 1: Normal Java
```java
public class SumZeroNormal {
    public static int[] sumZero(int n) {
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = i * 2 - n + 1;
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.IntStream;

public class SumZeroStream {
    public static int[] sumZero(int n) {
        return IntStream.range(0, n)
            .map(i -> i * 2 - n + 1)
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Simple index math inside loop.
- **Stream API**: `IntStream.range().map()` generates balanced zero-sum arrays.

---

### Q57: Divide Array Into Equal Pairs (LeetCode #2206)

#### 📄 Real-World Interview Problem Statement
> *"You are given an integer array `nums` consisting of `2 * n` integers. Determine if you can divide the array into `n` pairs such that every pair consists of equal elements."*

#### 🛍️ Real-World Intuitive Story
Imagine matching socks out of a laundry basket. Every single sock must find an identical twin pair. If any sock is left without a pair (odd frequency count), return `false`.

#### 📥 Input & 📤 Output
- **Input**: `nums = [3,2,3,2,2,2]` $\rightarrow$ **Output**: `true` (Pairs: (3,3), (2,2), (2,2))

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"divide into equal pairs"*, *"all frequencies must be even"*.
- **Pattern**: **Frequency Map AllMatch Even Check**.

#### 🛠️ Step-by-Step Strategy
1. Count element frequencies.
2. Verify that every frequency count is even (`count % 2 == 0`).

#### ☕ Solution 1: Normal Java
```java
import java.util.HashMap;
import java.util.Map;

public class DivideEqualPairsNormal {
    public static boolean divideArray(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) map.put(num, map.getOrDefault(num, 0) + 1);

        for (int count : map.values()) {
            if (count % 2 != 0) return false;
        }
        return true;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DivideEqualPairsStream {
    public static boolean divideArray(int[] nums) {
        return Arrays.stream(nums)
            .boxed()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .values().stream()
            .allMatch(count -> count % 2 == 0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Map population + value loop check.
- **Stream API**: `.values().stream().allMatch(c -> c % 2 == 0)` evaluates pair validity declaratively.

---

### Q58: Keep Multiplying Found Values by Two (LeetCode #2154)

#### 📄 Real-World Interview Problem Statement
> *"You are given an array of integers `nums` and an initial target integer `original`. Search for `original` in `nums`. If found, multiply `original` by 2. Repeat this process until `original` is no longer found in `nums`."*

#### 🛍️ Real-World Intuitive Story
Think of a video game power-up multiplier. Start with score `original`. Every time you encounter a matching power-up in the game level (`nums`), your score doubles ($3 \rightarrow 6 \rightarrow 12 \rightarrow 24$). Stop when no more matching power-ups exist.

#### 📥 Input & 📤 Output
- **Input**: `nums = [5,3,6,11,12]`, `original = 3` $\rightarrow$ **Output**: `24` (3 -> 6 -> 12 -> 24)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"keep multiplying found value by two"*, *"repeat lookup"*.
- **Pattern**: **HashSet Instant Lookup / Stream Reduction Iteration**.

#### 🛠️ Step-by-Step Strategy
1. Add all array elements into a `Set` for $O(1)$ fast lookup.
2. While `set.contains(original)`, update `original *= 2`.
3. Return `original`.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashSet;
import java.util.Set;

public class MultiplyFoundValuesNormal {
    public static int findFinalValue(int[] nums, int original) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) set.add(num);

        while (set.contains(original)) {
            original *= 2;
        }
        return original;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MultiplyFoundValuesStream {
    public static int findFinalValue(int[] nums, int original) {
        Set<Integer> set = Arrays.stream(nums).boxed().collect(Collectors.toSet());
        while (set.contains(original)) {
            original *= 2;
        }
        return original;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard `HashSet` population and `while` loop multiplier.
- **Stream API**: `Arrays.stream(nums).boxed().collect(Collectors.toSet())` converts array into set in 1 clean line.
