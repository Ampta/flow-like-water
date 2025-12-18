# AI Coding Master Playbook: Building Spring Boot Apps

This document contains a **sequenced list of prompts** you can feed to an AI (like me) to build a production-grade application step-by-step.

**Example Project**: Attendance Management System
**Tech Stack**: Spring Boot 3, MongoDB, Spring Security (JWT), Lombok.

---

## Phase 1: The Foundation (Setup & Security)

**Why start here?** Security touches everything. Adding it later breaks code. Build it first.

### Prompt 1: Project Initialization & Dependencies
> "I want to build a Spring Boot 3 application for an **Attendance Management System**.
> Please provide the `pom.xml` file.
> 
> **Requirements:**
> 1. Java 21, Spring Boot 3.3.x.
> 2. Dependencies: Spring Web, Spring Data MongoDB, Spring Security, JWT (jjwt), Lombok, Validation.
> 3. Use Maven."

### Prompt 2: Security Configuration (The Hardest Part)
> "Now, let's implement the Security Layer. I want a production-grade JWT authentication system.
> 
> **Please create the following files in `com.example.attendance.security`:**
> 1. `JwtUtils.java`: Ability to generate (sign) and validate tokens.
> 2. `AuthTokenFilter.java`: A filter that checks the `Authorization: Bearer <token>` header. Use `@RequiredArgsConstructor` for injection.
> 3. `AuthEntryPointJwt.java`: Handle unauthorized errors by returning a proper JSON response (status, error, message), NOT a redirect.
> 4. `WebSecurityConfig.java`:
>    - Disable CSRF.
>    - Set session policy to STATELESS.
>    - Public endpoints: `/api/auth/**`, `/api/docs/**`.
>    - All other endpoints: Authenticated.
>    - Use strict constructor injection (Lombok)."

### Prompt 3: User Model & Auth Logic
> "Create the User model and Auth logic.
> 
> 1. **Model**: `User.java`. Fields: `id` (UUID), `username`, `email`, `password` (hashed), `roles` (Set<String>).
> 2. **Repository**: `UserRepository.java`. Add `Optional<User> findByUsername(String username)` and `Boolean existsByUsername(...)`.
> 3. **Payloads**: Create DTOs `LoginRequest` (username, password) and `SignupRequest` (username, email, password, roles).
> 4. **Controller**: `AuthController.java` with endpoints:
>    - `POST /api/auth/signin`: Returns JWT + User details.
>    - `POST /api/auth/signup`: Registers new user with BCrypt password encoding."

---

## Phase 2: Domain Models (Defining Data)

**Tip:** Since you already have fields written on paper, paste them into the prompt!

### Prompt 4: Creating Core Models
> "Now let's build the business data. Create the MongoDB domain models.
> 
> **1. Employee.java**
> - Fields: `id`, `userId` (link to User), `firstName`, `lastName`, `department`, `designation`, `dateOfJoining`.
> 
> **2. AttendanceRecord.java**
> - Fields: `id`, `employeeId`, `date` (LocalDate), `checkInTime` (LocalDateTime), `checkOutTime` (LocalDateTime), `status` (PRESENT/ABSENT/LEAVE).
> 
> **Requirements:**
> - Use Lombok `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`.
> - Use `@Document(collection = "name")`."

---

## Phase 3: Service Layer (Business Logic)

### Prompt 5: Service Interfaces & Logic
> "Create the Service Layer. I want strict `Service Interface` -> `ServiceImpl` separation.
> 
> **1. EmployeeService**
> - Methods: `createEmployee`, `getProfile`, `updateProfile`.
> - Logic: Ensure `userId` from the token is linked to the Employee profile.
> 
> **2. AttendanceService**
> - Methods: `checkIn(employeeId)`, `checkOut(employeeId)`, `getMonthlyReport(employeeId, month)`.
> - Note: `checkIn` should fail if already checked in today. `checkOut` should calculate total hours."

---

## Phase 4: Controllers (The API)

**Tip:** Always ask for DTOs for inputs, never expose Entities directly in `POST` methods.

### Prompt 6: API Endpoints
> "Create the REST Controllers. Use `@RequiredArgsConstructor` and map requests to Services.
> 
> **1. EmployeeController** (`/api/employee`)
> - `POST /`: Create profile (DTO: `CreateEmployeeRequest`).
> - `GET /me`: Get my profile.
> 
> **2. AttendanceController** (`/api/attendance`)
> - `POST /check-in`: Trigger check-in for current user.
> - `POST /check-out`: Trigger check-out.
> - `GET /report`: Get my attendance records."

---

## Phase 5: "Fixing It" (When things go wrong)

If you get an error, use this prompt format:

### Prompt: Fixing Errors
> "I am getting this error when running the app:
> 
> ```
> [Paste Error Stack Trace Here]
> ```
> 
> Here is my current code for `[Filename.java]`:
> 
> ```java
> [Paste Code Here]
> ```
> 
> Please analyze the error and give me the **corrected code**."

---
