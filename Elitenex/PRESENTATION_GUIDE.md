# 🎓 Spring Security & JWT Presentation Master Guide

This master guide provides **simple real-world physical analogies**, **deep architectural breakdowns**, **layer-by-layer request flows**, **answers to random audience questions**, and **impressive presentation nuggets** for your presentation on **Implementing Security in Spring Boot Applications**.

---

## 🏛️ Part 1: Stateful vs Stateless Authentication

### 🍕 Real-World Analogy: Food Court Token (Stateful) vs. Movie Ticket / College ID (Stateless)

#### 1. Stateful Authentication (Session-Based) ──► *The Food Court Token Number*
* **How it works**: You pay at a food court counter and get a receipt token number `#85` (this is the `JSESSIONID` cookie).
* **The Server Overhead**: The kitchen staff MUST maintain a computer record (Server Session Memory) linking `#85` $\rightarrow$ *"Alex, 1 Large Pizza, 1 Coke"*.
* **The Problem**: If 50,000 customers order food, the kitchen computer needs huge memory to store all active orders. If you take Token `#85` to a different restaurant across the street, they have no idea what Token `#85` means unless they check the central cashier database.

#### 2. Stateless Authentication (Token-Based / JWT) ──► *A Printed Movie Ticket with Security Stamp*
* **How it works**: When you buy a movie ticket, everything is printed directly ON the ticket: *"Movie: Avengers, Seat: Row F-12, Date: Today, Time: 7:00 PM, Official Hologram Stamp"* (this is the **JWT Access Token**).
* **Why it's stateless**: The ticket checker at the hall entrance does NOT need to call the central manager or check a computer database. They just read the printed ticket, verify the official stamp, and let you in!
* **The Power**: Any usher at any entrance or snack bar can verify your ticket in 1 second without checking a central database server!

| Feature | Stateful Authentication (Session-Based) | Stateless Authentication (Token-Based / JWT) |
| :--- | :--- | :--- |
| **Real-world Analogy** | Food Court Token (`#85` linked to kitchen computer) | Printed Movie Ticket with Security Stamp |
| **Storage Location** | Server Memory / Redis | Client Side (Header: `Authorization: Bearer <JWT>`) |
| **Server Overhead** | High (Server stores session state for every user) | Low (Zero server session memory required) |
| **Scalability** | Complex (Requires sticky sessions / Redis cluster) | Seamless (Any server instance validates JWT independently) |
| **Revocation** | Instant (Delete session from server memory) | Requires Refresh Tokens or Database Checks |
| **Real-world Uses** | Traditional Web Apps, Monoliths, Online Banking | REST APIs, SPAs (React/Vue/Angular), Mobile Apps, Microservices |

---

## 🔬 Part 2: Deep Dive: How JWT Works in Detail

### 1. What is a JWT?
A **JWT (JSON Web Token)** is a compact, URL-safe token format defined by **RFC 7519**. It is used to securely transfer user claims (identity, roles, expiration) between a client and a server.

---

### 2. Anatomy of a JWT: The 3 Parts (`Header.Payload.Signature`)

A JWT string looks like this:
`eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZW1vQGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIifQ.s9D0xK...`

Notice it has **2 dots (`.`)** dividing the string into **3 distinct parts**:

```
 ┌─────────────────────────┐     ┌─────────────────────────────┐     ┌───────────────────────────────────┐
 │   1. HEADER (Red)       │  .  │   2. PAYLOAD (Green)        │  .  │   3. SIGNATURE (Blue)             │
 │ Meta-data & Algorithm   │     │ Claims (User Email, Roles)  │     │ HMAC-SHA256 Cryptographic Lock    │
 └─────────────────────────┘     └─────────────────────────────┘     └───────────────────────────────────┘
```

#### Part 1: Header (Algorithm & Token Type)
Base64Url encoded JSON containing token metadata:
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

#### Part 2: Payload (Claims / User Info)
Base64Url encoded JSON containing **Claims** (data statements about the user):
```json
{
  "sub": "alex@example.com",
  "role": "USER",
  "iat": 1756910000,
  "exp": 1756910900
}
```
* `sub` (Subject): User's unique identifier (e.g. email).
* `role`: User's permission role in database (`USER` / `ADMIN`).
* `iat` (Issued At): Timestamp when the token was created.
* `exp` (Expiration): Timestamp when the token expires.

#### Part 3: Signature (The Cryptographic Guarantee)
The Signature is created by taking the encoded Header, the encoded Payload, and signing them using a secret key on the server:

$$\text{Signature} = \text{HMAC-SHA256}\Big(\text{Base64Url}(\text{Header}) + \text{"."} + \text{Base64Url}(\text{Payload}), \quad \text{SecretKey}\Big)$$

---

### 3. Step-by-Step: How the Server Verifies a JWT

When a client sends an HTTP Request with `Authorization: Bearer <header>.<payload>.<signature>`:

```
Step 1: Server receives the 3 parts: Header, Payload, and Signature.
Step 2: Server takes Header + "." + Payload from the request.
Step 3: Server calculates ITS OWN Signature using its private SecretKey:
        CalculatedSignature = HMAC-SHA256(Header + "." + Payload, SecretKey)

Step 4: Server compares:
        IF (CalculatedSignature == RequestSignature) ──► Token is 100% AUTHENTIC & UNTAMPERED!
        IF (CalculatedSignature != RequestSignature) ──► TAMPER WARNING! Someone modified the Payload!
```

> 💡 **Why this is tamper-proof**: If a hacker changes `role: "USER"` to `role: "ADMIN"` in the Payload, the Base64 string changes. When the server re-calculates the signature, the signatures won't match, and the request is rejected immediately with `401 Unauthorized`!

---

### 4. Code Connection in Our Simplified `JwtUtils.java`

Our [`JwtUtils.java`](file:///Users/apple/Downloads/User-Auth-Oauth2/src/main/java/com/ampta/userauth/security/jwt/JwtUtils.java) has **only 3 clean methods**:

```java
// 1. SINGLE TOKEN GENERATION METHOD
public String generateToken(String email, String role) {
    return Jwts.builder()
            .subject(email)                         // Payload Claim: sub
            .claim("role", role)                    // Payload Claim: role ("USER" or "ADMIN")
            .issuedAt(new Date())                   // Payload Claim: iat
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // exp
            .signWith(secretKey, Jwts.SIG.HS256)    // Generates HMAC-SHA256 Signature
            .compact();
}

// 2. EXTRACT EMAIL FROM TOKEN
public String getEmailFromToken(String token) {
    return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
}

// 3. VALIDATE TOKEN SIGNATURE AND EXPIRATION
public boolean validateToken(String token) {
    Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    return true;
}
```

---

## 🔐 Part 3: Presentation Masterclass: Refresh Tokens WITH vs WITHOUT Examples

### 🛒 Real-World Scenario: Shopping on Amazon or Flipkart

Imagine you are using the Amazon / Flipkart App on your phone:

---

### ❌ SCENARIO 1: WITHOUT REFRESH TOKENS (Only One Access Token)

#### **Case A: Short Access Token (15 Minutes Expiration)**
* **What happens**: You open the Amazon app at 8:00 PM, search for shoes, add items to your cart, and read reviews.
* At 8:15 PM (15 minutes later), you click **"Proceed to Checkout"**.
* **The Disaster**: Suddenly, the screen freezes and kicks you out to the Login screen: *"Session expired! Enter email and password to log in again."*
* Every 15 minutes while shopping on Amazon/Flipkart, you are forced to re-enter your password! Customers get frustrated and abandon their shopping carts.
* ❌ **Verdict**: Terrible User Experience (UX Nightmare).

#### **Case B: Long Access Token (30 Days Expiration)**
* **What happens**: To fix the bad UX, Amazon makes the Access Token last for 30 days.
* **The Disaster**: You connect to public WiFi at a coffee shop, and a hacker intercepts your Access Token.
* Because the server is **stateless** and does NOT check a database for Access Tokens, the hacker now has full access to your Amazon account (viewing saved addresses, cart, order history) for **30 days**, and Amazon **CANNOT block or revoke the stolen Access Token!**
* ❌ **Verdict**: Severe Security Flaw (Security Nightmare).

---

### ✅ SCENARIO 2: WITH REFRESH TOKENS (Industry Gold Standard used by Amazon / Flipkart)

Amazon/Flipkart uses **Two Tokens working together**:
1. **Access Token (Short-lived: 15 Minutes)** $\rightarrow$ *Fast, Stateless Key used for searching products & adding to cart*
2. **Refresh Token (Long-lived: 7 Days)** $\rightarrow$ *Secure Database-backed Ticket saved in MySQL on `User` record*

#### **How it works step-by-step on Amazon / Flipkart**:
1. **8:00 PM (Login)**: You log into Amazon $\rightarrow$ Receives `accessToken` (15 min) + `refreshToken` (7 days, saved in MySQL database on `User` record).
2. **8:00 - 8:15 PM (Browsing Products)**: You search for items and add them to cart using `accessToken` (`Authorization: Bearer <accessToken>`).
3. **8:15 PM (Access Token Expires)**: Your 15-minute `accessToken` expires!
4. **Silent Background Refresh (Zero Interruption)**:
   - When you click **"Proceed to Checkout"**, the Amazon app receives a `401 Unauthorized`.
   - **Without showing any login screen or popup**, the Amazon app automatically sends a silent background call to `POST /api/v1/auth/refresh` sending `{ refreshToken }`.
   - Amazon's server checks MySQL database: *"Is this Refresh Token valid and not expired?"* $\rightarrow$ YES!
   - Server generates a **NEW Access Token** + rotates the Refresh Token and returns it.
   - Amazon app completes your checkout seamlessly. You never get logged out!
5. **What if your phone is stolen or hacked?**
   - You log into Amazon on your laptop and click **"Sign out of all devices"** $\rightarrow$ Amazon's server executes `user.setRefreshToken(null)` in MySQL.
   - The stolen phone's Refresh Token is **instantly revoked**! The thief is blocked immediately on the next refresh request!

---

### 📊 Summary Comparison Table for Presentation

| Aspect | WITHOUT Refresh Tokens | WITH Refresh Tokens (Amazon / Flipkart) |
| :--- | :--- | :--- |
| **Token Expiry Strategy** | Either 15 min (UX Nightmare) OR 30 Days (Security Risk) | Access Token = 15 Min, Refresh Token = 7 Days |
| **User Experience (UX)** | Annoying login redirects every 15 minutes | Seamless, silent background renewal during checkout |
| **Hacker Damage Window** | Up to 30 days if long token is stolen | Max 15 minutes (short access token) |
| **Revocation Capability** | Impossible (stateless tokens cannot be revoked) | Instant (Server sets `user.refreshToken = null` in DB) |

---

## 🧱 Part 4: Spring Security Architecture: Filters & Layers

### ✈️ Real-World Analogy: Airport Security Checkpoints

Think of an incoming HTTP Request like a passenger at an airport:

```
               HTTP REQUEST (Header: Authorization: Bearer <JWT>)
                                     │
                                     ▼
        ┌─────────────────────────────────────────────────────────┐
        │                 SPRING SECURITY FILTER CHAIN            │
        │                                                         │
        │   ┌─────────────────────────────────────────────────┐   │
        │   │ JwtAuthenticationFilter                         │   │
        │   │  - Officer checks Ticket (Bearer Token)         │   │
        │   │  - Validates signature via JwtUtils             │   │
        │   │  - Stamps forehead (SecurityContextHolder)      │   │
        │   └────────────────────────┬────────────────────────┘   │
        │                            │                            │
        │   ┌────────────────────────▼────────────────────────┐   │
        │   │ UsernamePasswordAuthenticationFilter            │   │
        │   └────────────────────────┬────────────────────────┘   │
        └────────────────────────────┼────────────────────────────┘
                                     │ (Request Authorized)
                                     ▼
        ┌─────────────────────────────────────────────────────────┐
        │                   APPLICATION LAYERS                    │
        │                                                         │
        │   1. CONTROLLER LAYER  (AuthController / UserController)│
        │   2. SERVICE LAYER     (AuthServiceImpl / UserServiceImpl)
        │   3. REPOSITORY LAYER  (UserRepository - JPA Data Access)│
        │   4. DATABASE LAYER    (MySQL Table: users)             │
        └─────────────────────────────────────────────────────────┘
```

---

## 🔄 Part 5: Detailed Step-by-Step Request Flows

### Flow 1: Registration Flow (`POST /api/v1/auth/register`)

```
Client ──► AuthController.registerUser()
               │
               ▼
           AuthServiceImpl.register()
               │
               ├─► UserRepository.existsByEmail() & existsByUsername()
               │   (Rejects request if user already exists)
               │
               ├─► PasswordEncoder.encode(rawPassword)
               │   (Hashes password using BCrypt algorithm)
               │
               ├─► UserRepository.save(user)
               │   (Stores new user in MySQL table with role: USER or ADMIN)
               │
               ▼
Client ◄── Returns 201 Created ("User registered successfully!")
```

---

### Flow 2: Login Flow (`POST /api/v1/auth/login`)

```
Client ──► AuthController.loginUser(email, password)
               │
               ▼
           AuthenticationManager.authenticate()
               │
               ├─► DaoAuthenticationProvider
               │       │
               │       ├─► UserDetailsServiceImpl.loadUserByUsername(email)
               │       │   (Fetches User from DB -> wraps in UserDetailsImpl)
               │       │
               │       └─► PasswordEncoder.matches(rawPassword, encodedPassword)
               │           (Verifies password hash matches)
               │
               ├─► jwtUtils.generateToken(user.email, user.role)
               │   (Creates short-lived JWT Access Token - 15 mins)
               │
               ├─► Generate UUID Refresh Token (7 days expiry)
               │   (Sets user.setRefreshToken(uuid) & user.setRefreshTokenExpiry())
               │   (Saves user in database)
               │
               ▼
Client ◄── Returns 200 OK { accessToken, refreshToken, email, role }
```

---

### Flow 3: Accessing Protected Endpoint (`GET /api/v1/users/me`)

```
Client ──► Request with Header: Authorization: Bearer <accessToken>
               │
               ▼
           JwtAuthenticationFilter (Intercepts Request)
               │
               ├─► jwtUtils.validateToken(token) (Checks signature & expiry)
               ├─► jwtUtils.getEmailFromToken(token) (Extracts subject email)
               ├─► UserDetailsServiceImpl.loadUserByUsername(email)
               │
               ├─► SecurityContextHolder.getContext().setAuthentication(auth)
               │   (Marks request as AUTHENTICATED in thread-local storage)
               │
               ▼
           SecurityConfig checks authorization rules
               │ (Passes: Route requires authenticated user)
               ▼
           UserController.getCurrentUser(@AuthenticationPrincipal userDetails)
               │
               ▼
Client ◄── Returns 200 OK User Profile JSON
```

---

### Flow 4: Token Expiration & Refresh Flow (`POST /api/v1/auth/refresh`)

```
Scenario: Client sends API request with EXPIRED Access Token -> Receives 401 Unauthorized.
Client sends Refresh Request to /api/v1/auth/refresh with { refreshToken: "<uuid>" }.

               │
               ▼
           AuthController.refreshToken()
               │
               ▼
           AuthServiceImpl.refreshToken()
               │
               ├─► UserRepository.findByRefreshToken(token)
               │   (If token not found -> Throws 401 Unauthorized)
               │
               ├─► Check Expiry: user.getRefreshTokenExpiry().isBefore(Instant.now())
               │   │
               │   ├─► IF EXPIRED:
               │   │   user.setRefreshToken(null) -> save in DB -> Throws 401 Unauthorized
               │   │
               │   └─► IF VALID:
               │       1. jwtUtils.generateToken(user.getEmail(), user.getRole())
               │          (Generates NEW short-lived Access Token)
               │       2. Rotate Refresh Token:
               │          user.setRefreshToken(newUUID)
               │          user.setRefreshTokenExpiry(now + 7 days)
               │          userRepository.save(user)
               │
               ▼
Client ◄── Returns 200 OK { new accessToken, new refreshToken }
```

---

## 💻 Part 6: How Automatic Token Refresh Works on the Frontend vs. Postman Demo

### 1. How Frontend Apps (React / Angular / Mobile) Do It Automatically

In real-world applications (like Amazon or Flipkart), the frontend code uses an **Axios Interceptor** (or HTTP Interceptor) that acts like a network middleman:

```javascript
// Axios Response Interceptor (Frontend Code)
axios.interceptors.response.use(
  (response) => response, // If request succeeds (200 OK), pass through!
  async (error) => {
    const originalRequest = error.config;

    // If server returns 401 Unauthorized (Access Token Expired)
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      // 1. Silently call POST /api/v1/auth/refresh in the background
      const res = await axios.post('/api/v1/auth/refresh', {
        refreshToken: getStoredRefreshToken()
      });

      // 2. Save the NEW accessToken
      saveAccessToken(res.data.accessToken);

      // 3. Update the Authorization header with the NEW accessToken
      originalRequest.headers['Authorization'] = 'Bearer ' + res.data.accessToken;

      // 4. Automatically RETRY the original failed API call!
      return axios(originalRequest);
    }

    return Promise.reject(error);
  }
);
```

---

### 2. How to Demonstrate This Live in Postman (Without Any Frontend Code!)

During your backend presentation, you do **NOT need any frontend code**! You can demonstrate the exact same flow live in Postman in 6 simple steps:

1. **Step 1: Register** $\rightarrow$ `POST /api/v1/auth/register`
2. **Step 2: Login** $\rightarrow$ `POST /api/v1/auth/login`
   - Show the response containing `accessToken` and `refreshToken`.
3. **Step 3: Access Profile** $\rightarrow$ `GET /api/v1/users/me` with header `Authorization: Bearer <accessToken>`
   - Returns `200 OK` User Profile.
4. **Step 4: Simulate Token Expiry**
   - Tell the audience: *"In production, access tokens expire in 15 minutes. Let's see what happens when the access token is invalid or expired."*
   - Change 1 character in the Access Token header $\rightarrow$ Send request $\rightarrow$ Receives `401 Unauthorized`!
5. **Step 5: Call Refresh Endpoint** $\rightarrow$ `POST /api/v1/auth/refresh` with `{ "refreshToken": "<refreshToken>" }`
   - Point out receiving a **brand new `accessToken`** and rotated `refreshToken`!
6. **Step 6: Access Profile Again** $\rightarrow$ `GET /api/v1/users/me` with the **NEW `accessToken`**
   - Returns `200 OK` User Profile!
7. **Step 7: Demonstrate Revocation / Logout** $\rightarrow$ `POST /api/v1/auth/logout`
   - Now try `POST /api/v1/auth/refresh` again $\rightarrow$ Receives `401 Unauthorized`! Explains how revoking the token in the database stops any future refresh attempts!

---

## 🛠️ Part 7: Step-by-Step Implementation Roadmap (1st File to Last File)

```
1. User.java                          ---> (Database Entity with USER / ADMIN role)
2. UserRepository.java                ---> (Data Access Layer)
3. DTOs                               ---> (Request/Response Models)
4. UserDetailsImpl.java               ---> (Spring Security Principal)
5. UserDetailsServiceImpl.java        ---> (User Loader)
6. JwtUtils.java                      ---> (JWT Generator & Validator: generateToken, getEmailFromToken, validateToken)
7. JwtAuthenticationFilter.java       ---> (Request Interceptor)
8. AuthEntryPointJwt.java             ---> (401 Error Handler)
9. SecurityConfig.java               ---> (Security Filter Chain)
10. AuthService / AuthServiceImpl     ---> (Business Logic)
11. AuthController & UserController   ---> (REST Endpoints)
```

---

## 🌟 Part 8: Cool & Smart Presentation Nuggets (Impress Your Audience!)

Teach these 5 "mind-blowing" insights during your presentation to look like a Security Pro:

### 1. 💡 Decoding JWT Live on `jwt.io` (Encoded vs Encrypted)
- **Concept**: Most beginners think JWTs are encrypted secrets.
- **The Smart Insight**: *"A JWT is NOT encrypted by default — it is simply Base64URL encoded! Anyone can read the payload."*
- **Presentation Trick**: Copy your generated token from Postman, paste it into `jwt.io`, and show the audience their email and role inside the payload. Then explain: *"The security comes from the THIRD part — the HMAC SHA-256 Signature. If a hacker alters the role to 'ADMIN', the signature check fails!"*

### 2. 🛡️ Why `Bearer <token>` Header Prevents CSRF Attacks Automatically
- **Real-World Analogy**: **Automatic Mailbox vs. Sealed Hand-Delivered Letter**
- **The Smart Insight**: *"Browsers automatically attach cookies to every outgoing request to a domain (making traditional cookie apps vulnerable to Cross-Site Request Forgery / CSRF). But browsers NEVER automatically attach custom `Authorization: Bearer` headers across origins. Stateless JWT headers make our API immune to CSRF by default!"*

### 3. ⏱️ BCrypt Work Factor (Why Password Hashing is Intentionally Slow)
- **Real-World Analogy**: **Custom Combination Vaults vs. Simple Padlocks**
- **The Smart Insight**: *"Fast hash functions like MD5 or SHA-256 can compute 10 BILLION hashes per second on a modern GPU. BCrypt uses a Work Factor (cost factor = 10) that forces the CPU to run 1024 hashing rounds per password, taking ~100ms. 100ms is unnoticeable for 1 logging-in user, but for a hacker guessing 1 billion combinations, it turns 2 seconds of cracking into 300+ years!"*

### 4. 🔮 Method-Level Security (`@PreAuthorize`)
- **Concept**: Going beyond URL matching in `SecurityConfig`.
- **The Smart Insight**: Show them how clean Spring Security annotations are:
  ```java
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable Long id) { ... }
  ```

### 5. 🧵 ThreadLocal SecurityContext & Magic Annotations
- **Concept**: How `@AuthenticationPrincipal` works.
- **The Smart Insight**: Explain how Spring Security binds the authenticated user to the current executing Java thread (`ThreadLocal`). That's why `@AuthenticationPrincipal UserDetails userDetails` works in any controller without manual parameter passing!

---

## 🚨 Part 9: Token Rotation & Logout Mechanics (Stolen Key Analogy)

### 🚨 What is Token Rotation?
* When a user calls `/api/v1/auth/refresh`, reception doesn't just renew the keycard — they also issue a **new Refresh Token UUID** and overwrite the old one in MySQL.
* **The Thief Trap**: If a thief stole an old refresh token and tries to use it 5 minutes later, the server checks `userRepository.findByRefreshToken(token)`. Because the token was already rotated, the query returns empty! The server detects an attack and returns `401 Unauthorized`.

### 🚪 What Happens During Logout?
* When the user clicks **Logout**, `authService.logout(request)` executes:
  ```java
  user.setRefreshToken(null);
  user.setRefreshTokenExpiry(null);
  userRepository.save(user);
  ```
* The front desk permanently deletes the refresh key. Even if a thief has the refresh token string, future refresh attempts are immediately blocked with `401 Unauthorized`.

---

## ❓ Part 10: Presentation FAQ / Random Questions & Answers Cheat Sheet

### Q1: Why use `OncePerRequestFilter` instead of standard `GenericFilterBean` or `Filter`?
> **Answer**: In Spring applications, an HTTP request can trigger internal dispatches (forward/include). Standard filters can execute multiple times per request. `OncePerRequestFilter` guarantees that `JwtAuthenticationFilter` executes **exactly once per HTTP request**.

### Q2: Why encode passwords with BCrypt? Why not SHA-256 or MD5?
> **Answer**: MD5 and SHA-256 are fast hash functions prone to rainbow table attacks. **BCrypt** is a slow, adaptive cryptographic hashing algorithm that includes an automatic **random salt** and configurable work factor (cost), making brute-force and rainbow table attacks computationally infeasible.

### Q3: What is `SecurityContextHolder` and how does it work?
> **Answer**: `SecurityContextHolder` is Spring Security's central storage where authentication details of the current user are stored. By default, it uses a `ThreadLocal` strategy, meaning the authentication context is bound to the current executing thread handling the HTTP request.

### Q4: What happens if an Access Token is stolen?
> **Answer**: Because Access Tokens are short-lived (e.g. 15 minutes), the window of potential compromise is limited. The attacker loses access as soon as the token expires in 15 minutes.

### Q5: What happens if a Refresh Token is stolen?
> **Answer**: Because Refresh Tokens are stored in our database, the server administrator or user can **instantly revoke** the refresh token by setting `user.refreshToken = null` or calling the `/logout` endpoint. Additionally, our **Token Rotation** policy ensures that using an old refresh token invalidates access immediately.

### Q6: Why set `SessionCreationPolicy.STATELESS`?
> **Answer**: By default, Spring Security creates an HTTP `HttpSession` on the server. Setting `SessionCreationPolicy.STATELESS` instructs Spring Security never to create or use HTTP sessions, ensuring pure stateless REST API token behavior.
