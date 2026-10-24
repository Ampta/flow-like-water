# Chapter 10: Spring Security, JWT & Enterprise Auth Architecture

This guide covers real-world enterprise security questions asked across **Infibeam, Transbnk, Axis Bank, Team Computers, Fable Fintech, C2L, In Solutions Global, and Rebit (RBI)**.

---

## 📑 Table of Contents
1. [Spring Security Architecture & Security Filter Chain](#1-spring-security-architecture--security-filter-chain)
2. [Deep-Dive into JWT (JSON Web Token) Structure & Security](#2-deep-dive-into-jwt-json-web-token-structure--security)
3. [Hashing vs Encryption & BCrypt Password Verification Mechanics](#3-hashing-vs-encryption--bcrypt-password-verification-mechanics)
4. [Protecting Compromised JWTs & Refresh Token Rotation Architecture](#4-protecting-compromised-jwts--refresh-token-rotation-architecture)
5. [CSRF (Cross-Site Request Forgery): Theory, Attack Vectors & Disabling in REST APIs](#5-csrf-cross-site-request-forgery-theory-attack-vectors--disabling-in-rest-apis)
6. [Role-Based Access Control (RBAC) & Method-Level Security](#6-role-based-access-control-rbac--method-level-security)
7. [Securing Sensitive Data: Storing Aadhaar & PAN Card in Cloud/Database](#7-securing-sensitive-data-storing-aadhaar--pan-card-in-clouddatabase)

---

## 1. Spring Security Architecture & Security Filter Chain

### 1. Previous Knowledge Required
- **Servlet Filters**: Intercepts HTTP requests and responses before they reach the `DispatcherServlet`.
- **Authentication** (Who you are) vs **Authorization** (What you are permitted to do).

### 2. Logic & Theory (Deep-Dive)

```
Incoming HTTP Request
        |
        v
[DelegatingFilterProxy] (Standard Servlet Filter in web.xml / Spring Boot)
        |
        v
[FilterChainProxy] (Spring-managed Bean holding SecurityFilterChain)
        |
        +---> [SecurityContextPersistenceFilter]
        |
        +---> [HeaderWriterFilter]
        |
        +---> [CorsFilter / CsrfFilter]
        |
        +---> [Custom JwtAuthenticationFilter (OncePerRequestFilter)]
        |          |--> Extracts Bearer Token from "Authorization" header
        |          |--> Validates Signature & Expiration via Secret/Public Key
        |          |--> Creates UsernamePasswordAuthenticationToken
        |          |--> Sets Authentication in SecurityContextHolder
        |
        +---> [UsernamePasswordAuthenticationFilter]
        |
        +---> [ExceptionTranslationFilter] (Translates AccessDeniedException -> 403 / 401)
        |
        +---> [AuthorizationFilter] (Enforces requestMatchers permits/roles)
        |
        v
   [DispatcherServlet] ---> [@RestController]
```

#### Core Components in Spring Security:
1. **`SecurityContextHolder`**: Holds the thread-local security context of the current request (`SecurityContextHolder.getContext().getAuthentication()`).
2. **`AuthenticationManager`**: The primary coordinator interface that receives an unauthenticated token and authenticates it via available providers.
3. **`DaoAuthenticationProvider`**: Standard provider that fetches credentials via `UserDetailsService` and compares passwords via `PasswordEncoder`.

### 3. How to Answer in an Interview
> *"Spring Security is essentially a chain of ordered servlet filters orchestrated by `FilterChainProxy`. Requests first pass through standard filters, then hit custom authentication filters like our `JwtAuthenticationFilter`.*
>
> *Our JWT filter parses the Bearer token, validates its signature, extracts user claims and granted authorities, constructs an `Authentication` object, and sets it in the `SecurityContextHolder`. Downstream authorization filters then evaluate whether the user has the required roles before dispatching the request to the controller."*

### 4. Practical Code Example
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enables @PreAuthorize
public class EnterpriseSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public EnterpriseSecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                                    AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Disabled for stateless REST APIs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/actuator/health").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

---

## 2. Deep-Dive into JWT (JSON Web Token) Structure & Security

### 1. Previous Knowledge Required
- Base64URL encoding (encoding binary/text data into URL-safe ASCII characters).
- Digital signatures (symmetric HMAC-SHA256 vs asymmetric RSA/ECDSA).

### 2. Logic & Theory (Deep-Dive)

A JWT consists of 3 distinct parts separated by dots (`.`):
```
[Header].[Payload].[Signature]
eyJhbGciOi... . eyJzdWIiOiIx... . SflKxwRJSMeKKF2Q...
```

1. **Header**: JSON metadata describing token type (`JWT`) and signing algorithm (`HS256` or `RS256`), Base64URL-encoded.
2. **Payload (Claims)**: JSON object containing user identity, roles, issued-at time (`iat`), and expiration (`exp`), Base64URL-encoded.
   - **CRITICAL INTERVIEW TRAP**: *Is the JWT payload encrypted?* **NO! It is Base64URL ENCODED, not encrypted.** Anyone who intercepts the token can paste it into `jwt.io` and read all claims in plain text! Therefore, **NEVER put passwords, PAN numbers, or credit card numbers in the JWT payload.**
3. **Signature**: Cryptographic proof of authenticity:
   $$\text{Signature} = \text{HMAC-SHA256}(\text{Base64Url}(H) + "." + \text{Base64Url}(P), \text{SecretKey})$$
   - The signature ensures **Integrity** and **Tamper-Resistance**. If an attacker tampers with the user ID in the payload, the recalculated signature will mismatch the token signature, causing the server to reject it immediately.

### 3. How to Answer in an Interview
> *"A JWT consists of three parts: Header, Payload, and Signature, separated by dots. The header declares the algorithm; the payload contains user claims and expiration.*
>
> *A very common misconception is that JWT encrypts data. It does NOT. The header and payload are merely Base64Url encoded—meaning anyone can read them. What provides security is the Signature: the server hashes the header and payload using a secret key (or private RSA key). If a user attempts to elevate their privileges or tamper with the payload, the signature verification fails instantly."*

### 4. Practical Code Example
```java
@Component
public class JwtService {

    private final Key signingKey = Keys.hmacShaKeyFor(
        Decoders.BASE64.decode("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970")
    );

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).toList());

        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 minutes validity
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
```

---

## 3. Hashing vs Encryption & BCrypt Password Verification Mechanics

### 1. Previous Knowledge Required
- Cryptographic hash functions (one-way mathematical algorithms: SHA-256, BCrypt, Argon2).
- Symmetric vs Asymmetric Encryption (AES vs RSA).

### 2. Logic & Theory (Deep-Dive)

| Criterion | Hashing (e.g., BCrypt) | Encryption (e.g., AES-256) |
| :--- | :--- | :--- |
| **Direction** | **One-way function**: irreversible by design. | **Two-way function**: plaintext can be decrypted back with key. |
| **Key Requirement** | No decryption key exists. | Requires encryption and decryption keys. |
| **Primary Purpose** | Password storage, data integrity verification. | Confidentiality of data at rest and in transit. |

#### Interview Trap (Asked by Nimap Infotech & Applied Cloud):
*"When verifying passwords with BCrypt during login, are you unhashing the stored password?"*
- **Answer: NO! Hashing cannot be undone or 'unhashed'.**
- BCrypt generates a hash with a randomly generated **Salt** embedded directly into the hash string:
  ```
  $2a$10$N9qo8uLOickgx2ZMRZoMye.I...
   ^^ ^^ ^^^^^^^^^^^^^^^^^^^^^
   |  |  Salt (22 chars) + Hash (31 chars)
   |  Cost Factor (10 rounds = 2^10 iterations)
   BCrypt algorithm identifier
  ```
- During login, BCrypt extracts the salt from the stored DB hash, salts the incoming plaintext password, computes the hash using the identical cost factor, and performs a **constant-time byte comparison** between the two hashes!

### 3. How to Answer in an Interview
> *"Hashing is strictly a one-way mathematical transformation; it cannot be decrypted or 'unhashed'. When storing passwords, we use adaptive salted hashing like BCrypt.*
>
> *During login, we do not unhash the password. Instead, BCrypt reads the salt stored within the DB hash, computes a new hash from the user's incoming plaintext password using that same salt, and checks if the calculated hash matches the stored hash in constant time to prevent timing attacks."*

### 4. Practical Code Example
```java
@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12); // Cost factor 12
    private final UserRepository userRepository;

    public void registerUser(String username, String rawPassword) {
        // Generates random salt, hashes password with 2^12 iterations
        String secureHash = passwordEncoder.encode(rawPassword);
        userRepository.save(new UserEntity(username, secureHash));
    }

    public boolean verifyLogin(String rawPassword, String storedHash) {
        // Internally extracts salt from storedHash, hashes rawPassword, and compares
        return passwordEncoder.matches(rawPassword, storedHash);
    }
}
```

---

## 4. Protecting Compromised JWTs & Refresh Token Rotation Architecture

### 1. Previous Knowledge Required
- Stateless tokens vs Stateful session storage.
- HTTP-only Secure SameSite Cookies vs LocalStorage.

### 2. Logic & Theory (Deep-Dive)

#### The Problem: What if a Hacker Steals an Access Token?
Because JWTs are stateless, the backend does not check a database on every call. If an attacker intercepts an access token valid for 24 hours, they can impersonate the victim until it expires.

#### Enterprise Solution: Dual-Token Architecture + Rotation in Redis

```
+-----------+                +-----------------------+              +---------------+
|  Client   |                |   Spring Boot API     |              | Redis Cache   |
+-----------+                +-----------------------+              +---------------+
      |                                  |                                  |
      | 1. POST /login                   |                                  |
      |--------------------------------->| Validate creds                   |
      |                                  | Generate AccessToken (15m)       |
      |                                  | Generate RefreshToken (7 days)   |
      |                                  | Save RefreshToken in Redis       |
      |                                  |--------------------------------->|
      | 2. Returns AccessToken + Cookie  |                                  |
      |<---------------------------------|                                  |
      |                                  |                                  |
      | 3. AccessToken Expired (401)     |                                  |
      | 4. POST /auth/refresh (Cookie)   |                                  |
      |--------------------------------->| Check RefreshToken in Redis      |
      |                                  |--------------------------------->|
      |                                  | Match OK -> Invalidate Old Token |
      |                                  | Generate Brand New Pair          |
      | 5. New Token Pair Returned       |                                  |
      |<---------------------------------|                                  |
```

1. **Short Access Token Lifespan**: 10 to 15 minutes max.
2. **Refresh Token in HttpOnly Cookie**: JavaScript cannot access HttpOnly cookies, neutralizing XSS token theft.
3. **Refresh Token Rotation (RTR)**: Every time a refresh token is used, it is revoked and replaced with a new one. If an old refresh token is reused, the server detects token theft and immediately revokes all sessions for that user family.
4. **Redis Blacklist for Logout**: Store revoked access tokens in Redis with a TTL equal to their remaining expiration time.

### 3. How to Answer in an Interview
> *"To mitigate compromised JWT tokens, we never issue long-lived access tokens. We use a dual-token architecture: a short-lived access token valid for 10-15 minutes, and a secure refresh token valid for 7 days.*
>
> *We store refresh tokens server-side in Redis and deliver them via HttpOnly, Secure, SameSite=Strict cookies to eliminate XSS theft. We also enforce Refresh Token Rotation (RTR): each token can be used once. If a token is reused, we detect replay attacks and invalidate all tokens for that user immediately."*

---

## 5. CSRF (Cross-Site Request Forgery): Theory, Attack Vectors & Disabling in REST APIs

### 1. Previous Knowledge Required
- Browser automatic cookie attachment behavior across domains.
- Same-Origin Policy (SOP).

### 2. Logic & Theory (Deep-Dive)
- **What is CSRF?** A malicious website tricks a user's browser into executing unwanted actions on a trusted site where the user is currently authenticated via session cookies.
- **Why REST APIs with JWT disable CSRF (`csrf.disable()`)?**
  - CSRF attacks rely strictly on the **browser automatically sending credentials (cookies)** with cross-origin requests.
  - In a standard JWT architecture, the token is stored in memory and sent explicitly inside the `Authorization: Bearer <token>` HTTP header.
  - Browsers do **not** automatically attach custom headers to cross-site requests. Therefore, headless REST APIs using custom Authorization headers are inherently immune to CSRF, making `csrf.disable()` both safe and standard practice.

### 3. How to Answer in an Interview
> *"CSRF occurs when a malicious site tricks a logged-in user's browser into sending unauthorized requests to a target website that authenticates via automatically sent cookies.*
>
> *In modern Spring Boot REST APIs using JWT authentication, the client sends the token explicitly inside the `Authorization: Bearer` header. Because browsers never automatically attach custom authorization headers to cross-origin requests, JWT-based REST APIs are inherently protected against CSRF, allowing us to safely call `http.csrf().disable()`."*

---

## 6. Role-Based Access Control (RBAC) & Method-Level Security

### 1. Previous Knowledge Required
- Spring Security `GrantedAuthority` interface.
- Spring Expression Language (SpEL).

### 2. Logic & Theory (Deep-Dive)
- Spring distinguishes between **Authorities** (fine-grained permissions: `OP_READ_LOAN`, `OP_APPROVE_LOAN`) and **Roles** (high-level group of authorities: `ROLE_ADMIN`, `ROLE_CREDIT_OFFICER`).
- In Spring Security, `hasRole("ADMIN")` automatically expects the prefix `ROLE_` in the granted authority list (`ROLE_ADMIN`).

### 3. Practical Code Example
```java
@RestController
@RequestMapping("/api/v1/loans")
public class LoanApprovalController {

    // Only Users with ROLE_CREDIT_OFFICER or ROLE_ADMIN can execute
    @PreAuthorize("hasAnyRole('CREDIT_OFFICER', 'ADMIN')")
    @PostMapping("/{loanId}/approve")
    public ResponseEntity<String> approveLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok("Loan " + loanId + " successfully approved.");
    }

    // Dynamic authorization checking current authenticated user ID against target resource
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    @GetMapping("/users/{userId}/documents")
    public ResponseEntity<List<DocumentDTO>> fetchUserDocuments(@PathVariable Long userId) {
        return ResponseEntity.ok(documentService.getUserDocs(userId));
    }
}
```

---

## 7. Securing Sensitive Data: Storing Aadhaar & PAN Card in Cloud/Database

### 1. Previous Knowledge Required
- Indian Regulatory Guidelines (RBI / UIDAI / IT Act compliance).
- Field-Level Encryption (FLE) vs Transparent Data Encryption (TDE).
- S3 / Cloud Bucket Server-Side Encryption (SSE-KMS) and Pre-Signed URLs.

### 2. Logic & Theory (Deep-Dive)

#### Interview Question (Asked by Transbnk, Axis Bank, NuSummit):
*"How will you store sensitive documents (Aadhaar/PAN) in the cloud? Will you hash them? What naming conventions? What if someone tampers with the document on the cloud?"*

```
User Uploads Aadhaar
        |
        v
[Loan Origination Service]
        |
        +---> Compute SHA-256 Checksum / Hash of Document Content
        |---> Upload Encrypted File to S3 (SSE-KMS with Customer Master Key)
        |     Key Name: /kyc/tenant-01/{UUIDv4}.enc (No PII in filename!)
        |
        +---> Write Metadata to MySQL (Field-Level Encrypted):
              - customer_id: 10492
              - s3_bucket_path: "/kyc/tenant-01/{UUIDv4}.enc"
              - document_sha256: "e3b0c44298fc1c149afbf4c8996fb92427ae41e4..."
              - aadhaar_last4: "9042" (masked)
              - aadhaar_vault_token: "tok_8f93e..." (encrypted via HashiCorp Vault)
```

1. **Hashing vs Encryption**:
   - **Do we hash the document?** You compute the cryptographic **SHA-256 checksum (hash)** of the document bytes and store it in your database. When downloading, recalculate the hash; if the hash mismatches, tampering has occurred!
   - **Do we hash the document content itself for storage?** NO! Hashing is one-way. If you hashed the PDF, you could never view the customer's Aadhaar card again! The PDF file itself must be **Encrypted** (AES-256 via AWS KMS).
2. **Cloud Storage & Naming Conventions**:
   - **Never include PII** (e.g., `saikiran_aadhaar_123456789012.pdf`) in bucket paths.
   - Use randomized, unguessable UUIDs with bucket partitioning:
     `s3://bank-kyc-vault/loans/2026/10/d3b07384-d113-4632-9c91-e408ec2be85d.dat`
3. **Addressing the Transbnk Tampering Scenario**:
   *"What if someone tampers with the file in cloud storage, replacing the photo while keeping the same filename?"*
   - **Solution 1: S3 Object Lock & Versioning**: Enable WORM (Write Once, Read Many). Objects cannot be overwritten or altered by anyone.
   - **Solution 2: Digital Signature & Checksum Verification**: When the document is originally uploaded, the backend stores its `SHA-256` digest in the database. During download, the system computes the hash of the downloaded stream and compares it with the database record. If a single pixel was changed, the digest fails and the document is quarantined.

### 3. How to Answer in an Interview
> *"For banking documents like Aadhaar and PAN, compliance mandates two layers of protection: Data at Rest Encryption and Tamper Verification.*
>
> *We encrypt the document in cloud storage using envelope encryption with AWS KMS (AES-256). For filenames, we never use customer names or Aadhaar numbers; we use randomized UUIDs partitioned by year/month.*
>
> *To detect tampering, we compute the SHA-256 checksum upon upload and store it in our database alongside S3 Object Versioning. When an officer accesses the file, our service downloads the file, recomputes the SHA-256 hash, and verifies it against the original hash. If someone edited the photo in the bucket, the hashes mismatch and our system blocks access immediately."*

---

*Continue to [Chapter 11: Microservices, Distributed Systems & System Design](file:///Users/apple/Workspace/Home/Javascript/Learning/11_Microservices_Cloud_and_Distributed_Systems.md)*
