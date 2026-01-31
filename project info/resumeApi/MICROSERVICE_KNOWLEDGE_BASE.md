# MICROSERVICE_KNOWLEDGE_BASE
**Your Technical Interview "Cheat Sheet" & Architectural Deep Dive**

---

## Part 1: The Codebase & Annotations (The "How & Where")

### Package: `com.ampta.resume_api.controller`
**Role**: The Entry Point. These classes handle incoming HTTP requests, validate input, and delegate business logic to Services.

#### `ResumeController.java`
*   **Purpose**: Manages Resume CRUD operations (Create, Read, Update, Delete) and image uploads.
*   **Key Annotations**:
    *   `@RestController`:
        *   **What it does**: Combines `@Controller` and `@ResponseBody`. Tells Spring "This class handles web requests and returns JSON directly," not HTML views.
        *   **Behind the Scenes**: Spring registers this bean in the ApplicationContext. When a request comes in, the `DispatcherServlet` scans these beans to find a matching URL path. It automatically serializes the returned objects (e.g., `Resume`) into JSON using the Jackson library.
    *   `@RequestMapping(RESUME_CONTROLLER)`:
        *   **What it does**: Sets the base URL path for all methods in this class (e.g., `/api/v1/resumes`).
    *   `@RequiredArgsConstructor` (Lombok):
        *   **What it does**: Generates a constructor for all `final` fields (`resumeService`, `fileUploadService`).
        *   **Behind the Scenes**: This is the preferred way to do **Dependency Injection** in modern Spring. Spring sees the constructor and automatically injects the required Service beans at startup.
    *   `@Valid`:
        *   **What it does**: Triggers Jakarta Bean Validation (e.g., checking `@NotNull` in DTOs) before the method runs.
        *   **Behind the Scenes**: If validation fails, Spring throws a `MethodArgumentNotValidException`, which your `GlobalExceptionHandler` catches to return a 400 Bad Request.
*   **Interactions**: Calls `ResumeService` for logic and `FileUploadService` for Cloudinary uploads.

#### `PaymentController.java`
*   **Purpose**: Handles payment processing with Razorpay integration.
*   **Key Annotations**:
    *   `@RequestBody Map<String, String>`:
        *   **What it does**: Maps the JSON body of the request dynamically to a Java Map.
        *   **Behind the Scenes**: Useful when the input doesn't map to a strict DTO or is dynamic. Jackson unmarshals the JSON into a HashMap.
*   **Interactions**: Talks to `PaymentService` which communicates externally with the Razorpay API.

---

### Package: `com.ampta.resume_api.service` & `impl`
**Role**: The Business Logic Layer. Contains the "brains" of the application.

#### `ResumeServiceImpl.java` (Implementation of `ResumeService`)
*   **Purpose**: Contains logic for creating resumes, ensuring users only access their own data, and mapping DTOs.
*   **Key Annotations**:
    *   `@Service`:
        *   **What it does**: Marks this class as a Service component.
        *   **Behind the Scenes**: Specialization of `@Component`. It tells Spring "This bean holds business logic." Spring scans for this during component scanning and instantiates it as a Singleton bean. Use this annotation so you can `@Autowired` it into Controllers.
    *   `@Transactional`: (If used here or on repository methods)
        *   **What it does**: Ensures that a series of database operations either all succeed or all fail (Atomic).
        *   **Behind the Scenes**: Spring creates a **Proxy** around the class. When you call a method, the proxy opens a DB transaction. If an exception is thrown, it rolls back; otherwise, it commits.
*   **Interactions**: Calls `ResumeRepository` (Database) and `Mapper` utilities.

#### `AuthServiceImpl.java`
*   **Purpose**: Handles User Registration, Login, JWT Token generation, and Password Hashing.
*   **Key Concepts**: Uses `PasswordEncoder` (BCrypt) to hash passwords before saving to MongoDB. Never store plain text passwords!

---

### Package: `com.ampta.resume_api.config` & `security`
**Role**: The Configuration & Security Layer.

#### `SecurityConfig.java`
*   **Purpose**: The security "Brain". Defines who can access what and how they log in.
*   **Key Annotations**:
    *   `@EnableWebSecurity`:
        *   **What it does**: Enables Spring Security’s web security support.
    *   `@Bean`:
        *   **What it does**: Manually defines a Bean to be managed by Spring (e.g., `Type: PasswordEncoder`).
        *   **Behind the Scenes**: The method `passwordEncoder()` is executed at startup, and the result (BCrypt instance) is stored in the ApplicationContext context for injection elsewhere.
*   **Critical Logic**:
    *   `sessionCreationPolicy(SessionCreationPolicy.STATELESS)`: Tells Spring NOT to create `HttpSession` cookies. This is mandatory for **JWT (Rest API)** architectures.
    *   `addFilterBefore(jwtAuthenticationFilter, ...)`: Inserts your custom JWT filter before the standard login filter.

#### `JwtAuthenticationFilter.java`
*   **Purpose**: Intercepts *every* request to check for a "Bearer Token" header.
*   **Behind the Scenes**:
    1.  User sends request with Header: `Authorization: Bearer <token>`.
    2.  Filter extracts token using `JwtUtil`.
    3.  If valid, it extracts the username and creates an `Authentication` object.
    4.  It places this object into the `SecurityContextHolder`.
    5.  **Result**: The Controller can now inject `Authentication auth` and know exactly who is calling.

---

### Package: `com.ampta.resume_api.exception`
**Role**: Error Handling.

#### `GlobalExceptionHandler.java`
*   **Purpose**: Catch-all for errors so the API always returns structured JSON, never a generic 500 HTML page.
*   **Key Annotations**:
    *   `@RestControllerAdvice`:
        *   **What it does**: A global "Interceptor" for exceptions thrown by ANY controller.
        *   **Behind the Scenes**: Uses AOP (Aspect Oriented Programming). It wraps your controllers. If an exception bubbles up from a controller, this advice catches it, matches it to an `@ExceptionHandler` method, and returns the custom response.
    *   `@ExceptionHandler(ResourceNotFoundException.class)`: Specifies which specific Java exception triggers this method.

---

## Part 2: Data Architecture & Schema (The "Why")

### Database Choice: MongoDB
**Driven by**: `@Document(collection = "resumes")` in `Resume.java`.

#### Why MongoDB for this Service?
1.  **Hierarchical Data Structure**: A Resume is naturally nested. It contains a User, then a list of `Education`, list of `WorkExperience`, list of `Skills`.
    *   **In SQL (PostgreSQL/MySQL)**: You would need `users` table, `resumes` table, `education` table, `experience` table, `skills` table. To fetch ONE resume, you would need complex `JOIN` queries across 5+ tables.
    *   **In MongoDB**: The entire resume is stored as **one single JSON document**.
    *   **Performance**: Fetching a resume is effectively `O(1)` (Key-Value lookup by ID). No expensive joins.
2.  **Schema Flexibility**: Code changes (e.g., adding a "LinkedIn URL" to `ContactInfo`) don't require database migration scripts (like Flyway/Liquibase). You just add the field in Java.

### Schema Design Rationale

#### `Resume.java` (The Aggregate Root)
*   **Structure**: Supports embedded static classes (`Template`, `ProfileInfo`, `WorkExperience`).
*   **Rationale**: These sub-items have no meaning outside the context of a Resume. A "Work Experience" entry doesn't need to be queried independently.
    *   *Design Pattern*: **Embedding** vs. **Referencing**. We chose Embedding.
    *   *Role Importance*: This ensures atomic writes. When a user updates their resume, they send the whole object. You replace the whole document. No risk of partial updates leaving the resume in a broken state.

#### `User.java`
*   **Structure**: Stores `username`, `email`, `role`, `password`.
*   **Rationale**: standard User collection.
    *   *Why Separate Collection?*: Users exist independently of Resumes. A user might have 0 or 10 resumes. Linking via `userId` allows scalability.

---

## Part 3: Microservice Infrastructure

### Security Architecture (JWT Flow)
The application handles security via **Token-Based Authentication**:
1.  **Login**: User sends `POST /auth/login`. Server verifies password, signs a JWT (JSON Web Token), and returns it.
2.  **Stateless Request**: The Client (React) stores this token (localStorage/cookie) and attaches it to every header: `Authorization: Bearer eyJhbGciOi...`.
3.  **Verification**: The `JwtAuthenticationFilter` intercepts the request. It validates the signature using the `jwt.secret` from `application.properties`. If valid, the request proceeds.
4.  **Protection**: Endpoints like `GET /resumes` are protected. If the token is missing or invalid, Spring Security sends a `403 Forbidden`.

### Docker & Infrastructure Connection
The interactions are defined in `docker-compose.yml` and `application.properties`.

#### 1. Service: `backend` (Spring Boot)
*   **Connection**:
    *   **To Database**: Defined by `SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/resume-api`.
    *   **Explanation**: `mongo` is the **hostname** of the database container in the Docker network. Docker's internal DNS resolves `mongo` to the container's IP address.
*   **Configuration**:
    *   `spring.mail.*`: Injected via Environment Variables for sending verification emails.
    *   `razorpay.*`: Keys for payment gateway integration.

#### 2. Service: `mongo` (Database)
*   **Persistence**:
    *   `volumes: mongo-data:/data/db`.
    *   **Importance**: If you restart the Docker container, the data inside it is lost *unless* you use a Volume. This line maps a folder on your host machine (managed by Docker) to the container's storage, ensuring your user data survives restarts.

#### 3. Networking (`resume-network`)
*   **Role**: A private bridge network.
*   **Why**: It allows the `backend` container to talk to the `mongo` container securely. The generic internet cannot access your MongoDB directly; only services on this network can.

---
**Summary for the Interviewer**:
> "This microservice uses a **Domain-Driven Design**. I chose **MongoDB** because the Resume domain is highly hierarchical and read-heavy, making a Document store more performant than a Relational model involving multiple joins. The architecture is **Stateless**, utilizing **Spring Security with JWT** to ensure horizontal scalability. It is containerized with **Docker**, ensuring consistency across development and production environments."

---

## Bonus: The "Spring Security" Interview Answer

**Interviewer:** "Tell me *how* you secured this application and *why* you chose this approach."

**Your Answer:**
"I implemented **Spring Security** because it is the industry standard for Java applications, offering a robust, adaptable **Filter Chain** architecture.

**The 'Why' (Strategy):**
1.  **Statelessness**: Since this is a REST API with a React frontend, I didn't want server-side sessions (Cookies). I needed a **Stateless** architecture where every request contains its own credentials.
2.  **Granular Control**: I needed to protect specific endpoints (like `GET /resumes`) while leaving others open (like `POST /auth/login`). Spring Security’s `SecurityFilterChain` makes this configuration purely declarative and easy to audit.

**The 'How' (Implementation):**
1.  **JWT (JSON Web Tokens)**: I used JWTs for authentication. When a user logs in, I sign a token containing their ID using a secret key.
2.  **Custom Filter**: I implemented a `JwtAuthenticationFilter` that sits *before* the standard Spring authentication filter. It intercepts every request, checks the `Authorization` header for a 'Bearer token', validates the signature, and sets the `Authentication` object in the `SecurityContext`.
3.  **Configuration**: I created a `SecurityConfig` class annotated with `@EnableWebSecurity` to wire everything together, disabling **CSRF** (since we use JWTs) and setting the session policy to `STATELESS`."

---

## Part 4: Spring Security Keyword Dictionary

For when they ask: *"What exactly does `cors()` or `http` do?"*

### 1. `HttpSecurity http`
*   **What is it?**: The builder object used to configure web-based security for specific HTTP requests.
*   **Why use it?**: It allows you to chain methods together (fluent API) to define rules like "Allow everyone to visit `/login` but only logged-in users to visit `/dashboard`".
*   **Benefit**: Gives you a readable, centralized place to define all network security rules.

### 2. `cors()` (Cross-Origin Resource Sharing)
*   **What is it?**: A mechanism that allows restricted resources on a web page to be requested from another domain.
*   **Why use it?**: By default, browsers block your React app (running on `localhost:5173`) from calling your Spring Boot API (running on `localhost:8080`) because they are different "Origins" (ports).
*   **Benefit**: Enabling CORS (specifically in `corsConfigurationSource()`) whitelists your frontend URL, allowing the two applications to talk to each other without security errors.

### 3. `csrf()` (Cross-Site Request Forgery)
*   **What is it?**: An attack where a malicious site tricks a logged-in user into executing unwanted actions (using their session cookies).
*   **Why disable it? (`.csrf(c -> c.disable())`)**: CSRF protection depends on **Cookies/Sessions**. Since we are using **JWTs** (Stateless), the server doesn't hold a session, so this attack vector is not possible.
*   **Benefit**: Removing unnecessary complexity. If we kept it enabled, we'd have to manage CSRF tokens, which is redundant for stateless APIs.

### 4. `jwt` (JSON Web Token)
*   **What is it?**: A compact, URL-safe means of representing claims to be transferred between two parties.
*   **Why use it?**: It acts as a digital "Passport". When specific users login, you give them a signed JWT. They show this JWT for future requests.
*   **Benefit**: **Statelessness**. The server doesn't need to store "Who is logged in?" in its memory (RAM). It just validates the signature of the incoming token. This allows your service to scale to millions of users easily.

### 5. `authenticated()` vs. `permitAll()`
*   **What is it?**: Access control rules in specific paths.
*   **Why use it?**:
    *   `permitAll()`: Public access. Used for Login/Register (otherwise how would a user ever log in?).
    *   `authenticated()`: Private access. Used for `/payment` or `/profile`. Requires a valid user (token).
*   **Benefit**: Principle of Least Privilege. You lock down the entire application by default and only open specific doors.

### 6. `addFilterBefore(...)`
*   **What is it?**: Inserting your custom code (`JwtAuthenticationFilter`) into Spring Security's standard "Filter Chain".
*   **Why use it?**: Spring doesn't know about JWTs out-of-the-box in its default login flow. We have to tell it: "Hey, before you try to check for a username/password form, check this Header for a Token first."
*   **Benefit**: Seamless integration. Once your filter runs and sets the context, the rest of Spring Security treats the user as a standard logged-in user.
