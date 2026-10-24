# Chapter 15: Real-World Project (LOS) Architecture & Production Scenario Interviews

This guide covers real-world enterprise scenario questions asked across **ParaMatrix, Team Computers, Nimap Infotech, Axis Bank, Transbnk, NuSummit, and Servosys**.

---

## 📑 Table of Contents
1. [End-to-End Architecture: Loan Origination System (LOS)](#1-end-to-end-architecture-loan-origination-system-los)
2. [CIBIL / Credit Score Integration & Underwriting Rules Engine](#2-cibil--credit-score-integration--underwriting-rules-engine)
3. [Aadhaar & PAN Verification Workflow (UIDAI / NSDL API)](#3-aadhaar--pan-verification-workflow-uidai--nsdl-api)
4. [The Hardest Production Bug Solved: Concurrency & Deadlocks](#4-the-hardest-production-bug-solved-concurrency--deadlocks)
5. [Production Incident Management: P1 Outages, Thread Dumps & Root Cause Analysis](#5-production-incident-management-p1-outages-thread-dumps--root-cause-analysis)
6. [Daily Roles, Agile Workflow & Behavioral/HR Interview Questions](#6-daily-roles-agile-workflow--behavioralhr-interview-questions)

---

## 1. End-to-End Architecture: Loan Origination System (LOS)

### 1. What is a Loan Origination System (LOS)?
A Loan Origination System (LOS) automates the end-to-end lifecycle of a loan application from customer onboarding, document collection, credit underwriting, KYC verification, to final sanction and disbursement.

```
[Angular 16 SPA (Customer & Backoffice Portal)]
                    |
                    | HTTPS / JWT
                    v
    [Spring Cloud API Gateway (Port 8080)]
        | (Rate Limiting, Routing, JWT Validation)
        +--------------------------------------------------------------+
        |                               |                              |
        v                               v                              v
[Customer & KYC Service]      [Loan Underwriting Service]    [Notification Service]
   - Aadhaar/PAN Verification    - CIBIL Bureau Integration     - Kafka Consumer
   - S3 Document Storage (KMS)   - Rule Engine (Camunda/Drools) - Email/SMS Alerts
   - MySQL (Customer DB)         - PostgreSQL (Loan DB)         - Redis Queue
        |                               |                              |
        +-------------------------------+------------------------------+
                                        |
                            [Apache Kafka Event Bus]
```

### 2. Core Microservices in the LOS Architecture:
1. **API Gateway Service**: Perimeter security, rate limiting (Redis token bucket), and SSL termination.
2. **Auth & IAM Service**: Spring Security + JWT, handles RBAC for applicants, loan verification officers, credit underwriters, and system admins.
3. **Customer & KYC Service**: Manages customer profiles, multi-address storage (Office, Permanent, Current), and document upload with SHA-256 validation to AWS S3.
4. **Bureau & CIBIL Integration Service**: Communicates with external credit bureaus (TransUnion CIBIL / Experian) using signed MTLS requests.
5. **Loan Evaluation & Underwriting Engine**: Evaluates Debt-to-Income (DTI), Loan-to-Value (LTV), and credit history to calculate sanction amounts and interest rates.
6. **Disbursement & Payment Gateway Service**: Integrates with banking payment switches (NEFT / RTGS / IMPS) for disbursal.

---

## 2. CIBIL / Credit Score Integration & Underwriting Rules Engine

### 1. How CIBIL Score is Fetched and Evaluated:
1. When an applicant submits their loan application, the `LoanService` emits a `LoanAppliedEvent` via Kafka.
2. The `BureauService` consumes the event, extracts customer PAN, Name, and Date of Birth.
3. It signs an XML/JSON payload and makes a secure outbound HTTPS call with Mutual TLS (mTLS) to the **CIBIL Bureau Gateway API**.
4. CIBIL returns:
   - Credit Score (Range: 300 to 900)
   - Number of active credit cards, loans, and past 36-month repayment defaults (DPD - Days Past Due).
5. **Underwriting Rules**:
   - `Score < 650`: Automatic rejection.
   - `650 <= Score < 750`: Forwarded to Manual Credit Underwriter review.
   - `Score >= 750`: Eligible for instant in-principle sanction.

---

## 3. Aadhaar & PAN Verification Workflow (UIDAI / NSDL API)

### 1. Verification Flow:
1. Customer enters 10-character PAN number (`[A-Z]{5}[0-9]{4}[A-Z]{1}`).
2. Service validates format via regex before hitting external APIs.
3. Call dispatched to **NSDL / Protean API** to verify PAN validity and match the name against the applicant's entered name using **Levenshtein Distance / Jaro-Winkler string similarity** (must exceed 85% match to tolerate minor typos).
4. For Aadhaar, the system uses the **UIDAI Aadhaar OTP eKYC API**:
   - Step 1: Send 12-digit Aadhaar to UIDAI -> UIDAI triggers OTP to customer's Aadhaar-linked mobile.
   - Step 2: Customer inputs OTP -> Backend submits OTP to UIDAI -> UIDAI returns cryptographically signed XML containing demographic data and photo.
   - Step 3: Raw Aadhaar number is masked (only last 4 digits stored) in compliance with UIDAI regulations.

---

## 4. The Hardest Production Bug Solved: Concurrency & Deadlocks

### 1. The Scenario (Interview Question from Team Computers / FYN Tune):
*"What was the hardest bug you solved in your project?"*

### 2. How to Explain in an Interview:
> *"The hardest bug I resolved occurred in our Loan Disbursement Module during a high-volume flash loan drive.*
>
> *We observed intermittent database deadlocks in MySQL InnoDB when concurrent disbursement jobs and automated repayment deduction webhooks attempted to update borrower account balances simultaneously.*
>
> *Upon analyzing MySQL deadlock logs using `SHOW ENGINE INNODB STATUS;`, we discovered that Job A locked Customer 101 then requested a lock on Account 501, while Job B locked Account 501 and requested a lock on Customer 101.*
>
> *To fix this permanently, we implemented **Strict Lock Ordering** across all transaction boundaries. Furthermore, for balance deduction, we replaced coarse entity-level pessimistic locks with atomic database decrement operations (`UPDATE accounts SET balance = balance - :amount WHERE id = :id AND balance >= :amount`), which eliminated the deadlock entirely and scaled throughput by 400%."*

---

## 5. Production Incident Management: P1 Outages, Thread Dumps & Root Cause Analysis

### 1. Troubleshooting Checklist for P1 Incidents:
1. **Initial Triage & Alert**: Triggered via PagerDuty / Opsgenie from Prometheus alerts.
2. **Log & Metrics Correlation**:
   - Trace requests via Distributed Tracing (Micrometer + Zipkin / OpenTelemetry `traceId` and `spanId`).
   - Inspect ELK / Grafana dashboard for error spikes (HTTP 5xx rate).
3. **Thread Dump Analysis**:
   - If JVM CPU is pinned or requests are timing out, capture 3 thread dumps 10 seconds apart:
     `jcmd <PID> Thread.print > /tmp/threaddump1.txt`
   - Look for states like `BLOCKED` on synchronization monitors or `WAITING` on HikariCP connection pools.
4. **Immediate Mitigation vs Root Cause**:
   - Immediate: Rollback deployment or restart pod / scale replicas.
   - Post-Mortem (RCA): Publish a Root Cause Analysis document detailing Timeline, Root Cause, Impact, and 5 Action Items to prevent recurrence.

---

## 6. Daily Roles, Agile Workflow & Behavioral/HR Interview Questions

### 1. "Walk me through your day-to-day roles and responsibilities"
> *"I work as a Fullstack Java & Spring Boot engineer in a 2-week Agile sprint cycle.*
>
> *My daily routine starts with our 15-minute daily standup to discuss progress, blockers, and dependencies. During the day, I implement business features (REST APIs, microservices integration, database schemas, and Angular UI components), write unit and integration tests using JUnit 5 and Mockito aiming for >80% coverage, and review peer pull requests on GitHub.*
>
> *I also actively participate in Sprint Planning, Backlog Grooming with our Product Owner/Business Analyst to review BRDs (Business Requirement Documents), and collaborate with DevOps on CI/CD pipelines and deployment troubleshooting."*

### 2. "Why do you want to leave your current organization?"
> *"I have had a rewarding experience at my current organization where I gained strong foundational expertise in building microservices and working with high-traffic enterprise systems.*
>
> *However, I am looking for a new technical challenge where I can take on greater architectural ownership, solve deeper scalability challenges, and contribute to cutting-edge cloud-native architectures that match my current career aspirations."*

### 3. "Why is LOS not deployed directly / deployment pipeline stages?"
> *"In banking and fintech systems, direct deployment to production is strictly forbidden due to regulatory compliance (RBI guidelines) and risk management.*
>
> *Our deployment pipeline strictly moves through four isolated environments:*
> 1. **DEV**: Continuous integration where developers test code.
> 2. **QA**: Automated regression and integration testing.
> 3. **UAT (User Acceptance Testing)**: Real business users and credit underwriters validate workflows against real-world loan scenarios using sanitized data.
> 4. **Pre-PROD / Staging**: An exact mirror of production hardware and networking for performance, load, and security penetration testing.
> 5. **Production**: Automated zero-downtime Blue-Green or Canary deployment scheduled during non-peak maintenance windows after Change Advisory Board (CAB) sign-off."*

---

*Back to [Master Index & Roadmap](file:///Users/apple/Workspace/Home/Javascript/Learning/00_Master_Index_and_Roadmap.md)*
