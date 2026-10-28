# 🗄️ Top 20 JPA, Hibernate & Spring Data JPA Interview Questions & Answers

---

### Q1: What is the relationship and difference between JPA, Hibernate, and Spring Data JPA?
**Answer:**
- **JPA (Jakarta/Java Persistence API):** A **specification** (standard interface and annotation definitions such as `@Entity`, `@Id`, `EntityManager`) defining object-relational mapping (ORM) in Java. It contains no implementation code itself.
- **Hibernate:** The actual **ORM implementation provider** that implements the JPA specification, plus offers proprietary extensions (e.g., Criteria API extensions, multi-tenancy, custom cache providers).
- **Spring Data JPA:** An **abstraction layer** built on top of JPA/Hibernate. It eliminates boilerplate data access code by providing repository interfaces (`JpaRepository`, `CrudRepository`), automatic query generation from method names, and unified pagination/sorting.

```
+-------------------------------------------------------+
|                   Spring Data JPA                     |
|           (Repositories, Method Query Derivation)      |
+-------------------------------------------------------+
                           |
+-------------------------------------------------------+
|                 JPA Specification                     |
|          (EntityManager, Entity Lifecycle)            |
+-------------------------------------------------------+
                           |
+-------------------------------------------------------+
|             Hibernate (ORM Implementation)            |
|       (Session, HQL, Dirty Checking, 1st/2nd Cache)   |
+-------------------------------------------------------+
                           |
+-------------------------------------------------------+
|                    JDBC Driver                        |
+-------------------------------------------------------+
```

---

### Q2: Explain the 4 Entity Lifecycle States in JPA.
**Answer:**
Every JPA entity instance exists in one of four states relative to the `EntityManager` (Hibernate `Session`):

1. **Transient (New):**
   - Instantiated via `new MyEntity()`.
   - Has no database identity (primary key is null or unassigned).
   - Not associated with any Persistence Context / Session.
2. **Persistent (Managed):**
   - Has a database identity and is managed by an active Persistence Context.
   - Any modification to entity fields is tracked via **dirty checking** and automatically synchronized to the DB upon transaction commit.
   - Transition: `entityManager.persist(entity)` or fetched via query/`find()`.
3. **Detached:**
   - Has a database identity, but its Persistence Context has been closed or cleared.
   - Changes made to detached instances are **not** written to the database automatically.
   - Transition: `entityManager.detach(entity)`, `clear()`, `close()`, or transaction end. Re-attach via `entityManager.merge(entity)`.
4. **Removed:**
   - Scheduled for deletion from the database upon transaction commit.
   - Transition: `entityManager.remove(entity)`.

---

### Q3: Explain Hibernate First-Level (L1) vs Second-Level (L2) Cache.
**Answer:**
| Cache Layer | First-Level (L1) Cache | Second-Level (L2) Cache |
| :--- | :--- | :--- |
| **Scope** | Bound to the current `Session` / `EntityManager` | Bound to the `SessionFactory` (Application / Cluster level) |
| **Enablement** | **Always enabled**; cannot be turned off | Disabled by default; configured explicitly (Ehcache, Redis, Hazelcast) |
| **Sharing** | Not thread-safe; never shared between threads/requests | Shared across all sessions, transactions, and user requests |
| **Lifecycle** | Destroyed when session closes | Survives across sessions throughout application lifetime |

**Query Cache:** Caches query parameter combinations and the primary keys of matching entities. Works only when L2 cache is also enabled.

---

### Q4: What is the N+1 Query Problem in JPA, and how do you resolve it?
**Answer:**
The **N+1 Problem** occurs when an application executes 1 initial SQL query to fetch $N$ parent records, and then executes $N$ additional subsequent queries to fetch child relationships for each parent.

*Example:* Fetching 50 `Authors` executes 1 query. Accessing `author.getBooks()` triggers 50 individual queries = $1 + 50 = 51$ queries!

**Solutions:**
1. **`JOIN FETCH` in JPQL:**
```java
@Query("SELECT a FROM Author a JOIN FETCH a.books")
List<Author> findAllWithBooks();
```
2. **`@EntityGraph` (Ad-hoc Fetch Plan):**
```java
@EntityGraph(attributePaths = {"books"})
List<Author> findAll();
```
3. **`@BatchSize` (Batch Loading):**
Hibernate loads children in batches using an `IN (...)` clause instead of 1-by-1:
```java
@OneToMany(mappedBy = "author")
@BatchSize(size = 25)
private List<Book> books;
```
4. **DTO Projections:** Query only required scalar fields directly, bypassing entity graphs entirely.

---

### Q5: Compare Lazy vs Eager Loading and how to prevent `LazyInitializationException`.
**Answer:**
- **`FetchType.EAGER`:** The associated entity/collection is loaded immediately with the parent (via SQL JOIN or immediate secondary query).
- **`FetchType.LAZY`:** The association is replaced with a proxy (ByteBuddy) and loaded only on the first field/method access.

**Default Fetch Types in JPA:**
- `@OneToMany`: `LAZY`
- `@ManyToMany`: `LAZY`
- `@ManyToOne`: `EAGER` *(Best practice: explicitly change to `LAZY`)*
- `@OneToOne`: `EAGER` *(Best practice: explicitly change to `LAZY`)*

**`LazyInitializationException`:**
Occurs when code accesses a lazy-loaded relationship outside an active transaction (after the `Session`/`EntityManager` has closed, e.g., in a Controller or Jackson JSON serialization).

**How to avoid:**
1. Use `JOIN FETCH` or `@EntityGraph` in repository queries.
2. Use DTO projections in the service layer.
3. Keep transactions open using `@Transactional(readOnly = true)` at service boundary.
4. *Avoid:* `spring.jpa.open-in-view=true` (anti-pattern in high-throughput production due to DB connection starvation).

---

### Q6: How do you model `@OneToMany` and `@ManyToOne` relationships properly?
**Answer:**
The bidirectional `@ManyToOne` / `@OneToMany` is the most efficient and common mapping.

**Rules for Correct Modeling:**
1. **Owning Side:** The `@ManyToOne` side is always the owner of the relationship because it holds the foreign key column (`@JoinColumn`).
2. **Inverse Side:** The `@OneToMany` side specifies `mappedBy = "<fieldNameInChild>"`.
3. **Helper Synchronization Methods:** Maintain both sides of bidirectional relationships in memory to prevent state discrepancies.

```java
@Entity
public class Department {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee emp) {
        employees.add(emp);
        emp.setDepartment(this);
    }
    public void removeEmployee(Employee emp) {
        employees.remove(emp);
        emp.setDepartment(null);
    }
}

@Entity
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
```

---

### Q7: Difference between `CascadeType.REMOVE` and `orphanRemoval = true`.
**Answer:**
| Operation | `CascadeType.REMOVE` | `orphanRemoval = true` |
| :--- | :--- | :--- |
| **Parent Entity Deleted** | Deletes all associated children | Deletes all associated children |
| **Child Disconnected from Parent Collection** (`parent.getChildren().remove(child)`) | **Does NOT delete child** from database (foreign key set to null or fails constraint) | **Deletes child row from database** permanently |
| **Concept** | Cascades the `entityManager.remove()` operation | Treats child as a dependent lifecycle component |

*Danger of `CascadeType.ALL`:* Can inadvertently delete or persist vast graphs of records if applied blindly on `@ManyToMany` or shared lookup tables.

---

### Q8: Compare Spring Data JPA Querying Mechanisms.
**Answer:**
1. **Derived Query Methods:**
   - `findByEmailAndStatus(String email, Status status)`
   - *Best for:* Simple queries; type-safe; validated at application startup.
2. **JPQL (`@Query` without `nativeQuery = true`):**
   - `@Query("SELECT u FROM User u WHERE u.email = :email")`
   - *Best for:* Complex joins, database-agnostic entity queries.
3. **Native SQL (`@Query(nativeQuery = true)`):**
   - `@Query(value = "SELECT * FROM users WHERE JSON_EXTRACT(data, '$.active') = true", nativeQuery = true)`
   - *Best for:* Database-specific functions (Postgres JSONB, window functions).
4. **JPA Criteria API / Specifications:**
   - Programmatic, type-safe queries using the Metamodel.
   - *Best for:* Dynamic search filters with optional query parameters (e.g., e-commerce search forms).

---

### Q9: What are Spring Data JPA Projections (DTO vs Interface)?
**Answer:**
Projections allow querying only specific columns rather than full entity tables, drastically saving memory and eliminating N+1 query risks.

**1. Interface-based Closed Projection:**
```java
public interface UserSummary {
    String getUsername();
    String getEmail();
}
// Repository:
List<UserSummary> findByRole(String role);
```
Spring generates a dynamic proxy holding only those two column values.

**2. Class-based / Java Record Constructor Projection (Fastest):**
```java
public record UserDto(String username, String email) {}

// Repository:
@Query("SELECT new com.example.UserDto(u.username, u.email) FROM User u WHERE u.role = :role")
List<UserDto> findUserSummaries(@Param("role") String role);
```
Direct instantiation by Hibernate without proxy overhead.

---

### Q10: How does Spring `@Transactional` work, and what are its Propagation Behaviors?
**Answer:**
Spring wraps `@Transactional` classes/methods with an AOP proxy (`TransactionInterceptor`). It opens a database connection/transaction before method execution, commits upon normal completion, and rolls back on unhandled runtime exceptions (`RuntimeException` and `Error`).

**Propagation Types:**
- **`REQUIRED` (Default):** Joins existing transaction; creates a new one if none exists.
- **`REQUIRES_NEW`:** Always suspends current transaction and creates an independent new transaction.
- **`SUPPORTS`:** Runs within a transaction if one exists; runs non-transactionally otherwise.
- **`NOT_SUPPORTED`:** Suspends current transaction and runs non-transactionally.
- **`MANDATORY`:** Must run within an active transaction; throws exception if none exists.
- **`NEVER`:** Throws exception if active transaction exists.
- **`NESTED`:** Executes within a nested transaction using savepoints.

---

### Q11: Explain Transaction Isolation Levels and Concurrency Anomalies.
**Answer:**
**Concurrency Anomalies:**
1. **Dirty Read:** Transaction A reads uncommitted modifications made by Transaction B.
2. **Non-Repeatable Read:** Transaction A reads a row, Transaction B updates that row and commits; Transaction A re-reads and gets different data.
3. **Phantom Read:** Transaction A reads a range of rows, Transaction B inserts a new row matching the range and commits; Transaction A re-reads and sees the "phantom" row.

**Isolation Levels:**
| Isolation Level | Dirty Read | Non-Repeatable Read | Phantom Read |
| :--- | :---: | :---: | :---: |
| **READ UNCOMMITTED** | Allowed | Allowed | Allowed |
| **READ COMMITTED** (Postgres/Oracle default) | Prevented | Allowed | Allowed |
| **REPEATABLE READ** (MySQL default) | Prevented | Prevented | Allowed |
| **SERIALIZABLE** | Prevented | Prevented | Prevented |

---

### Q12: Why does Self-Invocation break `@Transactional`, and how do you solve it?
**Answer:**
Spring's declarative transaction management relies on **dynamic proxies (CGLIB / JDK Dynamic Proxies)**.
When method `A()` calls method `B()` within the same class (`this.methodB()`), the call bypasses the Spring proxy and invokes the raw target instance directly. Thus, transaction interceptors are never triggered for `B()`.

**Solutions:**
1. **Extract to Separate Service Bean:** Move `methodB()` to a dedicated service class and inject it into the caller. (Cleanest architecture).
2. **Self-Injection:** Inject the service into itself via `@Lazy`:
```java
@Service
public class OrderService {
    @Lazy @Autowired
    private OrderService self;

    public void process() {
        self.saveInNewTransaction(); // Routed through proxy!
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveInNewTransaction() { ... }
}
```
3. **`TransactionTemplate` (Programmatic):**
```java
transactionTemplate.execute(status -> { ... });
```

---

### Q13: Compare Optimistic vs Pessimistic Locking in JPA.
**Answer:**
**1. Optimistic Locking:**
- Assumes data collisions are rare.
- Uses a `@Version` column (number or timestamp).
- When updating, generates: `UPDATE table SET val = ?, version = version + 1 WHERE id = ? AND version = ?`.
- If row was updated by another thread, row count is 0 $\rightarrow$ Hibernate throws `OptimisticLockException`.
- *Best for:* Read-heavy systems with occasional concurrent edits (e.g., editing user profiles).

**2. Pessimistic Locking:**
- Assumes collisions are frequent. Locks the row at the database level using `SELECT ... FOR UPDATE`.
- Other transactions must wait until the lock holder commits or rolls back.
- Annotations: `@Lock(LockModeType.PESSIMISTIC_WRITE)` or `PESSIMISTIC_READ`.
- *Best for:* Financial transactions, inventory decrementing, booking systems where conflicts are high and retries are expensive.

---

### Q14: How does Spring Data JPA Auditing work?
**Answer:**
Automatically populates tracking columns (who created/modified a record and when).

**Setup:**
1. Enable auditing on config: `@EnableJpaAuditing(auditorAwareRef = "auditorProvider")`.
2. Annotate entity or `@MappedSuperclass`:
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditableEntity {
    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}
```
3. Implement `AuditorAware<String>` using `SecurityContextHolder` to supply current user.

---

### Q15: Compare `Page<T>` vs `Slice<T>` vs `List<T>` in Spring Data JPA Pagination.
**Answer:**
- **`Page<T>`:** Executes two SQL queries: the data query (with `LIMIT` and `OFFSET`) AND a `COUNT(*)` query to calculate total elements and total pages.
  - *Performance warning:* `COUNT(*)` on large tables (millions of rows) is extremely slow.
- **`Slice<T>`:** Fetches `pageSize + 1` records. If the extra record exists, `hasNext()` is true.
  - *Zero count query overhead!* Ideal for infinite scrolling on mobile/web feeds.
- **Keyset Pagination (Cursor-based):**
  - Instead of `OFFSET 100000`, uses `WHERE id > :lastSeenId ORDER BY id ASC LIMIT 20`.
  - Retains constant $O(1)$ index seek performance across arbitrarily deep pagination.

---

### Q16: Explain JPA Inheritance Mapping Strategies.
**Answer:**
1. **`InheritanceType.SINGLE_TABLE` (Default):**
   - All classes in hierarchy mapped to one single database table.
   - Includes a discriminator column (`@DiscriminatorColumn(name="dtype")`).
   - *Pros:* Fastest queries (no joins). *Cons:* Subclass-specific columns must be nullable.
2. **`InheritanceType.JOINED`:**
   - Root class has a table; each subclass has its own table containing only unique fields, joined by foreign key.
   - *Pros:* Fully normalized database schema. *Cons:* Queries require expensive `JOIN`s across tables.
3. **`InheritanceType.TABLE_PER_CLASS`:**
   - Each concrete entity maps to its own standalone table containing all fields.
   - *Pros:* Clean separate tables. *Cons:* Polymorphic queries require slow `UNION` operations.
4. **`@MappedSuperclass`:**
   - Not an inheritance hierarchy entity itself. Shares common column definitions (like `id`, `createdAt`) with subclasses. Subclasses have independent tables.

---

### Q17: How do you map Composite Primary Keys in JPA?
**Answer:**
Two standard approaches:

**1. `@EmbeddedId` with `@Embeddable` (Preferred):**
```java
@Embeddable
public class OrderItemId implements Serializable {
    private Long orderId;
    private Long productId;
    // equals() and hashCode() mandatory!
}

@Entity
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;
    private int quantity;
}
```

**2. `@IdClass`:**
- Place `@IdClass(OrderItemId.class)` on entity.
- Repeat fields directly in both the entity and the PK class.
- `@EmbeddedId` is generally preferred because the composite key is encapsulated as an explicit object.

---

### Q18: What are JPA Entity Lifecycle Callbacks and `EntityListeners`?
**Answer:**
JPA allows intercepting entity lifecycle transitions:
- `@PrePersist` / `@PostPersist`: Before / after SQL INSERT.
- `@PreUpdate` / `@PostUpdate`: Before / after SQL UPDATE.
- `@PreRemove` / `@PostRemove`: Before / after SQL DELETE.
- `@PostLoad`: After entity is loaded from database.

**External Listener Example:**
```java
public class EncryptionListener {
    @PrePersist
    @PreUpdate
    public void encryptSensitiveFields(User user) {
        user.setSsn(CryptoUtil.encrypt(user.getSsn()));
    }

    @PostLoad
    public void decryptSensitiveFields(User user) {
        user.setSsn(CryptoUtil.decrypt(user.getSsn()));
    }
}
```

---

### Q19: How does Hibernate Dirty Checking work, and what is `@DynamicUpdate`?
**Answer:**
- **Dirty Checking Mechanism:** When an entity is loaded into the Persistence Context, Hibernate stores a snapshot copy of its initial property values. During transaction flush, Hibernate compares the current state of managed entities against the snapshot. If any property differs, Hibernate automatically schedules an SQL `UPDATE`.
- **Default Update SQL:** By default, Hibernate's prepared `UPDATE` statement includes **all columns**, regardless of which specific field changed.
- **`@DynamicUpdate`:** Tells Hibernate to generate the SQL `UPDATE` statement dynamically at runtime, including only the modified columns.
  - *Trade-off:* Saves network bandwidth and index write overhead for tables with 50+ columns, but prevents database prepared statement caching.

---

### Q20: How do you perform high-performance Batch Inserts and Updates in Hibernate?
**Answer:**
Without proper configuration, inserting 10,000 entities executes 10,000 individual SQL insert statements and overflows the first-level session cache with memory.

**Optimal Batch Processing Configuration:**
1. **Configure Batch Properties:**
```properties
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```
2. **Avoid `GenerationType.IDENTITY`:**
   - `IDENTITY` forces an immediate JDBC insert to fetch generated DB auto-increment keys, which **completely disables batching**.
   - Use `GenerationType.SEQUENCE` with an allocation size matching batch size.
3. **Flush and Clear Persistence Context Periodically:**
```java
@Transactional
public void batchInsert(List<Product> products) {
    for (int i = 0; i < products.size(); i++) {
        entityManager.persist(products.get(i));
        if (i > 0 && i % 50 == 0) {
            entityManager.flush(); // Send batch to DB
            entityManager.clear(); // Free L1 cache memory!
        }
    }
}
```
Alternatively, use Hibernate 6 `StatelessSession` for bulk data operations.

