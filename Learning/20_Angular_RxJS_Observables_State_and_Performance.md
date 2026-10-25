# Chapter 20: RxJS, Observables, State & Performance (Questions 29–35)

This guide covers Questions 29 to 35 of the Angular Interview Series with deep-dive technical logic, interview scripts, 3 distinct examples per question, and real-world project applications.

---

## 📑 Question Index
29. [What is RxJS, and why is it used in Angular?](#question-29-what-is-rxjs-and-why-is-it-used-in-angular)
30. [Explain Observables and Promises. Which one is better in Angular?](#question-30-explain-observables-and-promises-which-one-is-better-in-angular)
31. [What is `BehaviorSubject` in RxJS?](#question-31-what-is-behaviorsubject-in-rxjs)
32. [How do you improve performance in an Angular application?](#question-32-how-do-you-improve-performance-in-an-angular-application)
33. [What is Lazy Loading in Angular, and why is it used?](#question-33-what-is-lazy-loading-in-angular-and-why-is-it-used)
34. [What is Change Detection in Angular?](#question-34-what-is-change-detection-in-angular)
35. [Explain the difference between `OnPush` Change Detection Strategy and Default Change Detection.](#question-35-explain-the-difference-between-onpush-change-detection-strategy-and-default-change-detection)

---

## Question 29: What is RxJS, and why is it used in Angular?

### 1. Previous Knowledge Required
- Reactive Programming paradigms.
- The Observer Pattern and Iterator Pattern.

### 2. Logic & Theory (Deep-Dive)
**RxJS (Reactive Extensions for JavaScript)** is a library for composing asynchronous and event-based programs using observable sequences.
**Why Angular is deeply integrated with RxJS:**
1. **Unified Asynchronous Model**: Everything asynchronous in Angular is an Observable: HTTP requests (`HttpClient`), router events (`Router.events`), form control changes (`FormControl.valueChanges`), and component event emitters (`EventEmitter` extends `Subject`).
2. **Declarative Transformation Pipelines**: RxJS provides over 100 functional operators (`map`, `filter`, `switchMap`, `debounceTime`, `catchError`, `combineLatest`, `forkJoin`) allowing developers to chain complex asynchronous operations without callback hell.
3. **Cancellation & Race Condition Prevention**: Operators like `switchMap` automatically cancel previous pending HTTP requests when new user events arrive, preventing stale responses from overwriting new data.

### 3. How to Answer in an Interview
> *"RxJS is the reactive programming library powering Angular's asynchronous architecture. 
>
> *Angular uses RxJS because it provides a unified, stream-based API for handling HTTP requests, router navigation events, and reactive form value changes. 
>
> *Its core advantage lies in its composable operators: using operators like `switchMap`, `debounceTime`, and `catchError`, we can effortlessly coordinate complex async tasks, prevent race conditions by canceling stale HTTP requests, and handle retries cleanly."*

### 4. Three Practical Examples

#### Example 1: Preventing Race Conditions with `switchMap` (Typeahead Search)
```typescript
@Component({...})
export class ApplicantSearchComponent implements OnInit {
  searchControl = new FormControl('');
  applicants$!: Observable<ApplicantDTO[]>;

  constructor(private bureauService: BureauService) {}

  ngOnInit() {
    this.applicants$ = this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      // switchMap automatically CANCELS any previous in-flight request if user types again!
      switchMap(query => this.bureauService.search(query ?? ''))
    );
  }
}
```

#### Example 2: Parallel Requests with `forkJoin` (Equivalent to `Promise.all`)
Fires multiple independent HTTP requests concurrently and waits for all to complete:
```typescript
@Injectable({ providedIn: 'root' })
export class DashboardLoaderService {
  constructor(private http: HttpClient) {}

  loadDashboardData(customerId: number): Observable<{ profile: Profile; loans: Loan[]; score: Cibil }> {
    return forkJoin({
      profile: this.http.get<Profile>(`/api/v1/customer/${customerId}`),
      loans: this.http.get<Loan[]>(`/api/v1/loans/customer/${customerId}`),
      score: this.http.get<Cibil>(`/api/v1/bureau/score/${customerId}`)
    });
  }
}
```

#### Example 3: Combining Multiple Dynamic Streams with `combineLatest`
Recalculates loan interest whenever either the requested amount or the tenure slider moves:
```typescript
const amount$ = this.amountControl.valueChanges.pipe(startWith(500000));
const tenure$ = this.tenureControl.valueChanges.pipe(startWith(24));

combineLatest([amount$, tenure$]).pipe(
  map(([amount, tenure]) => this.calculateInterest(amount, tenure))
).subscribe(emi => this.projectedEmi = emi);
```

### 5. Real-World Project Application
- In **Loan Origination Dashboards**, `forkJoin` fetches the customer KYC status, bank verification reports, and credit bureau scores simultaneously when an underwriter opens an application file, slashing page loading latency.

---

## Question 30: Explain Observables and Promises. Which one is better in Angular?

### 1. Previous Knowledge Required
- ES6 Promises (`new Promise((resolve, reject) => ...)`).
- RxJS Observables (`new Observable(subscriber => ...)`).

### 2. Logic & Theory (Deep-Dive)

| Feature | Promise (ES6 Native) | Observable (RxJS) |
| :--- | :--- | :--- |
| **Emission Count** | Emits **exactly one single value** (or error), then settles forever. | Can emit **zero, one, or a continuous stream of multiple values** over time. |
| **Execution** | **Eager**: Starts executing immediately upon creation. | **Cold / Lazy**: Does not execute until an observer subscribes (`.subscribe()`). |
| **Cancelability** | **Cannot be cancelled** once initiated. | **Easily cancelled** via `subscription.unsubscribe()`. |
| **Operators** | Limited: `.then()`, `.catch()`, `.finally()`. | Extremely rich: 100+ mathematical, filtering, and transformation operators. |
| **Integration** | Native to browser JavaScript runtime. | Requires RxJS library (integrated by default in Angular). |

#### Which is better in Angular?
**Observables are significantly better for Angular applications.**
Angular's core systems (Router, Forms, HTTP, EventEmitters) are designed around stream semantics. Observables allow canceling obsolete HTTP requests, auto-unsubscribing with `AsyncPipe`, and handling continuous streams like WebSockets or user inputs. However, Promises are still useful when interoperating with browser Web APIs or when an operation is strictly one-and-done and requires no cancellation.

### 3. How to Answer in an Interview
> *"Promises and Observables both handle asynchronous tasks, but with fundamentally different architectures:
> - A Promise is eager, non-cancelable, and handles only a single event.
> - An Observable is lazy, cancelable, and can handle multiple events over time.
>
> *In Angular, Observables are definitively superior. Because Observables are lazy, no network request is made until subscribed to. Their cancelability allows operators like `switchMap` to kill in-flight HTTP requests when users change inputs. Furthermore, Angular's `AsyncPipe` natively manages subscribing and unsubscribing to Observables, completely preventing memory leaks."*

### 4. Three Practical Examples

#### Example 1: Lazy Execution Demonstration
```typescript
// Promise executes IMMEDIATELY upon declaration (Eager)
const myPromise = new Promise(resolve => {
  console.log('Promise executed!'); // Prints immediately, even if .then() is never called
  resolve(42);
});

// Observable executes ONLY when subscribed to (Lazy)
const myObservable = new Observable(subscriber => {
  console.log('Observable executed!'); // Will NOT print until .subscribe() is called
  subscriber.next(42);
  subscriber.complete();
});
```

#### Example 2: Cancellation of Heavy Workload
```typescript
const searchSubscription = http.get('/api/v1/huge-dataset').subscribe(data => {
  this.results = data;
});

// User navigates away before download completes:
// Calling unsubscribe() aborts the underlying XMLHttpRequest / Fetch connection!
searchSubscription.unsubscribe();
```

#### Example 3: Converting Between Promises and Observables
```typescript
import { firstValueFrom, from } from 'rxjs';

// Convert Promise to Observable
const promise = fetch('/api/data');
const observable$ = from(promise);

// Convert Observable to Promise (Modern replacement for toPromise())
async function fetchScore() {
  const score = await firstValueFrom(bureauService.getScore());
  console.log('Resolved score:', score);
}
```

### 5. Real-World Project Application
- When searching bank transactions, if an underwriter types "Acc-101", pauses, then types "Acc-102", Observables cancel the pending HTTP request for 101, avoiding wasted backend DB CPU cycles. Promises cannot cancel in-flight requests.

---

## Question 31: What is `BehaviorSubject` in RxJS?

### 1. Previous Knowledge Required
- `Observable` (Read-only consumer) vs `Subject` (Multicast EventEmitter).

### 2. Logic & Theory (Deep-Dive)

#### The 4 Types of Subjects in RxJS:
1. **`Subject`**: A standard multicast subject. It has **no initial value** and does **not store state**. Late subscribers only receive values emitted *after* they subscribed.
2. **`BehaviorSubject`**: Requires an **initial default value** and **stores the current/latest value**. Any late subscriber immediately receives the current value upon subscribing!
3. **`ReplaySubject(N)`**: Buffers and replays the last $N$ emitted values to new subscribers, regardless of when they subscribe.
4. **`AsyncSubject`**: Emits only the very last value of the execution, and only when `.complete()` is called.

```
Subject:
Time: --------(Value A)--------(Value B)----------------->
Sub 1: -------[Subscribed]-----(Gets B)------------------>
Sub 2: -----------------------------------[Subscribed]---> (Gets Nothing!)

BehaviorSubject(Initial: 'A'):
Time: -(Init: A)---(Value B)----------------------------->
Sub 1: ------------[Subscribed] (Gets B immediately)----->
Sub 2: -----------------------------------[Subscribed]---> (Gets B immediately!)
```

### 3. How to Answer in an Interview
> *"A `BehaviorSubject` is a specialized RxJS Subject that stores the latest value and requires an initial value upon creation.
>
> *When a new subscriber subscribes to a `BehaviorSubject`, it immediately receives the current stored value, whereas a plain `Subject` only delivers future values.
>
> *In Angular, `BehaviorSubject` is the standard tool for building lightweight state management services (such as tracking user authentication status or current loan application details across unrelated components)."*

### 4. Three Practical Examples

#### Example 1: Basic Subject vs BehaviorSubject Comparison
```typescript
// Plain Subject: Misses earlier emissions
const regularSubject = new Subject<string>();
regularSubject.next('Event 1');
regularSubject.subscribe(v => console.log('Subject received:', v)); // Receives NOTHING

// BehaviorSubject: Remembers current value
const behaviorSubject = new BehaviorSubject<string>('Default Initial State');
behaviorSubject.next('Event 1');
behaviorSubject.subscribe(v => console.log('BehaviorSubject received:', v)); // Prints: 'Event 1'
```

#### Example 2: Enterprise Shared State Service (Authentication Store)
```typescript
@Injectable({ providedIn: 'root' })
export class AuthStoreService {
  // 1. Private BehaviorSubject holding current state
  private authStateSubject = new BehaviorSubject<{ isAuthenticated: boolean; user: string | null }>({
    isAuthenticated: false,
    user: null
  });

  // 2. Public read-only Observable exposed to components
  public authState$ = this.authStateSubject.asObservable();

  // 3. Synchronous getter when needed in guards
  public get currentAuthState() {
    return this.authStateSubject.value;
  }

  // 4. State mutation method
  public setLogin(username: string) {
    this.authStateSubject.next({ isAuthenticated: true, user: username });
  }

  public setLogout() {
    this.authStateSubject.next({ isAuthenticated: false, user: null });
  }
}
```

#### Example 3: ReplaySubject for Recent Activity Logs (Buffering 3 Items)
```typescript
@Injectable({ providedIn: 'root' })
export class RecentActionsService {
  // Replays the last 3 user actions to any newly opened modal
  private recentActions$ = new ReplaySubject<string>(3);

  logAction(action: string) {
    this.recentActions$.next(action);
  }

  getAuditTrail(): Observable<string> {
    return this.recentActions$.asObservable();
  }
}
```

### 5. Real-World Project Application
- In **Banking Single Page Applications**, a `BehaviorSubject` holds the active currency context (e.g., `USD`, `INR`, `EUR`). When a user switches currency in the header, all open account widgets across the screen instantly re-render balances in the selected currency.

---

## Question 32: How do you improve performance in an Angular application?

### 1. Previous Knowledge Required
- Bundle size analysis, DOM rendering bottlenecks, and the JavaScript single-threaded event loop.

### 2. Logic & Theory (Deep-Dive)
Enterprise Angular performance optimization spans three areas:
1. **Change Detection Optimization**:
   - Use `ChangeDetectionStrategy.OnPush` across components to eliminate unnecessary tree traversals.
   - Run non-UI long-running tasks outside of Angular using `NgZone.runOutsideAngular()`.
   - Migrate to **Angular Signals** (Angular 16/17+) for granular, signal-level DOM updates without Zone.js.
2. **Build & Bundle Size Optimization**:
   - Enable **AOT Compilation** and **Build Optimizer** (enabled by default in production).
   - Implement **Lazy Loading** for all feature routes using `loadComponent` / `loadChildren`.
   - Set up `PreloadAllModules` or custom predictive preloading strategies.
   - Enforce strict bundle budgets in `angular.json` (e.g., warning at 500kB, error at 1MB).
3. **Runtime DOM Optimization**:
   - Always supply `trackBy` in `*ngFor` loops so Angular recycles existing DOM nodes instead of destroying and re-creating them.
   - Use **CDK Virtual Scrolling** for large datasets (e.g., lists with >100 rows).
   - Leverage **Pure Pipes** to cache expensive calculations in templates.
   - Optimize images using the `NgOptimizedImage` directive (`ngSrc`).

### 3. How to Answer in an Interview
> *"Improving Angular performance involves a comprehensive approach across bundle size, change detection, and DOM rendering:
>
> 1. **Change Detection**: We enforce `ChangeDetectionStrategy.OnPush` and use immutable data patterns, which reduces change-detection cycles by up to 80%. For canvas rendering or heavy timers, we escape the zone using `NgZone.runOutsideAngular()`.
> 2. **Code Splitting**: We lazy-load all feature routes using dynamic `import()` and configure smart preloading strategies.
> 3. **DOM Efficiency**: We always use `trackBy` in `*ngFor` to prevent DOM churn, use CDK Virtual Scrolling for large tables, and leverage pure pipes for calculation memoization.
> 4. In modern Angular 17, we adopt Angular Signals and deferrable views (`@defer`) to lazy-load heavy sub-components on viewport entry."*

### 4. Three Practical Examples

#### Example 1: `NgZone.runOutsideAngular` for Animation / Polling
Prevents polling intervals from triggering unnecessary change detection across the entire component tree:
```typescript
@Component({...})
export class HighFrequencyChartComponent implements OnInit, OnDestroy {
  private ngZone = inject(NgZone);
  private timerId: any;

  ngOnInit() {
    // Run outside Zone.js: Angular will NOT run change detection every 100ms!
    this.ngZone.runOutsideAngular(() => {
      this.timerId = setInterval(() => {
        this.updateCanvasDirectly();
      }, 100);
    });
  }

  ngOnDestroy() {
    clearInterval(this.timerId);
  }
}
```

#### Example 2: Modern Deferrable Views (`@defer` in Angular 17+)
Defers downloading and rendering heavy components until the user scrolls them into the viewport:
```html
<!-- The heavy loan amortisation chart is NOT downloaded in the initial bundle! -->
<!-- It downloads only when scrolled into view -->
@defer (on viewport) {
  <app-heavy-loan-amortisation-chart [loanId]="currentLoan.id" />
} @placeholder {
  <div class="skeleton-loader">Scroll down to view payment chart...</div>
} @loading (minimum 500ms) {
  <p>Loading interactive chart...</p>
}
```

#### Example 3: `NgOptimizedImage` for Instant Web Vitals Boost
```html
<!-- Replaces standard <img src="..."> to prevent Cumulative Layout Shift (CLS) -->
<img ngSrc="/assets/bank-logo.png" width="200" height="50" priority />
```

### 5. Real-World Project Application
- In **High-Frequency Trading Portals**, running WebSocket ticker updates outside Angular with `runOutsideAngular` and using `OnPush` prevented UI micro-stutters and reduced browser CPU utilization from 85% to under 12%.

---

## Question 33: What is Lazy Loading in Angular, and why is it used?

### 1. Previous Knowledge Required
- Single bundle files (`main.js`) vs asynchronous chunk splitting (`features_loan_chunk.js`).
- Dynamic ES modules `import()`.

### 2. Logic & Theory (Deep-Dive)

#### The Problem with Eager Loading:
In an eager-loaded application, Webpack/Esbuild compiles the entire codebase into a single monolithic `main.js` bundle. A customer who only visits the public landing page is forced to download all the administrative dashboards, underwriter portals, and reporting modules upfront, ballooning initial load time.

#### The Lazy Loading Solution:
Lazy Loading breaks the application into smaller physical JavaScript chunks. These chunks are downloaded **on demand** only when the user navigates to that specific route.
- **Benefits**: Slashes initial Time-To-Interactive (TTI), reduces initial download size by up to 70%, and saves user mobile bandwidth.
- **Modern Syntax**: Dynamic `loadComponent` or `loadChildren` with ES module import syntax:
  ```typescript
  loadComponent: () => import('./features/loan/loan.component').then(m => m.LoanComponent)
  ```

### 3. How to Answer in an Interview
> *"Lazy Loading is an architectural pattern where application routes and their associated components are split into separate JavaScript chunks and downloaded on demand only when the user navigates to that route.
>
> *We use lazy loading to minimize the initial bundle size, drastically improving Time-To-Interactive (TTI) and First Contentful Paint (FCP).
>
> *In modern standalone Angular, we configure lazy loading in our routes using `loadComponent` paired with a dynamic `import()` statement. We often pair this with a `PreloadAllModules` strategy so chunks are pre-fetched in the background after the initial screen loads."*

### 4. Three Practical Examples

#### Example 1: Modern Standalone Component Lazy Loading
```typescript
export const routes: Routes = [
  { path: '', component: LandingPageComponent }, // Eagerly loaded
  { 
    path: 'underwriter-portal', 
    // Downloaded ONLY when the user clicks 'underwriter-portal'
    loadComponent: () => import('./underwriting/underwriting-dashboard.component')
      .then(m => m.UnderwritingDashboardComponent)
  }
];
```

#### Example 2: Lazy Loading Child Routes (`loadChildren`)
```typescript
export const appRoutes: Routes = [
  {
    path: 'admin',
    // Lazy loads an entire sub-routing feature tree
    loadChildren: () => import('./admin/admin.routes').then(m => m.ADMIN_ROUTES)
  }
];
```

#### Example 3: Smart Preloading Strategy (`PreloadAllModules`)
Downloads lazy chunks silently in the background while the user is reading the current page:
```typescript
import { provideRouter, withPreloading, PreloadAllModules } from '@angular/router';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(
      routes,
      withPreloading(PreloadAllModules) // Instant navigation on subsequent clicks!
    )
  ]
};
```

### 5. Real-World Project Application
- In **Enterprise Banking Apps**, the core customer onboarding bundle is kept tiny (~180kB) for fast 4G mobile loading, while the heavy reporting engine with PDF generation libraries (~3MB) is lazy-loaded only when managers open the reports tab.

---

## Question 34: What is Change Detection in Angular?

### 1. Previous Knowledge Required
- Browser event loop (DOM events, Timers, XHR/Fetch).
- Zone.js monkey-patching.

### 2. Logic & Theory (Deep-Dive)
**Change Detection** is the fundamental mechanism by which Angular synchronizes the component's internal data state with its HTML template DOM.
**How it works under the hood:**
1. **Zone.js Monkey Patching**: At startup, Zone.js monkey-patches all asynchronous browser APIs (`addEventListener`, `setTimeout`, `setInterval`, `fetch`, `XMLHttpRequest`).
2. **Notification of Async Events**: When an async event finishes (e.g., user clicks a button, a timer fires, or an HTTP response arrives), Zone.js informs Angular that an event occurred.
3. **Change Detection Tree Traversal**: Angular starts at the **Root Component** and traverses downwards through the entire component tree, checking every data binding in every template to see if the current value differs from the previous value.
4. **DOM Updates**: If a value has changed, Angular updates the exact DOM text node or attribute.

```
                  User Clicks Button / HTTP Response Arrives
                                      |
                                      v
                                 [Zone.js]
                        (Intercepts async completion)
                                      |
                                      v
                             [ApplicationRef.tick()]
                                      |
                         [Root Component Checked]
                                /           \
                               v             v
                     [Component A]         [Component B]
                         /     \               /     \
                        v       v             v       v
                     [Child 1] [Child 2]   [Child 3] [Child 4]
             (Checks ALL template expressions from top to bottom)
```

### 3. How to Answer in an Interview
> *"Change Detection is the process by which Angular synchronizes the component class data model with the HTML DOM view.
>
> *Under the hood, it is powered by Zone.js, which monkey-patches all asynchronous browser execution contexts—including click events, timers, and HTTP requests. When any asynchronous event finishes, Zone.js triggers Angular's `ApplicationRef.tick()`.
>
> *Angular then traverses the component tree from top to bottom, evaluates data bindings, compares current values against previous values, and mutates the DOM if differences are detected."*

### 4. Three Practical Examples

#### Example 1: How Asynchronous Execution Triggers Change Detection
```typescript
@Component({
  selector: 'app-auto-timer',
  template: `<p>Time remaining: {{ seconds }}</p>`
})
export class AutoTimerComponent implements OnInit {
  seconds = 60;

  ngOnInit() {
    // Zone.js patches setInterval: Every 1 second, Angular automatically runs change detection
    setInterval(() => {
      this.seconds--; // DOM automatically updates without any manual refresh!
    }, 1000);
  }
}
```

#### Example 2: Manual Change Detection via `ChangeDetectorRef`
```typescript
@Component({...})
export class ManualRefreshComponent {
  private cdr = inject(ChangeDetectorRef);
  status = 'INITIAL';

  runHeavyCalculation() {
    this.status = 'COMPLETED';
    // Explicitly triggers change detection for this component and its children
    this.cdr.detectChanges();
  }
}
```

#### Example 3: Detaching Change Detector for High-Performance Canvas
```typescript
export class CanvasRendererComponent implements OnInit {
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    // Detaches this component from the change detection tree entirely!
    this.cdr.detach();
  }

  onCriticalDataArrived() {
    // Manually run check only when critical milestone is reached
    this.cdr.detectChanges();
  }
}
```

### 5. Real-World Project Application
- Understanding Change Detection allows senior engineers to diagnose and resolve the infamous `ExpressionChangedAfterItHasBeenCheckedError` by ensuring state mutations occur within appropriate lifecycle hooks.

---

## Question 35: Explain the difference between `OnPush` Change Detection Strategy and Default Change Detection.

### 1. Previous Knowledge Required
- Component tree traversal.
- Object Reference Equality (`===`) vs Deep Object Mutation.

### 2. Logic & Theory (Deep-Dive)

| Criterion | `ChangeDetectionStrategy.Default` | `ChangeDetectionStrategy.OnPush` |
| :--- | :--- | :--- |
| **Trigger Rule** | Checks the component on **EVERY async event** anywhere in the application (even if completely unrelated). | Checks the component **ONLY under 4 specific conditions**. |
| **Object Mutation** | Detects internal object mutations (e.g., `user.name = 'New'`). | **Ignores internal object mutations!** Requires a brand-new object reference (`user = { ...user, name: 'New' }`). |
| **Performance** | Slower; scans every component on every click, timer, and keystroke. | Blazing fast; skips entire component subtrees from change detection. |
| **Enterprise Standard**| Default for beginners. | Mandatory standard for enterprise design systems and tables. |

#### The 4 Conditions That Trigger `OnPush` Change Detection:
1. An **`@Input()` reference changes** (detected via shallow reference comparison `prevInput !== currInput`).
2. An **event originated from inside the component or its children** (e.g., a button click inside the component's own template).
3. An **Observable bound via the `| async` pipe emits a new value**.
4. Change detection is **manually requested** via `cdr.markForCheck()` or `cdr.detectChanges()`.

```
                    Default Strategy:
  [Root] ---> Checks A ---> Checks B ---> Checks C ---> Checks D
  (Every single component in the entire app is checked on every event!)

                    OnPush Strategy:
  [Root] ---> Checks A ---> [B: OnPush - Inputs Unchanged: SKIPPED!]
                             |
                             +---> [C: SKIPPED!]
                             +---> [D: SKIPPED!]
  (Entire subtree skipped, saving thousands of CPU cycles!)
```

### 3. How to Answer in an Interview
> *"The difference lies in how aggressively Angular checks a component for updates.
>
> *With `Default` change detection, Angular checks the component on every single asynchronous event occurring anywhere in the application, even if its data hasn't changed.
>
> *With `OnPush`, Angular skips checking the component unless one of four triggers occurs:
> 1. An `@Input()` reference changes (using strict `===` equality).
> 2. An event handler inside the component template fires.
> 3. An observable bound with the `AsyncPipe` emits a new value.
> 4. We manually invoke `ChangeDetectorRef.markForCheck()`.
>
> *Using `OnPush` combined with immutable data patterns dramatically optimizes performance by pruning entire component subtrees from the change detection pass."*

### 4. Three Practical Examples

#### Example 1: The `OnPush` Mutation Trap (Why Immutability Matters)
```typescript
// Child Component with OnPush
@Component({
  selector: 'app-borrower-badge',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<div>Borrower: {{ borrower.name }}</div>`
})
export class BorrowerBadgeComponent {
  @Input() borrower!: { name: string };
}

// In Parent Component:
// ❌ FAILS with OnPush: Mutating property retains the same object memory address!
// Angular sees prevObj === currObj and SKIPS re-rendering!
this.activeBorrower.name = 'New Name';

// ✅ SUCCEEDS with OnPush: Passing a new immutable object reference triggers update!
this.activeBorrower = { ...this.activeBorrower, name: 'New Name' };
```

#### Example 2: Clean `OnPush` with `AsyncPipe`
The `AsyncPipe` calls `markForCheck()` internally whenever the Observable emits:
```typescript
@Component({
  selector: 'app-loan-queue',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul>
      <li *ngFor="let loan of loans$ | async">{{ loan.applicant }}</li>
    </ul>
  `
})
export class LoanQueueComponent {
  loans$ = inject(LoanService).getPendingQueue();
}
```

#### Example 3: Explicit Triggering with `ChangeDetectorRef.markForCheck()`
When receiving data from an external WebSocket outside Angular:
```typescript
@Component({
  selector: 'app-live-telemetry',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<span>Live Rate: {{ liveRate }}</span>`
})
export class LiveTelemetryComponent implements OnInit {
  private cdr = inject(ChangeDetectorRef);
  liveRate = 0;

  ngOnInit() {
    externalSocket.on('rate_update', (newRate: number) => {
      this.liveRate = newRate;
      // Tells Angular to check this OnPush component in the upcoming change detection cycle
      this.cdr.markForCheck();
    });
  }
}
```

### 5. Real-World Project Application
- In **Banking Data Grids** rendering 1,000 active loan records, setting rows to `ChangeDetectionStrategy.OnPush` ensures that editing a loan in Row 1 does not trigger change detection across the remaining 999 rows, slashing rendering latency from 250ms to 4ms.

---

*Continue to [Chapter 21: Lifecycle Hooks, SSR Universal, Web Workers & Security](file:///Users/apple/Workspace/Home/Javascript/Learning/21_Angular_Lifecycle_Universal_Workers_and_Security.md)*
