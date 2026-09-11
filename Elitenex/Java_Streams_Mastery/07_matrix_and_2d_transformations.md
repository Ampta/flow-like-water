# 🟩 Category 7: Matrix & 2D Transformations

This guide provides exhaustive, production-grade solutions for **15 Matrix & 2D Array Transformations Problems** (Q119 – Q133). Every problem is presented with an authentic interview scenario, real-world intuitive story, step-by-step strategy, and dual implementation (Normal Java vs. Java Streams).

---

## 📑 Table of Contents
- [Q119: Transpose Matrix (LeetCode #867)](#q119-transpose-matrix-leetcode-867)
- [Q120: Rotate Image 90 Degrees (LeetCode #48)](#q120-rotate-image-90-degrees-leetcode-48)
- [Q121: Spiral Matrix Traversal (LeetCode #54)](#q121-spiral-matrix-traversal-leetcode-54)
- [Q122: Flatten 2D Matrix to 1D Array (Interview Classic)](#q122-flatten-2d-matrix-to-1d-array-interview-classic)
- [Q123: Row-Wise Sum of Matrix (Interview Classic)](#q123-row-wise-sum-of-matrix-interview-classic)
- [Q124: Column-Wise Sum of Matrix (Interview Classic)](#q124-column-wise-sum-of-matrix-interview-classic)
- [Q125: Find Matrix Diagonal Sum (LeetCode #1572)](#q125-find-matrix-diagonal-sum-leetcode-1572)
- [Q126: Search in a 2D Matrix II (LeetCode #240)](#q126-search-in-a-2d-matrix-ii-leetcode-240)
- [Q127: Matrix Multiplication (Interview Classic)](#q127-matrix-multiplication-interview-classic)
- [Q128: Set Matrix Zeroes (LeetCode #73)](#q128-set-matrix-zeroes-leetcode-73)
- [Q129: Reshape the Matrix (LeetCode #566)](#q129-reshape-the-matrix-leetcode-566)
- [Q130: Count Negative Numbers in a Sorted Matrix (LeetCode #1351)](#q130-count-negative-numbers-in-a-sorted-matrix-leetcode-1351)
- [Q131: Lucky Numbers in a Matrix (LeetCode #1380)](#q131-lucky-numbers-in-a-matrix-leetcode-1380)
- [Q132: Maximum Diagonal Sum in Square Matrix (Interview Classic)](#q132-maximum-diagonal-sum-in-square-matrix-interview-classic)
- [Q133: Convert 1D Array into 2D Array with Given Dimensions (LeetCode #2022)](#q133-convert-1d-array-into-2d-array-with-given-dimensions-leetcode-2022)

---

### Q119: Transpose Matrix (LeetCode #867)

#### 📄 Real-World Interview Problem Statement
> *"Given a 2D integer matrix `matrix`, return the transpose of `matrix` where the row and column indices are swapped (`result[j][i] = matrix[i][j]`)."*

#### 🛍️ Real-World Intuitive Story
Flipping a spreadsheet table along its main diagonal so rows become columns and columns become rows.

#### 📥 Input & 📤 Output
- **Input**: `matrix = [[1,2,3],[4,5,6]]` $\rightarrow$ **Output**: `[[1,4],[2,5],[3,6]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"transpose matrix swap rows and columns"*.
- **Pattern**: **IntStream range for columns mapping to rows**.

#### 🛠️ Step-by-Step Strategy
1. Determine dimensions $m \times n$.
2. Generate column index stream `0` to $n - 1$.
3. For each column $j$, map row values $i$ from $0$ to $m - 1$.

#### ☕ Solution 1: Normal Java
```java
public class TransposeMatrixNormal {
    public static int[][] transpose(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] result = new int[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.IntStream;

public class TransposeMatrixStream {
    public static int[][] transpose(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        return IntStream.range(0, n)
            .mapToObj(j -> IntStream.range(0, m)
                .map(i -> matrix[i][j])
                .toArray())
            .toArray(int[][]::new);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Nested `for` loop populating transposed matrix.
- **Stream API**: `IntStream.range()` outer and inner mapping creates transposed 2D array functionally.

---

### Q120: Rotate Image 90 Degrees (LeetCode #48)

#### 📄 Real-World Interview Problem Statement
> *"You are given an `n x n` 2D matrix representing an image. Rotate the image by 90 degrees clockwise in-place."*

#### 🛍️ Real-World Intuitive Story
Taking a photo frame and turning it 90 degrees to the right.

#### 📥 Input & 📤 Output
- **Input**: `matrix = [[1,2,3],[4,5,6],[7,8,9]]` $\rightarrow$ **Output**: `[[7,4,1],[8,5,2],[9,6,3]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"rotate image 90 degrees clockwise"*.
- **Pattern**: **Transpose Matrix + Reverse Rows**.

#### 🛠️ Step-by-Step Strategy
1. Transpose matrix (`swap matrix[i][j]` with `matrix[j][i]`).
2. Reverse each row left to right.

#### ☕ Solution 1: Normal Java
```java
public class RotateImageNormal {
    public static void rotate(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][n - 1 - j];
                matrix[i][n - 1 - j] = temp;
            }
        }
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.IntStream;

public class RotateImageStream {
    public static int[][] rotate(int[][] matrix) {
        int n = matrix.length;
        return IntStream.range(0, n)
            .mapToObj(j -> IntStream.range(0, n)
                .map(i -> matrix[n - 1 - i][j])
                .toArray())
            .toArray(int[][]::new);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: In-place transpose followed by horizontal row reversal.
- **Stream API**: Functional index transformation `matrix[n - 1 - i][j]`.

---

### Q121: Spiral Matrix Traversal (LeetCode #54)

#### 📄 Real-World Interview Problem Statement
> *"Given an `m x n` matrix, return all elements of the matrix in spiral order (clock-wise from outside to center)."*

#### 🛍️ Real-World Intuitive Story
Walking along the outer edge of a rectangular field clockwise, then stepping inward to repeat until reaching the center point.

#### 📥 Input & 📤 Output
- **Input**: `matrix = [[1,2,3],[4,5,6],[7,8,9]]` $\rightarrow$ **Output**: `[1,2,3,6,9,8,7,4,5]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"spiral order matrix traversal"*.
- **Pattern**: **Boundary tracking (top, bottom, left, right)**.

#### 🛠️ Step-by-Step Strategy
1. Maintain 4 pointers: `top`, `bottom`, `left`, `right`.
2. Traverse right along `top`, down along `right`, left along `bottom`, up along `left`.
3. Shrink boundaries until all elements are collected.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class SpiralMatrixNormal {
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix == null || matrix.length == 0) return result;
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {
            for (int j = left; j <= right; j++) result.add(matrix[top][j]);
            top++;
            for (int i = top; i <= bottom; i++) result.add(matrix[i][right]);
            right--;
            if (top <= bottom) {
                for (int j = right; j >= left; j--) result.add(matrix[bottom][j]);
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) result.add(matrix[i][left]);
                left++;
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;

public class SpiralMatrixStream {
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix == null || matrix.length == 0) return result;
        int top = 0, bottom = matrix.length - 1, left = 0, right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {
            final int t = top, b = bottom, l = left, r = right;
            java.util.stream.IntStream.rangeClosed(l, r).forEach(j -> result.add(matrix[t][j]));
            java.util.stream.IntStream.rangeClosed(t + 1, b).forEach(i -> result.add(matrix[i][r]));
            if (t < b) {
                java.util.stream.IntStream.iterate(r - 1, j -> j >= l, j -> j - 1).forEach(j -> result.add(matrix[b][j]));
            }
            if (l < r) {
                java.util.stream.IntStream.iterate(b - 1, i -> i > t, i -> i - 1).forEach(i -> result.add(matrix[i][l]));
            }
            top++; bottom--; left++; right--;
        }
        return result;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard pointer-boundary loops.
- **Stream API**: `IntStream.rangeClosed()` and `IntStream.iterate()` per side boundary.

---

### Q122: Flatten 2D Matrix to 1D Array (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a 2D integer matrix `int[][] matrix`, collapse all row arrays into a single 1D flat integer array."*

#### 🛍️ Real-World Intuitive Story
Unrolling a grid carpet into one long straight runner rug.

#### 📥 Input & 📤 Output
- **Input**: `[[1, 2], [3, 4], [5, 6]]` $\rightarrow$ **Output**: `[1, 2, 3, 4, 5, 6]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"flatten 2D matrix"*.
- **Pattern**: **Arrays.stream(matrix).flatMapToInt(Arrays::stream)**.

#### 🛠️ Step-by-Step Strategy
1. Stream rows of 2D matrix.
2. FlatMap rows into primitive `IntStream`.
3. Collect using `.toArray()`.

#### ☕ Solution 1: Normal Java
```java
public class FlattenMatrixNormal {
    public static int[] flatten(int[][] matrix) {
        int totalElements = 0;
        for (int[] row : matrix) totalElements += row.length;
        int[] result = new int[totalElements];

        int idx = 0;
        for (int[] row : matrix) {
            for (int val : row) {
                result[idx++] = val;
            }
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class FlattenMatrixStream {
    public static int[] flatten(int[][] matrix) {
        return Arrays.stream(matrix)
            .flatMapToInt(Arrays::stream)
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Double loop element copy.
- **Stream API**: `flatMapToInt(Arrays::stream)` flattens 2D matrices in 1 line.

---

### Q123: Row-Wise Sum of Matrix (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a 2D integer matrix `int[][] matrix`, calculate the sum of elements for each row and return a 1D array of row sums."*

#### 🛍️ Real-World Intuitive Story
Calculating total monthly expenses for each row in a spreadsheet budget table.

#### 📥 Input & 📤 Output
- **Input**: `[[1, 2, 3], [4, 5, 6]]` $\rightarrow$ **Output**: `[6, 15]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"row-wise sum matrix"*.
- **Pattern**: **Arrays.stream(matrix).mapToInt(row -> Arrays.stream(row).sum())**.

#### 🛠️ Step-by-Step Strategy
1. Stream rows of 2D matrix.
2. Map each row array to `Arrays.stream(row).sum()`.

#### ☕ Solution 1: Normal Java
```java
public class RowSumNormal {
    public static int[] rowSums(int[][] matrix) {
        int[] result = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int sum = 0;
            for (int val : matrix[i]) sum += val;
            result[i] = sum;
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class RowSumStream {
    public static int[] rowSums(int[][] matrix) {
        return Arrays.stream(matrix)
            .mapToInt(row -> Arrays.stream(row).sum())
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Outer row loop with inner sum loop.
- **Stream API**: `mapToInt(row -> Arrays.stream(row).sum())`.

---

### Q124: Column-Wise Sum of Matrix (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a 2D integer matrix `int[][] matrix`, calculate the sum of elements for each column and return a 1D array of column sums."*

#### 🛍️ Real-World Intuitive Story
Adding up vertical column totals at the bottom of a financial ledger table.

#### 📥 Input & 📤 Output
- **Input**: `[[1, 2], [3, 4], [5, 6]]` $\rightarrow$ **Output**: `[9, 12]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"column-wise sum matrix"*.
- **Pattern**: **IntStream.range(0, cols).map(j -> row sum at j)**.

#### 🛠️ Step-by-Step Strategy
1. Stream column indices `0` to `cols - 1`.
2. For index `j`, sum `matrix[i][j]` over all rows $i$.

#### ☕ Solution 1: Normal Java
```java
public class ColumnSumNormal {
    public static int[] columnSums(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        int[] result = new int[cols];
        for (int j = 0; j < cols; j++) {
            int sum = 0;
            for (int i = 0; i < rows; i++) {
                sum += matrix[i][j];
            }
            result[j] = sum;
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class ColumnSumStream {
    public static int[] columnSums(int[][] matrix) {
        int cols = matrix[0].length;
        return IntStream.range(0, cols)
            .map(j -> Arrays.stream(matrix).mapToInt(row -> row[j]).sum())
            .toArray();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Column outer loop summing down rows.
- **Stream API**: `IntStream.range()` mapping column indices to row sum extractions.

---

### Q125: Find Matrix Diagonal Sum (LeetCode #1572)

#### 📄 Real-World Interview Problem Statement
> *"Given a square matrix `mat`, return the sum of the matrix diagonals. Include elements from the primary diagonal and secondary diagonal, excluding the center element if counted twice."*

#### 🛍️ Real-World Intuitive Story
Drawing an 'X' across a square grid and summing all numbers touched by the lines.

#### 📥 Input & 📤 Output
- **Input**: `mat = [[1,2,3],[4,5,6],[7,8,9]]` $\rightarrow$ **Output**: `25` ($1+5+9 + 3+7 = 25$, center 5 not duplicated)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"matrix diagonal sum"*.
- **Pattern**: **IntStream.range(0, n).map(i -> mat[i][i] + mat[i][n-1-i])**.

#### 🛠️ Step-by-Step Strategy
1. Stream row index `i` from `0` to `n - 1`.
2. Sum primary `mat[i][i]` and secondary `mat[i][n - 1 - i]`.
3. Subtract center element if $n$ is odd.

#### ☕ Solution 1: Normal Java
```java
public class MatrixDiagonalSumNormal {
    public static int diagonalSum(int[][] mat) {
        int n = mat.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += mat[i][i];
            sum += mat[i][n - 1 - i];
        }
        if (n % 2 != 0) {
            sum -= mat[n / 2][n / 2];
        }
        return sum;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.IntStream;

public class MatrixDiagonalSumStream {
    public static int diagonalSum(int[][] mat) {
        int n = mat.length;
        int sum = IntStream.range(0, n)
            .map(i -> mat[i][i] + mat[i][n - 1 - i])
            .sum();
        return n % 2 != 0 ? sum - mat[n / 2][n / 2] : sum;
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Single loop summing diagonal indices with odd center subtraction.
- **Stream API**: `IntStream.range().map().sum()` clean 1-pass expression.

---

### Q126: Search in a 2D Matrix II (LeetCode #240)

#### 📄 Real-World Interview Problem Statement
> *"Write an efficient algorithm that searches for a `target` value in an `m x n` integer matrix where rows and columns are sorted in ascending order."*

#### 🛍️ Real-World Intuitive Story
Standing at the top-right corner of a sorted grid. If current number is larger than target, move left; if smaller, move down.

#### 📥 Input & 📤 Output
- **Input**: `matrix = [[1,4,7],[2,5,8],[3,6,9]]`, `target = 5` $\rightarrow$ **Output**: `true`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"search sorted 2D matrix"*.
- **Pattern**: **Top-right stair search / Stream row binary search**.

#### 🛠️ Step-by-Step Strategy
1. Start search at top-right corner `(0, n - 1)`.
2. Move left if `mat[row][col] > target`, move down if `mat[row][col] < target`.

#### ☕ Solution 1: Normal Java
```java
public class SearchMatrixIINormal {
    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0) return false;
        int row = 0, col = matrix[0].length - 1;
        while (row < matrix.length && col >= 0) {
            if (matrix[row][col] == target) return true;
            else if (matrix[row][col] > target) col--;
            else row++;
        }
        return false;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class SearchMatrixIIStream {
    public static boolean searchMatrix(int[][] matrix, int target) {
        return Arrays.stream(matrix)
            .anyMatch(row -> Arrays.binarySearch(row, target) >= 0);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Top-right corner pointer algorithm running in $O(M + N)$ time.
- **Stream API**: `anyMatch` with `Arrays.binarySearch(row, target)` running in $O(M \log N)$ time.

---

### Q127: Matrix Multiplication (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given two matrices `int[][] A` ($m \times n$) and `int[][] B` ($n \times p$), compute their matrix product matrix `C` ($m \times p$)."*

#### 🛍️ Real-World Intuitive Story
Multiplying row vectors of A by column vectors of B to calculate dot product entries of resulting matrix C.

#### 📥 Input & 📤 Output
- **Input**: `A = [[1, 2], [3, 4]]`, `B = [[2, 0], [1, 2]]` $\rightarrow$ **Output**: `[[4, 4], [10, 8]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"matrix multiplication dot product"*.
- **Pattern**: **Triple nested loop / IntStream range mapping**.

#### 🛠️ Step-by-Step Strategy
1. Determine dimensions $m, n, p$.
2. For each cell `(i, k)`, sum `A[i][j] * B[j][k]` for $j$ from $0$ to $n - 1$.

#### ☕ Solution 1: Normal Java
```java
public class MatrixMultiplicationNormal {
    public static int[][] multiply(int[][] A, int[][] B) {
        int m = A.length, n = A[0].length, p = B[0].length;
        int[][] C = new int[m][p];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < p; k++) {
                    C[i][k] += A[i][j] * B[j][k];
                }
            }
        }
        return C;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.IntStream;

public class MatrixMultiplicationStream {
    public static int[][] multiply(int[][] A, int[][] B) {
        int m = A.length, n = A[0].length, p = B[0].length;
        return IntStream.range(0, m)
            .mapToObj(i -> IntStream.range(0, p)
                .map(k -> IntStream.range(0, n)
                    .map(j -> A[i][j] * B[j][k])
                    .sum())
                .toArray())
            .toArray(int[][]::new);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Triple nested `for` loop matrix dot-product accumulator.
- **Stream API**: Nested `IntStream.range()` computing cell sums functionally.

---

### Q128: Set Matrix Zeroes (LeetCode #73)

#### 📄 Real-World Interview Problem Statement
> *"Given an `m x n` integer matrix, if an element is `0`, set its entire row and column to `0`."*

#### 🛍️ Real-World Intuitive Story
If a single room in a building tests positive for contamination, shut down the entire floor (row) and entire elevator shaft line (column).

#### 📥 Input & 📤 Output
- **Input**: `[[1,1,1],[1,0,1],[1,1,1]]` $\rightarrow$ **Output**: `[[1,0,1],[0,0,0],[1,0,1]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"set matrix zeroes row column"*.
- **Pattern**: **Row/Col zero sets tracking + Stream update**.

#### 🛠️ Step-by-Step Strategy
1. Identify all row indices and column indices that contain `0`.
2. Iterate through matrix and set `matrix[i][j] = 0` if row $i$ or col $j$ was flagged.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class SetMatrixZeroesNormal {
    public static void setZeroes(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        Set<Integer> zeroRows = new HashSet<>();
        Set<Integer> zeroCols = new HashSet<>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    zeroRows.add(i);
                    zeroCols.add(j);
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (zeroRows.contains(i) || zeroCols.contains(j)) {
                    matrix[i][j] = 0;
                }
            }
        }
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SetMatrixZeroesStream {
    public static void setZeroes(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        Set<Integer> zeroRows = IntStream.range(0, m)
            .filter(i -> IntStream.range(0, n).anyMatch(j -> matrix[i][j] == 0))
            .boxed().collect(Collectors.toSet());

        Set<Integer> zeroCols = IntStream.range(0, n)
            .filter(j -> IntStream.range(0, m).anyMatch(i -> matrix[i][j] == 0))
            .boxed().collect(Collectors.toSet());

        IntStream.range(0, m).forEach(i ->
            IntStream.range(0, n).forEach(j -> {
                if (zeroRows.contains(i) || zeroCols.contains(j)) matrix[i][j] = 0;
            })
        );
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Two-pass HashSets tracking zero coordinates.
- **Stream API**: `IntStream.range()` zero-coordinate detection and updates.

---

### Q129: Reshape the Matrix (LeetCode #566)

#### 📄 Real-World Interview Problem Statement
> *"Given an `m x n` matrix and two integers `r` and `c`, reshape the matrix into a new `r x c` matrix filled with all elements of the original matrix in row-traversal order."*

#### 🛍️ Real-World Intuitive Story
Taking 6 tiles laid out in a $2 \times 3$ grid and rearranging them into a $3 \times 2$ or $1 \times 6$ grid without changing their order.

#### 📥 Input & 📤 Output
- **Input**: `mat = [[1,2],[3,4]]`, `r = 1`, `c = 4` $\rightarrow$ **Output**: `[[1,2,3,4]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"reshape matrix r x c"*.
- **Pattern**: **Flatten to 1D stream -> map to index i * c + j**.

#### 🛠️ Step-by-Step Strategy
1. Verify $m \times n == r \times c$.
2. Flatten original matrix into 1D stream.
3. Map flat index `idx` to reshaped row `idx / c` and col `idx % c`.

#### ☕ Solution 1: Normal Java
```java
public class ReshapeMatrixNormal {
    public static int[][] matrixReshape(int[][] mat, int r, int c) {
        int m = mat.length, n = mat[0].length;
        if (m * n != r * c) return mat;

        int[][] result = new int[r][c];
        for (int i = 0; i < m * n; i++) {
            result[i / c][i % c] = mat[i / n][i % n];
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class ReshapeMatrixStream {
    public static int[][] matrixReshape(int[][] mat, int r, int c) {
        int m = mat.length, n = mat[0].length;
        if (m * n != r * c) return mat;

        int[] flat = Arrays.stream(mat).flatMapToInt(Arrays::stream).toArray();
        return IntStream.range(0, r)
            .mapToObj(i -> IntStream.range(0, c)
                .map(j -> flat[i * c + j])
                .toArray())
            .toArray(int[][]::new);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Modulo arithmetic converting flat index to 2D coordinates.
- **Stream API**: `flatMapToInt` flattening followed by `IntStream.range()` row partitioning.

---

### Q130: Count Negative Numbers in a Sorted Matrix (LeetCode #1351)

#### 📄 Real-World Interview Problem Statement
> *"Given an `m x n` matrix `grid` sorted in non-increasing order both row-wise and column-wise, count the number of negative numbers in `grid`."*

#### 🛍️ Real-World Intuitive Story
Counting negative temperature entries in a sorted weather matrix.

#### 📥 Input & 📤 Output
- **Input**: `grid = [[4,3,2,-1],[3,2,1,-1],[1,1,-1,-2],[-1,-1,-2,-3]]` $\rightarrow$ **Output**: `8`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"count negative numbers in sorted matrix"*.
- **Pattern**: **Arrays.stream(grid).flatMapToInt.filter(x -> x < 0).count()**.

#### 🛠️ Step-by-Step Strategy
1. Stream rows of matrix grid.
2. FlatMap rows into primitive `IntStream`.
3. Filter `x < 0` and count.

#### ☕ Solution 1: Normal Java
```java
public class CountNegativesNormal {
    public static int countNegatives(int[][] grid) {
        int count = 0;
        for (int[] row : grid) {
            for (int val : row) {
                if (val < 0) count++;
            }
        }
        return count;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;

public class CountNegativesStream {
    public static int countNegatives(int[][] grid) {
        return (int) Arrays.stream(grid)
            .flatMapToInt(Arrays::stream)
            .filter(x -> x < 0)
            .count();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Simple double loop negative number check.
- **Stream API**: Declarative `flatMapToInt().filter().count()`.

---

### Q131: Lucky Numbers in a Matrix (LeetCode #1380)

#### 📄 Real-World Interview Problem Statement
> *"Given an `m x n` matrix of distinct numbers, return all lucky numbers in the matrix. A lucky number is an element of the matrix such that it is the minimum element in its row and maximum in its column."*

#### 🛍️ Real-World Intuitive Story
Finding an employee who is the quietest person on their immediate team (row min), but the loudest person across the entire company department (col max).

#### 📥 Input & 📤 Output
- **Input**: `matrix = [[3,7,8],[9,11,13],[15,16,17]]` $\rightarrow$ **Output**: `[15]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"row min col max lucky numbers"*.
- **Pattern**: **Row minimum set intersection with Column maximum set**.

#### 🛠️ Step-by-Step Strategy
1. Compute minimum value for each row.
2. Compute maximum value for each column.
3. Return numbers present in both sets.

#### ☕ Solution 1: Normal Java
```java
import java.util.*;

public class LuckyNumbersNormal {
    public static List<Integer> luckyNumbers(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        List<Integer> rowMins = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int min = matrix[i][0];
            for (int j = 1; j < n; j++) min = Math.min(min, matrix[i][j]);
            rowMins.add(min);
        }

        List<Integer> colMaxs = new ArrayList<>();
        for (int j = 0; j < n; j++) {
            int max = matrix[0][j];
            for (int i = 1; i < m; i++) max = Math.max(max, matrix[i][j]);
            colMaxs.add(max);
        }

        List<Integer> result = new ArrayList<>();
        for (int val : rowMins) {
            if (colMaxs.contains(val)) result.add(val);
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LuckyNumbersStream {
    public static List<Integer> luckyNumbers(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;

        List<Integer> rowMins = Arrays.stream(matrix)
            .map(row -> Arrays.stream(row).min().orElseThrow())
            .toList();

        Set<Integer> colMaxs = IntStream.range(0, n)
            .map(j -> IntStream.range(0, m).map(i -> matrix[i][j]).max().orElseThrow())
            .boxed()
            .collect(Collectors.toSet());

        return rowMins.stream()
            .filter(colMaxs::contains)
            .toList();
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Standard min/max loops and list intersection.
- **Stream API**: Functional row-min stream filtering against col-max set.

---

### Q132: Maximum Diagonal Sum in Square Matrix (Interview Classic)

#### 📄 Real-World Interview Problem Statement
> *"Given a square 2D matrix `int[][] mat`, compute the maximum sum obtained between the main diagonal vs the anti-diagonal."*

#### 🛍️ Real-World Intuitive Story
Comparing the total weight of items along the Top-Left to Bottom-Right diagonal against Top-Right to Bottom-Left diagonal, picking the larger total sum.

#### 📥 Input & 📤 Output
- **Input**: `[[1, 2], [3, 4]]` $\rightarrow$ **Output**: `5` (Main diagonal: $1+4=5$, Anti diagonal: $2+3=5$)

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"maximum diagonal sum square matrix"*.
- **Pattern**: **Math.max(mainDiagonalSum, antiDiagonalSum)**.

#### 🛠️ Step-by-Step Strategy
1. Compute main diagonal sum `mat[i][i]`.
2. Compute anti-diagonal sum `mat[i][n - 1 - i]`.
3. Return `Math.max(sum1, sum2)`.

#### ☕ Solution 1: Normal Java
```java
public class MaxDiagonalSumNormal {
    public static int maxDiagonalSum(int[][] mat) {
        int n = mat.length;
        int mainSum = 0, antiSum = 0;
        for (int i = 0; i < n; i++) {
            mainSum += mat[i][i];
            antiSum += mat[i][n - 1 - i];
        }
        return Math.max(mainSum, antiSum);
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.stream.IntStream;

public class MaxDiagonalSumStream {
    public static int maxDiagonalSum(int[][] mat) {
        int n = mat.length;
        int mainSum = IntStream.range(0, n).map(i -> mat[i][i]).sum();
        int antiSum = IntStream.range(0, n).map(i -> mat[i][n - 1 - i]).sum();
        return Math.max(mainSum, antiSum);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: Single loop summing both diagonals into variables.
- **Stream API**: `IntStream.range()` mapping both diagonals to streams.

---

### Q133: Convert 1D Array into 2D Array with Given Dimensions (LeetCode #2022)

#### 📄 Real-World Interview Problem Statement
> *"Given a 0-indexed 1D integer array `original` and integers `m` and `n`, construct an `m x n` 2D array using all elements from `original`. If impossible, return an empty 2D array."*

#### 🛍️ Real-World Intuitive Story
Arranging a long line of 12 chairs into 3 rows of 4 chairs each.

#### 📥 Input & 📤 Output
- **Input**: `original = [1,2,3,4]`, `m = 2`, `n = 2` $\rightarrow$ **Output**: `[[1,2],[3,4]]`

#### 🔍 Interview Clues & Trigger Keywords
- **Trigger Keywords**: *"convert 1D array to 2D matrix"*.
- **Pattern**: **IntStream.range(0, m).mapToObj(i -> copyOfRange)**.

#### 🛠️ Step-by-Step Strategy
1. Check `original.length == m * n`.
2. Generate row stream `i` from `0` to `m - 1`.
3. Slice sub-array `Arrays.copyOfRange(original, i * n, (i + 1) * n)`.

#### ☕ Solution 1: Normal Java
```java
import java.util.Arrays;

public class Construct2DArrayNormal {
    public static int[][] construct2DArray(int[] original, int m, int n) {
        if (original.length != m * n) return new int[0][0];
        int[][] result = new int[m][n];
        for (int i = 0; i < m; i++) {
            result[i] = Arrays.copyOfRange(original, i * n, (i + 1) * n);
        }
        return result;
    }
}
```

#### ⚡ Solution 2: Java Stream API
```java
import java.util.Arrays;
import java.util.stream.IntStream;

public class Construct2DArrayStream {
    public static int[][] construct2DArray(int[] original, int m, int n) {
        if (original.length != m * n) return new int[0][0];
        return IntStream.range(0, m)
            .mapToObj(i -> Arrays.copyOfRange(original, i * n, (i + 1) * n))
            .toArray(int[][]::new);
    }
}
```

#### ⚖️ Comparison & Key Takeaways
- **Normal Java**: `Arrays.copyOfRange()` loop constructing row arrays.
- **Stream API**: `IntStream.range(0, m).mapToObj()` slicing 1D array into 2D row arrays.
