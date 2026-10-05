# 🚀 Enterprise RFQ System: The Zero-to-Hero Master Technical & Defense Guide
### *Complete Codebase Logic, Architecture, Code Snippets, UI Components & Live Defense Presentation Guide*

---

## 📖 Welcome: What Is This Application? (Beginner-Friendly 101)

Imagine a large manufacturing company (like Masstech) needs to purchase 500 Industrial Valves and 1,000 Steel Pipes.
Instead of emailing suppliers one by one, they use this **RFQ (Request For Quotation) ERP System**:

1. **Procurement Admin (Company)** creates a **Request For Quotation (RFQ)** with required items, quantities, and deadlines.
2. **Admin invites specific approved Vendors (Suppliers)** to participate in the bidding.
3. **Vendors log in**, review the requested line items, and submit competitive **Quotations (Bids)** with their unit price, promised delivery dates, and payment terms.
4. **Vendors can revise** their bids if market conditions change or negotiations take place.
5. **Admin compares all submitted bids** in a unified evaluation matrix and **awards the contract (Finalized Quotation)** to the winning vendor.

---

## 📑 Master Table of Contents
1. [Core Domain Model & Database Schema](#1-core-domain-model--database-schema)
2. [End-to-End System Architecture & Security Filter Chain](#2-end-to-end-system-architecture--security-filter-chain)
3. [Deep-Dive Feature Logic with Real Code Snippets](#3-deep-dive-feature-logic-with-real-code-snippets)
   - [Feature 1: 2-Step Login & 2FA (TOTP) Authentication](#feature-1-2-step-login--2fa-totp-authentication)
   - [Feature 2: Smart Role-Based Routing (`/dashboard`)](#feature-2-smart-role-based-routing-dashboard)
   - [Feature 3: RFQ Creation & Unique Number Generation](#feature-3-rfq-creation--unique-number-generation)
   - [Feature 4: Adding Line Items to an RFQ](#feature-4-adding-line-items-to-an-rfq)
   - [Feature 5: Vendor Invitations & Access Control](#feature-5-vendor-invitations--access-control)
   - [Feature 6: Vendor Quoting & Server-Side Price Calculations](#feature-6-vendor-quoting--server-side-price-calculations)
   - [Feature 7: Bid Revision with Ownership Security](#feature-7-bid-revision-with-ownership-security)
   - [Feature 8: Comparing Bids & Awarding Winning Quotations](#feature-8-comparing-bids--awarding-winning-quotations)
   - [Feature 9: Password Reset & 2FA Recovery Workflows](#feature-9-password-reset--2fa-recovery-workflows)
4. [Exhaustive Controller Endpoint Matrix](#4-exhaustive-controller-endpoint-matrix)
   - [4.1 AuthMvcController](#41-authmvccontroller-endpoints)
   - [4.2 AdminMvcController](#42-adminmvccontroller-endpoints)
   - [4.3 VendorMvcController](#43-vendormvccontroller-endpoints)
5. [Frontend UI Components & Thymeleaf Fragments](#5-frontend-ui-components--thymeleaf-fragments)
6. [Word-for-Word Presentation & Defense Script](#6-word-for-word-presentation--defense-script)
7. [Live Changes You Can Perform On-the-Fly (Before vs After)](#7-live-changes-you-can-perform-on-the-fly-before-vs-after)
8. [Future Extensibility Roadmap ("What Else You Can Do")](#8-future-extensibility-roadmap-what-else-you-can-do)

---

## 1. Core Domain Model & Database Schema

Here is how all the database entities connect together:

```
┌─────────────────┐             1:N             ┌─────────────────┐
│      User       │────────────────────────────<│    RFQVendor    │
│ (ADMIN / VENDOR)│                             │ (Invitations)   │
└────────┬────────┘                             └────────┬────────┘
         │                                               │
         │ 1:N (Vendor submits)                          │ N:1
         ▼                                               ▼
┌─────────────────┐             N:1             ┌─────────────────┐
│   RFQQuotation  │>────────────────────────────│       Rfq       │
│ (Submitted Bid) │                             │  (RFQ Header)   │
└────────┬────────┘                             └────────┬────────┘
         │                                               │
         │ N:1 (Bid for Item)                            │ 1:N (Has items)
         ▼                                               ▼
┌─────────────────┐                             ┌─────────────────┐
│     RfqItem     │<────────────────────────────│     RfqItem     │
│  (Line Item)    │                             │  (Line Item)    │
└─────────────────┘                             └─────────────────┘
         ▲
         │ 1:1 (Winning Quote)
┌────────┴─────────────┐
│  FinalizedQuotation  │ (Awarded Contract + Admin approver)
└──────────────────────┘
```

### Entity Summary:
1. **[User](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/entity/User.java)**: Stores email, hashed password, role (`ADMIN` or `VENDOR`), MFA secret key (`mfaSecret`), MFA status (`mfaEnabled`), and reset tokens.
2. **[Rfq](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/entity/Rfq.java)**: The master RFQ record containing `rfqNo` (e.g. `RFQ-20261005-A1B2`), `title`, `indentNo`, `expireDate`, and `status` (`DRAFT`, `OPEN`, `CLOSED`).
3. **[RfqItem](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/entity/RfqItem.java)**: Line item belonging to an RFQ (`lineNo`, `itemCode`, `description`, `requestQty`, `unitOfMeasurement`, `requestDeliveryDate`, `deliveryLocation`).
4. **[RFQVendor](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/entity/RFQVendor.java)**: Bridge table mapping which Vendors are invited to bid on which RFQ.
5. **[RFQQuotation](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/entity/RFQQuotation.java)**: A vendor's bid on a specific line item (`bidNo`, `pricePerUnit`, `totalPrice = pricePerUnit * requestQty`, `promisedDeliveryDate`, `paymentTerms`, `remarks`).
6. **[FinalizedQuotation](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/entity/FinalizedQuotation.java)**: Represents the winning bid approved by an Admin (`finalId`, `rfq`, `quotation`, `approvedBy`, `finalizedDate`).

---

## 2. End-to-End System Architecture & Security Filter Chain

The application uses **Spring Boot MVC + Spring Security + Thymeleaf + JWT + 2FA TOTP**:

```
Browser / REST Client
      │
      │ 1. HTTP Request (includes `jwtToken` Cookie or `Authorization: Bearer` Header)
      ▼
┌────────────────────────────────────────────────────────────────────────────┐
│ JwtAuthenticationFilter (OncePerRequestFilter)                             │
│ ├─ Extracts JWT from cookie or header                                      │
│ ├─ If token is "PRE_AUTH" (pending 2FA OTP) -> bypasses SecurityContext    │
│ ├─ If valid full JWT -> extracts email -> loads UserDetails                │
│ └─ Sets SecurityContextHolder.getContext().setAuthentication(...)          │
└─────────────────────────────────────┬──────────────────────────────────────┘
                                      │
                                      ▼
┌────────────────────────────────────────────────────────────────────────────┐
│ Spring MVC Controllers                                                     │
│ ├─ AuthMvcController   -> /login, /verify-2fa, /dashboard, /logout         │
│ ├─ AdminMvcController  -> /admin/rfqs, /admin/vendors, /admin/quotations   │
│ └─ VendorMvcController -> /vendor/rfqs, /vendor/bids, /vendor/awards       │
└─────────────────────────────────────┬──────────────────────────────────────┘
                                      │
                                      ▼
┌────────────────────────────────────────────────────────────────────────────┐
│ Service Layer (@Transactional Business Logic)                              │
│ ├─ AuthServiceImpl, RfqServiceImpl, QuotationServiceImpl, RfqVendorService │
└─────────────────────────────────────┬──────────────────────────────────────┘
                                      │
                                      ▼
┌────────────────────────────────────────────────────────────────────────────┐
│ Repositories & Database (Spring Data JPA -> Hibernate -> SQL)              │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Deep-Dive Feature Logic with Real Code Snippets

---

### Feature 1: 2-Step Login & 2FA (TOTP) Authentication

#### Why do we need this?
To provide enterprise-grade security. Password compromise alone is not enough to breach the system; the user must provide a 6-digit dynamic code from Google Authenticator or Microsoft Authenticator.

#### Step 1: User Enters Email & Password
In [AuthMvcController.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/controller/AuthMvcController.java#L49-L76):
```java
@PostMapping("/login")
public String processLogin(@RequestParam String email,
                           @RequestParam String password,
                           RedirectAttributes redirectAttributes) {
    try {
        // Authenticate password and issue short-lived PRE_AUTH token
        LoginResponse response = authService.login(email, password);

        // Flash attributes pass data safely across redirect to GET /verify-2fa
        redirectAttributes.addFlashAttribute("preAuthToken", response.getPreAuthToken());
        redirectAttributes.addFlashAttribute("isFirstTimeSetup", response.isFirstTimeSetup());
        redirectAttributes.addFlashAttribute("qrCodeDataUri", response.getQrCodeDataUri());
        redirectAttributes.addFlashAttribute("secretKey", response.getSecretKey());
        redirectAttributes.addFlashAttribute("userEmail", email);

        return "redirect:/verify-2fa";
    } catch (Exception ex) {
        redirectAttributes.addFlashAttribute("errorMessage", "Authentication failed: " + ex.getMessage());
        return "redirect:/login";
    }
}
```

In [AuthServiceImpl.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/service/impl/AuthServiceImpl.java#L125-L164):
```java
public LoginResponse login(String email, String password) {
    // 1. Verify credentials with BCrypt
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

    // 2. Generate short-lived PRE_AUTH token (5-minute validity)
    String preAuthToken = jwtUtils.generatePreAuthToken(user.getEmail());

    // 3. Check if first time setup:
    if (user.getMfaEnabled() == null || !user.getMfaEnabled()) {
        String secret = totpService.generateSecret();
        user.setMfaSecret(secret);
        userRepository.save(user);

        // Generate QR code Data URI for Google Authenticator
        String qrCodeDataUri = totpService.getQrCodeDataUri(secret, user.getEmail());

        return LoginResponse.builder()
                .mfaRequired(true)
                .isFirstTimeSetup(true)
                .preAuthToken(preAuthToken)
                .secretKey(secret)
                .qrCodeDataUri(qrCodeDataUri)
                .build();
    }

    return LoginResponse.builder()
            .mfaRequired(true)
            .isFirstTimeSetup(false)
            .preAuthToken(preAuthToken)
            .build();
}
```

#### Step 2: User Enters 6-Digit OTP Code
In [AuthMvcController.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/controller/AuthMvcController.java#L87-L131):
```java
@PostMapping("/verify-2fa")
public String processVerify2Fa(@RequestParam String preAuthToken,
                               @RequestParam String otpCode,
                               RedirectAttributes redirectAttributes,
                               HttpServletResponse httpResponse) {
    try {
        // Validate OTP with TOTP algorithm (RFC 6238)
        AuthResponse authResponse = authService.verify2Fa(preAuthToken, otpCode);

        // Save full JWT in HttpOnly Cookie for secure browser session
        Cookie jwtCookie = new Cookie("jwtToken", authResponse.getAccessToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        httpResponse.addCookie(jwtCookie);

        return "redirect:/dashboard";
    } catch (Exception ex) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        redirectAttributes.addFlashAttribute("preAuthToken", preAuthToken);
        return "redirect:/verify-2fa";
    }
}
```

In [TotpServiceImpl.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/security/totp/TotpServiceImpl.java#L47-L59):
```java
public boolean verifyCode(String secret, String code) {
    // Allows 1 time-step drift (past 30s, current 30s, next 30s) to handle clock skew
    return timeProvider.verifyCode(secret, code, 1);
}
```

---

### Feature 2: Smart Role-Based Routing (`/dashboard`)

#### How it works:
When any logged-in user visits `/dashboard`, the controller dynamically inspects their role from Spring Security and routes them to their portal.

In [AuthMvcController.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/controller/AuthMvcController.java#L133-L151):
```java
@GetMapping({"/", "/dashboard", "/employees"})
public String showDashboardPage(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
        return "redirect:/login";
    }

    String email = auth.getName();
    User currentUser = userRepository.findByEmail(email).orElse(null);
    if (currentUser != null) {
        if (currentUser.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        } else if (currentUser.getRole() == Role.VENDOR) {
            return "redirect:/vendor/dashboard";
        }
    }
    return "redirect:/login";
}
```

---

### Feature 3: RFQ Creation & Unique Number Generation

#### How it works:
Admin submits the RFQ form. The backend generates a formatted RFQ number (e.g. `RFQ-20261005-AB12`) and sets initial status.

In [AdminMvcController.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/controller/AdminMvcController.java#L157-L180):
```java
@PostMapping("/rfqs/create")
public String createRfq(@RequestParam String title,
                        @RequestParam String indentNo,
                        @RequestParam(required = false) String expireDate,
                        @RequestParam(defaultValue = "OPEN") String status,
                        RedirectAttributes redirectAttributes) {
    try {
        RfqRequest request = new RfqRequest();
        request.setTitle(title);
        request.setIndentNo(indentNo);
        if (expireDate != null && !expireDate.isBlank()) {
            request.setExpireDate(LocalDate.parse(expireDate));
        }
        request.setStatus(Status.valueOf(status.toUpperCase()));

        RfqResponse created = rfqService.createRFQ(request);
        redirectAttributes.addFlashAttribute("successMessage", "RFQ " + created.getRfqNo() + " created successfully!");
        return "redirect:/admin/rfqs/" + created.getRfqNo();
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Failed to create RFQ: " + e.getMessage());
        return "redirect:/admin/rfqs";
    }
}
```

In [RfqServiceImpl.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/service/impl/RfqServiceImpl.java#L27-L36):
```java
public RfqResponse createRFQ(RfqRequest request) {
    Rfq rfq = modelMapper.map(request, Rfq.class);
    // Automatic unique RFQ ID generation: RFQ-YYYYMMDD-XXXX
    rfq.setRfqNo(RfqUtils.generateRfqNo());
    rfq.setStatus(request.getStatus());

    Rfq savedRfq = rfqRepository.save(rfq);
    return modelMapper.map(savedRfq, RfqResponse.class);
}
```

---

### Feature 4: Adding Line Items to an RFQ

#### How it works:
An RFQ needs line items (what materials to purchase). The Admin enters item code, quantity, and unit of measure.

In [AdminMvcController.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/controller/AdminMvcController.java#L278-L307):
```java
@PostMapping("/rfqs/{rfqNo}/items/add")
public String addItemToRfq(@PathVariable String rfqNo,
                           @RequestParam String lineNo,
                           @RequestParam String itemCode,
                           @RequestParam String description,
                           @RequestParam Integer requestQty,
                           @RequestParam String unitOfMeasurement,
                           @RequestParam(required = false) String requestDeliveryDate,
                           @RequestParam(required = false) String deliveryLocation,
                           RedirectAttributes redirectAttributes) {
    try {
        RfqItem item = new RfqItem();
        item.setLineNo(lineNo);
        item.setItemCode(itemCode);
        item.setDescription(description);
        item.setRequestQty(requestQty);
        item.setUnitOfMeasurement(unitOfMeasurement);
        if (requestDeliveryDate != null && !requestDeliveryDate.isBlank()) {
            item.setRequestDeliveryDate(LocalDate.parse(requestDeliveryDate));
        }
        item.setDeliveryLocation(deliveryLocation);

        rfqItemService.addItemToRfq(rfqNo, item);
        redirectAttributes.addFlashAttribute("successMessage", "Line item '" + itemCode + "' added!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Failed: " + e.getMessage());
    }
    return "redirect:/admin/rfqs/" + rfqNo;
}
```

---

### Feature 5: Vendor Invitations & Access Control

#### How it works:
Only invited vendors can bid on an RFQ. Admins select vendors from a multi-select modal.

In [AdminMvcController.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/controller/AdminMvcController.java#L323-L339):
```java
@PostMapping("/rfqs/{rfqNo}/invite-vendor")
public String inviteVendorsToRfq(@PathVariable String rfqNo,
                                 @RequestParam(value = "vendorUserIds", required = false) List<Long> vendorUserIds,
                                 RedirectAttributes redirectAttributes) {
    if (vendorUserIds == null || vendorUserIds.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Please select at least one vendor.");
        return "redirect:/admin/rfqs/" + rfqNo;
    }
    rfqVendorService.inviteVendors(rfqNo, vendorUserIds);
    redirectAttributes.addFlashAttribute("successMessage", "Vendors invited successfully!");
    return "redirect:/admin/rfqs/" + rfqNo;
}
```

In [RfqVendorServiceImpl.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/service/impl/RfqVendorServiceImpl.java#L62-L85):
```java
public List<UserResponse> inviteVendors(String rfqId, List<Long> vendorUserIds) {
    Rfq rfq = findRfq(rfqId);
    List<UserResponse> invitedList = new ArrayList<>();

    for (Long vendorId : vendorUserIds) {
        User vendor = userRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + vendorId));

        // Prevent duplicate invitations
        if (!rfqVendorRepository.existsByRfq_RfqIdAndVendor_UserId(rfq.getRfqId(), vendor.getUserId())) {
            RFQVendor rfqVendor = new RFQVendor();
            rfqVendor.setRfq(rfq);
            rfqVendor.setVendor(vendor);
            rfqVendorRepository.save(rfqVendor);
        }
        invitedList.add(modelMapper.map(vendor, UserResponse.class));
    }
    return invitedList;
}
```

---

### Feature 6: Vendor Quoting & Server-Side Price Calculations

#### How it works:
When a vendor bids, the server automatically calculates `totalPrice = pricePerUnit * item.requestQty` to prevent browser tampering.

In [VendorMvcController.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/controller/VendorMvcController.java#L218-L243):
```java
@PostMapping("/rfqs/{rfqNo}/items/{itemId}/quote")
public String submitQuote(@PathVariable String rfqNo,
                          @PathVariable Long itemId,
                          @RequestParam BigDecimal pricePerUnit,
                          @RequestParam(required = false) String promisedDeliveryDate,
                          @RequestParam(required = false) String paymentTerms,
                          @RequestParam(required = false) String remarks,
                          RedirectAttributes redirectAttributes) {
    try {
        QuotationRequest request = new QuotationRequest();
        request.setPricePerUnit(pricePerUnit);
        if (promisedDeliveryDate != null && !promisedDeliveryDate.isBlank()) {
            request.setPromisedDeliveryDate(LocalDate.parse(promisedDeliveryDate));
        }
        request.setPaymentTerms(paymentTerms);
        request.setRemarks(remarks);

        QuotationResponse response = quotationService.submitQuote(itemId, request);
        redirectAttributes.addFlashAttribute("successMessage", "Quote " + response.getBidNo() + " submitted!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Failed: " + e.getMessage());
    }
    return "redirect:/vendor/rfqs/" + rfqNo;
}
```

In [QuotationServiceImpl.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/service/impl/QuotationServiceImpl.java#L128-L149):
```java
public QuotationResponse submitQuote(Long itemId, QuotationRequest request) {
    User currentVendor = getCurrentAuthenticatedUser();
    RfqItem item = rfqItemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("RfqItem not found with ID: " + itemId));

    RFQQuotation quotation = new RFQQuotation();
    quotation.setBidNo("BID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    quotation.setRfqItem(item);
    quotation.setVendor(currentVendor);
    quotation.setPricePerUnit(request.getPricePerUnit());

    // SERVER-SIDE ACID PRICE CALCULATION:
    int qty = item.getRequestQty() != null ? item.getRequestQty() : 1;
    quotation.setTotalPrice(request.getPricePerUnit().multiply(BigDecimal.valueOf(qty)));

    quotation.setPromisedDeliveryDate(request.getPromisedDeliveryDate());
    quotation.setPaymentTerms(request.getPaymentTerms());
    quotation.setRemarks(request.getRemarks());

    RFQQuotation saved = quotationRepository.save(quotation);
    return mapToQuotationResponse(saved);
}
```

---

### Feature 7: Bid Revision with Ownership Security

#### How it works:
A vendor can update their quoted price. The service checks `quotation.getVendor().getUserId().equals(currentVendor.getUserId())` so vendors cannot edit competitors' bids.

In [QuotationServiceImpl.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/service/impl/QuotationServiceImpl.java#L170-L198):
```java
public QuotationResponse reviseQuote(Long quotationId, QuotationRequest request) {
    User currentVendor = getCurrentAuthenticatedUser();
    RFQQuotation quotation = quotationRepository.findById(quotationId)
            .orElseThrow(() -> new ResourceNotFoundException("Quotation not found: " + quotationId));

    // SECURITY CHECK: Ensure vendor owns this quotation
    if (!quotation.getVendor().getUserId().equals(currentVendor.getUserId())) {
        throw new RuntimeException("You can only revise your own quotations.");
    }

    quotation.setPricePerUnit(request.getPricePerUnit());
    int qty = quotation.getRfqItem() != null && quotation.getRfqItem().getRequestQty() != null
            ? quotation.getRfqItem().getRequestQty() : 1;
    quotation.setTotalPrice(request.getPricePerUnit().multiply(BigDecimal.valueOf(qty)));

    if (request.getPromisedDeliveryDate() != null) quotation.setPromisedDeliveryDate(request.getPromisedDeliveryDate());
    if (request.getPaymentTerms() != null) quotation.setPaymentTerms(request.getPaymentTerms());
    if (request.getRemarks() != null) quotation.setRemarks(request.getRemarks());

    RFQQuotation updated = quotationRepository.save(quotation);
    return mapToQuotationResponse(updated);
}
```

---

### Feature 8: Comparing Bids & Awarding Winning Quotations

#### How it works:
Admin reviews all submitted quotes in a comparative evaluation matrix. When clicking "Award Bid", a `FinalizedQuotation` record is created.

In [AdminMvcController.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/controller/AdminMvcController.java#L355-L367):
```java
@PostMapping("/rfqs/{rfqNo}/finalize-quote")
public String finalizeQuote(@PathVariable String rfqNo,
                            @RequestParam Long quotationId,
                            RedirectAttributes redirectAttributes) {
    try {
        FinalizedQuotationResponse finalized = quotationService.finalizeQuote(quotationId);
        redirectAttributes.addFlashAttribute("successMessage", "Winning Bid '" + finalized.getBidNo() + "' awarded!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Failed to award quotation: " + e.getMessage());
    }
    return "redirect:/admin/rfqs/" + rfqNo;
}
```

In [QuotationServiceImpl.java](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/java/com/ampta/rfq/service/impl/QuotationServiceImpl.java#L200-L219):
```java
public FinalizedQuotationResponse finalizeQuote(Long quotationId) {
    User currentUser = getCurrentAuthenticatedUser();
    RFQQuotation quotation = quotationRepository.findById(quotationId)
            .orElseThrow(() -> new ResourceNotFoundException("Quotation not found: " + quotationId));

    Rfq rfq = quotation.getRfqItem().getRfq();

    // Create or retrieve FinalizedQuotation record
    FinalizedQuotation finalized = finalizedQuotationRepository.findByQuotation_QuotationId(quotationId)
            .orElseGet(() -> {
                FinalizedQuotation f = new FinalizedQuotation();
                f.setRfq(rfq);
                f.setQuotation(quotation);
                f.setApprovedBy(currentUser);
                return finalizedQuotationRepository.save(f);
            });

    return mapToFinalizedResponse(finalized);
}
```

---

### Feature 9: Password Reset & 2FA Recovery Workflows

#### Password Reset Flow:
1. User clicks *"Forgot Password"*, enters email.
2. `AuthServiceImpl.forgotPassword(email)` generates a UUID token with 15-minute expiry and sends an email with link: `/reset-password?token=...&email=...`.
3. User enters new password. `AuthServiceImpl.resetPassword()` validates token, hashes new password with BCrypt, and revokes all active sessions.

#### Emergency 2FA Lost Device Flow:
1. User clicks *"Lost 2FA Device?"*, enters email.
2. `AuthServiceImpl.requestLost2Fa(email)` generates a 6-digit numeric OTP with 15-minute expiry and emails it.
3. User enters the 6-digit code in `/reset-2fa`.
4. `AuthServiceImpl.confirmLost2Fa()` sets `user.mfaEnabled = false` and `user.mfaSecret = null`.
5. On next login, the user is presented with a brand new QR Code to configure their authenticator app.

---

## 4. Exhaustive Controller Endpoint Matrix

### 4.1 AuthMvcController Endpoints

| Endpoint | Method | Input Parameters | Success Response / View | Error / Failure Handling | UI Component Used |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `/login` | `GET` | `unauthorized` (optional) | `auth/login.html` | Adds flash error if `unauthorized=true` | Card, Email/Pass inputs, Submit button |
| `/login` | `POST` | `email`, `password` | `redirect:/verify-2fa` with Flash attrs | `redirect:/login` with `errorMessage` | Form in `auth/login.html` |
| `/verify-2fa` | `GET` | None | `auth/verify-2fa.html` | Redirects to `/login` if token missing | QR Image, Secret Badge, 6-digit OTP Box |
| `/verify-2fa` | `POST` | `preAuthToken`, `otpCode` | `redirect:/dashboard` + Sets `jwtToken` Cookie | `redirect:/verify-2fa` with `errorMessage` | Form in `auth/verify-2fa.html` |
| `/dashboard` | `GET` | None | `redirect:/admin/dashboard` or `/vendor/dashboard` | `redirect:/login` if unauthenticated | Smart Router |
| `/forgot-password` | `POST` | `email` | `redirect:/reset-password?email=...` | `redirect:/login` with `errorMessage` | Modal `#forgotPasswordModal` |
| `/reset-password` | `GET` | `token`, `email` | `auth/reset-password.html` | Displays empty form if params missing | Password Reset Form |
| `/reset-password` | `POST` | `email`, `resetToken`, `newPassword` | `redirect:/login` with `successMessage` | `redirect:/reset-password` with error | Form in `auth/reset-password.html` |
| `/2fa/lost-request` | `POST` | `email` | `redirect:/reset-2fa?email=...` | `redirect:/verify-2fa` with error | Modal in `auth/verify-2fa.html` |
| `/2fa/lost-confirm` | `POST` | `email`, `emailOtp` | `redirect:/login` with `successMessage` | `redirect:/reset-2fa` with error | Form in `auth/reset-2fa.html` |
| `/logout` | `GET` | None | `redirect:/login` + Clears `jwtToken` Cookie | None | Logout button in `sidebar.html` |

---

### 4.2 AdminMvcController Endpoints

| Endpoint | Method | Input Parameters | Backend Logic | Success Behavior | UI Component Used |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `/admin/dashboard` | `GET` | None | Queries counts for RFQs, Vendors, Quotes, Awards | Renders `admin/dashboard.html` | 4 Metric KPI Cards, Recent RFQs Table |
| `/admin/rfqs` | `GET` | `status`, `search` | Filters RFQs by status and keyword | Renders `admin/rfq-list.html` | Filter Bar, RFQ Table, Create Modal |
| `/admin/rfqs/create` | `POST` | `title`, `indentNo`, `expireDate`, `status` | `rfqService.createRFQ(request)` | `redirect:/admin/rfqs/{rfqNo}` | Modal `#createRfqModal` |
| `/admin/rfqs/{rfqNo}` | `GET` | `rfqNo` | Loads RFQ, Items, Invited Vendors, Bids, Awards | Renders `admin/rfq-detail.html` | Items Table, Vendor Badges, Bid Matrix |
| `/admin/rfqs/{rfqNo}/edit` | `POST` | `title`, `indentNo`, `expireDate` | `rfqService.updateRFQ(rfqNo, request)` | `redirect:/admin/rfqs/{rfqNo}` | Modal `#editRfqModal` |
| `/admin/rfqs/{rfqNo}/status` | `POST` | `status` | `rfqService.updateRFQStatus(rfqNo, status)` | `redirect:/admin/rfqs/{rfqNo}` | Status Select Dropdown |
| `/admin/rfqs/{rfqNo}/delete` | `POST` | `rfqNo` | `rfqService.deleteRFQ(rfqNo)` | `redirect:/admin/rfqs` | Delete Button with Confirm |
| `/admin/rfqs/{rfqNo}/items/add` | `POST` | `lineNo`, `itemCode`, `qty`, `uom`, etc. | `rfqItemService.addItemToRfq(rfqNo, item)` | `redirect:/admin/rfqs/{rfqNo}` | Modal `#addItemModal` |
| `/admin/rfqs/{rfqNo}/items/{id}/delete` | `POST` | `rfqNo`, `itemId` | `rfqItemService.deleteItem(rfqNo, itemId)` | `redirect:/admin/rfqs/{rfqNo}` | Trash Icon Button |
| `/admin/rfqs/{rfqNo}/invite-vendor` | `POST` | `vendorUserIds` | `rfqVendorService.inviteVendors(rfqNo, ids)` | `redirect:/admin/rfqs/{rfqNo}` | Modal `#inviteVendorModal` |
| `/admin/rfqs/{rfqNo}/remove-vendor` | `POST` | `vendorUserId` | `rfqVendorService.removeInvitedVendor(...)` | `redirect:/admin/rfqs/{rfqNo}` | Remove Button on Vendor Tag |
| `/admin/rfqs/{rfqNo}/finalize-quote` | `POST` | `quotationId` | `quotationService.finalizeQuote(quotationId)` | `redirect:/admin/rfqs/{rfqNo}` | "Award Bid" Button |
| `/admin/vendors` | `GET` | None | Loads vendors + calculated stats | Renders `admin/vendor-list.html` | Vendor Table, Provision Modal |
| `/admin/vendors/create` | `POST` | `fullName`, `email`, `phone`, `password` | `authService.register(request)` + send mail | `redirect:/admin/vendors` | Modal `#createVendorModal` |
| `/admin/vendors/delete` | `POST` | `id` | `authService.deleteUserById(id)` | `redirect:/admin/vendors` | Delete Button in Vendor Row |
| `/admin/quotations` | `GET` | `rfqNo` (optional) | Loads quotes with optional RFQ filter | Renders `admin/quotations.html` | RFQ Filter Dropdown, Quotes Table |

---

### 4.3 VendorMvcController Endpoints

| Endpoint | Method | Input Parameters | Backend Logic | Success Behavior | UI Component Used |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `/vendor/dashboard` | `GET` | None | Loads vendor's invitations, bids, and won awards | Renders `vendor/dashboard.html` | 4 Metric KPI Cards, Recent Bids Table |
| `/vendor/rfqs` | `GET` | `status`, `search` | Filters vendor's invited RFQs | Renders `vendor/rfq-list.html` | Search Bar, RFQ Progress Badges |
| `/vendor/rfqs/{rfqNo}` | `GET` | `rfqNo` | Loads RFQ items + vendor's quote per item | Renders `vendor/rfq-detail.html` | Line Items Grid with Quoting Modals |
| `/vendor/rfqs/{rfqNo}/items/{id}/quote` | `POST` | `pricePerUnit`, `deliveryDate`, `terms` | `quotationService.submitQuote(itemId, req)` | `redirect:/vendor/rfqs/{rfqNo}` | Modal `#quoteModal_{itemId}` |
| `/vendor/bids` | `GET` | None | Loads all submitted bids by this vendor | Renders `vendor/bids.html` | Bids Table, Revise Modal Trigger |
| `/vendor/bids/{id}/revise` | `POST` | `pricePerUnit`, `deliveryDate`, `terms` | `quotationService.reviseQuote(id, req)` | `redirect:/vendor/bids` | Modal `#reviseModal_{quotationId}` |
| `/vendor/awards` | `GET` | None | Queries winning contracts from `FinalizedQuotation` | Renders `vendor/awards.html` | Won Contracts Table with Award Badges |

---

## 5. Frontend UI Components & Thymeleaf Fragments

### Universal Sidebar Fragment: [sidebar.html](file:///Users/apple/Workspace/Masstech/RFQ-service/src/main/resources/templates/fragments/sidebar.html)
Included in every view using: `<div th:replace="~{fragments/sidebar :: sidebar(${userRole}, ${activePage})}"></div>`

```html
<!-- Admin Navigation Links -->
<ul class="nav nav-pills flex-column mb-auto gap-1" th:if="${role == 'ADMIN'}">
    <li><a th:href="@{/admin/dashboard}" class="nav-link" th:classappend="${activePage == 'dashboard' ? 'active' : ''}">Dashboard</a></li>
    <li><a th:href="@{/admin/rfqs}" class="nav-link" th:classappend="${activePage == 'rfqs' ? 'active' : ''}">RFQ Management</a></li>
    <li><a th:href="@{/admin/vendors}" class="nav-link" th:classappend="${activePage == 'vendors' ? 'active' : ''}">Vendors Directory</a></li>
    <li><a th:href="@{/admin/quotations}" class="nav-link" th:classappend="${activePage == 'quotations' ? 'active' : ''}">Quotations & Awards</a></li>
</ul>

<!-- Vendor Navigation Links -->
<ul class="nav nav-pills flex-column mb-auto gap-1" th:if="${role == 'VENDOR'}">
    <li><a th:href="@{/vendor/dashboard}" class="nav-link" th:classappend="${activePage == 'dashboard' ? 'active' : ''}">Dashboard</a></li>
    <li><a th:href="@{/vendor/rfqs}" class="nav-link" th:classappend="${activePage == 'rfqs' ? 'active' : ''}">Invited RFQs</a></li>
    <li><a th:href="@{/vendor/bids}" class="nav-link" th:classappend="${activePage == 'bids' ? 'active' : ''}">My Submitted Bids</a></li>
    <li><a th:href="@{/vendor/awards}" class="nav-link" th:classappend="${activePage == 'awards' ? 'active' : ''}">Won Awards</a></li>
</ul>
```

### Universal Alert Handling
Every template checks for flash messages from controllers:
```html
<div th:if="${successMessage}" class="alert alert-success alert-dismissible fade show" role="alert">
    <span th:text="${successMessage}">Operation successful!</span>
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
</div>
<div th:if="${errorMessage}" class="alert alert-danger alert-dismissible fade show" role="alert">
    <span th:text="${errorMessage}">An error occurred!</span>
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
</div>
```

---

## 6. Word-for-Word Presentation & Defense Script

Use this exact script if you need to explain the system in an interview or presentation:

> **"Hello everyone. Today I am presenting our Enterprise Request For Quotation (RFQ) ERP System."**
>
> **1. Problem Statement & Architecture:**
> *"In corporate procurement, managing multi-vendor bidding via manual emails leads to price tampering, delays, and lack of transparency. Our system solves this with a clean multi-role architecture: Procurement Admins and Suppliers/Vendors."*
>
> **2. Authentication & 2FA Security:**
> *"Security is built from the ground up using Spring Security, Stateless JWT tokens, and RFC 6238 TOTP Two-Factor Authentication. When a user logs in with email and password, they receive a restricted `PRE_AUTH` token and must verify a 6-digit OTP from Google Authenticator. Once verified, a full JWT is stored in an `HttpOnly` secure cookie for Thymeleaf SSR navigation and can also be used as a Bearer token for REST clients."*
>
> **3. End-to-End Business Flow:**
> *"The lifecycle begins when the Admin creates an RFQ with line items and invites selected vendors. The invited vendors log in, see the RFQ in their catalog, and submit competitive bids. The server computes the total price automatically to guarantee data integrity. The Admin compares all received bids in a live matrix and finalizes the winning quotation with one click. The winning vendor immediately sees their won contract in their Awards dashboard."*
>
> **4. Data Isolation & Security:**
> *"We enforce strict cross-tenant isolation. Vendors can only view RFQs they are invited to and cannot inspect or edit competitors' bids."*

---

## 7. Live Changes You Can Perform On-the-Fly (Before vs After)

If an evaluator asks you to make code changes on the spot, follow these recipes:

### Live Change 1: Add Minimum Bid Price Validation ($10.00)
**File**: `src/main/java/com/ampta/rfq/service/impl/QuotationServiceImpl.java` (in `submitQuote` method)

```diff
  public QuotationResponse submitQuote(Long itemId, QuotationRequest request) {
      User currentVendor = getCurrentAuthenticatedUser();
      RfqItem item = rfqItemRepository.findById(itemId)
              .orElseThrow(() -> new ResourceNotFoundException("RfqItem not found with ID: " + itemId));

+     // LIVE CHANGE: Minimum bid price rule
+     if (request.getPricePerUnit() == null || request.getPricePerUnit().compareTo(new BigDecimal("10.00")) < 0) {
+         throw new IllegalArgumentException("Minimum bid price per unit must be at least $10.00");
+     }

      RFQQuotation quotation = new RFQQuotation();
```

---

### Live Change 2: Automatically Close RFQ When All Line Items Are Awarded
**File**: `src/main/java/com/ampta/rfq/service/impl/QuotationServiceImpl.java` (in `finalizeQuote` method)

```diff
      FinalizedQuotation finalized = finalizedQuotationRepository.findByQuotation_QuotationId(quotationId)
              .orElseGet(() -> {
                  FinalizedQuotation f = new FinalizedQuotation();
                  f.setRfq(rfq);
                  f.setQuotation(quotation);
                  f.setApprovedBy(currentUser);
                  return finalizedQuotationRepository.save(f);
              });

+     // LIVE CHANGE: Auto-close RFQ when all line items are awarded
+     long totalItems = rfqItemRepository.findByRfq_RfqId(rfq.getRfqId()).size();
+     long awardedItems = finalizedQuotationRepository.findByRfq_RfqId(rfq.getRfqId()).size();
+     if (awardedItems >= totalItems) {
+         rfq.setStatus(Status.CLOSED);
+         rfqRepository.save(rfq);
+     }

      return mapToFinalizedResponse(finalized);
```

---

### Live Change 3: Block Bidding If RFQ Expiration Date Has Passed
**File**: `src/main/java/com/ampta/rfq/service/impl/QuotationServiceImpl.java` (in `submitQuote` method)

```diff
      RfqItem item = rfqItemRepository.findById(itemId)
              .orElseThrow(() -> new ResourceNotFoundException("RfqItem not found with ID: " + itemId));

+     // LIVE CHANGE: Check expiration date
+     if (item.getRfq() != null && item.getRfq().getExpireDate() != null) {
+         if (item.getRfq().getExpireDate().isBefore(LocalDate.now())) {
+             throw new IllegalStateException("Bidding is closed because this RFQ expired on " + item.getRfq().getExpireDate());
+         }
+     }
```

---

## 8. Future Extensibility Roadmap ("What Else You Can Do")

Here are 6 advanced architectural features you can propose to impress any interviewer:

1. **⚡ Real-Time Reverse Auction Engine (WebSockets + STOMP)**:
   - Allow vendors to participate in live 15-minute bidding wars where lowest bids are broadcasted in real-time without refreshing the browser.
2. **📄 Automated PDF Purchase Order Generation (iText / OpenPDF)**:
   - When an Admin awards a bid, automatically generate a stamped, signed PDF Purchase Order document ready for vendor download.
3. **⭐ Vendor Quality & Delivery Scoring Algorithm**:
   - Track on-time delivery rate, quality compliance, and price competitiveness to generate a 5-star vendor rating.
4. **🔒 Tamper-Proof Audit Trail (Hibernate Envers)**:
   - Record every single price revision, timestamp, and IP address for compliance and auditing.
5. **🚀 High-Speed Distributed Caching (Redis)**:
   - Cache active RFQ catalogs in Redis with automatic eviction upon quote submission.
6. **🌐 Multi-Currency & Live Exchange Rate Converter**:
   - Allow international suppliers to bid in EUR, JPY, or GBP with real-time conversion to USD.
