# Q129: Handling large data fetching (10,000+ records) without memory spikes?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
An enterprise banking system generated quarterly transaction statements. When an auditor requested 150,000 records via `transactionRepository.findAll()`, the backend threw `java.lang.OutOfMemoryError: Java heap space`. Loading 150,000 managed JPA entities into Hibernate's 1st-level cache consumed 3.5GB of RAM, triggering aggressive Garbage Collection pauses that froze the entire application. 

Implementing **Keyset Pagination (Chunked Streaming)** using Spring Data's `Stream<T>` inside `@Transactional(readOnly = true)`, paired with manual `entityManager.detach()` calls, allowed streaming hundreds of thousands of records in constant **15MB heap memory**.

---

### 2. Concept & Architecture

```
┌────────────────────────── Memory-Safe Streaming Architecture ──────────────────────────┐
│                                                                                        │
│  TRADITIONAL (OOM Hazard):                                                              │
│  [ Database ] ──► (Loads 150,000 Entities) ──► [ Hibernate 1st Level Cache ] ──► OOM!  │
│                                                                                        │
│  STREAMING (Constant Memory Footprint):                                                 │
│  [ Database ]                                                                          │
│       │ (Cursor-based JDBC ResultSet streaming: fetchSize = 500)                       │
│       ▼                                                                                │
│  [ Stream<Transaction> ] ──► Processes 1 record at a time                              │
│       │                                                                                │
│       ├── (1) Write row directly to HTTP Response OutputStream                         │
│       └── (2) entityManager.detach(entity) ──► Immediately purges from 1st-level cache!│
│                                                                                        │
│  * Result: JVM Heap usage remains flat at ~15MB regardless of 10,000 or 1,000,000 rows!│
│                                                                                        │
└────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### 3. Production Code (Spring Boot 3.x)

```java
package com.enterprise.finance.service;

import com.enterprise.finance.entity.Transaction;
import com.enterprise.finance.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionExportService {

    private final TransactionRepository transactionRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public StreamingResponseBody streamTransactionsToCsv() {
        return outputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                 Stream<Transaction> stream = transactionRepository.streamAllByOrderByTimestampDesc()) {

                writer.write("ID,ACCOUNT_NUMBER,AMOUNT,STATUS,TIMESTAMP\n");
                AtomicInteger counter = new AtomicInteger(0);

                stream.forEach(tx -> {
                    try {
                        writer.write(String.format("%d,%s,%s,%s,%s\n",
                            tx.getId(), tx.getAccountNumber(), tx.getAmount(), tx.getStatus(), tx.getTimestamp()));

                        // Clear Hibernate 1st-level cache every 500 rows to prevent heap exhaustion
                        if (counter.incrementAndGet() % 500 == 0) {
                            entityManager.detach(tx);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Error writing CSV chunk", e);
                    }
                });
                writer.flush();
            }
        };
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
When handling streaming downloads of massive datasets:
1. Do not buffer the entire 100MB response into browser memory (which crashes low-end mobile devices).
2. Leverage the native browser `fetch()` API with `response.body.getReader()` to stream chunks directly into an indexed storage buffer or prompt the native file-save stream.

---

### 2. Production Code (Angular 17+ Standalone)

#### Streaming File Download Service (`streaming-download.service.ts`)
```typescript
import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StreamingDownloadService {
  readonly downloadProgressBytes = signal<number>(0);
  readonly isDownloading = signal<boolean>(false);

  async downloadLargeCsvStream(url: string, filename: string): Promise<void> {
    this.isDownloading.set(true);
    this.downloadProgressBytes.set(0);

    const token = localStorage.getItem('auth_token');
    const response = await fetch(url, {
      headers: { Authorization: `Bearer ${token}` }
    });

    if (!response.body) throw new Error('ReadableStream not supported by browser.');

    const reader = response.body.getReader();
    const chunks: Uint8Array[] = [];
    let receivedBytes = 0;

    while (true) {
      const { done, value } = await reader.read();
      if (done) break;

      chunks.push(value);
      receivedBytes += value.length;
      this.downloadProgressBytes.set(receivedBytes);
    }

    // Assemble blob and trigger download
    const blob = new Blob(chunks, { type: 'text/csv' });
    const blobUrl = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = blobUrl;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(blobUrl);

    this.isDownloading.set(false);
  }
}
```
