# Ampta Loan Service — REST API Endpoint Naming Conventions & Architecture Guide

This document establishes the official REST API design standards for the **Ampta Loan Service** microservice. All developers must follow these conventions to ensure uniform design, predictable routing, clean team integration, and high security.

---

## 1. Core RESTful Design Rules

1. **Always Use Plural Nouns for Resources**
   * ✅ `/api/v1/loans`, `/api/v1/customers`, `/api/v1/cibils`, `/api/v1/notifications`
   * ❌ `/api/v1/loan`, `/api/v1/customer`, `/api/v1/cibil`

2. **Use kebab-case for All Multi-Word Paths**
   * ✅ `/api/v1/loan-accounts`, `/api/v1/sanction-letters`, `/api/v1/emi-schedules`, `/api/v1/score-cards`
   * ❌ `/api/v1/loanAccounts`, `/api/v1/loan_accounts`, `/api/v1/loanaccount`

3. **Use Proper HTTP Methods (Never Put Action Verbs in Resource Paths)**
   * **`GET`**: Retrieve resources (read-only, idempotent, safe).
   * **`POST`**: Create new resources or execute non-idempotent operations.
   * **`PUT`**: Replace an entire existing resource or complete an update.
   * **`PATCH`**: Partially update specific fields (e.g., status change).
   * **`DELETE`**: Remove resources.
   * ✅ `POST /api/v1/cibils/customers/10` (creates/checks report)
   * ❌ `POST /api/v1/cibil/customer/10/check-score` (action verb in path)

4. **Reference Centralized Constants in `Endpoints.java`**
   * Do NOT hardcode URL prefix strings like `@RequestMapping("/api/v1/loans")`.
   * Reference `Endpoints.V1_LOANS` directly.

5. **Path Variables vs Query Parameters**
   * **Path Variables (`@PathVariable`)**: Identify specific resources hierarchically:
     * `/api/v1/loans/{dealId}`
     * `/api/v1/customers/{customerId}/documents/{documentId}`
   * **Query Parameters (`@RequestParam`)**: For filtering, searching, sorting, and pagination:
     * `/api/v1/loans?status=APPROVED&page=0&size=10`
     * `/api/v1/notifications?read=false`

6. **Standardized Response Envelope**
   Every endpoint should return the unified `ApiResponse<T>`:
   ```json
   {
     "success": true,
     "message": "Operation completed successfully",
     "data": { ... }
   }
   ```

---

## 2. Controller-by-Controller Audit & Recommendations

### 2.1 AuthController (Owner: SHIVAM)
* **Base Path Constant**: `Endpoints.V1_AUTH` (`/api/v1/auth`)

| Method | Current Endpoint | Recommended Endpoint | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/register` | `/api/v1/auth/register` | Register customer & user |
| `GET` | `/api/v1/auth/verify-email` | `/api/v1/auth/verify-email?token=` | Verify email via email link |
| `POST` | `/api/v1/auth/verify-email` | `/api/v1/auth/verify-email?token=` | Verify email via client API |
| `POST` | `/api/v1/auth/resend-verification` | `/api/v1/auth/resend-verification?email=` | Resend verification email |
| `POST` | `/api/v1/auth/login` | `/api/v1/auth/login` | Initial credentials check |
| `POST` | `/api/v1/auth/verify-2fa` | `/api/v1/auth/verify-2fa` | Complete MFA authentication |
| `POST` | `/api/v1/auth/refresh-token` | `/api/v1/auth/refresh-token` | Rotate refresh token |
| `POST` | `/api/v1/auth/revoke-token` | `/api/v1/auth/revoke-token` | Invalidate active session |
| `POST` | `/api/v1/auth/lost-2fa/request` | `/api/v1/auth/lost-2fa/request` | Send recovery OTP |
| `POST` | `/api/v1/auth/lost-2fa/confirm` | `/api/v1/auth/lost-2fa/confirm` | Reset 2FA with OTP |
| `POST` | `/api/v1/auth/forgot-password` | `/api/v1/auth/forgot-password` | Generate reset token |
| `POST` | `/api/v1/auth/reset-password` | `/api/v1/auth/reset-password` | Set new password with token |

---

### 2.2 LoanDealController (Owner: MANISH)
* **Current Issue**: Base path is `@RequestMapping("/api/v1")` with singular `/loan`, user ID in POST path, and `@DeleteMapping("{dealId}")` which creates `/api/v1/{dealId}` instead of `/api/v1/loans/{dealId}`.
* **Recommended Base Path**: `Endpoints.V1_LOAN` (`/api/v1/loans`)

| Method | Current Endpoint | Recommended Endpoint | Recommendation Rationale |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/loan/{userId}` | `POST /api/v1/loans` | Plural resource. User ID should be extracted from JWT `Authentication`, not passed in URL to prevent impersonation. |
| `GET` | `/api/v1/loan/{dealId}` | `GET /api/v1/loans/{dealId}` | Plural resource `/loans`. |
| `GET` | `/api/v1/loan` | `GET /api/v1/loans` | Use query params: `?status=...&page=0&size=10`. |
| `DELETE` | `/api/v1/{dealId}` *(Bug!)* | `DELETE /api/v1/loans/{dealId}` | Fixes missing resource path in `@DeleteMapping`. |

---

### 2.3 LoanReviewController (Owner: MANISH / SHIVAM)
* **Current Base Path**: `@RequestMapping("/api/v1/officer")`
* **Current Endpoint**: `PUT /api/v1/officer/loan/{dealId}/decision`
* **Recommended Design**:
  * Keep loan actions anchored to loans: `PUT /api/v1/loans/{dealId}/review` or `POST /api/v1/loans/{dealId}/decisions`
  * Or under officers namespace: `PUT /api/v1/officers/loans/{dealId}/review`
  * Protect with `@PreAuthorize("hasRole('OFFICER')")` instead of relying on URL obscurity.

---

### 2.4 KycController (Owner: ANUPAMA)
* **Current Base Path**: `@RequestMapping("/api/v1/kyc")` (hardcoded string)
* **Recommended Base Path**: `Endpoints.V1_KYC` (`/api/v1/kyc`)

| Method | Current Endpoint | Recommended Endpoint | Recommendation Rationale |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/kyc/customers/{customerId}/documents` | `/api/v1/kyc/customers/{customerId}/documents` | Clean REST hierarchical sub-resource. |
| `GET` | `/api/v1/kyc/customers/{customerId}/documents` | `/api/v1/kyc/customers/{customerId}/documents` | Standard document listing. |
| `GET` | `/api/v1/kyc/customers/{customerId}/documents/{documentId}` | `/api/v1/kyc/customers/{customerId}/documents/{documentId}` | Standard single document metadata. |
| `GET` | `/api/v1/kyc/customers/{customerId}/documents/{documentId}/download` | `/api/v1/kyc/customers/{customerId}/documents/{documentId}/download` | Binary download endpoint. |
| `DELETE` | `/api/v1/kyc/customers/{customerId}/documents/{documentId}` | `/api/v1/kyc/customers/{customerId}/documents/{documentId}` | Document deletion. |
| `GET` | `/api/v1/kyc/officer/customers` | `/api/v1/kyc/officers/customers` | Standardize `officer` to plural `officers`. |
| `PUT` | `/api/v1/kyc/officer/customers/{customerId}/documents/bulk-review` | `/api/v1/kyc/officers/customers/{customerId}/documents/reviews` | Plural resource and clean HTTP PUT. |

---

### 2.5 CibilController (Owner: ANUPAMA)
* **Current Base Path**: `@RequestMapping("/api/v1/cibil")` (singular, hardcoded)
* **Recommended Base Path**: `Endpoints.V1_CIBIL` (`/api/v1/cibils`)

| Method | Current Endpoint | Recommended Endpoint | Recommendation Rationale |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/cibil/customer/{customerId}/check-score` | `POST /api/v1/cibils/customers/{customerId}` | Plural `/cibils` and `/customers`. Remove verb `check-score`. |
| `GET` | `/api/v1/cibil/customer/{customerId}/score` | `GET /api/v1/cibils/customers/{customerId}` | Retrieve latest CIBIL score/report. |

---

### 2.6 ScoreCardController (Owner: SHIVAM)
* **Base Path**: `Endpoints.V1_SCORE_CARD` (`/api/v1/score-cards`)

| Method | Current Endpoint | Recommended Endpoint | Recommendation Rationale |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/score-cards` | `POST /api/v1/score-cards` | Generates a new score card. |
| `GET` | *(Missing)* | `GET /api/v1/score-cards/customers/{customerId}` | Recommended: Fetch historical score cards for a customer. |

---

### 2.7 EligibilityController (Owner: SHIVAM)
* **Base Path**: `Endpoints.V1_ELIGIBILITY` (`/api/v1/eligibility`)

| Method | Current Endpoint | Recommended Endpoint | Recommendation Rationale |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/eligibility/check` | `POST /api/v1/eligibility` | In REST, POSTing to `/eligibility` calculates/creates the result. |
| `GET` | *(Missing)* | `GET /api/v1/eligibility/customers/{customerId}` | Recommended: Retrieve latest eligibility calculation. |

---

### 2.8 EmiScheduleController (Owner: PRINCE)
* **Current Base Path**: `Endpoints.V1_SCHEDULER` (`/api/v1/schedulers`)
* **Recommended Base Path**: `/api/v1/emi-schedules` or `/api/v1/loan-accounts/{loanAccountId}/emi-schedules`

| Method | Current Endpoint | Recommended Endpoint | Recommendation Rationale |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/schedulers` | `POST /api/v1/emi-schedules` | Generate EMI schedule. |
| `GET` | `/api/v1/schedulers/loan-account/{loanAccountId}` | `GET /api/v1/loan-accounts/{loanAccountId}/emi-schedules` | Plural `loan-accounts`. EMI schedule belongs to an account. |
| `PATCH` | `/api/v1/schedulers/{emiScheduleId}/payment-status` | `PATCH /api/v1/emi-schedules/{emiScheduleId}/status` | Standardize status update. |
| `GET` | `/api/v1/schedulers/loan-account/{id}/schedules/{id}/penalties` | `GET /api/v1/emi-schedules/{emiScheduleId}/penalties` | Clean direct sub-resource. |

---

### 2.9 PaymentController (Owner: PRINCE)
* **Base Path**: `Endpoints.V1_PAYMENT` (`/api/v1/payments`)

| Method | Current Endpoint | Recommended Endpoint | Recommendation Rationale |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/payments` | `POST /api/v1/payments` | Process a loan EMI payment. |
| `GET` | *(Missing)* | `GET /api/v1/payments/{paymentId}` | Retrieve payment status/receipt. |
| `GET` | *(Missing)* | `GET /api/v1/payments/loan-accounts/{loanAccountId}` | List payment history for an account. |

---

### 2.10 NotificationController (Owner: SHIVAM)
* **Base Path**: `Endpoints.V1_NOTIFICATION` (`/api/v1/notifications`)

| Method | Current Endpoint | Recommended Endpoint | Recommendation Rationale |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/notifications/users/{userId}` | `GET /api/v1/notifications?userId={userId}` or `/api/v1/users/{userId}/notifications` | Clean resource hierarchy. |
| `PATCH` | `/api/v1/notifications/users/{userId}/{id}/read` | `PATCH /api/v1/notifications/{notificationId}/read` | Notification ID is already unique. |
| `GET` | `/api/v1/notifications/users/{userId}/unread-count` | `GET /api/v1/notifications/unread-count?userId={userId}` | Clean query parameter. |

---

### 2.11 Planned Modules (Upcoming Sprints)

| Module | Team Member | Recommended Base Endpoint | Example REST Routes |
| :--- | :--- | :--- | :--- |
| **Wallets** | PRINCE | `Endpoints.V1_WALLET` (`/api/v1/wallets`) | `GET /api/v1/wallets/customers/{id}`, `POST /api/v1/wallets/{id}/topup` |
| **Penalties** | PRINCE | `Endpoints.V1_PENALTY` (`/api/v1/penalties`) | `GET /api/v1/penalties/loan-accounts/{id}` |
| **Bounces** | PRINCE | `Endpoints.V1_BOUNCE` (`/api/v1/bounces`) | `GET /api/v1/bounces`, `POST /api/v1/bounces` |
| **Sanction Letters** | MANISH | `Endpoints.V1_SANCTION_LETTER` (`/api/v1/sanction-letters`) | `GET /api/v1/sanction-letters/loans/{dealId}` |
| **Disbursements** | MANISH | `Endpoints.V1_DISBURSEMENT` (`/api/v1/disbursements`) | `POST /api/v1/disbursements/loans/{dealId}` |
| **Foreclosures** | FARAZ | `Endpoints.V1_FORECLOSURE` (`/api/v1/foreclosures`) | `POST /api/v1/foreclosures/loans/{dealId}` |
| **Closures** | FARAZ | `Endpoints.V1_CLOSURE` (`/api/v1/closures`) | `POST /api/v1/closures/loans/{dealId}` |
| **Tickets** | SHIVAM | `Endpoints.V1_TICKET` (`/api/v1/tickets`) | `GET /api/v1/tickets`, `POST /api/v1/tickets` |

---

## 3. Team Action Checklist Before Next Release

1. [ ] Update all `@RequestMapping` to use constants from `com.ampta.utils.Endpoints`.
2. [ ] Replace singular names (`/loan`, `/cibil`, `/customer`) with plurals (`/loans`, `/cibils`, `/customers`).
3. [ ] Fix bug in `LoanDealController` where `@DeleteMapping("{dealId}")` deletes at `/api/v1/{dealId}`.
4. [ ] Standardize response DTOs using `ApiResponse<T>`.
5. [ ] Ensure all path variables use camelCase (`{customerId}`, `{dealId}`, `{loanAccountId}`).
