# Building Task Management REST API

This API provides secure, robust task management capabilities using Spring Boot 3, Java 17, MySQL, and JWT authentication. It follows clean architecture principles with a layered design.

## User Review Required

Does this technical stack, entity configuration, and API structure align perfectly with your expectations? Please let me know if you want any adjustments before I proceed to the EXECUTION phase where I will generate all the code.

## Proposed Changes

### Configuration and Build
#### [NEW] `pom.xml`
Setting up dependencies: Spring Boot Web, Data JPA, Security, Validation, MySQL Driver, JJWT, MapStruct, Lombok, OpenAPI, and Mockito for testing.
#### [NEW] `src/main/resources/application.yml`
Configuration for database connections, Hibernate DDL (update), JWT secret properties, structured logging, and server port.

### Domain / Entities
#### [NEW] `src/main/java/com/taskapp/model/Role.java`
Role enum (`ADMIN`, `USER`).
#### [NEW] `src/main/java/com/taskapp/model/User.java`
User entity with fields `id`, `username`, `password`, `role`.
#### [NEW] `src/main/java/com/taskapp/model/TaskStatus.java`
TaskStatus enum (`TODO`, `IN_PROGRESS`, `DONE`).
#### [NEW] `src/main/java/com/taskapp/model/Task.java`
Task entity with fields `id`, `title`, `description`, `status`, `assignedTo` (ManyToOne mapping to User), `createdAt`, and `updatedAt`.

### Data Access / Repositories
#### [NEW] `src/main/java/com/taskapp/repository/UserRepository.java`
Extends `JpaRepository<User, Long>`. Includes `findByUsername`.
#### [NEW] `src/main/java/com/taskapp/repository/TaskRepository.java`
Extends `JpaRepository<Task, Long>`. Includes pagination queries and finding tasks by assigned user.

### Security layer
#### [NEW] `src/main/java/com/taskapp/security/JwtUtil.java`
Utility for generating, parsing, and validating JWT tokens.
#### [NEW] `src/main/java/com/taskapp/security/CustomUserDetailsService.java`
Loads user data from the database for authentication.
#### [NEW] `src/main/java/com/taskapp/security/JwtAuthenticationFilter.java`
Intercepts requests to validate the JWT in the `Authorization` header.
#### [NEW] `src/main/java/com/taskapp/security/SecurityConfig.java`
Configures Spring Security (disabling CSRF, setting stateless sessions, authorizing `/api/auth/**`, and securing `/api/tasks/**`).

### DTOs
#### [NEW] `src/main/java/com/taskapp/dto/AuthRequest.java` & `AuthResponse.java`
#### [NEW] `src/main/java/com/taskapp/dto/UserDto.java`
#### [NEW] `src/main/java/com/taskapp/dto/TaskDto.java`

### Services
#### [NEW] `src/main/java/com/taskapp/service/UserService.java`
Handles user registration, password encoding, and fetching users.
#### [NEW] `src/main/java/com/taskapp/service/TaskService.java`
Handles creating, updating, assigning, deleting tasks, updating status, and retrieving tasks with pagination/sorting.

### Controllers
#### [NEW] `src/main/java/com/taskapp/controller/AuthController.java`
Exposes `/api/auth/register` and `/api/auth/login`.
#### [NEW] `src/main/java/com/taskapp/controller/TaskController.java`
Exposes `/api/tasks` with standard CRUD endpoints, assignment endpoint, and status update endpoint.

### Exception Handling
#### [NEW] `src/main/java/com/taskapp/exception/GlobalExceptionHandler.java`
Catches exceptions and returns standardized API error responses (with proper validation error handling).
#### [NEW] `src/main/java/com/taskapp/exception/ResourceNotFoundException.java`

### Docker & Extras
#### [NEW] `Dockerfile`
Multi-stage build using Maven and Eclipse Temurin 17.
#### [NEW] `docker-compose.yml`
Spins up both the MySQL database and the Spring Boot application container.

## Verification Plan
### Automated Tests
- I will write unit tests using JUnit 5 and Mockito for the `TaskService`. 
- To run tests: Execute `mvn test` in the terminal to verify the service layer logic, specifically checking pagination, sorting, and task assignment logic.

### Manual Verification
- Execute `docker-compose up -d`
- Ensure that the application establishes a connection with MySQL successfully.
- Go to `http://localhost:8080/swagger-ui.html` to explore the OpenAPI documentation and manually trigger the Auth registration and Task CRUD endpoints.
