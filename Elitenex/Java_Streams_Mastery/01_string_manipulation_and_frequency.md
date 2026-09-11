# 🔤 Category 1: String Manipulation & Character Frequency

This guide provides exhaustive, dual-method solutions for **18 Core String Manipulation & Character Frequency Problems**. 

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
- [Q1: Valid Anagram (LeetCode #242)](#q1-valid-anagram-leetcode-242)
- [Q2: First Unique Character in a String (LeetCode #387)](#q2-first-unique-character-in-a-string-leetcode-387)
- [Q3: Group Anagrams (LeetCode #49)](#q3-group-anagrams-leetcode-49)
- [Q4: Reverse Words in a String (LeetCode #151)](#q4-reverse-words-in-a-string-leetcode-151)
- [Q5: Sort Characters By Frequency (LeetCode #451)](#q5-sort-characters-by-frequency-leetcode-451)
- [Q6: Find the Difference (LeetCode #389)](#q6-find-the-difference-leetcode-389)
- [Q7: Isomorphic Strings (LeetCode #205)](#q7-isomorphic-strings-leetcode-205)
- [Q8: Word Pattern (LeetCode #290)](#q8-word-pattern-leetcode-290)
- [Q9: Most Common Word (LeetCode #819)](#q9-most-common-word-leetcode-819)
- [Q10: Ransom Note (LeetCode #383)](#q10-ransom-note-leetcode-383)
- [Q11: Reorganize String (LeetCode #767)](#q11-reorganize-string-leetcode-767)
- [Q12: Check if All Characters Have Equal Occurrences (LeetCode #1941)](#q12-check-if-all-characters-have-equal-occurrences-leetcode-1941)
- [Q13: Determine if Two Strings Are Close (LeetCode #1657)](#q13-determine-if-two-strings-are-close-leetcode-1657)
- [Q14: Longest Substring Without Repeating Characters (LeetCode #3)](#q14-longest-substring-without-repeating-characters-leetcode-3)
- [Q15: Custom Sort String (LeetCode #791)](#q15-custom-sort-string-leetcode-791)
- [Q16: Count Vowel Substrings of a String (LeetCode #2062)](#q16-count-vowel-substrings-of-a-string-leetcode-2062)
- [Q17: Decode String Pattern Counts (Interview Classic)](#q17-decode-string-pattern-counts-interview-classic)
- [Q18: Check if Sentence Is Pangram (LeetCode #1832)](#q18-check-if-sentence-is-pangram-leetcode-1832)

---

### Q1: Valid Anagram (LeetCode #242)

#### 📄 Real-World Interview Problem Statement
> *"You are building a security verification service for a mobile banking platform. During a multi-factor authentication check, the server generates a challenge token string `s` and receives a user response code string `t`. The server must verify whether the response code `t` is an exact scrambled rearrangement of the challenge token `s`. A response code is considered valid if it contains the exact same set of characters with identical frequencies as the challenge token, regardless of order. Write a algorithm to validate if `t` is a valid anagram of `s`."*

#### 🛍️ Real-World Intuitive Story
Imagine two shopping bags of fruits. Bag A contains `[apple, apple, banana]` and Bag B contains `[banana, apple, apple]`. Even though they are packed in a different order, both bags contain the exact same quantity of each fruit. If Bag B contained `[apple, banana, orange]`, it would not match because `orange` was added and one `apple` was removed.

#### 📥 Input & 📤 Output
- **Input**: `s = "anagram"`, `t = "nagaram"` $\rightarrow$ **Output**: `true`
- **Input**: `s = "rat"`, `t = "car"` $\rightarrow$ **Output**: `false`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"exact scrambled rearrangement"*, *"same characters with identical frequencies"*.
- **Pattern**: **Character Frequency Comparison**.

#### 🛠️ Step-by-Step Strategy
1. If lengths of `s` and `t` differ, return `false` immediately.
2. Count frequency of each character in `s` and `t`.
3. Compare the two character count frequency maps for equality.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashMap;
import java.util.Map;

public class ValidAnagramNormal {
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        Map<Character, Integer> counts = new HashMap<>();
        for (char c : s.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        for (char c : t.toCharArray()) {
            if (!counts.containsKey(c)) return false;
            counts.put(c, counts.get(c) - 1);
            if (counts.get(c) == 0) counts.remove(c);
        }

        return counts.isEmpty();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ValidAnagramStream {
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        Map<Character, Long> mapS = s.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<Character, Long> mapT = t.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return mapS.equals(mapT);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Single loop with `counts.remove()` operates slightly faster with minimal object creation.
- **Stream API**: `Collectors.groupingBy()` builds character frequency maps declaratively in 3 readable lines.

---

### Q2: First Unique Character in a String (LeetCode #387)

#### 📄 Real-World Interview Problem Statement
> *"You are designing a log processor for a real-time event streaming pipeline. As log events arrive represented as a string `s`, you need to identify the zero-based index of the very first character that appears only once in the entire stream. If every character in the log string repeats at least once, return `-1`. Assume the string consists only of lowercase English letters."*

#### 🛍️ Real-World Intuitive Story
Think of a queue of people calling into a customer service desk. People with names `[Alice, Bob, Alice, Charlie, Bob]` call in. `Alice` called twice, `Bob` called twice, but `Charlie` called only once. The first caller who called uniquely without calling back is `Charlie`, who called at position index 3.

#### 📥 Input & 📤 Output
- **Input**: `s = "leetcode"` $\rightarrow$ **Output**: `0` (character `'l'`)
- **Input**: `s = "loveleetcode"` $\rightarrow$ **Output**: `2` (character `'v'`)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"first character that appears only once"*, *"preserve original string order"*.
- **Pattern**: **Insertion-Ordered Frequency Mapping (`LinkedHashMap`)**.

#### 🛠️ Step-by-Step Strategy
1. Count character frequencies using a `LinkedHashMap` to maintain insertion order.
2. Find the first character entry in the map with a frequency count of `1`.
3. Return its index in the original string `s`.

#### ☕ Solution 1: Normal Java
```java
import java.util.LinkedHashMap;
import java.util.Map;

public class FirstUniqueCharNormal {
    public static int firstUniqChar(String s) {
        Map<Character, Integer> counts = new LinkedHashMap<>();
        for (char c : s.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < s.length(); i++) {
            if (counts.get(s.charAt(i)) == 1) {
                return i;
            }
        }
        return -1;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FirstUniqueCharStream {
    public static int firstUniqChar(String s) {
        Map<Character, Long> freqMap = s.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(
                Function.identity(),
                LinkedHashMap::new,
                Collectors.counting()
            ));

        Character firstUnique = freqMap.entrySet().stream()
            .filter(entry -> entry.getValue() == 1)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);

        return firstUnique == null ? -1 : s.indexOf(firstUnique);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard loop index lookup avoids stream object allocations.
- **Stream API**: Passing `LinkedHashMap::new` to `groupingBy()` guarantees insertion-order preservation across stream collections.

---

### Q3: Group Anagrams (LeetCode #49)

#### 📄 Real-World Interview Problem Statement
> *"An e-commerce search indexing engine receives an array of product tag strings `strs`. Due to data ingestion errors, some tags are scrambled anagram variants of each other (e.g., `'eat'`, `'tea'`, and `'ate'`). Write a function that clusters all anagram tags together into groups. You can return the grouped list in any order."*

#### 🛍️ Real-World Intuitive Story
Imagine sorting letters into mailbox slots. If you take words like `"eat"`, `"tea"`, and `"ate"` and sort their individual letters alphabetically, all three turn into `"aet"`. By writing `"aet"` on the mailbox label, every anagram word drops into that exact same slot.

#### 📥 Input & 📤 Output
- **Input**: `strs = ["eat","tea","tan","ate","nat","bat"]`
- **Output**: `[["bat"],["nat","tan"],["ate","eat","tea"]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"group words together"*, *"cluster anagram variants"*.
- **Pattern**: **Sorted Signature Key Grouping (`groupingBy`)**.

#### 🛠️ Step-by-Step Strategy
1. For each string, convert to character array and sort it alphabetically.
2. Use the sorted string as a dictionary map key.
3. Group all original words matching that sorted key into a list.

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

            if (!map.containsKey(sortedKey)) {
                map.put(sortedKey, new ArrayList<>());
            }
            map.get(sortedKey).add(str);
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
                .collect(Collectors.groupingBy(s -> {
                    char[] chars = s.toCharArray();
                    Arrays.sort(chars);
                    return new String(chars);
                }))
                .values()
        );
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Stream API Elegance**: `Collectors.groupingBy()` compresses 15+ lines of map initialization into a single clean pipeline.

---

### Q4: Reverse Words in a String (LeetCode #151)

#### 📄 Real-World Interview Problem Statement
> *"You are implementing a text formatting utility for a document editor. Given an input string `s` containing words separated by spaces, reverse the order of the words. The input string may contain leading, trailing, or multiple consecutive spaces between words. The formatted output string must contain words joined by a single space with no extra leading or trailing spaces."*

#### 🛍️ Real-World Intuitive Story
Think of sentences printed on paper cards pinned to a board: `[ "the", "sky", "is", "blue" ]`. Unpin all cards, flip the order from right to left, and pin them back down: `[ "blue", "is", "sky", "the" ]`.

#### 📥 Input & 📤 Output
- **Input**: `s = "  the sky is  blue "` $\rightarrow$ **Output**: `"blue is sky the"`
- **Input**: `s = "a good   example"` $\rightarrow$ **Output**: `"example good a"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"reverse order of words"*, *"strip extra spaces"*.
- **Pattern**: **Regex Whitespace Splitting & Stream Joining**.

#### 🛠️ Step-by-Step Strategy
1. Trim leading and trailing spaces.
2. Split string by whitespace regex (`" +"`).
3. Reverse the array of words.
4. Join words with a single space delimiter (`" "`).

#### ☕ Solution 1: Normal Java
```java
public class ReverseWordsNormal {
    public static String reverseWords(String s) {
        String[] words = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i]);
            if (i > 0) sb.append(" ");
        }
        return sb.toString();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class ReverseWordsStream {
    public static String reverseWords(String s) {
        String[] words = s.trim().split("\\s+");
        Collections.reverse(Arrays.asList(words));
        return Arrays.stream(words).collect(Collectors.joining(" "));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- `Collectors.joining(" ")` formats string tokens with spaces without manual loop index bounds checking.

---

### Q5: Sort Characters By Frequency (LeetCode #451)

#### 📄 Real-World Interview Problem Statement
> *"A text analytics platform processes customer feedback strings `s`. To highlight key character metrics, reorder the characters of `s` in descending order based on their frequency of occurrence. If multiple characters have the same frequency, their relative order does not matter."*

#### 🛍️ Real-World Intuitive Story
Imagine counting election votes for candidates `'e'`, `'r'`, `'t'`. Candidate `'e'` got 2 votes, `'r'` got 1 vote, and `'t'` got 1 vote. When printing the scoreboard, print candidate names repeated by their vote counts from highest to lowest: `"eert"`.

#### 📥 Input & 📤 Output
- **Input**: `s = "tree"` $\rightarrow$ **Output**: `"eert"` (or `"eetr"`)
- **Input**: `s = "cccaaa"` $\rightarrow$ **Output**: `"cccaaa"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"sort by frequency descending"*, *"repeat character by count"*.
- **Pattern**: **Frequency Map + Entry Sorting + String Repeat**.

#### 🛠️ Step-by-Step Strategy
1. Build character frequency count map.
2. Sort map entries by frequency value descending.
3. Repeat each character by its frequency using `String.repeat()` and join.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class SortByFrequencyNormal {
    public static String frequencySort(String s) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : s.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        List<Character> characters = new ArrayList<>(counts.keySet());
        characters.sort((a, b) -> counts.get(b) - counts.get(a));

        StringBuilder sb = new StringBuilder();
        for (char c : characters) {
            int freq = counts.get(c);
            for (int i = 0; i < freq; i++) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SortByFrequencyStream {
    public static String frequencySort(String s) {
        Map<Character, Long> freqMap = s.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return freqMap.entrySet().stream()
            .sorted(Map.Entry.<Character, Long>comparingByValue().reversed())
            .map(entry -> String.valueOf(entry.getKey()).repeat(entry.getValue().intValue()))
            .collect(Collectors.joining());
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Combining `comparingByValue().reversed()`, `String.repeat()`, and `joining()` turns custom frequency sorting into a clean 1-pipeline stream.

---

### Q6: Find the Difference (LeetCode #389)

#### 📄 Real-World Interview Problem Statement
> *"You are given two strings `s` and `t`. String `t` is generated by randomly shuffling string `s` and then adding one extra character at a random position. Write a function to find and return the extra character that was added to `t`."*

#### 🛍️ Real-World Intuitive Story
Imagine a card game where player A has cards `[A, B, C, D]`. Player B takes player A's cards, shuffles them, and secretly slips in one extra card `E`, making `[C, A, E, D, B]`. If you cancel out matching pairs of cards between player A and player B, only card `E` remains.

#### 📥 Input & 📤 Output
- **Input**: `s = "abcd"`, `t = "abcde"` $\rightarrow$ **Output**: `'e'`
- **Input**: `s = ""`, `t = "y"` $\rightarrow$ **Output**: `'y'`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"one extra character added"*, *"shuffled string permutation"*.
- **Pattern**: **Bitwise XOR (`^`) Reduction across Combined Streams**.

#### 🛠️ Step-by-Step Strategy
1. Combine character streams of `s` and `t`.
2. Apply Bitwise XOR (`^`) reduction across all characters.
3. Matching characters cancel out ($x \oplus x = 0$), leaving only the extra character.

#### ☕ Solution 1: Normal Java
```java
public class FindDifferenceNormal {
    public static char findTheDifference(String s, String t) {
        char c = 0;
        for (char ch : s.toCharArray()) c ^= ch;
        for (char ch : t.toCharArray()) c ^= ch;
        return c;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.IntStream;

public class FindDifferenceStream {
    public static char findTheDifference(String s, String t) {
        int xorResult = IntStream.concat(s.chars(), t.chars())
            .reduce(0, (a, b) -> a ^ b);
        return (char) xorResult;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- `IntStream.concat(s.chars(), t.chars())` processes characters of both strings as a single continuous stream reduction in 1 pass.

---

### Q7: Isomorphic Strings (LeetCode #205)

#### 📄 Real-World Interview Problem Statement
> *"Given two strings `s` and `t`, determine if they are isomorphic. Two strings are isomorphic if the characters in `s` can be replaced to get `t` while preserving character order. Every occurrence of a character must map to the same character, and no two characters can map to the same character (1-to-1 bijection)."*

#### 🛍️ Real-World Intuitive Story
Think of a secret cipher key: `e` $\rightarrow$ `a`, `g` $\rightarrow$ `d`. If the secret word `"egg"` maps to `"add"`, the mapping is valid (isomorphic). But if `"foo"` tries to map to `"bar"`, `o` would have to map to both `a` and `r`, which breaks cipher rules!

#### 📥 Input & 📤 Output
- **Input**: `s = "egg"`, `t = "add"` $\rightarrow$ **Output**: `true`
- **Input**: `s = "foo"`, `t = "bar"` $\rightarrow$ **Output**: `false`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"1-to-1 character mapping"*, *"preserve character order"*.
- **Pattern**: **Index Occurrence Matching (`indexOf`)**.

#### 🛠️ Step-by-Step Strategy
1. Verify `s.length() == t.length()`.
2. Check that at every position `i`, the first occurrence index of `s.charAt(i)` matches the first occurrence index of `t.charAt(i)`.

#### ☕ Solution 1: Normal Java
```java
public class IsomorphicStringsNormal {
    public static boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length()) return false;
        int[] m1 = new int[256];
        int[] m2 = new int[256];

        for (int i = 0; i < s.length(); i++) {
            if (m1[s.charAt(i)] != m2[t.charAt(i)]) return false;
            m1[s.charAt(i)] = i + 1;
            m2[t.charAt(i)] = i + 1;
        }
        return true;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.IntStream;

public class IsomorphicStringsStream {
    public static boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length()) return false;

        return IntStream.range(0, s.length())
            .allMatch(i -> s.indexOf(s.charAt(i)) == t.indexOf(t.charAt(i)));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Stream API single-line check `s.indexOf(...) == t.indexOf(...)` validates 1-to-1 mapping without dual HashMaps.

---

### Q8: Word Pattern (LeetCode #290)

#### 📄 Real-World Interview Problem Statement
> *"Given a `pattern` string and a string `s` of space-separated words, determine if `s` follows the exact same pattern. Following the pattern means a full 1-to-1 bijection between a letter in `pattern` and a non-empty word in `s`."*

#### 🛍️ Real-World Intuitive Story
Imagine a rhythmic dance code: `[A, B, B, A]`. If dancers perform `[clap, jump, jump, clap]`, it perfectly follows the pattern! But if dancers perform `[clap, jump, jump, run]`, it breaks the pattern because `A` cannot mean both `clap` and `run`.

#### 📥 Input & 📤 Output
- **Input**: `pattern = "abba"`, `s = "dog cat cat dog"` $\rightarrow$ **Output**: `true`
- **Input**: `pattern = "abba"`, `s = "dog cat cat fish"` $\rightarrow$ **Output**: `false`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"1-to-1 bijection between letter and word"*, *"pattern match"*.
- **Pattern**: **Index Range Match (`pattern.indexOf` vs `wordList.indexOf`)**.

#### 🛠️ Step-by-Step Strategy
1. Split `s` into words array. If length mismatch with pattern, return `false`.
2. Check that at every position `i`, `pattern.indexOf(pattern.charAt(i))` equals `wordList.indexOf(words[i])`.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashMap;
import java.util.Map;

public class WordPatternNormal {
    public static boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");
        if (pattern.length() != words.length) return false;

        Map<Character, String> charToWord = new HashMap<>();
        Map<String, Character> wordToChar = new HashMap<>();

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            String w = words[i];

            if (charToWord.containsKey(c) && !charToWord.get(c).equals(w)) return false;
            if (wordToChar.containsKey(w) && wordToChar.get(w) != c) return false;

            charToWord.put(c, w);
            wordToChar.put(w, c);
        }
        return true;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class WordPatternStream {
    public static boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");
        if (pattern.length() != words.length) return false;

        List<String> wordList = Arrays.asList(words);
        return IntStream.range(0, pattern.length())
            .allMatch(i -> pattern.indexOf(pattern.charAt(i)) == wordList.indexOf(words[i]));
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Comparing first occurrence indices between pattern characters and words completely eliminates 2 separate mapping HashMaps.

---

### Q9: Most Common Word (LeetCode #819)

#### 📄 Real-World Interview Problem Statement
> *"Given a paragraph text and a list of banned words, return the most frequent word that is not banned. The paragraph text contains punctuation marks which should be ignored, and words are case-insensitive. It is guaranteed there is at least one word that is not banned."*

#### 🛍️ Real-World Intuitive Story
Imagine analyzing customer review comments for a restaurant. Words like `"the"`, `"a"`, `"and"` are on a banned stop-word list. Among all remaining valid words (`"delicious"`, `"food"`, `"delicious"`), `"delicious"` appears most frequently (2 times), so it wins.

#### 📥 Input & 📤 Output
- **Input**: `paragraph = "Bob hit a ball, the hit BALL flew far after it was hit."`, `banned = ["hit"]`
- **Output**: `"ball"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"most frequent non-banned word"*, *"ignore punctuation and case"*.
- **Pattern**: **Sanitization + Filtering + Grouping Frequency Map + Max Entry**.

#### 🛠️ Step-by-Step Strategy
1. Convert text to lowercase and replace non-alphabet characters with spaces (`replaceAll("[^a-z]", " ")`).
2. Split into words stream and filter out banned words.
3. Count frequencies with `Collectors.groupingBy()` and find `max(comparingByValue())`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class MostCommonWordNormal {
    public static String mostCommonWord(String paragraph, String[] banned) {
        Set<String> bannedSet = new HashSet<>(Arrays.asList(banned));
        String sanitized = paragraph.toLowerCase().replaceAll("[^a-z]", " ");
        String[] words = sanitized.split("\\s+");

        Map<String, Integer> counts = new HashMap<>();
        for (String w : words) {
            if (!w.isEmpty() && !bannedSet.contains(w)) {
                counts.put(w, counts.getOrDefault(w, 0) + 1);
            }
        }

        String result = "";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                result = entry.getKey();
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

public class MostCommonWordStream {
    public static String mostCommonWord(String paragraph, String[] banned) {
        Set<String> bannedSet = new HashSet<>(Arrays.asList(banned));

        return Arrays.stream(paragraph.toLowerCase().replaceAll("[^a-z]", " ").split("\\s+"))
            .filter(word -> !word.isEmpty() && !bannedSet.contains(word))
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("");
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Stream API combines text cleaning, regex splitting, filtering, grouping, and max extraction into a single fluid pipeline.

---

### Q10: Ransom Note (LeetCode #383)

#### 📄 Real-World Interview Problem Statement
> *"Given two strings `ransomNote` and `magazine`, return `true` if `ransomNote` can be constructed using the letters from `magazine`, and `false` otherwise. Each letter in `magazine` can only be used once in `ransomNote`."*

#### 🛍️ Real-World Intuitive Story
Imagine clipping letters out of a magazine to write a secret message. If your note needs 2 `'a'`s and 1 `'b'`, but the magazine page only has 1 `'a'` and 1 `'b'`, you cannot finish writing the note!

#### 📥 Input & 📤 Output
- **Input**: `ransomNote = "a"`, `magazine = "b"` $\rightarrow$ **Output**: `false`
- **Input**: `ransomNote = "aa"`, `magazine = "aab"` $\rightarrow$ **Output**: `true`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"constructed using letters from source"*, *"each letter used once"*.
- **Pattern**: **Frequency Map Bound Validation (`allMatch`)**.

#### 🛠️ Step-by-Step Strategy
1. Count character frequencies in `magazine`.
2. Count character frequencies in `ransomNote`.
3. Check that for every character in `ransomNote`, its required frequency is $\le$ available frequency in `magazine`.

#### ☕ Solution 1: Normal Java
```java
public class RansomNoteNormal {
    public static boolean canConstruct(String ransomNote, String magazine) {
        int[] counts = new int[26];
        for (char c : magazine.toCharArray()) {
            counts[c - 'a']++;
        }
        for (char c : ransomNote.toCharArray()) {
            if (--counts[c - 'a'] < 0) {
                return false;
            }
        }
        return true;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RansomNoteStream {
    public static boolean canConstruct(String ransomNote, String magazine) {
        Map<Character, Long> magFreq = magazine.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<Character, Long> ransomFreq = ransomNote.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return ransomFreq.entrySet().stream()
            .allMatch(e -> magFreq.getOrDefault(e.getKey(), 0L) >= e.getValue());
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Normal Java primitive `int[26]` array is optimal for performance; Stream API excels in declarative clarity for dynamic Unicode characters.

---

### Q11: Reorganize String (LeetCode #767)

#### 📄 Real-World Interview Problem Statement
> *"Given a string `s`, rearrange the characters of `s` so that no two adjacent characters are identical. Return any valid rearranged string. If it is impossible to rearrange `s` such that no adjacent characters match, return an empty string `""`."*

#### 🛍️ Real-World Intuitive Story
Imagine seating guests at a dinner table so no two people with the same shirt color sit next to each other. If 4 people wear red shirts and only 1 person wears blue, it's impossible to seat them without 2 red shirts sitting together!

#### 📥 Input & 📤 Output
- **Input**: `s = "aab"` $\rightarrow$ **Output**: `"aba"`
- **Input**: `s = "aaab"` $\rightarrow$ **Output**: `""`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"no two adjacent elements identical"*, *"rearrange string"*.
- **Pattern**: **Max Frequency Threshold Check + Index Interleaving**.

#### 🛠️ Step-by-Step Strategy
1. Count character frequencies. If any char frequency $> (s.length() + 1) / 2$, return `""`.
2. Sort characters by frequency descending.
3. Interleave placing characters into even indices `[0, 2, 4...]` first, then odd indices `[1, 3, 5...]`.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class ReorganizeStringNormal {
    public static String reorganizeString(String s) {
        int[] counts = new int[26];
        for (char c : s.toCharArray()) counts[c - 'a']++;

        PriorityQueue<char[]> maxHeap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (int i = 0; i < 26; i++) {
            if (counts[i] > 0) {
                if (counts[i] > (s.length() + 1) / 2) return "";
                maxHeap.offer(new char[]{(char) ('a' + i), counts[i]});
            }
        }

        StringBuilder sb = new StringBuilder();
        while (maxHeap.size() >= 2) {
            char[] first = maxHeap.poll();
            char[] second = maxHeap.poll();

            sb.append(first[0]).append(second[0]);
            if (--first[1] > 0) maxHeap.offer(first);
            if (--second[1] > 0) maxHeap.offer(second);
        }

        if (!maxHeap.isEmpty()) {
            sb.append(maxHeap.poll()[0]);
        }
        return sb.toString();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReorganizeStringStream {
    public static String reorganizeString(String s) {
        Map<Character, Long> freqMap = s.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long maxFreq = freqMap.values().stream().max(Long::compare).orElse(0L);
        if (maxFreq > (s.length() + 1) / 2) return "";

        List<Character> sortedChars = freqMap.entrySet().stream()
            .sorted(Map.Entry.<Character, Long>comparingByValue().reversed())
            .flatMap(e -> Collections.nCopies(e.getValue().intValue(), e.getKey()).stream())
            .collect(Collectors.toList());

        char[] result = new char[s.length()];
        int idx = 0;
        for (char c : sortedChars) {
            result[idx] = c;
            idx += 2;
            if (idx >= s.length()) idx = 1;
        }
        return new String(result);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- `Collections.nCopies(freq, char).stream()` flattens character lists by frequency into an ordered stream ready for index interleaving.

---

### Q12: Check if All Characters Have Equal Occurrences (LeetCode #1941)

#### 📄 Real-World Interview Problem Statement
> *"Given a string `s`, return `true` if all characters that appear in `s` have the exact same number of occurrences, or `false` otherwise."*

#### 🛍️ Real-World Intuitive Story
Imagine a fair sports tournament where team A played 2 matches, team B played 2 matches, and team C played 2 matches. Every team played an equal number of matches (2).

#### 📥 Input & 📤 Output
- **Input**: `s = "abacbc"` $\rightarrow$ **Output**: `true` (each char 'a', 'b', 'c' appears 2 times)
- **Input**: `s = "aaabb"` $\rightarrow$ **Output**: `false`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"all characters have equal occurrences"*, *"uniform frequencies"*.
- **Pattern**: **Grouping Map Values Distinct Count (`distinct().count() == 1`)**.

#### 🛠️ Step-by-Step Strategy
1. Count character frequencies into a Map.
2. Stream map `values()`, deduplicate with `distinct()`, and verify count equals `1`.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashMap;
import java.util.Map;

public class EqualOccurrencesNormal {
    public static boolean areOccurrencesEqual(String s) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : s.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        int targetFreq = counts.get(s.charAt(0));
        for (int freq : counts.values()) {
            if (freq != targetFreq) return false;
        }
        return true;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.function.Function;
import java.util.stream.Collectors;

public class EqualOccurrencesStream {
    public static boolean areOccurrencesEqual(String s) {
        return s.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .values()
            .stream()
            .distinct()
            .count() == 1;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Ultra-clean 1-liner combining `groupingBy()`, `values()`, `distinct()`, and `count() == 1`.

---

### Q13: Determine if Two Strings Are Close (LeetCode #1657)

#### 📄 Real-World Interview Problem Statement
> *"Two strings `word1` and `word2` are considered close if you can attain one from another using two operations: (1) Swap any two existing characters, or (2) Transform all occurrences of one existing character into another existing character. Given two strings, return `true` if `word1` and `word2` are close, and `false` otherwise."*

#### 🛍️ Real-World Intuitive Story
Think of two cipher codes: Code 1 uses letters `{A, B, C}` with frequencies `[3, 2, 1]`. Code 2 uses the exact same letters `{A, B, C}` with frequencies `[1, 3, 2]`. Since both use the exact same unique letter keys and the sorted frequency counts `[1, 2, 3]` match, they are close!

#### 📥 Input & 📤 Output
- **Input**: `word1 = "cabbba"`, `word2 = "abbccc"` $\rightarrow$ **Output**: `true`
- **Input**: `word1 = "a"`, `word2 = "aa"` $\rightarrow$ **Output**: `false`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"swap characters or transform occurrences"*, *"close strings"*.
- **Pattern**: **Same KeySet (`keySet().equals()`) AND Same Sorted Values (`values().stream().sorted()`)**.

#### 🛠️ Step-by-Step Strategy
1. Verify lengths match.
2. Build frequency map for both strings.
3. Verify unique keys match (`map1.keySet().equals(map2.keySet())`).
4. Verify sorted frequency counts match.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class CloseStringsNormal {
    public static boolean closeStrings(String s1, String s2) {
        if (s1.length() != s2.length()) return false;

        int[] c1 = new int[26];
        int[] c2 = new int[26];
        for (char c : s1.toCharArray()) c1[c - 'a']++;
        for (char c : s2.toCharArray()) c2[c - 'a']++;

        for (int i = 0; i < 26; i++) {
            if ((c1[i] == 0 && c2[i] > 0) || (c1[i] > 0 && c2[i] == 0)) return false;
        }

        Arrays.sort(c1);
        Arrays.sort(c2);
        return Arrays.equals(c1, c2);
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CloseStringsStream {
    public static boolean closeStrings(String s1, String s2) {
        if (s1.length() != s2.length()) return false;

        Map<Character, Long> map1 = s1.chars().mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Character, Long> map2 = s2.chars().mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (!map1.keySet().equals(map2.keySet())) return false;

        List<Long> freq1 = map1.values().stream().sorted().collect(Collectors.toList());
        List<Long> freq2 = map2.values().stream().sorted().collect(Collectors.toList());

        return freq1.equals(freq2);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- `keySet().equals()` and `sorted()` value list comparisons express the mathematical problem rules directly.

---

### Q14: Longest Substring Without Repeating Characters (LeetCode #3)

#### 📄 Real-World Interview Problem Statement
> *"Given a string `s`, find the length of the longest contiguous substring without repeating characters."*

#### 🛍️ Real-World Intuitive Story
Imagine walking down a street reading billboard letters `"abcabcbb"`. You write letters down on your notepad. As soon as you see a duplicate letter `'a'`, you slide your starting bookmark past the previous `'a'` so your notepad only holds unique letters (`"abc"` length 3).

#### 📥 Input & 📤 Output
- **Input**: `s = "abcabcbb"` $\rightarrow$ **Output**: `3` (`"abc"`)
- **Input**: `s = "bbbbb"` $\rightarrow$ **Output**: `1` (`"b"`)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"longest substring without repeating characters"*, *"unique characters window"*.
- **Pattern**: **Sliding Window + Last-Seen Index Map**.

#### 🛠️ Step-by-Step Strategy
1. Maintain two pointers: `left` and `right`.
2. Store last seen index of each character in a HashMap.
3. Whenever duplicate character is found, jump `left` pointer to `Math.max(left, lastSeen + 1)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashMap;
import java.util.Map;

public class LongestSubstringNormal {
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int maxLen = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (lastSeen.containsKey(c)) {
                left = Math.max(left, lastSeen.get(c) + 1);
            }
            lastSeen.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class LongestSubstringStream {
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        AtomicInteger left = new AtomicInteger(0);

        return IntStream.range(0, s.length())
            .map(right -> {
                char c = s.charAt(right);
                if (lastSeen.containsKey(c)) {
                    left.set(Math.max(left.get(), lastSeen.get(c) + 1));
                }
                lastSeen.put(c, right);
                return right - left.get() + 1;
            })
            .max()
            .orElse(0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Imperative sliding window loop is much cleaner and avoids side-effect state in stream `AtomicInteger`.

---

### Q15: Custom Sort String (LeetCode #791)

#### 📄 Real-World Interview Problem Statement
> *"You are given two strings `order` and `s`. All characters of `order` are unique and were sorted in some custom order. Permute the characters of `s` so that they match the order defined in `order`. If a character in `s` is not present in `order`, it can be placed anywhere at the end."*

#### 🛍️ Real-World Intuitive Story
Imagine organizing a priority line at an airport defined by custom order `"cba"`. Passengers carrying priority ticket `'c'` line up first, followed by `'b'`, followed by `'a'`. Any passenger carrying ticket `'d'` (not in priority list) stands at the back of the line.

#### 📥 Input & 📤 Output
- **Input**: `order = "cba"`, `s = "abcd"` $\rightarrow$ **Output**: `"cbad"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"custom sort order"*, *"permute string according to priority list"*.
- **Pattern**: **Stream Sorted via Custom Index Comparator (`order::indexOf`)**.

#### 🛠️ Step-by-Step Strategy
1. Box characters of string `s`.
2. Sort using custom comparator mapped to `order.indexOf(char)`.
3. Join sorted characters back into a string.

#### ☕ Solution 1: Normal Java
```java
import java.util.Arrays;

public class CustomSortNormal {
    public static String customSortString(String order, String s) {
        Character[] chars = new Character[s.length()];
        for (int i = 0; i < s.length(); i++) {
            chars[i] = s.charAt(i);
        }

        Arrays.sort(chars, (a, b) -> Integer.compare(order.indexOf(a), order.indexOf(b)));

        StringBuilder sb = new StringBuilder();
        for (char c : chars) sb.append(c);
        return sb.toString();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Comparator;
import java.util.stream.Collectors;

public class CustomSortStream {
    public static String customSortString(String order, String s) {
        return s.chars()
            .mapToObj(c -> (char) c)
            .sorted(Comparator.comparingInt(order::indexOf))
            .map(String::valueOf)
            .collect(Collectors.joining());
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Stream API single fluid pipeline maps, sorts via method reference `order::indexOf`, and joins.

---

### Q16: Count Vowel Substrings of a String (LeetCode #2062)

#### 📄 Real-World Interview Problem Statement
> *"Given a string `word` of lowercase English letters, return the number of vowel substrings in `word`. A vowel substring is a contiguous substring that consists ONLY of vowels (`'a'`, `'e'`, `'i'`, `'o'`, `'u'`) AND contains ALL 5 vowels at least once."*

#### 🛍️ Real-World Intuitive Story
Think of collecting a complete 5-card suit of vowel stickers `[A, E, I, O, U]`. A valid sticker album page must contain only vowel stickers, and must contain at least one sticker of every single letter in the set.

#### 📥 Input & 📤 Output
- **Input**: `word = "aeiouu"` $\rightarrow$ **Output**: `2` (substrings `"aeiou"` and `"aeiouu"`)
- **Input**: `word = "unicornariel"` $\rightarrow$ **Output**: `0`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"substring consists ONLY of vowels AND contains ALL 5 vowels"*.
- **Pattern**: **Substring Boundary Filtering (`vowels.contains` && `distinct().count() == 5`)**.

#### 🛠️ Step-by-Step Strategy
1. Iterate over all substring boundaries $(i, j)$.
2. Filter substrings that consist exclusively of vowels.
3. Count substrings where distinct vowel count equals 5.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashSet;
import java.util.Set;

public class VowelSubstringsNormal {
    public static int countVowelSubstrings(String word) {
        int count = 0;
        Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u');

        for (int i = 0; i < word.length(); i++) {
            Set<Character> seen = new HashSet<>();
            for (int j = i; j < word.length(); j++) {
                char c = word.charAt(j);
                if (!vowels.contains(c)) break;
                seen.add(c);
                if (seen.size() == 5) count++;
            }
        }
        return count;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Set;
import java.util.stream.IntStream;

public class VowelSubstringsStream {
    public static int countVowelSubstrings(String word) {
        Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u');

        return (int) IntStream.range(0, word.length())
            .flatMap(i -> IntStream.range(i + 5, word.length() + 1)
                .filter(j -> {
                    String sub = word.substring(i, j);
                    return sub.chars().mapToObj(c -> (char) c).allMatch(vowels::contains) &&
                           sub.chars().mapToObj(c -> (char) c).distinct().count() == 5;
                }))
            .count();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Traditional nested loop is significantly faster due to early breaking on non-vowel characters.

---

### Q17: Decode String Pattern Counts (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given an encoded string `s` following the format `k[encoded_string]`, return its expanded decoded string. The encoding rule is that the `encoded_string` inside square brackets is repeated exactly `k` times. You may assume the input string is always valid and contains no extra spaces."*

#### 🛍️ Real-World Intuitive Story
Think of reading a music sheet notation: `3[a]2[bc]`. The instruction means: play note `'a'` 3 times (`"aaa"`), then play note sequence `'bc'` 2 times (`"bcbc"`), producing `"aaabcbc"`.

#### 📥 Input & 📤 Output
- **Input**: `s = "3[a]2[bc]"` $\rightarrow$ **Output**: `"aaabcbc"`
- **Input**: `s = "3[a2[c]]"` $\rightarrow$ **Output**: `"accaccacc"`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"pattern k[string]"*, *"repeat inner string k times"*.
- **Pattern**: **Regex Matching + `String.repeat(k)`**.

#### 🛠️ Step-by-Step Strategy
1. Match pattern regex `(\d+)\[([a-zA-Z]+)\]`.
2. Parse multiplier `k` and token string.
3. Replace pattern match with `token.repeat(k)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.Stack;

public class DecodeStringNormal {
    public static String decodeString(String s) {
        Stack<Integer> countStack = new Stack<>();
        Stack<StringBuilder> stringStack = new Stack<>();
        StringBuilder current = new StringBuilder();
        int k = 0;

        for (char ch : s.toCharArray()) {
            if (Character.isDigit(ch)) {
                k = k * 10 + (ch - '0');
            } else if (ch == '[') {
                countStack.push(k);
                stringStack.push(current);
                current = new StringBuilder();
                k = 0;
            } else if (ch == ']') {
                StringBuilder decoded = stringStack.pop();
                int count = countStack.pop();
                for (int i = 0; i < count; i++) {
                    decoded.append(current);
                }
                current = decoded;
            } else {
                current.append(ch);
            }
        }
        return current.toString();
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecodeStringStream {
    public static String decodeString(String s) {
        Pattern pattern = Pattern.compile("(\\d+)\\[([a-zA-Z]+)\\]");
        while (s.contains("[")) {
            Matcher matcher = pattern.matcher(s);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                int count = Integer.parseInt(matcher.group(1));
                String str = matcher.group(2);
                matcher.appendReplacement(sb, str.repeat(count));
            }
            matcher.appendTail(sb);
            s = sb.toString();
        }
        return s;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Regex matching combined with `String.repeat()` provides a concise alternative to stack parsing.

---

### Q18: Check if Sentence Is Pangram (LeetCode #1832)

#### 📄 Real-World Interview Problem Statement
> *"A pangram is a sentence where every letter of the English alphabet appears at least once. Given a string `sentence` containing only lowercase English letters, return `true` if `sentence` is a pangram, or `false` otherwise."*

#### 🛍️ Real-World Intuitive Story
Imagine a stamp collector checking an alphabet collection book with 26 empty slots (`'a'` to `'z'`). As you read a sentence, you place a stamp in the matching slot. If all 26 slots are filled at the end, the sentence is a pangram!

#### 📥 Input & 📤 Output
- **Input**: `sentence = "thequickbrownfoxjumpsoverthelazydog"` $\rightarrow$ **Output**: `true`
- **Input**: `sentence = "leetcode"` $\rightarrow$ **Output**: `false`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"sentence contains every letter of alphabet"*, *"all 26 letters present"*.
- **Pattern**: **Distinct Character Count (`distinct().count() == 26`)**.

#### 🛠️ Step-by-Step Strategy
1. Filter letter characters.
2. Count distinct characters using `distinct()`.
3. Verify if count equals `26`.

#### ☕ Solution 1: Normal Java
```java
import java.util.HashSet;
import java.util.Set;

public class PangramNormal {
    public static boolean checkIfPangram(String sentence) {
        Set<Character> seen = new HashSet<>();
        for (char c : sentence.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                seen.add(c);
            }
        }
        return seen.size() == 26;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
public class PangramStream {
    public static boolean checkIfPangram(String sentence) {
        return sentence.chars()
            .filter(Character::isLetter)
            .distinct()
            .count() == 26;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- Outstanding 1-line stream solution that is clean, readable, and self-documenting.
