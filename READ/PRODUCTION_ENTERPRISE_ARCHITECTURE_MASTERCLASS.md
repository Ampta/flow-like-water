# Enterprise Architecture Blueprint: Designing & Delivering Complex Multi-Module Systems

---

## 🎯 Executive Overview: How Real-World Production Systems Work

When junior developers build software, they write **isolated CRUD operations** on single database tables.
When lead architects build enterprise software, they build **end-to-end data assembly lines** where one event triggers state mutations across multiple modules.

This masterclass document teaches you the exact mental model, architectural patterns, entity design decisions, and **50 production-grade business endpoints** across a complete Enterprise Procurement-to-Pay (P2P) lifecycle:

1. **Module 1: Strategic Sourcing, RFQ, Multi-Vendor Bidding & Awarding Engine**
2. **Module 2: Purchase Order (PO), Multi-Milestone Delivery & Quality Inspection (GRN) Engine**
3. **Module 3: Invoicing, 3-Way Match Verification, Penalty Calculation & Payment Disbursement Engine**

---

# 🏗️ Part 1: How to Decide Entities Like a Staff Engineer

In enterprise applications, **never design entities based on UI screens**. Design them around **Transaction Boundaries, Immutability, and Auditability**.

```
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                                 ENTITY TYPOLOGY                                        │
├───────────────────┬──────────────────────────────────┬─────────────────────────────────┤
│ Type              │ Real-World Purpose               │ Example                         │
├───────────────────┼──────────────────────────────────┼─────────────────────────────────┤
│ 1. Aggregate Root │ Master life-cycle owner          │ Rfq, PurchaseOrder, Invoice     │
│ 2. Dependent Child│ Meaningless without parent       │ RfqItem, PurchaseOrderItem      │
│ 3. Junction State │ Dynamic relationship with state  │ RFQVendor (status: INVITED/BID) │
│ 4. Competitive Bid│ Independent external submission  │ RFQQuotation                    │
│ 5. Snapshot / Lock│ Immutable historical record      │ FinalizedQuotation, PoSnapshot  │
│ 6. Physical Event │ Real-world warehouse action      │ GoodsReceiptNote (GRN), Quality │
│ 7. Financial Event│ Ledger calculation & disbursement│ ThreeWayMatchRecord, PaymentTx   │
└───────────────────┴──────────────────────────────────┴─────────────────────────────────┘
```

### The "Snapshot Rule" (Why Real Systems Duplicate Data)
> **Rule**: When an entity transitions from a negotiation phase (RFQ/Bid) to a legal contract (Purchase Order), you **must never rely on live foreign keys that can be edited later**.
> You create an **immutable snapshot**. If a vendor changes their company name, price, or item specifications next month, the executed Purchase Order must preserve the exact price agreed upon at that exact second.

---

# 🔄 Part 2: The Complete 50-Endpoint Lifecycle Map

Here is the exact visual map of how data flows from **Day 1 (RFQ Creation)** to **Day 45 (Vendor Payment Receipt)**:

```
[ MODULE 1: RFQ & SOURCING ]
  1. Create RFQ ──► 2. Add Line Items ──► 3. Attach Specs ──► 4. Invite Vendors ──► 5. Publish RFQ
                                                                                            │
  8. Finalize Award ◄── 7. Compare Quotes ◄── 6. Vendors Submit Competitive Bids ◄──────────┘
         │
         ▼
[ MODULE 2: PURCHASE ORDER & FULFILLMENT ]
  9. Auto-Generate PO from Awarded Quotes ──► 10. Buyer Approval ──► 11. Vendor Signs PO
                                                                             │
  14. Warehouse Generates GRN ◄── 13. Vendor Ships & Submits ASN ◄───────────┘
         │
         ▼
  15. Quality Control Inspection (Accept / Reject / Partial Return)
         │
         ▼
[ MODULE 3: INVOICING & 3-WAY MATCHING ]
  16. Vendor Submits Invoice against PO & GRN
         │
         ▼
  17. Automated 3-Way Matching Engine (PO Price == GRN Quantity == Invoice Amount)
         ├───► Mismatch Detected? ──► Raise Dispute & Apply SLA Penalties
         └───► Match Passed (Variance <= 0.05%) ──► Approve for Payment
                                                             │
  18. Schedule Net-30 Payment Batch ──► 19. Bank Disbursement ──► 20. Ledger Reconciliation
```

---

# 📦 Complete Breakdown of the 50 Business Endpoints

---

## 🏛️ Module 1: RFQ, Sourcing, Bidding & Awarding Engine (Endpoints 1 – 17)

This module handles sourcing requirements, line item specifications, invitation governance, vendor bid submissions, comparative evaluations, and contract awarding.

```
Base Path: /api/v1
```

| # | HTTP Method | Endpoint URI | Business Logic & Workflow State Changes |
| :- | :--- | :--- | :--- |
| **1** | `POST` | `/rfqs` | **Create Draft RFQ**: Generates unique `RFQ-2026-XXXX`, sets initial status to `DRAFT`, assigns Buyer department. |
| **2** | `GET` | `/rfqs` | **Search & Filter RFQs**: Paginated query supporting status (`DRAFT`, `OPEN`, `EVALUATION`, `AWARDED`), date ranges, and creator ID. |
| **3** | `GET` | `/rfqs/{rfqId}` | **Get RFQ Full Details**: Fetches RFQ aggregate with nested line items, invited vendor count, and deadline countdown. |
| **4** | `PUT` | `/rfqs/{rfqId}` | **Update RFQ Metadata**: Updates title, terms, submission deadline. Guard: Rejected if RFQ status is not `DRAFT`. |
| **5** | `DELETE` | `/rfqs/{rfqId}` | **Cancel / Delete Draft RFQ**: Soft-deletes RFQ. Guard: Forbidden if bids are already received. |
| **6** | `POST` | `/rfqs/{rfqId}/items` | **Add Line Item**: Adds item code, technical spec, required quantity, target unit price, and delivery location. |
| **7** | `GET` | `/rfqs/{rfqId}/items` | **List RFQ Line Items**: Fetches all bill-of-materials line items under this RFQ. |
| **8** | `PUT` | `/rfqs/{rfqId}/items/{itemId}` | **Update Line Item**: Modifies quantity or specifications. Guard: Blocked if RFQ is already published. |
| **9** | `DELETE` | `/rfqs/{rfqId}/items/{itemId}` | **Remove Line Item**: Removes line item from RFQ before publishing. |
| **10** | `POST` | `/rfqs/{rfqId}/vendors/invite` | **Invite Vendor Pool**: Takes `List<vendorId>`, verifies vendor KYC status, creates `RFQVendor` records, and sends email notifications. |
| **11** | `GET` | `/rfqs/{rfqId}/vendors` | **List Invited Vendors**: Returns invited vendors with acknowledgment status (`INVITED`, `VIEWED`, `SUBMITTED`, `DECLINED`). |
| **12** | `DELETE` | `/rfqs/{rfqId}/vendors/{vendorId}` | **Revoke Vendor Invitation**: Removes unsubmitted vendor from the sourcing event. |
| **13** | `POST` | `/rfqs/{rfqId}/publish` | **Publish RFQ**: Transitions status from `DRAFT` ➡️ `OPEN`. Validates: Must have at least 1 line item and 1 invited vendor. |
| **14** | `POST` | `/quotations/items/{itemId}` | **Submit Line Item Bid**: Vendor submits `pricePerUnit`, lead time, warranty terms. Auto-calculates `totalPrice = pricePerUnit * qty`. |
| **15** | `PUT` | `/quotations/{quotationId}` | **Revise Submitted Quote**: Allows vendor to submit revised pricing before deadline. Creates audit log of price change. |
| **16** | `GET` | `/rfqs/{rfqId}/comparison-matrix` | **Buyer Bid Comparative Sheet**: Generates normalized side-by-side pricing matrix comparing all vendor quotes across all items. |
| **17** | `POST` | `/quotations/{quotationId}/finalize` | **Award Winning Quote**: Awards specific line item to vendor, transitions RFQ to `AWARDED`, and triggers PO Generation. |

---

## 🚚 Module 2: Purchase Order, Milestone Delivery & Goods Receipt (GRN) (Endpoints 18 – 33)

Once quotes are finalized, this module turns awards into legally binding Purchase Orders, manages vendor acknowledgments, advance shipping notices (ASN), warehouse delivery receipts, and quality inspection.

```
Base Path: /api/v1
```

| # | HTTP Method | Endpoint URI | Business Logic & Workflow State Changes |
| :- | :--- | :--- | :--- |
| **18** | `POST` | `/purchase-orders/generate-from-rfq/{rfqId}` | **Auto-Generate PO**: Compiles all `FinalizedQuotations` for this RFQ, freezes unit prices into immutable PO Line Items, assigns `PO-2026-XXXX`. |
| **19** | `GET` | `/purchase-orders` | **List Purchase Orders**: Filter by buyer, vendor, status (`ISSUED`, `ACKNOWLEDGED`, `PARTIALLY_DELIVERED`, `COMPLETED`). |
| **20** | `GET` | `/purchase-orders/{poId}` | **Get PO Header & Items**: Full PO view with billing address, shipping terms, delivery schedule, and line items. |
| **21** | `POST` | `/purchase-orders/{poId}/approve` | **Buyer Manager PO Sign-off**: Internal approval for high-value orders above financial authorization limit. |
| **22** | `POST` | `/purchase-orders/{poId}/issue` | **Issue PO to Vendor**: Transitions PO to `ISSUED`, generates digitally signed PDF contract, and emails vendor. |
| **23** | `POST` | `/purchase-orders/{poId}/acknowledge` | **Vendor Acknowledgment**: Vendor accepts PO delivery timeline and legally commits to fulfillment. |
| **24** | `POST` | `/purchase-orders/{poId}/shipments` | **Submit Advance Shipping Notice (ASN)**: Vendor registers dispatch with carrier name, tracking number, container/box count, and expected ETA. |
| **25** | `GET` | `/purchase-orders/{poId}/shipments` | **Track Order Shipments**: List all active shipments and delivery milestones for this PO. |
| **26** | `POST` | `/shipments/{shipmentId}/deliver` | **Warehouse Gate Entry**: Security/Gate registers physical truck arrival at warehouse dock, status ➡️ `ARRIVED`. |
| **27** | `POST` | `/goods-receipts` | **Generate GRN (Goods Receipt Note)**: Warehouse counts received boxes against PO lines. Generates `GRN-2026-XXXX`. |
| **28** | `GET` | `/goods-receipts/{grnId}` | **View GRN Details**: Shows received quantities vs PO ordered quantities (detects short-shipments). |
| **29** | `POST` | `/goods-receipts/{grnId}/inspections` | **Quality Inspection Record**: QA Team tests received items, records `acceptedQty`, `rejectedQty`, and defect notes. |
| **30** | `POST` | `/goods-receipts/{grnId}/return-rejected` | **Return-to-Vendor (RTV)**: Generates return dispatch for rejected items with debit memo trigger. |
| **31** | `GET` | `/purchase-orders/{poId}/fulfillment-status` | **PO Fulfillment Summary**: Computes total ordered vs total accepted vs pending fulfillment balance. |
| **32** | `POST` | `/purchase-orders/{poId}/amend` | **PO Amendment**: Formal price/quantity revision with version tracking (`PO-XXXX-V2`). |
| **33** | `POST` | `/purchase-orders/{poId}/close` | **Close PO**: Closes fulfilled PO or cancels unfulfilled balance after final delivery. |

---

## 💳 Module 3: Vendor Invoicing, 3-Way Matching, Penalties & Reconciliation (Endpoints 34 – 50)

This module handles vendor billing, automated algorithmic verification against PO & GRN (3-Way Matching), SLA penalty deductions, dispute arbitration, and payment batch disbursement.

```
Base Path: /api/v1
```

| # | HTTP Method | Endpoint URI | Business Logic & Workflow State Changes |
| :- | :--- | :--- | :--- |
| **34** | `POST` | `/invoices` | **Submit Vendor Invoice**: Vendor submits tax invoice (`INV-XXXX`), referencing `poId` and `grnId`, with tax breakdown and bank details. |
| **35** | `GET` | `/invoices` | **Search Invoices**: Filter invoices by status (`SUBMITTED`, `MATCH_PASSED`, `DISPUTED`, `APPROVED_FOR_PAYMENT`, `PAID`). |
| **36** | `GET` | `/invoices/{invoiceId}` | **Get Invoice Details**: Header, itemized tax lines, attached PDF, and match verification history. |
| **37** | `POST` | `/invoices/{invoiceId}/verify-3way-match` | **Run Automated 3-Way Match**: Executes rule engine comparing `PO.unitPrice == Invoice.unitPrice` AND `GRN.acceptedQty >= Invoice.billedQty`. |
| **38** | `GET` | `/invoices/{invoiceId}/match-audit` | **View 3-Way Match Report**: Displays itemized variance analysis (price variance, quantity variance, tax discrepancy). |
| **39** | `POST` | `/invoices/{invoiceId}/apply-penalties` | **Calculate SLA Delay Penalties**: If `GRN.deliveryDate > PO.promisedDate`, deducts 0.5% per day of delay from invoice payable amount. |
| **40** | `POST` | `/invoices/{invoiceId}/dispute` | **Raise Invoice Dispute**: If price or quantity mismatches exceed tolerance, flags invoice and notifies vendor with dispute reasons. |
| **41** | `POST` | `/invoices/{invoiceId}/resolve-dispute` | **Resolve Dispute**: Finance officer and vendor agree on credit note adjustment or price correction. |
| **42** | `POST` | `/invoices/{invoiceId}/credit-notes` | **Issue Credit Note**: Registers vendor credit memo for returned/damaged items reducing invoice gross payable. |
| **43** | `POST` | `/invoices/{invoiceId}/approve` | **Finance Final Approval**: Approves 3-Way verified invoice and queues it for the next Net-30 payment run. |
| **44** | `POST` | `/payments/batches` | **Create Payment Batch**: Groups all approved invoices due in the current payment cycle into a unified disbursement batch. |
| **45** | `GET` | `/payments/batches/{batchId}` | **View Payment Batch**: Itemized summary of total payable, vendor bank routing details, and cash requirement. |
| **46** | `POST` | `/payments/batches/{batchId}/execute` | **Execute Bank Wire / Disbursement**: Connects to payment gateway/bank API, registers transfer reference (`TXN-XXXXXX`). |
| **47** | `POST` | `/payments/webhook/bank-notification` | **Payment Webhook**: Handles bank callback. On success: Marks invoices as `PAID`, updates PO payment ledger. |
| **48** | `GET` | `/vendors/{vendorId}/account-statement` | **Vendor Financial Ledger**: Complete transaction history showing total billed, total paid, pending balances, and debit memos. |
| **49** | `POST` | `/vendors/{vendorId}/hold-payments` | **Finance Vendor Freeze**: Puts freeze on all disbursements to a vendor during legal audit or quality breach. |
| **50** | `GET` | `/reports/procure-to-pay-analytics` | **Executive P2P Analytics**: Full funnel metrics: Average RFQ cycle time, PO fulfillment rate, 3-Way match pass rate, total spend. |

---

# 💻 Part 3: Step-by-Step Code Architecture for the Deepest Workflows

Let us look at how the code is structured in the service layer for the most complex transactions:

---

### Step 1: The Awarding & PO Auto-Generation Workflow
When a buyer finalizes an awarded quotation (`Endpoint 17`), the system must freeze the negotiation state and construct an enforceable Purchase Order (`Endpoint 18`).

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PurchaseOrderGenerationServiceImpl implements PurchaseOrderService {

    private final RfqRepository rfqRepository;
    private final FinalizedQuotationRepository finalizedQuotationRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public PurchaseOrderResponse generatePOFromRFQ(Long rfqId) {
        // 1. Guard Clause: Verify RFQ state
        Rfq rfq = rfqRepository.findById(rfqId)
            .orElseThrow(() -> new ResourceNotFoundException("RFQ not found: " + rfqId));

        if (rfq.getStatus() != Status.AWARDED) {
            throw new BusinessValidationException("Cannot generate PO. RFQ must be in AWARDED status first.");
        }

        // 2. Fetch all winning finalized quotes for this RFQ
        List<FinalizedQuotation> winningQuotes = finalizedQuotationRepository.findByRfq_RfqId(rfqId);
        if (winningQuotes.isEmpty()) {
            throw new BusinessValidationException("No finalized winning quotations found for RFQ: " + rfqId);
        }

        // Group winning quotes by vendor (one RFQ may have different vendors winning different items)
        Map<User, List<FinalizedQuotation>> quotesByVendor = winningQuotes.stream()
            .collect(Collectors.groupingBy(fq -> fq.getQuotation().getVendor()));

        List<PurchaseOrder> generatedPOs = new ArrayList<>();

        for (Map.Entry<User, List<FinalizedQuotation>> entry : quotesByVendor.entrySet()) {
            User vendor = entry.getKey();
            List<FinalizedQuotation> vendorQuotes = entry.getValue();

            PurchaseOrder po = new PurchaseOrder();
            po.setPoNumber("PO-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
            po.setRfq(rfq);
            po.setVendor(vendor);
            po.setStatus(PoStatus.DRAFT);
            po.setIssuedDate(LocalDate.now());
            
            BigDecimal grandTotal = BigDecimal.ZERO;

            for (FinalizedQuotation fq : vendorQuotes) {
                RFQQuotation quote = fq.getQuotation();
                RfqItem rfqItem = quote.getRfqItem();

                PurchaseOrderItem poItem = new PurchaseOrderItem();
                poItem.setPurchaseOrder(po);
                poItem.setItemCode(rfqItem.getItemCode());
                poItem.setDescription(rfqItem.getDescription());
                poItem.setOrderedQty(rfqItem.getRequestQty());
                poItem.setUnitOfMeasurement(rfqItem.getUnitOfMeasurement());
                
                // CRITICAL: Freeze the agreed price into the PO Item permanently
                poItem.setAgreedUnitPrice(quote.getPricePerUnit());
                poItem.setLineTotal(quote.getPricePerUnit().multiply(BigDecimal.valueOf(rfqItem.getRequestQty())));
                poItem.setPromisedDeliveryDate(quote.getPromisedDeliveryDate());

                grandTotal = grandTotal.add(poItem.getLineTotal());
                po.getItems().add(poItem);
            }

            po.setTotalAmount(grandTotal);
            generatedPOs.add(purchaseOrderRepository.save(po));
            log.info("Generated Purchase Order {} for Vendor {}", po.getPoNumber(), vendor.getEmail());
        }

        return mapToDto(generatedPOs.get(0));
    }
}
```

---

### Step 2: The Core 3-Way Match Verification Engine (`Endpoint 37`)
This is the heart of enterprise finance software. An invoice cannot be paid simply because it was sent. The software mathematically reconciles:
1. **Contract Price Check**: `Invoice.UnitPrice == PurchaseOrder.UnitPrice`
2. **Physical Receipt Check**: `Invoice.BilledQty <= GRN.AcceptedQty`
3. **Double-Billing Prevention**: `CumulativeBilledQtyAcrossInvoices <= PO.OrderedQty`

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ThreeWayMatchingServiceImpl implements ThreeWayMatchingService {

    private final VendorInvoiceRepository invoiceRepository;
    private final GoodsReceiptRepository grnRepository;
    private final PurchaseOrderRepository poRepository;
    private final ThreeWayMatchAuditRepository matchAuditRepository;

    private static final BigDecimal TOLERANCE_PERCENTAGE = new BigDecimal("0.02"); // 2% financial variance allowed

    @Override
    public ThreeWayMatchResult verifyInvoice(Long invoiceId) {
        VendorInvoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + invoiceId));

        PurchaseOrder po = invoice.getPurchaseOrder();
        GoodsReceiptNote grn = invoice.getGoodsReceipt();

        boolean priceMatchPassed = true;
        boolean quantityMatchPassed = true;
        List<String> mismatchReasons = new ArrayList<>();

        for (InvoiceLineItem invItem : invoice.getItems()) {
            // Find corresponding PO Item
            PurchaseOrderItem poItem = po.getItems().stream()
                .filter(p -> p.getItemCode().equals(invItem.getItemCode()))
                .findFirst()
                .orElseThrow(() -> new BusinessValidationException("Item on invoice not found in PO: " + invItem.getItemCode()));

            // Find corresponding GRN Quality-Accepted Item
            GrnLineItem grnItem = grn.getItems().stream()
                .filter(g -> g.getItemCode().equals(invItem.getItemCode()))
                .findFirst()
                .orElseThrow(() -> new BusinessValidationException("Item on invoice not found in GRN: " + invItem.getItemCode()));

            // 1. PRICE CHECK
            BigDecimal priceDiff = invItem.getUnitPrice().subtract(poItem.getAgreedUnitPrice()).abs();
            BigDecimal allowedPriceVariance = poItem.getAgreedUnitPrice().multiply(TOLERANCE_PERCENTAGE);

            if (priceDiff.compareTo(allowedPriceVariance) > 0) {
                priceMatchPassed = false;
                mismatchReasons.add(String.format("Price mismatch on %s: Invoiced $%.2f vs Agreed PO $%.2f",
                    invItem.getItemCode(), invItem.getUnitPrice(), poItem.getAgreedUnitPrice()));
            }

            // 2. QUANTITY CHECK (Cannot invoice more than quality-accepted goods in warehouse)
            if (invItem.getBilledQty() > grnItem.getQualityAcceptedQty()) {
                quantityMatchPassed = false;
                mismatchReasons.add(String.format("Quantity mismatch on %s: Invoiced %d units vs Accepted GRN %d units",
                    invItem.getItemCode(), invItem.getBilledQty(), grnItem.getQualityAcceptedQty()));
            }
        }

        // 3. Persist Match Audit Trail
        ThreeWayMatchAudit audit = new ThreeWayMatchAudit();
        audit.setInvoice(invoice);
        audit.setExecutedAt(Instant.now());
        audit.setPriceMatchPassed(priceMatchPassed);
        audit.setQuantityMatchPassed(quantityMatchPassed);
        audit.setMismatchDetails(String.join("; ", mismatchReasons));

        if (priceMatchPassed && quantityMatchPassed) {
            audit.setOverallStatus(MatchStatus.PASSED);
            invoice.setStatus(InvoiceStatus.APPROVED_FOR_PAYMENT);
            log.info("3-Way Match PASSED for Invoice {}", invoice.getInvoiceNumber());
        } else {
            audit.setOverallStatus(MatchStatus.FAILED);
            invoice.setStatus(InvoiceStatus.DISPUTED);
            log.warn("3-Way Match FAILED for Invoice {}: {}", invoice.getInvoiceNumber(), mismatchReasons);
        }

        matchAuditRepository.save(audit);
        invoiceRepository.save(invoice);

        return new ThreeWayMatchResult(audit.getOverallStatus(), mismatchReasons);
    }
}
```

---

# 🧠 Part 4: The 7 Rules of Enterprise Production Architecture

When building standalone enterprise systems by yourself, always apply these 7 production rules:

### 1. The `@Transactional` Safety Net
Any service method that updates more than 1 database table (e.g. converting RFQ quotes to Purchase Orders, or approving an Invoice and queueing a payment) **must have `@Transactional`**. If step 3 fails, steps 1 and 2 will automatically roll back, preventing corrupted half-saved data.

### 2. Guard Clauses First, Business Logic Second
Always validate permissions and prerequisites at the very top of your service methods:
```java
// ❌ BAD: Nested ifs deep in the method
// ✅ GOOD: Guard clauses that fail fast
if (rfq.getStatus() != Status.OPEN) {
    throw new IllegalStateException("RFQ is not in OPEN status");
}
if (rfq.getExpireDate().isBefore(LocalDate.now())) {
    throw new SourcingExpiredException("Deadline for this RFQ has already passed");
}
```

### 3. Prevent Negative Numbers & Precision Errors with `BigDecimal`
Never use `double` or `float` for money or pricing. Always use `java.math.BigDecimal` with explicit rounding modes (`RoundingMode.HALF_UP`) to prevent floating-point decimal calculation errors.

### 4. Idempotency on Financial Endpoints
On endpoints that execute payments (`POST /payments/batches/{id}/execute`) or submit invoices, require an `Idempotency-Key` header from the client. If the user clicks the button twice or network retries, the server verifies the key and avoids double-charging.

### 5. Never Return Entities from Controllers
Always return DTOs (`PurchaseOrderResponse`, `QuotationResponse`). Returning entities leads to:
- Infinite JSON recursion loops on `@OneToMany` relationships.
- LazyInitializationException when accessing uninitialized collections outside transactions.
- Accidental exposure of internal database fields.

### 6. Event Auditing Over Data Overwriting
In real enterprise applications, you almost never perform a SQL `UPDATE` that wipes out old data.
- When an RFQ quote is revised ➡️ insert a `QuotationRevisionHistory` row.
- When a PO is amended ➡️ create a `POAmendmentLog`.
- When an invoice fails 3-way matching ➡️ store a `ThreeWayMatchAudit` record.

### 7. Soft Deletes Over Hard Deletes
Add an `isDeleted` boolean or `deletedAt` timestamp to business entities instead of calling `repository.delete()`. Financial and legal regulations require audit preservation for 7+ years.

---

# 🏁 Summary: How to Build Big Projects Autonomously

When faced with any large system (Healthcare, FinTech, Logistics, Sourcing):
1. **Draw the Life Cycle Flowchart** from the first user action to the final outcome.
2. **Break it into 3 to 4 Modules** (e.g. Negotiation ➡️ Execution ➡️ Settlement).
3. **Define the Entities and Freeze Boundaries** (Where does negotiation end and legal contract start?).
4. **Write the 50 Endpoints on paper** as standard HTTP actions (`POST /publish`, `POST /acknowledge`, `POST /verify-3way-match`).
5. **Implement with Guard Clauses and State Machines** in Spring Boot.
