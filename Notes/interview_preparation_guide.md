# Task Manager - Interview Preparation Guide

This guide is tailored directly from your `task-manager` codebase to help you confidently answer interview questions about how you built this project.

## 1. Project Structure & Architecture
**"How is your project structured?"**

*   **Architecture Pattern:** The application follows a classic **Layered (N-Tier) Monolithic Architecture**. 
*   **Framework:** Built using **Java 17** and **Spring Boot 3.5.x**, utilizing Maven for dependency management.
*   **Package Structure:** I organized the code by technical concerns:
    *   **`controllers/`**: Handles incoming HTTP requests and responses (Presentation Layer).
    *   **`services/`**: Contains the core business logic (Business Layer). For example, `TaskService` processes task creation and assignment logic.
    *   **`repositories/`**: Interfaces extending Spring Data JPA for data access (Data Access Layer).
    *   **`entities/`**: JPA domain models mapping directly to database tables.
    *   **`dtos/`**: Data Transfer Objects to ensure we don't expose database entities directly to the client (Security & Decoupling).
    *   **`security/`**: Contains configuration for Spring Security and JWT filters.
    *   **`exception/`**: Global exception handling (e.g., handling `ResourceNotFoundException`).

## 2. Schema and Database Connectivity
**"Tell me about your database schema and how the app connects to the database."**

*   **Database:** I used **MySQL 8.0** as the relational database.
*   **Connectivity:** Connection is managed by Spring Boot's auto-configuration through `application.properties`. I used environment variables for credentials (`${DB_URL}`, `${DB_USER}`, `${DB_PASS}`) making it secure and easy to deploy via Docker.
*   **ORM:** I used **Hibernate** (via Spring Data JPA) for Object-Relational Mapping.
*   **Schema Overview:**
    *   **`users` table:** Mapped to the `User` entity. Stores `id`, `username` (unique), `password` (hashed), and `role` (e.g., USER, ADMIN).
    *   **`tasks` table:** Mapped to the `Task` entity. Stores `id`, `title`, `description`, `status` (Enum: TODO, etc.), and timestamps (`createdAt`, `updatedAt` managed automatically by Hibernate annotations).
    *   **Relationships:** There is a Many-To-One relationship mapped by `@JoinColumn(name = "assigned_to_id")`, linking a Task back to a specific User. To optimize performance, I configured `FetchType.LAZY` so user data is only loaded when explicitly requested.

## 3. Test Cases (Unit Testing)
**"How did you test the application?"**

*   **Testing Philosophy:** I focused heavily on the **Service Layer** using **JUnit 5** and **Mockito**.
*   **Implementation Example (`TaskServiceTest`):**
    *   I mocked dependencies like `TaskRepository` and `UserRepository` using `@Mock` and injected them into `TaskService` using `@InjectMocks`.
    *   **Happy Paths:** I tested that `createTask`, `getTaskById`, and `getAllTasks` behave as expected when data is valid. (e.g., verifying that `taskRepository.save()` is called exactly once).
    *   **Pagination:** Implemented tests to ensure `getAllTasks` correctly handles Spring `Pageable` objects.
    *   **Edge Cases / Exception Handling:** I wrote tests to ensure `ResourceNotFoundException` is correctly thrown during `assignTaskToUser` if either the Task or User ID doesn't exist in the database.

## 4. Authentication and Authorization (Security)
**"How did you implement security steps in your project?"**

*   **Authentication (Who are you?):** I implemented **Token-Based Authentication using JSON Web Tokens (JWT)**.
    *   The application is completely stateless (`SessionCreationPolicy.STATELESS`), making it scalable.
    *   Passwords are never stored in plaintext. I configured a `BCryptPasswordEncoder` bean to securely hash passwords before storing them in the DB.
    *   A custom `JwtAuthenticationFilter` intercepts requests to validate the token before allowing access.
*   **Authorization (What can you do?):** I used Spring Security's `SecurityFilterChain` to enforce Role-Based Access Control (RBAC).
    *   **Public Endpoints:** `/auth/login`, `/auth/register`, and Swagger documentation paths are explicitly opened up using `permitAll()`.
    *   **Role Requirements:** Creating tasks (`POST /tasks/**`) is strictly restricted to users with the `ADMIN` role (`hasRole("ADMIN")`). Reading tasks (`GET /tasks/**`) is open to anyone. Everything else requires standard authentication.

## 5. Docker Implementation
**"Explain your Dockerfiles and how the app is containerized."**

*   **Dockerfile (Multi-Stage Build):**
    *   **Stage 1 (Builder):** I use a heavy Maven image (`maven:3.9.6-eclipse-temurin-17`) to download dependencies (`go-offline` to utilize caching) and compile the `.jar` file. I used `-DskipTests` to speed up the image build process.
    *   **Stage 2 (Runtime):** I use a lightweight JRE image (`eclipse-temurin:17-jre`), taking only the compiled `.jar` from Stage 1. This drastically reduces the final image size and minimizes security vulnerabilities.
*   **Docker Compose:**
    *   I wrote a `docker-compose.yml` file to orchestrate both the Spring Boot app and MySQL database simultaneously.
    *   It defines a `mysql` service, setting root passwords via environment variables, mapping ports, and utilizing **Docker Volumes** (`mysql_data`) so database records survive container restarts.
    *   The `app` service depends on `mysql` (`depends_on`) and injects the Docker network connection string (e.g., `jdbc:mysql://mysql:3306/tasks_db`) directly into the Spring environment.

## 6. What Still Needs Improvement?
**"If you had more time, what would you improve?"**

If asked this question, bring up a few of these advanced topics to show your maturity as a developer:
1.  **Database Migration Tools:** Right now, JPA is set to `spring.jpa.hibernate.ddl-auto=update`. For production, this is dangerous. I would implement **Flyway** or **Liquibase** to manage database schema versioning securely.
2.  **Advanced Security:** Implementing a **Refresh Token** strategy. Currently, if the JWT expires, the user is forcefully logged out. A refresh token would allow silently fetching a new access token for a better user experience. Adding Rate Limiting (e.g., using Bucket4j) to prevent DDoS attacks.
3.  **Caching for Performance:** To speed up repeated read operations (like fetching task lists), I would integrate a caching layer like **Redis**.
4.  **Integration Testing:** While the unit tests with Mockito are solid, I would add full integration tests using **Testcontainers** to spin up a real MySQL database during the build cycle to test end-to-end repository functionality.
5.  **CI/CD Pipeline:** Add a GitHub Actions workflow to automatically run the tests and build the Docker image whenever code is pushed.

## 7. Essential Commands (Git, Docker, Linux)
**"What commands do you use daily for Git, Docker, and Linux?"**

### Git Commands
*   `git status`: Check the current state of the repository, seeing which files are tracked/untracked or modified.
*   `git add .`: Stage all modified and new files for the next commit.
*   `git commit -m "message"`: Commit staged changes with a descriptive message.
*   `git push origin main`: Push local commits to the remote repository's main branch.
*   `git pull origin main`: Fetch and merge changes from the remote repository.
*   `git branch <name>` / `git checkout -b <name>`: Create and switch to a new feature branch.
*   `git log --oneline`: View the commit history compactly.

### Docker Commands
*   `docker build -t task-manager:latest .`: Build a Docker image from the Dockerfile in the current directory and tag it.
*   `docker run -p 8080:8080 task-manager`: Run a container from the image, mapping port 8080.
*   `docker ps` / `docker ps -a`: List currently running containers / all containers (including stopped).
*   `docker stop <container_id>`: Stop a running container gracefully.
*   `docker logs -f <container_id>`: View the logs of a container in real-time.
*   **Docker Compose:**
    *   `docker-compose up -d`: Start all services defined in `docker-compose.yml` in detached mode.
    *   `docker-compose down -v`: Stop and remove all containers, networks, and volumes (useful for clean slates).

### Linux Commands
*   **Navigation & Files:**
    *   `ls -la`: List all files, including hidden (`.env`), with detailed permissions and sizes.
    *   `cd <dir>`: Change the current directory.
    *   `pwd`: Print the present working directory.
    *   `cat filename` / `less filename`: Display the contents of a file (e.g., viewing logs or config files).
    *   `grep "Exception" app.log`: Search for a specific word/pattern inside a file (crucial for debugging logs).
    *   `tail -f app.log`: Output the last few lines of a file and monitor new lines continuously (streaming logs).
*   **System & Processes:**
    *   `ps aux | grep java`: Find running Java processes (like your Spring app).
    *   `kill -9 <PID>`: Forcefully terminate a stuck process.
    *   `top` / `htop`: Monitor system resources (CPU, RAM utilization).
    *   `chmod +x script.sh`: Make a script run/executable by changing its permissions.

## 8. Core Concepts (Enums, EC2, Java Collections)
**"Can you explain Enums, EC2, and Java Collections, and how they relate to your project?"**

### What is an Enum?
*   **Definition:** An `enum` (enumeration) is a special Java type used to define collections of constants. It restricts a variable to have one of only a few predefined values.
*   **Relation to Project:** In this project, I used enums for `TaskStatus` (TODO, IN_PROGRESS, DONE) and `Role` (USER, ADMIN). This ensures type safety and prevents bugs where someone might pass a random string (like "STARTING") instead of a valid status into the database. JPA handles the mapping of these enums to strings in the database using `@Enumerated(EnumType.STRING)`.

### What is AWS EC2?
*   **Definition:** EC2 (Elastic Compute Cloud) is a web service provided by Amazon Web Services (AWS) that allows you to rent a virtual machine (a server) in the cloud. It provides resizable computing capacity.
*   **Relation to Project:** EC2 is the ideal place to deploy this `task-manager` project for production. Instead of running the Docker containers on your local laptop, you would provision an EC2 instance (e.g., running Ubuntu), install Docker on it, and run `docker-compose up -d`. Your application would then be accessible over the internet via the EC2 instance's public IP address.

### What are Java Collections?
*   **Definition:** The Java Collections Framework is an architecture to store and manipulate a group of objects. It provides interfaces (like `List`, `Set`, `Map`) and classes (like `ArrayList`, `HashSet`, `HashMap`) that handle data structures and algorithms (searching, sorting, insertion, manipulation, deletion).
    *   **List:** An ordered collection that allows duplicates (e.g., `ArrayList`).
    *   **Set:** A collection that contains no duplicates (e.g., `HashSet`).
    *   **Map:** An object that maps keys to values; no duplicate keys allowed (e.g., `HashMap`).
*   **Relation to Project:** Collections are heavily used in the service layer. For example, when fetching all tasks (`taskService.getAllTasks()`), the repository returns a `Page<Task>` which internally holds a `List<Task>`. Also, if iterating through tasks to find specific ones, map them to DTOs, or collect unique user assignments, I would utilize Java Streams backed by these Collection structures (e.g., `tasks.stream().map(TaskDto::new).collect(Collectors.toList())`).

## 9. Key Technologies (Core Java, Spring Boot, MongoDB, Postman)
**"Can you explain your experience with Core Java, Spring Boot, MongoDB, and Postman?"**

### Core Java
*   **Definition:** Core Java refers to the fundamental foundational concepts of the Java programming language. It includes Object-Oriented Programming (OOP) principles, Exception Handling, Multithreading, and the Collections Framework.
*   **Relation to Project:** 
    *   **OOP:** The entire project relies on OOP. I used *Encapsulation* by making entity fields `private` and exposing them via `Getter/Setter` methods (using Lombok).
    *   **Exception Handling:** I implemented global exception handling. For instance, when a task is not found, the service throws a custom `ResourceNotFoundException`, which ensures the API returns a clean 404 response rather than crashing.
    *   **Java 8+ Features:** I heavily utilized Java Streams and Lambda expressions to map Entities to DTOs concisely.

### Spring Boot
*   **Definition:** Spring Boot is an extension of the Spring framework that simplifies the setup and development of Java applications. It provides "auto-configuration" to quickly start projects without writing boilerplate XML configurations.
*   **Relation to Project:** Spring Boot is the backbone of this `task-manager` API.
    *   It provided the embedded Tomcat server, meaning I didn't have to manually configure a web server to run the app.
    *   **Dependency Injection (IoC):** I used annotations like `@Service`, `@RestController`, and `@RequiredArgsConstructor` to let Spring automatically manage object creation and inject dependencies (like injecting the `TaskRepository` into the `TaskService`).
    *   **Spring Data JPA:** It eliminated the need to write complex SQL queries by allowing me to interact with the database using simple Java repository interfaces.

### MongoDB vs. MySQL (Relational vs. NoSQL)
*   **Definition:** MongoDB is a NoSQL, document-oriented database that stores data in flexible, JSON-like formats (BSON) rather than rigid tables with rows and columns.
*   **Relation to Project (Hypothetical):** This project uses **MySQL** (a Relational DB) because tasks and users have a clear, structured relationship (A Task belongs to a User). If I were to use **MongoDB**, I would embed the user information directly inside the task document, or link them via document IDs. MongoDB is excellent for projects where the data structure changes frequently or when storing massive amounts of unstructured data (like logs or flexible user profiles), but MySQL is better for enforcing strict data integrity via Foreign Keys.

### Postman
*   **Definition:** Postman is an API platform and testing client used by developers to build, test, and debug RESTful APIs.
*   **Relation to Project:** Postman is the primary tool used to test this application while it was being built locally.
    *   I created HTTP requests (GET, POST, PUT, DELETE) in Postman to hit the controller endpoints (e.g., `POST http://localhost:8080/tasks`).
    *   I used Postman to pass the necessary JSON request bodies (like the `TaskDto` data) when creating a new task.
    *   For the secured endpoints, I used Postman to easily attach the generated **JWT Bearer Token** into the Authorization header to simulate a logged-in user and test Role-Based Access Control (RBAC).

## 10. Networking Basics (OSI Layer, TCP vs UDP)
**"Can you explain the OSI Model and the difference between TCP and UDP? How do they relate to your application?"**

### The OSI Model
*   **Definition:** The Open Systems Interconnection (OSI) model is a conceptual framework used to describe the functions of a networking system. It has 7 layers. From top to bottom:
    1.  **Application:** Network applications (HTTP, FTP, SMTP). **(Where your Spring Boot API lives)**
    2.  **Presentation:** Data formatting, encryption, compression (e.g., JSON parsing, SSL/TLS).
    3.  **Session:** Establishing/terminating connections (e.g., managing the connection between Postman and your API).
    4.  **Transport:** Reliable/Unreliable data transfer (TCP/UDP, Ports). **(Your app uses Port 8080 here)**
    5.  **Network:** Routing data across networks (IP Addressing, Routers).
    6.  **Data Link:** Physical addressing on the local network (MAC Addresses, Switches).
    7.  **Physical:** The physical hardware (Cables, Radio Waves, Bits).
*   **Relation to Project:** Your `task-manager` API operates heavily at the top layers. It receives **Layer 7 (Application)** `HTTP` requests, usually carrying JSON data prepared at **Layer 6 (Presentation)**. It runs on **Layer 4 (Transport)** using a specific port (`8080`) backed by the TCP protocol.

### TCP vs. UDP
*   **TCP (Transmission Control Protocol):**
    *   **Characteristics:** Connection-oriented, reliable, orders packets, error-checked. It performs a "3-way handshake" before sending data to ensure the receiver is ready.
    *   **Relation to Project:** Your entire application relies on TCP. HTTP (which your Spring REST API uses) runs over TCP. This guarantees that when a user creates a task, the JSON data arrives intact and in order; if a packet over the network is lost, TCP automatically requests it to be retransmitted. It also ensures the connection to your MySQL database is stable.
*   **UDP (User Datagram Protocol):**
    *   **Characteristics:** Connectionless, unreliable, fast, no ordering, no error recovery. It "fires and forgets" the data packets.
    *   **Relation to Project:** Your `task-manager` app generally does *not* use UDP. UDP is used for applications where speed is more critical than reliability and minor data loss is acceptable, such as live video streaming (Zoom, Twitch) or fast-paced online gaming. Since your app is a strict database-backed REST API, data integrity (TCP) is infinitely more important than raw streaming speed (UDP).
