# Chapter 13: All 57-Page Coding Challenges & Stream API Solutions

This master guide compiles and solves every single practical coding puzzle and Stream API challenge asked in the company interview questions.

---

## 📑 Challenge Index
1. [Even at Even Index & Odd at Odd Index in Sorted Order (Inferyx Hinjewadi)](#1-even-at-even-index--odd-at-odd-index-in-sorted-order-inferyx-hinjewadi)
2. [In-Place Array Reversal Without Built-In Functions (Inferyx Hinjewadi)](#2-in-place-array-reversal-without-built-in-functions-inferyx-hinjewadi)
3. [Find Nth Highest Price Product from HashMap (Applied Cloud Computing)](#3-find-nth-highest-price-product-from-hashmap-applied-cloud-computing)
4. [File Extension Count Map using Java 8 Streams (Applied Cloud Computing)](#4-file-extension-count-map-using-java-8-streams-applied-cloud-computing)
5. [Integer to Binary Representation Without Built-in Methods (NeoSoft)](#5-integer-to-binary-representation-without-built-in-methods-neosoft)
6. [Sum of Digits in Alphanumeric String "ABCXYZ123" (Rebit)](#6-sum-of-digits-in-alphanumeric-string-abcxyz123-rebit)
7. [Character with Second Highest Frequency in String (Rebit)](#7-character-with-second-highest-frequency-in-string-rebit)
8. [Multi-Level Grouping: Designation & Gender Count via Streams (Cbt Infotech / Rebit)](#8-multi-level-grouping-designation--gender-count-via-streams-cbt-infotech--rebit)
9. [City Name Character Length Frequency via Java 8 (Crisil)](#9-city-name-character-length-frequency-via-java-8-crisil)
10. [Reverse Individual Words "hello world" -> "olleh dlrow" (In Solutions Global)](#10-reverse-individual-words-hello-world---olleh-dlrow-in-solutions-global)
11. [Shift All Zeros to Right Side of Array in $O(N)$ (NuSummit)](#11-shift-all-zeros-to-right-side-of-array-in-on-nusummit)
12. [Longest Consecutive Increasing Subarray (Servosys)](#12-longest-consecutive-increasing-subarray-servosys)
13. [Rightmost Digits Transformation (V2STech Solutions)](#13-rightmost-digits-transformation-v2stech-solutions)
14. [StudentDTO Filtering, Sorting & Email Extraction via Stream (Team Computers)](#14-studentdto-filtering-sorting--email-extraction-via-stream-team-computers)
15. [Stream Lazy Evaluation Trap: Why `list.stream().map(i -> i * i)` Outputs Original Values](#15-stream-lazy-evaluation-trap-why-liststreammapi---i--i-outputs-original-values)

---

## 1. Even at Even Index & Odd at Odd Index in Sorted Order (Inferyx Hinjewadi)

### Problem Statement
Given an array of integers, arrange even numbers at even indices and odd numbers at odd indices, with both subsets sorted in ascending order. Do not use built-in sort or collection utilities.
- **Input**: `[5, 10, 9, 14, 7, 12]`
- **Output**: `[5, 10, 7, 12, 9, 14]` (or odd at even: check question variant: `5 10 7 12 9 14` -> odd at index 0,2,4 and even at index 1,3,5).

### Previous Knowledge Required
- Array traversal, manual sorting (Bubble / Insertion Sort), two-pointer array manipulation.

### Logic & Solution
1. Count the number of odd and even elements.
2. Separate into two temporary primitive arrays: `odds[]` and `evens[]`.
3. Manually sort both arrays using standard bubble/insertion sort.
4. Merge them back into the target array alternating by index.

```java
public class EvenOddArranger {
    public static void arrange(int[] arr) {
        int n = arr.length;
        int oddCount = 0, evenCount = 0;
        for (int x : arr) {
            if (x % 2 == 0) evenCount++;
            else oddCount++;
        }

        int[] evens = new int[evenCount];
        int[] odds = new int[oddCount];
        int eIdx = 0, oIdx = 0;

        for (int x : arr) {
            if (x % 2 == 0) evens[eIdx++] = x;
            else odds[oIdx++] = x;
        }

        // Manual sort without built-in methods (Bubble Sort)
        manualSort(odds);
        manualSort(evens);

        // Place alternately into result
        int i = 0, o = 0, e = 0;
        while (o < odds.length || e < evens.length) {
            if (o < odds.length) arr[i++] = odds[o++];
            if (e < evens.length) arr[i++] = evens[e++];
        }
    }

    private static void manualSort(int[] a) {
        int len = a.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                }
            }
        }
    }
}
```

---

## 2. In-Place Array Reversal Without Built-In Functions (Inferyx Hinjewadi)

### Problem Statement
Reverse array in-place without creating a secondary array.
- **Input**: `{12, 9, 21, 17, 33, 7}`
- **Output**: `{7, 33, 17, 21, 9, 12}`

### Logic & Solution
Use the classic **Two-Pointer Technique**. Initialize `left = 0` and `right = arr.length - 1`. Swap `arr[left]` and `arr[right]`, then increment `left` and decrement `right` until `left >= right`.
- **Time Complexity**: $O(N)$
- **Space Complexity**: $O(1)$ auxiliary memory.

```java
public class InPlaceArrayReversal {
    public static void reverseInPlace(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            // Swap without extra array
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;

            left++;
            right--;
        }
    }
}
```

---

## 3. Find Nth Highest Price Product from HashMap (Applied Cloud Computing)

### Problem Statement
Given a `Map<String, String>` of products and prices as strings, find the $N$-th highest price product using Java 8 Stream API.

```java
Map<String, String> productMap = new HashMap<>();
productMap.put("Phone", "16000.0");
productMap.put("Tablet", "6000.0");
productMap.put("Laptop", "150000.0");
productMap.put("Monitor", "3000.0");
productMap.put("Camera", "8000.0");
productMap.put("Keyboard", "1800.0");
```

### Logic & Solution
1. Convert `Double.parseDouble(entry.getValue())`.
2. Group products by unique price using `Collectors.groupingBy` with reverse sorting to handle ties cleanly.
3. Skip $N - 1$ unique prices and pick the first element.

```java
public class NthHighestProduct {
    public static Map.Entry<Double, List<String>> findNthHighest(Map<String, String> map, int n) {
        return map.entrySet().stream()
            // Group by parsed Double price -> List of product names
            .collect(Collectors.groupingBy(
                e -> Double.parseDouble(e.getValue()),
                Collectors.mapping(Map.Entry::getKey, Collectors.toList())
            ))
            .entrySet().stream()
            // Sort by price descending
            .sorted(Map.Entry.<Double, List<String>>comparingByKey().reversed())
            .skip(n - 1)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("N exceeds unique price count"));
    }

    public static void main(String[] args) {
        // Example: 2nd highest price -> Phone ($16000.0)
        var result = findNthHighest(productMap, 2);
        System.out.println("2nd Highest Price: " + result.getKey() + " -> " + result.getValue());
    }
}
```

---

## 4. File Extension Count Map using Java 8 Streams (Applied Cloud Computing)

### Problem Statement
Given a list of filenames, count the frequency of each file extension into a `Map<String, Long>`.
- **Input**: `["doc.txt", "resume.pdf", "notes.txt", "report.pdf", "sheet.xlsx", "bio.pdf"]`
- **Output**: `{txt=2, pdf=3, xlsx=1}`

### Practical Code
```java
public class FileExtensionCounter {
    public static Map<String, Long> countExtensions(List<String> fileNames) {
        return fileNames.stream()
            .filter(name -> name != null && name.contains("."))
            .map(name -> name.substring(name.lastIndexOf('.') + 1).toLowerCase())
            .collect(Collectors.groupingBy(
                Function.identity(), 
                Collectors.counting()
            ));
    }
}
```

---

## 5. Integer to Binary Representation Without Built-in Methods (NeoSoft)

### Problem Statement
Convert an integer to its binary string without using `Integer.toBinaryString()` or any third-party library.
- **Input**: `8`
- **Output**: `"1000"`

### Logic & Solution
Continuously divide the number by 2 and collect the remainder (`num % 2`). Prepend the remainder to a `StringBuilder` or reverse at the end. Handle edge case `0`.

```java
public class CustomBinaryConverter {
    public static String toBinary(int n) {
        if (n == 0) return "0";

        StringBuilder binary = new StringBuilder();
        int num = Math.abs(n);

        while (num > 0) {
            int remainder = num % 2;
            binary.append(remainder);
            num /= 2;
        }

        // Add negative sign if necessary
        if (n < 0) binary.append("-");

        return binary.reverse().toString();
    }
}
```

---

## 6. Sum of Digits in Alphanumeric String "ABCXYZ123" (Rebit)

### Problem Statement
Given `String s = "ABCXYZ123"`, sum all numerical digits inside the string ($1 + 2 + 3 = 6$).

### Practical Code (Java 8 Streams)
```java
public class AlphanumericDigitSum {
    public static int sumDigits(String input) {
        if (input == null) return 0;

        return input.chars()
            .filter(Character::isDigit)
            .map(Character::getNumericValue) // Converts char '1' to int 1
            .sum();
    }
}
```

---

## 7. Character with Second Highest Frequency in String (Rebit)

### Problem Statement
Given `String s = "aaabbc"`, find the character with the second highest frequency.
- Counts: `'a': 3`, `'b': 2`, `'c': 1`
- Highest: `'a'` (3), **Second Highest: `'b'` (2)**.

```java
public class SecondHighestFrequencyChar {
    public static char findSecondHighest(String str) {
        // Step 1: Count frequency of each char
        Map<Character, Long> freqMap = str.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Step 2: Group by frequency and sort frequencies descending
        return freqMap.entrySet().stream()
            .collect(Collectors.groupingBy(
                Map.Entry::getValue,
                Collectors.mapping(Map.Entry::getKey, Collectors.toList())
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<Long, List<Character>>comparingByKey().reversed())
            .skip(1) // Skip 1st highest frequency
            .findFirst()
            .map(e -> e.getValue().get(0))
            .orElseThrow(() -> new RuntimeException("No second highest frequency found"));
    }
}
```

---

## 8. Multi-Level Grouping: Designation & Gender Count via Streams (Cbt Infotech / Rebit)

### Problem Statement
Given a list of `Employee(id, name, salary, gender, designation)`, group by `designation` and then by `gender` to get counts: e.g. `{"SQL Developer": {"Female": 2, "Male": 1}}`.

```java
public record Employee(Long id, String name, double salary, String gender, String designation) {}

public class MultiLevelEmployeeGrouping {
    public static Map<String, Map<String, Long>> countByDesignationAndGender(List<Employee> employees) {
        return employees.stream()
            .collect(Collectors.groupingBy(
                Employee::designation,
                Collectors.groupingBy(
                    Employee::gender,
                    Collectors.counting()
                )
            ));
    }
}
```

---

## 9. City Name Character Length Frequency via Java 8 (Crisil)

### Problem Statement
Input: `"Delhi", "Shanghai", "Dhaka", "SaoPaulo", "MexicoCity", "Beijing", "Mumbai", "Osaka"`.
Count how many city names have the same number of characters.
- Length 5: Delhi, Dhaka, Osaka (Count: 3)
- Length 6: Mumbai (Count: 1)
- Length 7: Beijing (Count: 1)
- Length 8: Shanghai, SaoPaulo (Count: 2)
- Length 10: MexicoCity (Count: 1)

```java
public class CityLengthCounter {
    public static void main(String[] args) {
        List<String> cities = List.of(
            "Delhi", "Shanghai", "Dhaka", "SaoPaulo", "MexicoCity", "Beijing", "Mumbai", "Osaka"
        );

        Map<Integer, Long> lengthCount = cities.stream()
            .collect(Collectors.groupingBy(
                String::length,
                Collectors.counting()
            ));

        lengthCount.forEach((len, count) -> 
            System.out.println("Length " + len + " characters: " + count + " cities")
        );
    }
}
```

---

## 10. Reverse Individual Words "hello world" -> "olleh dlrow" (In Solutions Global)

### Approach 1: Using Built-in Methods
```java
public static String reverseWordsBuiltIn(String sentence) {
    return Arrays.stream(sentence.split(" "))
        .map(word -> new StringBuilder(word).reverse().toString())
        .collect(Collectors.joining(" "));
}
```

### Approach 2: Without Any Built-In Reverse Methods (Pointer Approach)
```java
public static String reverseWordsManual(String sentence) {
    char[] chars = sentence.toCharArray();
    int start = 0;

    for (int end = 0; end <= chars.length; end++) {
        // When space is reached or end of string
        if (end == chars.length || chars[end] == ' ') {
            reverseWord(chars, start, end - 1);
            start = end + 1;
        }
    }
    return new String(chars);
}

private static void reverseWord(char[] arr, int left, int right) {
    while (left < right) {
        char temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
        left++;
        right--;
    }
}
```

---

## 11. Shift All Zeros to Right Side of Array in $O(N)$ (NuSummit)

### Problem Statement
Shift all zeros in array `[1, 0, 5, 7, 0, 6, 4, 0, 2, 0, 8, 0]` to the right while preserving relative order of non-zero elements.
- **Output**: `[1, 5, 7, 6, 4, 2, 8, 0, 0, 0, 0, 0]`

```java
public class ZeroShifter {
    public static void shiftZeros(int[] arr) {
        int nonZeroIndex = 0;

        // Shift non-zeros forward
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                arr[nonZeroIndex++] = arr[i];
            }
        }

        // Fill remaining slots with 0
        while (nonZeroIndex < arr.length) {
            arr[nonZeroIndex++] = 0;
        }
    }
}
```

---

## 12. Longest Consecutive Increasing Subarray (Servosys)

### Problem Statement
Input: `arr = {1, 5, 6, 7, 9, 10, 11, 12, 13}`. Find longest consecutive increasing sequence ($a[i] == a[i-1] + 1$).
- Increasing sequences: `[5, 6, 7]` (length 3), `[9, 10, 11, 12, 13]` (length 5).
- Output: `9, 10, 11, 12, 13`.

```java
public class LongestIncreasingSubarray {
    public static int[] findLongest(int[] arr) {
        if (arr == null || arr.length == 0) return new int[0];

        int maxLen = 1, currentLen = 1;
        int maxStart = 0, currentStart = 0;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1] + 1) {
                currentLen++;
            } else {
                if (currentLen > maxLen) {
                    maxLen = currentLen;
                    maxStart = currentStart;
                }
                currentLen = 1;
                currentStart = i;
            }
        }

        if (currentLen > maxLen) {
            maxLen = currentLen;
            maxStart = currentStart;
        }

        return Arrays.copyOfRange(arr, maxStart, maxStart + maxLen);
    }
}
```

---

## 13. Rightmost Digits Transformation (V2STech Solutions)

### Problem Statement
Given a list of non-negative integers, return a list containing only their rightmost digits:
- `[1, 22, 93]` -> `[1, 2, 3]`
- `[16, 8, 886, 8, 1]` -> `[6, 8, 6, 8, 1]`
- `[10, 0]` -> `[0, 0]`

```java
public class RightmostDigitExtractor {
    public static List<Integer> rightDigit(List<Integer> nums) {
        return nums.stream()
            .map(n -> n % 10) // Extracts the rightmost unit digit
            .collect(Collectors.toList());
    }
}
```

---

## 14. StudentDTO Filtering, Sorting & Email Extraction via Stream (Team Computers)

### Problem Statement
Given `StudentDTO(name, email, city, grade, age)`, return list of emails where:
1. `age > 18`
2. `city.equalsIgnoreCase("Mumbai")`
3. Sorted by `grade` ascending.

```java
public record StudentDTO(String name, String email, String city, int grade, int age) {}

public class StudentStreamProcessor {
    public static List<String> getEligibleEmails(List<StudentDTO> students) {
        return students.stream()
            .filter(s -> s.age() > 18)
            .filter(s -> "Mumbai".equalsIgnoreCase(s.city()))
            .sorted(Comparator.comparingInt(StudentDTO::grade))
            .map(StudentDTO::email)
            .collect(Collectors.toList());
    }
}
```

---

## 15. Stream Lazy Evaluation Trap: Why `list.stream().map(i -> i * i)` Outputs Original Values

### Interview Question (Applied Cloud Computing):
```java
List<Integer> list = new ArrayList<>();
list.add(4);
list.add(4);
list.add(4);
list.add(4);

list.stream().map(i -> i * i);

for(Integer num: list){
    System.out.println(num);
}
```
**What will be the output, and why?**

### The Answer:
The output is:
```
4
4
4
4
```
### Technical Explanation:
1. **No Terminal Operation Executed**: Streams in Java are evaluated **lazily**. The method `.map()` is an **intermediate operation**. In Java Stream pipelines, intermediate operations are never executed until a **terminal operation** (e.g., `.collect()`, `.forEach()`, `.count()`) is invoked! Because no terminal operation was attached, the lambda `i -> i * i` was **never even called**.
2. **Streams Do Not Mutate the Source**: Even if `.collect()` was called, Streams adhere to functional immutability principles and **never modify their backing collection**. The output would only change if the result of the stream was assigned to a new list.

---

*Continue to [Chapter 14: Angular Frontend, RxJS & Fullstack Integration](file:///Users/apple/Workspace/Home/Javascript/Learning/14_Angular_Frontend_and_FullStack_Integration.md)*
