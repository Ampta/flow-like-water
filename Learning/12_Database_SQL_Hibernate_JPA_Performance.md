# Chapter 12: Database Performance, SQL Queries, Hibernate & JPA Internals

This guide covers real-world database and ORM interview questions asked across **NUSUMMIT, Axis Bank, Paramatrix, Crisil, Smart e, Infibeam, NCS Soft Solutions, C2L, and Applied Cloud Computing**.

---

## 📑 Table of Contents
1. [Nth Highest & 2nd Highest Salary Queries: Subqueries vs Window Functions](#1-nth-highest--2nd-highest-salary-queries-subqueries-vs-window-functions)
2. [Deleting Duplicate Records from a Table](#2-deleting-duplicate-records-from-a-table)
3. [Deep-Dive into SQL Joins & Subqueries](#3-deep-dive-into-sql-joins--subqueries)
4. [Keys, Constraints & Differences: Primary vs Unique, TRUNCATE vs DELETE vs DROP](#4-keys-constraints--differences-primary-vs-unique-truncate-vs-delete-vs-drop)
5. [Hibernate vs JPA & The 4 Entity States](#5-hibernate-vs-jpa--the-4-entity-states)
6. [Hibernate `get()` vs `load()` & Lazy Loading Internals](#6-hibernate-get-vs-load--lazy-loading-internals)
7. [The N+1 Query Problem: Causes, Profiling & 3 Concrete Fixes](#7-the-n1-query-problem-causes-profiling--3-concrete-fixes)
8. [First-Level (L1) vs Second-Level (L2) Cache Architecture](#8-first-level-l1-vs-second-level-l2-cache-architecture)
9. [Designing High-Performance Pagination for 10 Million Records (Offset vs Keyset)](#9-designing-high-performance-pagination-for-10-million-records-offset-vs-keyset)
10. [Database Sharding & Table Partitioning Strategies](#10-database-sharding--table-partitioning-strategies)

---

## 1. Nth Highest & 2nd Highest Salary Queries: Subqueries vs Window Functions

### 1. Previous Knowledge Required
- `ORDER BY` with `LIMIT` and `OFFSET`.
- Correlated subqueries vs Analytical Window Functions (`DENSE_RANK()`).

### 2. Logic & Theory (Deep-Dive)

#### Why `LIMIT 1 OFFSET 1` is Flawed for Real Interviews:
If two employees tie for the highest salary (e.g., both earn $150,000), `SELECT DISTINCT salary ... LIMIT 1 OFFSET 1` works, but if distinct is omitted or multiple rows are expected, it fails.
The **Gold Standard** in enterprise interviews is using analytical window functions (`DENSE_RANK()`).

#### Why `DENSE_RANK()` over `RANK()`:
- `RANK()` leaves gaps on ties: Salaries [100, 100, 80] are ranked [1, 1, 3] (Rank 2 is skipped!).
- `DENSE_RANK()` has no gaps: Salaries [100, 100, 80] are ranked [1, 1, 2] (Rank 2 is preserved!).

### 3. Practical SQL Queries

#### Approach 1: Window Function (Enterprise Standard for any $N$th highest)
```sql
WITH RankedSalaries AS (
    SELECT 
        emp_id, 
        emp_name, 
        salary, 
        dept_id,
        DENSE_RANK() OVER (ORDER BY salary DESC) AS salary_rank
    FROM employees
)
SELECT emp_id, emp_name, salary, dept_id
FROM RankedSalaries
WHERE salary_rank = 2; -- Change to N for Nth highest
```

#### Approach 2: Correlated Subquery (Engine-independent)
```sql
SELECT emp_name, salary
FROM employees e1
WHERE 2 = (
    SELECT COUNT(DISTINCT e2.salary)
    FROM employees e2
    WHERE e2.salary >= e1.salary
);
```

#### Approach 3: MySQL Quick Syntax
```sql
SELECT DISTINCT salary 
FROM employees 
ORDER BY salary DESC 
LIMIT 1 OFFSET 1; -- 2nd highest (LIMIT 1 OFFSET N-1)
```

---

## 2. Deleting Duplicate Records from a Table

### 1. Previous Knowledge Required
- Common Table Expressions (CTE).
- Primary keys and partition grouping.

### 2. Logic & Theory (Deep-Dive)
In production, accidental duplicate imports happen (e.g., same PAN or email entered multiple times). You must delete duplicates while preserving exactly one record (e.g., the lowest or highest primary key `id`).

### 3. Practical SQL Queries

#### Approach 1: Modern CTE with `ROW_NUMBER()` (PostgreSQL, SQL Server, Oracle, MySQL 8.0+)
```sql
WITH DuplicateCTE AS (
    SELECT 
        id, 
        pan_number, 
        created_at,
        ROW_NUMBER() OVER (
            PARTITION BY pan_number 
            ORDER BY id ASC -- Keeps the original first record
        ) as row_num
    FROM loan_customers
)
DELETE FROM loan_customers
WHERE id IN (
    SELECT id FROM DuplicateCTE WHERE row_num > 1
);
```

#### Approach 2: Self-Join (Compatible with older MySQL versions)
```sql
DELETE c1 FROM loan_customers c1
INNER JOIN loan_customers c2 
WHERE c1.pan_number = c2.pan_number 
  AND c1.id > c2.id; -- Deletes higher IDs, keeps lowest ID
```

---

## 3. Deep-Dive into SQL Joins & Subqueries

### 1. Previous Knowledge Required
- Relational algebra and Venn diagram sets.
- Null values in outer sets.

### 2. Logic & Theory (Deep-Dive)

```
       INNER JOIN                   LEFT JOIN                  FULL OUTER JOIN
    +-------+-------+           +-------+-------+           +-------+-------+
    |   A   |   B   |           |   A   |   B   |           |   A   |   B   |
    |     +---+     |           |  +++++++  |   |           |  +++++++++++  |
    |     |+++|     |           |  +++++++  |   |           |  +++++++++++  |
    |     +---+     |           |  +++++++  |   |           |  +++++++++++  |
    +-------+-------+           +-------+-------+           +-------+-------+
    Only matching rows        All of A + matching B         All rows from A & B
```

- **INNER JOIN**: Returns rows only when there is a match in both tables.
- **LEFT (OUTER) JOIN**: Returns all records from the left table, and matched records from the right table (NULL if no match).
- **RIGHT (OUTER) JOIN**: Returns all records from the right table, and matched records from the left table.
- **CROSS JOIN**: Cartesian product (every row in table A paired with every row in table B).
- **Interview Question from Smart e**: *"Count rows per `department_id`, exclude NULL `department_id`, but DO NOT use a `WHERE` clause."*
  - **Solution**: Use `HAVING` or an `INNER JOIN` with the departments table!
  ```sql
  SELECT department_id, COUNT(*) 
  FROM employees 
  GROUP BY department_id 
  HAVING department_id IS NOT NULL;
  ```

---

## 4. Keys, Constraints & Differences: Primary vs Unique, TRUNCATE vs DELETE vs DROP

### 1. Primary Key vs Unique Key
| Feature | Primary Key | Unique Key |
| :--- | :--- | :--- |
| **Null Values** | Strictly **NO NULLs** allowed. | Allows **NULL** values (MySQL allows multiple NULLs; SQL Server allows one). |
| **Count Per Table** | Exactly **one** primary key per table. | Multiple unique constraints per table. |
| **Clustered Index** | Automatically creates a **Clustered Index** by default. | Creates a **Non-Clustered Index** by default. |

### 2. TRUNCATE vs DELETE vs DROP
| Feature | `DELETE` | `TRUNCATE` | `DROP` |
| :--- | :--- | :--- | :--- |
| **Command Type** | DML (Data Manipulation) | DDL (Data Definition) | DDL (Data Definition) |
| **Rollback Capability** | Can be rolled back inside transaction. | Cannot be rolled back in most databases (auto-commits). | Cannot be rolled back. |
| **WHERE Clause** | Supported (`DELETE FROM t WHERE id=1`). | NOT supported (clears entire table). | NOT supported (drops schema). |
| **Identity Reset** | Preserves auto-increment sequence. | Resets auto-increment counter back to 1. | Drops table and sequence entirely. |
| **Triggers** | Fires `ON DELETE` triggers row-by-row. | Does **NOT** fire delete triggers. | Does not fire triggers. |
| **Performance** | Slow (logs row-by-row deletions). | Blazing fast (deallocates data pages). | Instantaneous metadata deletion. |

---

## 5. Hibernate vs JPA & The 4 Entity States

### 1. Previous Knowledge Required
- ORM (Object-Relational Mapping).
- Java Persistence API (JPA) as a specification vs Hibernate as the provider.

### 2. Logic & Theory (Deep-Dive)
- **JPA** is only an **interface / specification** (part of `jakarta.persistence.*`). It contains annotations (`@Entity`, `@Table`, `@Id`) and interfaces (`EntityManager`).
- **Hibernate** is the **concrete implementation engine** that implements JPA specifications and adds proprietary extensions (e.g., Caching, `@DynamicUpdate`).

#### The 4 JPA Entity States:
```
           new Customer()
                 |
                 v
           [ TRANSIENT ]  (In JVM memory; no DB identity, not tracked by Hibernate)
                 |
                 | entityManager.persist(c) / repo.save(c)
                 v
           [ MANAGED / PERSISTENT ] (Tracked by L1 cache; dirty checking updates DB)
              /     \
             /       \ entityManager.detach(c) / session.close()
            v         v
     [ REMOVED ]   [ DETACHED ] (Has DB ID, but changes are NO LONGER tracked)
   (Marked for          |
    SQL DELETE)         | entityManager.merge(c)
                        v
                   [ MANAGED ]
```

---

## 6. Hibernate `get()` vs `load()` & Lazy Loading Internals

### 1. Previous Knowledge Required
- Hibernate `Session` / JPA `EntityManager`.
- CGLIB Proxy classes and Java bytecode generation.

### 2. Logic & Theory (Deep-Dive)

| Criterion | `session.get()` / `entityManager.find()` | `session.load()` / `entityManager.getReference()` |
| :--- | :--- | :--- |
| **Database Hit** | **Eager**: Hits the database immediately via SQL `SELECT`. | **Lazy**: Does **NOT** hit the database immediately. |
| **Return Type** | Returns the real entity object (or `null` if not found). | Returns a lightweight **Bytecode Proxy** (CGLIB subclass). |
| **When Record Missing** | Returns `null`. | Throws `ObjectNotFoundException` when uninitialized proxy is accessed. |
| **Best Use Case** | When you aren't sure if the entity exists and need data now. | When creating a foreign-key relationship where you only need the ID! |

```java
// Performance Tip: Creating a relationship without hitting DB!
Order order = new Order();
// Does NOT execute SELECT * FROM customers WHERE id = 101!
Customer customerProxy = entityManager.getReference(Customer.class, 101L);
order.setCustomer(customerProxy); // Sets foreign key customer_id = 101 directly
entityManager.persist(order);
```

---

## 7. The N+1 Query Problem: Causes, Profiling & 3 Concrete Fixes

### 1. Previous Knowledge Required
- One-to-Many associations (`@OneToMany`).
- SQL subqueries and join overhead.

### 2. Logic & Theory (Deep-Dive)

#### How N+1 Happens:
Suppose you have 100 `Department` records, each having multiple `Employee` records mapped with `@OneToMany(fetch = FetchType.LAZY)`:
```java
List<Department> depts = departmentRepository.findAll(); // 1 Query: SELECT * FROM department
for (Department d : depts) {
    System.out.println(d.getEmployees().size()); // Fires 100 separate SELECT queries!
}
// Total queries: 1 + 100 = 101 queries executed! Catastrophic DB performance!
```

#### 3 Concrete Industry Fixes:

##### Fix 1: `JOIN FETCH` in JPQL
Forces Hibernate to fetch both entities in a single SQL query using an `INNER/LEFT JOIN`:
```java
@Query("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees")
List<Department> findAllWithEmployees();
```

##### Fix 2: `@EntityGraph` (Spring Data JPA)
Declaratively configures fetch plans without writing raw JPQL joins:
```java
@EntityGraph(attributePaths = {"employees"})
List<Department> findAll();
```

##### Fix 3: Batch Fetching (`@BatchSize` / `default_batch_fetch_size`)
Instead of 100 queries, loads associations in batches of 25 using `WHERE department_id IN (?, ?, ?, ?)`:
```yaml
# application.yml
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 25
```

---

## 8. First-Level (L1) vs Second-Level (L2) Cache Architecture

### 1. Previous Knowledge Required
- `Session` / `EntityManager` scope vs `SessionFactory` application scope.

### 2. Logic & Theory (Deep-Dive)

```
+-------------------------------------------------------------+
|                 Application (All Users)                     |
|                                                             |
|   +-----------------------------------------------------+   |
|   |         Second-Level (L2) Cache (Redis / EhCache)    |   |
|   +-----------------------------------------------------+   |
|                 ^                               ^           |
|                 |                               |           |
|      +---------------------+         +--------------------+ |
|      | User 1 Session (L1) |         | User 2 Session(L1) | |
|      +---------------------+         +--------------------+ |
|                 |                               |           |
+-----------------|-------------------------------|-----------+
                  v                               v
             +-----------------------------------------+
             |            Database Server              |
             +-----------------------------------------+
```

| Criterion | First-Level (L1) Cache | Second-Level (L2) Cache |
| :--- | :--- | :--- |
| **Scope** | Session / Transaction scope (Bound to current thread). | `SessionFactory` / Application scope (Shared across all threads). |
| **Default State** | **Enabled by default** (Cannot be turned off). | **Disabled by default** (Requires configuration). |
| **Provider** | Built into Hibernate internally. | External caching providers: Redis, Hazelcast, EhCache. |
| **Lifecycle** | Destroyed when `session.close()` or transaction ends. | Survives sessions; cleared on eviction or TTL expiry. |

---

## 9. Designing High-Performance Pagination for 10 Million Records (Offset vs Keyset)

### 1. Previous Knowledge Required
- B-Tree index traversal.
- Database execution plans (`EXPLAIN ANALYZE`).

### 2. Logic & Theory (Deep-Dive)

#### The Problem with Offset Pagination on Large Datasets:
```sql
SELECT * FROM loan_records ORDER BY id ASC LIMIT 20 OFFSET 5000000;
```
- **Why it kills the database**: The database engine must scan all **5,000,020 rows** from disk/index, discard the first 5,000,000 rows, and return only the last 20 rows! Response time degrades from 2ms on page 1 to 15 seconds on page 50,000.

#### The Solution: Keyset Pagination (Seek Method)
Instead of skipping records with `OFFSET`, remember the last seen primary key or timestamp:
```sql
-- Client passes id of the last record from the previous page
SELECT * FROM loan_records 
WHERE id > 5000000 
ORDER BY id ASC 
LIMIT 20;
```
- **Performance**: Instantaneous ($O(\log N)$ B-Tree index lookup). Executes in 2ms whether you are on the first page or the 10-millionth record!

---

## 10. Database Sharding & Table Partitioning Strategies

### 1. Previous Knowledge Required
- Horizontal vs Vertical scaling.
- Read-replicas vs Master-writer.

### 2. Logic & Theory (Deep-Dive)

#### Table Partitioning (Single Database Node):
Splits a massive table into smaller physical partitions on the same server:
- **Range Partitioning**: Partition by date (e.g., `loans_2024`, `loans_2025`, `loans_2026`). The query engine uses **Partition Pruning** to scan only the relevant partition.
- **Hash Partitioning**: Distributes rows evenly using `HASH(customer_id)`.

#### Database Sharding (Multiple Independent Database Nodes):
When data exceeds the disk, memory, or CPU of a single physical server:
- Data is horizontally partitioned across multiple database clusters (e.g., Shard 1 for `user_id % 4 == 0`, Shard 2 for `user_id % 4 == 1`, etc.).
- A sharding proxy (e.g., Vitess, Apache ShardingSphere) routes SQL queries to the appropriate shard.

---

*Continue to [Chapter 13: All 57-Page Coding Challenges & Stream API Solutions](file:///Users/apple/Workspace/Home/Javascript/Learning/13_Coding_Challenges_and_Stream_API_Solutions.md)*
