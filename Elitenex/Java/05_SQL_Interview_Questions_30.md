# 📊 Top 30 SQL Interview Questions & Answers

---

### Q1: What are the main Sub-languages of SQL (DDL, DML, DQL, DCL, TCL)?
**Answer:**
SQL statements are classified into five functional categories:

1. **DDL (Data Definition Language):** Defines, alters, and manages database schema structures. Auto-commits in most RDBMS.
   - Commands: `CREATE`, `ALTER`, `DROP`, `TRUNCATE`, `RENAME`.
2. **DML (Data Manipulation Language):** Manages data within existing database tables.
   - Commands: `INSERT`, `UPDATE`, `DELETE`, `MERGE`.
3. **DQL (Data Query Language):** Queries and retrieves data from tables and views.
   - Command: `SELECT`.
4. **DCL (Data Control Language):** Manages permissions and access controls.
   - Commands: `GRANT`, `REVOKE`.
5. **TCL (Transaction Control Language):** Manages database transactions to maintain ACID compliance.
   - Commands: `COMMIT`, `ROLLBACK`, `SAVEPOINT`.

---

### Q2: What is the difference between `WHERE` and `HAVING` clauses?
**Answer:**
| Feature | `WHERE` Clause | `HAVING` Clause |
| :--- | :--- | :--- |
| **Filtering Level** | Filters **individual rows** before aggregation | Filters **aggregated groups** after aggregation |
| **Aggregate Functions** | **Cannot** use aggregate functions (`COUNT`, `AVG`, `SUM`) | **Can** use aggregate functions (`HAVING SUM(sales) > 1000`) |
| **Execution Order** | Executes before `GROUP BY` | Executes after `GROUP BY` |
| **Usage without GROUP BY**| Always valid | Valid, but acts on the entire table as a single aggregate group |

```sql
-- WHERE filters individual employee salaries; HAVING filters departments with avg > 80000
SELECT department_id, AVG(salary) AS avg_sal
FROM employees
WHERE status = 'ACTIVE'
GROUP BY department_id
HAVING AVG(salary) > 80000;
```

---

### Q3: Explain all types of SQL Joins with diagrams and use cases.
**Answer:**
Joins combine columns from one or more tables based on a related column between them.

```
       Table A            Table B
    +-----------+      +-----------+
    | 1 | Alice |      | 1 | HR    |
    | 2 | Bob   |      | 2 | IT    |
    | 3 | Carol |      | 4 | Sales |
    +-----------+      +-----------+
```

1. **`INNER JOIN`:** Returns only rows with matching values in both tables. (IDs: 1, 2)
2. **`LEFT (OUTER) JOIN`:** Returns all rows from left table, plus matched rows from right table (unmatched right columns are `NULL`). (IDs: 1, 2, 3)
3. **`RIGHT (OUTER) JOIN`:** Returns all rows from right table, plus matched rows from left table. (IDs: 1, 2, 4)
4. **`FULL (OUTER) JOIN`:** Returns all rows when there is a match in either table. Unmatched values on either side are `NULL`. (IDs: 1, 2, 3, 4)
5. **`CROSS JOIN`:** Cartesian product; produces every combination of rows from Table A with Table B ($M \times N$ rows).
6. **`SELF JOIN`:** A regular join where a table is joined with itself (useful for hierarchical data like employee-manager relationships).

```sql
-- Self Join Example: Finding Employee and Manager Name
SELECT e.name AS employee_name, m.name AS manager_name
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.id;
```

---

### Q4: How does SQL handle `NULL` values, and what is Three-Valued Logic?
**Answer:**
`NULL` represents missing, unknown, or inapplicable data. It is **not** equal to zero, an empty string `""`, or another `NULL`.

- **Three-Valued Logic:** In SQL, boolean comparisons with `NULL` evaluate to **`UNKNOWN`** (not `TRUE` or `FALSE`):
  - `NULL = NULL` $\rightarrow$ `UNKNOWN`
  - `NULL != 5` $\rightarrow$ `UNKNOWN`
- **Testing for NULL:** Always use `IS NULL` or `IS NOT NULL` (never `= NULL`).

**Key Handling Functions:**
- **`COALESCE(val1, val2, ..., valN)`:** Returns the first non-null expression in the argument list.
  ```sql
  SELECT name, COALESCE(phone, email, 'No Contact Info') AS contact FROM users;
  ```
- **`NULLIF(expr1, expr2)`:** Returns `NULL` if `expr1 = expr2`; otherwise returns `expr1`. (Great for preventing division-by-zero errors: `total / NULLIF(count, 0)`).

---

### Q5: Compare `UNION` vs `UNION ALL`.
**Answer:**
Both combine result sets from two or more `SELECT` queries (queries must have the same number of columns with compatible data types).

| Feature | `UNION` | `UNION ALL` |
| :--- | :--- | :--- |
| **Duplicates** | **Removes duplicate rows** | **Keeps all duplicate rows** |
| **Performance** | **Slower**; performs sorting/hashing in memory to deduplicate | **Faster**; simply concatenates datasets without sorting |
| **Memory Usage** | High memory/temp disk overhead | Minimal memory overhead |

**Rule of Thumb:** Always use `UNION ALL` unless you specifically require distinct rows.

---

### Q6: Compare `DELETE`, `TRUNCATE`, and `DROP`.
**Answer:**
| Dimension | `DELETE` | `TRUNCATE` | `DROP` |
| :--- | :--- | :--- | :--- |
| **Command Type** | DML | DDL | DDL |
| **Scope** | Deletes specific rows or all rows (`WHERE` allowed) | Removes all rows; resets auto-increment PK (`WHERE` not allowed) | Completely deletes table schema and data |
| **Rollback** | Can be rolled back inside transaction | Rollback depends on RDBMS (yes in Postgres/SQL Server; no in MySQL) | Cannot be rolled back in most RDBMS |
| **Triggers** | Fires row-level `ON DELETE` triggers | Does **not** fire delete triggers | Does not fire delete triggers |
| **Performance** | Slow (logs row deletion individually in transaction log) | Fast (deallocates data pages / high-water mark) | Instantaneous |

---

### Q7: Compare Primary Key vs Unique Key vs Foreign Key.
**Answer:**
- **Primary Key (PK):** Uniquely identifies each record in a table. Does **not** allow `NULL` values. Only **one** primary key is allowed per table. Automatically creates a unique clustered index (in MySQL InnoDB and SQL Server).
- **Unique Key (UK):** Guarantees uniqueness across column(s). Allows `NULL` values (PostgreSQL and MySQL allow multiple `NULL`s; SQL Server allows only one `NULL`). Multiple unique constraints can exist on a single table.
- **Foreign Key (FK):** Enforces referential integrity between child and parent tables.
  - Cascade actions: `ON DELETE CASCADE` (deletes child rows), `ON DELETE SET NULL` (sets child FK to null), `ON DELETE RESTRICT` / `NO ACTION` (blocks parent deletion).

---

### Q8: What are SQL Constraints?
**Answer:**
Constraints enforce data integrity rules at the column or table level:
1. `NOT NULL`: Prevents insertion of null values.
2. `UNIQUE`: Ensures all values in a column are distinct.
3. `PRIMARY KEY`: Combination of `NOT NULL` and `UNIQUE`.
4. `FOREIGN KEY`: Validates that column values exist in a referenced parent table.
5. `CHECK`: Validates that values satisfy a boolean condition (e.g., `CHECK (age >= 18 AND salary > 0)`).
6. `DEFAULT`: Supplies a fallback value when none is explicitly provided (`created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP`).

---

### Q9: Compare Correlated vs Non-Correlated Subqueries.
**Answer:**
- **Non-Correlated Subquery:**
  - Independent of the outer query.
  - Executes **once**; its result is substituted into the outer query.
  ```sql
  -- Finds employees earning more than the company average salary
  SELECT name, salary FROM employees
  WHERE salary > (SELECT AVG(salary) FROM employees);
  ```
- **Correlated Subquery:**
  - References columns from the outer query table (`e1.department_id = e2.department_id`).
  - Evaluated **row-by-row for every single candidate row** processed by the outer query ($O(N \times M)$ overhead if unindexed).
  ```sql
  -- Finds employees earning more than the average of THEIR OWN department
  SELECT e1.name, e1.salary, e1.department_id
  FROM employees e1
  WHERE e1.salary > (
      SELECT AVG(e2.salary)
      FROM employees e2
      WHERE e2.department_id = e1.department_id
  );
  ```

---

### Q10: Difference between `EXISTS` and `IN`, and how do they handle `NULL`?
**Answer:**
- **`IN`:** Evaluates the entire subquery result list and performs equality checks (`col = val1 OR col = val2`).
  - *Pitfall:* If the subquery in `NOT IN` returns even a **single `NULL`**, the entire `NOT IN` expression evaluates to `UNKNOWN` and returns **zero records**!
- **`EXISTS`:** Returns boolean `TRUE` as soon as the database finds the first matching row (short-circuit evaluation).
  - Handles `NULL`s safely because it checks for the existence of rows rather than value equality.

```sql
-- DANGEROUS: If any dept has manager_id NULL, this returns 0 rows!
SELECT * FROM employees WHERE id NOT IN (SELECT manager_id FROM departments);

-- SAFE & EFFICIENT:
SELECT * FROM employees e
WHERE NOT EXISTS (SELECT 1 FROM departments d WHERE d.manager_id = e.id);
```

---

### Q11: What are Window Functions, and how do they differ from `GROUP BY`?
**Answer:**
A **Window Function** performs calculations across a set of table rows related to the current row, **without collapsing individual rows** into a single aggregated summary row.

**Syntax:**
```sql
FUNCTION() OVER (
    PARTITION BY partition_col
    ORDER BY sort_col
    ROWS/RANGE BETWEEN ...
)
```

| Feature | `GROUP BY` | Window Function |
| :--- | :--- | :--- |
| **Row Count** | Collapses rows down to 1 row per group | **Retains all original individual rows** |
| **Access to Details**| Cannot see individual row fields alongside aggregates | Can display individual row fields alongside partition aggregates |
| **Where Clause** | Evaluated before `GROUP BY` | Evaluated after `WHERE`, `GROUP BY`, and `HAVING` |

---

### Q12: Compare `ROW_NUMBER()`, `RANK()`, and `DENSE_RANK()`.
**Answer:**
All three rank rows within an `OVER (ORDER BY ...)` partition, but handle identical (tied) values differently:

| Value | `ROW_NUMBER()` | `RANK()` | `DENSE_RANK()` |
| :---: | :---: | :---: | :---: |
| 100 | **1** | **1** | **1** |
| 100 | **2** | **1** (tied) | **1** (tied) |
| 90 | **3** | **3** (skips 2) | **2** (no skip) |
| 80 | **4** | **4** | **3** |

```sql
SELECT name, score,
       ROW_NUMBER() OVER (ORDER BY score DESC) AS row_num,
       RANK()       OVER (ORDER BY score DESC) AS rnk,
       DENSE_RANK() OVER (ORDER BY score DESC) AS dense_rnk
FROM exam_results;
```

---

### Q13: Explain `LEAD()` and `LAG()` Window Functions with a Real-World Example.
**Answer:**
- **`LAG(col, offset, default)`:** Accesses data from a previous row in the partition without a self-join.
- **`LEAD(col, offset, default)`:** Accesses data from a subsequent row in the partition.

**Use Case: Calculating Month-over-Month Revenue Growth:**
```sql
SELECT 
    month,
    revenue,
    LAG(revenue, 1) OVER (ORDER BY month) AS prev_month_rev,
    ROUND(
      (revenue - LAG(revenue, 1) OVER (ORDER BY month)) * 100.0 / 
      LAG(revenue, 1) OVER (ORDER BY month), 2
    ) AS mom_growth_pct
FROM monthly_sales;
```

---

### Q14: How do you calculate a Running Total and 7-Day Moving Average in SQL?
**Answer:**
Using the `SUM()` / `AVG()` window functions with frame specifications:

```sql
SELECT 
    order_date,
    amount,
    -- Running Total (Cumulative Sum)
    SUM(amount) OVER (
        ORDER BY order_date
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS running_total,
    -- 7-Day Moving Average
    AVG(amount) OVER (
        ORDER BY order_date
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
    ) AS moving_avg_7d
FROM daily_sales;
```

---

### Q15: Query Challenge: How to find the $N$-th Highest Salary in SQL?
**Answer:**
**Method 1: Using `DENSE_RANK()` (Handles duplicate salaries properly - Recommended):**
```sql
WITH RankedSalaries AS (
    SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) AS rnk
    FROM employees
)
SELECT DISTINCT salary 
FROM RankedSalaries 
WHERE rnk = 3; -- e.g., 3rd highest
```

**Method 2: Using `LIMIT` and `OFFSET` (MySQL / PostgreSQL):**
```sql
SELECT DISTINCT salary 
FROM employees 
ORDER BY salary DESC 
LIMIT 1 OFFSET 2; -- (N - 1) offset for N-th
```

**Method 3: Correlated Subquery (Database Agnostic):**
```sql
SELECT DISTINCT e1.salary
FROM employees e1
WHERE 2 = ( -- N - 1
    SELECT COUNT(DISTINCT e2.salary)
    FROM employees e2
    WHERE e2.salary > e1.salary
);
```

---

### Q16: Query Challenge: How to Find and Delete Duplicate Rows from a Table?
**Answer:**
**Scenario:** A table `users(id, email, created_at)` has duplicate `email` entries. Keep the oldest record (`MIN(id)` or oldest `created_at`) and remove duplicates.

**Step 1: Finding Duplicates:**
```sql
SELECT email, COUNT(*) AS occurrences
FROM users
GROUP BY email
HAVING COUNT(*) > 1;
```

**Step 2: Deleting Duplicates using CTE and `ROW_NUMBER()`:**
```sql
WITH NumberedRows AS (
    SELECT id, email,
           ROW_NUMBER() OVER (PARTITION BY email ORDER BY created_at ASC, id ASC) AS rn
    FROM users
)
DELETE FROM users
WHERE id IN (
    SELECT id FROM NumberedRows WHERE rn > 1
);
```

---

### Q17: Query Challenge: Find Employees who earn more than their direct Managers.
**Answer:**
Requires a **Self-Join** comparing the employee record to their manager's record:

```sql
SELECT 
    e.name AS employee_name,
    e.salary AS employee_salary,
    m.name AS manager_name,
    m.salary AS manager_salary
FROM employees e
INNER JOIN employees m ON e.manager_id = m.id
WHERE e.salary > m.salary;
```

---

### Q18: What is a Common Table Expression (CTE), and how does it compare to Subqueries and Temp Tables?
**Answer:**
A **CTE (Common Table Expression)** is a temporary, named result set defined using the `WITH` clause that exists only during the execution scope of a single `SELECT`, `INSERT`, `UPDATE`, or `DELETE` statement.

| Dimension | CTE (`WITH`) | Subquery (Derived Table) | Temp Table (`#temp`) |
| :--- | :--- | :--- | :--- |
| **Readability** | High; modular top-to-bottom flow | Low; nested inside-out | Medium; separate script blocks |
| **Reusability** | Can be referenced multiple times in same query | Must be copy-pasted | Accessible across multiple queries in session |
| **Indexing** | Cannot be indexed directly | Cannot be indexed directly | Can add custom indexes and statistics |
| **Lifetime** | Single statement | Single statement | Entire database connection session |

---

### Q19: What is a Recursive CTE, and how do you query Hierarchical Data?
**Answer:**
A **Recursive CTE** references itself to traverse recursive relationships (e.g., manager-employee org trees, folder structures).

**Components:**
1. **Anchor Member:** Initial query that returns the base root row(s).
2. **Recursive Member:** Joins the CTE back to the base table to fetch child levels.
3. **Termination Condition:** Loop finishes when no new rows are produced.

```sql
WITH RECURSIVE OrgHierarchy AS (
    -- Anchor: Top-level CEO (manager_id is NULL)
    SELECT id, name, manager_id, 1 AS level
    FROM employees
    WHERE manager_id IS NULL

    UNION ALL

    -- Recursive Step: Find subordinates of current level
    SELECT e.id, e.name, e.manager_id, o.level + 1
    FROM employees e
    INNER JOIN OrgHierarchy o ON e.manager_id = o.id
)
SELECT level, name, manager_id FROM OrgHierarchy ORDER BY level, name;
```

---

### Q20: What is a Database Index? Compare Clustered vs Non-Clustered Indexes.
**Answer:**
An **Index** is a data structure (predominantly **B+ Tree**) that accelerates data retrieval operations at the cost of slower writes (`INSERT`, `UPDATE`, `DELETE`) and additional storage.

| Feature | Clustered Index | Non-Clustered (Secondary) Index |
| :--- | :--- | :--- |
| **Data Storage** | Table data rows are physically sorted & stored directly at the leaf nodes | Leaf nodes store index key values + pointers (RID or PK) to actual data rows |
| **Count per Table**| **Exactly 1** per table (defines physical ordering) | **Multiple** (up to hundreds) per table |
| **Lookup Cost** | Direct seek to data row ($O(\log N)$) | Seek on secondary index + lookup jump to clustered table ($O(\log N) + O(\log N)$) |
| **Default** | Primary Key column | Explicitly created via `CREATE INDEX` |

---

### Q21: What is a Composite Index and the "Leftmost Prefix" Rule?
**Answer:**
A **Composite Index** is an index built on multiple columns: `CREATE INDEX idx_user_loc ON users(country, state, city)`.

**Leftmost Prefix Rule:**
The query optimizer can only use the index if the query filters include columns starting from the leftmost column in sequence.

- `WHERE country = 'US' AND state = 'CA'` $\rightarrow$ **Index Used!**
- `WHERE country = 'US'` $\rightarrow$ **Index Used!**
- `WHERE country = 'US' AND city = 'SF'` $\rightarrow$ **Partially Used** (Only `country` uses index seek; `city` is filtered afterwards).
- `WHERE state = 'CA' AND city = 'SF'` $\rightarrow$ **Index CANNOT be used!** (Leftmost column `country` is missing).

---

### Q22: What is a Covering Index, and what is the difference between Index Seek and Index Scan?
**Answer:**
- **Index Seek:** The engine traverses the B-Tree from root to leaf to locate specific target rows directly. Fastest operation ($O(\log N)$).
- **Index Scan:** The engine reads through all leaf nodes of the index sequentially. Happens when looking for a range or when selectivity is low.
- **Table / Sequential Scan:** The engine reads the entire table from disk.
- **Covering Index:** An index that contains **every column requested by the `SELECT`, `WHERE`, `JOIN`, and `ORDER BY` clauses**.
  - *Why it matters:* Eliminates the secondary bookmark lookup (Table/Heap Lookup) completely because the answer can be satisfied entirely from index pages!

---

### Q23: Why would a Database Ignore an existing Index?
**Answer:**
Common reasons the query optimizer skips an index and defaults to a full table scan:
1. **Applying Functions on Indexed Columns:**
   ```sql
   -- BAD (Forces Full Table Scan):
   WHERE UPPER(email) = 'TEST@GMAIL.COM'
   WHERE YEAR(created_at) = 2024
   
   -- GOOD (Uses Index Seek):
   WHERE email = 'test@gmail.com'
   WHERE created_at >= '2024-01-01' AND created_at < '2025-01-01'
   ```
2. **Leading Wildcard in LIKE Queries:** `LIKE '%phone'` cannot use B-tree index seek (must use `LIKE 'phone%'`).
3. **Implicit Data Type Conversion:** Comparing a `VARCHAR` column with an integer literal (`WHERE phone_number = 12345`).
4. **Low Selectivity / High Percentage of Rows:** If a query returns > 20-30% of total table rows, a full sequential scan is faster than thousands of random disk seeks.
5. **Violation of the Leftmost Prefix Rule.**

---

### Q24: Explain Database Normalization (1NF, 2NF, 3NF, BCNF) and when to Denormalize.
**Answer:**
Normalization reduces data redundancy and prevents update, insert, and delete anomalies.

1. **1NF (First Normal Form):** Every column must contain atomic (indivisible) values; no repeating groups or comma-separated lists; unique primary key.
2. **2NF (Second Normal Form):** In 1NF + **No partial dependency** (every non-key attribute must depend on the *entire* primary key, relevant for composite primary keys).
3. **3NF (Third Normal Form):** In 2NF + **No transitive dependency** (non-key columns must not depend on other non-key columns: $A \rightarrow B, B \rightarrow C$).
4. **BCNF (Boyce-Codd Normal Form):** Stricter version of 3NF; for every functional dependency $X \rightarrow Y$, $X$ must be a super key.

**When to Denormalize:**
In read-heavy systems, data warehouses, and reporting dashboards (OLAP) to avoid 10-table joins and optimize sub-second read query latency.

---

### Q25: Explain ACID Properties and how RDBMS engines implement them.
**Answer:**
- **Atomicity:** All operations in a transaction succeed or all fail together. Implemented via **Undo Logs** (rolling back on failure).
- **Consistency:** Database transitions from one valid state to another valid state, respecting all constraints and foreign keys.
- **Isolation:** Concurrent transactions execute without interfering with one another. Implemented via **Locks (Row/Table locks)** and **MVCC (Multi-Version Concurrency Control)**.
- **Durability:** Once committed, changes survive server crashes or power failures. Implemented via **Write-Ahead Logging (WAL) / Redo Logs** flushed to disk before acknowledging commit.

---

### Q26: Compare Views vs Materialized Views.
**Answer:**
| Feature | Standard View | Materialized View |
| :--- | :--- | :--- |
| **Data Storage** | **Virtual table**; stores only the SQL query text; holds no physical data | **Physical table**; executes query and saves actual data to disk |
| **Execution** | Query is re-run dynamically every time the view is queried | Queries read precomputed results from disk instantly |
| **Data Freshness** | Always 100% real-time accurate | Stale until refreshed (manual, on schedule, or trigger-based) |
| **Indexing** | Cannot be indexed directly in most engines | Can have custom B-Tree indexes created directly on it |
| **Best Used For** | Security access abstraction, hiding complex join logic | Heavy analytical aggregations, reporting dashboards |

---

### Q27: Compare Stored Procedures vs User-Defined Functions (UDFs).
**Answer:**
| Feature | Stored Procedure | User-Defined Function (UDF) |
| :--- | :--- | :--- |
| **Return Value** | Can return 0, 1, or multiple result sets and output parameters | Must return exactly **one value** (scalar) or a single table |
| **Usage in SQL** | Invoked independently via `CALL` or `EXEC` | Can be embedded directly inside `SELECT`, `WHERE`, `JOIN` |
| **Transactions** | Can initiate and manage transactions (`COMMIT`, `ROLLBACK`) | Cannot manage transactions |
| **Side Effects** | Allowed to modify database state (`INSERT`, `UPDATE`, `DELETE`) | Strictly read-only; cannot perform DDL or DML mutations |

---

### Q28: What are Database Triggers, and why should they be used with caution?
**Answer:**
A **Trigger** is a stored SQL block automatically executed by the database engine in response to an event (`BEFORE` or `AFTER` an `INSERT`, `UPDATE`, or `DELETE`) on a specified table.

**Dangers of Overusing Triggers in Enterprise Microservices:**
1. **Hidden Side Effects:** Business logic is obscured inside the database rather than visible in application code.
2. **Performance Latency:** Row-level triggers run synchronously, drastically slowing down batch inserts/updates.
3. **Debugging Nightmare:** Hard to trace failures when triggers trigger cascading triggers on other tables.
4. **Bypassed Testing:** Application-level unit tests cannot mock or test triggers easily.

---

### Q29: What is SQL Injection, and how do Prepared Statements eliminate it?
**Answer:**
**SQL Injection (SQLi)** occurs when malicious user input is concatenated directly into a dynamic SQL query string, altering the query's syntactic structure.

*Vulnerable Code:*
```java
String sql = "SELECT * FROM users WHERE user = '" + inputUser + "' AND pass = '" + inputPass + "'";
// Input: ' OR '1'='1
// Result: SELECT * FROM users WHERE user = '' OR '1'='1' ... (Dumps all records!)
```

**How Prepared Statements Prevent SQLi:**
Prepared Statements pre-compile the SQL statement into an Abstract Syntax Tree (AST) on the database engine with parameter placeholders (`?`). When user parameters are sent later, they are treated strictly as **literal data**, never as executable SQL tokens, completely rendering injection impossible.

---

### Q30: What causes Database Deadlocks, and how can Applications prevent them?
**Answer:**
A **Deadlock** occurs when two or more transactions hold locks on resources that the other transactions need, creating a circular wait cycle:
- Transaction 1 locks Row A, waits for Row B.
- Transaction 2 locks Row B, waits for Row A.
- Result: Neither can proceed; database deadlock detector kills one transaction (the "deadlock victim") and rolls it back.

**How to Prevent Deadlocks in Code:**
1. **Consistent Lock Ordering:** Always access and update tables and rows in the **exact same deterministic order** across all service methods (e.g., sort IDs before locking: `UPDATE accounts WHERE id IN (1, 2) ORDER BY id`).
2. **Keep Transactions Short:** Avoid external REST calls or heavy processing inside database transactions.
3. **Use Lower Isolation Levels:** Use `READ COMMITTED` with MVCC instead of aggressive serializable table locks.
4. **Implement Retry Logic:** Catch database deadlock exceptions (`DeadlockLoserDataAccessException`) in application code and retry with exponential backoff.

